package pl.wroc.pwr.zpi.standAlone;

import java.io.Console;
import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import pl.wroc.pwr.zpi.model.Network;
import pl.wroc.pwr.zpi.model.User;

public class LoadFromConsole {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		if (args.length < 6) {
			args = new String[6];
			System.out
					.println("wywolanie zawiera mniej parametrow, niz jest to wymagane");
			System.out
					.println("user password localPath networkName isPublic delimiter");
			System.out.println("musisz podac parametry poprzez konsole:");
			Console console = System.console();
			System.out.println("podaj nazwę użytkownika");
			args[0] = console.readLine();
			System.out.println("podaj hasło");
			args[1] = console.readLine();

			System.out.println("podaj sciezke pliku z siecia");
			args[2] = console.readLine();

			// System.out.println("file exist "+file.exists());
			// System.out.println("file absolute path "+file.getAbsolutePath());
			System.out.println("wpisz nazwe sieci i nacisnij enter");
			args[3] = console.readLine();
			System.out
					.println("Czy sieć ma być dostępna dla wszystkich użytkowników? (T/N)");
			args[4] = console.readLine();
			// boolean publ = args[4].charAt(0) == 'T';
			System.out.println("podaj delimiter danych w pliku wejściowym");
			args[5] = console.readLine();

		}
		if (!User.isAuthenticated(args[0], args[1])) {
			System.out.println("nieprawidłowa nazwa użytkownika lub hasło");
			System.exit(-1);
		} else {
			System.out.println("logowanie poprawne");
		}
		File file = new File(args[2]);
		if (file.isFile()) {
			System.out.println("trwa kopiowanie, prosze czekac...");
			Configuration conf = new Configuration();
			FileSystem hdfs = FileSystem.get(conf);

			System.out.println(hdfs.getUri() + " config: "
					+ hdfs.getConf().toString());
			try {
				hdfs.copyFromLocalFile(new Path(file.getPath()), new Path(
						args[3]));
				if (hdfs.exists(new Path(args[3]))) {
					System.out.println("dodano do HDFS poprawnie");

					Network net = new Network(args[3], args[2],
							args[4].equals("T") ? 1 : 0, User.getByName(args[0])
									.getId(), args[5]);
					System.out.println("dodano siec o id " + net.getId());
					// new Network(null, name, null, name, null, null, null,
					// null);
				} else {
					System.out
							.println("nie udalo sie umiescic pliku w HDFS, sprobuj ponownie.");
				}
			} catch (IOException e) {
				System.out
						.println("blad podczas przesylania, operacja anulowana.");
				e.printStackTrace();
			}
		} else {
			System.out
					.println("plik pod podana sciezka nie istnieje, operacja kopiowania anulowana!");
		}
	}

}
