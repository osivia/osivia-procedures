package org.osivia.services.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.osivia.services.procedure.portlet.adapter.ProcedureJSONAdapter;
import org.osivia.services.procedure.portlet.model.Action;
import org.osivia.services.procedure.portlet.model.Field;
import org.osivia.services.procedure.portlet.model.FilePath;
import org.osivia.services.procedure.portlet.model.Filter;
import org.osivia.services.procedure.portlet.model.GlobalVariablesValuesType;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.Step;
import org.osivia.services.procedure.portlet.model.Variable;
import org.osivia.services.procedure.portlet.model.VariableTypesEnum;


public class JSONAdapterTest {

    public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {

        final ProcedureModel procedure = new ProcedureModel();
        procedure.setDescription("description");
        procedure.setName("nameW");
        procedure.setPath("path//");
        final Step step = new Step();
        step.setStepName("stepName");
        final List<String> groups = new ArrayList<String>();
        groups.add("equipe-dev");
        step.setActors(groups);
        step.setIndex(1);
        final Action action = new Action();
        action.setStepReference("actionPath");
        Set<Filter> filtersList = new HashSet<Filter>();
        Filter filter = new Filter();
        filter.setFilterPath("0");
        filter.setFilterId("filterId");
        filter.setFilterInstanceId(filter.getFilterId().concat(filter.getFilterPath()));
        filter.setHasChildren(true);
        filter.setLabelKey("LABEL_KEY");
        filter.setDescriptionKey("DESC_KEY");
        filter.setClassLoader(Thread.class.getClassLoader());
        filtersList.add(filter);
        action.setFiltersList(filtersList);
        step.getActions().add(action);
        final Field field = new Field();
        field.setInput(false);
        field.setName("fieldName");
        step.getFields().add(field);
        final Field field2 = new Field();
        field2.setInput(true);
        field2.setName("fieldName2");
        step.getFields().add(field2);
        Action initAction = new Action();
        initAction.setFiltersList(filtersList);
        step.setInitAction(initAction);
        procedure.getSteps().add(step);
        final Variable variable = new Variable("fieldName", "fieldLabel", VariableTypesEnum.TEXT, null);
        procedure.getVariables().put("fieldName", variable);

        String json = ProcedureJSONAdapter.getInstance().toJSON(procedure.getVariables().values());

        System.out.println(json);

        json = ProcedureJSONAdapter.getInstance().toJSON(procedure.getSteps());
        System.out.println("steps: " + json);

        final ProcedureInstance procedureInstance = new ProcedureInstance();
        procedureInstance.setGlobalVariablesValues(new HashMap<String, String>());
        procedureInstance.getGlobalVariablesValues().put("plop", "plopoui");
        procedureInstance.getGlobalVariablesValues().put("plip", "plipoui");


        final List<GlobalVariablesValuesType> gvvList = new ArrayList<GlobalVariablesValuesType>();
        for (final Entry<String, String> entry : procedureInstance.getGlobalVariablesValues().entrySet()) {
            gvvList.add(new GlobalVariablesValuesType(entry.getKey(), entry.getValue()));
        }

        json = ProcedureJSONAdapter.getInstance().toJSON(gvvList);

        System.out.println(json);


        final FilePath filepath1 = new FilePath();
        filepath1.setFileName("XKLJHSDFJK");
        filepath1.setVariableName("comentaire");
        procedureInstance.getFilesPath().put("comentaire", filepath1);
        final FilePath filepath2 = new FilePath();
        filepath2.setFileName("sdfLJHSDFJDGSD");
        filepath2.setVariableName("comentaire2");
        procedureInstance.getFilesPath().put("comentaire2", filepath2);

        json = ProcedureJSONAdapter.getInstance().toJSON(procedureInstance.getFilesPath().values());

        System.out.println(json);
    }

}
