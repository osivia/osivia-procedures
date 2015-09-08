package org.osivia.services.procedure.portlets.service;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.procedure.portlets.form.utils.FormUtils;
import org.osivia.services.procedure.portlets.model.Procedure;
import org.osivia.services.procedure.portlets.model.WidgetDefinition;
import org.osivia.services.procedure.portlets.model.proto.lmg.Form;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyEntry;

@Service
public class ProcedureServiceImpl implements IProcedureService {

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
    public Procedure getProcedure(NuxeoController nuxeoController) throws PortletException {
        Document procedureInstance = this.getDocument(nuxeoController);
        return this.toViewObject(nuxeoController, procedureInstance);
    }

    /**
     * Convert procedureInstance Nuxeo document to view-object.
     *
     * @param document ProcedureInstance Nuxeo document
     * @return view-object
     * @throws PortletException
     */
    public Procedure toViewObject(NuxeoController nuxeoController, Document document) throws PortletException {
        try {

            Procedure vo = new Procedure();

            vo.setName(document.getString("pi:name"));
            vo.setInitiator(document.getString("pi:initiator"));
            vo.setDirective(document.getString("pi:directive"));
            vo.setMessage(document.getString("pi:message"));
            vo.setCurrentStep(document.getString("pi:currentStep"));
            vo.setNextStep(document.getString("pi:nextStep"));
            vo.setFormId(document.getString("pi:formId"));

            PropertyList actorsProperty = document.getProperties().getList("pi:currentActors");
            if (actorsProperty != null) {
                String actors = FormUtils.getActors(actorsProperty);
                vo.setNextActors(actors);

                String currentUser = nuxeoController.getRequest().getRemoteUser();
                vo.setCurrentUserIsActor(StringUtils.contains(actors, currentUser));
            }

            Form form = new Form();

            PropertyMap properties = document.getProperties();
            if (properties != null) {

                PropertyMap map = properties.getMap("pi:form");

                if (map != null) {
                    PropertyMap blobProp = map.getMap("document");

                    if (blobProp != null) {
                        String fileName = (String) blobProp.get("fileName");
                        form.setDocumentName(fileName);
                        
                        String documentURL = nuxeoController.createFileLink(document.getPath(), "pi:form/document/content", fileName);
                        form.setDocumentURL(documentURL);
                    }
                    form.setComment(map.getString("comment"));
                    form.setNature(map.getString("nature"));
                    form.setAction(map.getString("action"));
                }
            }

            // TODO in other way
            VocabularyEntry vocabularyEntry = FormUtils.getVocabulary(nuxeoController, "[CNS] Nature");
            form.setVocabEntry(vocabularyEntry);

            List<WidgetDefinition> wdgtDef = new ArrayList<WidgetDefinition>(0);

            vo.setForm(form);

            return vo;
        } catch (Exception e) {
            throw new PortletException(e);
        }
    }

    @Override
    public void updateProcedure(Procedure procedure, Procedure newProcedure) throws PortletException {

        try {
            BeanUtils.copyProperties(procedure, newProcedure);
        } catch (Exception e) {
            throw new PortletException(e);
        }

    }

    @Override
    public void goToNextStep(NuxeoController nuxeoController, Document procedureInstance, Procedure procedure) throws PortletException {
        INuxeoCommand command = new GoToNextStepCommand(procedureInstance, procedure);
        nuxeoController.executeNuxeoCommand(command);
    }

}
