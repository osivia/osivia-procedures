package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Variable types enumeration.
 *
 * @author Dorian Licois
 */
public enum VariableTypesAllEnum {

    TEXT, TEXTAREA, DATE, NUMBER, RADIOLIST, CHECKBOXLIST, SELECTLIST, FIELDSET, FIELDLIST, WYSIWYG, VOCABULARY, PERSON, RECORD, FILE, PICTURE;


    /**
     * Constructor.
     */
    private VariableTypesAllEnum() {
    }


    /**
     * Get filtered values.
     *
     * @return filtered values
     */
    public static VariableTypesAllEnum[] filteredValues() {
        List<VariableTypesAllEnum> list = new ArrayList<>(Arrays.asList(VariableTypesAllEnum.values()));
        list.remove(FIELDSET);
        return list.toArray(new VariableTypesAllEnum[list.size()]);
    }

}
