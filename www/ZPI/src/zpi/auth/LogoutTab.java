package zpi.auth;

import zpi.MainWindow;
import zpi.tabs.ZpiTab;

import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * Zakladka do wylogowywania.
 *
 */
@SuppressWarnings("serial")
public class LogoutTab extends ZpiTab{

	private Application application = null;
	
	public LogoutTab(Application application, MainWindow mainWindow){

		super(mainWindow);
		this.application = application;
	}
	
	
	public void createGUI(){
		
		removeAllComponents();
		VerticalLayout generalLayout = new VerticalLayout();
		
		Panel panel = new Panel();
		panel.setWidth(280f, UNITS_PIXELS);
		panel.setHeight(100f, UNITS_PIXELS);
		
		VerticalLayout panelLayout = new VerticalLayout();
	
		Label infoLabel = new Label("Czy chcesz siê wylogowaæ?");
		infoLabel.setStyleName("h2");
		panelLayout.setSizeFull();
		
		panelLayout.addComponent(infoLabel);
		panelLayout.setComponentAlignment(infoLabel, Alignment.MIDDLE_CENTER);
		
		Button logoutButton = new Button("Wyloguj");
		logoutButton.addListener(new LogoutListener());
		panelLayout.addComponent(logoutButton);
		panelLayout.setComponentAlignment(logoutButton, Alignment.MIDDLE_CENTER);
		
		panel.addComponent(panelLayout);
		
		generalLayout.addComponent(panel);
		generalLayout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
		
		addComponent(generalLayout);
		
	}
	
	private class LogoutListener implements Button.ClickListener {
		
		@Override
		public void buttonClick(ClickEvent event) {
				application.close();
		}
		
	}
}
