/**
 * 
 */
package org.osivia.services.procedure.portlets.model.service;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.procedure.portlets.model.ProcedureModel;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;


/**
 * @author david
 *
 */
public interface IProcedureModelService {
    
    /**
     * @return the procedure model document.
     */
    Document getDocument(NuxeoController nuxeoController) throws PortletException;

    
    /**
     * 
     * @return the procedure model object.
     */
    ProcedureModel getProcedureModel(NuxeoController nuxeoController) throws PortletException;
    
    /**
     * Starts a procedure of given name
     * and returns the procedure instance.
     */
    Document startProcedure(NuxeoController nuxeoController, String procedureName) throws PortletException;

}
