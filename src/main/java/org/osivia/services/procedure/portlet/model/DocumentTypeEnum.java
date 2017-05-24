package org.osivia.services.procedure.portlet.model;


public enum DocumentTypeEnum {

    PROCEDUREMODEL("ProcedureModel"), PROCEDUREINSTANCE("ProcedureInstance"), TASKDOC("TaskDoc"), RECORDFOLDER("RecordFolder"), RECORDCONTAINER(
            "RecordContainer");

    String docType;

    private DocumentTypeEnum(String docType) {
        this.docType = docType;
    }


    public String getDocType() {
        return docType;
    }
}
