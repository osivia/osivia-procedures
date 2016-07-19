package org.osivia.services.procedure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osivia.services.procedure.portlet.model.Field;

public class TestTriePath {

	public static void main(String[] args) {
		List<Field> fieldList = new ArrayList<Field>();
		fieldList.add(new Field("1"));
		fieldList.add(new Field("0,1"));
		fieldList.add(new Field("7"));
		fieldList.add(new Field("0,0,1"));
		fieldList.add(new Field("3"));
		fieldList.add(new Field("2"));
		fieldList.add(new Field("0,2"));
		fieldList.add(new Field("0,0"));
		fieldList.add(new Field("0"));
		fieldList.add(new Field("2"));
		fieldList.add(new Field("0,0,0"));
		
		Collections.sort(fieldList);
		
		for (Field field : fieldList) {
			System.out.println(field.getPath());
		}
		
	}
}
