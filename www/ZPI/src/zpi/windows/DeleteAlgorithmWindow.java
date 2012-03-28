package zpi.windows;


import zpi.hadoopModel.Algorithm;
import zpi.tabs.ZpiTab;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

/**
 * okno sluzace do usuwania algorytmu
 *
 */
@SuppressWarnings("serial")
public class DeleteAlgorithmWindow extends Window {

	private Algorithm algorithm = null;
	
	private ZpiTab tab;
	
	public DeleteAlgorithmWindow(Algorithm algorithm, ZpiTab tab){
		super("Usuniêcie algorytmu");
		this.algorithm = algorithm;
		this.tab = tab;
		createGUI();
		setHeight(240f, UNITS_PIXELS);
		setWidth(350f, UNITS_PIXELS);
	}
	
	private void createGUI(){
		
		Label questionLabel = new Label("Czy chcesz usun¹æ algorytm "+ algorithm.getShortName() +"?");
		questionLabel.setStyleName("h2");
		questionLabel.setHeight(100f, UNITS_PIXELS);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(300f, UNITS_PIXELS);

		Button yesButton = new Button("Tak");
		yesButton.addListener(new ConfirmDeleteAlgorithmListener(this));
		Button noButton = new Button("Nie");
		noButton.addListener(new CancelDeleteAlgorithmListener(this));
		horizontalLayout.addComponent(yesButton);
		horizontalLayout.addComponent(noButton);
		
		horizontalLayout.setComponentAlignment(yesButton, Alignment.MIDDLE_LEFT);
		horizontalLayout.setComponentAlignment(noButton, Alignment.MIDDLE_RIGHT);
		
		addComponent(questionLabel);
		addComponent(horizontalLayout);
	}
	
	@SuppressWarnings("serial")
	private class ConfirmDeleteAlgorithmListener implements Button.ClickListener {

		Window _window = null;
		
		public ConfirmDeleteAlgorithmListener(Window window){
			_window = window;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			Algorithm.deleteById(algorithm.getId());
			Notification notification = new Notification("Usuniêcie algorytmu przebieg³o pomyœlnie", Notification.TYPE_HUMANIZED_MESSAGE);
			notification.setDelayMsec(2000);
			((Window)_window.getParent()).showNotification(notification);
			((Window)_window.getParent()).removeWindow(_window);
			tab.createGUI();
		}
		
	}
	
	private class CancelDeleteAlgorithmListener implements Button.ClickListener {

		Window _window = null;
		
		public CancelDeleteAlgorithmListener(Window window){
			_window = window;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			((Window)_window.getParent()).removeWindow(_window);
		}
		
	}
}
