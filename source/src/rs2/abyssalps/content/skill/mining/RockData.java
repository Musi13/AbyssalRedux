package rs2.abyssalps.content.skill.mining;

import java.util.HashMap;

public enum RockData {

	TIN_ROCK(13712, 1, 17, 438),
	COPPER_ROCK(13708, 1, 17, 436),
	IRON_ROCK(13710, 15, 35, 440),
	COAL_ROCK(13714, 30, 50, 453),
	MITHRIL_ROCK(13718, 50, 80, 447),
	ADAMANTITE_ROCK(14168, 70, 95, 449),
	RUNITE_ROCK(7418, 85, 125, 451);

	RockData(int id, int level, int experience, int oreId) {
		this.id = id;
		this.level = level;
		this.experience = experience;
		this.oreId = oreId;
	}

	private int id;
	private int level;
	private int experience;
	private int oreId;

	public int getLevel() {
		return this.level;
	}

	public int getExperience() {
		return this.experience;
	}

	public int getOreId() {
		return this.oreId;
	}

	private static HashMap<Integer, RockData> rockMap = new HashMap<Integer, RockData>();

	public static RockData forId(int id) {
		return rockMap.get(id);
	}

	static {
		for (RockData r : RockData.values()) {
			rockMap.put(r.id, r);
		}
	}

	public static boolean rockExist(int id) {
		return rockMap.containsKey(id);
	}
}
