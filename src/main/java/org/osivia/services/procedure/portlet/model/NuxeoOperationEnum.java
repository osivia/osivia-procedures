package org.osivia.services.procedure.portlet.model;


public enum NuxeoOperationEnum {

    CreateDocument("Document.Create"), RetrieveDocument("Document.Fetch"), UpdateDocument("Document.Update"), DeleteDocument("Document.Delete"), StartProcedure(
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
