package org.osivia.services.procedure.portlet.model;


public enum DocumentTypeEnum {

    PROCEDUREMODEL("ProcedureModel"), PROCEDUREINSTANCE("ProcedureInstance");

    String name;

    private DocumentTypeEnum(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
}
