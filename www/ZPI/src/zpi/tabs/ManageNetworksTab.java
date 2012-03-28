package zpi.tabs;

import java.io.IOException;
import java.util.List;

import zpi.MainWindow;
import zpi.components.NetworkFileUploader;
import zpi.hadoopModel.Network;
import zpi.windows.DeleteNetworkWindow;
import zpi.windows.EditSchemaWindow;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Zakladka do zarzadzania siecami
 *
 */
@SuppressWarnings("serial")
public class ManageNetworksTab extends ZpiTab{

	private MainWindow mainWindow;
	
	private CheckBox isPublicCheckbox;
	
	private TextField nameTextField;
	
	private NetworkFileUploader networkUploader;
	
	public ManageNetworksTab(String string, MainWindow mainWindow) {
		super(string, mainWindow);
		this.mainWindow = mainWindow;
	}

	public void createGUI(){
		
		removeAllComponents();
		HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
		hsplit.setWidth(900f, UNITS_PIXELS);
		
		Component leftPanel = createLeftPanel();
		Component rightPanel = createRightPanel();
		hsplit.setFirstComponent(leftPanel);
		hsplit.setSecondComponent(rightPanel);

		hsplit.setSplitPosition(60, Sizeable.UNITS_PERCENTAGE);
		
		addComponent(hsplit);
	}
	
	private Component createLeftPanel(){
		
		Network network = new Network();
		 
		VerticalLayout wholeLayout = new VerticalLayout();

		Label topLabel = new Label("Dodaj sieæ");
		topLabel.setStyle("h2");
		wholeLayout.addComponent(topLabel);
		
		Label inputNameLabel = new Label("Wpisz nazwê");
		Label isPublicLabel = new Label("Sieæ publiczna");
		Label inputFileLabel = new Label("Wybierz plik");
		
		inputNameLabel.setWidth(100f, UNITS_PIXELS);
		inputFileLabel.setWidth(100f, UNITS_PIXELS);
		
		nameTextField = new TextField();
		isPublicCheckbox = new CheckBox();
		isPublicCheckbox.setValue(true);
		networkUploader = new NetworkFileUploader(mainWindow, network);
		GridLayout gridLayout = new GridLayout(2, 3);
		gridLayout.addComponent(inputNameLabel, 0, 0);
		gridLayout.addComponent(isPublicLabel, 0, 1);
		gridLayout.addComponent(inputFileLabel, 0, 2);
		
		gridLayout.addComponent(nameTextField, 1, 0);
		gridLayout.addComponent(isPublicCheckbox, 1, 1);
		gridLayout.addComponent(networkUploader, 1, 2);
		
		gridLayout.setMargin(true, false, true, false);
		
		wholeLayout.addComponent(gridLayout);
		
		VerticalLayout verticalLayout = new VerticalLayout();
		
		Panel algorithmParametersPanel = new Panel();
		algorithmParametersPanel.setWidth(500f, UNITS_PIXELS);
		algorithmParametersPanel.setHeight(100f, UNITS_PIXELS);

		Label tableLabel = new Label("Zdefiniuj schemat sieci");
		tableLabel.setStyleName("h2");
		Button setParametersButton = new Button("Zdefiniuj schemat");
		setParametersButton.addListener(new EditNetworkListener(network));
		
		verticalLayout.addComponent(tableLabel);
		tableLabel.setHeight(30f, UNITS_PIXELS);
		tableLabel.setWidth(null);
		verticalLayout.addComponent(setParametersButton);
		verticalLayout.setComponentAlignment(tableLabel, Alignment.MIDDLE_CENTER);
		verticalLayout.setComponentAlignment(setParametersButton, Alignment.MIDDLE_CENTER);
		
		algorithmParametersPanel.addComponent(verticalLayout);
		
		wholeLayout.addComponent(algorithmParametersPanel);
		VerticalLayout sendLayout = new VerticalLayout();
		sendLayout.setWidth(500f, UNITS_PIXELS);
		sendLayout.setHeight(80f, UNITS_PIXELS);
		
		Label sendLabel = new Label("PotwierdŸ dodanie sieci");
		sendLabel.setStyleName("h2");
		
		Button sendButton = new Button("Wyœlij");
		sendButton.addListener(new ConfirmNetworkListener(network));
		
		sendLayout.addComponent(sendLabel);
		sendLabel.setWidth(null);
		sendLayout.addComponent(sendButton);
		
		sendLayout.setComponentAlignment(sendLabel, Alignment.MIDDLE_CENTER);
		sendLayout.setComponentAlignment(sendButton, Alignment.MIDDLE_CENTER);
		
		wholeLayout.addComponent(sendLayout);
		
		return wholeLayout;
	}
	
	private Component createRightPanel(){

		Table table = new Table();
		table.setStyleName("strong borderless");
		
		List<Network> networks = Network.getNetworksByUserID(mainWindow.getContext().getUser().getId());
		if (networks != null){
			
			table.setPageLength(networks.size());
			table.addContainerProperty("Nazwa sieci", String.class,  null);
			table.addContainerProperty("Edytuj schemat", Button.class,  null);
			table.addContainerProperty("Usuñ", Button.class,  null);
			
			for (Network network : networks){
	
				Button editButton = new Button("Edytuj");
				editButton.addListener(new EditNetworkListener(network));
				Button deleteButton = new Button("Usuñ");
				deleteButton.addListener(new DeleteNetworkListener(network));
				
				table.addItem(new Object[]{network.getName(), editButton, deleteButton}, network);
			}
		}
		
		return table;
	}

	private class EditNetworkListener implements Button.ClickListener{
		
		Network _network = null;
		
		public EditNetworkListener(Network network){
			_network = network;
		}
		
		@Override
	    public void buttonClick(ClickEvent event) {
			
			Window editNetworkmWindow = new EditSchemaWindow(_network, ManageNetworksTab.this);
			editNetworkmWindow.setModal(true);
			getWindow().addWindow(editNetworkmWindow);
	        
	    }
	}
	
	private class DeleteNetworkListener implements Button.ClickListener{
		
		Network _network = null;
		
		public DeleteNetworkListener(Network network){
			_network = network;
		}
		
		@Override
	    public void buttonClick(ClickEvent event) {
			
			try {
	        	Process proc = Runtime.getRuntime().exec(new String[] { 
						"java", 
						"-cp", 
						"/usr/lib/hadoop/conf/:/home/wn/ZPI.jar", 
						"pl.wroc.pwr.zpi.standAlone.DeleteNetwork", 
						_network.getNetworkFileLocation() });
	        	
				Network.deleteById(_network.getId());
				Window deleteAlgorithmWindow = new DeleteNetworkWindow(_network, ManageNetworksTab.this);
				deleteAlgorithmWindow.setModal(true);
				getWindow().addWindow(deleteAlgorithmWindow);
	        	
			} catch (IOException e2) {
				System.out.println("Nie uda³o siê usun¹æ sieci");
			}


	        
	    }
	}
	
	private class ConfirmNetworkListener implements Button.ClickListener{
		
		private Network network;
		
		public ConfirmNetworkListener(Network network){
			this.network = network;
		}
		
		@Override
	    public void buttonClick(ClickEvent event) {
			network.setUserId(mainWindow.getContext().getUser().getId());
			network.setName((String) nameTextField.getValue());
			network.setIsPublic(isPublicCheckbox.booleanValue());
			networkUploader.submitUpload();
			
	        
	    }
	}
	
	
}
