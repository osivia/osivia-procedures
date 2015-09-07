/**
 * 
 */
package org.osivia.services.procedure.portlets.model.service;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author david
 *
 */
public class StartProcedureCommand implements INuxeoCommand {
    
    /** Procedure's name. */
    private String name;
    
    /**
     * Constructor.
     */
    public StartProcedureCommand(String name){
        super();
        this.name = name;
    }

    @Override
    public Document execute(Session nuxeoSession) throws Exception {
        
        OperationRequest request = nuxeoSession.newRequest("Services.StartProcedure");
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("name", this.name);
        
        // Return the procedure instance; 
        return (Document) nuxeoSession.execute(request);
        
    }

    @Override
    public String getId() {
        return StartProcedureCommand.class.getCanonicalName();
    }

}
