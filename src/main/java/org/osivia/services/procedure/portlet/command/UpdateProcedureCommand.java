package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


public class UpdateProcedureCommand implements INuxeoCommand {

    /** document the document to update */
    private Document document;

    /** properties to update */
    private PropertyMap properties;

    /** taskTitle */
    private String taskTitle;

    public UpdateProcedureCommand(Document document, PropertyMap properties, String taskTitle) {
        super();
        this.document = document;
        this.properties = properties;
        this.taskTitle = taskTitle;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.UpdateProcedure.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.setInput(document);
        request.set("properties", properties);
        request.set("taskTitle", taskTitle);

        return request.execute();
    }

    @Override
    public String getId() {
        return "UpdateProcedureCommand/" + document + "/" + properties + "/" + taskTitle;
    }

}
