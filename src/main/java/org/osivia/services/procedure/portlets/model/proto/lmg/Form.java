/**
 * 
 */
package org.osivia.services.procedure.portlets.model.proto.lmg;

import java.util.List;

import org.osivia.services.procedure.portlets.model.Widget;
import org.osivia.services.procedure.portlets.model.WidgetDefinition;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.VocabularyEntry;


/**
 * @author david
 *
 */
public class Form {
    
    /** Attached file. */
    private MultipartFile document;
    /** Comment. */
    private String comment;
    /** Nature of document. */
    private String nature;
    /** Attachment name. */
    private String documentName;
    /** Attachment URL. */
    private String documentURL;
    /** Action to do when form is submit. */
    private String action;
    /** Vocabulary entry. */
    private VocabularyEntry vocabEntry;
    
    /** Widgets definitions of form. */
    private List<WidgetDefinition> widgetsDefinitions;
    /** Widgets values. */
    private List<Widget> widgets;
    
    /**
     * @return the document
     */
    public MultipartFile getDocument() {
        return document;
    }
    
    /**
     * @param document the document to set
     */
    public void setDocument(MultipartFile document) {
        this.document = document;
    }
    
    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }
    
    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    /**
     * @return the nature
     */
    public String getNature() {
        return nature;
    }
    
    /**
     * @param nature the nature to set
     */
    public void setNature(String nature) {
        this.nature = nature;
    }

    
    /**
     * @return the documentName
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * @param documentName the documentName to set
     */
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    /**
     * @return the documentURL
     */
    public String getDocumentURL() {
        return documentURL;
    }

    /**
     * @param documentURL the documentURL to set
     */
    public void setDocumentURL(String documentURL) {
        this.documentURL = documentURL;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the vocabEntry
     */
    public VocabularyEntry getVocabEntry() {
        return vocabEntry;
    }

    /**
     * @param vocabEntry the vocabEntry to set
     */
    public void setVocabEntry(VocabularyEntry vocabEntry) {
        this.vocabEntry = vocabEntry;
    }

    
    /**
     * @return the widgetsDefinitions
     */
    public List<WidgetDefinition> getWidgetsDefinitions() {
        return widgetsDefinitions;
    }

    /**
     * @param widgetsDefinitions the widgetsDefinitions to set
     */
    public void setWidgetsDefinitions(List<WidgetDefinition> widgetsDefinitions) {
        this.widgetsDefinitions = widgetsDefinitions;
    }
    
    /**
     * @return the widgets
     */
    public List<Widget> getWidgets() {
        return widgets;
    }

    /**
     * @param widgets the widgets to set
     */
    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }

}
