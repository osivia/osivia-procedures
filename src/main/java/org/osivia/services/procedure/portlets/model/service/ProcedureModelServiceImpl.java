/**
 * 
 */
package org.osivia.services.procedure.portlets.model.service;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.procedure.portlets.model.ProcedureModel;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;


/**
 * @author david
 *
 */
@Service
public class ProcedureModelServiceImpl implements IProcedureModelService {

    @Override
    public Document getDocument(NuxeoController nuxeoController) throws PortletException {
        try {

            // Current window
            PortalWindow window = WindowFactory.getWindow(nuxeoController.getRequest());
            // Path window property
            String pathProperty = window.getProperty(Constants.WINDOW_PROP_URI);
            // Nuxeo document
            return nuxeoController.fetchDocument(pathProperty, true);

        } catch (Exception e) {
            throw new PortletException(e);
        }

    }

    @Override
    public ProcedureModel getProcedureModel(NuxeoController nuxeoController) throws PortletException {
        Document procedureInstance = this.getDocument(nuxeoController);
        return this.toViewObject(nuxeoController, procedureInstance);
    }

    /**
     * Convert procedure (model) Nuxeo document to view-object.
     *
     * @param document procedure Nuxeo document
     * @return view-object
     * @throws PortletException
     */
    private ProcedureModel toViewObject(NuxeoController nuxeoController, Document document) throws PortletException {
        try {

            ProcedureModel vo = new ProcedureModel();
            vo.setName(document.getString("dc:title"));
            vo.setDescription(document.getString("dc:description"));

            return vo;
        } catch (Exception e) {
            throw new PortletException(e);
        }
    }

    @Override
    public Document startProcedure(NuxeoController nuxeoController, String procedureName) throws PortletException {
        StartProcedureCommand command = new StartProcedureCommand(procedureName);
        return (Document) nuxeoController.executeNuxeoCommand(command);
    }


}
