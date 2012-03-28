package zpi;

import zpi.auth.LogoutTab;
import zpi.auth.ManageAccountTab;
import zpi.auth.ManageUsersTab;
import zpi.listeners.TabChangedListener;
import zpi.tabs.ManageAlgorithmsTab;
import zpi.tabs.ManageNetworksTab;
import zpi.tabs.PublicAlgorithmsTab;
import zpi.tabs.UserAlgorithmsTab;
import zpi.tabs.WelcomePageTab;

import com.vaadin.Application;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Klasa reprezentuj퉏a podstawowe okno aplikacji. 
 */
@SuppressWarnings("serial")
public class MainWindow extends Window {

	private Panel topPanel = null;
	
	private Panel mainPanel = null; 
	
	private TabSheet tabSheet = null;
	
	private ZpiApplication application = null;
	
	private ZpiContext context;
	
	public MainWindow(ZpiApplication app, ZpiContext context){
		
		super();
		setContext(context);
		application = app;
		topPanel = new Panel();
		mainPanel = new Panel();
	}
	
	public void init(){
		initTop();
		initMain();
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addComponent(topPanel);
		verticalLayout.addComponent(mainPanel);
		addComponent(verticalLayout);
	}
	
	private void initTop(){
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(getWidth());
		
		Embedded em = new Embedded (null, new ThemeResource("img/logo-1-lowres.png"));
		em.setType(Embedded.TYPE_IMAGE);
		em.setHeight(130, UNITS_PIXELS);
		Label nameLabel = new Label("Platforma do przetwarzania sieci spo쿮cznych w chmurze");
		nameLabel.setStyleName("h1");
		nameLabel.setSizeFull();
		horizontalLayout.addComponent(em);
		horizontalLayout.addComponent(nameLabel);
		horizontalLayout.setComponentAlignment(nameLabel, Alignment.TOP_LEFT);
		horizontalLayout.setSizeFull();
		topPanel.setSizeFull();
		topPanel.setLayout(horizontalLayout);
	}
	
	private void initTabs(){
		tabSheet = new TabSheet();
		tabSheet.setStyleName("borderless");
		
		WelcomePageTab startPage = new WelcomePageTab("Strona powitalna", this);
		startPage.createGUI();
		tabSheet.addTab(startPage).setCaption("Status kolejki");
		
		tabSheet.addTab(new PublicAlgorithmsTab(this)).setCaption("Algorytmy publiczne");
		tabSheet.addTab(new UserAlgorithmsTab(this)).setCaption("Algorytmy u퓓tkownika");
		tabSheet.addTab(new ManageAlgorithmsTab("Zarz퉐zaj algorytmami", this)).setCaption("Zarz퉐zaj algorytmami");
		tabSheet.addTab(new ManageNetworksTab("Zarz퉐zaj sieciami", this)).setCaption("Zarz퉐zaj sieciami");
		tabSheet.addTab(new ManageAccountTab(this)).setCaption("Zarz퉐zaj kontem");
		if (context.getUser().isAdmin()){
			tabSheet.addTab(new ManageUsersTab(this)).setCaption("Zarz퉐zaj u퓓tkownikami");
		}
		tabSheet.addTab(new LogoutTab(application, this)).setCaption("Wyloguj");
		tabSheet.addListener(new TabChangedListener(tabSheet));
		mainPanel.addComponent(tabSheet);
	}
		
	private void initMain(){

		
		initTabs();
	}

	public Panel getMainPanel() {
		return mainPanel;
	}
	
	public Application getMyApplication(){
		return application;
	}

	public ZpiContext getContext() {
		return context;
	}

	public void setContext(ZpiContext _context) {
		this.context = _context;
	}
	
	public TabSheet getTabSheet(){
		return tabSheet;
	}
	
	
	
	
}
