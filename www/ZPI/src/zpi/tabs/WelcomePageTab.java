package zpi.tabs;

import java.sql.Timestamp;
import java.util.List;

import zpi.MainWindow;
import zpi.hadoopModel.Algorithm;
import zpi.hadoopModel.Network;
import zpi.hadoopModel.Queue;
import zpi.hadoopModel.QueueUtil;
import zpi.hadoopModel.User;
import zpi.listeners.JobDetailsListener;
import zpi.listeners.JobResultsListener;
import zpi.listeners.StopJobListener;

import com.vaadin.data.Container;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;

/**
 * Zakladka pokazujaca status kolejki
 *
 */
@SuppressWarnings("serial")
public class WelcomePageTab extends ZpiTab {

	public WelcomePageTab(String string, MainWindow mainWindow) {
		super(string, mainWindow);
	}

	public void createGUI() {

		removeAllComponents();
		Table table = new Table();
		table.setStyleName("strong borderless");
		table.setTableFieldFactory(new WelcomeTableFieldFactory());

		table.addContainerProperty("Id", Integer.class, null);
		table.addContainerProperty("Nazwa algorytmu", String.class, null);
		table.addContainerProperty("Nazwa sieci", String.class, null);
		table.addContainerProperty("U¿ytkownik", String.class, null);
		table.addContainerProperty("Czas rozpoczêcia", Timestamp.class, null);
		table.addContainerProperty("Czas zakoñczenia", Timestamp.class, null);
		table.addContainerProperty("Status", String.class, null);
		table.addContainerProperty("Szczegó³y wykonania", Button.class, null);
		table.addContainerProperty("Zatrzymanie wykonania", Button.class, null);
		table.addContainerProperty("Wyniki", Button.class, null);

		List<Queue> queues = Queue.getQueuesByStartTimeDesc();
		if (queues != null) {
			for (Queue queue : queues) {
				table.setPageLength(queues.size());
				ProgressIndicator indicator = new ProgressIndicator();
				indicator.setValue(queue.getProgress() / 100);

				Algorithm algorithm = (Algorithm) Algorithm.getByID(queue
						.getAlgorithmId());
				String algorithmName = "";
				if (algorithm != null) {
					algorithmName = algorithm.getShortName();
				}
				User user = User.getByID(queue.getUserId());
				String userName = "";
				if (user != null) {
					userName = user.getName();
				}
				Network network = Network.getByID(queue.getNetworkId());
				String networkName = "";
				if (network != null) {
					networkName = network.getName();
				}

				Button detailButton = new Button("Szczegó³y");
				detailButton.addListener(new JobDetailsListener(queue, this));

				Button resultsButton = new Button("Wyniki");

				resultsButton.addListener(new JobResultsListener(this, queue));

				Button stopButton = new Button("Stop");
				stopButton.addListener(new StopJobListener(this, queue));

				if (queue.getStatus() == null || queue.getStatus().equals("0")) {
					resultsButton.setEnabled(false);
				}
				if (queue.getStatus() != null
						&& (queue.getStatus().equals("-1") || queue.getStatus()
								.equals("1"))) {
					stopButton.setEnabled(false);
				}
				if (queue.getUserId() != mainWindow.getContext().getUser()
						.getId()) {
					resultsButton.setEnabled(false);
					detailButton.setEnabled(false);
				}

				table.addItem(
						new Object[] { queue.getId(), algorithmName,
								networkName, userName,
								queue.getStartedAtTimestamp(),
								queue.getEndedAtTimestamp(),
								QueueUtil.getStatus(queue.getStatus()),
								detailButton, stopButton, resultsButton },
						queue);
			}
		}
		Button refreshButton = new Button("Odœwie¿");
		refreshButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				createGUI();
			}
		});
		addComponent(table);
		addComponent(refreshButton);

	}

	private class WelcomeTableFieldFactory implements TableFieldFactory {

		@Override
		public Field createField(Container container, Object itemId,
				Object propertyId, Component uiContext) {
			return null;
		}

	}

}
