package zpi.tools;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.vaadin.ui.Table;

import zpi.windows.SchemaTableRow;

/**
 * Klasa sluzaca do parsowania schematu
 *
 */
public class SchemaParsing {

	public static String toSchema(ArrayList<ArrayList<String>> list) {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < list.size(); j++) {
			ArrayList<String> l = list.get(j);
			for (int i = 0; i < l.size(); i++) {
				sb.append(l.get(i));
				if (i != l.size() - 1)
					sb.append(";");

			}
			if (j != list.size() - 1)
				sb.append(",");
		}

		return sb.toString();

	}

	public static ArrayList<ArrayList<String>> fromSchema(String schema){
		String st[] = new String[]{};
		ArrayList<ArrayList<String>> l = new ArrayList<ArrayList<String>>();
		if (schema != null){
			st = schema.split("\\,");
		}
		for(String s : st){
			String st1[];
			st1 = s.split("\\;");
			ArrayList<String> list = new ArrayList<String>();
			for(String s1 :st1){
				list.add(s1);
			}
			l.add(list);
		}
		
		return l;
		
	}

	public static void fillTableUsingSchema(String schema, Table table) {
		ArrayList<ArrayList<String>> fromSchema = SchemaParsing
				.fromSchema(schema);
		if (!fromSchema.isEmpty()) {
			table.setPageLength(fromSchema.size());
			for (ArrayList<String> list : fromSchema) {
				SchemaTableRow row = new SchemaTableRow(list, table);
				table.addItem(row.toArray(), row);
			}
		} else {
			table.setPageLength(1);
			SchemaTableRow row = new SchemaTableRow(table);
			table.addItem(row.toArray(), row);
		}
	}

}
