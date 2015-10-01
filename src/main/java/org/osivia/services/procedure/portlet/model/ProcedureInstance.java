package org.osivia.services.procedure.portlet.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;


/**
 * @author dorian
 */
@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE)
public class ProcedureInstance {

    /** globalVariablesValues */
    @JsonProperty("pi:globalVariablesValues")
    private Map<String, String> globalVariablesValues;

    /** currentStep */
    @JsonProperty("pi:currentStep")
    private String currentStep;

    /** taskTitle */
    @JsonProperty("pi:taskTitle")
    private String taskTitle;

    /** procedureModelPath */
    @JsonIgnore
    private String procedureModelPath;

    public ProcedureInstance() {
        globalVariablesValues = new HashMap<String, String>();
    }

    public ProcedureInstance(String currentStep) {
        globalVariablesValues = new HashMap<String, String>();
        this.currentStep = currentStep;
    }

    public ProcedureInstance(Document document) {
        globalVariablesValues = new HashMap<String, String>();
        PropertyMap properties = document.getProperties();
        currentStep = properties.getString("pi:currentStep");
        procedureModelPath = properties.getString("pi:procedureModelPath");

        // global variables
        PropertyList gvvList = properties.getList("pi:globalVariablesValues");
        Iterator<Object> gvvIterator;
        if (gvvList != null) {
            gvvIterator = gvvList.list().iterator();
            while (gvvIterator.hasNext()) {
                PropertyMap gvv = (PropertyMap) gvvIterator.next();
                globalVariablesValues.put(gvv.getString("name"), gvv.getString("value"));
            }
        }
    }

    /**
     * Getter for globalVariablesValues.
     *
     * @return the globalVariablesValues
     */
    public Map<String, String> getGlobalVariablesValues() {
        return globalVariablesValues;
    }

    /**
     * Setter for globalVariablesValues.
     *
     * @param globalVariablesValues the globalVariablesValues to set
     */
    public void setGlobalVariablesValues(Map<String, String> globalVariablesValues) {
        this.globalVariablesValues = globalVariablesValues;
    }


    /**
     * Getter for currentStep.
     *
     * @return the currentStep
     */
    public String getCurrentStep() {
        return currentStep;
    }


    /**
     * Setter for currentStep.
     *
     * @param currentStep the currentStep to set
     */
    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }


    /**
     * Getter for taskTitle.
     *
     * @return the taskTitle
     */
    public String getTaskTitle() {
        return taskTitle;
    }


    /**
     * Setter for taskTitle.
     *
     * @param taskTitle the taskTitle to set
     */
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }


    /**
     * Getter for procedureModelPath.
     *
     * @return the procedureModelPath
     */
    public String getProcedureModelPath() {
        return procedureModelPath;
    }


    /**
     * Setter for procedureModelPath.
     *
     * @param procedureModelPath the procedureModelPath to set
     */
    public void setProcedureModelPath(String procedureModelPath) {
        this.procedureModelPath = procedureModelPath;
    }

}
