package zpi.listeners;

import zpi.tabs.ZpiTab;

import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;
import com.vaadin.ui.TabSheet;

/**
 * Listener zmieniajacy zakladki
 *
 */
@SuppressWarnings("serial")
public class TabChangedListener implements Listener {

	private TabSheet tabSheet; 
	
	public TabChangedListener(TabSheet sheet){
		super();
		tabSheet = sheet;
	}
	
	@Override
	public void componentEvent(Event event) {
		ZpiTab selectedTab = (ZpiTab) tabSheet.getSelectedTab();
		selectedTab.createGUI();
	}

}
