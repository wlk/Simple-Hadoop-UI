package zpi.components;

import java.io.BufferedReader;
import java.io.FileReader;

import zpi.hadoopModel.Queue;
import zpi.listeners.ReturnToBaseViewListener;
import zpi.tabs.AbstractAlgorithmTab;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

/**
 * Komponent do pokazywania wynikow algorytmu.
 *
 */
@SuppressWarnings("serial")
public class AlgorithmDetailsComponent extends VerticalLayout {

	private Queue queue;

	public AlgorithmDetailsComponent(AbstractAlgorithmTab tab, Queue queue) {
		super();
		createGUI(tab);
	}

	private void createGUI(AbstractAlgorithmTab tab) {

		Label titleLabel = new Label("Podgl¹d pliku");
		titleLabel.setStyleName("h2");

		TextArea textArea = new TextArea();
		textArea.setWidth(800f, UNITS_PIXELS);
		String fileContent = readFile(queue.getOutputFileLocation());
		textArea.setValue(fileContent);

		Button getFileButton = new Button("Pobierz plik");
		getFileButton.setWidth(800f, UNITS_PIXELS);

		Button linkButton = new Button("Powrót");
		linkButton.addListener(new ReturnToBaseViewListener(tab));
		linkButton.setStyleName("link");

		addComponent(titleLabel);
		addComponent(textArea);
		addComponent(getFileButton);
		addComponent(linkButton);

	}

	private String readFile(String file) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			String ls = System.getProperty("line.separator");
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
}
