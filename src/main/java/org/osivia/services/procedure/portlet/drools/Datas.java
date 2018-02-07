package org.osivia.services.procedure.portlet.drools;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.Variable;
import org.osivia.services.procedure.portlet.model.VariableTypesAllEnum;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Datas {
	public Map<String, String> values;
	public Map<String, Datas> convertedValues;

	private ProcedureModel model;

	public static Date getCurrentDate() {
		return new Date();
	}

	public Map<String, String> getValues() {
		return values;
	}

	public Object getValue(String name) throws ParseException {

		// TODO memorization

		Object res = getValues().get(name);
		Variable type = model.getVariables().get(name);

		if (VariableTypesAllEnum.DATE.equals(type.getType())) {
			SimpleDateFormat sdfmt1 = new SimpleDateFormat("dd/MM/yyyy");

			if (!StringUtils.isEmpty((String) res))
				res = sdfmt1.parse((String) res);
			else
				res = null;
		}

		if (VariableTypesAllEnum.NUMBER.equals(type.getType())) {
			Long val = new Long((String) res);

			return val;

		}

		if (VariableTypesAllEnum.FIELDLIST.equals(type.getType())) {

			if (res != null) {

				JSONArray json = JSONArray.fromObject(res);
				List list = new ArrayList<>(json.size());
				for (int i = 0; i < json.size(); i++) {
					JSONObject jsonObject = json.getJSONObject(i);

					Map<String, String> childs = new HashMap<>();

					if (jsonObject != null) {

						Iterator<String> itr = (Iterator<String>) jsonObject.keys();

						while (itr.hasNext()) {
							String key = itr.next();
							childs.put(key, jsonObject.getString(key));
						}
					}

					list.add(new Datas(childs, model));
				}
				return list;
			} else
				return new ArrayList<>();
		}

		return res;
	}

	public void setValue(String name, Object value) throws ParseException {

		getValues().put(name, convertValue(name, value));

	}

	private String convertValue(String name, Object value) {
		Variable type = model.getVariables().get(name);
		String res = null;
		if (type != null) {
			if (VariableTypesAllEnum.DATE.equals(type.getType())) {

				SimpleDateFormat sdfmt1 = new SimpleDateFormat("dd/MM/yyyy");

				res = sdfmt1.format((Date) value);

			}

			if (VariableTypesAllEnum.NUMBER.equals(type.getType())) {

				// Double is the standard format for drools accumulaation
				if (value instanceof Double) {
					DecimalFormat format = new DecimalFormat();
					format.setDecimalSeparatorAlwaysShown(false);
					format.setGroupingUsed(false);

					res = format.format(value);
				}
			}

		}

		if (res == null)
			res = value.toString();
		return res;

	}

	public void setRelevant(String name, boolean value) throws ParseException {
		getValues().put("osivia." + name + ".applicable", Boolean.toString(value));
	}

	public void setValue(String name, String field, Object value) throws ParseException {
		getValues().put("osivia." + name + "." + field, convertValue(field, value));
	}

	public Datas(Map<String, String> values, ProcedureModel model) {
		super();
		this.values = values;
		this.model = model;

	}

}
