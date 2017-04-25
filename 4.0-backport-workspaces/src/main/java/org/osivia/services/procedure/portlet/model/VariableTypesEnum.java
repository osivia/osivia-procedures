package org.osivia.services.procedure.portlet.model;


public enum VariableTypesEnum {
    TEXT("TEXT", "Champ texte"), TEXTAREA("TEXTAREA", "Zone de texte"), DATE("DATE", "Champ date"), NUMBER("NUMBER", "Champ nombre entier"), FILE("FILE",
            "Fichier"), RADIOLIST("RADIOLIST", "Boutons radio"), RADIOVOCAB("RADIOVOCAB", "Boutons radio depuis vocabulaire"), CHECKBOXLIST("CHECKBOXLIST",
                    "Cases à cocher"), CHECKBOXVOCAB("CHECKBOXVOCAB", "Cases à cocher depuis vocabulaire"), SELECTLIST("SELECTLIST", "Liste sélectionnable"), SELECTVOCAB(
                            "SELECTVOCAB", "Liste sélectionnable depuis vocabulaire"), SELECTVOCABMULTI("SELECTVOCABMULTI",
                                    "Liste sélectionnable multivaluée depuis vocabulaire");

    private String id;

    private String label;

    private VariableTypesEnum(String id, String label) {
        this.id = id;
        this.label = label;
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
}
