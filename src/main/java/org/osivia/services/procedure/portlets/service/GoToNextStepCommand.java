/**
 * 
 */
package org.osivia.services.procedure.portlets.service;

import java.io.IOException;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.procedure.portlets.form.utils.FormUtils;
import org.osivia.services.procedure.portlets.model.Procedure;
import org.osivia.services.procedure.portlets.model.proto.lmg.Form;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author david
 *
 */
public class GoToNextStepCommand implements INuxeoCommand {
    
    /** Procedure instance. */
    private Document procedureInstance;
    /** Procedure Object. */
    private Procedure procedure;
    
    public GoToNextStepCommand(Document procedureInstance, Procedure procedure){
        super();
        this.procedureInstance = procedureInstance;
        this.procedure = procedure;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        
        this.procedureInstance = updateFormProperties(nuxeoSession, this.procedureInstance, this.procedure);
        
        OperationRequest request = nuxeoSession.newRequest("Services.GoToNextStep");
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.setInput(this.procedureInstance);
        
        nuxeoSession.execute(request);
        
        return null;
    }

    @Override
    public String getId() {
        return GoToNextStepCommand.class.getCanonicalName();
    }
    
    /**
     * @param pi
     * @param p
     * @return procedureInstance updated with forms properties.
     * @throws Exception 
     */
    protected Document updateFormProperties(Session nuxeoSession, Document pi, Procedure p) throws Exception{
        // Update blob
        DocRef piRef = Document.newRef(pi.getId());
        DocumentService service = nuxeoSession.getAdapter(DocumentService.class);
        
        MultipartFile mpFile = p.getForm().getDocument();
        if(mpFile != null){
            Blob blob = FormUtils.adaptFile(mpFile);
            service.setBlob(piRef, blob, "pi:form/document/content");
            service.setProperty(piRef, "pi:form/document/fileName", blob.getFileName());
        }
        pi = service.getDocument(piRef);
        
        // Update other properties
        Form form = p.getForm();
        service.setProperty(piRef, "pi:form/comment", form.getComment());
        service.setProperty(piRef, "pi:form/action", form.getAction());
        service.setProperty(piRef, "pi:form/nature", form.getNature());
        
        return pi;
    }

}
