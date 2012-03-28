package zpi.windows;


import java.util.ArrayList;
import java.util.Collection;

import zpi.hadoopModel.Algorithm;
import zpi.listeners.AddParameterToTableListener;
import zpi.tools.SchemaParsing;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

/**
 * Okno sluzace do edytowania algorytmu
 *
 */
@SuppressWarnings("serial")
public class EditAlgorithmWindow extends Window {

	private Algorithm algorithm = null;
	
	public EditAlgorithmWindow(Algorithm algorithm){
		super("Edytuj parametry algorytmu");
		this.algorithm = algorithm;
		createGUI();
		setHeight(600f, UNITS_PIXELS);
		setWidth(1050f, UNITS_PIXELS);
	}
	
	private void createGUI(){
		
		Label tableLabel = new Label("Podaj parametry algorytmu");
		tableLabel.setStyleName("h2");
		
		addComponent(tableLabel);
		
		Table table = new Table();
		table.setStyleName("strong borderless");
		table.setEditable(true);
		table.addContainerProperty("Parametr", String.class,  null);
		table.addContainerProperty("Typ", ComboBox.class,  null);
		table.addContainerProperty("Wartoœæ domyœlna", String.class,  null);
		table.addContainerProperty("Wartoœæ minimalna", String.class,  null);
		table.addContainerProperty("Wartoœæ maksymalna", String.class,  null);
		table.addContainerProperty("Opcjonalny", Boolean.class,  null);
		table.addContainerProperty("Usuñ parametr", Button.class,  null);
		
		ArrayList<ArrayList<String>> fromSchema = SchemaParsing.fromSchema(algorithm.getParameters());
		if (!fromSchema.isEmpty()){
			table.setPageLength(fromSchema.size());
			for (ArrayList<String> list : fromSchema){
				ParametersTableRow row = new ParametersTableRow(list, table);
				table.addItem(row.toArray(), row);
			}
		}
		else{
			ParametersTableRow row = new ParametersTableRow(table);
			table.setPageLength(1);
			table.addItem(row.toArray(), row);
		}
		Button addParameterButton = new Button("Dodaj parametr");
		addParameterButton.addListener(new AddParameterToTableListener(table));
		
		Button confirmButton = new Button("ZatwierdŸ zmiany");
		confirmButton.setWidth(120f, UNITS_PIXELS);
		confirmButton.addListener(new ConfirmEditParametersListener(this, table));
		
		Button cancelButton = new Button("Anuluj");
		cancelButton.addListener(new CancelEditParametersListener(this));
		cancelButton.setWidth(120f, UNITS_PIXELS);
		
		addComponent(table);
		addComponent(addParameterButton);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(1000f, UNITS_PIXELS);
		horizontalLayout.setHeight(80f, UNITS_PIXELS);
		horizontalLayout.addComponent(confirmButton);
		horizontalLayout.addComponent(cancelButton);
		horizontalLayout.setComponentAlignment(confirmButton, Alignment.MIDDLE_CENTER);
		horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
		
		addComponent(horizontalLayout);
	}
	
	private class ConfirmEditParametersListener implements Button.ClickListener {

		Window _window = null;
		Table _table = null;
		
		public ConfirmEditParametersListener(Window window, Table table){
			_window = window;
			_table = table;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			if (!validateTable(_table)){
				return;
			}
			ArrayList<ArrayList<String>> baseList = new ArrayList<ArrayList<String>>();
			Collection<ParametersTableRow> itemIds = (Collection<ParametersTableRow>) _table.getItemIds();
			for (ParametersTableRow row : itemIds){
				String name = (String) _table.getContainerProperty(row, "Parametr").getValue();
				ComboBox combo = (ComboBox) _table.getContainerProperty(row, "Typ").getValue();
				String type = (String) combo.getValue();
				
				String defaultValue = (String) _table.getContainerProperty(row, "Wartoœæ domyœlna").getValue();
				String minValue = (String) _table.getContainerProperty(row, "Wartoœæ minimalna").getValue();
				String maxValue = (String) _table.getContainerProperty(row, "Wartoœæ maksymalna").getValue();
				boolean optional = (Boolean) _table.getContainerProperty(row, "Opcjonalny").getValue();
				
				ArrayList<String> list = new ArrayList<String>();
				
				list.add(name);
				list.add(type);
				list.add(defaultValue);
				list.add(minValue);
				list.add(maxValue);
				list.add(Boolean.toString(optional));
				baseList.add(list);
			}
			String schema = SchemaParsing.toSchema(baseList);
			boolean needsToBeSaved = false;
			if (algorithm.getParameters() != null){
				needsToBeSaved = true;
			}
			algorithm.setParameters(schema);
			if (needsToBeSaved){
				algorithm.save();
			}
			((Window)_window.getParent()).removeWindow(_window);
		}
		
	}
	
	private class CancelEditParametersListener implements Button.ClickListener {

		Window _window = null;
		
		
		public CancelEditParametersListener(Window window){
			_window = window;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			((Window)_window.getParent()).removeWindow(_window);
		}
		
	}
	
	private boolean validateTable(Table table) {
		boolean isValid = true;
		Collection<ParametersTableRow> itemIds = (Collection<ParametersTableRow>) table.getItemIds();
		try {
			for (ParametersTableRow row : itemIds) {
				String name = (String) table.getContainerProperty(row, "Parametr").getValue();
				String defValue = (String) table.getContainerProperty(row,
						"Wartoœæ domyœlna").getValue();
				String min = (String) table.getContainerProperty(row,
						"Wartoœæ minimalna").getValue();
				String max = (String) table.getContainerProperty(row,
						"Wartoœæ maksymalna").getValue();
				ComboBox combo = (ComboBox) table.getContainerProperty(row, "Typ").getValue();
				String type = (String) combo.getValue();
				if (type.equals("Integer") || type.equals("Long")) {
					int defVal = 0;
					
					int minVal = Integer.MIN_VALUE;
					if (!min.isEmpty()) {
						minVal = Integer.parseInt(min);
					}
					if (!defValue.isEmpty()) {
						defVal = Integer.parseInt(defValue);
					}
					else{
						defVal = minVal;
					}
					int maxVal = Integer.MAX_VALUE;
					if (!max.isEmpty()) {
						maxVal = Integer.parseInt(max);
					}
					if (minVal > maxVal 
							|| defVal < minVal || defVal > maxVal) {
						throw new Exception(
								"Wartoœæ przekracza dozwolony zakres dla parametru "
										+ name);
					}
				} else if (type.equals("Float") || type.equals("Double")) {
					Float defVal = 0.0f;
					
					Float minVal = Float.MIN_VALUE;
					if (!min.isEmpty()) {
						minVal = Float.parseFloat(min);
					}
					if (!defValue.isEmpty()) {
						defVal = Float.parseFloat(defValue);
					}
					else{
						defVal = minVal;
					}
					Float maxVal = Float.MAX_VALUE;
					if (!max.isEmpty()) {
						maxVal = Float.parseFloat(max);
					}
					if (minVal > maxVal 
							|| defVal < minVal || defVal > maxVal) {
						throw new Exception("Wartoœæ przekracza dozwolony zakres dla parametru "
								+ name);
					}
				} else if (type.equals("Boolean")) {
					Boolean.parseBoolean(defValue);
					if (!min.isEmpty() || !max.isEmpty()) {
						throw new Exception(
								"Typ Boolean nie mo¿e mieæ wartoœci maksymalnej ani minimalnej");
					}
					if(!defValue.isEmpty() && (!defValue.equalsIgnoreCase("true") && !defValue.equalsIgnoreCase("false"))){
						throw new Exception("Nieprawid³owa wartoœæ dla typu Boolean w kolumnie \"Wartoœæ domyœlna\"");
					}
				} else if (type.equals("String")) {
					if (!min.isEmpty() || !max.isEmpty()) {
						throw new Exception(
								"Typ String nie mo¿e mieæ wartoœci maksymalnej ani minimalnej");
					}
				}
			}
		} catch (Exception e) {
			String message = e.getMessage();
			if (e instanceof NumberFormatException) {
				message = "B³¹d konwersji typu!";
			}
			Notification notification = new Notification(
					"Tabela zawiera niepoprawne wartoœci <BR/>" + "Szczegó³y b³êdu: "
							+ message, Notification.TYPE_ERROR_MESSAGE);
			notification.setDelayMsec(3000);
			this.getParent().showNotification(notification);
			isValid = false;
		}
		return isValid;
	}
	
}
