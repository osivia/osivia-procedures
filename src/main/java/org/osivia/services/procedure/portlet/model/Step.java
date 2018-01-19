package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * @author Dorian Licois
 */
@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
creatorVisibility = Visibility.NONE)
public class Step implements Comparable<Step> {

    private static final String DEFAULT_NOTIFICATION_MSG = "${document:linkWithText(taskPath,taskName)}";

    /** stepName */
    @JsonProperty("name")
    private String stepName;

    /** fields */
    @JsonIgnore
    private List<Field> fields;

    @JsonProperty("globalVariablesReferences")
    private Set<Field> fieldsSet;

    /** actions */
    @JsonProperty("actions")
    private List<Action> actions;


    /** initAction */
    @JsonProperty("initAction")
    private Action initAction;

    /** index */
    @JsonProperty("index")
    private Integer index;

    /** id */
    @JsonProperty("reference")
    private String reference;

    /** oldReference */
    @JsonIgnore
    private String oldReference;

    /** actors */
    @JsonProperty("actors")
    private List<String> actors;

    /** notifiable */
    @JsonProperty("notifiable")
    private Boolean notifiable;

    /** notifEmail */
    @JsonProperty("notifEmail")
    private Boolean notifEmail;

    /** acquitable */
    @JsonProperty("acquitable")
    private Boolean acquitable;

    /** closable */
    @JsonProperty("closable")
    private Boolean closable;

    /** actionIdClosable */
    @JsonProperty("actionIdClosable")
    private String actionIdClosable;

    /** stringMsg */
    @JsonProperty("stringMsg")
    private String stringMsg;

    /** actionIdYes */
    @JsonProperty("actionIdYes")
    private String actionIdYes;

    /** actionIdNo */
    @JsonProperty("actionIdNo")
    private String actionIdNo;

    /** actionIdDefault */
    @JsonProperty("actionIdDefault")
    private String actionIdDefault;

    /** endStepMsg */
    @JsonProperty("endStepMsg")
    private String endStepMsg;

    public Step() {
        fields = new ArrayList<Field>();
        actions = new ArrayList<Action>();
    }

    public Step(final PropertyMap stepM, Map<String, Variable> variables, NuxeoController nuxeoController) {
        fields = new ArrayList<Field>();
        actions = new ArrayList<Action>();

        final PropertyList gvvList = stepM.getList("globalVariablesReferences");
        if (gvvList != null) {
            final Map<String, Field> fieldMap = new HashMap<String, Field>();
            Field field;
            for (final Object gvvO : gvvList.list()) {
                final PropertyMap gvvMap = (PropertyMap) gvvO;
                field = new Field(gvvMap, variables);
                fieldMap.put(field.getPath(), field);
            }

            // on recrée l'architecture
            boolean completed = false;
            int i = 0;
            while (!completed) {
                final String fieldIndex = String.valueOf(i);
                final Field mapField = fieldMap.get(fieldIndex);
                if (mapField != null) {
                    // nested fields
                    fillField(fieldMap, mapField);

                    List<Field> fieldsList = getFields();
                    if (fieldsList == null) {
                        fieldsList = new ArrayList<Field>();
                    }
                    // add to parent
                    getFields().add(mapField);
                    i++;
                } else {
                    completed = true;
                }
            }
        }

        final PropertyMap initActionPmap = stepM.getMap("initAction");
        Action action;
        if (initActionPmap != null) {
            action = new Action(initActionPmap, nuxeoController);
        } else {
            action = new Action();
        }
        setInitAction(action);

        final PropertyList actionsList = stepM.getList("actions");
        if (actionsList != null) {
            for (final Object actionO : actionsList.list()) {
                final PropertyMap actionN = (PropertyMap) actionO;
                action = new Action(actionN, nuxeoController);
                getActions().add(action);
            }
        }

        final PropertyList actorsObjectsList = stepM.getList("actors");
        if (actorsObjectsList != null) {
            for (final Object actorsObject : actorsObjectsList.list()) {
                getActors().add((String) actorsObject);
            }
        }
        setStepName(stepM.getString("name"));
        setIndex(stepM.getLong("index").intValue());
        setReference(stepM.getString("reference"));
        setOldReference(stepM.getString("reference"));
        setNotifiable(BooleanUtils.isTrue(stepM.getBoolean("notifiable")));
        setNotifEmail(BooleanUtils.isTrue(stepM.getBoolean("notifEmail")));
        setClosable(BooleanUtils.isTrue(stepM.getBoolean("closable")));
        setAcquitable(BooleanUtils.isTrue(stepM.getBoolean("acquitable")));
        setStringMsg(stepM.getString("stringMsg"));
        setActionIdYes(stepM.getString("actionIdYes"));
        setActionIdNo(stepM.getString("actionIdNo"));
        setActionIdDefault(stepM.getString("actionIdDefault"));
        setActionIdClosable(stepM.getString("actionIdClosable"));
        setEndStepMsg(stepM.getString("endStepMsg"));
    }

    public Step(Step step) {
        setAcquitable(step.getAcquitable());
        setActionIdClosable(step.getActionIdClosable());
        setActionIdDefault(step.getActionIdDefault());
        setActionIdNo(step.getActionIdNo());
        setActionIdYes(step.getActionIdYes());
        getActions().addAll(step.getActions());
        getActors().addAll(step.getActors());
        setClosable(step.getClosable());
        setEndStepMsg(step.getEndStepMsg());
        getFields().addAll(step.getFields());
        getFieldsSet().addAll(step.getFieldsSet());
        setIndex(step.getIndex());
        setInitAction(step.getInitAction());
        setNotifEmail(step.getNotifEmail());
        setNotifiable(step.getNotifiable());
        setOldReference(step.getOldReference());
        setReference(step.getReference());
        setStepName(step.getStepName());
        setStringMsg(step.getStringMsg());
    }


    private void fillField(Map<String, Field> fieldMap, Field field) {
        boolean completed = false;
        int i = 0;
        while (!completed) {
            final String fieldIndex = String.valueOf(i);
            final Field mapField = fieldMap.get(field.getPath() + "," + fieldIndex);
            if (mapField != null) {
                // nested fields
                fillField(fieldMap, mapField);

                if (field.getFields() == null) {
                    field.setFields(new ArrayList<Field>());
                }
                // add to parent
                field.getFields().add(mapField);
                i++;
            } else {
                completed = true;
            }
        }
    }

    public Step(Integer index, String reference) {
        fields = new ArrayList<Field>();
        actions = new ArrayList<Action>();
        initAction = new Action();
        setIndex(index);
        setReference(reference);
        setNotifiable(true);
        setStringMsg(DEFAULT_NOTIFICATION_MSG);
    }

    /**
     * get the next available index for field
     *
     * @return
     */
    public String getNextPath() {
        int nextPath;
        if (CollectionUtils.isEmpty(getFields())) {
            nextPath = 0;
        } else {
            int lastPath = Integer.parseInt(getFields().get(getFields().size() - 1).getPath());
            nextPath = lastPath++;
        }
        return String.valueOf(nextPath);
    }

    public void addAction() {

        String actionId;
        Set<String> actionIds = new HashSet<String>(actions.size());
        for (Action action : actions) {
            actionIds.add(action.getActionId());
        }

        actionId = reference + actionIds.size();
        int i = 0;
        while (!isUnique(actionId, actionIds)) {
            i++;
            int idRef = actionIds.size() + i;
            actionId = reference + String.valueOf(idRef);
        }

        actions.add(new Action(actionId));
    }


    private boolean isUnique(String stringToTest, Set<String> setToCompare) {
        for (String stringToCompare : setToCompare) {
            if (StringUtils.equals(stringToTest, stringToCompare)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Getter for fields.
     *
     * @return the fields
     */
    public List<Field> getFields() {
        if (fields == null) {
            fields = new ArrayList<Field>();
        }
        return fields;
    }


    /**
     * Setter for fields.
     *
     * @param fields the fields to set
     */
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }


    /**
     * Getter for actions.
     *
     * @return the actions
     */
    public List<Action> getActions() {
        if (actions == null) {
            actions = new ArrayList<Action>();
        }
        return actions;
    }


    /**
     * Setter for actions.
     *
     * @param actions the actions to set
     */
    public void setActions(List<Action> actions) {
        this.actions = actions;
    }


    /**
     * Getter for index.
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Setter for index.
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * Getter for stepName.
     *
     * @return the stepName
     */
    public String getStepName() {
        return stepName;
    }

    /**
     * Setter for stepName.
     *
     * @param stepName the stepName to set
     */
    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    @Override
    public int compareTo(Step step) {
        return getIndex().compareTo(step.getIndex());
    }


    /**
     * Getter for reference.
     *
     * @return the reference
     */
    public String getReference() {
        return reference;
    }


    /**
     * Setter for reference.
     *
     * @param reference the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }


    /**
     * Getter for oldReference.
     *
     * @return the oldReference
     */
    public String getOldReference() {
        return oldReference;
    }


    /**
     * Setter for oldReference.
     *
     * @param oldReference the oldReference to set
     */
    public void setOldReference(String oldReference) {
        this.oldReference = oldReference;
    }

    public Set<Field> getFieldsSet() {
        if (fieldsSet == null) {
            fieldsSet = new HashSet<Field>();
        }
        return fieldsSet;
    }

    /**
     * Setter for fieldsSet.
     *
     * @param fieldsSet the fieldsSet to set
     */
    public void setFieldsSet(Set<Field> fieldsSet) {
        this.fieldsSet = fieldsSet;
    }


    /**
     * Getter for notifiable.
     *
     * @return the notifiable
     */
    public Boolean getNotifiable() {
        return notifiable;
    }


    /**
     * Setter for notifiable.
     *
     * @param notifiable the notifiable to set
     */
    public void setNotifiable(Boolean notifiable) {
        this.notifiable = notifiable;
    }


    /**
     * Getter for acquitable.
     *
     * @return the acquitable
     */
    public Boolean getAcquitable() {
        return acquitable;
    }


    /**
     * Setter for acquitable.
     *
     * @param acquitable the acquitable to set
     */
    public void setAcquitable(Boolean acquitable) {
        this.acquitable = acquitable;
    }


    /**
     * Getter for stringMsg.
     *
     * @return the stringMsg
     */
    public String getStringMsg() {
        return stringMsg;
    }


    /**
     * Setter for stringMsg.
     *
     * @param stringMsg the stringMsg to set
     */
    public void setStringMsg(String stringMsg) {
        this.stringMsg = stringMsg;
    }


    /**
     * Getter for actionIdYes.
     *
     * @return the actionIdYes
     */
    public String getActionIdYes() {
        return actionIdYes;
    }


    /**
     * Setter for actionIdYes.
     *
     * @param actionIdYes the actionIdYes to set
     */
    public void setActionIdYes(String actionIdYes) {
        this.actionIdYes = actionIdYes;
    }



    /**
     * Getter for actionIdNo.
     *
     * @return the actionIdNo
     */
    public String getActionIdNo() {
        return actionIdNo;
    }


    /**
     * Setter for actionIdNo.
     *
     * @param actionIdNo the actionIdNo to set
     */
    public void setActionIdNo(String actionIdNo) {
        this.actionIdNo = actionIdNo;
    }


    /**
     * Getter for actionIdDefault.
     *
     * @return the actionIdDefault
     */
    public String getActionIdDefault() {
        return actionIdDefault;
    }


    /**
     * Setter for actionIdDefault.
     *
     * @param actionIdDefault the actionIdDefault to set
     */
    public void setActionIdDefault(String actionIdDefault) {
        this.actionIdDefault = actionIdDefault;
    }


    /**
     * Getter for closable.
     *
     * @return the closable
     */
    public Boolean getClosable() {
        return closable;
    }


    /**
     * Setter for closable.
     *
     * @param closable the closable to set
     */
    public void setClosable(Boolean closable) {
        this.closable = closable;
    }


    /**
     * Getter for actionIdClosable.
     *
     * @return the actionIdClosable
     */
    public String getActionIdClosable() {
        return actionIdClosable;
    }


    /**
     * Setter for actionIdClosable.
     *
     * @param actionIdClosable the actionIdClosable to set
     */
    public void setActionIdClosable(String actionIdClosable) {
        this.actionIdClosable = actionIdClosable;
    }


    /**
     * Getter for endStepMsg.
     *
     * @return the endStepMsg
     */
    public String getEndStepMsg() {
        return endStepMsg;
    }


    /**
     * Setter for endStepMsg.
     *
     * @param endStepMsg the endStepMsg to set
     */
    public void setEndStepMsg(String endStepMsg) {
        this.endStepMsg = endStepMsg;
    }


    /**
     * Getter for notifEmail.
     *
     * @return the notifEmail
     */
    public Boolean getNotifEmail() {
        return notifEmail;
    }


    /**
     * Setter for notifEmail.
     *
     * @param notifEmail the notifEmail to set
     */
    public void setNotifEmail(Boolean notifEmail) {
        this.notifEmail = notifEmail;
    }

    /**
     * Getter for initAction.
     *
     * @return the initAction
     */
    public Action getInitAction() {
        return initAction;
    }


    /**
     * Setter for initAction.
     *
     * @param initAction the initAction to set
     */
    public void setInitAction(Action initAction) {
        this.initAction = initAction;
    }


    /**
     * Getter for actors.
     *
     * @return the actors
     */
    public List<String> getActors() {
        if (actors == null) {
            actors = new ArrayList<String>();
        }
        return actors;
    }


    /**
     * Setter for actors.
     *
     * @param actors the actors to set
     */
    public void setActors(List<String> actors) {
        this.actors = actors;
    }

}
