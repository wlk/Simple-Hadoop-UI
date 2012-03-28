package zpi.tabs;

import java.util.List;

import zpi.MainWindow;
import zpi.hadoopModel.Algorithm;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;

/**
 * Abstrakcyjna zakladka algorytmow uzytkownika
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractUserAlgorithmTab extends AbstractAlgorithmTab {

	protected String selectedItem = null;

	protected Object selectedObject = null;

	public AbstractUserAlgorithmTab(MainWindow mainWindow) {
		super(mainWindow);
	}

	protected abstract List<Algorithm> getAlgorithms();

	public void createGUI() {

		removeAllComponents();
		List<Algorithm> algorithms = getAlgorithms();
		Panel panel = new Panel();
		Label selectAlgorithmLabel = new Label("Wybierz algorytm");
		selectAlgorithmLabel.setStyleName("h2");

		Select selectAlgorithm = new Select();
		for (Algorithm algorithm : algorithms) {
			selectAlgorithm.addItem(algorithm);
		}
		if (!algorithms.isEmpty()) {
			selectAlgorithm.setValue(algorithms.get(0));
			selectedObject = algorithms.get(0);
		}
		selectAlgorithm.setInvalidAllowed(false);
		selectAlgorithm.setNullSelectionAllowed(false);
		selectAlgorithm.addListener(new AlgorithmSelectionListener());

		Button confirmButton = new Button("ZatwierdŸ wybór");
		confirmButton.addListener(new ConfirmSelectedAlgorithmListener());

		panel.addComponent(selectAlgorithmLabel);
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.addComponent(selectAlgorithm);
		horizontalLayout.addComponent(confirmButton);
		horizontalLayout.setWidth(300f, UNITS_PIXELS);
		horizontalLayout.setComponentAlignment(selectAlgorithm,
				Alignment.MIDDLE_LEFT);
		horizontalLayout.setComponentAlignment(confirmButton,
				Alignment.MIDDLE_RIGHT);
		horizontalLayout.setMargin(true, false, false, false);

		panel.addComponent(horizontalLayout);

		addComponent(panel);
	}

	private class AlgorithmSelectionListener implements
			Property.ValueChangeListener {

		@Override
		public void valueChange(ValueChangeEvent event) {
			selectedObject = event.getProperty().getValue();
		}

	}

	private class ConfirmSelectedAlgorithmListener implements
			Button.ClickListener {

		@Override
		public void buttonClick(ClickEvent event) {
			if (selectedObject != null) {
				setAlgorithm((Algorithm) selectedObject);
				clearComponents();
				createGuiForAlgorithm();
			}
		}

	}

}
