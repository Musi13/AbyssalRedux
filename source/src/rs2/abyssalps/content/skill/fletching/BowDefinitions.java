package rs2.abyssalps.content.skill.fletching;

import java.util.HashMap;
import java.util.Map;

public enum BowDefinitions {

	NORMAL_LOGS(1511, 50, 1, 10, 48, 10, 20),
	OAK_LOGS(1521, 54, 20, 16, 56, 25, 25),
	WILLOW_LOGS(1519, 60, 35, 33, 58, 40, 41),
	MAPLE_LOGS(1517, 64, 50, 50, 62, 55, 58),
	YEW_LOGS(1515, 68, 65, 67, 66, 70, 75),
	MAGIC_LOGS(1513, 72, 80, 83, 70, 85, 91);

	private final int logsId;
	private final int[] bows;
	private final int[] levelsRequired;
	private final int[] experience;

	private BowDefinitions(int logsId, int bowA, int levelA, int experienceA, int bowB, int levelB, int experienceB) {
		this.logsId = logsId;
		this.bows = new int[] { bowA, bowB };
		this.levelsRequired = new int[] { levelA, levelB };
		this.experience = new int[] { experienceA, experienceB };
	}

	public int getLogsId() {
		return logsId;
	}

	public int[] getBows() {
		return bows;
	}

	public int[] getLevelsRequired() {
		return levelsRequired;
	}

	public int[] getExperience() {
		return experience;
	}

	private static Map<Integer, BowDefinitions> definitions = new HashMap<>();

	public static BowDefinitions getDefinition(int logsId) {
		return definitions.get(logsId);
	}

	static {
		for (BowDefinitions def : BowDefinitions.values()) {
			definitions.put(def.getLogsId(), def);
		}
	}

}