package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.osivia.services.procedure.portlet.filter.IFilter;

@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
creatorVisibility = Visibility.NONE)
public class Action {

    /** stepReference */
    @JsonProperty("stepReference")
    private String stepReference;

    /** label */
    @JsonProperty("label")
    private String label;

    /** listeFiltres */
    private List<IFilter> listeFiltres;

    /**
     * Getter for label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter for label.
     *
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }


    /**
     * Getter for stepReference.
     *
     * @return the stepReference
     */
    public String getStepReference() {
        return stepReference;
    }


    /**
     * Setter for stepReference.
     *
     * @param stepReference the stepReference to set
     */
    public void setStepReference(String stepReference) {
        this.stepReference = stepReference;
    }

    public List<IFilter> getListeFiltres() {
        if (listeFiltres == null) {
            listeFiltres = new ArrayList<IFilter>();
        }
        return listeFiltres;
    }

    public void setListeFiltres(List<IFilter> listeFiltres) {
        this.listeFiltres = listeFiltres;
    }
}
