package zpi.windows;


import java.util.ArrayList;
import java.util.Collection;

import zpi.hadoopModel.Algorithm;
import zpi.listeners.AddNetworkElementToTableListener;
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
 * Okno sluzace do definiowania schematu sieci jaki wymaga algorytm
 *
 */
@SuppressWarnings("serial")
public class EditSchemaForAlgorithmWindow extends Window {

	private Algorithm algorithm;
	
	TextField delimeterField = null;
	
	public EditSchemaForAlgorithmWindow(Algorithm algorithm){
		super("Definiowanie schematu sieci");
		this.algorithm = algorithm;
		createGUI();
		setHeight(600f, UNITS_PIXELS);
		setWidth(500f, UNITS_PIXELS);;
	}
	
	private void createGUI(){
		
		Label tableLabel = new Label("Zdefiniuj schemat sieci");
		tableLabel.setStyleName("h2");
		
		addComponent(tableLabel);
		
		Table table = new Table();
		table.setStyleName("strong borderless");
		table.setEditable(true);
		table.setPageLength(1); 
		table.addContainerProperty("Nazwa", String.class,  null);
		table.addContainerProperty("Typ", ComboBox.class,  null);
		table.addContainerProperty("Usuñ", Button.class,  null);
		
		
		SchemaTableRow tableRow = new SchemaTableRow(table);
		
		table.addItem(tableRow.toArray(), tableRow);
		addComponent(table);
		
		
		Button addParameterButton = new Button("Nowy wiersz");
		addParameterButton.addListener(new AddNetworkElementToTableListener(table));
		
		Button confirmButton = new Button("ZatwierdŸ zmiany");
		confirmButton.addListener(new ConfirmAlgorithmSchemaListener(this, table));
		confirmButton.setWidth(120f, UNITS_PIXELS);
		
		Button cancelButton = new Button("Anuluj");
		cancelButton.addListener(new CancelEditNetworkElementsListener(this));
		cancelButton.setWidth(120f, UNITS_PIXELS);
		
		addComponent(addParameterButton);
		
		Panel delimeterPanel = new Panel();
		Label delimeterLabel = new Label("Separator");
		delimeterField = new TextField();
		
		HorizontalLayout delimeterHorizontalLayout = new HorizontalLayout();
		delimeterHorizontalLayout.setWidth(250f, UNITS_PIXELS);
		delimeterHorizontalLayout.setMargin(true, false, true, false);
		delimeterHorizontalLayout.addComponent(delimeterLabel);
		delimeterHorizontalLayout.addComponent(delimeterField);
		delimeterHorizontalLayout.setComponentAlignment(delimeterLabel, Alignment.MIDDLE_LEFT);
		delimeterHorizontalLayout.setComponentAlignment(delimeterField, Alignment.MIDDLE_RIGHT);
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
	
	private class ConfirmAlgorithmSchemaListener implements Button.ClickListener {

		Window window = null;
		Table table;
		
		public ConfirmAlgorithmSchemaListener(Window window,Table table){
			this.window = window;
			this.table = table;
		}
		@Override
		public void buttonClick(ClickEvent event) {	
		ArrayList<ArrayList<String>> baseList = new ArrayList<ArrayList<String>>();
		Collection<SchemaTableRow> itemIds = (Collection<SchemaTableRow>) table.getItemIds();
		for (SchemaTableRow row : itemIds){
			String name = (String) table.getContainerProperty(row, "Nazwa").getValue();
			ComboBox combo = (ComboBox) table.getContainerProperty(row, "Typ").getValue();
			String type = (String) combo.getValue();
			
			ArrayList<String> list = new ArrayList<String>();
			
			list.add(name);
			list.add(type);
			baseList.add(list);
		}
		String schema = SchemaParsing.toSchema(baseList);
		algorithm.setSchema(schema);
		algorithm.setDelimiter(delimeterField.getValue().toString());
		((Window)window.getParent()).removeWindow(window);
	}
		
	}
	
	private class CancelEditNetworkElementsListener implements Button.ClickListener {

		Window _window = null;
		
		public CancelEditNetworkElementsListener(Window window){
			_window = window;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			((Window)_window.getParent()).removeWindow(_window);
		}
		
	}
}
