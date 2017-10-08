package rs2.util.tools.account;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DumpAccountNames {

	public static void main(String[] parameters) {
		File[] files;
		File directory = new File("./Data/characters");
		files = directory.listFiles();

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					"resets.txt", true));
			for (int index = 0; index < files.length; index++) {
				System.out.println(files[index].getName().replace(".txt", "")
						+ " dumped.");
				writer.write(files[index].getName().replace(".txt", ""));
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
