package org.osivia.services.procedure.portlet.controller;

import javax.annotation.PostConstruct;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.services.procedure.portlet.model.AdminForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

@Controller
@RequestMapping("ADMIN")
public class ProcedurePortletAdminController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

    /** Portlet context. */
    private PortletContext portletContext;
    /** Portlet config. */
    private PortletConfig portletConfig;

    private static final String ADMIN_VIEW = "admin";

    public static final String PROCEDURE_PATH_KEY = "osivia.services.procedure.procedurePath";


    public ProcedurePortletAdminController() {
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


    @RenderMapping
    public String defaultView(RenderRequest request, RenderResponse response) throws PortletException, CMSException {
        return ADMIN_VIEW;
    }

    @ModelAttribute("adminForm")
    public AdminForm getAdminForm(PortletRequest request, PortletResponse response) throws PortletException {
        final PortalWindow window = WindowFactory.getWindow(request);
        return new AdminForm(window.getProperty(PROCEDURE_PATH_KEY));
    }

    @ActionMapping(value = "action", params = "valider")
    public void setAdminProperty(@ModelAttribute("adminForm") final AdminForm adminForm, final ActionRequest request, final ActionResponse response)
            throws Exception {
        final PortalWindow window = WindowFactory.getWindow(request);
        window.setProperty(PROCEDURE_PATH_KEY, adminForm.getProcedurePath());
        window.setProperty("osivia.hideEmptyPortlet", "1");
        response.setPortletMode(PortletMode.VIEW);
        response.setRenderParameter("action", "default");
        response.setWindowState(WindowState.NORMAL);
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
