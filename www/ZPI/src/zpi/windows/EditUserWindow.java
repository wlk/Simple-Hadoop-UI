package zpi.windows;

import zpi.hadoopModel.User;
import zpi.tabs.ZpiTab;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Okno sluzace do edytowania kont uzytkownikow
 *
 */
@SuppressWarnings("serial")
public class EditUserWindow extends Window {

	private TextField loginTextField = null;
	
	private TextField passwordTextField = null;
	
	private CheckBox isAdmin;
	
	private User user;
	
	private boolean isNewUser;
	
	private ZpiTab tab; 

	public EditUserWindow(User user, boolean isNewUser, String caption, ZpiTab tab) {
		super(caption);
		this.user = user;
		this.isNewUser = isNewUser;
		this.tab = tab;
		
		setHeight(400f, UNITS_PIXELS);
		setWidth(380f, UNITS_PIXELS);
		createGUI();
	}

	private void createGUI() {

		VerticalLayout generalLayout = new VerticalLayout();

		Label infoLabel = new Label("Edycja uøytkownika");
		if (isNewUser){
			infoLabel = new Label("Dodawanie uøytkownika");
		}
		infoLabel.setStyleName("h2");
		generalLayout.setSizeFull();

		generalLayout.addComponent(infoLabel);
		generalLayout.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);

		HorizontalLayout hl1 = new HorizontalLayout();
		Label loginLabel = new Label("Login:");
		loginLabel.setWidth(120f, UNITS_PIXELS);
		loginTextField = new TextField();
		loginTextField.setValue(user.getName());
		hl1.setMargin(true);
		hl1.addComponent(loginLabel);
		hl1.addComponent(loginTextField);

		HorizontalLayout hl2 = new HorizontalLayout();
		Label passwordLabel = new Label("Has≥o:");
		passwordLabel.setWidth(120f, UNITS_PIXELS);
		passwordTextField = new TextField();
		passwordTextField.setValue(user.getPassword());
		hl2.setMargin(true);
		hl2.addComponent(passwordLabel);
		hl2.addComponent(passwordTextField);
		
		HorizontalLayout hl3 = new HorizontalLayout();
		Label isAdminLabel = new Label("Uprawnienia administratora:");
		isAdmin = new CheckBox();
		isAdmin.setValue(true);
		hl3.setMargin(true);
		hl3.addComponent(isAdminLabel);
		hl3.addComponent(isAdmin);
		
		
		generalLayout.setMargin(true, true, true, true);

		generalLayout.addComponent(hl1);
		generalLayout.addComponent(hl2);
		generalLayout.addComponent(hl3);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(280f, UNITS_PIXELS);

		Button yesButton = new Button("Potwierdü");
		yesButton.addListener(new ConfirmEditUserListener(this));
		Button noButton = new Button("Anuluj");
		noButton.addListener(new CancelEditUserListener(this));
		horizontalLayout.addComponent(yesButton);
		horizontalLayout.addComponent(noButton);
		
		horizontalLayout.setComponentAlignment(yesButton, Alignment.MIDDLE_LEFT);
		horizontalLayout.setComponentAlignment(noButton, Alignment.MIDDLE_RIGHT);

		generalLayout.addComponent(horizontalLayout);
		
		addComponent(generalLayout);
	}

	private class ConfirmEditUserListener implements Button.ClickListener {

		Window _window;
		
		public ConfirmEditUserListener(Window window){
			_window = window;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			
			String login = (String) loginTextField.getValue();
			String password = (String) passwordTextField.getValue();
			
			// jesli uzytkownik nie istnial wtedy trzeba sprawdzic czy nazwa sie nie zdubluje
			if (isNewUser){
				User userExists = User.getByName(login);
				if (userExists != null && userExists.getRemovedAt() == null){
					Notification notification = new Notification("Uøytkownik o takim loginie juø istnieje!", Notification.TYPE_ERROR_MESSAGE);
					notification.setDelayMsec(3000);
					_window.showNotification(notification);
					return;
				}
			}
			user.setName(login);
			user.setPassword(password);
			boolean isAdminBool = (Boolean) isAdmin.getValue();
			user.setAdmin(isAdminBool? 1 : 0);
			user.save();
			Notification notification = new Notification("Zmiany zosta≥y zapisane!");
			notification.setDelayMsec(3000);
			_window.showNotification(notification);
			((Window) _window.getParent()).removeWindow(_window);
			tab.createGUI();

		}
		
	}

	private class CancelEditUserListener implements
			Button.ClickListener {

		Window _window = null;

		public CancelEditUserListener(Window window) {
			_window = window;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			((Window) _window.getParent()).removeWindow(_window);
		}

	}
}
