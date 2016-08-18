package org.osivia.services.procedure.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
//import org.osivia.portal.api.cms.DocumentContext;
//import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
//import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;

/**
 * Procedure menubar module.
 * 
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
public class ProcedureMenubarModule {
//implements MenubarModule {

//    /** Portal URL factory. */
//    private final IPortalUrlFactory portalUrlFactory;
//    /** Bundle factory. */
//    private final IBundleFactory bundleFactory;
//
//
//    /**
//     * Constructor.
//     */
//    public ProcedureMenubarModule() {
//        super();
//
//        // Portal URL factory
//        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
//
//        // Bundle factory
//        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
//                IInternationalizationService.MBEAN_NAME);
//        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
//            DocumentContext<? extends EcmDocument> spaceDocumentContext) throws PortalException {
//        // Do nothing
//    }
//
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
//            DocumentContext<? extends EcmDocument> documentContext) throws PortalException {
//        // Request
//        PortletRequest request = portalControllerContext.getRequest();
//        // Bundle
//        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());
//
//        // Style
//        String style = (String) request.getAttribute("style");
//
//        if (StringUtils.equals(style, "adminproc")) {
//            // Title
//            String title = bundle.getString("CREATE_PROCEDURE_ACTION");
//
//            // Window properties
//            Map<String, String> properties = new HashMap<String, String>();
//            properties.put("osivia.title", title);
//            properties.put("osivia.procedure.admin", style);
//
//            // URL
//            String url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "osivia-services-procedure-portletInstance", properties);
//
//            // Menubar item
//            MenubarItem item = new MenubarItem("CREATE_PROCEDURE", title, "glyphicons glyphicons-plus-sign", MenubarGroup.SPECIFIC, 22, url, null, null, null);
//
//            menubar.add(item);
//        }
//    }

}
