/**
 * 
 */
package org.osivia.services.procedure.portlets.model;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.procedure.portlets.model.proto.lmg.Form;



/**
 * @author david
 *
 */
public class Procedure {
    
    /** Name of procedure. */
    private String name;
    /** Current step of procedure. */
    private String currentStep;
    /** Next step of procedure. */
    private String nextStep;
    /** Actors. */
    private String nextActors;
    /** Current user is actor. */
    private boolean currentUserIsActor;
    /** Form to show. */
    private String formId;
    /** Form. */
    private Form form;
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the currentStep
     */
    public String getCurrentStep() {
        return currentStep;
    }
    
    /**
     * @param currentStep the currentStep to set
     */
    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    /**
     * @return the nextStep
     */
    public String getNextStep() {
        return nextStep;
    }

    /**
     * @param nextStep the nextStep to set
     */
    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    /**
     * @return the nextActors
     */
    public String getNextActors() {
        return nextActors;
    }

    /**
     * @param nextActors the nextActors to set
     */
    public void setNextActors(String nextActors) {
        this.nextActors = nextActors;
    }

    /**
     * @return the currentUserIsActor
     */
    public boolean getCurrentUserIsActor() {
        return currentUserIsActor;
    }

    /**
     * @param currentUserIsActor the currentUserIsActor to set
     */
    public void setCurrentUserIsActor(boolean currentUserIsActor) {
        this.currentUserIsActor = currentUserIsActor;
    }

    /**
     * @return the formId
     */
    public String getFormId() {
        return formId;
    }
    
    /**
     * @param formId the formId to set
     */
    public void setFormId(String formId) {
        this.formId = formId;
    }

    /**
     * @return the form
     */
    public Form getForm() {
        return form;
    }

    /**
     * @param form the form to set
     */
    public void setForm(Form form) {
        this.form = form;
    }
    
}
