package rs2.abyssalps.content.skill.slayer;

import java.util.HashMap;

public enum SlayerTable {

	DARK_BEAST(4005, 90),

	CERBERUS(5862, 91),

	BANSHEE(414, 15),

	COCKATRICE(419, 25),

	INFERNAL_MAGE8(443, 45),

	TUROTH(426, 55),

	DUST_DEVIL(423, 65),

	NECH(11, 80),

	ABYSSAL_DEMON(415, 85),

	CRAWLING_HAND(448, 5),

	SMOKE_DEVIL(498, 93);

	SlayerTable(int id, int lvl) {
		this.id = id;
		this.level = lvl;
	}

	private int id;

	private int level;

	public int getId() {
		return this.id;
	}

	public int getLevel() {
		return this.level;
	}

	private static HashMap<Integer, SlayerTable> slayerMap = new HashMap<Integer, SlayerTable>();

	public static SlayerTable forId(int id) {
		return slayerMap.get(id);
	}

	static {
		for (SlayerTable s : SlayerTable.values()) {
			slayerMap.put(s.id, s);
		}
	}
}
