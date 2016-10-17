package org.osivia.services.procedure.portlet.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;


/**
 * @author dorian
 */
public class ProcedureInstance {

    /** globalVariablesValues */
    private Map<String, String> globalVariablesValues;

    /** currentStep */
    private String currentStep;

    /** taskTitle */
    private String taskTitle;

    /** procedureModelWebId */
    private String procedureModelWebId;

    /** filesPath */
    private Map<String, FilePath> filesPath;

    /** procedureObjectInstances */
    private Map<String, ProcedureObjectInstance> procedureObjectInstances;

    /** le document task */
    private PropertyMap taskDoc;

    /** url */
    private String url;

    public ProcedureInstance() {
        globalVariablesValues = new HashMap<String, String>();
        filesPath = new HashMap<String, FilePath>();
        setProcedureObjects(new HashMap<String, ProcedureObjectInstance>());
        procedureObjectInstances = new HashMap<String, ProcedureObjectInstance>();
    }

    public ProcedureInstance(String currentStep) {
        globalVariablesValues = new HashMap<String, String>();
        filesPath = new HashMap<String, FilePath>();
        setProcedureObjects(new HashMap<String, ProcedureObjectInstance>());
        this.currentStep = currentStep;
        procedureObjectInstances = new HashMap<String, ProcedureObjectInstance>();
    }

    public ProcedureInstance(Document document) {
        globalVariablesValues = new HashMap<String, String>();
        filesPath = new HashMap<String, FilePath>();
        procedureObjectInstances = new HashMap<String, ProcedureObjectInstance>();
        setProcedureObjects(new HashMap<String, ProcedureObjectInstance>());
        PropertyMap properties = document.getProperties();
        setTaskDoc(properties.getMap("pi:task"));
        currentStep = properties.getString("pi:currentStep");
        setProcedureModelWebId(properties.getString("pi:procedureModelWebId"));

        // global variables
        PropertyMap gvvList = properties.getMap("pi:globalVariablesValues");
        if (gvvList != null) {
            for (Entry<String, Object> gvvO : gvvList.getMap().entrySet()) {
                globalVariablesValues.put(gvvO.getKey(), (String) gvvO.getValue());
            }
        }

        // files
        PropertyList fileList = properties.getList("pi:attachments");
        if (fileList != null) {
            FilePath filePath;
            for (Object fileO : fileList.list()) {
                PropertyMap file = (PropertyMap) fileO;
                filePath = new FilePath();
                filePath.setVariableName(file.getString("variableName"));
                filePath.setFileName(file.getString("fileName"));
                filesPath.put(filePath.getVariableName(), filePath);
            }
        }

        // objects
        PropertyList objectsList = properties.getList("pi:procedureObjectInstances");
        if (objectsList != null) {
            ProcedureObjectInstance procedureObjectInstance;
            for (Object procedureObjectInstanceO : objectsList.list()) {
                PropertyMap procedureObjectM = (PropertyMap) procedureObjectInstanceO;
                procedureObjectInstance = new ProcedureObjectInstance();
                procedureObjectInstance.setName(procedureObjectM.getString("name"));
                procedureObjectInstance.setProcedureObjectid(procedureObjectM.getString("procedureObjectId"));
                procedureObjectInstances.put(procedureObjectInstance.getName(), procedureObjectInstance);
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
     * Getter for filesPath.
     *
     * @return the filesPath
     */
    public Map<String, FilePath> getFilesPath() {
        return filesPath;
    }


    /**
     * Setter for filesPath.
     *
     * @param filesPath the filesPath to set
     */
    public void setFilesPath(Map<String, FilePath> filesPath) {
        this.filesPath = filesPath;
    }

    /**
     * Getter for procedureObjects.
     * @return the procedureObjects
     */
    public Map<String, ProcedureObjectInstance> getProcedureObjects() {
        return procedureObjectInstances;
    }

    /**
     * Setter for procedureObjects.
     * @param procedureObjects the procedureObjects to set
     */
    public void setProcedureObjects(Map<String, ProcedureObjectInstance> procedureObjectInstancess) {
        procedureObjectInstances = procedureObjectInstancess;
    }


    /**
     * Getter for procedureObjectInstances.
     *
     * @return the procedureObjectInstances
     */
    public Map<String, ProcedureObjectInstance> getProcedureObjectInstances() {
        return procedureObjectInstances;
    }


    /**
     * Setter for procedureObjectInstances.
     *
     * @param procedureObjectInstances the procedureObjectInstances to set
     */
    public void setProcedureObjectInstances(Map<String, ProcedureObjectInstance> procedureObjectInstances) {
        this.procedureObjectInstances = procedureObjectInstances;
    }

    /**
     * Getter for taskDoc.
     *
     * @return the taskDoc
     */
    public PropertyMap getTaskDoc() {
        return taskDoc;
    }

    /**
     * Setter for taskDoc.
     *
     * @param taskDoc the taskDoc to set
     */
    public void setTaskDoc(PropertyMap taskDoc) {
        this.taskDoc = taskDoc;
    }

    /**
     * Getter for procedureModelWebId.
     * @return the procedureModelWebId
     */
    public String getProcedureModelWebId() {
        return procedureModelWebId;
    }

    /**
     * Setter for procedureModelWebId.
     * @param procedureModelWebId the procedureModelWebId to set
     */
    public void setProcedureModelWebId(String procedureModelWebId) {
        this.procedureModelWebId = procedureModelWebId;
    }

    /**
     * Getter for url.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for url.
     * 
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
