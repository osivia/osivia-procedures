package org.osivia.services.procedure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;


public class TestEL {

    public static void main(String[] args) {
        String expression = "${var1.toString}";
        Map<String, String> variables = new HashMap<String, String>();
        variables.put("var1", "3");
        variables.put("var2", "4");


        // Expression factory
        ExpressionFactory factory = new ExpressionFactoryImpl();

        // Simple context
        SimpleContext context = new SimpleContext();

        // Variables
        if (MapUtils.isNotEmpty(variables)) {
            for (Entry<String, String> entry : variables.entrySet()) {
                context.setVariable(entry.getKey(), factory.createValueExpression(entry.getValue(), String.class));
            }
        }

        try {
            context.setFunction("fmt", "date", TestEL.class.getMethod("toDate", String.class));
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }

        // Value expression
        ValueExpression value = factory.createValueExpression(context, StringUtils.trimToEmpty(expression), String.class);

        System.out.println(value.getValue(context));
    }

    private Date toDate(String varDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("DD/mm/yyyy");
        return sdf.parse(varDate);
    }

}
