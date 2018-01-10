package org.osivia.services.procedure.portlet.model;


/**
 * @author Dorian Licois
 */
public enum VariableTypesAllEnum {
    TEXT, TEXTAREA, DATE, NUMBER, WYSIWYG, VOCABULARY,
    // FILE,
    RADIOLIST,
    // RADIOVOCAB,
    CHECKBOXLIST,
    // CHECKBOXVOCAB,
    SELECTLIST,
    // SELECTVOCAB,
    // SELECTVOCABMULTI,
    FIELDSET;

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
