package rs2.abyssalps.content.skill.firemaking;

import java.util.HashMap;

public enum FiremakingTable {

	NORMAL_LOGS(1511, 1, 40), OAK_LOGS(1521, 15, 60), WILLOW_LOGS(1519, 30, 90), MAPLE_LOGS(
			1517, 45, 135), YEW_LOGS(1515, 60, (int) Math.floor(202.5)), MAGIC_LOGS(
			1513, 75, (int) Math.floor(303.8));

	private FiremakingTable(int logId, int lvl, int xp) {
		this.logId = logId;
		this.levelRequired = lvl;
		this.experience = xp;
	}

	private int logId;
	private int levelRequired;
	private int experience;

	public int getLogId() {
		return this.logId;
	}

	public int getLevel() {
		return this.levelRequired;
	}

	public int getExperience() {
		return this.experience;
	}

	private static HashMap<Integer, FiremakingTable> fireMap = new HashMap<Integer, FiremakingTable>();

	public static FiremakingTable forId(int logId) {
		return fireMap.get(logId);
	}

	static {
		for (FiremakingTable f : FiremakingTable.values()) {
			fireMap.put(f.getLogId(), f);
		}
	}
}
