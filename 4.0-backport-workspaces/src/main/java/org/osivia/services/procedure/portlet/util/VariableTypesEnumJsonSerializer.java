package org.osivia.services.procedure.portlet.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.osivia.services.procedure.portlet.model.VariableTypesEnum;


public class VariableTypesEnumJsonSerializer extends JsonSerializer<VariableTypesEnum> {

    @Override
    public void serialize(VariableTypesEnum value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", value.name());
        map.put("label", value.getLabel());
        jgen.writeObject(map);
    }

}
