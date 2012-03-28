package zpi.tabs;

import zpi.MainWindow;

import com.vaadin.ui.Panel;

/**
 * Abstrakcyjna klasa zakladki
 *
 */
@SuppressWarnings("serial")
public abstract class ZpiTab extends Panel{

	protected MainWindow mainWindow = null;
	
	private float width;
	
	private float height;
	
	public ZpiTab(){
		super();
	}
	
	public ZpiTab(MainWindow window){
		super();
		this.mainWindow = window;
		this.width = mainWindow.getMainPanel().getWidth();
		this.height = mainWindow.getMainPanel().getHeight();
	}
	
	public ZpiTab(String caption, MainWindow window){
		super();
		mainWindow = window;
		width = mainWindow.getMainPanel().getWidth();
		height = mainWindow.getMainPanel().getHeight();
	}

	public float getHeight() {
		return height;
	}
	
	public float setWidth() {
		return width;
	}
	
	public MainWindow getMainWindow(){
		return mainWindow;
	}
	
	public abstract void createGUI();


	
}
