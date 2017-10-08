package rs2.util.tools.dumper;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

import rs2.Config;
import rs2.abyssalps.model.items.defs.ItemDefinitions;

/**
 * 
 * @author http://www.rune-server.org/members/sponjebobu/
 * 
 * 
 *         This class is used to dump grand exchange prices from RuneScape 2007
 *         Wiki
 */

public class GEDumper {

	public static void main(String[] parameters) {

		/**
		 * Unpack item definitions
		 */
		try {
			ItemDefinitions.init();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int index = 12643; index < Config.ITEM_LIMIT; index++) {

			/**
			 * Fetches the item definition class
			 */

			ItemDefinitions def = ItemDefinitions.forId(index);

			/**
			 * If item definition is null we continue;
			 */
			if (def == null) {
				continue;
			}

			/**
			 * Represents the websites "lines" / "code lines"
			 */
			String line = "";

			/**
			 * Declare a String variable for the item name Replace any <space>
			 * with '_'
			 */
			String itemName = def.getName().replace(" ", "_");

			try {

				/**
				 * URL getter
				 */
				URL url = new URL("http://2007.runescape.wikia.com/wiki/"
						+ itemName);

				/**
				 * Application now connects to
				 * http://2007.runescape.wikia.com/wiki/itemName
				 */
				url.openConnection();

				/**
				 * Scanner reads through the websites code
				 */
				Scanner scanner = new Scanner(new InputStreamReader(
						url.openStream()));

				/**
				 * BufferedWriter write the data we need to a .txt file
				 */
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						"prices.txt", true));

				while (scanner.hasNextLine()) {

					/**
					 * Everytime the while loop loops String line fetches a new
					 * line of the website
					 */
					line = scanner.nextLine();

					if (line.contains("</th><td> <span id=\"GEPrice\"><span class=\"GEItem\"><span>")) {
						line = line
								.replace(
										"</th><td> <span id=\"GEPrice\"><span class=\"GEItem\"><span>",
										"");
						line = line.replace("</span></span> coins</span>", "");
						line = line.replace(" (<a href=\"/wiki/Exchange:"
								+ itemName + "\" title=\"Exchange:" + itemName
								+ "\">info</a>)", "");
						line = line.replace(
								" (<a href=\"/wiki/Exchange:" + itemName
										+ "\" title=\"Exchange:"
										+ def.getName() + "\">info</a>)", "");
						line = line.replace(",", "").replace(" ", "")
								.replace("%27", "").replace("56", "");

						writer.write(index + ":" + line);

						System.out.println(index + ":" + line);

						writer.newLine();
					}

				}
				scanner.close();
				writer.close();

			} catch (IOException e) {

			}
		}

	}
}
