package zpi.listeners;

import zpi.hadoopModel.Queue;
import zpi.tabs.ZpiTab;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
/**
 * Listener do stopowania joba
 *
 */
@SuppressWarnings("serial")
public class StopJobListener implements Button.ClickListener {

		private ZpiTab tab;
	
		private Queue queue;
		
		public StopJobListener(ZpiTab algorithmTab, Queue queue){
			this.tab = algorithmTab;
			this.queue = queue;
		}
	
		@Override
		public void buttonClick(ClickEvent event) {
			queue.kill();
			tab.getMainWindow().open(new ExternalResource("http://10.10.1.29:50030","_new"));
			
		}
	}