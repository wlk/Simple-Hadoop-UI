package zpi.tabs;

import java.util.List;

import zpi.MainWindow;
import zpi.hadoopModel.Algorithm;

/**
 * Zakladka do algorytmow publicznych
 *
 */
@SuppressWarnings("serial")
public class PublicAlgorithmsTab extends AbstractUserAlgorithmTab{

	public PublicAlgorithmsTab(MainWindow mainWindow) {
		super(mainWindow);
	}

	@Override
	protected List<Algorithm> getAlgorithms() {
		return mainWindow.getContext().getPublicAlgorithms();
	}

}
