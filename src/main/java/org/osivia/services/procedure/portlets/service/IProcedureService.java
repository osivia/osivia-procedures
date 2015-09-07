/**
 * 
 */
package org.osivia.services.procedure.portlets.service;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.procedure.portlets.model.Procedure;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;


/**
 * @author david
 *
 */
public interface IProcedureService {
    
    /**
     * @return the procedure document.
     */
    Document getDocument(NuxeoController nuxeoController) throws PortletException;
    
    /**
     * 
     * @return the procedure object.
     */
    Procedure getProcedure(NuxeoController nuxeoController) throws PortletException;
    
    /**
     * Go to next step of procedure.
     * 
     * @param nuxeoController
     * @throws PortletException
     */
    void goToNextStep(NuxeoController nuxeoController, Document procedureInstance, Procedure procedure) throws PortletException;
    
    /**
     * Update Procedure model.
     * @param procedure
     * @throws PortletException
     */
    public void updateProcedure(Procedure procedure, Procedure newProcedure) throws PortletException;
    
}
