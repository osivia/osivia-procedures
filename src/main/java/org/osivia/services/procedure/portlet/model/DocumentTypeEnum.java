package org.osivia.services.procedure.portlet.model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public enum DocumentTypeEnum {

    PROCEDUREMODEL("ProcedureModel"), PROCEDUREINSTANCE("ProcedureInstance"), TASKDOC("TaskDoc"), RECORDFOLDER("RecordFolder"), RECORDCONTAINER(
            "RecordContainer"), RECORD("Record");

    String docType;

    private static final Map<String, DocumentTypeEnum> ENUM_MAP;

    private DocumentTypeEnum(String docType) {
        this.docType = docType;
    }


    public String getDocType() {
        return docType;
    }

    static {
        Map<String, DocumentTypeEnum> map = new ConcurrentHashMap<String, DocumentTypeEnum>();
        for (DocumentTypeEnum instance : DocumentTypeEnum.values()) {
            map.put(instance.getDocType(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static DocumentTypeEnum get(String docType) {
        return ENUM_MAP.get(docType);
    }
}
