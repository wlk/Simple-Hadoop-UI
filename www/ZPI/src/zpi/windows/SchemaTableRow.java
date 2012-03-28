package zpi.windows;

import java.util.ArrayList;

import zpi.components.TypeComboBox;
import zpi.listeners.DeleteParameterFromTableListener;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;

/**
 * Klasa reprezentujaca wiersz w tabeli ze schematem
 *
 */
public class SchemaTableRow{
		
		private String name = "";
		
		private ComboBox combo = new TypeComboBox();
		
		private Button deleteButton ;
		
		public SchemaTableRow(Table table){
			deleteButton = new Button("Usuñ");
			deleteButton.addListener(new DeleteParameterFromTableListener(table, this));
		}
		
		public SchemaTableRow(ArrayList<String> list, Table table){
			deleteButton = new Button("Usuñ");
			deleteButton.addListener(new DeleteParameterFromTableListener(table, this));
			if (list.size() == 2){
				name = list.get(0);
				combo.setValue(list.get(1));
			}
			
		}
		
		public Object[] toArray(){
			return new Object[]{name, combo, deleteButton};
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


		public Button getDeleteButton() {
			return deleteButton;
		}

		public void setDeleteButton(Button deleteButton) {
			this.deleteButton = deleteButton;
		}
		
	}