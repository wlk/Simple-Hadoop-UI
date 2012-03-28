package zpi.listeners;

import zpi.windows.ParametersTableRow;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;

/**
 * Listener wykorzystywany przy dodawaniu zawartosci do tabeli
 *
 */
@SuppressWarnings("serial")
public class AddParameterToTableListener implements Button.ClickListener {

	private Table table = null;

	public AddParameterToTableListener(Table table) {
		super();
		this.table = table;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		ParametersTableRow row = new ParametersTableRow(table);
		Object returnValue = table.addItem(row.toArray(), row);
		if (returnValue != null){
			table.setPageLength(table.getPageLength() + 1);
		}
	}
}
