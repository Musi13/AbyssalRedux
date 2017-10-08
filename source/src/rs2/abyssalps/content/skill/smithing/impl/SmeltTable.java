package rs2.abyssalps.content.skill.smithing.impl;

import java.util.HashMap;
import java.util.Map;

public enum SmeltTable {

	BRONZE_BAR(436, 438, 1, 1, 2349, 1, (int) Math.floor(6.25)), IRON_BAR(440,
			-1, 1, 0, 2351, 15, (int) Math.floor(12.5)), STEEL_BAR(440, 453, 1,
			2, 2353, 30, (int) Math.floor(17.5)), MITHRIL_BAR(447, 453, 1, 4,
			2359, 50, 30), ADAMANTITE_BAR(449, 453, 1, 6, 2361, 70, (int) Math
			.floor(37.5)), RUNITE_BAR(451, 453, 1, 8, 2363, 85, 50);

	private SmeltTable(int oreId, int oreId2, int amount, int amount2,
			int barId, int level, int xp) {
		this.oreId = oreId;
		this.oreId2 = oreId2;
		this.amount = amount;
		this.amount2 = amount2;
		this.barId = barId;
		this.levelRequired = level;
		this.experience = xp;
	}

	private int oreId;
	private int oreId2;
	private int amount;
	private int amount2;
	private int barId;
	private int levelRequired;
	private int experience;

	public int getOreId() {
		return this.oreId;
	}

	public int getOreId2() {
		return this.oreId2;
	}

	public int getAmount() {
		return this.amount;
	}

	public int getAmount2() {
		return this.amount2;
	}

	public int getBarId() {
		return this.barId;
	}

	public int getLevel() {
		return this.levelRequired;
	}

	public int getExperience() {
		return this.experience;
	}

	private static Map<Integer, SmeltTable> smeltMap = new HashMap<Integer, SmeltTable>();

	public static SmeltTable forId(int barId) {
		return smeltMap.get(barId);
	}

	static {
		for (SmeltTable smelt : SmeltTable.values()) {
			smeltMap.put(smelt.barId, smelt);
		}
	}
}
