package org.osivia.services.procedure.plugin;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;


public class ProcedureMenubarModule implements MenubarModule {

    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext spaceDocumentContext)
            throws PortalException {
        // TODO Auto-generated method stub

    }

    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar, DocumentContext documentContext)
            throws PortalException {
        if (documentContext instanceof NuxeoDocumentContext) {
            documentContext = (NuxeoDocumentContext) documentContext;
            Document document = ((NuxeoDocumentContext) documentContext).getDocument();
            if (document != null && StringUtils.equals(document.getType(), "RecordFolder")) {
                String documentPath = document.getPath();
                NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
                // Map<String, String> pageParams = new HashMap<String, String>();
                String displayContext = "create";
                String cmsUrl = nuxeoController.getPortalUrlFactory().getCMSUrl(portalControllerContext, null, documentPath, null, null, displayContext, null,
                        null, null, null);

                final MenubarItem item = new MenubarItem("ADD", "Ajouter un élément", "halflings halflings-plus", MenubarGroup.ADD, 0, cmsUrl, null, null, null);
                item.setAjaxDisabled(true);

                menubar.add(item);
            }
        }

        // osivia.services.procedure.webid
        // osivia.doctype

    }

}
