package org.osivia.services.procedure.portlet.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.procedure.portlet.model.Action;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.Field;
import org.osivia.services.procedure.portlet.model.FilePath;
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.ProcedureObject;
import org.osivia.services.procedure.portlet.model.Step;
import org.osivia.services.procedure.portlet.model.VariableTypesEnum;
import org.osivia.services.procedure.portlet.service.IProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;
import org.springframework.web.portlet.multipart.MultipartActionRequest;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

@Controller
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

    private static final String LIST_VIEW = "list";


    /** Portlet context. */
    private PortletContext portletContext;
    /** Portlet config. */
    private PortletConfig portletConfig;

    /** procedureService */
    @Autowired
    private IProcedureService procedureService;


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
        return EDIT_VIEW;
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
            ProcedureModel procedureModel = procedureService.retrieveProcedureByPath(nuxeoController, getPath(request));
            form = new Form(procedureModel);
            if (StringUtils.isNotEmpty(selectedStep)) {
                form.setSelectedStep(selectedStep);
            } else {
                form.setSelectedStep("0");
            }
        } else if (StringUtils.isNotEmpty(getPath(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREINSTANCE.getName())) {
            ProcedureInstance procedureInstance = procedureService.retrieveProcedureInstanceByPath(nuxeoController, getPath(request));
            ProcedureModel procedureModel = procedureService.retrieveProcedureByPath(nuxeoController, procedureInstance.getProcedureModelPath());
            form = new Form(procedureModel, procedureInstance);
        } else {
            List<ProcedureModel> listProcedures = procedureService.listProcedures(nuxeoController, getPortalUrlFactory());
            form = new Form(listProcedures);
        }
        return form;
    }

    @ActionMapping(value = "editProcedure", params = "launchProcedure")
    public void launchProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, IOException {

        form.setProcedureInstance(new ProcedureInstance());
        form.getProcedureInstance().setProcedureModelPath(form.getProcedureModel().getPath());

        response.setRenderParameter("action", "viewProcedure");
    }


    @ActionMapping(value = "actionProcedure", params = "saveDocument")
    public void saveDocument(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form,
            @RequestParam(value = "variableName") String variableName) throws PortletException {

        String path = getPath(request);
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.createDocumentFromBlob(nuxeoController, path, variableName);

        form.setAlertSuccess("Le document a bien été enregistré");

        response.setRenderParameter("action", "viewProcedure");
    }


    @ActionMapping(value = "actionProcedure", params = "proceedProcedure")
    public void proceedProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "stepReference") String stepReference) throws PortletException, IOException {

        if (request instanceof MultipartActionRequest) {
            // set the uploaded files in the instance
            MultipartActionRequest multipartActionRequest = (MultipartActionRequest) request;
            for (Field field : form.getTheCurrentStep().getFields()) {
                if (StringUtils.equals(field.getType(), VariableTypesEnum.FILE.name()) && field.isInput()) {
                    MultipartFile multipartFile = multipartActionRequest.getFileMap().get("file:" + field.getName());
                    if (multipartFile.getSize() > 0) {
                        FilePath filePath = new FilePath();
                        filePath.setFile(multipartFile);
                        filePath.setVariableName(field.getName());
                        filePath.setFileName(String.valueOf(multipartFile.getOriginalFilename()));
                        form.getProcedureInstance().getFilesPath().put(filePath.getVariableName(), filePath);
                    }
                }
            }
        }

        // validating next step
        String taskTitle = buildTaskTitle(form, stepReference);

        if (taskTitle != null) {
            if (StringUtils.isNotEmpty(getPath(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREMODEL.getName())) {
                // if there is no instance, create it
                final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
                procedureService.createProcedureInstance(nuxeoController, form.getProcedureModel(), form.getProcedureInstance(), taskTitle);
                // redirect to end of step page
                response.setRenderParameter("action", "endStep");
            } else if (StringUtils.isNotEmpty(getPath(request)) && StringUtils.equals(getDocType(request), DocumentTypeEnum.PROCEDUREINSTANCE.getName())) {
                // update the instance
                final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
                ProcedureInstance procedureInstance = procedureService.updateProcedureInstance(nuxeoController, form.getProcedureInstance(), getPath(request),
                        taskTitle);
                form.setProcedureInstance(procedureInstance);

                // redirect to end of step page
                response.setRenderParameter("action", "endStep");
            } else {
                // shouldn't happen
                response.setRenderParameter("action", "viewProcedure");
            }
        } else {
            // if step doesn't exist do nothing and display current step
            response.setRenderParameter("action", "viewProcedure");
        }
    }

    /**
     * @param form
     * @param stepReference
     * @return
     */
    private String buildTaskTitle(Form form, String stepReference) {
        String taskTitle = null;
        if (StringUtils.isNotEmpty(stepReference)) {
            if (StringUtils.equals(stepReference, "endStep")) {
                // if it's the final step
                form.getProcedureInstance().setCurrentStep(stepReference);
                taskTitle = StringUtils.EMPTY;
            } else {
                // check step exist
                for (Step step : form.getProcedureModel().getSteps()) {
                    if (StringUtils.equals(stepReference, step.getReference())) {
                        form.getProcedureInstance().setCurrentStep(stepReference);
                        taskTitle = step.getStepName();
                        break;
                    }
                }
            }
        }
        return taskTitle;
    }


    @ActionMapping(value = "editProcedure", params = "saveProcedure")
    public void saveProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, IOException {

        String path = getPath(request);

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        if (StringUtils.isNotEmpty(path)) {
            // if the procedure exist in database, update it
            procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
            response.setRenderParameter("action", "editProcedure");
        } else {
            // if the procedure doesn't exist in database, create it
            ProcedureModel createdProcedure = procedureService.createProcedure(nuxeoController, form.getProcedureModel());


            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put(Constants.WINDOW_PROP_URI, createdProcedure.getPath());
            windowProperties.put("osivia.doctype", DocumentTypeEnum.PROCEDUREMODEL.getName());
            windowProperties.put("osivia.title", "Éditer une procedure");
            windowProperties.put("osivia.hideDecorators", "1");
            windowProperties.put("osivia.ajaxLink", "1");
            windowProperties.put("osivia.procedure.admin", "adminproc");
            String redirectUrl;
            try {
                redirectUrl = getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance",
                        windowProperties, false);
            } catch (final PortalException e) {
                throw new PortletException(e);
            }
            response.sendRedirect(redirectUrl);
        }
    }

    @ActionMapping(value = "editProcedure", params = "deleteProcedure")
    public void deleteProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, IOException {

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.deleteProcedure(nuxeoController, form.getProcedureModel());

        String redirectUrl = getPortalUrlFactory().getBackURL(nuxeoController.getPortalCtx(), false);
        response.sendRedirect(redirectUrl);
    }

    @ActionMapping(value = "editProcedure", params = "addStep")
    public void addStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, IOException {

        Integer newIndex = Integer.valueOf(form.getProcedureModel().getSteps().size());
        String path = getPath(request);

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        if (StringUtils.isNotEmpty(path)) {
            // if the procedure exist in database, update it
            form.getProcedureModel().getSteps().add(new Step(newIndex));
            form.setSelectedStep(String.valueOf(newIndex));
            procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
            response.setRenderParameter("action", "editStep");
        } else {
            // if the procedure doesn't exist in database, create it
            ProcedureModel createdProcedure = procedureService.createProcedure(nuxeoController, form.getProcedureModel());
            createdProcedure.getSteps().add(new Step(newIndex));
            procedureService.updateProcedure(nuxeoController, createdProcedure);

            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put(Constants.WINDOW_PROP_URI, createdProcedure.getPath());
            windowProperties.put("osivia.doctype", DocumentTypeEnum.PROCEDUREMODEL.getName());
            windowProperties.put("osivia.title", "Éditer une procedure");
            windowProperties.put("osivia.hideDecorators", "1");
            windowProperties.put("osivia.ajaxLink", "1");
            windowProperties.put("osivia.procedure.admin", "adminprocstep");
            String redirectUrl;
            try {
                redirectUrl = getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance",
                        windowProperties, false);
            } catch (final PortalException e) {
                throw new PortletException(e);
            }
            response.sendRedirect(redirectUrl);
        }
    }

    @ActionMapping(value = "editProcedure", params = "addObject")
    public void addObject(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, IOException {


        String path = getPath(request);

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        if (StringUtils.isNotEmpty(path)) {
            // if the procedure exist in database, update it
            form.getProcedureModel().getProcedureObjects().add(new ProcedureObject());
            procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
            response.setRenderParameter("action", "editProcedure");
        } else {
            // if the procedure doesn't exist in database, create it
            ProcedureModel createdProcedure = procedureService.createProcedure(nuxeoController, form.getProcedureModel());
            createdProcedure.getProcedureObjects().add(new ProcedureObject());
            procedureService.updateProcedure(nuxeoController, createdProcedure);

            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put(Constants.WINDOW_PROP_URI, createdProcedure.getPath());
            windowProperties.put("osivia.doctype", DocumentTypeEnum.PROCEDUREMODEL.getName());
            windowProperties.put("osivia.title", "Éditer une procedure");
            windowProperties.put("osivia.hideDecorators", "1");
            windowProperties.put("osivia.ajaxLink", "1");
            windowProperties.put("osivia.procedure.admin", "adminproc");
            String redirectUrl;
            try {
                redirectUrl = getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance",
                        windowProperties, false);
            } catch (final PortalException e) {
                throw new PortletException(e);
            }
            response.sendRedirect(redirectUrl);
        }
    }

    /**
     * @param request
     * @return
     */
    private String getPath(PortletRequest request) {
        PortalWindow window = WindowFactory.getWindow(request);
        String path = window.getProperty(Constants.WINDOW_PROP_URI);
        return path;
    }

    /**
     * @param request
     * @return
     */
    private String getDocType(PortletRequest request) {
        PortalWindow window = WindowFactory.getWindow(request);
        String path = window.getProperty("osivia.doctype");
        return path;
    }

    /**
     * @param request
     * @return
     */
    private String getAction(PortletRequest request) {
        PortalWindow window = WindowFactory.getWindow(request);
        String path = window.getProperty("osivia.procedure.admin");
        return path;
    }


    @ActionMapping(value = "editProcedure", params = "editStep")
    public void editStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, IOException {

        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editStep", params = "saveStep")
    public void saveStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {

        form.getProcedureModel().getSteps().set(Integer.valueOf(form.getTheSelectedStep().getIndex()), form.getTheSelectedStep());

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editProcedure");
    }

    @ActionMapping(value = "editStep", params = "deleteStep")
    public void deleteStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        form.getProcedureModel().getSteps().remove(Integer.valueOf(form.getSelectedStep()).intValue());
        form.getProcedureModel().updateStepsIndexes();

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editProcedure");
    }

    @ActionMapping(value = "editProcedure", params = "deleteStep")
    public void deleteStep(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(value = "selectedStep",
            required = false) String selectedStep) throws PortletException {

        form.getProcedureModel().getSteps().remove(Integer.valueOf(selectedStep).intValue());
        form.getProcedureModel().updateStepsIndexes();

        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());
        response.setRenderParameter("action", "editProcedure");
    }

    @ActionMapping(value = "editStep", params = "addField")
    public void addField(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        Field field = new Field(form.getTheSelectedStep().getHighestOrder() + 1);
        field.setInput(true);
        form.getTheSelectedStep().getFields().add(field);
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());

        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editStep", params = "addButton")
    public void addButton(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException {
        form.getTheSelectedStep().getActions().add(new Action());
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());

        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editStep", params = "deleteButton")
    public void deleteButton(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form,
            @RequestParam(value = "selectedButton") String index) throws PortletException {
        form.getTheSelectedStep().getActions().remove(Integer.valueOf(index).intValue());
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());

        response.setRenderParameter("action", "editStep");
    }

    @ActionMapping(value = "editStep", params = "deleteField")
    public void deleteField(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form,
            @RequestParam(value = "selectedRow") String index) throws PortletException {
        form.getTheSelectedStep().getFields().remove(Integer.valueOf(index).intValue());
        final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        procedureService.updateProcedure(nuxeoController, form.getProcedureModel());

        response.setRenderParameter("action", "editStep");
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
