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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.ICustomizationModule;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.player.IPlayerModule;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSExtendedDocumentInfos;
import org.osivia.portal.core.cms.CMSPublicationInfos;
import org.osivia.portal.core.cms.CMSServiceCtx;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.IMenubarModule;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;


/**
 * Technical portlet for attributes bundles customization.
 *
 * @author Jean-Sébastien steux
 * @see GenericPortlet
 * @see ICustomizationModule
 */
public class ProcedurePlugin extends AbstractPluginPortlet implements ICustomizationModule, IMenubarModule{

    /** Customizer name. */
    private static final String CUSTOMIZER_NAME = "procedure.plugin";

    /** Picturebook list template. */
    public static final String STYLE_ADMIN = "adminproc";
    /** Picturebook schemas. */
    public static final String SCHEMAS_PROCEDUREINSTANCE = "dublincore, procedure";
    /** SCHEMAS_ADMIN */
    public static final String SCHEMAS_ADMIN = "dublincore, common, toutatice";

    /** Logger. */
    private static final Log LOGGER = LogFactory.getLog(ProcedurePlugin.class);

    /** Bundle factory. */
    protected IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public ProcedurePlugin() {
        super();

    }


    @Override
    public void init() throws PortletException {
        super.init();

        // Bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(String customizationID, CustomizationContext context) {

        Map<String, DocumentType> docTypes = getDocTypes(context);

        ArrayList<String> portalFormSubTypes = new ArrayList<String>(0);
        docTypes.put("ProcedureModel", new DocumentType("ProcedureModel", false, false, false, false, false, false, portalFormSubTypes, null,
                "glyphicons glyphicons-conversation"));


        Map<String, ListTemplate> templates = getListTemplates(context);

        ListTemplate picturebookTemplate = new ListTemplate(STYLE_ADMIN, "admin téléprocédure", SCHEMAS_ADMIN);
        templates.put(STYLE_ADMIN, picturebookTemplate);


        List<IPlayerModule> modules = getPlayers(context);
        // ! insertion au début
        modules.add(0, new ProcedurePlayer(getPortletContext()));

        List<IMenubarModule> menubars = getMenubars(context);
        menubars.add(this);
    }


    @Override
    public void adaptContentMenuBar(CMSServiceCtx C, List<MenubarItem> menuBar, CMSPublicationInfos publicationInfos,
            CMSExtendedDocumentInfos extendedDocumentInfos) throws CMSException {

        String style = (String) C.getRequest().getAttribute("style");
        if (StringUtils.equals(style, "adminproc")) {
            PortalControllerContext pcc = new PortalControllerContext(getPortletContext(), C.getRequest(), C.getResponse());

            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put("osivia.title", getMessage(pcc, "CREATE_PROCEDURE_ACTION"));
            windowProperties.put("osivia.procedure.admin", style);
            try {
                String urlCreate = getPortalUrlFactory().getStartPortletUrl(pcc, "osivia-services-procedure-portletInstance", windowProperties, false);
                MenubarItem manageMembersItem = new MenubarItem("CREATE_PROCEDURE", getMessage(pcc, "CREATE_PROCEDURE_ACTION"),
                        "glyphicons glyphicons-circle-plus", MenubarGroup.SPECIFIC, 22, urlCreate, null, null, null);

                menuBar.add(manageMembersItem);

            } catch (PortalException e) {
                LOGGER.warn(e.getMessage());
            }
        }

    }


    @Override
    protected String getPluginName() {
        return CUSTOMIZER_NAME;
    }
}
