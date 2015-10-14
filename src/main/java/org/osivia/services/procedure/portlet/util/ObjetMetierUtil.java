package org.osivia.services.procedure.portlet.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.procedure.portlet.model.ObjetMetier;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.ProcedureObject;


public class ObjetMetierUtil {

    // ecm://{object}/{variable}
    public static final Pattern objectPattern = Pattern.compile("^ecm:\\/\\/([\\w.]+)\\/([\\w.:]+)");

    public static Map<String, ObjetMetier> buildObjetMetiers(ProcedureModel procedureModel, ProcedureInstance procedureInstance) {

        Map<String, ObjetMetier> objetMetiers = new HashMap<String, ObjetMetier>();
        Matcher matcher;
        // for each variable
        for (Entry<String, String> gvv : procedureInstance.getGlobalVariablesValues().entrySet()) {
            matcher = ObjetMetierUtil.objectPattern.matcher(gvv.getKey());

            if (matcher.matches()) {
                // if the name matches the pattern
                String objectName = matcher.group(1);
                String objectProperty = matcher.group(2);
                String fileContentName = "ecm://" + objectName + "/file:content";

                ObjetMetier objetMetier = objetMetiers.get(objectName);
                if (objetMetier != null) {
                    objetMetier.getProperties().set(objectProperty, gvv.getValue());
                } else {
                    objetMetiers.put(objectName,
                            new ObjetMetier(getProcedureObject(procedureModel, objectName), procedureInstance.getProcedureObjects().get(objectName),
                                    procedureInstance.getFilesPath().get(fileContentName)));
                    objetMetiers.get(objectName).getProperties().set(objectProperty, gvv.getValue());
                    procedureInstance.getGlobalVariablesValues().remove(gvv.getKey());
                    procedureInstance.getFilesPath().remove(fileContentName);
                }
            }
        }
        return objetMetiers;
    }

    /**
     * @param procedureModel
     * @param objectName
     * @return
     */
    private static ProcedureObject getProcedureObject(ProcedureModel procedureModel, String objectName) {
        for (ProcedureObject procedureObject : procedureModel.getProcedureObjects()) {
            if (StringUtils.equals(procedureObject.getName(), objectName)) {
                return procedureObject;
            }
        }
        return null;
    }
}
