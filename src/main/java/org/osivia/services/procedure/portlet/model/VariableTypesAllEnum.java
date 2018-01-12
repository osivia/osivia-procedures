package org.osivia.services.procedure.portlet.model;


/**
 * @author Dorian Licois
 */
public enum VariableTypesAllEnum {

    TEXT, TEXTAREA, DATE, NUMBER, RADIOLIST, CHECKBOXLIST, SELECTLIST, FIELDSET, WYSIWYG, VOCABULARY, PERSON, RECORD;

    public static VariableTypesAllEnum[] filteredValues() {


        VariableTypesAllEnum[] variableTypesEnum = new VariableTypesAllEnum[VariableTypesAllEnum.values().length - 1];
        int i = 0;
        for (VariableTypesAllEnum variableType : VariableTypesAllEnum.values()) {
            if (!VariableTypesAllEnum.FIELDSET.equals(variableType)) {
                variableTypesEnum[i] = variableType;
                i++;
            }
        }

        return variableTypesEnum;
    }
}
