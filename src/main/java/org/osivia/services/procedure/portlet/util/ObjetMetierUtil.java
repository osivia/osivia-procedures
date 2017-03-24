package org.osivia.services.procedure.portlet.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.procedure.portlet.model.ObjetMetier;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;


public class ObjetMetierUtil {

    private ObjetMetierUtil() {
    }

    // ecm://{object}/{variable}
    public static final Pattern objectPattern = Pattern.compile("^ecm:\\/\\/([\\w.]+)\\/([\\w.:]+)");

    public static Map<String, ObjetMetier> buildObjetMetiers(ProcedureModel procedureModel, ProcedureInstance procedureInstance) {

        Map<String, ObjetMetier> objetMetiers = new HashMap<String, ObjetMetier>();
        Matcher matcher;
        Set<String> fileContentToDelete = new TreeSet<String>();

        // for each variable
        Iterator<Entry<String, String>> gvvI = procedureInstance.getGlobalVariablesValues().entrySet().iterator();
        while (gvvI.hasNext()) {
            Entry<String, String> gvv = gvvI.next();
            matcher = ObjetMetierUtil.objectPattern.matcher(gvv.getKey());

            if (matcher.matches()) {
                // if the name matches the pattern
                String objectName = matcher.group(1);
                String objectProperty = matcher.group(2);
                String fileContentName = "ecm://" + objectName + "/file:content";

                if (procedureInstance.getFilesPath().containsKey(fileContentName)) {
                    ObjetMetier objetMetier = objetMetiers.get(objectName);
                    if (objetMetier != null) {
                        objetMetier.getProperties().set(objectProperty, gvv.getValue());
                    } else {
                        objetMetiers.put(objectName, new ObjetMetier(procedureModel.getProcedureObject(objectName), procedureInstance.getProcedureObjects()
                                .get(objectName), procedureInstance.getFilesPath().get(fileContentName)));
                        objetMetiers.get(objectName).getProperties().set(objectProperty, gvv.getValue());
                        gvvI.remove();
                        fileContentToDelete.add(fileContentName);

                    }
                }
            }
        }

        for (String toDelete: fileContentToDelete)  {
            procedureInstance.getFilesPath().remove(toDelete);
        }

        return objetMetiers;
    }

    public static boolean isObject(String string) {
        return ObjetMetierUtil.objectPattern.matcher(string).matches();
    }

    public static String getObjectProperty(String string) {
        Matcher matcher = ObjetMetierUtil.objectPattern.matcher(string);
        if (matcher.matches()) {
            return matcher.group(2);
        }
        return null;
    }

    public static String getObjectName(String string) {
        Matcher matcher = ObjetMetierUtil.objectPattern.matcher(string);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

    public static boolean isContent(String string) {
        Matcher matcher = ObjetMetierUtil.objectPattern.matcher(string);
        if (matcher.matches()) {
            return StringUtils.equals(matcher.group(2), "file:content");
        }
        return false;
    }

}
