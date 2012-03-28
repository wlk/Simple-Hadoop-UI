package pl.wroc.pwr.zpi.standAlone;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class DeleteNetwork {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

			Configuration conf = new Configuration();
			FileSystem hdfs = FileSystem.get(conf);
			
			if (hdfs.exists(new Path(args[0]))) {
				boolean result;
				result = hdfs.delete(new Path(args[0]), true);
				if (!result)
				      throw new IllegalArgumentException("Delete: deletion failed");
			}

	}
}