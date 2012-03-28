package zpi.components;

import java.io.OutputStream;

import zpi.MainWindow;
import zpi.hadoopModel.AbstractModelBase;
import zpi.tabs.ZpiTab;

import com.vaadin.ui.Upload;
import com.vaadin.ui.Window.Notification;

/**
 * Klasa wykorzystywana przy uploadowaniu zawartosci na serwer.
 *
 */
@SuppressWarnings("serial")
public abstract class FileUploader extends Upload implements
		Upload.SucceededListener, Upload.FailedListener, Upload.Receiver {

	protected MainWindow mainWindow;
	
	protected AbstractModelBase base;
	
	protected volatile boolean isFinished = false;
	
	public FileUploader(MainWindow mainWindow, AbstractModelBase base){
		super();
		this.mainWindow = mainWindow;
		this.base = base;
    	setButtonCaption(null);
        
        addListener((Upload.SucceededListener) this);
        addListener((Upload.FailedListener) this);
        setReceiver(this);
	}
	

    public abstract OutputStream receiveUpload(String filename,
                                      String MIMEType);

    public void uploadSucceeded(Upload.SucceededEvent event) {
        
		base.save();
		
		Notification confirmNotification = new Notification("Zapis do bazy przebieg³ pomyœlnie!");
		confirmNotification.setDelayMsec(2000);
		mainWindow.showNotification(confirmNotification);
		isFinished = true;
		ZpiTab tab = (ZpiTab) mainWindow.getTabSheet().getSelectedTab();
		tab.createGUI();

    }

    public void uploadFailed(Upload.FailedEvent event) {
    	
    	Notification notification = new Notification("Zapis zakoñczony niepowodzeniem!", Notification.TYPE_ERROR_MESSAGE);
    	
		notification.setDelayMsec(2000);
		mainWindow.showNotification(notification);
    }
    
    public boolean isFinished(){
    	return isFinished;
    }
    

}
