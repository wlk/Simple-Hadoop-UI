package zpi.windows;


import zpi.hadoopModel.Network;
import zpi.tabs.ZpiTab;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

/**
 * Okno sluzace do usuwania sieci
 *
 */
@SuppressWarnings("serial")
public class DeleteNetworkWindow extends Window {

	private Network network = null;
	
	private ZpiTab tab;
	
	public DeleteNetworkWindow(Network network, ZpiTab tab){
		super("Usuniêcie sieci");
		this.network = network;
		this.tab = tab;
		createGUI();
		setHeight(200f, UNITS_PIXELS);
		setWidth(300f, UNITS_PIXELS);
	}
	
	private void createGUI(){
		
		Label questionLabel = new Label("Czy chcesz usun¹æ sieæ o ID "+ network.getId() +"?");
		questionLabel.setStyleName("h2");
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(200f, UNITS_PIXELS);

		Button yesButton = new Button("Tak");
		yesButton.addListener(new ConfirmDeleteNetworkListener(this));
		Button noButton = new Button("Nie");
		noButton.addListener(new CancelDeleteNetworkListener(this));
		horizontalLayout.addComponent(yesButton);
		horizontalLayout.addComponent(noButton);
		
		horizontalLayout.setComponentAlignment(yesButton, Alignment.MIDDLE_LEFT);
		horizontalLayout.setComponentAlignment(noButton, Alignment.MIDDLE_RIGHT);
		
		addComponent(questionLabel);
		addComponent(horizontalLayout);
	}
	
	private class ConfirmDeleteNetworkListener implements Button.ClickListener {

		Window _window = null;
		
		public ConfirmDeleteNetworkListener(Window window){
			_window = window;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			Network.deleteById(network.getId());
			((Window)_window.getParent()).showNotification("Usuniecie sieci przebieg³o pomyœlnie");
			((Window)_window.getParent()).removeWindow(_window);
			tab.createGUI();
			
		}
		
	}
	
	private class CancelDeleteNetworkListener implements Button.ClickListener {

		Window _window = null;
		
		public CancelDeleteNetworkListener(Window window){
			_window = window;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			((Window)_window.getParent()).removeWindow(_window);
		}
		
	}
}
