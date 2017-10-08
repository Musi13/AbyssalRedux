package rs2.abyssalps.content.skill.woodcutting;

import java.util.HashMap;

public enum TreeData {

	NORMAL_TREE(new int[] { 1276, 1277, 1278, 1279, 1280, 1330, 1331, 1332,
			2409, 3879, 3881, 3882, 3883, 9730, 9731, 9732, 9733, 10041, 14308,
			14309, 16264, 16265, 13599, }, 1, 25, 1511),

	OAK_TREE(new int[] { 8467, 9734, 11756, 13599 }, 15, 37, 1521),

	WILLOW_TREE(new int[] { 11755, 11759, 11761, 11763, 13599, }, 30, 67, 1519),

	MAPLE_TREE(new int[] { 4674, 8444, 11762, 13599 }, 45, 100, 1517),

	YEW_TREE(new int[] { 11758, 13599 }, 60, 175, 1515),

	MAGIC_TREE(new int[] { 8409, 11764, 13599 }, 75, 250, 1513);

	TreeData(int[] id, int level, int xp, int logId) {
		this.id = id;
		this.levelRequired = level;
		this.experience = xp;
		this.logId = logId;
	}

	private int[] id;
	private int levelRequired;
	private int experience;
	private int logId;

	public int getLevel() {
		return levelRequired;
	}

	public int getExperience() {
		return experience;
	}

	public int getLog() {
		return logId;
	}

	private static HashMap<Integer, TreeData> woodMap = new HashMap<Integer, TreeData>();

	public static TreeData forId(int objectId) {
		return woodMap.get(objectId);
	}

	static {
		for (TreeData d : TreeData.values()) {
			for (int index = 0; index < d.id.length; index++) {
				woodMap.put(d.id[index], d);
			}
		}
	}

	public static boolean treeExist(int id) {
		return woodMap.containsKey(id);
	}
}
