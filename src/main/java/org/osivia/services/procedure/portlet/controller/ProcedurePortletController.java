package org.osivia.services.procedure.portlet.controller;

import java.io.IOException;
import java.util.HashMap;
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
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.Step;
import org.osivia.services.procedure.portlet.service.IProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

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
        PortalWindow window = WindowFactory.getWindow(request);
        String action = window.getProperty("osivia.procedure.admin");
        if (StringUtils.equals(action, "adminproc")) {
            return CREATE_VIEW;
        } else if (StringUtils.equals(action, "adminprocstep")) {
            return EDIT_VIEW;
        } else {
            return VIEW_PROCEDURE;
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
        PortalWindow window = WindowFactory.getWindow(request);
        String path = window.getProperty(Constants.WINDOW_PROP_URI);
        String doctype = window.getProperty("osivia.doctype");


        Form form;
        if (StringUtils.isNotEmpty(path) && StringUtils.equals(doctype, DocumentTypeEnum.PROCEDUREMODEL.getName())) {
            final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
            ProcedureModel procedureModel = procedureService.retrieveProcedureByPath(nuxeoController, path);
            form = new Form(procedureModel);
            if (StringUtils.isNotEmpty(selectedStep)) {
                form.setSelectedStep(selectedStep);
            } else {
                form.setSelectedStep("0");
            }
        } else if (StringUtils.isNotEmpty(path) && StringUtils.equals(doctype, DocumentTypeEnum.PROCEDUREINSTANCE.getName())) {
            final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
            ProcedureInstance procedureInstance = procedureService.retrieveProcedureInstanceByPath(nuxeoController, path);
            ProcedureModel procedureModel = procedureService.retrieveProcedureByPath(nuxeoController, procedureInstance.getProcedureModelPath());
            form = new Form(procedureModel, procedureInstance);
        } else {
            form = new Form(new ProcedureModel());
        }
        return form;
    }

    @ActionMapping(value = "editProcedure", params = "launchProcedure")
    public void launchProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, IOException {

        form.setProcedureInstance(new ProcedureInstance());
        form.getProcedureInstance().setProcedureModelPath(form.getProcedureModel().getPath());

        response.setRenderParameter("action", "viewProcedure");
    }

    @ActionMapping(value = "actionProcedure", params = "proceedProcedure")
    public void proceedProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form, @RequestParam(
            value = "stepReference") String stepReference) throws PortletException, IOException {

        // validating next step
        boolean isStepValid = false;
        String taskTitle = null;
        if (StringUtils.isNotEmpty(stepReference)) {
            if (StringUtils.equals(stepReference, "endStep")) {
                // if it's the final step
                isStepValid = true;
                form.getProcedureInstance().setCurrentStep(stepReference);
                taskTitle = StringUtils.EMPTY;
            } else {
                // check step exist
                for (Step step : form.getProcedureModel().getSteps()) {
                    if (StringUtils.equals(stepReference, step.getReference())) {
                        isStepValid = true;
                        form.getProcedureInstance().setCurrentStep(stepReference);
                        taskTitle = step.getStepName();
                        break;
                    }
                }
            }
        }

        if (isStepValid) {
            // if step exist check if there is an instance
            PortalWindow window = WindowFactory.getWindow(request);
            String path = window.getProperty(Constants.WINDOW_PROP_URI);
            String doctype = window.getProperty("osivia.doctype");

            if (StringUtils.isNotEmpty(path) && StringUtils.equals(doctype, DocumentTypeEnum.PROCEDUREMODEL.getName())) {
                // id there is no instance, create it
                final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
                procedureService.createProcedureInstance(nuxeoController, form.getProcedureModel(), form.getProcedureInstance(), taskTitle);
                // redirect to end of step page
                response.setRenderParameter("action", "endStep");
            } else if (StringUtils.isNotEmpty(path) && StringUtils.equals(doctype, DocumentTypeEnum.PROCEDUREINSTANCE.getName())) {
                // update the instance
                final NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
                ProcedureInstance procedureInstance = procedureService.updateProcedureInstance(nuxeoController, form.getProcedureInstance(), path, taskTitle);
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


    @ActionMapping(value = "editProcedure", params = "saveProcedure")
    public void saveProcedure(ActionRequest request, ActionResponse response, @ModelAttribute(value = "form") Form form) throws PortletException, IOException {

        PortalWindow window = WindowFactory.getWindow(request);
        String path = window.getProperty(Constants.WINDOW_PROP_URI);

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
            windowProperties.put("osivia.title", "Éditer un procedure");
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
        PortalWindow window = WindowFactory.getWindow(request);
        String path = window.getProperty(Constants.WINDOW_PROP_URI);

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
            windowProperties.put("osivia.title", "Éditer un procedure");
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
