package zpi.auth;

import zpi.MainWindow;
import zpi.hadoopModel.User;
import zpi.tabs.ZpiTab;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window.Notification;

/**
 * Zakladka do zarzadzania kontem.
 *
 */
@SuppressWarnings("serial")
public class ManageAccountTab extends ZpiTab {

	private PasswordField oldPassField;
	
	private PasswordField newPassField;
	
	private PasswordField newPassField2;
	
	public ManageAccountTab(MainWindow window) {
		super(window);
	}

	public void createGUI() {

		removeAllComponents();
		VerticalLayout generalLayout = new VerticalLayout();

		Panel panel = new Panel();
		panel.setWidth(350f, UNITS_PIXELS);

		VerticalLayout panelLayout = new VerticalLayout();

		Label infoLabel = new Label("Zmiana has³a");
		infoLabel.setStyleName("h2");
		panelLayout.setSizeFull();

		panelLayout.addComponent(infoLabel);
		panelLayout.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);

		HorizontalLayout hl1 = new HorizontalLayout();
		Label oldPass = new Label("Stare has³o:");
		oldPassField = new PasswordField();
		oldPass.setWidth(150f, UNITS_PIXELS);
		hl1.setMargin(true);
		hl1.addComponent(oldPass);
		hl1.addComponent(oldPassField);

		HorizontalLayout hl2 = new HorizontalLayout();
		Label newPass = new Label("Nowe has³o:");
		newPass.setWidth(150f, UNITS_PIXELS);
		newPassField = new PasswordField();
		hl2.setMargin(true);
		hl2.addComponent(newPass);
		hl2.addComponent(newPassField);
		
		HorizontalLayout hl3 = new HorizontalLayout();
		Label newPass2 = new Label("Powtórz nowe has³o:");
		newPass2.setWidth(150f, UNITS_PIXELS);
		newPassField2 = new PasswordField();
		hl3.setMargin(true);
		hl3.addComponent(newPass2);
		hl3.addComponent(newPassField2);
		
		panelLayout.addComponent(hl1);
		panelLayout.addComponent(hl2);
		panelLayout.addComponent(hl3);
		
		Button changeButton = new Button("Zmieñ has³o!");
		changeButton.addListener(new ChangePasswordsListener());
		panelLayout.addComponent(changeButton);
		panelLayout
				.setComponentAlignment(changeButton, Alignment.MIDDLE_CENTER);

		panel.addComponent(panelLayout);

		generalLayout.addComponent(panel);
		generalLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);

		addComponent(generalLayout);

	}

	private class ChangePasswordsListener implements Button.ClickListener {

		@Override
		public void buttonClick(ClickEvent event) {
			User user = mainWindow.getContext().getUser();
			String oldPass = (String) oldPassField.getValue();
			String password = user.getPassword();
			if (!oldPass.equals(password)){
				Notification notification = new Notification("Podane stare has³o jest nieprawid³owe!", Notification.TYPE_WARNING_MESSAGE);
				notification.setDelayMsec(2000);
				mainWindow.showNotification(notification);
				return;
			}
			
			String newPass = (String) newPassField.getValue();
			String newPass2 = (String) newPassField2.getValue();
			
			if (!newPass.equals(newPass2)){
				Notification notification = new Notification("Dok³adnie powtórz nowe has³o!", Notification.TYPE_WARNING_MESSAGE);
				notification.setDelayMsec(2000);
				mainWindow.showNotification(notification);
				return;
			}
			
			user.setPassword(newPass);
			user.save();
			Notification notification = new Notification("Has³o zosta³o zmienione!", Notification.TYPE_WARNING_MESSAGE);
			notification.setDelayMsec(2000);
			mainWindow.showNotification(notification);
		}

	}
}
