package zpi.listeners;

import zpi.windows.SchemaTableRow;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;

/**
 * Listener wykorzystywany do dodawania zawartosci do tabeli przy definiowaniu sieci.
 *
 */
@SuppressWarnings("serial")
public class AddNetworkElementToTableListener implements Button.ClickListener {

	private Table table = null;

	public AddNetworkElementToTableListener(Table table) {
		super();
		this.table = table;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		SchemaTableRow row = new SchemaTableRow(table);
		Object returnValue = table.addItem(row.toArray(), row);
		if (returnValue != null){
			table.setPageLength(table.getPageLength() + 1);
		}
	}
}
