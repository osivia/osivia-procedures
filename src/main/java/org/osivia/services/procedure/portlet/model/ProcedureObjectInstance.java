package org.osivia.services.procedure.portlet.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Dorian Licois
 */
@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE)
public class ProcedureObjectInstance {

    /** procedureObjectName */
    @JsonProperty("name")
    private String name;

    /** procedureObjectPath */
    @JsonProperty("procedureObjectId")
    private String procedureObjectId;


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
     * Getter for procedureObjectid.
     *
     * @return the procedureObjectid
     */
    public String getProcedureObjectid() {
        return procedureObjectId;
    }


    /**
     * Setter for procedureObjectid.
     *
     * @param id the procedureObjectid to set
     */
    public void setProcedureObjectid(String procedureObjectid) {
        procedureObjectId = procedureObjectid;
    }

}
