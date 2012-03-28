package zpi.components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import zpi.MainWindow;
import zpi.hadoopModel.Network;
import zpi.tabs.ZpiTab;

import com.vaadin.ui.Upload;
import com.vaadin.ui.Window.Notification;

/**
 * Uploader dla pliku sieci
 * 
 */
@SuppressWarnings("serial")
public class NetworkFileUploader extends FileUploader {

	private Network network;

	public NetworkFileUploader(MainWindow mw, Network network) {

		super(mw, network);
		this.network = network;

	}

	public OutputStream receiveUpload(String filename, String MIMEType) {
		File file;
		FileOutputStream fos = null;
		String hdfsFileLocation = "/networks/" + System.nanoTime() + ".net";
		System.out.println("receive=>Lokalizacja: " + "/srv/uploads"
				+ hdfsFileLocation);
		System.out.println("receive=>HDFS: " + hdfsFileLocation);
		file = new File("/srv/uploads" + hdfsFileLocation);
		try {
			fos = new FileOutputStream(file);
			network.setNetworkFileLocation(hdfsFileLocation);
		} catch (FileNotFoundException e) {
			System.out.println("Nie uda³o siê stworzyæ pliku");
			return null;
		}

		return fos;
	}

	public void uploadSucceeded(Upload.SucceededEvent event) {
		Process proc = null;
		try {
			System.out.println("manage=>Lokalizacka: " + "/srv/uploads"
					+ network.getNetworkFileLocation());
			System.out.println("manage=>HDFS: "
					+ network.getNetworkFileLocation());
			proc = Runtime.getRuntime().exec(
					new String[] { "java", "-cp",
							"/usr/lib/hadoop/conf/:/home/wn/ZPI.jar",
							"pl.wroc.pwr.zpi.standAlone.SaveNetwork",
							"/srv/uploads" + network.getNetworkFileLocation(),
							network.getNetworkFileLocation() });

		} catch (IOException e2) {
			System.out.println("B³¹d przy wykonaniu skryptu");
		}

		/*
		 * String line; BufferedReader input = new BufferedReader( new
		 * InputStreamReader(proc.getInputStream())); BufferedReader error = new
		 * BufferedReader( new InputStreamReader(proc.getErrorStream()));
		 * System.out.println("input"); try { while((line =
		 * input.readLine())==null) System.out.println(line); } catch
		 * (IOException e) { e.printStackTrace(); } System.out.println("error");
		 * try { while((line = error.readLine())==null)
		 * System.out.println(line); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
		base.save();
		Notification confirmNotification = new Notification(
				"Zapis do bazy przebieg³ pomyœlnie!");
		confirmNotification.setDelayMsec(2000);
		mainWindow.showNotification(confirmNotification);
		isFinished = true;
		ZpiTab tab = (ZpiTab) mainWindow.getTabSheet().getSelectedTab();
		tab.createGUI();

	}

}