package zpi;

import zpi.auth.LoginWindow;
import zpi.hadoopModel.User;

import com.vaadin.Application;

/**
 * Klasa aplikacji.
 *
 */
@SuppressWarnings("serial")
public class ZpiApplication extends Application {
	
	private User loggedUser = null;
	
	@Override
	public void init() {
		setTheme("reindeer");
		ZpiContext context = new ZpiContext();
		
		LoginWindow loginWindow = new LoginWindow(this, context);
		loginWindow.init();
		
		setMainWindow(loginWindow);
		
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User _loggedUser) {
		this.loggedUser = _loggedUser;
	}

}
