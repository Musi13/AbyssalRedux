package rs2.abyssalps.content.skill.crafting;

import java.util.HashMap;

public enum GemTable {

	sapphire(1623, 1727, 1, 50, 888), emerald(1621, 1729, 27, (int) 67.5, 889), ruby(
			1619, 1725, 34, 85, 887), diamond(1617, 1731, 43, (int) 107.5, 886), dragonStone(
			1631, 1712, 55, (int) 137.5, 885), onyx(6571, 6585, 67,
			(int) 167.5, 2717);

	private GemTable(int itemId, int newItemId, int level, int exp, int anim) {
		this.uncutId = itemId;
		this.amuletId = newItemId;
		this.levelRequired = level;
		this.experience = exp;
		this.emote = anim;
	}

	private int uncutId;
	private int amuletId;
	private int levelRequired;
	private int experience;
	private int emote;

	public int getAmuletId() {
		return this.amuletId;
	}

	public int getLevel() {
		return this.levelRequired;
	}

	public int getExperience() {
		return this.experience;
	}

	public int getEmote() {
		return this.emote;
	}

	private static HashMap<Integer, GemTable> gemMap = new HashMap<Integer, GemTable>();

	public static GemTable forId(int gemId) {
		return gemMap.get(gemId);
	}

	static {
		for (GemTable g : GemTable.values()) {
			gemMap.put(g.uncutId, g);
		}
	}
}
