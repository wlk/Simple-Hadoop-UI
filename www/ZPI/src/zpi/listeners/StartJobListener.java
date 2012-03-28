package zpi.listeners;

import java.util.ArrayList;

import zpi.MainWindow;
import zpi.hadoopModel.Algorithm;
import zpi.hadoopModel.Network;
import zpi.hadoopModel.Queue;
import zpi.tabs.AbstractAlgorithmTab;
import zpi.tools.SchemaParsing;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * Listener do starowania joba
 *
 */
@SuppressWarnings("serial")
public class StartJobListener implements Button.ClickListener {

		private AbstractAlgorithmTab algorithmTab;
	
		public StartJobListener(AbstractAlgorithmTab algorithmTab){
			this.algorithmTab = algorithmTab;
		}
	
		@Override
		public void buttonClick(ClickEvent event) {

			Queue queue = algorithmTab.getQueue();
			Algorithm algorithm = algorithmTab.getAlgorithm();
			MainWindow mainWindow = algorithmTab.getMainWindow();
			
			if (queue.getParameters() == null){
				ArrayList<ArrayList<String>> fromSchema = SchemaParsing
						.fromSchema(algorithm.getParameters());
				StringBuffer params = new StringBuffer();
				for (ArrayList<String> list : fromSchema) {
					params.append(list.get(2));
					params.append(" ");
				}
				queue.setParameters(params.toString());
			}
			
			queue.setUserId(mainWindow.getContext().getUser().getId());
			queue.setNetworkId(((Network) algorithmTab.getSelectNetwork().getValue()).getId());
			queue.setAlgorithmId(algorithm.getId());
			queue.save();
			
			algorithmTab.getQueueTable().removeAllItems();
			algorithmTab.addItemsToQueueTable();
		}
	}