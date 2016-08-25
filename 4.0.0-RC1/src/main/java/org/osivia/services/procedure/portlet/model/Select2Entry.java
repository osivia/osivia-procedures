package org.osivia.services.procedure.portlet.model;

import java.util.List;


public class Select2Entry {

    private String id;

    private String text;

    private List<Select2Entry> children;


    public Select2Entry(String id, String text) {
        this.id = id;
        this.text = text;
    }


    public Select2Entry(String text) {
        id = text;
        this.text = text;
    }


    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }


    /**
     * Setter for id.
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * Getter for text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }


    /**
     * Setter for text.
     *
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }


    /**
     * Getter for children.
     *
     * @return the children
     */
    public List<Select2Entry> getChildren() {
        return children;
    }


    /**
     * Setter for children.
     *
     * @param children the children to set
     */
    public void setChildren(List<Select2Entry> children) {
        this.children = children;
    }

}
