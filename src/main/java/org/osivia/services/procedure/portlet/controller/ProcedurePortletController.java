package org.osivia.services.procedure.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import net.sf.json.JSONArray;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Group;
import org.osivia.portal.api.directory.v2.service.GroupService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSPublicationInfos;
import org.osivia.services.procedure.portlet.model.Action;
import org.osivia.services.procedure.portlet.model.AddField;
import org.osivia.services.procedure.portlet.model.Column;
import org.osivia.services.procedure.portlet.model.Dashboard;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.Field;
import org.osivia.services.procedure.portlet.model.FilePath;
import org.osivia.services.procedure.portlet.model.Filter;
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.ProcedureObject;
import org.osivia.services.procedure.portlet.model.ProcedureRepository;
import org.osivia.services.procedure.portlet.model.Record;
import org.osivia.services.procedure.portlet.model.Step;
import org.osivia.services.procedure.portlet.model.Variable;
import org.osivia.services.procedure.portlet.model.VariableTypesAllEnum;
import org.osivia.services.procedure.portlet.model.WebIdException;
import org.osivia.services.procedure.portlet.service.IProcedureService;
import org.osivia.services.procedure.portlet.util.VariableTypesEnumJsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;
import org.springframework.web.portlet.multipart.MultipartActionRequest;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

@Controller
@SessionAttributes("form")
@RequestMapping(value = "VIEW")
public class ProcedurePortletController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

    /** logger */
    private static final Log logger = LogFactory.getLog(ProcedurePortletController.class);

    /** create view. */
    private static final String CREATE_VIEW = "editProcedure";
    /** edit view. */
    private static final String EDIT_VIEW = "editStep";
    /** editRecord view */
    private static final String EDIT_RECORD_VIEW = "editRecord";
    /** displayRecord view */
    private static final String DISPLAY_RECORD_VIEW = "displayRecord";
    /** editTdb view */
    private static final String EDIT_TDB = "editTdb";
    /** procedure view. */
    private static final String VIEW_PROCEDURE = "viewProcedure";
    /** endStep view */
    private static final String VIEW_ENDSTEP = "endStep";
    /** action view */
    private static final String VIEW_ACTION = "editAction";
    /** procedure list view */
    private static final String LIST_PROC_VIEW = "list";
    /** procedure detail view */
    private static final String DETAIL_PROC = "detailProc";
    /** manage variables view */
    private static final String MANAGE_VIEW = "manageVariables";
    /** dashboard view */
    private static final String DASHBOARD_VIEW = "procedureDashboard";
    /** VIEW_ERROR */
    private static final String VIEW_ERROR = "error";


    /** Portlet context. */
    private PortletContext portletContext;
    /** Portlet config. */
    private PortletConfig portletConfig;

    /** procedureService */
    @Autowired
    private IProcedureService procedureService;

    /** groupService */
    private GroupService groupService;


    public ProcedurePortletController() {
        super();

        this.groupService = DirServiceFactory.getService(GroupService.class);

    }

    /**
     * Portlet initialization.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(portletConfig);
    }

    /**
     * View page render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view page path
     * @throws PortletException
     * @throws CMSException
     */
    @RenderMapping
    public String defaultView(RenderRequest request, RenderResponse response, @ModelAttribute(value = "form") Form form, @ModelAttribute(
            value = "addProcedureUrl") String addProcedureUrl, @ModelAttribute(value = "procedureList") List<ProcedureModel> procedureModels) throws PortletException, CMSException {

        if (StringUtils.isNotBlank((String) request.getAttribute("errorText"))) {
            request.setAttribute("errorText", request.getAttribute("errorText"));
            return VIEW_ERROR;
        }

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        defaultRenderAction(nuxeoController);
        if (StringUtils.equals(getDocType(request), DocumentTypeEnum.RECORDFOLDER.getDocType())) {
            if (StringUtils.equals(getAction(request), "adminrecord")) {
                // édition procédure type RECORD
                return EDIT_RECORD_VIEW;
            } else if (getWebId(request) != null) {
                // lancement d'une procédure type RECORD
                return VIEW_PROCEDURE;
            } else {
                // liste des procédures du path
                if (StringUtils.isBlank(addProcedureUrl)) {
                    boolean hide = true;
                    if (procedureModels != null) {
                        Iterator<ProcedureModel> procedureModelsI = procedureModels.iterator();
                        while (hide && procedureModelsI.hasNext()) {
                            ProcedureModel procedureModel = procedureModelsI.next();
                            if (StringUtils.isNotBlank(procedureModel.getUrl())) {
                                hide = false;
                            }
                        }
                    }
                    if (hide) {
                        // If the user cannot edit any procedure and can't add, then don't display the portlet
                        request.setAttribute("osivia.emptyResponse", "1");
                    }
                }
                return LIST_PROC_VIEW;
            }
        } else if (StringUtils.equals(getDocType(request), DocumentTypeEnum.RECORD.getDocType())) {
            if (StringUtils.isNotBlank(getAction(request))) {
                // launch procedure
                return VIEW_PROCEDURE;
            } else {
                // visualisation d'un record
                return DISPLAY_RECORD_VIEW;
            }
        } else {
            if (StringUtils.isBlank(getDocType(request)) && StringUtils.isNotBlank(getDashboardPath(request))) {
                return DASHBOARD_VIEW;
            } else if (StringUtils.equals(getAction(request), "adminproc")) {
                validateModel(form, nuxeoController);
                // édition procédure
                return CREATE_VIEW;
            } else if (StringUtils.equals(getAction(request), "adminprocstep")) {
                validateModel(form, nuxeoController);
                // édition procédure, étape
                return EDIT_VIEW;
            } else if (StringUtils.equals(getAction(request), "detailproc")) {
                // affichage du detail d'un élément de RECORD
                return DETAIL_PROC;
            } else if (getWebId(request) != null || (getId(request) != null)) {
                // lancement d'une procédure
                return VIEW_PROCEDURE;
            } else {
                // liste des procédures du path
                if (StringUtils.isBlank(addProcedureUrl)) {
                    boolean hide = true;
                    if (procedureModels != null) {
                        Iterator<ProcedureModel> procedureModelsI = procedureModels.iterator();
                        while (hide && procedureModelsI.hasNext()) {
                            ProcedureModel procedureModel = procedureModelsI.next();
                            if (StringUtils.isNotBlank(procedureModel.getUrl())) {
                                hide = false;
                            }
                        }
                    }
                    if (hide) {
                        // If the user cannot edit any procedure and can't add, then don't display the portlet
                        request.setAttribute("osivia.emptyResponse", "1");
                    }
                }
                return LIST_PROC_VIEW;
            }
        }
    }

    @RenderMapping(params = "action=editProcedure")
    public String editProcedureView(RenderRequest request, RenderResponse response, @ModelAttribute(value = "form") Form form) throws PortletException,
            CMSException {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        validateModel(form, nuxeoController);
        return CREATE_VIEW;
    }

    @RenderMapping(params = "action=editStep")
    public String editStepView(RenderRequest request, RenderResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, CMSException {
        request.setAttribute("activeTab", request.getParameter("activeTab"));
        request.setAttribute("activeFormTab", request.getParameter("activeFormTab"));
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        if (StringUtils.isBlank(request.getParameter("activeTab"))) {
            // valide the model except when refreshing and creating
            validateStep(form, nuxeoController);
        }
        return EDIT_VIEW;
    }

    @RenderMapping(params = "action=editTdb")
    public String editTdbView(RenderRequest request, RenderResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, CMSException {
        return EDIT_TDB;
    }

    @RenderMapping(params = "action=editRecord")
    public String editRecordView(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        request.setAttribute("activeTab", request.getParameter("activeTab"));
        request.setAttribute("activeFormTab", request.getParameter("activeFormTab"));
        return EDIT_RECORD_VIEW;
    }

    @RenderMapping(params = "action=editAction")
    public String endStepView(RenderRequest request, RenderResponse response, @RequestParam(value = "editAction", required = false) String editAction)
            throws PortletException, CMSException {
        request.setAttribute("activeTab", request.getParameter("activeTab"));
        return VIEW_ACTION;
    }

    @RenderMapping(params = "action=viewProcedure")
    public String viewProcedure(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        return VIEW_PROCEDURE;
    }

    @RenderMapping(params = "action=manageVariables")
    public String viewManageVariables(RenderRequest request, RenderResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, CMSException {
        return MANAGE_VIEW;
    }

    @RenderMapping(params = "action=endStep")
    public String endStepView(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // Close current tab URL
        String closeUrl = this.procedureService.getCloseUrl(portalControllerContext);
        request.setAttribute("closeUrl", closeUrl);

        return VIEW_ENDSTEP;
    }

    @RenderMapping(params = "action=error")
    public String errorView(RenderRequest request, RenderResponse response) {
        request.setAttribute("errorText", request.getParameter("errorText"));
        return VIEW_ERROR;
    }

    private void defaultRenderAction(NuxeoController nuxeoController) {
        // Current window
        PortalWindow window = WindowFactory.getWindow(nuxeoController.getRequest());
        // Nuxeo path
        String nuxeoPath = window.getProperty(Constants.WINDOW_PROP_URI);
        String dashboardPath = getDashboardPath(nuxeoController.getRequest());

        if (StringUtils.isNotBlank(nuxeoPath)) {
            // Computed path
            nuxeoPath = nuxeoController.getComputedPath(nuxeoPath);
            // Nuxeo document
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(nuxeoPath);
            Document document = documentContext.getDocument();
            nuxeoController.setCurrentDoc(document);
            nuxeoController.insertContentMenuBarItems();
        } else if (StringUtils.isNotBlank(dashboardPath)) {
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(dashboardPath);
            Document document = documentContext.getDocument();
            nuxeoController.setCurrentDoc(document);
            nuxeoController.insertContentMenuBarItems();
        }
    }


    @ModelAttribute(value = "form")
    public Form getForm(PortletRequest request, PortletResponse response, @RequestParam(value = "selectedStep", required = false) String selectedStep)
            throws PortletException {

        Form form;
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        if (StringUtils.isNotEmpty(getWebId(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREMODEL.getDocType())) {
            final ProcedureModel procedureModel = procedureService.retrieveProcedureByWebId(nuxeoController, getWebId(request));
            form = new Form(procedureModel);
            form.setSelectedStep(StringUtils.defaultIfBlank(selectedStep, "0"));

            if (!StringUtils.equals(getAction(request), "adminproc") && !StringUtils.equals(getAction(request), "adminprocstep")) {
                try {
                    Map<String, String> initVariables = nuxeoController.getNuxeoCMSService().getFormsService()
                            .init(nuxeoController.getPortalCtx(), procedureModel.getOriginalDocument(), null);
                    form.setProcedureInstance(new ProcedureInstance(initVariables));
                } catch (PortalException e) {
                    if (StringUtils.isNotBlank(e.getMessage())) {
                        request.setAttribute("errorText", e.getMessage());
                    } else {
                        throw new PortletException(e);
                    }
                } catch (FormFilterException e) {
                    addNotification(nuxeoController.getPortalCtx(), e.getMessage(), NotificationsType.ERROR);
                }
            }

            if (!StringUtils.equals(getAction(request), "adminproc") && !StringUtils.equals(getAction(request), "adminprocstep")) {
                procedureService.updateData(nuxeoController, form);
            }
        } else if (StringUtils.isNotEmpty(getWebId(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREINSTANCE.getDocType())) {
            // déroulement d'une procédure
            final ProcedureInstance procedureInstance = procedureService.retrieveProcedureInstanceByWebId(nuxeoController, getWebId(request));
            final ProcedureModel procedureModel = procedureService.retrieveProcedureByWebId(nuxeoController, procedureInstance.getProcedureModelWebId());
            form = new Form(procedureModel, procedureInstance);
            procedureService.updateData(nuxeoController, form);
        } else if (StringUtils.isNotEmpty(getId(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.TASKDOC.getDocType())) {
            final ProcedureInstance procedureInstance = procedureService.retrieveProcedureInstanceById(nuxeoController, getId(request));
            final ProcedureModel procedureModel = procedureService.retrieveProcedureByWebId(nuxeoController, procedureInstance.getProcedureModelWebId());
            form = new Form(procedureModel, procedureInstance);

            try {
                Map<String, String> initVariables = nuxeoController.getNuxeoCMSService().getFormsService()
                        .init(nuxeoController.getPortalCtx(), procedureInstance.getOriginalDocument(), null);
                procedureInstance.getGlobalVariablesValues().putAll(initVariables);
            } catch (PortalException e) {
                if (StringUtils.isNotBlank(e.getMessage())) {
                    request.setAttribute("errorText", e.getMessage());
                } else {
                    throw new PortletException(e);
                }
            } catch (FormFilterException e) {
                addNotification(nuxeoController.getPortalCtx(), e.getMessage(), NotificationsType.ERROR);
            }

            procedureService.updateData(nuxeoController, form);
        } else if (StringUtils.isNotEmpty(getWebId(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.RECORDFOLDER.getDocType())) {
            if (StringUtils.equals(getAction(request), "adminrecord")) {
                // édition d'une procédure de type RecordFolder
                final ProcedureModel procedureModel = procedureService.retrieveProcedureByWebId(nuxeoController, getWebId(request));
                form = new Form(procedureModel);
            } else {
                // affichage d'une procédure de type RecordFolder
                final ProcedureModel procedureModel = procedureService.retrieveProcedureByWebId(nuxeoController, getWebId(request));
                form = new Form(procedureModel);
                try {
                    Map<String, String> variables = new HashMap<String, String>();
                    variables.put("pcd:startingStep", getAction(request));
                    Map<String, String> initVariables = nuxeoController.getNuxeoCMSService().getFormsService()
                            .init(nuxeoController.getPortalCtx(), procedureModel.getOriginalDocument(), variables);
                    form.getProcedureModel().setStartingStep(initVariables.get("pcd:startingStep"));
                    form.setProcedureInstance(new ProcedureInstance(initVariables));
                } catch (PortalException e) {
                    if (StringUtils.isNotBlank(e.getMessage())) {
                        request.setAttribute("errorText", e.getMessage());
                    } else {
                        throw new PortletException(e);
                    }
                } catch (FormFilterException e) {
                    addNotification(nuxeoController.getPortalCtx(), e.getMessage(), NotificationsType.ERROR);
                }

                procedureService.updateData(nuxeoController, form);
            }
        } else if (StringUtils.isNotEmpty(getWebId(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.RECORD.getDocType())) {

            Record record = procedureService.retrieveRecordInstanceByWebId(nuxeoController, getWebId(request));
            ProcedureModel procedureModel = procedureService.retrieveProcedureByWebId(nuxeoController, record.getProcedureModelWebId());
            form = new Form(procedureModel, record);
            
            if (StringUtils.isNotBlank(getAction(request))) {
                try {
                    Map<String, String> variables = new HashMap<String, String>();
                    variables.put("pcd:startingStep", getAction(request));
                    variables.put("rcdPath", record.getOriginalDocument().getPath());
                    variables.put("rcdFolderPath", procedureModel.getOriginalDocument().getPath());
                    
                    variables.putAll(record.getGlobalVariablesValues());
                    Map<String, String> initVariables = nuxeoController.getNuxeoCMSService().getFormsService()
                            .init(nuxeoController.getPortalCtx(), procedureModel.getOriginalDocument(), variables);
                    form.getProcedureModel().setStartingStep(initVariables.get("pcd:startingStep"));
                    form.setProcedureInstance(new ProcedureInstance(initVariables));
                } catch (PortalException e) {
                    if (StringUtils.isNotBlank(e.getMessage())) {
                        request.setAttribute("errorText", e.getMessage());
                    } else {
                        throw new PortletException(e);
                    }
                } catch (FormFilterException e) {
                    addNotification(nuxeoController.getPortalCtx(), e.getMessage(), NotificationsType.ERROR);
                }
            }
            procedureService.updateData(nuxeoController, form);
        } else if (StringUtils.isNotBlank(getDashboardPath(request))) {
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(getDashboardPath(request));
            form = new Form(new ProcedureModel(documentContext.getDocument(), nuxeoController));
        } else {
            if (StringUtils.equals(getAction(request), "adminproc")) {
                // création d'une procédure
                form = new Form();
            } else if (StringUtils.equals(getAction(request), "adminrecord")) {
                // création record folder
                form = new Form(ProcedureRepository.recordFolder());
            } else {
                // liste des procédures du path
                form = new Form();
            }
        }
        if (response instanceof RenderResponse && form.getProcedureModel() != null) {
            ((RenderResponse) response).setTitle(form.getProcedureModel().getName());
        }
        return form;
    }

    /**
     * Displays warning(s) if model is missing critical elements
     * 
     * @param form
     * @param nuxeoController
     */
    private void validateModel(Form form, NuxeoController nuxeoController) {
        
        ProcedureModel procedureModel = form.getProcedureModel();
        
        if (procedureModel != null) {
            List<Step> steps = procedureModel.getSteps();
            if(steps == null || steps.size()==0){
                addNotification(nuxeoController.getPortalCtx(), "WARNING_NO_STEPS", NotificationsType.WARNING);
            }else{
                for (Step step : steps) {
                    String stepName = StringUtils.defaultIfBlank(step.getStepName(), step.getReference());
                    if (StringUtils.isNotBlank(stepName)) {
                        List<Action> actions = step.getActions();
                        if (actions == null || actions.size() == 0) {
                            addNotification(nuxeoController.getPortalCtx(), "WARNING_STEP_WITHOUT_ACTION", NotificationsType.WARNING, stepName);
                        } else {
                            for (Action action : actions) {
                                String stepReference = action.getStepReference();
                                if (StringUtils.isBlank(stepReference)) {
                                    addNotification(nuxeoController.getPortalCtx(), "WARNING_ACTION_WITHOUT_STEP", NotificationsType.WARNING,
                                            action.getLabel(), stepName);
                                } else if (!StringUtils.equals(stepReference, "endStep") && procedureModel.getStepsMap().get(stepReference) == null) {
                                    addNotification(nuxeoController.getPortalCtx(), "WARNING_ACTION_WRONG_STEP", NotificationsType.WARNING, action.getLabel(),
                                            stepName);
                                }
                            }
                        }
                    }
                }
            }

            if (StringUtils.isBlank(procedureModel.getStartingStep())) {
                addNotification(nuxeoController.getPortalCtx(), "WARNING_NO_STARTING_STEP", NotificationsType.WARNING);
            } else if (procedureModel.getStepsMap().get(procedureModel.getStartingStep()) == null) {
                addNotification(nuxeoController.getPortalCtx(), "WARNING_WRONG_STARTING_STEP", NotificationsType.WARNING);
            }
        }
    }

    /**
     * Displays warning(s) if step is missing critical elements
     * 
     * @param form
     * @param nuxeoController
     */
    private void validateStep(Form form, NuxeoController nuxeoController) {
        ProcedureModel procedureModel = form.getProcedureModel();
        
        if (procedureModel != null && CollectionUtils.isNotEmpty(procedureModel.getSteps())) {

            List<Action> actions = form.getTheSelectedStep().getActions();
            if (CollectionUtils.isEmpty(actions)) {
                addNotification(nuxeoController.getPortalCtx(), "WARNING_THIS_STEP_WITHOUT_ACTION", NotificationsType.WARNING);
            } else {
                for (Action action : actions) {
                    String stepReference = action.getStepReference();
                    if (StringUtils.isBlank(stepReference)) {
                        addNotification(nuxeoController.getPortalCtx(), "WARNING_THIS_STEP_ACTION_WITHOUT_STEP", NotificationsType.WARNING, action.getLabel());
                    } else if (!StringUtils.equals(stepReference, "endStep") && procedureModel.getStepsMap().get(procedureModel.getStartingStep()) == null) {
                        addNotification(nuxeoController.getPortalCtx(), "WARNING_THIS_STEP_ACTION_WRONG_STEP", NotificationsType.WARNING, action.getLabel());
                    }
                }
            }
        }
    }


    @ModelAttribute(value = "procedureList")
    public List<ProcedureModel> getListProcedureModel(PortletRequest request, PortletResponse response) throws PortletException {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        String procedurePath = getProcedurePath(request);
        return procedurePath != null ? procedureService.listProcedures(nuxeoController, procedurePath) : null;
    }

    @ModelAttribute(value = "editProcedureUrl")
    public String getEditProcedureUrl(PortletRequest request, PortletResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        
        String editProcedureUrl = null;
        if(form.getProcedureModel() != null && form.getProcedureModel().getOriginalDocument() != null){
            try {
                CMSPublicationInfos publicationInfos = NuxeoController.getCMSService().getPublicationInfos(nuxeoController.getCMSCtx(),
                        form.getProcedureModel().getPath());
                if (publicationInfos.isEditableByUser()) {
                    editProcedureUrl = nuxeoController.getLink(form.getProcedureModel().getOriginalDocument(), "adminproc").getUrl();
                }
            } catch (CMSException e) {
                throw new PortletException(e);
            }
        }

        return editProcedureUrl;
    }

    @ModelAttribute(value = "linkProcedureUrl")
    public String getLinkProcedureUrl(PortletRequest request, PortletResponse response, @ModelAttribute(value = "form") Form form) {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        return form.getProcedureModel() != null && form.getProcedureModel().getOriginalDocument() != null ? nuxeoController.getLink(
                form.getProcedureModel().getOriginalDocument()).getUrl() : null;
    }

    @ModelAttribute(value = "addProcedureUrl")
    public String getAddProcedureUrl(PortletRequest request, PortletResponse response) throws PortletException {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        String procedurePath = getProcedurePath(request);
        return procedureService.getAddUrl(nuxeoController, procedurePath, "adminproc", DocumentTypeEnum.PROCEDUREMODEL.getDocType());
    }

    @ModelAttribute(value = "listeFiltres")
    public List<FormFilter> getListeFiltres(PortletRequest request, PortletResponse response) throws PortletException {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        // formsFilters
        Map<String, FormFilter> formsFilters = nuxeoController.getNuxeoCMSService().getCMSCustomizer().getFormsFilters();
        ArrayList<FormFilter> listeFiltres = new ArrayList<FormFilter>(formsFilters.size());
        for (Entry<String, FormFilter> entryFilter : formsFilters.entrySet()) {
            listeFiltres.add(entryFilter.getValue());
        }
        return listeFiltres;
    }

    @ModelAttribute(value = "webIdPrefix")
    public String getWebIdPrefix(PortletRequest request, PortletResponse response) {
        return IFormsService.FORMS_WEB_ID_PREFIX;
    }

    @ResourceMapping(value = "groupSearch")
    public void getProfils(ResourceRequest request, ResourceResponse response, @RequestParam(value = "filter", required = false) String filter)
            throws PortletException {

        Group searchGroup = groupService.getEmptyGroup();
        searchGroup.setCn(filter + "*");
        List<Group> groups = groupService.search(searchGroup);

        final List<Map<String, String>> listeProfils = new ArrayList<Map<String, String>>(groups.size());
        for (Group group : groups) {
            listeProfils.add(buildProfilEntry(group));
        }
        response.setContentType("application/json");
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getPortletOutputStream(), listeProfils);
        } catch (final IOException e) {
            throw new PortletException(e);
        }
    }

    private Map<String, String> buildProfilEntry(Group group) {
        Map<String, String> superAdministrators = new HashMap<String, String>(2);
        superAdministrators.put("cn", group.getCn());
        superAdministrators.put("displayName", group.getCn());
        return superAdministrators;
    }


    @ResourceMapping(value = "stepSearch")
    public void getSteps(ResourceRequest request, ResourceResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "filter",
            required = false) String filter) throws PortletException {

        NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        final List<Map<String, String>> listeSteps = new ArrayList<Map<String, String>>();

        List<Step> steps = form.getProcedureModel().getSteps();
        for (Step step : steps) {
            if ((filter == null) || (StringUtils.contains(step.getStepName(), filter) || StringUtils.contains(step.getReference(), filter))) {
                Map<String, String> demoGroup = new HashMap<String, String>(2);
                demoGroup.put("id", step.getReference());
                demoGroup.put("text", step.getStepName());
                listeSteps.add(demoGroup);
            }
        }
        Map<String, String> demoGroup = new HashMap<String, String>(2);
        demoGroup.put("id", "endStep");
        demoGroup.put("text", getMessage(nuxeoController.getPortalCtx(), "END_STEP"));
        
        listeSteps.add(demoGroup);
        response.setContentType("application/json");
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getPortletOutputStream(), listeSteps);
        } catch (final IOException e) {
            throw new PortletException(e);
        }
    }

    @ResourceMapping(value = "fieldSearch")
    public void getFields(ResourceRequest request, ResourceResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "filter",
            required = false) String filter, @RequestParam(value = "defaultVars", required = false) Boolean defaultVars) throws PortletException {

        List<Variable> listeVar = new ArrayList<Variable>();
        if (StringUtils.isNotBlank(filter)) {
            boolean exactMatch = false;
            for (Entry<String, Variable> entryVar : form.getProcedureModel().getVariables().entrySet()) {
                if (VariableTypesAllEnum.FIELDSET.equals(entryVar.getValue().getType())) {
                    if (StringUtils.equalsIgnoreCase(entryVar.getValue().getName(), filter)) {
                        exactMatch = true;
                        listeVar.add(0, new Variable(buildUniqueVariableName(form.getProcedureModel().getVariables(), filter), null, VariableTypesAllEnum.TEXT,
                                null));
                    }
                } else {
                    if (StringUtils.equalsIgnoreCase(entryVar.getValue().getName(), filter)) {
                        listeVar.add(0, entryVar.getValue());
                        exactMatch = true;
                    } else if (StringUtils.containsIgnoreCase(entryVar.getValue().getName(), filter)
                            || StringUtils.containsIgnoreCase(entryVar.getValue().getLabel(), filter)) {
                        listeVar.add(0, entryVar.getValue());
                    }
                }
            }
            if (!exactMatch && BooleanUtils.isNotTrue(defaultVars)) {
                listeVar.add(0, new Variable(StringUtils.deleteWhitespace(filter), null, VariableTypesAllEnum.TEXT, null));
            }
        } else {
            listeVar.addAll(form.getProcedureModel().getVariables().values());
            if (BooleanUtils.isTrue(defaultVars)) {
                // add default vars
                listeVar.add(Variable.DC_CREATOR);
                listeVar.add(Variable.DC_CREATED);
                listeVar.add(Variable.DC_LAST_CONTRIBUTOR);
                listeVar.add(Variable.DC_MODIFIED);
            }
        }

        response.setContentType("application/json");
        try {
            final ObjectMapper mapper = new ObjectMapper();
            // VariableTypesEnumJsonSerializer pour avoir les bons label
            SimpleModule simpleModule = new SimpleModule("SimpleModule", new Version(1, 0, 0, null));
            Bundle bundle = getBundleFactory().getBundle(request.getLocale());
            simpleModule.addSerializer(VariableTypesAllEnum.class, new VariableTypesEnumJsonSerializer(bundle));
            mapper.registerModule(simpleModule);

            mapper.writeValue(response.getPortletOutputStream(), listeVar);
        } catch (final IOException e) {
            throw new PortletException(e);
        }
    }

    @ResourceMapping(value = "modelSearch")
    public void getModels(ResourceRequest request, ResourceResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "filter",
            required = false) String filter) throws PortletException {

        final NuxeoController nuxeoController = new NuxeoController(request, response, getPortletContext());

        final List<Map<String, String>> listeProcedures = new ArrayList<Map<String, String>>();
        List<ProcedureModel> procedureModels = procedureService.retrieveProcedureModels(nuxeoController, getProcedurePath(request), filter);

        for (ProcedureModel procedureModel : procedureModels) {
            Map<String, String> procedure = new HashMap<String, String>(2);
            procedure.put("id", procedureModel.getCurrentWebId());
            procedure.put("text", procedureModel.getName());
            listeProcedures.add(procedure);
        }

        response.setContentType("application/json");
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getPortletOutputStream(), listeProcedures);
        } catch (final IOException e) {
            throw new PortletException(e);
        }

    }


    @ResourceMapping(value = "vocabularySearch")
    public void getVocabulary(ResourceRequest request, ResourceResponse response, @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "vocabularyName", required = true) String vocabularyName) throws PortletException {

        // Nuxeo controller
        final NuxeoController nuxeoController = new NuxeoController(request, response, getPortletContext());
        nuxeoController.setCacheTimeOut(TimeUnit.HOURS.toMillis(1));
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);

        try {
            final JSONArray values = procedureService.getVocabularyValues(nuxeoController, filter, vocabularyName);
            final PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
            printWriter.write(values.toString());
            printWriter.close();
        } catch (final IOException e) {
            throw new PortletException(e);
        } catch (final PortletException e) {
            throw new PortletException(e);
        }
    }

    @ResourceMapping(value = "formulaireSearch")
    public void getFormulaire(ResourceRequest request, ResourceResponse response, @RequestParam(value = "filter", required = false) String filter)
            throws PortletException {

        final NuxeoController nuxeoController = new NuxeoController(request, response, getPortletContext());

        List<Map<String, String>> results = procedureService.retrieveStepsByName(nuxeoController, filter);

        response.setContentType("application/json");
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getPortletOutputStream(), results);
        } catch (final IOException e) {
            throw new PortletException(e);
        }
    }

    @ActionMapping(value = "editProcedure", params = "changeMode")
    public void changeModeProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) {
        form.setAdvancedMode(!form.isAdvancedMode());
        response.setRenderParameter("action", "editProcedure");
    }

    @ActionMapping(value = "editStep", params = "changeMode")
    public void changeModeStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) {
        form.setAdvancedMode(!form.isAdvancedMode());
        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "actionProcedure", params = "proceedProcedure")
    public void proceedProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form,
            @RequestParam(value = "actionId") String actionId) throws PortletException {

        if (StringUtils.isNotBlank((String) request.getAttribute("errorText"))) {
            response.setRenderParameter("errorText", (String) request.getAttribute("errorText"));
            response.setRenderParameter("action", "error");
            return;
        }

        if (request instanceof MultipartActionRequest) {
            // set the uploaded files in the instance
            final MultipartActionRequest multipartActionRequest = (MultipartActionRequest) request;
            for (final Field field : form.getTheCurrentStep().getFields()) {
                setMultipartFile(field, multipartActionRequest, form);
            }
        }

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        try {
            Map<String, String> globalVariablesValues = form.getProcedureInstance().getGlobalVariablesValues();
            if (StringUtils.isNotEmpty(getWebId(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREMODEL.getDocType())) {
                // if there is no instance, start the procedure
                PortalControllerContext portalControllerContext = nuxeoController.getPortalCtx();
                String currentWebId = form.getProcedureModel().getCurrentWebId();
                globalVariablesValues = nuxeoController.getNuxeoCMSService().getFormsService()
                        .start(portalControllerContext, currentWebId, actionId, globalVariablesValues);
                manageEndStep(nuxeoController, globalVariablesValues, form);
            } else if (StringUtils.isNotEmpty(getWebId(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREINSTANCE.getDocType())) {
                // instance already exist
                PropertyMap taskProperties = form.getProcedureInstance().getTaskDoc();
                PortalControllerContext portalControllerContext = nuxeoController.getPortalCtx();
                globalVariablesValues = nuxeoController.getNuxeoCMSService().getFormsService()
                        .proceed(portalControllerContext, taskProperties, actionId, globalVariablesValues);
                manageEndStep(nuxeoController, globalVariablesValues, form);
            } else if (StringUtils.isNotEmpty(getId(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.TASKDOC.getDocType())) {
                // instance already exist
                PropertyMap taskProperties = form.getProcedureInstance().getTaskDoc();
                PortalControllerContext portalControllerContext = nuxeoController.getPortalCtx();
                globalVariablesValues = nuxeoController.getNuxeoCMSService().getFormsService()
                        .proceed(portalControllerContext, taskProperties, actionId, globalVariablesValues);
                manageEndStep(nuxeoController, globalVariablesValues, form);
            } else if (StringUtils.isNotEmpty(getWebId(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.RECORDFOLDER.getDocType())) {
                PortalControllerContext portalControllerContext = nuxeoController.getPortalCtx();
                String currentWebId = form.getProcedureModel().getCurrentWebId();
                globalVariablesValues = nuxeoController.getNuxeoCMSService().getFormsService()
                        .start(portalControllerContext, currentWebId, actionId, globalVariablesValues);
                manageEndStep(nuxeoController, globalVariablesValues, form);
            } else if (StringUtils.isNotEmpty(getWebId(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.RECORD.getDocType())) {
                PortalControllerContext portalControllerContext = nuxeoController.getPortalCtx();
                String currentWebId = form.getProcedureModel().getCurrentWebId();
                globalVariablesValues = nuxeoController.getNuxeoCMSService().getFormsService()
                        .start(portalControllerContext, currentWebId, actionId, globalVariablesValues);
                manageEndStep(nuxeoController, globalVariablesValues, form);
            } else {
                // shouldn't happen
                response.setRenderParameter("action", "viewProcedure");
            }
        } catch (PortalException e) {
            if (StringUtils.isNotBlank(e.getMessage())) {
                response.setRenderParameter("errorText", e.getMessage());
                response.setRenderParameter("action", "error");
                logger.debug(e.getMessage(), e);
            } else {
                throw new PortletException(e);
            }
        } catch (final FormFilterException e) {
            getNotificationsService().addSimpleNotification(nuxeoController.getPortalCtx(), e.getMessage(), NotificationsType.ERROR);
            request.setAttribute("filterMessage", e.getMessage());
            response.setRenderParameter("action", "viewProcedure");
        }
    }

    private void manageEndStep(NuxeoController nuxeoController, Map<String, String> globalVariablesValues, Form form)
            throws PortletException {
        
        ActionResponse response = (ActionResponse) nuxeoController.getResponse();

        String cmsPath = globalVariablesValues.get(IFormsService.REDIRECT_CMS_PATH_PARAMETER);

        String displayContext = globalVariablesValues.get(IFormsService.REDIRECT_DISPLAYCONTEXT_PARAMETER);

        if (StringUtils.isNotBlank(cmsPath)) {
            // redirect to provided cms page
            String redirectUrl = nuxeoController.getPortalUrlFactory().getCMSUrl(nuxeoController.getPortalCtx(), null, cmsPath, new HashMap<String, String>(),
                    null, displayContext, nuxeoController.getHideMetaDatas(), nuxeoController.getScope(), nuxeoController.getDisplayLiveVersion(), null);
            
            try {
                response.sendRedirect(redirectUrl);
            } catch (IOException e) {
                throw new PortletException(e);
            }

            // notification message set by filter or from model
            String notificationMessage = StringUtils.isNotBlank(globalVariablesValues.get(IFormsService.REDIRECT_MESSAGE_PARAMETER)) ? globalVariablesValues
                    .get(IFormsService.REDIRECT_MESSAGE_PARAMETER) : form.getTheCurrentStep().getStringMsg();

            if (StringUtils.isNotBlank(notificationMessage)) {
                addNotification(nuxeoController.getPortalCtx(), notificationMessage, NotificationsType.SUCCESS);
            }

        }else{
            // redirect to end of step page
            response.setRenderParameter("action", "endStep");

        }
    }


    @ActionMapping(value = "editProcedure", params = "saveProcedure")
    public void saveProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        try {
            if (StringUtils.isNotBlank(form.getProcedureModel().getName())) {
                addAllFieldsToSet(form);
                addAllFiltersToSet(form);
                if (StringUtils.isNotEmpty(form.getProcedureModel().getCurrentWebId())) {
                    // if the procedure exist in database, update it
                    form.setProcedureModel(procedureService.updateProcedure(nuxeoController, form.getProcedureModel()));
                } else {
                    // if the procedure doesn't exist in database, create it
                    form.setProcedureModel(procedureService.createProcedure(nuxeoController, form.getProcedureModel(), getProcedurePath(request)));
                }
                addNotification(nuxeoController.getPortalCtx(), "MODEL_SAVED", NotificationsType.SUCCESS);
                response.setRenderParameter("action", "editProcedure");
            }
        } catch (WebIdException e) {
            addNotification(nuxeoController.getPortalCtx(), "WEBID_ERROR", NotificationsType.ERROR);
        }
    }

    @ActionMapping(value = "editProcedure", params = "exit")
    public void exit(ActionRequest request, ActionResponse response) throws PortletException {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        String redirectUrl = nuxeoController.getPortalUrlFactory().getBackURL(nuxeoController.getPortalCtx(), false);
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            throw new PortletException(e);
        }
    }

    @ActionMapping(value = "editProcedure", params = "deleteProcedure")
    public void deleteProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.deleteProcedure(nuxeoController, form.getProcedureModel());

        final String redirectUrl = getPortalUrlFactory().getBackURL(nuxeoController.getPortalCtx(), false);
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            throw new PortletException(e);
        }

    }

    @ActionMapping(value = "editProcedure", params = "addStep")
    public void addStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {

        final Integer newIndex = Integer.valueOf(form.getProcedureModel().getSteps().size());

        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        form.setEditedStep(new Step(newIndex, form.getProcedureModel().getNewStepReference()));
        form.setSelectedStep(String.valueOf(newIndex));
        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editProcedure", params = "addTdb")
    public void addTdb(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {
        addDashboard(request, response, form);
    }

    private void addDashboard(ActionRequest request, ActionResponse response, Form form) throws PortletException {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        Dashboard newDashboard = new Dashboard();
        newDashboard.setName(getMessage(nuxeoController.getPortalCtx(), "PROCEDURE_DASHBOARD"));
        form.setEditedDashboard(newDashboard);
        form.setSelectedTdb(String.valueOf(form.getProcedureModel().getDashboards().size() + 1));
        response.setRenderParameter("action", "editTdb");
    }

    @ActionMapping(value = "editRecord", params = "addColumn")
    public void addColumnEditRecord(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {
        form.getTheSelectedTdb().getColumns().add(form.getNewColumn());
        form.setNewColumn(new Column());
        response.setRenderParameter("action", "editRecord");
        response.setRenderParameter("activeTab", "dashboard");
    }

    @ActionMapping(value = "editTdb", params = "addColumn")
    public void addColumn(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {
        form.getTheSelectedTdb().getColumns().add(form.getNewColumn());
        form.setNewColumn(new Column());
        response.setRenderParameter("action", "editTdb");
    }

    @ActionMapping(value = "editTdb", params = "addExportVar")
    public void addExportVar(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {
        form.getTheSelectedTdb().getExportVarList().add(form.getNewExportVar());
        form.setNewExportVar(null);
        response.setRenderParameter("action", "editTdb");
    }


    @ActionMapping(value = "editProcedure", params = "manageVariables")
    public void manageVariables(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {

        Map<String, Variable> variables = form.getProcedureModel().getVariables();

        List<Variable> sortedVariables = new ArrayList<>(variables.size());
        for (Entry<String, Variable> entryVar : variables.entrySet()) {
            sortedVariables.add(new Variable(entryVar.getValue()));
        }

        Collections.sort(sortedVariables);

        for (Step step : form.getProcedureModel().getSteps()) {
            for (Field field : step.getFields()) {
                for (Variable variable : sortedVariables) {
                    if (StringUtils.equals(variable.getName(), field.getName())) {
                        List<Field> usedInFields = variable.getUsedInFields().get(step.getStepName());
                        if (usedInFields == null) {
                            usedInFields = new ArrayList<Field>();
                        }
                        usedInFields.add(field);
                        variable.getUsedInFields().put(step.getStepName(), usedInFields);
                    }
                }
            }
        }

        form.setEditedVariables(sortedVariables);

        response.setRenderParameter("action", "manageVariables");
    }

    @ActionMapping(value = "manageVariables", params = "cancel")
    public void cancel(ActionRequest request, ActionResponse response) {
        response.setRenderParameter("action", "editProcedure");
    }

    @ActionMapping(value = "manageVariables", params = "save")
    public void save(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {

        List<Variable> editedVariables = form.getEditedVariables();
        for (Variable variable : editedVariables) {
            form.getProcedureModel().getVariables().put(variable.getName(), variable);
        }

        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        updateStepReferences(form);

        response.setRenderParameter("action", "editProcedure");
    }

    @ActionMapping(value = "manageVariables", params = "deleteVariable")
    public void deleteVariable(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form,
            @RequestParam(value = "selectedVar") String selectedVar) throws PortletException {

        ListIterator<Variable> listIterator = form.getEditedVariables().listIterator();
        while (listIterator.hasNext()) {
            Variable variable = listIterator.next();
            if (StringUtils.equals(selectedVar, variable.getName())) {
                listIterator.remove();
            }
        }

        response.setRenderParameter("action", "manageVariables");
    }

    @ActionMapping(value = "manageVariables", params = "selectVariable")
    public void selectVariable(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "selectedVar",
            required = false) String selectedVar) throws PortletException {

        for (Variable variable : form.getEditedVariables()) {
            if (StringUtils.equals(selectedVar, variable.getName())) {
                form.setSelectedVariable(variable);
            }
        }

        response.setRenderParameter("action", "manageVariables");
    }

    @ActionMapping(value = "manageVariables", params = "saveVariable")
    public void saveVariable(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        Variable selectedVariable = form.getSelectedVariable();
        if (StringUtils.isNotBlank(selectedVariable.getName())) {

            ListIterator<Variable> listIterator = form.getEditedVariables().listIterator();
            while (listIterator.hasNext()) {
                Variable variable = listIterator.next();
                if (StringUtils.equals(selectedVariable.getName(), variable.getName())) {
                    listIterator.remove();
                    listIterator.add(selectedVariable);
                }
            }

            form.setSelectedVariable(null);
        }
        response.setRenderParameter("action", "manageVariables");
    }

    @ActionMapping(value = "editProcedure", params = "addObject")
    public void addObject(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {

        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        form.getProcedureModel().getProcedureObjects().add(new ProcedureObject());
        response.setRenderParameter("action", "editProcedure");
    }

    @ActionMapping(value = "editProcedure", params = "deleteObject")
    public void deleteObject(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "selectedObject",
            required = false) String selectedObject) throws PortletException {

        form.getProcedureModel().getProcedureObjects().remove(Integer.valueOf(selectedObject).intValue());
        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        response.setRenderParameter("action", "editProcedure");
    }

    /**
     * @param request
     * @return
     */
    private String getWebId(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        final String webId = window.getProperty("osivia.services.procedure.webid");
        return webId;
    }

    /**
     * @param request
     * @return
     */
    private String getId(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        final String uuid = window.getProperty("osivia.services.procedure.uuid");
        return uuid;
    }

    /**
     * @param request
     * @return
     */
    private String getDocType(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        final String docType = window.getProperty("osivia.doctype");
        return docType;
    }

    /**
     * @param request
     * @return
     */
    private String getAction(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        final String action = window.getProperty("osivia.procedure.admin");
        return action;
    }

    private String getDashboardPath(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        return window.getPageProperty("osivia.procedure.dashboard.path");
    }

    private String getProcedurePath(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        return window.getProperty(ProcedurePortletAdminController.PROCEDURE_PATH_KEY);
    }

    @ActionMapping(value = "editProcedure", params = "editStep")
    public void editStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        Step step = form.getProcedureModel().getSteps().get(NumberUtils.toInt(form.getSelectedStep()));

        form.setEditedStep(new Step(step));

        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editProcedure", params = "editTdb")
    public void editTdb(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        Dashboard editedDashboard = form.getProcedureModel().getDashboards().get(NumberUtils.toInt(form.getSelectedTdb()));

        form.setEditedDashboard(new Dashboard(editedDashboard));

        response.setRenderParameter("action", "editTdb");
    }

    @ActionMapping(value = "editStep", params = "cancelStep")
    public void cancelStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) {
        response.setRenderParameter("action", "editProcedure");
        form.setSelectedField(null);
        form.setSelectedAction(null);
        form.setSelectedStep(null);
    }

    @ActionMapping(value = "editTdb", params = "cancelTdb")
    public void cancelTdb(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) {
        response.setRenderParameter("action", (String) request.getParameter("cancelTdb"));
    }

    @ActionMapping(value = "editStep", params = "saveStep")
    public void saveStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {

        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        updateStepReferences(form);

        Integer index = Integer.valueOf(form.getTheSelectedStep().getIndex());

        if (form.getProcedureModel().getSteps().size() <= index) {
            form.getProcedureModel().getSteps().add(form.getTheSelectedStep());
        } else {
            form.getProcedureModel().getSteps().set(index, form.getTheSelectedStep());
        }
    }

    @ActionMapping(value = "editTdb", params = "saveTdb")
    public void saveTdb(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {
        addAllFieldsToSet(form);
        addAllFiltersToSet(form);

        Integer index = Integer.valueOf(form.getSelectedTdb());

        if (form.getProcedureModel().getDashboards().size() <= index) {
            form.getProcedureModel().getDashboards().add(form.getTheSelectedTdb());
        } else {
            form.getProcedureModel().getDashboards().set(index, form.getTheSelectedTdb());
        }

        response.setRenderParameter("action", "editProcedure");
    }

    @ActionMapping(value = "editRecord", params = "saveRecord")
    public void saveRecord(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException {
        if (StringUtils.isNotBlank(form.getProcedureModel().getName())) {
            addAllFieldsToSet(form);
            final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
            try {
                if (StringUtils.isNotEmpty(form.getProcedureModel().getCurrentWebId())) {
                    // if the procedure exist in database, update it
                    form.setProcedureModel(procedureService.updateProcedure(nuxeoController, form.getProcedureModel()));
                } else {
                    // if the procedure doesn't exist in database, create it
                    form.setProcedureModel(procedureService.createProcedure(nuxeoController, form.getProcedureModel(), getProcedurePath(request)));
                    String redirectUrl = nuxeoController.getPortalUrlFactory().getBackURL(nuxeoController.getPortalCtx(), false);
                    response.sendRedirect(redirectUrl);
                }
                sessionStatus.setComplete();

                addNotification(nuxeoController.getPortalCtx(), "MODEL_SAVED", NotificationsType.SUCCESS);
            } catch (IOException e) {
                throw new PortletException(e);
            } catch (WebIdException e) {
                addNotification(nuxeoController.getPortalCtx(), "WEBID_ERROR", NotificationsType.ERROR);
            }
        }
    }

    private void addAllFiltersToSet(Form form) {
        for (final Step step : form.getProcedureModel().getSteps()) {
            for (Action action : step.getActions()) {
                Set<Filter> filtersList = new HashSet<Filter>();
                addAllFiltersToSet(filtersList, action.getFilters());
                action.setFiltersList(filtersList);
            }
            if (step.getInitAction() != null) {
                Set<Filter> filtersList = new HashSet<Filter>();
                addAllFiltersToSet(filtersList, step.getInitAction().getFilters());
                step.getInitAction().setFiltersList(filtersList);
            }
        }
    }

    private void addAllFiltersToSet(Set<Filter> filtersList, List<Filter> filters) {
        filtersList.addAll(filters);
        for (final Filter filter : filters) {
            if (filter.getFilters() != null) {
                addAllFiltersToSet(filtersList, filter.getFilters());
            }
        }
    }

    private void addAllFieldsToSet(Form form) {
        for (final Step step : form.getProcedureModel().getSteps()) {
            final Set<Field> fieldsSet = new HashSet<Field>();
            addAllFieldsToSet(fieldsSet, step.getFields());
            step.setFieldsSet(fieldsSet);
        }
    }

    private void addAllFieldsToSet(Set<Field> fieldsSet, List<Field> fields) {
        fieldsSet.addAll(fields);
        for (final Field field : fields) {
            if (field.getFields() != null) {
                addAllFieldsToSet(fieldsSet, field.getFields());
            }
        }
    }

    private void updateStepReferences(Form form) {
        // if stepReference has changed
        if (form.getTheSelectedStep() != null && !StringUtils.equals(form.getTheSelectedStep().getReference(), form.getTheSelectedStep().getOldReference())) {
            
            //update starting step reference if it's the one that has changed
            if (StringUtils.equals(form.getProcedureModel().getStartingStep(), form.getTheSelectedStep().getOldReference())) {
                form.getProcedureModel().setStartingStep(form.getTheSelectedStep().getReference());
            }
            
            // update action's stepreference if it's the one that has changed
            List<Step> steps = form.getProcedureModel().getSteps();
            if(steps!=null){
                for (Step step : steps) {
                    List<Action> actions = step.getActions();
                    if(actions!=null){
                        for (Action action : actions) {
                            if (StringUtils.equals(action.getStepReference(), form.getTheSelectedStep().getOldReference())) {
                                action.setStepReference(form.getTheSelectedStep().getReference());
                            }
                        }
                    }
                }
            }
        }
    }

    @ActionMapping(value = "editStep", params = "deleteStep")
    public void deleteStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {
        form.getProcedureModel().getSteps().remove(Integer.valueOf(form.getSelectedStep()).intValue());
        form.getProcedureModel().updateStepsIndexes();
        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        response.setRenderParameter("action", "editProcedure");
        form.setSelectedStep(null);
    }

    @ActionMapping(value = "editTdb", params = "deleteTdb")
    public void deleteTdb(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {
        form.getProcedureModel().getDashboards().remove(Integer.valueOf(form.getSelectedTdb()).intValue());
        addAllFieldsToSet(form);
        addAllFiltersToSet(form);

        addAllFieldsToSet(form);
        addAllFiltersToSet(form);

        response.setRenderParameter("action", "editProcedure");
        form.setSelectedTdb(null);
    }

    @ActionMapping(value = "editTdb", params = "deleteCol")
    public void deleteCol(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "selectedCol",
            required = false) String selectedCol) throws PortletException {

        form.getTheSelectedTdb().getColumns().remove(Integer.valueOf(selectedCol).intValue());
        response.setRenderParameter("action", "editTdb");
    }

    @ActionMapping(value = "editTdb", params = "deleteExportVar")
    public void deleteExportVar(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "selectedExportVar", required = false) String selectedExportVar) throws PortletException {

        form.getTheSelectedTdb().getExportVarList().remove(Integer.valueOf(selectedExportVar).intValue());
        response.setRenderParameter("action", "editTdb");
    }

    @ActionMapping(value = "editRecord", params = "deleteCol")
    public void deleteColEditRecord(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "selectedCol", required = false) String selectedCol) throws PortletException {

        if (form.getTheSelectedTdb().getColumns().get(Integer.valueOf(selectedCol)).isDeletable()) {
            form.getTheSelectedTdb().getColumns().remove(Integer.valueOf(selectedCol).intValue());
        }
        response.setRenderParameter("action", "editRecord");
        response.setRenderParameter("activeTab", "dashboard");
    }

    private void editField(ActionResponse response, Form form, String action) {
        final String[] path = form.getSelectedField().getPath().split(",");
        final Field editedField = getFieldByPath(form.getTheSelectedStep().getFields(), path);
        if (editedField != null) {
            form.getProcedureModel().getVariables().put(editedField.getName(), new Variable(editedField));
        }
        form.setProcedureInstance(null);
        response.setRenderParameter("activeTab", "form");
        response.setRenderParameter("activeFormTab", "edit");
        response.setRenderParameter("action", action);
    }

    @ActionMapping(value = "editStep", params = "editField")
    public void editFieldStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        editField(response, form, "editStep");
    }

    @ActionMapping(value = "editRecord", params = "editField")
    public void editFieldList(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        editField(response, form, "editRecord");
    }

    private Field getFieldByPath(List<Field> fields, String[] path) {
        final Integer index = Integer.parseInt(path[0]);
        Field nestedField;
        if ((path.length == 1) && (fields != null)) {
            // on a fini de parcourir le path
            final ListIterator<Field> listIterator = fields.listIterator();
            while (listIterator.hasNext()) {
                final Field field = listIterator.next();
                final String[] pathArray = StringUtils.split(field.getPath(), ',');
                if ((pathArray.length > 0) && (Integer.parseInt(pathArray[pathArray.length - 1]) == index)) {
                    return field;
                }
            }
        } else {
            // on continue de parcourir le path
            nestedField = fields.get(index);
            path = (String[]) ArrayUtils.remove(path, 0);
            return getFieldByPath(nestedField.getFields(), path);
        }
        return null;
    }

    private void addField(ActionRequest request, ActionResponse response, Form form, String action, Boolean forceInput) throws PortletException {
        final AddField addField = form.getNewField();
        if (StringUtils.isNotBlank(addField.getVariableName()) || StringUtils.isNotBlank(addField.getLabel())) {
            Map<String, Variable> variables = form.getProcedureModel().getVariables();
            if (StringUtils.isBlank(addField.getVariableName())) {
                addField.setVariableName(buildUniqueVariableName(variables, addField.getLabel()));
            }
            final Field field = new Field(form.getTheSelectedStep().getNextPath(), addField, false);
            variables.put(addField.getVariableName(), new Variable(addField));
            updateProcedureWithForm(request, response, form, field, action, forceInput);
        } else {
            response.setRenderParameter("activeTab", "form");
            response.setRenderParameter("action", action);
        }
    }

    private String buildUniqueVariableName(Map<String, Variable> variables, String label) throws PortletException {
        String cleanLabel;
        try {
            cleanLabel = URLEncoder.encode(StringUtils.deleteWhitespace(label), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new PortletException(e);
        }
        int i = 0;
        String uniqueVarName = StringUtils.isNotBlank(cleanLabel) ? cleanLabel : cleanLabel+i;
        while (variables.containsKey(uniqueVarName)) {
            uniqueVarName = cleanLabel + i;
            i++;
        }
        return uniqueVarName;
    }

    @ActionMapping(value = "editStep", params = "addField")
    public void addFieldInStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        addField(request, response, form, "editStep", null);
    }

    @ActionMapping(value = "editRecord", params = "addField")
    public void addFieldInRecord(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        addField(request, response, form, "editRecord", true);
    }

    @ActionMapping(value = "editStep", params = "addFieldSet")
    public void addFieldSet(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        AddField newFieldSet = form.getNewFieldSet();
        newFieldSet.setType(VariableTypesAllEnum.FIELDSET);
        Map<String, Variable> variables = form.getProcedureModel().getVariables();
        if (StringUtils.isBlank(newFieldSet.getVariableName())) {
            newFieldSet.setVariableName(buildUniqueVariableName(variables, newFieldSet.getLabel()));
        }
        final Field field = new Field(form.getTheSelectedStep().getNextPath(), newFieldSet, true);
        variables.put(newFieldSet.getVariableName(), new Variable(newFieldSet));
        updateProcedureWithForm(request, response, form, field, "editStep");
    }

    private void updateProcedureWithForm(ActionRequest request, ActionResponse response, Form form, final Field field, String action) throws PortletException {
        updateProcedureWithForm(request, response, form, field, action, null);
    }

    private void updateProcedureWithForm(ActionRequest request, ActionResponse response, Form form, final Field field, String action, Boolean input)
            throws PortletException {
        field.setPath(String.valueOf(form.getTheSelectedStep().getFields().size()));
        if (BooleanUtils.isTrue(input)) {
            field.setInput(true);
        }
        form.getTheSelectedStep().getFields().add(field);
        form.setNewField(new AddField());
        form.setNewFieldSet(new AddField());
        response.setRenderParameter("activeTab", "form");
        response.setRenderParameter("action", action);
    }

    private void updateForm(ActionResponse response, Form form, String activeTab, String action) {
        final Map<String, List<Field>> allFieldsMap = new HashMap<String, List<Field>>();


        if (form.getTheSelectedStep() != null) {
            form.setSelectedField(null);
            addAllFields(allFieldsMap, form.getTheSelectedStep().getFields());

            rebuildStep(allFieldsMap, form.getTheSelectedStep());
        }
        if (form.getTheSelectedTdb() != null) {
            Collections.sort(form.getTheSelectedTdb().getColumns());
        }

        response.setRenderParameter("activeTab", activeTab);
        response.setRenderParameter("action", action);
    }

    @ActionMapping(value = "editStep", params = "updateForm")
    public void updateFormStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        updateForm(response, form, "form", "editStep");
    }

    @ActionMapping(value = "editRecord", params = "updateForm")
    public void updateFormRecord(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        updateForm(response, form, "form", "editRecord");
    }

    @ActionMapping(value = "editTdb", params = "updateDashboard")
    public void updateDashboard(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "exportVarList", required = false) String exportVarList) throws PortletException {
        updateForm(response, form, "dashboard", "editTdb");
        if (StringUtils.isNotBlank(exportVarList)) {
            form.getTheSelectedTdb().setExportVarList(Arrays.asList(StringUtils.split(exportVarList, ',')));
        }
    }

    private void rebuildStep(Map<String, List<Field>> allFieldsMap, Step step) {
        final List<Field> baseFields = new ArrayList<Field>();

        final List<Field> fieldList = allFieldsMap.get(StringUtils.EMPTY);
        if (fieldList != null) {
            baseFields.addAll(fieldList);
            Collections.sort(baseFields);
        }
        rebuildFields(allFieldsMap, baseFields);

        step.setFields(baseFields);
    }

    private void rebuildFields(Map<String, List<Field>> allFieldsMap, List<Field> fields) {
        if (fields != null) {
            for (final Field field : fields) {
                field.setFields(allFieldsMap.get(field.getPath()));
                rebuildFields(allFieldsMap, field.getFields());
            }
        }
    }

    private void addAllFields(Map<String, List<Field>> allFieldsMap, List<Field> fields) {
        if (fields != null) {
            for (final Field field : fields) {
                if (field.getPath() != null) {
                    // on ajoute la field dans la map avec le path parent comme clé
                    final String parentPath = StringUtils.split(field.getPath(), ',').length > 1 ? StringUtils.substringBeforeLast(field.getPath(), ",")
                            : StringUtils.EMPTY;
                    List<Field> parentFields = allFieldsMap.get(parentPath);
                    if (parentFields == null) {
                        parentFields = new ArrayList<Field>();
                    }
                    parentFields.add(field);
                    Collections.sort(parentFields);
                    allFieldsMap.put(parentPath, parentFields);
                    addAllFields(allFieldsMap, field.getFields());
                }
            }
        }
    }

    @ActionMapping(value = "editStep", params = "editButton")
    public void editAction(final ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "selectedButton") String index) throws PortletException {

        List<Action> actions = form.getTheSelectedStep().getActions();

        Action action = actions.get(NumberUtils.toInt(index));

        form.setEditedAction(new Action(action));
        form.setSelectedAction(index);

        Map<String, Variable> variables = form.getProcedureModel().getVariables();

        List<Variable> sortedVariables = new ArrayList<>(variables.size());
        for (Entry<String, Variable> entryVar : variables.entrySet()) {
            sortedVariables.add(new Variable(entryVar.getValue()));
        }

        Collections.sort(sortedVariables);
        form.setEditedVariables(sortedVariables);

        response.setRenderParameter("action", "editAction");
    }

    @ActionMapping(value = "editStep", params = "addButton")
    public void addButton(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        form.getTheSelectedStep().addAction();

        response.setRenderParameter("activeTab", "action");
        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editStep", params = "deleteButton")
    public void deleteButton(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form,
            @RequestParam(value = "selectedButton") String index) throws PortletException {
        form.getTheSelectedStep().getActions().remove(Integer.valueOf(index).intValue());
        response.setRenderParameter("activeTab", "action");
        response.setRenderParameter("action", "editStep");
    }

    private void fillEditFieldTab(ActionResponse response, Form form, String selectedFieldPath, String action) {
        Field fieldByFieldPath = getFieldByFieldPath(form.getTheSelectedStep().getFields(), selectedFieldPath);
        form.setSelectedField(fieldByFieldPath);
        response.setRenderParameter("activeTab", "form");
        response.setRenderParameter("activeFormTab", "edit");
        response.setRenderParameter("action", action);
    }

    @ActionMapping(value = "editStep", params = "selectField")
    public void fillEditFieldTabStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "selectedFieldPath") String selectedFieldPath) {
        fillEditFieldTab(response, form, selectedFieldPath, "editStep");
    }

    @ActionMapping(value = "editRecord", params = "selectField")
    public void fillEditFieldTabList(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "selectedFieldPath") String selectedFieldPath) {
        fillEditFieldTab(response, form, selectedFieldPath, "editRecord");
    }

    private Field getFieldByFieldPath(List<Field> fields, String selectedFieldPath) {
        Field returnField = null;
        if (fields != null) {
            for (Field field : fields) {
                if (StringUtils.equals(field.getPath(), selectedFieldPath)) {
                    returnField = field;
                }
                Field fieldByPath = getFieldByFieldPath(field.getFields(), selectedFieldPath);
                if (fieldByPath != null) {
                    returnField = fieldByPath;
                }
            }
        }
        return returnField;
    }

    @ActionMapping(value = "editAction", params = "addFilter")
    public void addFilter(final ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "selectedFilterId") String filterId) throws PortletException {

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        // formsFilters
        Map<String, FormFilter> formsFilters = nuxeoController.getNuxeoCMSService().getCMSCustomizer().getFormsFilters();
        FormFilter selectedFilter = formsFilters.get(filterId);

        int newPath;
        if (form.getTheSelectedAction().getFilters().isEmpty()) {
            newPath = 0;
        } else {
            String lastPath = form.getTheSelectedAction().getFilters().get(form.getTheSelectedAction().getFilters().size() - 1).getFilterPath();
            newPath = Integer.parseInt(lastPath) + 1;
        }
        Filter newFilter = new Filter(selectedFilter, String.valueOf(newPath));
        form.getTheSelectedAction().getFilters().add(newFilter);
        response.setRenderParameter("action", "editAction");
    }

    @ActionMapping(value = "editAction", params = "deleteFilter")
    public void deleteFilter(final ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        if (removeFilterByFilterPath(form.getTheSelectedAction().getFilters(), form.getSelectedFilter().getFilterPath())) {
            updateFiltersPath(form.getTheSelectedAction().getFilters(), StringUtils.EMPTY);
        }
        form.setSelectedFilter(null);
        response.setRenderParameter("action", "editAction");
        response.setRenderParameter("activeTab", "");
    }

    @ActionMapping(value = "editAction", params = "cancelAction")
    public void cancelAction(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) {
        response.setRenderParameter("action", "editStep");
        response.setRenderParameter("activeTab", "action");
    }

    @ActionMapping(value = "editAction", params = "cancelAction=toStep")
    public void cancelActionToEditStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) {
        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editAction", params = "cancelAction=toProc")
    public void cancelActionToEditProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) {
        response.setRenderParameter("action", "editProcedure");
    }

    @ActionMapping(value = "editTdb", params = "cancelAction=toProc")
    public void cancelEditTdbActionToEditProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) {
        response.setRenderParameter("action", "editProcedure");
    }

    private void updateFiltersPath(List<Filter> filters, String currentPath) {
        if (filters != null) {
            for (int i = 0; i < filters.size(); i++) {
                String newPath = currentPath.length() > 0 ? currentPath.concat(",").concat(String.valueOf(i)) : String.valueOf(i);
                filters.get(i).updateFilterPath(newPath);
                updateFiltersPath(filters.get(i).getFilters(), newPath);
            }
        }
    }

    private boolean removeFilterByFilterPath(List<Filter> filters, String filterPath) {
        if (filters != null) {
            ListIterator<Filter> filtersI = filters.listIterator();
            while (filtersI.hasNext()) {
                Filter filter = filtersI.next();
                if (StringUtils.equals(filter.getFilterPath(), filterPath)) {
                    filtersI.remove();
                    return true;
                }
                if (removeFilterByFilterPath(filter.getFilters(), filterPath)) {
                    return true;
                }
            }
        }
        return false;
    }

    @ActionMapping(value = "editAction", params = "editFilter")
    public void editFilter(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) {
        updateFilterByFilterPath(form.getTheSelectedAction().getFilters(), form.getSelectedFilter());
        response.setRenderParameter("action", "editAction");
        response.setRenderParameter("activeTab", "edit");
    }

    @ActionMapping(value = "editAction", params = "selectFilter")
    public void fillEditTab(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "selectedFilterPath") String selectedFilterPath) {

        if ((form.getSelectedFilter() != null) && StringUtils.equals(form.getSelectedFilter().getFilterPath(), selectedFilterPath)) {
            form.setSelectedFilter(null);
            response.setRenderParameter("activeTab", "");
        } else {
            Filter filterByFilterPath = getFilterByFilterPath(form.getTheSelectedAction().getFilters(), selectedFilterPath);
            form.setSelectedFilter(filterByFilterPath);
            response.setRenderParameter("activeTab", "edit");
        }
        response.setRenderParameter("action", "editAction");

    }

    private Filter getFilterByFilterPath(List<Filter> filtersList, String selectedFilterPath) {
        Filter returnFilter = null;
        if (filtersList != null) {
            for (Filter filter : filtersList) {
                if (StringUtils.equals(filter.getFilterPath(), selectedFilterPath)) {
                    returnFilter = filter;
                }
                Filter filterbyId = getFilterByFilterPath(filter.getFilters(), selectedFilterPath);
                if (filterbyId != null) {
                    returnFilter = filterbyId;
                }
            }
        }
        return returnFilter;
    }

    private void updateFilterByFilterPath(List<Filter> filtersList, Filter filterUpdate) {
        if (filtersList != null) {
            for (Filter filter : filtersList) {
                if (StringUtils.equals(filter.getFilterPath(), filterUpdate.getFilterPath())) {
                    filter.updateFilter(filterUpdate);
                    return;
                }
                updateFilterByFilterPath(filter.getFilters(), filterUpdate);
            }
        }
    }

    @ActionMapping(value = "editAction", params = "saveAction")
    public void saveAction(final ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {

        addAllFieldsToSet(form);
        addAllFiltersToSet(form);

        Action theSelectedAction = form.getTheSelectedAction();
        
        Integer index = NumberUtils.toInt(form.getSelectedAction());
        
        if (form.getTheSelectedStep().getActions().size() <= index) {
            form.getTheSelectedStep().getActions().add(theSelectedAction);
        } else {
            form.getTheSelectedStep().getActions().set(index, theSelectedAction);
        }

        response.setRenderParameter("action", "editStep");
        response.setRenderParameter("activeTab", "action");
    }

    @ActionMapping(value = "editAction", params = "updateForm")
    public void updateFormAction(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        final Map<String, List<Filter>> allFiltersMap = new HashMap<String, List<Filter>>();
        addAllFilters(allFiltersMap, form.getTheSelectedAction().getFilters());
        rebuildAction(allFiltersMap, form.getTheSelectedAction());
        form.setSelectedFilter(null);
        response.setRenderParameter("action", "editAction");
    }

    private void addAllFilters(Map<String, List<Filter>> allFiltersMap, List<Filter> filters) {
        if (filters != null) {
            for (final Filter filter : filters) {
                if (filter.getFilterPath() != null) {
                    // on ajoute le filtre dans la map avec le path parent comme clé
                    final String parentPath = filter.getFilterPath().length() > 1 ? StringUtils.substringBeforeLast(filter.getFilterPath(), ",")
                            : StringUtils.EMPTY;
                    List<Filter> parentFilters = allFiltersMap.get(parentPath);
                    if (parentFilters == null) {
                        parentFilters = new ArrayList<Filter>();
                    }
                    filter.setFilterInstanceId(filter.getFilterId().concat(filter.getFilterPath()));
                    parentFilters.add(filter);
                    Collections.sort(parentFilters);
                    allFiltersMap.put(parentPath, parentFilters);
                    addAllFilters(allFiltersMap, filter.getFilters());
                }
            }
        }
    }

    private void rebuildAction(Map<String, List<Filter>> allFiltersMap, Action action) {
        final List<Filter> baseFilters = new ArrayList<Filter>();

        final List<Filter> filtersList = allFiltersMap.get(StringUtils.EMPTY);
        if (filtersList != null) {
            baseFilters.addAll(filtersList);
            Collections.sort(baseFilters);
        }
        rebuildFilters(allFiltersMap, baseFilters);

        action.setFilters(baseFilters);
    }

    private void rebuildFilters(Map<String, List<Filter>> allFiltersMap, List<Filter> baseFilters) {
        if (baseFilters != null) {
            for (final Filter filter : baseFilters) {
                filter.setFilters(allFiltersMap.get(filter.getFilterPath()));
                rebuildFilters(allFiltersMap, filter.getFilters());
            }
        }
    }

    private void deleteField(ActionResponse response, Form form, String action) {
        if (form.getSelectedField().isDeletable()) {
            if (removeFieldsByFieldPath(form.getTheSelectedStep().getFields(), form.getSelectedField().getPath())) {
                updateFieldsPath(form.getTheSelectedStep().getFields(), StringUtils.EMPTY);
            }
            form.setSelectedField(null);
        }
        response.setRenderParameter("activeTab", "form");
        response.setRenderParameter("activeFormTab", "");
        response.setRenderParameter("action", action);
    }

    @ActionMapping(value = "editStep", params = "deleteField")
    public void deleteFieldStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        deleteField(response, form, "editStep");
    }

    @ActionMapping(value = "editRecord", params = "deleteField")
    public void deleteFieldList(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        deleteField(response, form, "editRecord");
    }

    @ActionMapping(value = "editRecord", params = "deleteRecord")
    public void deleteRecord(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form)
            throws PortletException {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.deleteProcedure(nuxeoController, form.getProcedureModel());

        final String redirectUrl = getPortalUrlFactory().getBackURL(nuxeoController.getPortalCtx(), false);
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            throw new PortletException(e);
        }
    }

    private void updateFieldsPath(List<Field> list, String currentPath) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String newPath = currentPath.length() > 0 ? currentPath.concat(",").concat(String.valueOf(i)) : String.valueOf(i);
                list.get(i).setPath(newPath);
                updateFieldsPath(list.get(i).getFields(), newPath);
            }
        }
    }

    private boolean removeFieldsByFieldPath(List<Field> list, String fieldPath) {
        if (list != null) {
            ListIterator<Field> filtersI = list.listIterator();
            while (filtersI.hasNext()) {
                Field field = filtersI.next();
                if (StringUtils.equals(field.getPath(), fieldPath)) {
                    filtersI.remove();
                    return true;
                }
                if (removeFieldsByFieldPath(field.getFields(), fieldPath)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * set the uploaded files in the instance, traversing recursive fields
     *
     * @param field
     * @param multipartActionRequest
     * @param form
     */
    private void setMultipartFile(Field field, MultipartActionRequest multipartActionRequest, Form form) {
        if (!field.isFieldSet()) {
            if (field.isInput()) {
                final MultipartFile multipartFile = multipartActionRequest.getFileMap().get("file:" + field.getName());
                if ((multipartFile != null) && (multipartFile.getSize() > 0)) {
                    final FilePath filePath = new FilePath();
                    filePath.setFile(multipartFile);
                    filePath.setVariableName(field.getName());
                    filePath.setFileName(String.valueOf(multipartFile.getOriginalFilename()));
                    form.getProcedureInstance().getFilesPath().put(filePath.getVariableName(), filePath);
                }
            }
        } else {
            for (final Field nestedField : field.getFields()) {
                setMultipartFile(nestedField, multipartActionRequest, form);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

}
