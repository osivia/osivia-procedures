package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author Dorian Licois
 */
public class UpdateDocumentFromBlobCommand implements INuxeoCommand {

    /** path */
    private String path;

    /** properties */
    private PropertyMap properties;

    /** blob */
    private Blob blob;

    /**
     * @param path
     * @param properties
     * @param blob
     */
    public UpdateDocumentFromBlobCommand(String path, PropertyMap properties, Blob blob) {
        this.path = path;
        this.properties = properties;
        this.blob = blob;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.UpdateDocumentFromBlob.getId());
        request.setInput(blob);
        request.set("path", path);
        request.set("properties", properties);

        return request.execute();
    }

    @Override
    public String getId() {
        return "UpdateDocumentFromBlobCommand/" + path + "/" + properties + "/" + blob;
    }

}
