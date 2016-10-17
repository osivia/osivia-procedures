/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/), OSIVIA (http://www.osivia.com) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.osivia.services.procedure.plugin;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;

import org.apache.commons.lang.StringUtils;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.impl.BasicPublicationInfos;
import org.osivia.portal.api.player.Player;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.ProcedureTypeEnum;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;
import fr.toutatice.portail.cms.nuxeo.api.plugin.PluginModule;
import fr.toutatice.portail.cms.nuxeo.api.portlet.ViewList;


/**
 * The Class PlayerModule.
 *
 * @author Jean-Sébastien Steux
 */
public class ProcedurePlayer extends PluginModule implements INuxeoPlayerModule {


    /**
     * Instantiates a new player module.
     *
     * @param portletContext the portlet context
     */
    public ProcedurePlayer(PortletContext portletContext) {
        super(portletContext);
    }

    private Player getProcedureInstancePlayer(DocumentContext<Document> docCtx) {
        final Document document = docCtx.getDoc();

        final BasicPublicationInfos publicationInfos = docCtx.getPublicationInfos(BasicPublicationInfos.class);
        String displayContext = publicationInfos.getDisplayContext();
        final Map<String, String> windowProperties = getProcedureWindowProperties(document);
        windowProperties.put("osivia.title", document.getTitle());
        windowProperties.put("osivia.procedure.admin", displayContext);

        final Player linkProps = getProcedurePlayer(windowProperties);

        return linkProps;
        
    }

    private Map<String, String> getProcedureWindowProperties(final Document document) {
        final Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put("osivia.services.procedure.webid", document.getProperties().getString("ttc:webid"));
        windowProperties.put("osivia.doctype", document.getType());
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, Constants.PORTLET_VALUE_ACTIVATE);
        windowProperties.put("osivia.ajaxLink", "1");
        return windowProperties;
    }
    
    private Player getProcedureModelPlayer(DocumentContext<Document> docCtx) {
        final Document document = docCtx.getDoc();

        final BasicPublicationInfos publicationInfos = docCtx.getPublicationInfos(BasicPublicationInfos.class);
        String displayContext = publicationInfos.getDisplayContext();
        String procedureType = document.getProperties().getString("pcd:procedureType");
        if (StringUtils.equals(ProcedureTypeEnum.LIST.name(), procedureType)) {
            if (StringUtils.equals(displayContext, "adminlist") || StringUtils.equals(displayContext, "listadditem")) {
                // édition d'une procédure de type LIST
                final Map<String, String> windowProperties = getProcedureWindowProperties(document);
                windowProperties.put("osivia.procedure.admin", displayContext);
                windowProperties.put("osivia.title", "Éditer une liste");
                windowProperties.put("osivia.services.procedure.procType", procedureType);
                return getProcedurePlayer(windowProperties);
            } else {
                // affichage du contenu dans une portletList
                Map<String, String> windowProperties = viewListWindowProperties(docCtx, document, publicationInfos);

                Player player = new Player();
                player.setWindowProperties(windowProperties);
                player.setPortletInstance("toutatice-portail-cms-nuxeo-viewListPortletInstance");
                return player;
            }
        } else {
            final Map<String, String> windowProperties = getProcedureWindowProperties(document);
            if (StringUtils.equals(displayContext, "adminproc") || StringUtils.equals(displayContext, "adminprocstep")) {
                // édition d'une procédure
                windowProperties.put("osivia.procedure.admin", displayContext);
                windowProperties.put("osivia.title", "Éditer une procédure");
                return getProcedurePlayer(windowProperties);
            } else {
                // lancement d'une procédure type LIST
                windowProperties.put("osivia.title", document.getTitle());
                return getProcedurePlayer(windowProperties);
            }
        }
    }

    private Player getProcedurePlayer(final Map<String, String> windowProperties) {
        final Player player = new Player();
        player.setWindowProperties(windowProperties);
        player.setPortletInstance("osivia-services-procedure-portletInstance");
        return player;
    }

    private Map<String, String> viewListWindowProperties(DocumentContext<Document> docCtx, final Document document, final BasicPublicationInfos publicationInfos) {
        Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put("osivia.nuxeoRequest", NuxeoController.createFolderRequest(docCtx, false));
        windowProperties.put("osivia.cms.style", ProcedurePlugin.STYLE_VIEW_LISTPROC);
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put("theme.dyna.partial_refresh_enabled", "false");
        windowProperties.put(Constants.WINDOW_PROP_SCOPE, publicationInfos.getScope());
        windowProperties.put(Constants.WINDOW_PROP_VERSION, "1");
        windowProperties.put("osivia.document.metadata", String.valueOf(false));
        windowProperties.put("osivia.title", document.getTitle());
        windowProperties.put(ViewList.USE_ES_WINDOW_PROPERTY, "true");
        if (docCtx.getDoc() != null) {
            windowProperties.put(ViewList.CREATION_PARENT_PATH_WINDOW_PROPERTY, docCtx.getDoc().getPath());
        }
        return windowProperties;
    }

    @Override
    public Player getCMSPlayer(DocumentContext<Document> docCt) {
        final Document doc = docCt.getDoc();
        final String docType = doc.getType();

        if (StringUtils.equals(docType, DocumentTypeEnum.PROCEDUREINSTANCE.getName())) {
            return getProcedureInstancePlayer(docCt);
        }

        if (StringUtils.equals(docType, DocumentTypeEnum.PROCEDUREMODEL.getName())) {
            return getProcedureModelPlayer(docCt);
        }
        return null;
    }
}
