package org.osivia.services.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.osivia.services.procedure.portlet.adapter.ProcedureJSONAdapter;
import org.osivia.services.procedure.portlet.model.Action;
import org.osivia.services.procedure.portlet.model.Field;
import org.osivia.services.procedure.portlet.model.GlobalVariablesValuesType;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.Step;
import org.osivia.services.procedure.portlet.model.Variable;
import org.osivia.services.procedure.portlet.model.VariableTypesEnum;


public class JSONAdapterTest {

    public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {

        ProcedureModel procedure = new ProcedureModel();
        procedure.setDescription("description");
        procedure.setName("nameW");
        procedure.setPath("path//");
        Step step = new Step();
        step.setStepName("stepName");
        step.setIndex(1);
        Action action = new Action();
        action.setStepReference("actionPath");
        step.getActions().add(action);
        Field field = new Field();
        field.setInput(true);
        field.setLabel("fieldLabel");
        field.setName("fieldName");
        field.setOrder(0);
        field.setType("text");
        step.getFields().add(field);
        procedure.getSteps().add(step);
        Variable variable = new Variable("fieldName", "fieldLabel", VariableTypesEnum.TEXT);
        procedure.getVariables().put("fieldName", variable);

        String json = ProcedureJSONAdapter.getInstance().toJSON(procedure.getVariables().values());

        System.out.println(json);

        ProcedureInstance procedureInstance = new ProcedureInstance();
        procedureInstance.setGlobalVariablesValues(new HashMap<String, String>());
        procedureInstance.getGlobalVariablesValues().put("plop", "plopoui");
        procedureInstance.getGlobalVariablesValues().put("plip", "plipoui");

        List<GlobalVariablesValuesType> gvvList = new ArrayList<GlobalVariablesValuesType>();
        for (Entry<String, String> entry : procedureInstance.getGlobalVariablesValues().entrySet()) {
            gvvList.add(new GlobalVariablesValuesType(entry.getKey(), entry.getValue()));
        }

        json = ProcedureJSONAdapter.getInstance().toJSON(gvvList);

        System.out.println(json);

    }

}
