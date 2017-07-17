package org.osivia.services.procedure.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.services.procedure.portlet.controller.ProcedurePortletAdminController;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;


/**
 * MenubarModule for procedure
 * 
 * @author Dorian Licois
 */
public class ProcedureMenubarModule implements MenubarModule {

    /** Menubar service. */
    private final IMenubarService menubarService;

    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;

    public ProcedureMenubarModule() {
        super();

        this.menubarService = Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);

        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }

    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext spaceDocumentContext)
            throws PortalException {

    }

    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext)
            throws PortalException {
        if (documentContext instanceof NuxeoDocumentContext) {
            documentContext = (NuxeoDocumentContext) documentContext;
            Document document = ((NuxeoDocumentContext) documentContext).getDocument();
            NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

            Bundle bundle = bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

            if (document != null && StringUtils.equals(document.getType(), DocumentTypeEnum.RECORDFOLDER.getDocType())) {
                String documentPath = document.getPath();

                // ADD RECORD
                String cmsUrl = nuxeoController.getPortalUrlFactory().getCMSUrl(portalControllerContext, null, documentPath, null, null, "create", null,
                        null, null, null);

                MenubarItem item = new MenubarItem("ADD", bundle.getString("ADD_ELEMENT"), "halflings halflings-plus", MenubarGroup.ADD, 0, cmsUrl, null, null,
                        null);
                item.setAjaxDisabled(true);

                menubar.add(item);

                // EDIT RECORD FOLDER
                String webId = document.getProperties().getString("ttc:webid");
                Map<String, String> windowProperties = new HashMap<String, String>();
                windowProperties.put("osivia.doctype", DocumentTypeEnum.RECORDFOLDER.getDocType());
                windowProperties.put("osivia.hideDecorators", "1");
                windowProperties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, Constants.PORTLET_VALUE_ACTIVATE);
                windowProperties.put("osivia.ajaxLink", "1");
                windowProperties.put("osivia.procedure.admin", "adminrecord");
                windowProperties.put("osivia.services.procedure.webid", webId);
                windowProperties.put("osivia.title", bundle.getString("EDIT_PROCEDURE"));

                String editRecUrl = nuxeoController.getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(),
                        "osivia-services-procedure-portletInstance", windowProperties, PortalUrlType.DEFAULT);
                

                final MenubarDropdown parent = menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);

                item = new MenubarItem("EDIT", bundle.getString("EDIT_RECORD_FOLDER"), "halflings halflings-pencil", parent, 0, editRecUrl, null, null, null);
                item.setAjaxDisabled(true);

                menubar.add(item);
            } else if (document != null && StringUtils.equals(document.getType(), DocumentTypeEnum.RECORDCONTAINER.getDocType())) {
                String documentPath = document.getPath();

                // ADD RECORD FOLDER
                final Map<String, String> windowProperties = new HashMap<String, String>();
                windowProperties.put("osivia.doctype", DocumentTypeEnum.RECORDFOLDER.getDocType());
                windowProperties.put(ProcedurePortletAdminController.PROCEDURE_PATH_KEY, documentPath);
                windowProperties.put("osivia.hideDecorators", "1");
                windowProperties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, Constants.PORTLET_VALUE_ACTIVATE);
                windowProperties.put("osivia.ajaxLink", "1");
                windowProperties.put("osivia.procedure.admin", "adminrecord");
                windowProperties.put("osivia.title", bundle.getString("CREATE_RECORD_FOLDER"));
                String addRecUrl = nuxeoController.getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(),
                        "osivia-services-procedure-portletInstance", windowProperties, PortalUrlType.DEFAULT);

                MenubarItem item = new MenubarItem("ADD", bundle.getString("ADD_RECORD_FOLDER"), "halflings halflings-plus", MenubarGroup.ADD, 0, addRecUrl,
                        null, null, null);
                item.setAjaxDisabled(true);

                menubar.add(item);
            } else if (document != null && StringUtils.equals(document.getType(), DocumentTypeEnum.RECORD.getDocType())) {
                String documentPath = document.getPath();

                // EDIT RECORD
                final MenubarDropdown parent = menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);
                String cmsUrl = nuxeoController.getPortalUrlFactory().getCMSUrl(portalControllerContext, null, documentPath, null, null, "edit", null, null,
                        null, null);
                MenubarItem item = new MenubarItem("EDIT", bundle.getString("EDIT_RECORD"), "halflings halflings-pencil", parent, 0, cmsUrl, null, null, null);
                item.setAjaxDisabled(true);
                menubar.add(item);

                // DELETE RECORD
                cmsUrl = nuxeoController.getPortalUrlFactory().getCMSUrl(portalControllerContext, null, documentPath, null, null, "delete", null, null,
                        null, null);
                item = new MenubarItem("EDIT", bundle.getString("DELETE_RECORD"), "halflings halflings-trash", parent, 20, cmsUrl, null, null, null);
                item.setAjaxDisabled(true);
                item.setDivider(true);
                menubar.add(item);
            }
        }
    }

}
