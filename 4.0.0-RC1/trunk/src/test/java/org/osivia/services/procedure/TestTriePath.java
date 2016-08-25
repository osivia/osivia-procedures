package org.osivia.services.procedure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osivia.services.procedure.portlet.model.AddField;
import org.osivia.services.procedure.portlet.model.Field;

public class TestTriePath {

	public static void main(String[] args) {
		List<Field> fieldList = new ArrayList<Field>();
		AddField addField = new AddField();
        fieldList.add(new Field("1", addField, true));
        fieldList.add(new Field("0,1", addField, true));
        fieldList.add(new Field("7", addField, true));
        fieldList.add(new Field("0,0,1", addField, true));
        fieldList.add(new Field("3", addField, true));
        fieldList.add(new Field("2", addField, true));
        fieldList.add(new Field("0,2", addField, true));
        fieldList.add(new Field("0,0", addField, true));
        fieldList.add(new Field("0", addField, true));
        fieldList.add(new Field("2", addField, true));
        fieldList.add(new Field("0,0,0", addField, true));
		
		Collections.sort(fieldList);
		
		for (Field field : fieldList) {
			System.out.println(field.getPath());
		}
		
	}
}
