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
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.impl.BasicPublicationInfos;
import org.osivia.portal.api.player.Player;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;

import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;
import fr.toutatice.portail.cms.nuxeo.api.plugin.PluginModule;


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

    /**
     * Get procedure thread player.
     *
     * @param cmsContext CMS context
     * @return procedure thread player
     */
    private Player getProcedurePlayer(DocumentContext<Document> docCtx) {
        final Document document = docCtx.getDoc();

        final Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put(Constants.WINDOW_PROP_URI, document.getPath());
        windowProperties.put("osivia.doctype", document.getType());
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put("osivia.ajaxLink", "1");

        final BasicPublicationInfos publicationInfos = docCtx.getPublicationInfos(BasicPublicationInfos.class);
        if (StringUtils.equals(publicationInfos.getDisplayContext(), "adminproc")) {
            windowProperties.put("osivia.procedure.admin", publicationInfos.getDisplayContext());
            windowProperties.put("osivia.title", "Éditer une procédure");
        }

        final Player linkProps = new Player();
        linkProps.setWindowProperties(windowProperties);
        linkProps.setPortletInstance("osivia-services-procedure-portletInstance");

        return linkProps;
    }

    @Override
    public Player getCMSPlayer(DocumentContext<Document> docCt) {
        final Document doc = docCt.getDoc();
        final String docType = doc.getType();

        if (StringUtils.equals(docType, DocumentTypeEnum.PROCEDUREMODEL.getName()) || StringUtils.equals(docType, DocumentTypeEnum.PROCEDUREINSTANCE.getName())) {
            return getProcedurePlayer(docCt);
        }

        return null;
    }

}
