package org.osivia.services.procedure.portlet.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;


/**
 * @author Dorian Licois
 */
@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE)
public class ProcedureObject {

    /** name */
    @JsonProperty("name")
    private String name;

    /** path */
    @JsonProperty("path")
    private String path;

    /** type */
    @JsonProperty("type")
    private String type;


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
     * Getter for type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }


    /**
     * Setter for type.
     *
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
