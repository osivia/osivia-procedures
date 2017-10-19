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

import java.util.List;
import java.util.Map;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.player.IPlayerModule;
import org.osivia.services.procedure.formFilters.AddActorFormFilter;
import org.osivia.services.procedure.formFilters.CreateRecordFilter;
import org.osivia.services.procedure.formFilters.DefineVariableFilter;
import org.osivia.services.procedure.formFilters.DeleteOnEndingFormFilter;
import org.osivia.services.procedure.formFilters.DeleteRecordFilter;
import org.osivia.services.procedure.formFilters.EditRecordFilter;
import org.osivia.services.procedure.formFilters.IfFilter;
import org.osivia.services.procedure.formFilters.SendMailFilter;
import org.osivia.services.procedure.formFilters.SetActorFormFilter;
import org.osivia.services.procedure.formFilters.SetAdditionalAuthorization;
import org.osivia.services.procedure.formFilters.SetInitiatorVariableFilter;
import org.osivia.services.procedure.formFilters.ThrowExceptionFilter;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;


/**
 * customization module for procedures
 *
 * @author Dorian Licois
 * @see AbstractPluginPortlet
 */
public class ProcedurePlugin extends AbstractPluginPortlet {

    /** Customizer name. */
    private static final String CUSTOMIZER_NAME = "procedure.plugin";

    /** STYLE_VIEW_CONTAINER */
    public static final String STYLE_VIEW_CONTAINER = "containerlist";

    /** STYLE_ADMIN */
    public static final String STYLE_ADMIN = "adminproc";

    /** Picturebook schemas. */
    public static final String SCHEMAS_PROCEDUREINSTANCE = "dublincore, common, toutatice, procedure, procedureInstance";
    /** SCHEMAS_ADMIN */
    public static final String SCHEMAS_ADMIN = "dublincore, common, toutatice";

    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;

    /**
     * Constructor.
     */
    public ProcedurePlugin() {
        super();

        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(CustomizationContext context) {
        updateDocTypes(context);
        updatePlayers(context);
        updateFormFilters(context);
        updateListTemplates(context);
        customizeMenubarModules(context);
    }


    private void updateListTemplates(CustomizationContext context) {
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(context.getLocale());

        Map<String, ListTemplate> listTemplates = getListTemplates(context);

        // containerList
        ListTemplate container = new ListTemplate(STYLE_VIEW_CONTAINER, bundle.getString("LIST_TEMPLATE_CONTAINER"), SCHEMAS_ADMIN);
        listTemplates.put(container.getKey(), container);
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

        DocumentType recordContainer = DocumentType.createNode("RecordContainer");
        recordContainer.addSubtypes("RecordFolder");
        recordContainer.setIcon("glyphicons glyphicons-flowchart");
        recordContainer.setNavigable(true);
        recordContainer.setPreventedCreation(true);
        docTypes.put(recordContainer.getName(), recordContainer);

        DocumentType recordFolder = DocumentType.createNode("RecordFolder");
        recordFolder.addSubtypes("Record");
        recordFolder.setIcon("glyphicons glyphicons-list-alt");
        recordFolder.setNavigable(true);
        recordFolder.setPreventedCreation(true);
        docTypes.put(recordFolder.getName(), recordFolder);

        DocumentType record = DocumentType.createLeaf("Record");
        record.setIcon("glyphicons glyphicons-list");
        docTypes.put(record.getName(), record);

        DocumentType tasks = DocumentType.createLeaf("TaskDoc");
        docTypes.put(tasks.getName(), tasks);
    }

    /**
     * update form filters
     * 
     * @param context
     */
    private void updateFormFilters(CustomizationContext context) {
        Map<String, FormFilter> formFilters = getFormFilters(context);

        formFilters.put(IfFilter.ID, new IfFilter());
        formFilters.put(DefineVariableFilter.ID, new DefineVariableFilter());
        formFilters.put(SendMailFilter.ID, new SendMailFilter());
        formFilters.put(SetActorFormFilter.ID, new SetActorFormFilter());
        formFilters.put(AddActorFormFilter.ID, new AddActorFormFilter());
        formFilters.put(ThrowExceptionFilter.ID, new ThrowExceptionFilter());
        formFilters.put(DeleteOnEndingFormFilter.ID, new DeleteOnEndingFormFilter());
        formFilters.put(SetAdditionalAuthorization.ID, new SetAdditionalAuthorization());
    	formFilters.put(SetInitiatorVariableFilter.ID, new SetInitiatorVariableFilter());
        formFilters.put(CreateRecordFilter.ID, new CreateRecordFilter());
        formFilters.put(EditRecordFilter.ID, new EditRecordFilter());
        formFilters.put(DeleteRecordFilter.ID, new DeleteRecordFilter());
    }

    @Override
    protected String getPluginName() {
        return CUSTOMIZER_NAME;
    }

    /**
     * customizeMenubarModules
     * 
     * @param context
     */
    private void customizeMenubarModules(CustomizationContext context) {
        // Menubar modules
        List<MenubarModule> modules = this.getMenubarModules(context);

        MenubarModule mbModule = new ProcedureMenubarModule();
        modules.add(mbModule);
    }
}
