package zpi.windows;


import java.util.List;

import zpi.hadoopModel.Algorithm;
import zpi.hadoopModel.INetAlg;
import zpi.hadoopModel.Network;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.Window;

/**
 * Okno sluzace do kopiowania schematu
 *
 */
@SuppressWarnings("serial")
public class CopySchemaWindow extends Window {

	private int userId;
	
	private EditSchemaWindow parentWindow;
	
	private Select schemaSelect;
	
	public CopySchemaWindow(int userId,  EditSchemaWindow parent){
		super("Kopiowanie schematu");
		this.userId = userId;
		this.parentWindow = parent;
		createGUI();
		setHeight(250f, UNITS_PIXELS);
		setWidth(400f, UNITS_PIXELS);
	}
	
	private void createGUI(){
		
		Label nameLabel = new Label("Skopiuj schemat z:");
		nameLabel.setWidth(140f, UNITS_PIXELS);
		
		HorizontalLayout horizontalSchemaLayout = new HorizontalLayout();
		horizontalSchemaLayout.setWidth(350f, UNITS_PIXELS);
		horizontalSchemaLayout.setHeight(120f, UNITS_PIXELS);
		
		schemaSelect = new Select();
		List<Algorithm> algorithms = Algorithm.getPrivateAndPublicAlgorithmsByUserID(userId);
		List<Network> networks = Network.getPrivateAndPublicNetworksByUserID(userId);
		for (Algorithm algorithm : algorithms) {
			schemaSelect.addItem(algorithm);
		}
		for (INetAlg network : networks) {
			schemaSelect.addItem(network);
		}
		if (!networks.isEmpty()){
			schemaSelect.setValue(networks.get(0));
		}
		else if (!algorithms.isEmpty()){
			schemaSelect.setValue(algorithms.get(0));
		}
		
		horizontalSchemaLayout.addComponent(nameLabel);
		horizontalSchemaLayout.addComponent(schemaSelect);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(350f, UNITS_PIXELS);

		Button yesButton = new Button("Kopiuj");
		yesButton.addListener(new ConfirmCopySchemaListener());
		Button noButton = new Button("Anuluj");
		noButton.addListener(new CancelCopySchemaListener());
		horizontalLayout.addComponent(yesButton);
		horizontalLayout.addComponent(noButton);
		
		horizontalLayout.setComponentAlignment(yesButton, Alignment.MIDDLE_CENTER);
		horizontalLayout.setComponentAlignment(noButton, Alignment.MIDDLE_CENTER);
		
		addComponent(horizontalSchemaLayout);
		addComponent(horizontalLayout);
	}
	
	private class ConfirmCopySchemaListener implements Button.ClickListener {

		@Override
		public void buttonClick(ClickEvent event) {
			
			INetAlg netAlg = (INetAlg) schemaSelect.getValue();
			parentWindow.setSchema(netAlg.getSchema());
			parentWindow.setDelimiter(netAlg.getDelimiter());
			parentWindow.removeAllComponents();
			parentWindow.createGUI();
			CopySchemaWindow.this.getParent().removeWindow(CopySchemaWindow.this);
		}
		
	}
	
	private class CancelCopySchemaListener implements Button.ClickListener {
		
		@Override
		public void buttonClick(ClickEvent event) {
			CopySchemaWindow.this.getParent().removeWindow(CopySchemaWindow.this);
		}
		
	}
}
