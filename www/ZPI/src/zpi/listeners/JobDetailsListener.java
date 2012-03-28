package zpi.listeners;

import zpi.hadoopModel.Queue;
import zpi.tabs.ZpiTab;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * Listener otwierajacy strone ze szczegolami job'a
 *
 */
@SuppressWarnings("serial")
public class JobDetailsListener implements Button.ClickListener {

		Queue queue;
		
		ZpiTab tab;
		
		public JobDetailsListener(Queue queue, ZpiTab tab){
			this.queue = queue;
			this.tab = tab;
		}
		
		@Override
		public void buttonClick(ClickEvent event) {

			tab.getMainWindow().open(new ExternalResource("http://10.10.1.29:50030", "_new"));
		}
	}