package zpi.windows;


import zpi.hadoopModel.User;
import zpi.tabs.ZpiTab;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

/**
 * Okno do usuwania uzytkownikow
 *
 */
@SuppressWarnings("serial")
public class DeleteUserWindow extends Window {

	private User user = null;
	
	private ZpiTab tab;
	
	public DeleteUserWindow(User user, ZpiTab tab){
		super("Usuniêcie u¿ytkownika");
		this.user = user;
		this.tab = tab;
		createGUI();
		setHeight(200f, UNITS_PIXELS);
		setWidth(300f, UNITS_PIXELS);
	}
	
	private void createGUI(){
		
		Label questionLabel = new Label("Czy chcesz usun¹æ u¿ytkownika "+ user.getName() +"?");
		questionLabel.setStyleName("h2");
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(200f, UNITS_PIXELS);

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
			User.removeUserData(user.getId());
			Notification notification = new Notification("Usuniêcie u¿ytkownika przebieg³o pomyœlnie", Notification.TYPE_HUMANIZED_MESSAGE);
			notification.setDelayMsec(2000);
			((Window)_window.getParent()).showNotification(notification);
			((Window)_window.getParent()).removeWindow(_window);
			tab.createGUI();
		}
		
	}
	
	@SuppressWarnings("serial")
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
