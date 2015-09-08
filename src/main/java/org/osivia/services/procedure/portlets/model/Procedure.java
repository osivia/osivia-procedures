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
    /** Initiator of procedure. */
    private String initiator;
    /** Directive of current step. */
    private String directive;
    /** Message. */
    private String message;
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
     * @return the initiator
     */
    public String getInitiator() {
        return initiator;
    }

    /**
     * @param initiator the initiator to set
     */
    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    /**
     * @return the directive
     */
    public String getDirective() {
        return directive;
    }

    /**
     * @param directive the directive to set
     */
    public void setDirective(String directive) {
        this.directive = directive;
    }
    
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
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
