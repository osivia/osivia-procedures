/**
 *
 */
package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Blobs;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author david
 *
 */
public class StartProcedureCommand implements INuxeoCommand {

    /** taskTitle */
    private final String taskTitle;

    /** groups */
    private final String groups;

    /** users */
    private final String users;

    /** properties */
    private final PropertyMap properties;

    /** blobs */
    private Blobs blobs;

    /**
     * Constructor.
     */
    public StartProcedureCommand(String taskTitle, String groups, String users, PropertyMap properties) {
        super();
        this.taskTitle = taskTitle;
        this.groups = groups;
        this.users = users;
        this.properties = properties;
    }

    public StartProcedureCommand(String taskTitle, String groups, String users, PropertyMap properties, Blobs blobs) {
        super();
        this.taskTitle = taskTitle;
        this.groups = groups;
        this.users = users;
        this.properties = properties;
        this.blobs = blobs;
    }

    @Override
    public Document execute(Session nuxeoSession) throws Exception {

        final OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.StartProcedure.getId());
        request.set("taskTitle", taskTitle);
        request.set("properties", properties);
        request.set("groups", groups);
        request.set("users", users);
        if (blobs != null) {
            request.setInput(blobs);
        }

        return (Document) nuxeoSession.execute(request);
    }

    @Override
    public String getId() {
        return "StartProcedureCommand/" + taskTitle + "/" + groups + "/" + users + "/" + properties;
    }

}
