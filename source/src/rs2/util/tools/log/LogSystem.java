package rs2.util.tools.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import rs2.abyssalps.model.player.Client;

public class LogSystem {

	public static void addConnection(Client player, String adr) {
		if (!player.playerIPs.contains(adr)) {
			player.playerIPs.add(adr);
		}
	}

	public static void writeToFile(String playerName, String text, LogTypes type) {

		BufferedWriter writer = null;
		try {
			switch (type) {

			case TRADE:
				writer = new BufferedWriter(new FileWriter("./Data/logs/trade/"
						+ playerName + ".txt", true));
				writer.write(text + " - " + Calendar.getInstance().getTime());
				writer.newLine();
				writer.close();
				break;

			case DUEL:
				writer = new BufferedWriter(new FileWriter("./Data/logs/duel/"
						+ playerName + ".txt", true));
				writer.write(text + " - " + Calendar.getInstance().getTime());
				writer.newLine();
				writer.close();
				break;

			case COMMANDS:
				writer = new BufferedWriter(new FileWriter(
						"./Data/logs/commands/" + playerName + ".txt", true));
				writer.write(text + " - " + Calendar.getInstance().getTime());
				writer.newLine();
				writer.close();
				break;

			case PICKUP:
				writer = new BufferedWriter(new FileWriter(
						"./Data/logs/pickups/" + playerName + ".txt", true));
				writer.write(text + " - " + Calendar.getInstance().getTime());
				writer.newLine();
				writer.close();
				break;

			case DROP:
				writer = new BufferedWriter(new FileWriter("./Data/logs/drops/"
						+ playerName + ".txt", true));
				writer.write(text + " - " + Calendar.getInstance().getTime());
				writer.newLine();
				writer.close();
				break;

			case DEATH:
				writer = new BufferedWriter(new FileWriter(
						"./Data/logs/deaths/" + playerName + ".txt", true));
				writer.write(text + " - " + Calendar.getInstance().getTime());
				writer.newLine();
				writer.close();
				break;

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
