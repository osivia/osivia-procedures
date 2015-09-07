/**
 * 
 */
package org.osivia.services.procedure.portlets.model;


/**
 * @author david
 *
 */
public class WidgetDefinition {
    
    /** Label of widget. */
    private String label;
    /** Type of widget. */
    private String type;
    /** Property mapped with widget. */
    private String property;
    
    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * @return the property
     */
    public String getProperty() {
        return property;
    }
    
    /**
     * @param property the property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }
    
}
