package rs2.util.tools.account;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ItemWiper {

	private static File charDir = new File("Data/characters/");

	public static void main(String[] args) {
		if (charDir.exists() && charDir.isDirectory()) {
			File[] charFiles = charDir.listFiles();
			for (int i = 0; i < charFiles.length; i++) {
				resetEcoChar(charFiles[i]);
				System.out.println("Reset the Player Economy for: "
						+ charFiles[i].toString());
			}
		}
	}

	private static void resetEcoChar(File charFile) {
		try {

			String tempData;
			String tempAdd = "";
			int curEquip = 0;
			File tempCharFile = new File(charDir.toString() + "ECOBOOST$TEMP");
			DataInputStream fileStream = new DataInputStream(
					new FileInputStream(charFile));
			BufferedWriter tempOut = new BufferedWriter(new FileWriter(
					tempCharFile));

			while ((tempData = fileStream.readLine()) != null) {
				for (int index = 0; index < 8; index++) {
					if (!tempData.trim().contains("bank-tab = " + index + "	")) {
						tempAdd = tempData.trim();
					}
					tempOut.write(tempAdd);
					tempOut.newLine();
				}
				if (!tempData.trim().startsWith("character-item =")) {
					tempAdd = tempData.trim();
					if (tempData.trim().startsWith("character-equip =")) {
						tempAdd = "character-equip = " + curEquip + "\t-1\t0";
						curEquip++;
					}
					tempOut.write(tempAdd);
					tempOut.newLine();
				}
			}
			fileStream.close();
			tempOut.close();
			charFile.delete();
			tempCharFile.renameTo(charFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
