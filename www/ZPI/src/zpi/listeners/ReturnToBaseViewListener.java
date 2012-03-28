package zpi.listeners;

import zpi.tabs.AbstractAlgorithmTab;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * listener do przelaczania miedzy widokami
 *
 */
@SuppressWarnings("serial")
public class ReturnToBaseViewListener implements Button.ClickListener {

	private AbstractAlgorithmTab algorithmTab = null;

	public ReturnToBaseViewListener(AbstractAlgorithmTab algorithmTab) {
		super();
		this.algorithmTab = algorithmTab;
	}

	@Override
	public void buttonClick(ClickEvent event) {

		algorithmTab.switchToBaseView();

	}
}
