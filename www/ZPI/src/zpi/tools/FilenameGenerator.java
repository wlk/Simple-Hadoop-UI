package zpi.tools;

import java.util.Random;

/**
 * Generator nazw pliku
 *
 */
public class FilenameGenerator {
	
	public static String getRandomFilename(){
		String filename = "";
		Random random = new Random();
		for(int i = 0; i<10;i++){
			int r = random.nextInt(63)+64;
			char ascii = (char)r;
			filename = filename.concat(Character.toString(ascii));
		}
		
		return filename;
	}

}
