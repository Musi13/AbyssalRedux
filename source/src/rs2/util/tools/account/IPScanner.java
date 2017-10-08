package rs2.util.tools.account;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class IPScanner {

	private static File directory = new File("./Data/characters/");
	
	private static final String IP = "127.0.0.1";
	
	public static void main(String[] lol) {
		
		File[] files;
		
		files = directory.listFiles();
		
		for (int index = 0; index < files.length; index++) {
			try {
				Scanner scanner = new Scanner(new FileReader(files[index]));
				String line = "";
				while (scanner.hasNext()) {
					line = scanner.nextLine();
					if (line.contains(IP)) {
						System.out.println(files[index].getName());
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
