package zpi.listeners;

import zpi.hadoopModel.Queue;
import zpi.tabs.ZpiTab;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * Listener otwierajacy strone z wynikami joba
 *
 */
@SuppressWarnings("serial")
public class JobResultsListener implements Button.ClickListener {
    
	private ZpiTab algorithmTab = null;
	
	private Queue queue = null;
	
	public JobResultsListener(ZpiTab algorithmTab, Queue queue){
		super();
		this.algorithmTab = algorithmTab;
		this.queue = queue;
	}
	
	@Override
    public void buttonClick(ClickEvent event) {
		algorithmTab.getMainWindow().open(new ExternalResource(queue.getOutputURL(), "_new"));
    }
}