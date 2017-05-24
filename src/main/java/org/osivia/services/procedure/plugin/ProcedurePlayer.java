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
import org.osivia.portal.api.player.Player;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;

import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoPublicationInfos;
import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;
import fr.toutatice.portail.cms.nuxeo.api.plugin.PluginModule;
import fr.toutatice.portail.cms.nuxeo.portlets.forms.ViewProcedurePortlet;


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

    private Player getProcedureInstancePlayer(NuxeoDocumentContext docCtx) {
        final Document document = docCtx.getDocument();

        String displayContext = docCtx.getDisplayContext();
        final Map<String, String> windowProperties = getProcedureWindowProperties((Document) document);
        windowProperties.put("osivia.title", document.getTitle());
        windowProperties.put("osivia.procedure.admin", displayContext);

        final Player linkProps = getProcedurePlayer(windowProperties);

        return linkProps;
    }

    private Player getTaskPlayer(NuxeoDocumentContext docCtx) {
        final Document document = docCtx.getDocument();

        String displayContext = docCtx.getDisplayContext();
        final Map<String, String> windowProperties = getProcedureWindowProperties(document);

        windowProperties.put("osivia.title", document.getProperties().getString("nt:name"));
        windowProperties.put("osivia.procedure.admin", displayContext);

        final Player linkProps = getProcedurePlayer(windowProperties);

        return linkProps;
    }

    private Map<String, String> getProcedureWindowProperties(final Document document) {
        final Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put("osivia.services.procedure.webid", document.getProperties().getString("ttc:webid"));
        windowProperties.put("osivia.services.procedure.uuid", document.getId());
        windowProperties.put("osivia.doctype", document.getType());
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, Constants.PORTLET_VALUE_ACTIVATE);
        windowProperties.put("osivia.ajaxLink", "1");
        return windowProperties;
    }
    
    private Player getProcedureModelPlayer(NuxeoDocumentContext docCtx) {
        final Document document = docCtx.getDocument();

        final NuxeoPublicationInfos publicationInfos = docCtx.getPublicationInfos();
        String displayContext = docCtx.getDisplayContext();
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

    private Player getProcedurePlayer(final Map<String, String> windowProperties) {
        final Player player = new Player();
        player.setWindowProperties(windowProperties);
        player.setPortletInstance("osivia-services-procedure-portletInstance");
        return player;
    }

    private Player getRecordPlayer(NuxeoDocumentContext docCtx) {
        final Player player = new Player();
        if (docCtx.getDocument() != null) {
            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put("osivia.hideDecorators", "1");
            windowProperties.put("osivia.title", docCtx.getDocument().getTitle());

            String displayContext = docCtx.getDisplayContext();
            if (StringUtils.equals(displayContext, "create")) {
                windowProperties.putAll(getProcedureWindowProperties(docCtx.getDocument()));
                windowProperties.put("osivia.procedure.admin", displayContext);
                player.setWindowProperties(windowProperties);
                player.setPortletInstance("osivia-services-procedure-portletInstance");
            } else {
                // windowProperties.put(ViewList.CREATION_PARENT_PATH_WINDOW_PROPERTY, docCtx.getDocument().getPath());
                windowProperties.put(Constants.WINDOW_PROP_VERSION, "1");
                windowProperties.put(ViewProcedurePortlet.PROCEDURE_MODEL_ID_WINDOW_PROPERTY, docCtx.getDocument().getProperties().getString("ttc:webid"));
                player.setWindowProperties(windowProperties);
                player.setPortletInstance("toutatice-portail-cms-nuxeo-viewProcedurePortletInstance");
            }
        }
        return player;
    }

    @Override
    public Player getCMSPlayer(NuxeoDocumentContext docCtx) {
        final Document doc = docCtx.getDocument();
        final String docType = doc.getType();

        if (StringUtils.equals(docType, DocumentTypeEnum.PROCEDUREINSTANCE.getDocType())) {
            return getProcedureInstancePlayer(docCtx);
        }

        if (StringUtils.equals(docType, DocumentTypeEnum.PROCEDUREMODEL.getDocType())) {
            return getProcedureModelPlayer(docCtx);
        }

        if (StringUtils.equals(docType, DocumentTypeEnum.TASKDOC.getDocType())) {
            return getTaskPlayer(docCtx);
        }

        if (StringUtils.equals(docType, DocumentTypeEnum.RECORDFOLDER.getDocType())) {
            return getRecordPlayer(docCtx);
        }

        return null;
    }

}
