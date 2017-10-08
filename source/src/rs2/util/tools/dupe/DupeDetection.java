package rs2.util.tools.dupe;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.items.ItemAssistant;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.Misc;

public class DupeDetection {

	private static void writeToLog(String text, Type type) {
		BufferedWriter writer = null;
		try {
			if (type == Type.NAME) {
				writer = new BufferedWriter(new FileWriter(
						"./data/logs/dupers/potential_dupers.txt", true));
			} else if (type == Type.IP) {
				writer = new BufferedWriter(new FileWriter(
						"./data/logs/dupers/potential_duper_ips.txt", true));
				DupeDetectionConstants.dupeIPs.add(text);
			}
			writer.write(text);
			writer.newLine();
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void scanPlayers() {
		List<GameItem> items = new ArrayList<GameItem>();
		for (int index = 0; index < PlayerHandler.players.length; index++) {
			if (PlayerHandler.players[index] == null) {
				continue;
			}
			if (PlayerHandler.players[index].isJailed()) {
				continue;
			}
			Client player = (Client) PlayerHandler.players[index];
			boolean jail = false;
			for (String ip : DupeDetectionConstants.dupeIPs) {
				if (player.connectedFrom.equalsIgnoreCase(ip)) {
					jail = true;
				}
			}
			for (GameItem item : DupeDetectionConstants.ITEMS_TO_CHECK) {
				if (player.getItems().getItemAmount(item.id) >= item.amount
						|| player.getItems().bankItemAmount(item.id) >= item.amount) {
					items.add(item);
					jail = true;
				}
			}
			if (jail) {
				for (GameItem item : items) {
					writeToLog(
							Misc.formatPlayerName(player.playerName) + " - "
									+ item.amount + " x "
									+ ItemAssistant.getItemName(item.id)
									+ " - " + Calendar.getInstance().getTime(),
							Type.NAME);
					writeToLog(player.connectedFrom, Type.IP);
				}
				items.clear();
				player.setJailed(true);
				player.getPA().movePlayer(3104, 9517, 0);
				player.sendMessage("Your account has been jailed temporarily for suspecious activity.");
			}
		}
	}

	private static enum Type {
		NAME, IP
	}
}
