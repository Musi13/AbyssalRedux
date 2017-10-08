package rs2.abyssalps.content.skill.woodcutting;

import java.util.HashMap;
import java.util.Map;

import rs2.abyssalps.model.player.Client;

public enum HatchetData {

	BRONZE_HATCHET(1351, 1, 879),
	
	IRON_HATCHET(1349, 1, 877),
	
	STEEL_HATCHET(1353, 6, 875),
	
	BLACK_HATCHET(1361, 6, 873),
	
	MITHRIL_HATCHET(1355, 21, 871),
	
	ADAMANT_HATCHET(1357, 31, 869),
	
	RUNE_HATCHET(1359, 41, 867),
	
	DRAGON_HATCHET(6739, 61, 2846);

	HatchetData(int id, int level, int emote) {
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

	private static Map<Integer, HatchetData> hatchetMap = new HashMap<Integer, HatchetData>();

	public static HatchetData forId(int id) {
		return hatchetMap.get(id);
	}

	static {
		for (HatchetData h : HatchetData.values()) {
			hatchetMap.put(h.id, h);
		}
	}

	public static int getPlayerHatchetId(Client player) {
		for (int index = 0; index < player.playerItems.length; index++) {
			if (player.playerItems[index] > 0) {
				if (HatchetData.forId(player.playerItems[index] - 1) == null) {
					continue;
				}
				if (player.playerLevel[player.playerWoodcutting] < HatchetData
						.forId(player.playerItems[index] - 1).getLevel()) {
					player.sendMessage("You need a woodcutting level of "
							+ HatchetData.forId(player.playerItems[index] - 1)
									.getLevel() + " to use this axe.");
					continue;
				}
				return player.playerItems[index] - 1;
			}
		}
		for (int index = 0; index < player.playerEquipment.length; index++) {
			if (player.playerEquipment[index] > 0) {
				if (HatchetData.forId(player.playerEquipment[index]) == null) {
					continue;
				}
				if (player.playerLevel[player.playerWoodcutting] < HatchetData
						.forId(player.playerEquipment[index]).getLevel()) {
					player.sendMessage("You need a woodcutting level of "
							+ HatchetData.forId(player.playerEquipment[index])
									.getLevel() + " to use this axe.");
					continue;
				}
				return player.playerEquipment[index];
			}
		}
		return 0;
	}
}
