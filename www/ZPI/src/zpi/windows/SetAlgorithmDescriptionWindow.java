package zpi.windows;


import zpi.hadoopModel.Algorithm;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Window;

/**
 * Okno sluzace do definiowania opisu algorytmu
 *
 */
@SuppressWarnings("serial")
public class SetAlgorithmDescriptionWindow extends Window {

	private Algorithm algorithm = null;
	
	TextArea textArea = null;
	
	public SetAlgorithmDescriptionWindow(Algorithm algorithm){
		super("Wprowadzanie opisu algorytmu");
		this.algorithm = algorithm;
		createGUI();
		setHeight(600f, UNITS_PIXELS);
		setWidth(530f, UNITS_PIXELS);
	}
	
	private void createGUI(){
		
		Label tableLabel = new Label("Opis algorytmu");
		tableLabel.setStyleName("h2");
		
		addComponent(tableLabel);
		
		textArea = new TextArea();
		textArea.setHeight(350f, UNITS_PIXELS);
		textArea.setWidth(480f, UNITS_PIXELS);
		String content = "";
		if (algorithm.getLongName() != null && !algorithm.getLongName().equals("null")){
			content = algorithm.getLongName();
		}
		textArea.setValue(content);
		
		addComponent(textArea);
		
		Button confirmButton = new Button("Zapisz opis");
		confirmButton.setWidth(120f, UNITS_PIXELS);
		confirmButton.addListener(new ConfirmDescListener(this));
		
		Button cancelButton = new Button("Anuluj");
		cancelButton.addListener(new CancelDescListener(this));
		cancelButton.setWidth(120f, UNITS_PIXELS);
		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidth(480f, UNITS_PIXELS);
		horizontalLayout.setHeight(80f, UNITS_PIXELS);
		horizontalLayout.addComponent(confirmButton);
		horizontalLayout.addComponent(cancelButton);
		horizontalLayout.setComponentAlignment(confirmButton, Alignment.MIDDLE_CENTER);
		horizontalLayout.setComponentAlignment(cancelButton, Alignment.MIDDLE_CENTER);
		
		addComponent(horizontalLayout);
	}
	
	private class ConfirmDescListener implements Button.ClickListener {

		Window _window = null;
		
		public ConfirmDescListener(Window window){
			_window = window;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			((Window)_window.getParent()).removeWindow(_window);
			algorithm.setLongName((String) textArea.getValue());
		}
		
	}
	
	private class CancelDescListener implements Button.ClickListener {

		Window _window = null;
		
		public CancelDescListener(Window window){
			_window = window;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {
			((Window)_window.getParent()).removeWindow(_window);
		}
		
	}
}
