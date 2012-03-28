package zpi.listeners;

import zpi.MainWindow;
import zpi.tabs.ZpiTab;
import zpi.windows.CopySchemaWindow;
import zpi.windows.EditSchemaWindow;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

/**
 * Listener wykorzystywany do kopiowania schematu
 *
 */
@SuppressWarnings("serial")
public class CopySchemaListener implements Button.ClickListener {

		private ZpiTab tab;
		
		private EditSchemaWindow window;
	
		public CopySchemaListener(ZpiTab tab, EditSchemaWindow window){
			this.tab = tab;
			this.window = window;
		}
	
		@Override
		public void buttonClick(ClickEvent event) {

			MainWindow mainWindow = tab.getMainWindow();
			Window copySchemaWindow = new CopySchemaWindow(mainWindow.getContext().getUser().getId(), window);
			copySchemaWindow.setModal(true);
			mainWindow.addWindow(copySchemaWindow);
			
			
			
		}
	}