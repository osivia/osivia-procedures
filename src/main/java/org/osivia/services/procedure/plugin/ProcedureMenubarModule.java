package org.osivia.services.procedure.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.Permissions;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.AccessibilityRoles;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.html.HTMLConstants;
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
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;


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

            Permissions permissions = documentContext.getPermissions();
            NuxeoPublicationInfos pubInfos = ((NuxeoDocumentContext) documentContext).getPublicationInfos();            

            Bundle bundle = bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

            if (document != null && StringUtils.equals(document.getType(), DocumentTypeEnum.RECORDFOLDER.getDocType())) {
                String documentPath = document.getPath();


                // ADD RECORD
                if( pubInfos.getSubtypes().size() >0)   {
                     String cmsUrl = nuxeoController.getPortalUrlFactory().getCMSUrl(portalControllerContext, null, documentPath, null, null, "create", null,
                            null, null, null);

                    MenubarItem item = new MenubarItem("ADD", bundle.getString("ADD_ELEMENT"), "halflings halflings-plus", MenubarGroup.ADD, 0, cmsUrl, null,
                            null, null);
                    item.setAjaxDisabled(true);

                    menubar.add(item);
                }

                // EDIT RECORD FOLDER
                
                if( permissions.isEditable())   {
                    String webId = document.getProperties().getString("ttc:webid");
                    Map<String, String> windowProperties = new HashMap<String, String>();
                    windowProperties.put("osivia.doctype", DocumentTypeEnum.RECORDFOLDER.getDocType());
                    windowProperties.put("osivia.hideDecorators", "1");
                    windowProperties.put("osivia.procedure.admin", "adminrecord");
                    windowProperties.put("osivia.services.procedure.webid", webId);
                    windowProperties.put("osivia.title", bundle.getString("EDIT_RECORD_FOLDER"));

                    String editRecUrl = nuxeoController.getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(),
                            "osivia-services-procedure-portletInstance", windowProperties, PortalUrlType.DEFAULT);


                    final MenubarDropdown parent = menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);

                    MenubarItem item = new MenubarItem("EDIT", bundle.getString("EDIT_RECORD_FOLDER"), "halflings halflings-pencil", parent, 0, editRecUrl, null, null,
                            null);
                    item.setAjaxDisabled(true);

                    menubar.add(item);
                }

                // DELETE RECORD FOLDER
                if( permissions.isDeletable() )   {

                    final MenubarDropdown parent = menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);
                    
                    String itemLabel = bundle.getString("DELETE");
                    MenubarItem item = new MenubarItem("DELETE", itemLabel, "glyphicons glyphicons-bin", parent, 20, null, null, null, null);
                    item.setAjaxDisabled(true);
                    item.setDivider(true);

                    // Fancybox properties
                    final Map<String, String> properties = new HashMap<String, String>();
                    properties.put("docId", document.getId());
                    properties.put("docPath", document.getPath());

                    // Fancybox identifier
                    final String fancyboxId = nuxeoController.getCMSCtx().getResponse().getNamespace() + "_PORTAL_DELETE";


                    // Fancybox delete action URL

                    /*

                    String removeURL = nuxeoController.getPortalUrlFactory().getPutDocumentInTrashUrl(portalControllerContext, pubInfos.getLiveId(),
                            pubInfos.getPath());

                    // Fancybox HTML data
                    final String fancybox = this.generateDeleteConfirmationFancybox(properties, bundle, fancyboxId, removeURL);
                    item.setAssociatedHTML(fancybox);
                    

                    // URL
                    final String url = "#" + fancyboxId;*/
                    
                    final String url = "#";
                    item.setUrl("javascript:;");
                    item.getData().put("fancybox", StringUtils.EMPTY);
                    item.getData().put("src", url);

                    menubar.add(item);
                }


            } else if (document != null && StringUtils.equals(document.getType(), DocumentTypeEnum.RECORDCONTAINER.getDocType())) {
                String documentPath = document.getPath();
                
                if( pubInfos.getSubtypes().size() >0)   {

                    // ADD RECORD FOLDER
                    final Map<String, String> windowProperties = new HashMap<String, String>();
                    windowProperties.put("osivia.doctype", DocumentTypeEnum.RECORDFOLDER.getDocType());
                    windowProperties.put(ProcedurePortletAdminController.PROCEDURE_PATH_KEY, documentPath);
                    windowProperties.put("osivia.hideDecorators", "1");
                    windowProperties.put("osivia.procedure.admin", "adminrecord");
                    windowProperties.put("osivia.title", bundle.getString("CREATE_RECORD_FOLDER"));
                    String addRecUrl = nuxeoController.getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(),
                            "osivia-services-procedure-portletInstance", windowProperties, PortalUrlType.DEFAULT);

                    MenubarItem item = new MenubarItem("ADD", bundle.getString("ADD_RECORD_FOLDER"), "halflings halflings-plus", MenubarGroup.ADD, 0, addRecUrl,
                            null, null, null);
                    item.setAjaxDisabled(true);

                    menubar.add(item);
                }
            } else if (document != null && StringUtils.equals(document.getType(), DocumentTypeEnum.RECORD.getDocType())) {
                String documentPath = document.getPath();
                
                if( permissions.isEditable())	{

	                // EDIT RECORD
	                final MenubarDropdown parent = menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);
	                String cmsUrl = nuxeoController.getPortalUrlFactory().getCMSUrl(portalControllerContext, null, documentPath, null, null, "edit", null, null,
	                        null, null);
	                MenubarItem item = new MenubarItem("EDIT", bundle.getString("EDIT_RECORD"), "halflings halflings-pencil", parent, 0, cmsUrl, null, null, null);
	                item.setAjaxDisabled(true);
	                menubar.add(item);
                }
                if( permissions.isDeletable()) {
                    final MenubarDropdown parent = menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);

                    // DELETE RECORD
                    String cmsUrl = nuxeoController.getPortalUrlFactory().getCMSUrl(portalControllerContext, null, documentPath, null, null, "delete", null,
                            null, null, null);
                    MenubarItem item = new MenubarItem("EDIT", bundle.getString("DELETE_RECORD"), "halflings halflings-trash", parent, 20, cmsUrl, null, null,
                            null);
                    item.setAjaxDisabled(true);
                    item.setDivider(true);
                    menubar.add(item);
                }
            }
        }
    }

    /**
     * Generate delete confirmation fancybox HTML data.
     *
     * @param properties fancybox properties
     * @param bundle internationalization bundle
     * @param fancyboxId fancybox identifier
     * @param actionURL delete action URL
     * @return fancybox HTML data
     */
    private String generateDeleteConfirmationFancybox(Map<String, String> properties, Bundle bundle, String fancyboxId, String actionURL) {
        // Fancybox container
        final Element fancyboxContainer = DOM4JUtils.generateDivElement("hidden");

        // Container
        final Element container = DOM4JUtils.generateDivElement(null);
        DOM4JUtils.addAttribute(container, HTMLConstants.ID, fancyboxId);
        fancyboxContainer.add(container);

        // Form
        final Element form = DOM4JUtils.generateElement(HTMLConstants.FORM, "text-center", null, null, AccessibilityRoles.FORM);
        DOM4JUtils.addAttribute(form, HTMLConstants.ACTION, actionURL);
        DOM4JUtils.addAttribute(form, HTMLConstants.METHOD, HTMLConstants.FORM_METHOD_POST);
        container.add(form);

        // Message
        final Element message = DOM4JUtils.generateElement(HTMLConstants.P, null, bundle.getString("CMS_DELETE_CONFIRM_MESSAGE"));
        form.add(message);

        // Hidden fields
        for (final Entry<String, String> property : properties.entrySet()) {
            final Element hidden = DOM4JUtils.generateElement(HTMLConstants.INPUT, null, null);
            DOM4JUtils.addAttribute(hidden, HTMLConstants.TYPE, HTMLConstants.INPUT_TYPE_HIDDEN);
            DOM4JUtils.addAttribute(hidden, HTMLConstants.NAME, property.getKey());
            DOM4JUtils.addAttribute(hidden, HTMLConstants.VALUE, property.getValue());
            form.add(hidden);
        }

        // OK button
        final Element okButton = DOM4JUtils
                .generateElement(HTMLConstants.BUTTON, "btn btn-warning", bundle.getString("YES"), "halflings halflings-alert", null);
        DOM4JUtils.addAttribute(okButton, HTMLConstants.TYPE, HTMLConstants.INPUT_TYPE_SUBMIT);
        form.add(okButton);

        // Cancel button
        final Element cancelButton = DOM4JUtils.generateElement(HTMLConstants.BUTTON, "btn btn-default", bundle.getString("NO"));
        DOM4JUtils.addAttribute(cancelButton, HTMLConstants.TYPE, HTMLConstants.INPUT_TYPE_BUTTON);
        DOM4JUtils.addAttribute(cancelButton, HTMLConstants.ONCLICK, "closeFancybox()");
        form.add(cancelButton);

        return DOM4JUtils.write(fancyboxContainer);
    }

}
