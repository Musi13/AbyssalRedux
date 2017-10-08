package rs2.abyssalps.content.skill.cooking;

import java.util.HashMap;

public enum CookData {

	SHRIMPS(317, 7954, 315, 1, 30),

	TROUT(335, 343, 333, 15, 70),

	TUNA(359, 367, 361, 30, 100),

	SWORDFISH(371, 375, 373, 45, 140),

	KARAMBWAN(3142, 3148, 3144, 30, 180),

	SHARK(383, 387, 385, 80, 210),

	ANGLERFISH(13439, 13443, 13441, 84, 230);

	CookData(int id, int id2, int id3, int lvl, int xp) {
		this.rawId = id;
		this.burntId = id2;
		this.cookedId = id3;
		this.level = lvl;
		this.experience = xp;
	}

	private int rawId;
	private int burntId;
	private int cookedId;
	private int level;
	private int experience;

	public int getRawId() {
		return this.rawId;
	}

	public int getBurntId() {
		return this.burntId;
	}

	public int getCookedId() {
		return cookedId;
	}

	public int getLevel() {
		return this.level;
	}

	public int getExperience() {
		return this.experience;
	}

	private static HashMap<Integer, CookData> cookMap = new HashMap<Integer, CookData>();

	public static CookData forId(int id) {
		return cookMap.get(id);
	}

	static {
		for (CookData c : CookData.values()) {
			cookMap.put(c.rawId, c);
		}
	}

	public static boolean cookable(int id) {
		return cookMap.containsKey(id);
	}
}
