package pl.wroc.pwr.zpi.standAlone;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class SaveNetwork {


	public static void main(String[] args) throws IOException {

		File file = new File(args[0]);
		if (file.isFile()) {
			System.out.println("trwa kopiowanie, prosze czekac...");
			Configuration conf = new Configuration();
			FileSystem hdfs = FileSystem.get(conf);

			System.out.println(hdfs.getUri() + " config: "
					+ hdfs.getConf().toString());
			try {
				hdfs.copyFromLocalFile(new Path(file.getPath()), new Path(
						args[1]));
				if (hdfs.exists(new Path(args[1]))) {
			        boolean success = file.delete();
				    if (!success)
				      throw new IllegalArgumentException("Delete: deletion failed");
					System.out.println("dodano do HDFS poprawnie");

				} else {
					System.out.println("nie udalo sie umiescic pliku w HDFS, sprobuj ponownie.");
				}
			} catch (IOException e) {
				System.out.println("blad podczas przesylania, operacja anulowana.");
				e.printStackTrace();
			}
		} 
		else {
			System.out.println("plik pod podana sciezka nie istnieje, operacja kopiowania anulowana!");
		}
	}
}