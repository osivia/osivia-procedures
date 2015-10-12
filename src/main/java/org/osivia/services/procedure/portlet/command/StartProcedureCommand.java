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
    private String taskTitle;

    /** properties */
    private PropertyMap properties;

    /** blobs */
    private Blobs blobs;

    /**
     * Constructor.
     */
    public StartProcedureCommand(String taskTitle, PropertyMap properties) {
        super();
        this.taskTitle = taskTitle;
        this.properties = properties;
    }

    public StartProcedureCommand(String taskTitle, PropertyMap properties, Blobs blobs) {
        super();
        this.taskTitle = taskTitle;
        this.properties = properties;
        this.blobs = blobs;
    }

    @Override
    public Document execute(Session nuxeoSession) throws Exception {

        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.StartProcedure.getId());
        request.set("taskTitle", taskTitle);
        request.set("properties", properties);
        if (blobs != null) {
            request.setInput(blobs);
        }

        return (Document) nuxeoSession.execute(request);
    }

    @Override
    public String getId() {
        return "StartProcedureCommand/" + taskTitle + "/" + properties;
    }

}
