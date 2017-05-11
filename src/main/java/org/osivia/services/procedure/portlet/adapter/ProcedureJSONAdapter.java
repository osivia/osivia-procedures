package org.osivia.services.procedure.portlet.adapter;

import java.io.IOException;

import net.sf.json.JSONArray;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * Utilitarian class to parse from and serialize to JSON with custom configuration
 *
 * @author Dorian Licois
 */
public class ProcedureJSONAdapter {

    private static class ProcedureJSONAdaptereHolder {
        public static ProcedureJSONAdapter instance = new ProcedureJSONAdapter();
    }

    public static ProcedureJSONAdapter getInstance() {
        return ProcedureJSONAdaptereHolder.instance;
    }

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.getSerializationConfig().setSerializationInclusion(Inclusion.NON_DEFAULT);
    }

    public String toJSON(Object jsonableObject) throws JsonGenerationException, JsonMappingException, IOException {
        // pretty output for debugging purpose
        // ObjectWriter writer = mapper.defaultPrettyPrintingWriter();
        ObjectWriter writer = mapper.writer();

        return writer.writeValueAsString(jsonableObject);
    }

    public String toJSONArray(Object jsonableObject) throws JsonGenerationException, JsonMappingException, IOException {
        // pretty output for debugging purpose
        // ObjectWriter writer = mapper.defaultPrettyPrintingWriter();
        ObjectWriter writer = mapper.writer();

        return writer.writeValueAsString(JSONArray.fromObject(jsonableObject));
    }

}
