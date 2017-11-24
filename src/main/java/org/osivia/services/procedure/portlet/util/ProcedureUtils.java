package org.osivia.services.procedure.portlet.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.services.procedure.portlet.model.Action;
import org.osivia.services.procedure.portlet.model.Field;
import org.osivia.services.procedure.portlet.model.Filter;
import org.osivia.services.procedure.portlet.model.Step;
import org.osivia.services.procedure.portlet.model.Variable;


/**
 * main util class
 * 
 * @author Dorian Licois
 */
public class ProcedureUtils {

    private static final Pattern ICDMpattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");//$NON-NLS-1$
    
    private static final Pattern NOT_ALPHANUMERIC_PATTERN = Pattern.compile("[^\\p{Alnum}]", Pattern.UNICODE_CHARACTER_CLASS);

    private ProcedureUtils() {
    }
    
    /**
     * build the map of fields where each variable is used in the model
     * 
     * @param sortedVariables
     * @param stepList
     */
    public static void fillUsedInFields(List<Variable> sortedVariables, List<Step> stepList) {

        if (CollectionUtils.isNotEmpty(stepList)) {
            for (Step step : stepList) {
                fillUsedInFields(sortedVariables, step.getFields(), step.getStepName());
            }
        }
    }

    /**
     * populate the map of fields used in this step
     * 
     * @param sortedVariables
     * @param fields
     * @param stepName
     */
    private static void fillUsedInFields(List<Variable> sortedVariables, List<Field> fields, String stepName) {
        if (CollectionUtils.isNotEmpty(fields)) {
            for (Field field : fields) {
                Variable variable = getVariableByName(sortedVariables, field.getName());
                if (variable != null) {
                    List<Field> stepFields = variable.getUsedInFields().get(stepName);
                    if (stepFields == null) {
                        stepFields = new ArrayList<>();
                    }
                    stepFields.add(field);
                    variable.getUsedInFields().put(stepName, stepFields);
                }
                fillUsedInFields(sortedVariables, field.getFields(), stepName);
            }
        }
    }

    /**
     * retrieve a Variable in a List by its name, null if it can't be found
     * 
     * @param sortedVariables
     * @param variableName
     * @return
     */
    private static Variable getVariableByName(List<Variable> sortedVariables, String variableName) {
        if (CollectionUtils.isNotEmpty(sortedVariables)) {
            for (Variable variable : sortedVariables) {
                if (StringUtils.equals(variableName, variable.getName())) {
                    return variable;
                }
            }
        }
        return null;
    }

    /**
     * remove a filter from a filter tree given its path
     * 
     * @param step
     * @param fieldPath
     */
    public static void removeFieldsByFieldPath(Step step, String fieldPath) {
        if (removeFieldsByFieldPath(step.getFields(), fieldPath)) {
            updateFieldsPath(step.getFields(), StringUtils.EMPTY);
        }
    }
    
    /**
     * remove a filter from a filter tree given its path
     * 
     * @param list
     * @param fieldPath
     * @return
     */
    private static boolean removeFieldsByFieldPath(List<Field> list, String fieldPath) {
        if (list != null) {
            ListIterator<Field> filtersI = list.listIterator();
            while (filtersI.hasNext()) {
                Field field = filtersI.next();
                if (StringUtils.equals(field.getPath(), fieldPath)) {
                    filtersI.remove();
                    return true;
                }
                if (removeFieldsByFieldPath(field.getFields(), fieldPath)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * recursively update the path of fields
     * 
     * @param list
     * @param currentPath
     */
    public static void updateFieldsPath(List<Field> list, String currentPath) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String newPath = currentPath.length() > 0 ? currentPath.concat(",").concat(String.valueOf(i)) : String.valueOf(i);
                list.get(i).setPath(newPath);
                updateFieldsPath(list.get(i).getFields(), newPath);
            }
        }
    }

    /**
     * build the filters tree of an action with their path
     * 
     * @param action
     */
    public static void rebuildAction(Action action) {
        final Map<String, List<Filter>> allFiltersMap = new HashMap<String, List<Filter>>();
        addAllFilters(allFiltersMap, action.getFilters());
        rebuildAction(allFiltersMap, action);
    }


    private static void addAllFilters(Map<String, List<Filter>> allFiltersMap, List<Filter> filters) {
        if (filters != null) {
            for (final Filter filter : filters) {
                if (filter.getFilterPath() != null) {
                    // on ajoute le filtre dans la map avec le path parent comme clé
                    final String parentPath = filter.getFilterPath().length() > 1 ? StringUtils.substringBeforeLast(filter.getFilterPath(), ",")
                            : StringUtils.EMPTY;
                    List<Filter> parentFilters = allFiltersMap.get(parentPath);
                    if (parentFilters == null) {
                        parentFilters = new ArrayList<Filter>();
                    }
                    filter.setFilterInstanceId(filter.getFilterId().concat(filter.getFilterPath()));
                    parentFilters.add(filter);
                    Collections.sort(parentFilters);
                    allFiltersMap.put(parentPath, parentFilters);
                    addAllFilters(allFiltersMap, filter.getFilters());
                }
            }
        }
    }

    private static void rebuildAction(Map<String, List<Filter>> allFiltersMap, Action action) {
        final List<Filter> baseFilters = new ArrayList<Filter>();

        final List<Filter> filtersList = allFiltersMap.get(StringUtils.EMPTY);
        if (filtersList != null) {
            baseFilters.addAll(filtersList);
            Collections.sort(baseFilters);
        }
        rebuildFilters(allFiltersMap, baseFilters);

        action.setFilters(baseFilters);
    }

    private static void rebuildFilters(Map<String, List<Filter>> allFiltersMap, List<Filter> baseFilters) {
        if (baseFilters != null) {
            for (final Filter filter : baseFilters) {
                filter.setFilters(allFiltersMap.get(filter.getFilterPath()));
                rebuildFilters(allFiltersMap, filter.getFilters());
            }
        }
    }

    /**
     * get the filter in a tree given its path
     * 
     * @param filtersList
     * @param selectedFilterPath
     * @return
     */
    public static Filter getFilterByFilterPath(List<Filter> filtersList, String selectedFilterPath) {
        Filter returnFilter = null;
        if (filtersList != null) {
            for (Filter filter : filtersList) {
                if (StringUtils.equals(filter.getFilterPath(), selectedFilterPath)) {
                    returnFilter = filter;
                }
                Filter filterbyId = getFilterByFilterPath(filter.getFilters(), selectedFilterPath);
                if (filterbyId != null) {
                    returnFilter = filterbyId;
                }
            }
        }
        return returnFilter;
    }

    /**
     * Update a filter tree with the given filter
     * 
     * @param filtersList
     * @param filterUpdate
     */
    public static void updateFilter(List<Filter> filtersList, Filter filterUpdate) {
        if (filtersList != null) {
            for (Filter filter : filtersList) {
                if (StringUtils.equals(filter.getFilterPath(), filterUpdate.getFilterPath())) {
                    filter.updateFilter(filterUpdate);
                    return;
                }
                updateFilter(filter.getFilters(), filterUpdate);
            }
        }
    }

    /**
     * remove a filter from a filter tree given its path
     * 
     * @param filters
     * @param filterPath
     * @return
     */
    private static boolean removeFilterByFilterPath(List<Filter> filters, String filterPath) {
        if (filters != null) {
            ListIterator<Filter> filtersI = filters.listIterator();
            while (filtersI.hasNext()) {
                Filter filter = filtersI.next();
                if (StringUtils.equals(filter.getFilterPath(), filterPath)) {
                    filtersI.remove();
                    return true;
                }
                if (removeFilterByFilterPath(filter.getFilters(), filterPath)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * remove a filter from a filter tree given its path
     * 
     * @param filters
     * @param filterPath
     */
    public static void removeFilterByFilterPath(Action action, String filterPath) {
        if (removeFilterByFilterPath(action.getFilters(), filterPath)) {
            updateFiltersPath(action.getFilters(), StringUtils.EMPTY);
        }
    }

    /**
     * recursively update the path of filters
     * 
     * @param filters
     * @param currentPath
     */
    public static void updateFiltersPath(List<Filter> filters, String currentPath) {
        if (filters != null) {
            for (int i = 0; i < filters.size(); i++) {
                String newPath = currentPath.length() > 0 ? currentPath.concat(",").concat(String.valueOf(i)) : String.valueOf(i);
                filters.get(i).updateFilterPath(newPath);
                updateFiltersPath(filters.get(i).getFilters(), newPath);
            }
        }
    }

    /**
     * gets a field given its path
     * 
     * @param fields
     * @param selectedFieldPath
     * @return
     */
    public static Field getFieldByFieldPath(List<Field> fields, String selectedFieldPath) {
        Field returnField = null;
        if (fields != null) {
            for (Field field : fields) {
                if (StringUtils.equals(field.getPath(), selectedFieldPath)) {
                    returnField = field;
                }
                Field fieldByPath = getFieldByFieldPath(field.getFields(), selectedFieldPath);
                if (fieldByPath != null) {
                    returnField = fieldByPath;
                }
            }
        }
        return returnField;
    }

    /**
     * build the fields tree of a step with their path
     * 
     * @param step
     */
    public static void rebuildStep(Step step) {
        final Map<String, List<Field>> allFieldsMap = new HashMap<String, List<Field>>();
        addAllFields(allFieldsMap, step.getFields());
        rebuildStep(allFieldsMap, step);
    }
    
    
    /**
     * populate a map with every fields of a step
     * 
     * @param allFieldsMap
     * @param fields
     */
    private static void addAllFields(Map<String, List<Field>> allFieldsMap, List<Field> fields) {
        if (fields != null) {
            for (final Field field : fields) {
                if (field.getPath() != null) {
                    // on ajoute la field dans la map avec le path parent comme clé
                    final String parentPath = StringUtils.split(field.getPath(), ',').length > 1 ? StringUtils.substringBeforeLast(field.getPath(), ",")
                            : StringUtils.EMPTY;
                    List<Field> parentFields = allFieldsMap.get(parentPath);
                    if (parentFields == null) {
                        parentFields = new ArrayList<Field>();
                    }
                    parentFields.add(field);
                    Collections.sort(parentFields);
                    allFieldsMap.put(parentPath, parentFields);
                    addAllFields(allFieldsMap, field.getFields());
                }
            }
        }
    }
    
    /**
     * build the fields tree of a step with their path
     * 
     * @param allFieldsMap
     * @param step
     */
    private static void rebuildStep(Map<String, List<Field>> allFieldsMap, Step step) {
        final List<Field> baseFields = new ArrayList<Field>();

        final List<Field> fieldList = allFieldsMap.get(StringUtils.EMPTY);
        if (fieldList != null) {
            baseFields.addAll(fieldList);
            Collections.sort(baseFields);
        }
        rebuildFields(allFieldsMap, baseFields);

        step.setFields(baseFields);
    }

    /**
     * build the fields tree of a field with their path
     * 
     * @param allFieldsMap
     * @param fields
     */
    private static void rebuildFields(Map<String, List<Field>> allFieldsMap, List<Field> fields) {
        if (fields != null) {
            for (final Field field : fields) {
                field.setFields(allFieldsMap.get(field.getPath()));
                rebuildFields(allFieldsMap, field.getFields());
            }
        }
    }

    /**
     * return a Field in a form given it's path
     * 
     * @param fields
     * @param path
     * @return null if the field is not found
     */
    public static Field getFieldByPath(List<Field> fields, String[] path) {
        final Integer index = Integer.parseInt(path[0]);
        Field nestedField;
        if ((path.length == 1) && (fields != null)) {
            // on a fini de parcourir le path
            final ListIterator<Field> listIterator = fields.listIterator();
            while (listIterator.hasNext()) {
                final Field field = listIterator.next();
                final String[] pathArray = StringUtils.split(field.getPath(), ',');
                if ((pathArray.length > 0) && (Integer.parseInt(pathArray[pathArray.length - 1]) == index)) {
                    return field;
                }
            }
        } else {
            // on continue de parcourir le path
            nestedField = fields.get(index);
            path = (String[]) ArrayUtils.remove(path, 0);
            return getFieldByPath(nestedField.getFields(), path);
        }
        return null;
    }

    /**
     * format a string to be used as variable name and make sure it doesn't exist yet
     * 
     * @param variables
     * @param label
     * @return a formated string that isn't in the procedure dictionary yet
     * @throws PortletException
     */
    public static String buildUniqueVariableName(Map<String, Variable> variables, String varName) throws PortletException {

        varName = normalizeVariableName(varName);

        int i = 0;
        String uniqueVarName = StringUtils.isNotBlank(varName) ? varName : varName + i;
        while (variables.containsKey(uniqueVarName)) {
            uniqueVarName = varName + i;
            i++;
        }
        return uniqueVarName;
    }

    /**
     * format a string to be used as variable name
     * 
     * @param varName
     * @return
     */
    public static String normalizeVariableName(String varName) {
        varName = StringUtils.deleteWhitespace(varName);
        varName = normalizeAccents(varName);
        varName = NOT_ALPHANUMERIC_PATTERN.matcher(varName).replaceAll(StringUtils.EMPTY);
        varName = varName.toLowerCase();
        return varName;
    }

    /**
     * <p>
     * Removes diacritics (~= accents) from a string. The case will not be altered.
     * </p>
     * <p>
     * For instance, '&agrave;' will be replaced by 'a'.
     * </p>
     * <p>
     * Note that ligatures will be left as is.
     * </p>
     *
     * <pre>
     * StringUtils.stripAccents(null)                = null
     * StringUtils.stripAccents("")                  = ""
     * StringUtils.stripAccents("control")           = "control"
     * StringUtils.stripAccents("&eacute;clair")     = "eclair"
     * </pre>
     *
     * @param input String to be stripped
     * @return input text with diacritics removed
     *
     * @since 3.0
     * @see StringUtils.stripAccents
     */
    // See also Lucene's ASCIIFoldingFilter (Lucene 2.9) that replaces accented characters by their unaccented equivalent (and uncommitted bug fix:
    // https://issues.apache.org/jira/browse/LUCENE-1343?focusedCommentId=12858907&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#action_12858907).
    private static String normalizeAccents(final String input) {
        if (input == null) {
            return null;
        }
        final StringBuilder decomposed = new StringBuilder(Normalizer.normalize(input, Normalizer.Form.NFD));
        convertRemainingAccentCharacters(decomposed);
        // Note that this doesn't correctly remove ligatures...
        return ICDMpattern.matcher(decomposed).replaceAll(StringUtils.EMPTY);
    }

    private static void convertRemainingAccentCharacters(final StringBuilder decomposed) {
        for (int i = 0; i < decomposed.length(); i++) {
            if (decomposed.charAt(i) == '\u0141') {
                decomposed.deleteCharAt(i);
                decomposed.insert(i, 'L');
            } else if (decomposed.charAt(i) == '\u0142') {
                decomposed.deleteCharAt(i);
                decomposed.insert(i, 'l');
            }
        }
    }
}
