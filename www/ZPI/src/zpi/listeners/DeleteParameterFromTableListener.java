package zpi.listeners;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Table;

/**
 * Listener usuwajacy wiersz z tabeli
 *
 */
@SuppressWarnings("serial")
public class DeleteParameterFromTableListener implements Button.ClickListener {

	private Table table = null;
	
	private Object itemId = null;

	public DeleteParameterFromTableListener(Table table, Object itemId) {
		super();
		this.itemId = itemId;
		this.table = table;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (table.removeItem(itemId)){
			table.setPageLength(table.getPageLength() - 1);
		}
	}
}