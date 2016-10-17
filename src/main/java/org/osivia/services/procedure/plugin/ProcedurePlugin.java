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

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.player.IPlayerModule;
import org.osivia.services.procedure.formFilters.DefineVariableFilter;
import org.osivia.services.procedure.formFilters.DeleteOnEndingFormFilter;
import org.osivia.services.procedure.formFilters.IfFilter;
import org.osivia.services.procedure.formFilters.SendMailFilter;
import org.osivia.services.procedure.formFilters.SetActorFormFilter;
import org.osivia.services.procedure.formFilters.TestBooleanFilter;
import org.osivia.services.procedure.formFilters.ThrowExceptionFilter;
import org.osivia.services.procedure.module.ListProcListModule;

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

    /** proclistadminlist list template. */
    // public static final String STYLE_VIEW_LIST_ADMIN = "proclistadminlist";

    /** viewListProc list template. */
    public static final String STYLE_VIEW_LISTPROC = "listproc";

    /** Picturebook schemas. */
    public static final String SCHEMAS_PROCEDUREINSTANCE = "dublincore, common, toutatice, procedureInstance";
    /** SCHEMAS_ADMIN */
    public static final String SCHEMAS_ADMIN = "dublincore, common, toutatice";


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
        updateDocTypes(context);
        updateListTemplates(context);
        updatePlayers(context);
        updateFormFIlters(context);
    }


    /**
     * update players
     * 
     * @param context
     */
    @SuppressWarnings("rawtypes")
    private void updatePlayers(CustomizationContext context) {
        List<IPlayerModule> modules = getPlayers(context);
        // ! insertion au début
        modules.add(0, new ProcedurePlayer(getPortletContext()));
    }


    /**
     * update document types
     * 
     * @param context
     */
    private void updateDocTypes(CustomizationContext context) {
        Map<String, DocumentType> docTypes = getDocTypes(context);

        ArrayList<String> portalFormSubTypes = new ArrayList<String>(0);
        docTypes.put("ProcedureModel", new DocumentType("ProcedureModel", false, false, false, false, false, false, portalFormSubTypes, null,
                "glyphicons glyphicons-flowchart"));
    }

    /**
     * Update list templates.
     *
     * @param context customization context
     */
    private void updateListTemplates(CustomizationContext context) {
        Map<String, ListTemplate> templates = getListTemplates(context);

        // ListTemplate proclistadminlist = new ListTemplate(STYLE_VIEW_LIST_ADMIN, "liste de téléprocédure - adminlist", SCHEMAS_ADMIN);
        // templates.put(STYLE_VIEW_LIST_ADMIN, proclistadminlist);
        
        ListTemplate viewListProc = new ListTemplate(STYLE_VIEW_LISTPROC, "procédure LIST", SCHEMAS_PROCEDUREINSTANCE);
        viewListProc.setModule(new ListProcListModule(getPortletContext()));
        templates.put(STYLE_VIEW_LISTPROC, viewListProc);
    }

    /**
     * update form filters
     * 
     * @param context
     */
    private void updateFormFIlters(CustomizationContext context) {
        Map<String, FormFilter> formFilters = getFormFilters(context);

        formFilters.put(IfFilter.ID, new IfFilter());
        formFilters.put(DefineVariableFilter.ID, new DefineVariableFilter());
        formFilters.put(SendMailFilter.ID, new SendMailFilter());
        formFilters.put(SetActorFormFilter.ID, new SetActorFormFilter());
        formFilters.put(ThrowExceptionFilter.ID, new ThrowExceptionFilter());
        formFilters.put(DeleteOnEndingFormFilter.ID, new DeleteOnEndingFormFilter());
        formFilters.put(TestBooleanFilter.ID, new TestBooleanFilter());
    }

    @Override
    protected String getPluginName() {
        return CUSTOMIZER_NAME;
    }
}
