package rs2.abyssalps.content.skill.prayer;

import java.util.HashMap;

public enum BoneData {

	BONES(526, new double[] { 4.5, 4.5 }),

	BIG_BONES(532, new double[] { 15, 52.5 }),

	DAGANNOTH_BONES(6729, new double[] { 125, 437.5 }),

	BABYDRAGON_BONES(534, new double[] { 30, 120 }),

	DRAGON_BONES(536, new double[] { 72, 252 }),

	WYVERN_BONES(6812, new double[] { 72, 252 }),

	LAVA_DRAGON_BONES(11943, new double[] { 85, 297.5 });

	BoneData(int id, double[] experience) {
		this.id = id;
		this.experience = experience;
	}

	private int id;
	private double[] experience;

	public double[] getExperience() {
		return experience;
	}

	public static boolean boneExist(int id) {
		return boneMap.containsKey(id);
	}

	private static HashMap<Integer, BoneData> boneMap = new HashMap<Integer, BoneData>();

	public static BoneData getBoneData(int id) {
		return boneMap.get(id);
	}

	static {
		for (BoneData b : BoneData.values()) {
			boneMap.put(b.id, b);
		}
	}

}
