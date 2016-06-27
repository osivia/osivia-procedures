package org.osivia.services.procedure.portlet.filter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.procedure.portlet.exception.FilterException;
import org.osivia.services.procedure.portlet.filter.IFilter;
import org.osivia.services.procedure.portlet.model.ObjetMetier;


public class NotificationInspecteurFilter implements IFilter {

    private static final String ELEM = "élémentaire";

    private static final String COLL = "Collège ou LTG";

    private static final String EREA = "EREA ou LP";

    private static final String BARBE = "INS-ELEM";

    private static final String PRIGENT = "INS-LGT";

    private static final String OLIVIERO = "INS-EREA";

    @Override
    public void validate(Map<String, String> globalVariablesValues, Map<String, ObjetMetier> objetMetiers) throws FilterException {

    }

    @Override
    public void action(Map<String, String> globalVariablesValues, Map<String, ObjetMetier> objetMetiers, String stepReference, List<String> groups)
            throws FilterException {
        groups = new ArrayList<String>();
        if (StringUtils.equals(globalVariablesValues.get("typeEtab"), ELEM)) {
            groups.add(BARBE);
        } else if (StringUtils.equals(globalVariablesValues.get("typeEtab"), COLL)) {
            groups.add(PRIGENT);
        } else if (StringUtils.equals(globalVariablesValues.get("typeEtab"), EREA)) {
            groups.add(OLIVIERO);
        } else {
            throw new FilterException("La valeur type établissement est incorrect");
        }

    }

}
