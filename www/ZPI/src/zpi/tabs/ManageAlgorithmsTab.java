package zpi.tabs;

import java.util.List;

import zpi.MainWindow;
import zpi.components.AlgorithmFileUploader;
import zpi.hadoopModel.Algorithm;
import zpi.windows.DeleteAlgorithmWindow;
import zpi.windows.EditAlgorithmWindow;
import zpi.windows.EditSchemaWindow;
import zpi.windows.SetAlgorithmDescriptionWindow;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Zakladka do zarzadzania algorytmami
 *
 */
@SuppressWarnings("serial")
public class ManageAlgorithmsTab extends ZpiTab{

	private MainWindow mainWindow;
	
	private TextField nameTextField;
	
	private CheckBox isPublicCheckbox;
	
	AlgorithmFileUploader algorithmUploader;
	
	public ManageAlgorithmsTab(String string, MainWindow mainWindow) {
		super(string, mainWindow);
		this.mainWindow = mainWindow;
	}
	
	public void createGUI(){
		
		removeAllComponents();
		HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
		hsplit.setWidth(1150f, UNITS_PIXELS);
		
		Component leftPanel = createLeftPanel();
		Component rightPanel = createRightPanel();
		hsplit.setFirstComponent(leftPanel);
		hsplit.setSecondComponent(rightPanel);

		hsplit.setSplitPosition(55, Sizeable.UNITS_PERCENTAGE);
		
		addComponent(hsplit);
		 
		
	}
	
	private Component createLeftPanel(){
		
		Algorithm tempAlgorithm = new Algorithm();
		
		VerticalLayout wholeLayout = new VerticalLayout();

		Label topLabel = new Label("Dodaj algorytm");
		topLabel.setStyle("h2");
		wholeLayout.addComponent(topLabel);
		
		Label inputNameLabel = new Label("Wpisz nazwê");
		Label inputFileLabel = new Label("Wybierz JAR");
		Label isPublicLabel = new Label("Algorytm publiczny");
		isPublicLabel.setWidth(100f, UNITS_PIXELS);
		
		inputNameLabel.setWidth(100f, UNITS_PIXELS);
		inputFileLabel.setWidth(100f, UNITS_PIXELS);
		
		nameTextField = new TextField();
		algorithmUploader = new AlgorithmFileUploader(mainWindow, tempAlgorithm);
		isPublicCheckbox = new CheckBox();
		isPublicCheckbox.setValue(true);
		
		GridLayout gridLayout = new GridLayout(2, 3);
		gridLayout.addComponent(inputNameLabel, 0, 0);
		gridLayout.addComponent(isPublicLabel, 0, 1);
		gridLayout.addComponent(inputFileLabel, 0, 2);
		
		
		gridLayout.addComponent(nameTextField, 1, 0);
		gridLayout.addComponent(isPublicCheckbox, 1, 1);
		gridLayout.addComponent(algorithmUploader, 1, 2);
		
		gridLayout.setMargin(true, false, true, false);
		
		wholeLayout.addComponent(gridLayout);
		
		wholeLayout.addComponent(createSettingsPanel(tempAlgorithm));
		
		return wholeLayout;
	}
	
	private Component createSettingsPanel(Algorithm algorithm){
		Panel settingsPanel = new Panel();
		
		settingsPanel.setWidth(500f, UNITS_PIXELS);
		
		HorizontalLayout descLayout = new HorizontalLayout();
		descLayout.setMargin(true, true, true, true);
		descLayout.setWidth(500f, UNITS_PIXELS);
		descLayout.setHeight(100f, UNITS_PIXELS);
		Label descLabel = new Label("Opis algorytmu");
		descLabel.setWidth(200f, UNITS_PIXELS);
		descLabel.setStyleName("h3");
		Button descButton = new Button("WprowadŸ opis");
		descButton.addListener(new SetDescriptionListener(algorithm));
		descButton.setWidth(200f, UNITS_PIXELS);
		descLayout.addComponent(descLabel);
		descLayout.addComponent(descButton);
	
		settingsPanel.addComponent(descLayout);
		
		HorizontalLayout paramLayout = new HorizontalLayout();
		paramLayout.setMargin(true, true, true, true);
		paramLayout.setWidth(500f, UNITS_PIXELS);
		paramLayout.setHeight(100f, UNITS_PIXELS);
		Label paramLabel = new Label("Parametry algorytmu");
		paramLabel.setWidth(200f, UNITS_PIXELS);
		paramLabel.setStyleName("h3");
		Button paramButton = new Button("WprowadŸ parametry algorytmu");
		paramButton.addListener(new EditAlgorithmListener(algorithm));
		paramButton.setWidth(200f, UNITS_PIXELS);
		
		paramLayout.addComponent(paramLabel);
		paramLayout.addComponent(paramButton);
		
		settingsPanel.addComponent(paramLayout);
		
		HorizontalLayout schemaLayout = new HorizontalLayout();
		schemaLayout.setMargin(true, true, true, true);
		schemaLayout.setWidth(500f, UNITS_PIXELS);
		schemaLayout.setHeight(100f, UNITS_PIXELS);
		Label schemaLabel = new Label("Schemat sieci");
		schemaLabel.setWidth(200f, UNITS_PIXELS);
		schemaLabel.setStyleName("h3");
		Button schemaButton = new Button("Okreœl schemat sieci");
		schemaButton.addListener(new EditSchemaListener(algorithm));
		schemaButton.setWidth(200f, UNITS_PIXELS);
		
		schemaLayout.addComponent(schemaLabel);
		schemaLayout.addComponent(schemaButton);
		
		settingsPanel.addComponent(schemaLayout);
		
		HorizontalLayout sendLayout = new HorizontalLayout();
		sendLayout.setMargin(true, true, true, true);
		sendLayout.setWidth(500f, UNITS_PIXELS);
		schemaLayout.setHeight(100f, UNITS_PIXELS);
		Label sendLabel = new Label("PotwierdŸ dodanie algorytmu");
		sendLabel.setWidth(200f, UNITS_PIXELS);
		sendLabel.setStyleName("h3");
		Button sendButton = new Button("Dodaj algorytm!");
		sendButton.addListener(new ConfirmAlgorithmListener(algorithm));
		sendButton.setWidth(200f, UNITS_PIXELS);
		
		sendLayout.addComponent(sendLabel);
		sendLayout.addComponent(sendButton);
		
		settingsPanel.addComponent(sendLayout);

		return settingsPanel;
	}
	
	private Component createRightPanel(){

		Table table = new Table();
		table.setStyleName("strong borderless");
		List<Algorithm> algorithms = mainWindow.getContext().getUserAlgorithms();
		table.setPageLength(algorithms.size());
		table.addContainerProperty("Nazwa algorytmu", String.class,  null);
		table.addContainerProperty("Parametry", Button.class,  null);
		table.addContainerProperty("Schemat", Button.class,  null);
		table.addContainerProperty("Usuñ", Button.class,  null);
		
		
		for (Algorithm algorithm : algorithms){
			Button editButton = new Button("Edytuj parametry");
			editButton.addListener(new EditAlgorithmListener(algorithm));
			Button editShemaButton = new Button("Edytuj schemat");
			editShemaButton.addListener(new EditSchemaListener(algorithm));
			Button deleteButton = new Button("Usuñ");
			deleteButton.addListener(new DeleteAlgorithmListener(algorithm));
			table.addItem(new Object[]{algorithm.getShortName(), editButton, editShemaButton, deleteButton}, algorithm);
			
		}
		
		
		return table;
	}

	private class ConfirmAlgorithmListener implements Button.ClickListener{
		
		private Algorithm algorithm;
		
		public ConfirmAlgorithmListener(Algorithm algorithm){
			this.algorithm = algorithm;
		}
		
		@Override
	    public void buttonClick(ClickEvent event) {
			
			algorithm.setShortName((String) nameTextField.getValue());
			algorithm.setIsPublic(isPublicCheckbox.booleanValue());
			algorithm.setUserId(mainWindow.getContext().getUser().getId());
			
			
			algorithmUploader.submitUpload();
			
	    }
	}
	
	
	private class EditAlgorithmListener implements Button.ClickListener{
		
		private Algorithm _algorithm = null;
		
		public EditAlgorithmListener(Algorithm algorithm){
			_algorithm = algorithm;
		}
		
		@Override
	    public void buttonClick(ClickEvent event) {
			
			Window editAlgorithmWindow = new EditAlgorithmWindow(_algorithm);
			editAlgorithmWindow.setModal(true);
			getWindow().addWindow(editAlgorithmWindow);
	        
	    }
	}
	
	private class SetDescriptionListener implements Button.ClickListener{
		
		private Algorithm _algorithm = null;
		
		public SetDescriptionListener(Algorithm algorithm){
			_algorithm = algorithm;
		}
		
		@Override
	    public void buttonClick(ClickEvent event) {
			
			Window descWindow = new SetAlgorithmDescriptionWindow(_algorithm);
			descWindow.setModal(true);
			getWindow().addWindow(descWindow);
	        
	    }
	}
	
	private class EditSchemaListener implements Button.ClickListener{
		
		Algorithm algorithm;
		
		public EditSchemaListener(Algorithm algorithm){
			this.algorithm = algorithm;
		}
		
		@Override
	    public void buttonClick(ClickEvent event) {
			
			Window editNetworkWindow = new EditSchemaWindow(algorithm, ManageAlgorithmsTab.this);
			editNetworkWindow.setModal(true);
			getWindow().addWindow(editNetworkWindow);
	        
	    }
	}
	
	private class DeleteAlgorithmListener implements Button.ClickListener{
		
		private Algorithm _algorithm = null;
		
		public DeleteAlgorithmListener(Algorithm algorithm){
			_algorithm = algorithm;
		}
		
		@Override
	    public void buttonClick(ClickEvent event) {
			
			Window deleteAlgorithmWindow = new DeleteAlgorithmWindow(_algorithm, ManageAlgorithmsTab.this);
			deleteAlgorithmWindow.setModal(true);
			getWindow().addWindow(deleteAlgorithmWindow);
	        
	    }
	}
	
	
}
