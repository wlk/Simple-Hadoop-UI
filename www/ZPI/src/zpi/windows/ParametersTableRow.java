package zpi.windows;

import java.util.ArrayList;

import zpi.components.TypeComboBox;
import zpi.listeners.DeleteParameterFromTableListener;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;

/**
 * Klasa reprezentujaca wiersz w tabeli z parametrami
 *
 */
public class ParametersTableRow{
		
		private String name = "";
		
		private ComboBox combo = new TypeComboBox();
		
		private String defaultValue = "";
		
		private String minValue = "";
		
		private String maxValue = "";
		
		private boolean optional = true;
		
		private Button deleteButton ;
		
		public ParametersTableRow(Table table){
			deleteButton = new Button("Usuñ");
			deleteButton.addListener(new DeleteParameterFromTableListener(table, this));
		}
		
		public ParametersTableRow(ArrayList<String> list, Table table){
			deleteButton = new Button("Usuñ");
			deleteButton.addListener(new DeleteParameterFromTableListener(table, this));
			if (list.size() == 6){
				name = list.get(0);
				combo.setValue(list.get(1));
				defaultValue = list.get(2);
				minValue = list.get(3);
				maxValue = list.get(4);
				optional = Boolean.parseBoolean(list.get(5));
			}
			
		}
		
		public Object[] toArray(){
			return new Object[]{name, combo, defaultValue, minValue , maxValue, optional, deleteButton};
		}
		

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ComboBox getCombo() {
			return combo;
		}

		public void setCombo(ComboBox combo) {
			this.combo = combo;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		public String getMinValue() {
			return minValue;
		}

		public void setMinValue(String minValue) {
			this.minValue = minValue;
		}

		public String getMaxValue() {
			return maxValue;
		}

		public void setMaxValue(String maxValue) {
			this.maxValue = maxValue;
		}

		public boolean isOptional() {
			return optional;
		}

		public void setOptional(boolean optional) {
			this.optional = optional;
		}

		public Button getDeleteButton() {
			return deleteButton;
		}

		public void setDeleteButton(Button deleteButton) {
			this.deleteButton = deleteButton;
		}
		
	}