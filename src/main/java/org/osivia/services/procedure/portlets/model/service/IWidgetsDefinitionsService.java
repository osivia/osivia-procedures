/**
 * 
 */
package org.osivia.services.procedure.portlets.model.service;

import org.osivia.services.procedure.portlets.model.WidgetDefinition;


/**
 * @author david
 *
 */
public interface IWidgetsDefinitionsService {
    
    /**
     * Adds a widget definition.
     * 
     * @param label
     * @param type
     * @param property
     */
    void addWidgetDefinition(String label, String type, String property);
    
    /**
     * Adds a widget definition.
     * 
     * @param widgetDef
     */
    void addWidgetDefinition(WidgetDefinition widgetDef);
    
    /**
     * Removes a widget definition
     * 
     * @param label
     * @param type
     * @param property
     */
    void removeWidgetDefinition(String label, String type, String property);
    
    /**
     * Removes a widget definition
     */
    void removeWidgetDefinition(WidgetDefinition widgetDef);

}
