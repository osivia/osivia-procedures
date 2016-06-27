package org.osivia.services.procedure.portlet.filter.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.procedure.portlet.exception.FilterException;
import org.osivia.services.procedure.portlet.filter.IFilter;
import org.osivia.services.procedure.portlet.model.ObjetMetier;


public class NumberLessThanFilter implements IFilter {

    private final Integer value;

    private final String valueName;

    public NumberLessThanFilter(String valueName, Integer value) {
        this.value = value;
        this.valueName = valueName;
    }

    @Override
    public void validate(Map<String, String> globalVariablesValues, Map<String, ObjetMetier> objetMetiers) throws FilterException {

        if(StringUtils.isBlank(globalVariablesValues.get(valueName))){
            throw new FilterException("La valeur " + valueName + " est incorrect");
        }

        if (!(Integer.valueOf(globalVariablesValues.get(valueName)) < value)) {
            throw new FilterException("La valeur " + valueName + " est supérieure à " + value);
        }
    }

    @Override
    public void action(Map<String, String> globalVariablesValues, Map<String, ObjetMetier> objetMetiers, String stepReference, List<String> groups)
            throws FilterException {

    }
}
