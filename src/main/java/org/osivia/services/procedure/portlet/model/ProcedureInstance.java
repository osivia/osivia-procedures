package org.osivia.services.procedure.portlet.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


/**
 * @author Dorian Licois
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
    
    /** originalDocument */
    private Document originalDocument;

    /** url */
    private String url;



    public ProcedureInstance() {
        this.globalVariablesValues = new HashMap<>();
        this.filesPath = new HashMap<>();
        this.setProcedureObjects(new HashMap<String, ProcedureObjectInstance>());
        this.procedureObjectInstances = new HashMap<String, ProcedureObjectInstance>();
    }

    public ProcedureInstance(String currentStep) {
        this();
        this.currentStep = currentStep;
    }

    public ProcedureInstance(Document document) {
        this();
        PropertyMap documentProperties = document.getProperties();
        setTaskDoc(documentProperties.getMap("pi:task"));
        this.currentStep = documentProperties.getString("pi:currentStep");
        setProcedureModelWebId(documentProperties.getString("pi:procedureModelWebId"));
        setOriginalDocument(document);

        // global variables
        PropertyMap gvvList = documentProperties.getMap("pi:globalVariablesValues");
        if (gvvList != null) {
            for (Entry<String, Object> gvvO : gvvList.getMap().entrySet()) {
                globalVariablesValues.put(gvvO.getKey(), (String) gvvO.getValue());
            }
        }

        // files
        PropertyList fileList = documentProperties.getList("pi:attachments");
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
        PropertyList objectsList = documentProperties.getList("pi:procedureObjectInstances");
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
    
    public ProcedureInstance(Map<String, String> variables) {
        this();
        globalVariablesValues = variables;
    }


    /**
     * Get variable JSON values.
     * 
     * @return JSON values
     */
    public Map<String, JSON> getJsonValues() {
        Map<String, JSON> jsonValues;
        if (MapUtils.isEmpty(this.globalVariablesValues)) {
            jsonValues = null;
        } else {
            jsonValues = new HashMap<>(this.globalVariablesValues.size());

            for (Entry<String, String> entry : this.globalVariablesValues.entrySet()) {
                String name = entry.getKey();
                String value = StringUtils.trim(entry.getValue());

                JSON json;
                try {
                    if (StringUtils.startsWith(value, "[")) {
                        json = JSONArray.fromObject(value);
                    } else if (StringUtils.startsWith(value, "{")) {
                        json = JSONObject.fromObject(value);
                    } else {
                        json = null;
                    }
                } catch (JSONException e) {
                    json = null;
                }

                if (json != null) {
                    jsonValues.put(name, json);
                }
            }
        }

        return jsonValues;
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
     * Getter for originalDocument.
     *
     * @return the originalDocument
     */
    public Document getOriginalDocument() {
        return originalDocument;
    }

    /**
     * Setter for originalDocument.
     *
     * @param originalDocument the originalDocument to set
     */
    public void setOriginalDocument(Document originalDocument) {
        this.originalDocument = originalDocument;
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
