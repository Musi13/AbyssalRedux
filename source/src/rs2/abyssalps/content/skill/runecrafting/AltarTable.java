package rs2.abyssalps.content.skill.runecrafting;

import java.util.HashMap;

import rs2.abyssalps.model.items.GameItem;

public enum AltarTable {

	CHAOS_ALTAR(14906, 35, 8.5, new GameItem(562, 1)),

	AIR_ALTAR(14897, 1, 5, new GameItem(556, 1)),

	MINE_ALTAR(14898, 2, 5.5, new GameItem(558, 1)),

	WATER_ALTAR(14899, 5, 6, new GameItem(555, 1)),

	EARTH_ALTAR(14900, 9, 6.5, new GameItem(557, 1)),

	FIRE_ALTAR(14901, 14, 7, new GameItem(554, 1)),

	BODY_ALTAR(14902, 20, 7.5, new GameItem(559, 1)),

	COSMIC_ALTAR(14903, 27, 7, new GameItem(564, 1)),

	NATURE_ALTAR(14905, 44, 9, new GameItem(561, 1)),

	LAW_ALTAR(14904, 54, 9.5, new GameItem(563, 1)),

	DEATH_ALTAR(14907, 65, 10, new GameItem(560, 1)),

	ASTRAL_RUNE(14911, 40, 8.7, new GameItem(9075, 1));

	AltarTable(int id, int level, double experience, GameItem item) {
		this.id = id;
		this.levelRequired = level;
		this.experience = experience;
		this.item = item;
	}

	private int id;
	private int levelRequired;
	private double experience;
	private GameItem item;

	public int getLevelRequired() {
		return this.levelRequired;
	}

	public double getExperience() {
		return this.experience;
	}

	public GameItem getItem() {
		return this.item;
	}

	private static HashMap<Integer, AltarTable> altarMap = new HashMap<Integer, AltarTable>();

	public static AltarTable forId(int id) {
		return altarMap.get(id);
	}

	public static boolean isAltar(int id) {
		return altarMap.containsKey(id);
	}

	static {
		for (AltarTable a : AltarTable.values()) {
			altarMap.put(a.id, a);
		}
	}
}
