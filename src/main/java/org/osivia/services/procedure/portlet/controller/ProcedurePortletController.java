package org.osivia.services.procedure.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.procedure.portlet.model.Action;
import org.osivia.services.procedure.portlet.model.AddField;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.Field;
import org.osivia.services.procedure.portlet.model.FilePath;
import org.osivia.services.procedure.portlet.model.Filter;
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.ProcedureObject;
import org.osivia.services.procedure.portlet.model.Step;
import org.osivia.services.procedure.portlet.model.Variable;
import org.osivia.services.procedure.portlet.service.IProcedureService;
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
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

@Controller
@SessionAttributes("form")
@RequestMapping(value = "VIEW")
public class ProcedurePortletController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

    /** create view. */
    private static final String CREATE_VIEW = "editProcedure";

    /** edit view. */
    private static final String EDIT_VIEW = "editStep";

    /** procedure view. */
    private static final String VIEW_PROCEDURE = "viewProcedure";

    /** VIEW_ENDSTEP */
    private static final String VIEW_ENDSTEP = "endStep";

    private static final String VIEW_ACTION = "editAction";

    private static final String LIST_VIEW = "list";


    /** Portlet context. */
    private PortletContext portletContext;
    /** Portlet config. */
    private PortletConfig portletConfig;


    /** procedureService */
    @Autowired
    private IProcedureService procedureService;

    //    @Autowired
    //    private Profil profil;

    public ProcedurePortletController() {
        super();
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
    public String defaultView(RenderRequest request, RenderResponse response) throws PortletException, CMSException {

        if (StringUtils.equals(getAction(request), "adminproc")) {
            return CREATE_VIEW;
        } else if (StringUtils.equals(getAction(request), "adminprocstep")) {
            return EDIT_VIEW;
        } else {
            if (getPath(request) != null) {
                return VIEW_PROCEDURE;
            } else {
                return LIST_VIEW;
            }
        }
    }

    @RenderMapping(params = "action=editProcedure")
    public String editProcedureView(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        return CREATE_VIEW;
    }

    @RenderMapping(params = "action=editStep")
    public String editStepView(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        request.setAttribute("activeTab", request.getParameter("activeTab"));
        request.setAttribute("activeFormTab", request.getParameter("activeFormTab"));
        return EDIT_VIEW;
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

    @RenderMapping(params = "action=endStep")
    public String endStepView(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        return VIEW_ENDSTEP;
    }


    @ModelAttribute(value = "form")
    public Form getForm(PortletRequest request, PortletResponse response, @RequestParam(value = "selectedStep", required = false) String selectedStep)
            throws PortletException {

        Form form;
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        if (StringUtils.isNotEmpty(getPath(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREMODEL.getName())) {
            final ProcedureModel procedureModel = procedureService.retrieveProcedureByWebId(nuxeoController, getPath(request));
            form = new Form(procedureModel);
            if (StringUtils.isNotEmpty(selectedStep)) {
                form.setSelectedStep(selectedStep);
            } else {
                form.setSelectedStep("0");
            }
            if (!StringUtils.equals(getAction(request), "adminproc") && !StringUtils.equals(getAction(request), "adminprocstep")) {
                procedureService.updateVocabulariesWithValues(nuxeoController, form);
            }
        } else if (StringUtils.isNotEmpty(getPath(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREINSTANCE.getName())) {
            final ProcedureInstance procedureInstance = procedureService.retrieveProcedureInstanceByPath(nuxeoController, getPath(request));
            final ProcedureModel procedureModel = procedureService.retrieveProcedureByWebId(nuxeoController, procedureInstance.getProcedureModelWebId());
            form = new Form(procedureModel, procedureInstance);
            procedureService.updateFormWithObjectsValues(nuxeoController, form);
            procedureService.updateVocabulariesWithValues(nuxeoController, form);
        } else {
            form = new Form();
        }
        return form;
    }

    @ModelAttribute(value = "procedureList")
    public List<ProcedureModel> getListProcedureModel(PortletRequest request, PortletResponse response) throws PortletException {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        return procedureService.listProcedures(nuxeoController, getPortalUrlFactory(), getProcedurePath(request));
    }

    @ModelAttribute(value = "addUrl")
    public String getAddUrl(PortletRequest request, PortletResponse response) throws PortletException {
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        String procedurePath = getProcedurePath(request);
        return procedureService.getAddUrl(nuxeoController, getPortalUrlFactory(), procedurePath);
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

        //        final List<Profil> listeProfils = profil.findProfilByFiltre("(&(objectClass=groupOfNames)(cn=*" + filter + "*))");
        final List<Map<String, String>> listeProfils = new ArrayList<Map<String, String>>();
        listeProfils.add(buildProfilEntry("demo-group"));
        listeProfils.add(buildProfilEntry("personnel-tous"));
        listeProfils.add(buildProfilEntry("Administrators"));
        listeProfils.add(buildProfilEntry("SuperAdministrators"));
        listeProfils.add(buildProfilEntry("Metro2020Enseignants"));
        listeProfils.add(buildProfilEntry("Metro2020Inspecteurs"));
        listeProfils.add(buildProfilEntry("Metro2020SEMTCARpersonnels"));
        response.setContentType("application/json");
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getPortletOutputStream(), listeProfils);
        } catch (final IOException e) {
            throw new PortletException(e);
        }
    }

    private Map<String, String> buildProfilEntry(String entryName) {
        Map<String, String> superAdministrators = new HashMap<String, String>(2);
        superAdministrators.put("cn", entryName);
        superAdministrators.put("displayName", entryName);
        return superAdministrators;
    }


    @ResourceMapping(value = "stepSearch")
    public void getSteps(ResourceRequest request, ResourceResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "filter",
    required = false) String filter) throws PortletException {

        final List<Map<String, String>> listeSteps = new ArrayList<Map<String, String>>();

        List<Step> steps = form.getProcedureModel().getSteps();
        for (Step step : steps) {
            if ((filter == null) || (StringUtils.contains(step.getStepName(), filter) && StringUtils.contains(step.getReference(), filter))) {
                Map<String, String> demoGroup = new HashMap<String, String>(2);
                demoGroup.put("id", step.getReference());
                demoGroup.put("text", step.getStepName());
                listeSteps.add(demoGroup);
            }
        }
        Map<String, String> demoGroup = new HashMap<String, String>(2);
        demoGroup.put("id", "endStep");
        demoGroup.put("text", "Terminer la procédure");
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
    required = false) String filter) throws PortletException {

        List<Variable> listeVar = new ArrayList<Variable>();
        if (StringUtils.isNotBlank(filter)) {
            boolean exactMatch = false;
            for (Entry<String, Variable> entryVar : form.getProcedureModel().getVariables().entrySet()) {
                if (StringUtils.equals(entryVar.getValue().getName(), filter)) {
                    listeVar.add(0, entryVar.getValue());
                    exactMatch = true;
                } else if (StringUtils.contains(entryVar.getValue().getName(), filter) || StringUtils.contains(entryVar.getValue().getLabel(), filter)) {
                    listeVar.add(entryVar.getValue());
                }
            }
            if (!exactMatch) {
                listeVar.add(0, new Variable(StringUtils.deleteWhitespace(filter), null, null, null));
            }
        } else {
            listeVar.addAll(form.getProcedureModel().getVariables().values());
        }

        response.setContentType("application/json");
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getPortletOutputStream(), listeVar);
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

    @ActionMapping(value = "editProcedure", params = "launchProcedure")
    public void launchProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        form.setProcedureInstance(new ProcedureInstance());
        form.getProcedureInstance().setProcedureModelWebId(form.getProcedureModel().getCurrentWebId());

        response.setRenderParameter("action", "viewProcedure");
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
    public void proceedProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "actionId") String actionId, SessionStatus sessionStatus) throws PortletException, IOException {

        if (request instanceof MultipartActionRequest) {
            // set the uploaded files in the instance
            final MultipartActionRequest multipartActionRequest = (MultipartActionRequest) request;
            for (final Field field : form.getTheCurrentStep().getFields()) {
                setMultipartFile(field, multipartActionRequest, form);
            }
        }

        try {
            Map<String, String> globalVariablesValues = form.getProcedureInstance().getGlobalVariablesValues();
            if (StringUtils.isNotEmpty(getPath(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREMODEL.getName())) {
                // if there is no instance, start the procedure
                final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
                PortalControllerContext portalControllerContext = nuxeoController.getPortalCtx();
                String currentWebId = form.getProcedureModel().getCurrentWebId();
                String fetchWebId = StringUtils.removeStart(currentWebId, IFormsService.FORMS_WEB_ID_PREFIX);
                nuxeoController.getNuxeoCMSService().getFormsService().start(portalControllerContext, fetchWebId, actionId, globalVariablesValues);
                // redirect to end of step page
                response.setRenderParameter("action", "endStep");
                sessionStatus.setComplete();
            } else if (StringUtils.isNotEmpty(getPath(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREINSTANCE.getName())) {
                // instance already exist
                final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
                PropertyMap taskProperties = form.getProcedureInstance().getTaskDoc();
                PortalControllerContext portalControllerContext = nuxeoController.getPortalCtx();
                nuxeoController.getNuxeoCMSService().getFormsService().proceed(portalControllerContext, taskProperties, actionId, globalVariablesValues);
                // redirect to end of step page
                response.setRenderParameter("action", "endStep");
                sessionStatus.setComplete();
            } else {
                // shouldn't happen
                response.setRenderParameter("action", "viewProcedure");
            }
        } catch (PortalException e) {
            throw new PortletException(e);
        } catch (final FormFilterException e) {
            form.setFilterMessage(e.getMessage());
            request.setAttribute("filterMessage", e.getMessage());
            response.setRenderParameter("action", "viewProcedure");
        }
    }


    @ActionMapping(value = "editProcedure", params = "saveProcedure")
    public void saveProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException, IOException {

        final String path = getPath(request);

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        if (StringUtils.isNotEmpty(path)) {
            // if the procedure exist in database, update it
            addAllFieldsToSet(form);
            addAllFiltersToSet(form);
            procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        } else {
            // if the procedure doesn't exist in database, create it
            final ProcedureModel createdProcedure = procedureService.createProcedure(nuxeoController, form.getProcedureModel(), getProcedurePath(request));
        }
        String redirectUrl = nuxeoController.getPortalUrlFactory().getBackURL(nuxeoController.getPortalCtx(), false);
        response.sendRedirect(redirectUrl);
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editProcedure", params = "deleteProcedure")
    public void deleteProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException, IOException {

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.deleteProcedure(nuxeoController, form.getProcedureModel());

        final String redirectUrl = getPortalUrlFactory().getBackURL(nuxeoController.getPortalCtx(), false);
        response.sendRedirect(redirectUrl);
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editProcedure", params = "addStep")
    public void addStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException, IOException {

        final Integer newIndex = Integer.valueOf(form.getProcedureModel().getSteps().size());
        final String path = getPath(request);

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        if (StringUtils.isNotEmpty(path)) {
            // if the procedure exist in database, update it
            form.getProcedureModel().getSteps().add(new Step(newIndex));
            form.setSelectedStep(String.valueOf(newIndex));
            procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
            response.setRenderParameter("action", "editStep");
        } else {
            // if the procedure doesn't exist in database, create it
            final ProcedureModel createdProcedure = procedureService.createProcedure(nuxeoController, form.getProcedureModel(), getProcedurePath(request));
            createdProcedure.getSteps().add(new Step(newIndex));
            procedureService.updateProcedure(nuxeoController, createdProcedure);

            String redirectUrl = nuxeoController.getLink(createdProcedure.getOriginalDocument(), "adminprocstep").getUrl();
            response.sendRedirect(redirectUrl);
        }
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editProcedure", params = "addObject")
    public void addObject(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException, IOException {

        final String path = getPath(request);

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        if (StringUtils.isNotEmpty(path)) {
            // if the procedure exist in database, update it
            form.getProcedureModel().getProcedureObjects().add(new ProcedureObject());
            procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
            response.setRenderParameter("action", "editProcedure");
        } else {
            // if the procedure doesn't exist in database, create it
            final ProcedureModel createdProcedure = procedureService.createProcedure(nuxeoController, form.getProcedureModel(), getProcedurePath(request));
            createdProcedure.getProcedureObjects().add(new ProcedureObject());
            procedureService.updateProcedure(nuxeoController, createdProcedure);

            String redirectUrl = nuxeoController.getLink(createdProcedure.getOriginalDocument(), "adminproc").getUrl();
            response.sendRedirect(redirectUrl);
        }
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editProcedure", params = "deleteObject")
    public void deleteObject(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "selectedObject",
    required = false) String selectedObject, SessionStatus sessionStatus) throws PortletException, IOException {

        form.getProcedureModel().getProcedureObjects().remove(Integer.valueOf(selectedObject).intValue());
        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editProcedure");
        sessionStatus.setComplete();
    }

    /**
     * @param request
     * @return
     */
    private String getPath(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        final String path = window.getProperty("osivia.services.procedure.webid");
        return path;
    }

    /**
     * @param request
     * @return
     */
    private String getDocType(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        final String path = window.getProperty("osivia.doctype");
        return path;
    }

    /**
     * @param request
     * @return
     */
    private String getAction(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        final String path = window.getProperty("osivia.procedure.admin");
        return path;
    }

    private String getProcedurePath(PortletRequest request) {
        final PortalWindow window = WindowFactory.getWindow(request);
        return window.getProperty(ProcedurePortletAdminController.PROCEDURE_PATH_KEY);
    }

    @ActionMapping(value = "editProcedure", params = "editStep")
    public void editStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, IOException {
        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editStep", params = "cancelStep")
    public void cancelStep(ActionRequest request, ActionResponse response, SessionStatus sessionStatus) {
        response.setRenderParameter("action", "editProcedure");
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editStep", params = "saveStep")
    public void saveStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException {

        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        updateStartingStep(form);

        form.getProcedureModel().getSteps().set(Integer.valueOf(form.getTheSelectedStep().getIndex()), form.getTheSelectedStep());

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editProcedure");
        sessionStatus.setComplete();
    }

    private void addAllFiltersToSet(Form form) {
        for (final Step step : form.getProcedureModel().getSteps()) {
            for (Action action : step.getActions()) {
                Set<Filter> filtersList = new HashSet<Filter>();
                addAllFiltersToSet(filtersList, action.getFilters());
                action.setFiltersList(filtersList);
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

    private void updateStartingStep(Form form) {
        if (!StringUtils.equals(form.getTheSelectedStep().getReference(), form.getTheSelectedStep().getOldReference())) {
            if (StringUtils.equals(form.getProcedureModel().getStartingStep(), form.getTheSelectedStep().getOldReference())) {
                form.getProcedureModel().setStartingStep(form.getTheSelectedStep().getReference());
            }
        }
    }

    @ActionMapping(value = "editStep", params = "deleteStep")
    public void deleteStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException {
        form.getProcedureModel().getSteps().remove(Integer.valueOf(form.getSelectedStep()).intValue());
        form.getProcedureModel().updateStepsIndexes();
        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editProcedure");
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editProcedure", params = "deleteStep")
    public void deleteStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "selectedStep",
    required = false) String selectedStep, SessionStatus sessionStatus) throws PortletException {

        form.getProcedureModel().getSteps().remove(Integer.valueOf(selectedStep).intValue());
        form.getProcedureModel().updateStepsIndexes();
        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editProcedure");
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editProcedure", params = "duplicateStep")
    public void duplicateStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "selectedStep",
    required = false) String selectedStep, SessionStatus sessionStatus) throws PortletException {

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        final Integer newIndex = Integer.valueOf(form.getProcedureModel().getSteps().size());

        final Step copiedStep = form.getProcedureModel().getSteps().get(Integer.valueOf(selectedStep).intValue());
        addAllFieldsToSet(form);
        addAllFiltersToSet(form);
        form.getProcedureModel().getSteps().add(new Step(newIndex, copiedStep));
        form.setSelectedStep(String.valueOf(newIndex));
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editStep");
        sessionStatus.setComplete();
    }


    @ActionMapping(value = "editStep", params = "editField")
    public void editField(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        final String[] path = form.getSelectedField().getPath().split(",");
        final Field editedField = getFieldByPath(form.getTheSelectedStep().getFields(), path);
        if (editedField != null) {
            form.getProcedureModel().getVariables().put(editedField.getName(), new Variable(editedField));
        }
        response.setRenderParameter("activeTab", "form");
        response.setRenderParameter("activeFormTab", "edit");
        response.setRenderParameter("action", "editStep");
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

    @ActionMapping(value = "editStep", params = "addField")
    public void addField(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        final AddField addField = form.getNewField();
        final Field field = new Field(form.getTheSelectedStep().getNextPath(), addField, false);
        form.getProcedureModel().getVariables().put(addField.getVariableName(), new Variable(addField));
        updateProcedureWithForm(request, response, form, field);
    }

    @ActionMapping(value = "editStep", params = "addFieldSet")
    public void addFieldSet(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        final Field field = new Field(form.getTheSelectedStep().getNextPath(), form.getNewFieldSet(), true);
        updateProcedureWithForm(request, response, form, field);
    }

    private void updateProcedureWithForm(ActionRequest request, ActionResponse response, Form form, final Field field) throws PortletException {
        field.setPath(String.valueOf(form.getTheSelectedStep().getFields().size()));
        form.getTheSelectedStep().getFields().add(field);
        form.setNewField(new AddField());
        form.setNewFieldSet(new AddField());
        response.setRenderParameter("activeTab", "form");
        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editStep", params = "updateForm")
    public void updateFormStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        final Map<String, List<Field>> allFieldsMap = new HashMap<String, List<Field>>();
        addAllFields(allFieldsMap, form.getTheSelectedStep().getFields());

        rebuildStep(allFieldsMap, form.getTheSelectedStep());

        response.setRenderParameter("activeTab", "form");
        response.setRenderParameter("action", "editStep");
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
                    final String parentPath = field.getPath().length() > 1 ? StringUtils.substringBeforeLast(field.getPath(), ",") : StringUtils.EMPTY;
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
    public void editAction(final ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form,
            @RequestParam(value = "selectedButton") String index) throws PortletException {

        form.setSelectedAction(index);
        response.setRenderParameter("action", "editAction");
    }

    @ActionMapping(value = "editStep", params = "addButton")
    public void addButton(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        form.getTheSelectedStep().getActions().add(new Action());
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

    @ActionMapping(value = "editStep", params = "selectField")
    public void fillEditFieldTab(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "selectedFieldPath") String selectedFieldPath) {

        Field fieldByFieldPath = getFieldByFieldPath(form.getTheSelectedStep().getFields(), selectedFieldPath);
        form.setSelectedField(fieldByFieldPath);
        response.setRenderParameter("action", "editStep");
        response.setRenderParameter("activeTab", "form");
        response.setRenderParameter("activeFormTab", "edit");
    }

    private Field getFieldByFieldPath(List<Field> fields, String selectedFieldPath) {
        if (fields != null) {
            for (Field field : fields) {
                if (StringUtils.equals(field.getPath(), selectedFieldPath)) {
                    return field;
                }
                Field fieldByPath = getFieldByFieldPath(field.getFields(), selectedFieldPath);
                if (fieldByPath != null) {
                    return fieldByPath;
                }
            }
        }
        return null;
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
    }

    @ActionMapping(value = "editAction", params = "cancelAction")
    public void cancelAction(ActionRequest request, ActionResponse response, SessionStatus sessionStatus) {
        response.setRenderParameter("action", "editStep");
        response.setRenderParameter("activeTab", "action");
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editAction", params = "cancelAction=toStep")
    public void cancelActionToEditStep(ActionRequest request, ActionResponse response, SessionStatus sessionStatus) {
        response.setRenderParameter("action", "editStep");
        sessionStatus.setComplete();
    }

    @ActionMapping(value = "editAction", params = "cancelAction=toProc")
    public void cancelActionToEditProcedure(ActionRequest request, ActionResponse response, SessionStatus sessionStatus) {
        response.setRenderParameter("action", "editProcedure");
        sessionStatus.setComplete();
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
    public void fillEditTab(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form,
            @RequestParam(
                    value = "selectedFilterPath") String selectedFilterPath) {

        Filter filterByFilterPath = getFilterByFilterPath(form.getTheSelectedAction().getFilters(), selectedFilterPath);
        form.setSelectedFilter(filterByFilterPath);
        response.setRenderParameter("action", "editAction");
        response.setRenderParameter("activeTab", "edit");
    }

    private Filter getFilterByFilterPath(List<Filter> filtersList, String selectedFilterPath) {
        if (filtersList != null) {
            for (Filter filter : filtersList) {
                if (StringUtils.equals(filter.getFilterPath(), selectedFilterPath)) {
                    return filter;
                }
                Filter filterbyId = getFilterByFilterPath(filter.getFilters(), selectedFilterPath);
                if (filterbyId != null) {
                    return filterbyId;
                }
            }
        }
        return null;
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
    public void saveAction(final ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, SessionStatus sessionStatus)
            throws PortletException {

        addAllFieldsToSet(form);
        addAllFiltersToSet(form);

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editStep");
        response.setRenderParameter("activeTab", "action");
        sessionStatus.setComplete();
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

    @ActionMapping(value = "editStep", params = "deleteField")
    public void deleteField(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        final String[] path = form.getSelectedField().getPath().split(",");
        removeFieldByPath(form.getTheSelectedStep().getFields(), path);

        response.setRenderParameter("activeTab", "form");
        response.setRenderParameter("action", "editStep");
    }

    private void removeFieldByPath(List<Field> fields, String[] path) {

        final Integer index = Integer.parseInt(path[0]);
        Field nestedField;
        if ((path.length == 1) && (fields != null)) {
            // on a fini de parcourir le path
            final ListIterator<Field> listIterator = fields.listIterator();
            while (listIterator.hasNext()) {
                final Field field = listIterator.next();
                final String[] pathArray = StringUtils.split(field.getPath(), ',');
                if ((pathArray.length > 0) && (Integer.parseInt(pathArray[pathArray.length - 1]) == index)) {
                    listIterator.remove();
                }
            }
        } else {
            // on continue de parcourir le path
            nestedField = fields.get(index);
            path = (String[]) ArrayUtils.remove(path, 0);
            removeFieldByPath(nestedField.getFields(), path);
        }
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
