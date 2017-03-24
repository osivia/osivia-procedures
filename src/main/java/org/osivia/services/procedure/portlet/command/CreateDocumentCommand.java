package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author Dorian Licois
 */
public class CreateDocumentCommand implements INuxeoCommand {

    /** path the location where to create the document */
    private Document path;

    /** name the name of the document to create */
    private String name;

    /** type the type of the document to create */
    private DocumentTypeEnum type;

    /** webId */
    private String webId;

    public CreateDocumentCommand(Document path, String name, String webId, DocumentTypeEnum type) {
        super();
        this.path = path;
        this.name = name;
        this.type = type;
        this.webId = webId;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.CreateDocument.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.setInput(path);
        PropertyMap properties = new PropertyMap();
        properties.set("dc:title", name);
        properties.set("ttc:webid", webId);
        request.set("type", type.getName()).set("properties", properties);

        return request.execute();
    }

    @Override
    public String getId() {
        return "CreateDocumentCommand/" + path + "/" + type + "/" + name + "/" + webId;
    };


}
