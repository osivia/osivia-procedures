package org.osivia.services.procedure.portlets.service;

import javax.portlet.PortletException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.procedure.portlets.form.utils.FormUtils;
import org.osivia.services.procedure.portlets.model.Procedure;
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
            vo.setCurrentStep(document.getString("pi:currentStep"));
            vo.setNextStep(document.getString("pi:nextStep"));
            vo.setFormId(document.getString("pi:formId"));
            
            PropertyList actorsProperty = document.getProperties().getList("pi:currentActors");
            if(actorsProperty != null){
                String actors = FormUtils.getActors(actorsProperty);
                vo.setNextActors(actors);
                    
                String currentUser = nuxeoController.getRequest().getRemoteUser();
                vo.setCurrentUserIsActor(StringUtils.contains(actors, currentUser));
            }
            
            Form form = new Form();
            
            Blob blob = document.getProperties().getBlob("pi:form/document");
            if (blob != null) {
                form.setDocumentName(blob.getFileName());
                String documentURL = nuxeoController.createFileLink(document.getPath(), "pi:form/document", blob.getFileName());
                form.setDocumentURL(documentURL);
            }
            form.setComment(document.getString("pi:form/comment"));
            form.setNature(document.getString("pi:form/nature"));
            form.setAction(document.getString("pi:form/action"));
            // TODO in other way
            VocabularyEntry vocabularyEntry = FormUtils.getVocabulary(nuxeoController, "[CNS] Nature");
            form.setVocabEntry(vocabularyEntry);
            
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
        // procedure.setName(newProcedure.getName());
        // procedure.setCurrentStep(newProcedure.getCurrentStep());
        // procedure.setNextStep(newProcedure.getNextStep());
        // procedure.setNextActors(newProcedure.getNextActors());
        // procedure.setCurrentUserIsActor(newProcedure.getCurrentUserIsActor());
        // procedure.setFormId(newProcedure.getFormId());

    }

    @Override
    public void goToNextStep(NuxeoController nuxeoController, Document procedureInstance, Procedure procedure) throws PortletException {
        INuxeoCommand command = new GoToNextStepCommand(procedureInstance, procedure);
        nuxeoController.executeNuxeoCommand(command);
    }
    
}
