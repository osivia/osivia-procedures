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
    private final Document document;

    /** properties to update */
    private final PropertyMap properties;

    /** taskTitle */
    private final String taskTitle;

    /** groups */
    private final String groups;

    /** users */
    private final String users;

    public UpdateProcedureCommand(Document document, PropertyMap properties, String taskTitle, String groups, String users) {
        super();
        this.document = document;
        this.properties = properties;
        this.taskTitle = taskTitle;
        this.groups = groups;
        this.users = users;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        final OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.UpdateProcedure.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.setInput(document);
        request.set("properties", properties);
        request.set("taskTitle", taskTitle);
        request.set("groups", groups);
        request.set("users", users);
        return request.execute();
    }

    @Override
    public String getId() {
        return "UpdateProcedureCommand/" + document + "/" + taskTitle + "/" + groups + "/" + users + "/" + properties;
    }

}
