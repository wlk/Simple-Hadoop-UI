package zpi.tabs;

import java.util.List;

import zpi.MainWindow;
import zpi.hadoopModel.Algorithm;

/**
 * Zakladka do algorytmow uzytkownika
 *
 */
@SuppressWarnings("serial")
public class UserAlgorithmsTab extends AbstractUserAlgorithmTab{

	public UserAlgorithmsTab(MainWindow mainWindow) {
		super(mainWindow);
	}

	@Override
	protected List<Algorithm> getAlgorithms() {
		return mainWindow.getContext().getUserAlgorithms();
	}

}
