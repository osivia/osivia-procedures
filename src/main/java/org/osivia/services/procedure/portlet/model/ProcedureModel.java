package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.procedure.portlet.command.RetrieveDocumentByWebIdCommand;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;

/**
 * @author Dorian Licois
 */
public class ProcedureModel {

    /** the name of the procedure */
    private String name;

    /** description of the procedure */
    private String description;

    /** the global variables for the procedure */
    private Map<String, Variable> variables;

    /** the ordered list of steps in the procedure */
    private List<Step> steps;

    /** procedureObjects */
    private List<ProcedureObject> procedureObjects;

    /** dashboards */
    private List<Dashboard> dashboards;

    /** startingStep */
    private String startingStep;

    /** path of the document */
    private String path;

    /** url */
    private String url;

    /** webId */
    private String currentWebId;

    /** newWebId */
    private String newWebId;

    /** webIdParent */
    private String webIdParent;

    /** procedureType */
    private String procedureType;

    /** originalDocument */
    private Document originalDocument;

    /** ProcedureParent */
    private ProcedureModel ProcedureParent;

    public ProcedureModel() {
        variables = new HashMap<String, Variable>();
        steps = new ArrayList<Step>();
        procedureObjects = new ArrayList<ProcedureObject>();
    }

    public ProcedureModel(Document document, NuxeoController nuxeoController) {
        variables = new HashMap<String, Variable>();
        steps = new ArrayList<Step>();
        procedureObjects = new ArrayList<ProcedureObject>();

        setOriginalDocument(document);

        final PropertyMap properties = document.getProperties();
        name = properties.getString("dc:title");
        currentWebId = properties.getString("ttc:webid");
        newWebId = StringUtils.startsWith(currentWebId, IFormsService.FORMS_WEB_ID_PREFIX) ? StringUtils.removeStart(currentWebId,
                IFormsService.FORMS_WEB_ID_PREFIX) : null;
        path = document.getPath();
        startingStep = properties.getString("pcd:startingStep");
        procedureType = document.getType();
        webIdParent = properties.getString("pcd:webIdParent");

        // global variables
        final PropertyList globalVariablesList = properties.getList("pcd:globalVariablesDefinitions");
        if (globalVariablesList != null) {
            Variable var;
            for (final Object globalVariableO : globalVariablesList.list()) {
                final PropertyMap globalVariable = (PropertyMap) globalVariableO;

                Object varOptionsO = globalVariable.get("varOptions");
                String varOptions = null;
                if ((varOptionsO != null) && (varOptionsO.getClass() == String.class)) {
                    varOptions = varOptionsO.toString();
                }

                var = new Variable(globalVariable.getString("name"), globalVariable.getString("label"), VariableTypesAllEnum.valueOf(StringUtils
                        .defaultIfBlank(globalVariable.getString("type"), VariableTypesAllEnum.TEXT.name())), varOptions);
                getVariables().put(var.getName(), var);
            }
        }
        // steps
        final PropertyList stepsList = properties.getList("pcd:steps");
        if (stepsList != null) {
            Step step;
            for (final Object stepO : stepsList.list()) {
                final PropertyMap stepM = (PropertyMap) stepO;
                step = new Step(stepM, getVariables(), nuxeoController);
                getSteps().add(step);
            }
            Collections.sort(getSteps());
        }
        final PropertyList procedureObjectsList = properties.getList("pcd:procedureObjects");
        if (procedureObjectsList != null) {
            ProcedureObject newProcedureObject;
            for (final Object procedureObject : procedureObjectsList.list()) {
                final PropertyMap procedureObjectMap = (PropertyMap) procedureObject;
                newProcedureObject = new ProcedureObject(procedureObjectMap);
                procedureObjects.add(newProcedureObject);
            }
        }

        final PropertyList procedureDashboardsList = properties.getList("pcd:dashboards");
        if (procedureDashboardsList != null) {
            Dashboard dashboard;
            for (final Object dashboardObject : procedureDashboardsList.list()) {
                final PropertyMap dashboardObjectMap = (PropertyMap) dashboardObject;
                dashboard = new Dashboard(dashboardObjectMap);
                getDashboards().add(dashboard);
            }
        }

        if (StringUtils.isNotBlank(getWebIdParent())) {
            INuxeoCommand command = new RetrieveDocumentByWebIdCommand(getWebIdParent());
            Document documentParent = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
            ProcedureModel procedureParent = new ProcedureModel(documentParent, nuxeoController);
            setProcedureParent(procedureParent);
        }
    }

    public void updateStepsIndexes() {
        Collections.sort(getSteps());
        for (int i = 0; i < getSteps().size(); i++) {
            getSteps().get(i).setIndex(i);
        }
    }

    /**
     * Getter for variables.
     *
     * @return the variables
     */
    public Map<String, Variable> getVariables() {
        return variables;
    }

    /**
     * Setter for variables.
     *
     * @param variables the variables to set
     */
    public void setVariables(Map<String, Variable> variables) {
        this.variables = variables;
    }

    /**
     * Getter for steps.
     *
     * @return the steps
     */
    public List<Step> getSteps() {
        return steps;
    }

    /**
     * Setter for steps.
     *
     * @param steps the steps to set
     */
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    /**
     * Getter for description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Setter for path.
     *
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Getter for startingStep.
     *
     * @return the startingStep
     */
    public String getStartingStep() {
        return startingStep;
    }

    /**
     * Setter for startingStep.
     *
     * @param startingStep the startingStep to set
     */
    public void setStartingStep(String startingStep) {
        this.startingStep = startingStep;
    }

    /**
     * Getter for procedureObjects.
     * @return the procedureObjects
     */
    public List<ProcedureObject> getProcedureObjects() {
        return procedureObjects;
    }

    public ProcedureObject getProcedureObject(String objectName) {
        for (final ProcedureObject procedureObject : procedureObjects) {
            if (StringUtils.equals(procedureObject.getName(), objectName)) {
                return procedureObject;
            }
        }
        return null;
    }

    /**
     * Setter for procedureObjects.
     * @param procedureObjects the procedureObjects to set
     */
    public void setProcedureObjects(List<ProcedureObject> procedureObjects) {
        this.procedureObjects = procedureObjects;
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
     * Getter for currentWebId.
     *
     * @return the currentWebId
     */
    public String getCurrentWebId() {
        return currentWebId;
    }


    /**
     * Setter for currentWebId.
     *
     * @param currentWebId the currentWebId to set
     */
    public void setCurrentWebId(String currentWebId) {
        this.currentWebId = currentWebId;
    }


    /**
     * Getter for newWebId.
     *
     * @return the newWebId
     */
    public String getNewWebId() {
        return newWebId;
    }


    /**
     * Setter for newWebId.
     *
     * @param newWebId the newWebId to set
     */
    public void setNewWebId(String newWebId) {
        this.newWebId = newWebId;
    }

    /**
     * Getter for procedureType.
     * 
     * @return the procedureType
     */
    public String getProcedureType() {
        return procedureType;
    }

    /**
     * Setter for procedureType.
     * 
     * @param procedureType the procedureType to set
     */
    public void setProcedureType(String procedureType) {
        this.procedureType = procedureType;
    }

    /**
     * Getter for webIdParent.
     * 
     * @return the webIdParent
     */
    public String getWebIdParent() {
        return webIdParent;
    }

    /**
     * Setter for webIdParent.
     * 
     * @param webIdParent the webIdParent to set
     */
    public void setWebIdParent(String webIdParent) {
        this.webIdParent = webIdParent;
    }

    /**
     * Getter for dashboards.
     * 
     * @return the dashboards
     */
    public List<Dashboard> getDashboards() {
        if (dashboards == null) {
            dashboards = new ArrayList<Dashboard>();
        }
        return dashboards;
    }


    /**
     * Setter for dashboards.
     * 
     * @param dashboards the dashboards to set
     */
    public void setDashboards(List<Dashboard> dashboards) {
        this.dashboards = dashboards;
    }

    /**
     * Getter for ProcedureParent.
     * @return the procedureParent
     */
    public ProcedureModel getProcedureParent() {
        return ProcedureParent;
    }

    /**
     * Setter for ProcedureParent.
     * @param procedureParent the procedureParent to set
     */
    public void setProcedureParent(ProcedureModel procedureParent) {
        ProcedureParent = procedureParent;
    }

    public void updateVariables() {
        if (StringUtils.equals(DocumentTypeEnum.RECORDFOLDER.getDocType(), getProcedureType())) {
            Map<String, Variable> variablesU = new HashMap<String, Variable>();
            for (Step step : steps) {
                for (Field field : step.getFieldsSet()) {
                    variablesU.put(field.getName(), getVariables().get(field.getName()));
                }
            }
            setVariables(variablesU);
        }
    }

}
