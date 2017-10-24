package org.osivia.services.procedure.portlet.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.services.procedure.portlet.model.VariableTypesAllEnum;


/**
 * custom serializer for enum used in select2
 * 
 * @author Dorian Licois
 */
public class VariableTypesEnumJsonSerializer extends JsonSerializer<VariableTypesAllEnum> {

    private Bundle bundle;

    public VariableTypesEnumJsonSerializer(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void serialize(VariableTypesAllEnum value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", value.name());
        map.put("label", bundle.getString(value.name()));
        jgen.writeObject(map);
    }

}
