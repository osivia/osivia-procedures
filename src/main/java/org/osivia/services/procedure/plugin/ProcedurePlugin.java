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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.CustomizationModuleMetadatas;
import org.osivia.portal.api.customization.ICustomizationModule;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.core.cms.CMSItemType;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.IPlayerModule;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;


/**
 * Technical portlet for attributes bundles customization.
 * 
 * @author Jean-Sébastien steux
 * @see GenericPortlet
 * @see ICustomizationModule
 */
public class ProcedurePlugin extends AbstractPluginPortlet implements ICustomizationModule {

    /** Customizer name. */
    private static final String CUSTOMIZER_NAME = "procedure.plugin";

    /** Picturebook schemas. */
    public static final String SCHEMAS_PROCEDUREINSTANCE = "dublincore, procedureInstance";

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
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }

    /**
     * Utility method used to generate attributes bundles customization module metadatas.
     * 
     * @return metadatas
     */
    protected CustomizationModuleMetadatas generateMetadatas() {
        CustomizationModuleMetadatas metadatas = new CustomizationModuleMetadatas();
        metadatas.setName(CUSTOMIZER_NAME);
        metadatas.setModule(this);
        metadatas.setCustomizationIDs(Arrays.asList(ICustomizationModule.PLUGIN_ID));
        metadatas.setOrder(1020);
        return metadatas;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(String customizationID, CustomizationContext context) {

        Map<String, CMSItemType> docTypes = getDocTypes(context);

        docTypes.put("ProcedureInstance", new CMSItemType("ProcedureInstance", false, false, false, false, false, false, new ArrayList<String>(0), null,
                "glyphicons glyphicons-conversation"));
 

        List<IPlayerModule> modules = getPlayers(context);
        // ! insertion au début
        modules.add(0, new ProcedurePlayer(getPortletContext()));

    }






}
