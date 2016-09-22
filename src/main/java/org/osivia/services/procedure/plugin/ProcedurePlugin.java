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
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.player.IPlayerModule;
import org.osivia.services.procedure.formFilters.DefineVariableFilter;
import org.osivia.services.procedure.formFilters.IfFilter;
import org.osivia.services.procedure.formFilters.SendMailFilter;
import org.osivia.services.procedure.formFilters.SetVariableAsActorFilter;
import org.osivia.services.procedure.formFilters.ThrowExceptionFilter;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;


/**
 * Technical portlet for attributes bundles customization.
 *
 * @author Jean-Sébastien steux
 * @see AbstractPluginPortlet
 */
public class ProcedurePlugin extends AbstractPluginPortlet {

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


    /**
     * Constructor.
     */
    public ProcedurePlugin() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(String customizationID, CustomizationContext context) {

        Map<String, DocumentType> docTypes = getDocTypes(context);

        ArrayList<String> portalFormSubTypes = new ArrayList<String>(0);
        docTypes.put("ProcedureModel", new DocumentType("ProcedureModel", false, false, false, false, false, false, portalFormSubTypes, null,
                "glyphicons glyphicons-flowchart"));


        Map<String, ListTemplate> templates = getListTemplates(context);

        ListTemplate picturebookTemplate = new ListTemplate(STYLE_ADMIN, "admin téléprocédure", SCHEMAS_ADMIN);
        templates.put(STYLE_ADMIN, picturebookTemplate);


        List<IPlayerModule> modules = getPlayers(context);
        // ! insertion au début
        modules.add(0, new ProcedurePlayer(getPortletContext()));


        // Menubar modules
        List<MenubarModule> menubarModules = this.getMenubarModules(context);

        MenubarModule menubarModule = new ProcedureMenubarModule();
        menubarModules.add(menubarModule);


        Map<String, FormFilter> formFilters = getFormFilters(context);
        formFilters.put(IfFilter.ID, new IfFilter());
        formFilters.put(DefineVariableFilter.ID, new DefineVariableFilter());
        formFilters.put(SendMailFilter.ID, new SendMailFilter());
        formFilters.put(SetVariableAsActorFilter.ID, new SetVariableAsActorFilter());
        formFilters.put(ThrowExceptionFilter.ID, new ThrowExceptionFilter());
    }


    @Override
    protected String getPluginName() {
        return CUSTOMIZER_NAME;
    }
}
