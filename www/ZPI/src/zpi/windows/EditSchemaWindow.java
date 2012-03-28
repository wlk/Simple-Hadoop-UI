package zpi.windows;


import java.util.ArrayList;
import java.util.Collection;

import zpi.hadoopModel.INetAlg;
import zpi.listeners.AddNetworkElementToTableListener;
import zpi.listeners.CopySchemaListener;
import zpi.tabs.ZpiTab;
import zpi.tools.SchemaParsing;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

/**
 * Okno sluzace do definiowania schematu
 *
 */
@SuppressWarnings("serial")
public class EditSchemaWindow extends Window {

	private INetAlg netAlg = null;
	
	private TextField delimeterField = null;
	
	private ZpiTab tab;
	
	private String schema;
	
	private String delimiter;
	
	public EditSchemaWindow(INetAlg netAlg, ZpiTab tab){
		super("Definiowanie schematu sieci");
		this.netAlg = netAlg;
		this.tab = tab;
		this.schema = netAlg.getSchema();
		this.delimiter = netAlg.getDelimiter();
		createGUI();
		setHeight(650f, UNITS_PIXELS);
		setWidth(500f, UNITS_PIXELS);;
	}
	
	public void createGUI(){
		
		Label tableLabel = new Label("Zdefiniuj schemat sieci");
		tableLabel.setStyleName("h2");
		
		addComponent(tableLabel);
		
		Table table = new Table();
		table.setStyleName("strong borderless");
		table.setEditable(true);
		table.addContainerProperty("Nazwa", String.class,  null);
		table.addContainerProperty("Typ", ComboBox.class,  null);
		table.addContainerProperty("Usuñ parametr", Button.class,  null);
		
		ArrayList<ArrayList<String>> fromSchema = SchemaParsing.fromSchema(schema);
		if (!fromSchema.isEmpty()){
			table.setPageLength(fromSchema.size());
			for (ArrayList<String> list : fromSchema){
				SchemaTableRow row = new SchemaTableRow(list, table);
				table.addItem(row.toArray(), row);
			}
		}
		else{
			table.setPageLength(1);
			SchemaTableRow row = new SchemaTableRow(table);
			table.addItem(row.toArray(), row);
		}
		
		addComponent(table);
		
		Panel delimeterPanel = new Panel();
		Label delimeterLabel = new Label("Separator");
		delimeterField = new TextField();
		if (delimiter != null && delimiter != "null"){
			delimeterField.setValue(delimiter);
		}
		
		HorizontalLayout delimeterHorizontalLayout = new HorizontalLayout();
		delimeterHorizontalLayout.setWidth(250f, UNITS_PIXELS);
		delimeterHorizontalLayout.setMargin(true, false, true, false);
		delimeterLabel.setWidth(120f, UNITS_PIXELS);
		delimeterField.setWidth(120f, UNITS_PIXELS);
		delimeterHorizontalLayout.addComponent(delimeterLabel);
		delimeterHorizontalLayout.addComponent(delimeterField);
		delimeterHorizontalLayout.setComponentAlignment(delimeterLabel, Alignment.MIDDLE_LEFT);
		delimeterHorizontalLayout.setComponentAlignment(delimeterField, Alignment.MIDDLE_RIGHT);
		delimeterPanel.addComponent(delimeterHorizontalLayout);
		
		Button addParameterButton = new Button("Nowy wiersz");
		addParameterButton.addListener(new AddNetworkElementToTableListener(table));
		addParameterButton.setWidth(120f, UNITS_PIXELS);
		
		Button schemaButton = new Button("Kopiuj schemat");
		schemaButton.addListener(new CopySchemaListener(tab, this));
		schemaButton.setWidth(120f, UNITS_PIXELS);
		
		Button confirmButton = new Button("ZatwierdŸ zmiany");
		confirmButton.addListener(new ConfirmEditSchemaElementListener(this, table));
		confirmButton.setWidth(120f, UNITS_PIXELS);
		
		Button cancelButton = new Button("Anuluj");
		cancelButton.addListener(new CancelEditSchemaElementsListener(this));
		cancelButton.setWidth(120f, UNITS_PIXELS);
		
		Panel schemaPanel = new Panel();
		HorizontalLayout schemaHorizontalLayout = new HorizontalLayout();
		schemaHorizontalLayout.setWidth(250f, UNITS_PIXELS);
		schemaHorizontalLayout.setMargin(true, false, true, false);
		schemaHorizontalLayout.addComponent(addParameterButton);
		schemaHorizontalLayout.addComponent(schemaButton);
		schemaHorizontalLayout.setComponentAlignment(addParameterButton, Alignment.MIDDLE_LEFT);
		schemaHorizontalLayout.setComponentAlignment(schemaButton, Alignment.MIDDLE_RIGHT);
		schemaPanel.addComponent(schemaHorizontalLayout);
		
		addComponent(schemaHorizontalLayout);
//		addComponent(addParameterButton);
		addComponent(delimeterHorizontalLayout);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(470f, UNITS_PIXELS);
		horizontalLayout.setHeight(80f, UNITS_PIXELS);
		horizontalLayout.addComponent(confirmButton);
		horizontalLayout.addComponent(cancelButton);
		horizontalLayout.setComponentAlignment(confirmButton, Alignment.MIDDLE_CENTER);
		horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
		
		addComponent(horizontalLayout);
	}
	
	public void setSchema(String schema){
		this.schema = schema;
	}
	
	public void setDelimiter(String delimiter){
		this.delimiter = delimiter;
	}
	
	private class ConfirmEditSchemaElementListener implements
			Button.ClickListener {

		Window window = null;
		Table table;

		public ConfirmEditSchemaElementListener(Window window, Table table) {
			this.window = window;
			this.table = table;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			ArrayList<ArrayList<String>> baseList = new ArrayList<ArrayList<String>>();
			Collection<SchemaTableRow> itemIds = (Collection<SchemaTableRow>) table
					.getItemIds();
			for (SchemaTableRow row : itemIds) {
				String name = (String) table.getContainerProperty(row, "Nazwa")
						.getValue();
				ComboBox combo = (ComboBox) table.getContainerProperty(row,
						"Typ").getValue();
				String type = (String) combo.getValue();

				ArrayList<String> list = new ArrayList<String>();

				list.add(name);
				list.add(type);
				baseList.add(list);
			}
			String schema = SchemaParsing.toSchema(baseList);
			boolean needsToBeSaved = false;
			if (netAlg.getSchema() != null) {
				needsToBeSaved = true;
			}
			netAlg.setSchema(schema);
			netAlg.setDelimiter(delimeterField.getValue().toString());
			if (needsToBeSaved) {
				netAlg.save();
			}
			((Window) window.getParent()).removeWindow(window);
		}
		
	}
	
	private class CancelEditSchemaElementsListener implements Button.ClickListener {

		Window _window = null;
		
		public CancelEditSchemaElementsListener(Window window){
			_window = window;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			((Window)_window.getParent()).removeWindow(_window);
		}
		
	}
	
	
}
