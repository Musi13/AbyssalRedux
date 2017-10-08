package rs2.abyssalps.content.skill.mining;

import java.util.HashMap;
import java.util.Map;

import rs2.abyssalps.model.player.Client;

public enum PickData {

	BRONZE_PICKAXE(1265, 1, 625),

	IRON_PICKAXE(1267, 1, 626),

	STEEL_PICKAXE(1269, 6, 627),

	MITHRIL_PICKAXE(1273, 21, 629),

	ADAMANT_PICKAXE(1271, 31, 628),

	RUNE_PICKAXE(1275, 41, 624),

	DRAGON_PICKAXE(11920, 61, 7139);

	PickData(int id, int level, int emote) {
		this.id = id;
		this.level = level;
		this.emote = emote;
	}

	private int id;
	private int level;
	private int emote;

	public int getId() {
		return this.id;
	}

	public int getLevel() {
		return this.level;
	}

	public int getEmote() {
		return this.emote;
	}

	private static Map<Integer, PickData> pickMap = new HashMap<Integer, PickData>();

	public static PickData forId(int id) {
		return pickMap.get(id);
	}

	static {
		for (PickData p : PickData.values()) {
			pickMap.put(p.id, p);
		}
	}

	public static int getPlayerHatchetId(Client player) {
		for (int index = 0; index < player.playerItems.length; index++) {
			if (player.playerItems[index] > 0) {
				if (PickData.forId(player.playerItems[index] - 1) == null) {
					continue;
				}
				if (player.playerLevel[player.playerMining] < PickData.forId(
						player.playerItems[index] - 1).getLevel()) {
					player.sendMessage("You need a mining level of "
							+ PickData.forId(player.playerItems[index] - 1)
									.getLevel() + " to use this axe.");
					continue;
				}
				return player.playerItems[index] - 1;
			}
		}
		for (int index = 0; index < player.playerEquipment.length; index++) {
			if (player.playerEquipment[index] > 0) {
				if (PickData.forId(player.playerEquipment[index]) == null) {
					continue;
				}
				if (player.playerLevel[player.playerMining] < PickData.forId(
						player.playerEquipment[index]).getLevel()) {
					player.sendMessage("You need a mining level of "
							+ PickData.forId(player.playerEquipment[index])
									.getLevel() + " to use this axe.");
					continue;
				}
				return player.playerEquipment[index];
			}
		}
		return 0;
	}
}
