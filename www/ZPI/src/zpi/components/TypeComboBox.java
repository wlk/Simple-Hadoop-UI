package zpi.components;

import com.vaadin.ui.ComboBox;

/**
 * Combobox sluzacy do wyboru typu
 *
 */
@SuppressWarnings("serial")
public class TypeComboBox extends ComboBox {

	public TypeComboBox(){
		super();
		addItem("Integer");
		addItem("String");
		addItem("Long");
		addItem("Boolean");
		addItem("Float");
		addItem("Double");
		setNullSelectionAllowed(false);
		setValue("Integer");
	}
	
}
