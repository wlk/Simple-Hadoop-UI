package zpi.auth;

import zpi.MainWindow;
import zpi.ZpiApplication;
import zpi.ZpiContext;
import zpi.hadoopModel.User;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Okno logowania.
 *
 */
@SuppressWarnings("serial")
public class LoginWindow extends Window {
	
	private ZpiApplication application = null;
	
	private TextField loginTextField = null;
	
	private PasswordField passwordTextField = null;
	
	private ZpiContext context = null;
	
	public LoginWindow(ZpiApplication application, ZpiContext context){
		super();
		this.application = application;
		this.context = context;
	}
	
	public void init(){
		
		VerticalLayout generalLayout = new VerticalLayout();
		Panel panel = new Panel();
		panel.setWidth(350f, UNITS_PIXELS);
		panel.setHeight(260f, UNITS_PIXELS);
		VerticalLayout panelLayout = new VerticalLayout();
		Label infoLabel = new Label("Logowanie do aplikacji");
		infoLabel.setStyleName("h2");
		panelLayout.setSizeFull();
		panelLayout.addComponent(infoLabel);
		panelLayout.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);
		
		HorizontalLayout hl1 = new HorizontalLayout();
		Label loginLabel = new Label("Login:");
		loginLabel.setWidth(70f, UNITS_PIXELS);
		loginTextField = new TextField();
		hl1.setMargin(true);
		hl1.addComponent(loginLabel);
		hl1.addComponent(loginTextField);
		
		HorizontalLayout hl2 = new HorizontalLayout();
		Label passwordLabel = new Label("Has³o:");
		passwordLabel.setWidth(70f, UNITS_PIXELS);
		passwordTextField = new PasswordField();
		hl2.setMargin(true);
		hl2.addComponent(passwordLabel);
		hl2.addComponent(passwordTextField);
		
		Button loginButton = new Button("Zaloguj");
		loginButton.setClickShortcut(KeyCode.ENTER);
		loginButton.addListener(new LoginListener(this));
		
		panelLayout.setMargin(true, true, true, true);
		
		panelLayout.addComponent(hl1);
		panelLayout.addComponent(hl2);
		panelLayout.addComponent(loginButton);
		panelLayout.setComponentAlignment(loginButton, Alignment.MIDDLE_CENTER);
		panel.addComponent(panelLayout);
		generalLayout.addComponent(panel);
		generalLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		addComponent(generalLayout);
		
	}
	
	private class LoginListener implements Button.ClickListener {

		Window _window;
		
		public LoginListener(Window window){
			_window = window;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			
			String login = (String) loginTextField.getValue();
			String password = (String) passwordTextField.getValue();
			boolean found = isOk(login, password);
			
			if (found){
				application.removeWindow(_window);
				MainWindow mainWindow = new MainWindow(application, context);
				mainWindow.init();
				application.setMainWindow(mainWindow);
			}
			else{
				Notification notification = new Notification("Niepoprawny login lub has³o!", Notification.TYPE_WARNING_MESSAGE);
				notification.setDelayMsec(2000);
				_window.showNotification(notification);
			}
		}
		
	}
	
	private boolean isOk(String login, String pass){
		User user = User.getByName(login);
		if (user != null && user.getRemovedAt() == null){
			if (user.getPassword().equals(pass)){
				context.setUser(user);
				return true;
			}
		}
		return false;
	}

}
