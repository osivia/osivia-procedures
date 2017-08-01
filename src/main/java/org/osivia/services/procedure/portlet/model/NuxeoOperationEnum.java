package org.osivia.services.procedure.portlet.model;


/**
 * Enum for operation used in osivia-procedure
 * 
 * @author Dorian Licois
 */
public enum NuxeoOperationEnum {

    CreateDocument("Document.TTCCreate"), RetrieveDocument("Document.Fetch"), UpdateDocument("Document.TTCUpdate"), DeleteDocument("Document.Delete"), StartProcedure(
            "Services.StartProcedure"), UpdateProcedure("Services.UpdateProcedure"), CreateDocumentFromAttachment("Services.CreateDocumentFromAttachment"), CreateDocumentFromBlob(
                    "Services.CreateDocumentFromBlob"), Query("Document.Query"), QueryElasticSearch("Document.QueryES"), UpdateDocumentFromBlob(
                            "Services.UpdateDocumentFromBlob");

    String id;

    private NuxeoOperationEnum(String id) {
        this.id = id;
    }


    /**
     * Getter for id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

}
