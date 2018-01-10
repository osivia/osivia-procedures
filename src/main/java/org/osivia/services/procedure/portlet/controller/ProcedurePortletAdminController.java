package org.osivia.services.procedure.portlet.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
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

@Controller
@RequestMapping("ADMIN")
public class ProcedurePortletAdminController {

    private static final String ADMIN_VIEW = "admin";

    public static final String PROCEDURE_PATH_KEY = "osivia.services.procedure.procedurePath";


    public ProcedurePortletAdminController() {
        super();
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

}
