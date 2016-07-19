package org.osivia.services.procedure.portlet.filter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osivia.services.procedure.portlet.exception.FilterException;
import org.osivia.services.procedure.portlet.model.ObjetMetier;


public interface IFilter {

    public void validate(Map<String, String> globalVariablesValues, Map<String, ObjetMetier> objetMetiers) throws FilterException;

    public void action(Map<String, String> globalVariablesValues, Map<String, ObjetMetier> objetMetiers, String stepReference, List<String> groups)
            throws FilterException;

//    public void execute(Map<String, String> globalVariablesValues);
//
//    public Set<Parametre> getParametres();

}
