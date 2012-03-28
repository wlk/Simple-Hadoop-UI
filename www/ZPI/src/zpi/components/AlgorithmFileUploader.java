package zpi.components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import zpi.MainWindow;
import zpi.hadoopModel.Algorithm;

/**
 * Uploader dla pliku .jar
 *
 */
@SuppressWarnings("serial")
public class AlgorithmFileUploader extends FileUploader{

    private Algorithm algorithm;

    public AlgorithmFileUploader(MainWindow mw, Algorithm algorithm) {
    	
    	super(mw, algorithm);
    	this.algorithm = algorithm;
       
    }

    @Override
    public OutputStream receiveUpload(String filename,
                                      String MIMEType) {
        FileOutputStream fos = null; 
        
        System.out.println("receive");
        
        String jarLocation = "/srv/uploads/jars/" + filename;
        
        File file = new File(jarLocation);
        try {
            fos = new FileOutputStream(file);
            algorithm.setJarFileLocation(jarLocation);
        } catch (final java.io.FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return fos; 
    }

}