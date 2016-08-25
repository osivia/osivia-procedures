package org.osivia.services.procedure.portlet.exception;


public class FilterException extends Exception {


    private String filterMessage;

    public FilterException() {
    }

    public FilterException(String filterMessage) {
        this.setFilterMessage(filterMessage);
    }

    public String getFilterMessage() {
        return filterMessage;
    }

    public void setFilterMessage(String filterMessage) {
        this.filterMessage = filterMessage;
    }
}
