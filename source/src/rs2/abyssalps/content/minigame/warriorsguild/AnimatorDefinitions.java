package rs2.abyssalps.content.minigame.warriorsguild;

import java.util.HashMap;
import java.util.Map;

public enum AnimatorDefinitions {

	BRONZE_ARMOUR(1155, 1117, 1075, 5, 2450, 10, 2, 10, 10),
	IRON_ARMOUR(1153, 1115, 1067, 10, 2451, 20, 4, 20, 20),
	STEEL_ARMOUR(1157, 1119, 1069, 15, 2452, 40, 5, 40, 40),
	BLACK_ARMOUR(1165, 1125, 1077, 20, 2453, 60, 7, 60, 60),
	MITHRIL_ARMOUR(1159, 1121, 1071, 25, 2454, 80, 10, 80, 80),
	ADAMANT_ARMOUR(1161, 1123, 1073, 30, 2455, 99, 12, 99, 99),
	RUNE_ARMOUR(1163, 1127, 1079, 40, 2456, 120, 14, 120, 120);
	

	private final int[] items;
	private final int rewardAmount;

	private final int npcId;
	private final int hitpoints, maxHit, attack, defence;

	private AnimatorDefinitions(int helmetId, int platebodyId, int legsId, int rewardAmount, int npcId, int hitpoints, int maxHit, int attack, int defence) {
		this.items = new int[] { helmetId, platebodyId, legsId };
		this.rewardAmount = rewardAmount;
		this.npcId = npcId;
		this.hitpoints = hitpoints;
		this.maxHit = maxHit;
		this.attack = attack;
		this.defence = defence;
	}

	public int[] getItems() {
		return items;
	}

	public int getRewardAmount() {
		return rewardAmount;
	}

	public int getNpcId() {
		return npcId;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	public int getMaxHit() {
		return maxHit;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefence() {
		return defence;
	}

	private static Map<Integer, AnimatorDefinitions> npcToDefinition = new HashMap<>();
	private static Map<Integer, AnimatorDefinitions> itemToDefinition = new HashMap<>();
	
	public static AnimatorDefinitions getDefinitionByNpc(int npcId) {
		return npcToDefinition.get(npcId);
	}
	
	public static AnimatorDefinitions getDefinitionByItem(int itemId) {
		return itemToDefinition.get(itemId);
	}
	
	static {
		for (AnimatorDefinitions def : AnimatorDefinitions.values()) {
			npcToDefinition.put(def.getNpcId(), def);
			itemToDefinition.put(def.getItems()[0], def);
			itemToDefinition.put(def.getItems()[1], def);
			itemToDefinition.put(def.getItems()[2], def);
		}
	}
	
}