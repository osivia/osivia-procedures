package org.osivia.services.procedure.portlets.controller;

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
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.procedure.portlets.form.service.IFormService;
import org.osivia.services.procedure.portlets.model.Procedure;
import org.osivia.services.procedure.portlets.model.ProcedureModel;
import org.osivia.services.procedure.portlets.model.proto.lmg.Form;
import org.osivia.services.procedure.portlets.model.service.IProcedureModelService;
import org.osivia.services.procedure.portlets.service.IProcedureService;
import org.osivia.services.procedure.portlets.service.ProcedureServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Procedure portlet controller.
 *
 * @author David Chevrier
 * @see CMSPortlet
 * @see PortletContextAware
 * @see PortletConfigAware
 */
@Controller
@RequestMapping(value = "VIEW")
public class ProcedurePortletController extends CMSPortlet implements PortletContextAware, PortletConfigAware {
    
    /** Start view. */
    private static final String START_VIEW = "start-procedure";
    /** View path. */
    private static final String VIEW = "view";
    /** View next step. */
    private static final String VIEW_END_PROCEDURE = "view-end";

    /** ProcedureModel service. */
    @Autowired
    private IProcedureModelService modelService;
    /** Procedure service. */
    @Autowired
    private IProcedureService procedureService;
    /** Form service. */
    @Autowired
    private IFormService formService;

    /** Portlet context. */
    private PortletContext portletContext;
    /** Portlet config. */
    private PortletConfig portletConfig;


    /**
     * Default constructor.
     */
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
        super.init(this.portletConfig);
    }
    
    /**
     * Get procedure model.
     *
     * @param request portlet request
     * @param response portlet response
     * @return procedure model
     * @throws PortletException
     */
    @ModelAttribute(value = "procedureModel")
    public ProcedureModel getProcedureModel(PortletRequest request, PortletResponse response) throws PortletException {
        NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);
        return this.modelService.getProcedureModel(nuxeoController);
    }
    
    /**
     * Get procedure.
     *
     * @param request portlet request
     * @param response portlet response
     * @return procedure
     * @throws PortletException
     */
    @ModelAttribute(value = "procedure")
    public Procedure getProcedure(PortletRequest request, PortletResponse response) throws PortletException {
        NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);
        return this.procedureService.getProcedure(nuxeoController);
    }
    
    /**
     * Gets form.
     *
     * @param request portlet request
     * @param response portlet response
     * @return form
     * @throws PortletException
     */
    @ModelAttribute(value = "form")
    public Form getForm(PortletRequest request, PortletResponse response) throws PortletException {
        return new Form();
    }
    
   /**
    * Start procedure action.
    * 
    * @param request
    * @param response
    * @param procedure
    * @throws PortletException
    * @throws IOException 
    */
    @ActionMapping(value = "startProcedureAction")
    public void startProcedureAction(ActionRequest request, ActionResponse response, @ModelAttribute(value = "procedureModel") ProcedureModel procedureModel, 
            @ModelAttribute(value = "procedure") Procedure procedure)
            throws PortletException, IOException {
        NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);
        
        String procedureName = procedureModel.getName();

        Document procedureInstance = this.modelService.startProcedure(nuxeoController, procedureName);
        // Update procedure
        Procedure newProcedure = ((ProcedureServiceImpl) this.procedureService).toViewObject(nuxeoController, procedureInstance);
        this.procedureService.updateProcedure(procedure, newProcedure);
        
        final Map<String, String> windowProperties = new HashMap<String, String>(1);
        windowProperties.put(Constants.WINDOW_PROP_URI, procedureInstance.getPath());
        windowProperties.put("osivia.doctype", procedureInstance.getType());
        windowProperties.put("osivia.title", procedureInstance.getString("pi:directive"));
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put("osivia.ajaxLink", "1");
        String redirectUrl;
        try {
            redirectUrl = getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance", windowProperties, false);
        } catch (final PortalException e) {
            throw new PortletException(e);
        }

        // To update portlets, especially Tasks portlet
        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);

        response.sendRedirect(redirectUrl);
        
    }
    
    /**
     * Next step action.
     * 
     * @param request
     * @param response
     * @param procedure
     * @throws PortletException
     */
    @ActionMapping(value = "nextStepAction")
    public void goToNextStepAction(ActionRequest request, ActionResponse response, @ModelAttribute(value = "procedure") Procedure procedure)
            throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);

        response.setRenderParameter("nextStep", procedure.getNextStep());
        Document procedureInstance = this.procedureService.getDocument(nuxeoController);
        
        this.procedureService.goToNextStep(nuxeoController, procedureInstance, procedure);
        
        String oldMessage = procedureInstance.getString("pi:message");
        this.procedureService.updateProcedure(procedure, this.procedureService.getProcedure(nuxeoController));
        procedure.setMessage(oldMessage);
        
        // To update portlets, especially Tasks portlet
        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);
        
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
    public String startView(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        String view = StringUtils.EMPTY;
        NuxeoController nuxeoController = new NuxeoController(request, response, this.portletContext);
        
        PortalWindow window = WindowFactory.getWindow(nuxeoController.getRequest());
        String docType = window.getProperty("osivia.doctype");
        if("Procedure".equals(docType)){
            view = START_VIEW;
        } else if("ProcedureInstance".equals(docType)){
            view = VIEW;
        }
        
        return view;
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
    @RenderMapping(params = "nextStep")
    public String view(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        return VIEW;
    }
    
    
    @RenderMapping(params = "nextStep=endStep")
    public String viewNextStep(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        return VIEW_END_PROCEDURE;
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
