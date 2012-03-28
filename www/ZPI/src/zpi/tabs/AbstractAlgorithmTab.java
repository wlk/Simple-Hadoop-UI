package zpi.tabs;

import java.sql.Timestamp;
import java.util.List;

import zpi.MainWindow;
import zpi.components.AlgorithmDetailsComponent;
import zpi.hadoopModel.Algorithm;
import zpi.hadoopModel.INetAlg;
import zpi.hadoopModel.Network;
import zpi.hadoopModel.Queue;
import zpi.hadoopModel.QueueUtil;
import zpi.listeners.JobDetailsListener;
import zpi.listeners.JobResultsListener;
import zpi.listeners.StartJobListener;
import zpi.listeners.StopJobListener;
import zpi.windows.InputForAlgorithmWindow;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Abstrakcyjna zakladka algorytmu
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractAlgorithmTab extends ZpiTab {

	private Component baseComponent = null;

	private Component detailsComponent = null;

	private Select selectNetwork = null;

	private Algorithm algorithm = null;

	private Queue queue = null;

	private Table queueTable;

	public AbstractAlgorithmTab(MainWindow mainWindow) {
		super("", mainWindow);
	}

	public AbstractAlgorithmTab(MainWindow mainWindow, Algorithm algorithm) {
		super(mainWindow);
		this.algorithm = algorithm;

	}

	protected void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}

	public void createGUI() {
	}

	public void createGuiForAlgorithm() {

		if (baseComponent != null) {
			removeComponent(baseComponent);
		}

		queue = new Queue();
		Component topLeftComponent = createTopLeftComponent();
		Component descriptionComponent = createDescriptionComponent();
		Component detailsComponent = createDetailsComponent();

		HorizontalLayout topLayout = new HorizontalLayout();
		topLayout.addComponent(topLeftComponent);
		topLayout.addComponent(descriptionComponent);

		VerticalLayout overallLayout = new VerticalLayout();
		overallLayout.addComponent(topLayout);
		overallLayout.addComponent(detailsComponent);

		baseComponent = overallLayout;
		addComponent(baseComponent);
	}

	private Component createTopLeftComponent() {

		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setHeight(250f, UNITS_PIXELS);
		verticalLayout.setWidth(250f, UNITS_PIXELS);
		verticalLayout.setStyleName("light");

		Label titleLabel = new Label(algorithm.getShortName());
		titleLabel.setStyleName("h1");

		selectNetwork = new Select("Wybierz sieæ o id:");
		selectNetwork.setWidth(180f, UNITS_PIXELS);
		selectNetwork
				.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
		List<Network> networks = Network.getNetworksForAlgorithm(mainWindow
				.getContext().getUser().getId(), algorithm.getId());
		if (!networks.isEmpty()) {
			for (INetAlg network : networks) {
				selectNetwork.addItem(network);
			}
			selectNetwork.setValue(networks.get(0));
		}

		Button inputParametersButton = new Button("WprowadŸ parametry");
		inputParametersButton.setWidth(180f, UNITS_PIXELS);
		inputParametersButton.addListener(new OpenInputForAlgorithmListener());

		Button startButton = new Button("Start");
		startButton.setWidth(180f, UNITS_PIXELS);
		startButton.addListener(new StartJobListener(this));

		verticalLayout.addComponent(titleLabel);
		verticalLayout.addComponent(selectNetwork);
		verticalLayout.addComponent(inputParametersButton);
		verticalLayout.addComponent(startButton);
		verticalLayout.setMargin(true, false, true, false);

		return verticalLayout;
	}

	private Component createDescriptionComponent() {

		Panel descPanel = new Panel();
		descPanel.setHeight(225f, UNITS_PIXELS);
		descPanel.setWidth(650f, UNITS_PIXELS);

		TextArea descriptionTextArea = new TextArea();

		descriptionTextArea.setValue(algorithm.getLongName());
		descriptionTextArea.setSizeFull();

		descriptionTextArea.setReadOnly(true);

		descPanel.addComponent(descriptionTextArea);
		return descPanel;
	}

	private Component createDetailsComponent() {

		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setMargin(true, false, true, false);

		Label titleLabel = new Label("Przegl¹darka wyników");
		titleLabel.setStyleName("h2");
		verticalLayout.addComponent(titleLabel);

		queueTable = new Table();
		queueTable.removeAllItems();
		queueTable.setWidth(900f, UNITS_PIXELS);
		queueTable.setStyleName("borderless strong");

		queueTable.addContainerProperty("ID", String.class, null);
		queueTable.addContainerProperty("Czas rozpoczêcia", Timestamp.class, null);
		queueTable.addContainerProperty("Czas zakoñczenia", Timestamp.class, null);
		queueTable.addContainerProperty("Status", String.class, null);
		queueTable.addContainerProperty("Zatrzymanie wykonania", Button.class,
				null);
		queueTable.addContainerProperty("Szczegó³y wykonania", Button.class,
				null);
		queueTable.addContainerProperty("Wyniki", Button.class, null);

		addItemsToQueueTable();
		verticalLayout.addComponent(queueTable);

		return verticalLayout;
	}

	public void addItemsToQueueTable() {
		List<Queue> queues = Queue.getQueuesByAlgorithmId(algorithm.getId());
		if (queues != null) {
			queueTable.setPageLength(queues.size());
			for (Queue queue : queues) {
				Button stopButton = new Button("Stop");
				stopButton.addListener(new StopJobListener(this, queue));

				if (queue.getStatus() != null && !queue.getStatus().equals("0")) {
					stopButton.setEnabled(false);
				}

				Button detailButton = new Button("Szczegó³y");
				detailButton.addListener(new JobDetailsListener(queue, this));

				Button resultsButton = new Button("Wyniki");

				resultsButton.addListener(new JobResultsListener(this, queue));

				if (queue.getStatus() == null || queue.getStatus().equals("0")) {
					resultsButton.setEnabled(false);
				}

				queueTable.addItem(
						new Object[] { queue.getId(), queue.getStartedAtTimestamp(),
								queue.getEndedAtTimestamp(),
								QueueUtil.getStatus(queue.getStatus()),
								stopButton, detailButton, resultsButton },
						queue);
			}
		}

	}

	public void switchToDetailsView(Queue queue) {
		detailsComponent = new AlgorithmDetailsComponent(this, queue);
		removeComponent(baseComponent);
		addComponent(detailsComponent);

	}

	public void switchToBaseView() {

		removeComponent(detailsComponent);
		addComponent(baseComponent);

	}

	public void clearComponents() {
		if (baseComponent != null) {
			removeComponent(baseComponent);
		}
		if (detailsComponent != null) {
			removeComponent(detailsComponent);
		}
	}

	public Select getSelectNetwork() {
		return selectNetwork;
	}

	public Algorithm getAlgorithm() {
		return algorithm;
	}

	public Queue getQueue() {
		return queue;
	}

	public Table getQueueTable() {
		return queueTable;
	}

	private class OpenInputForAlgorithmListener implements Button.ClickListener {

		@Override
		public void buttonClick(ClickEvent event) {

			Window inputForAlgorithmWindow = new InputForAlgorithmWindow(
					algorithm, queue);
			inputForAlgorithmWindow.setModal(true);
			getWindow().addWindow(inputForAlgorithmWindow);

		}
	}

}
