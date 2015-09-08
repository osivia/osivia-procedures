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
 *
 *
 *    
 */
package org.osivia.services.procedure.plugin;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletContext;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSHandlerProperties;
import org.osivia.portal.core.cms.CMSPublicationInfos;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;

import fr.toutatice.portail.cms.nuxeo.api.domain.IPlayerModule;



/**
 * The Class PlayerModule.
 *
 * @author Jean-Sébastien Steux
 */
public class ProcedurePlayer implements IPlayerModule {
    
    
    /**
     * Instantiates a new player module.
     *
     * @param portletContext the portlet context
     */
    public ProcedurePlayer(PortletContext portletContext) {

    } 

    /**
     * Get procedure thread player.
     *
     * @param cmsContext CMS context
     * @return procedure thread player
     */
    private CMSHandlerProperties getProcedurePlayer(CMSServiceCtx cmsContext) {
        Document document = (Document) cmsContext.getDoc();

        Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put(Constants.WINDOW_PROP_URI, document.getPath());
        windowProperties.put("osivia.doctype", document.getType());
        windowProperties.put("osivia.title", getPortletTitle(document));
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put("osivia.ajaxLink", "1");

        CMSHandlerProperties linkProps = new CMSHandlerProperties();
        linkProps.setWindowProperties(windowProperties);
        linkProps.setPortletInstance("osivia-services-procedure-portletInstance");

        return linkProps;
    }

    @Override
    public CMSHandlerProperties getCMSPlayer(CMSServiceCtx ctx, ICMSService cmsService) throws CMSException {
        Document doc = (Document) ctx.getDoc();
        String docType = doc.getType();

        if ("ProcedureInstance".equals(docType) || "Procedure".equals(docType)) {
            return this.getProcedurePlayer(ctx);
        }
        
        return null;
    }
    
    /**
     * @return the portlet title.
     */
    protected String getPortletTitle(Document document){
        String title = StringUtils.EMPTY;
        
        String docType = document.getType();
        if("ProcedureInstance".equals(docType)){
            title = document.getString("pi:directive");
        } else if("Procedure".equals(docType)){
            title = document.getString("dc:title");
        }
        
        return title;
    }

}
