package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    /** procedureModelPath */
    private String procedureModelPath;

    /** filesPath */
    private List<FilePath> filesPath;

    public ProcedureInstance() {
        globalVariablesValues = new HashMap<String, String>();
        filesPath = new ArrayList<FilePath>();
    }

    public ProcedureInstance(String currentStep) {
        globalVariablesValues = new HashMap<String, String>();
        filesPath = new ArrayList<FilePath>();
        this.currentStep = currentStep;
    }

    public ProcedureInstance(Document document) {
        globalVariablesValues = new HashMap<String, String>();
        filesPath = new ArrayList<FilePath>();
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

        // files
        PropertyList fileList = properties.getList("pi:files");
        Iterator<Object> fileListIterator;
        if (fileList != null) {
            fileListIterator = fileList.list().iterator();
            FilePath filePath;
            while (fileListIterator.hasNext()) {
                PropertyMap file = (PropertyMap) fileListIterator.next();
                filePath = new FilePath();
                filePath.setVariableName(file.getString("variableName"));
                filePath.setFileName(file.getString("fileName"));
                filesPath.add(filePath);
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


    /**
     * Getter for filesPath.
     *
     * @return the filesPath
     */
    public List<FilePath> getFilesPath() {
        return filesPath;
    }


    /**
     * Setter for filesPath.
     *
     * @param filesPath the filesPath to set
     */
    public void setFilesPath(List<FilePath> filesPath) {
        this.filesPath = filesPath;
    }
}
