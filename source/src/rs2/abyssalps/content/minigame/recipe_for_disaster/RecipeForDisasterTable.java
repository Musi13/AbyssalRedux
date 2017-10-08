package rs2.abyssalps.content.minigame.recipe_for_disaster;

import java.util.HashMap;

public enum RecipeForDisasterTable {

	WAVE_1(0, RecipeForDisasterConstants.AGRITH_NA_NA_ID, new int[] { 200, 16,
			83, 150 }),

	WAVE_2(1, RecipeForDisasterConstants.FLAMBEED_ID, new int[] { 210, 13, 240,
			75 }),

	WAVE_3(2, RecipeForDisasterConstants.KARAMEL_ID, new int[] { 250, 7, 100,
			100 }),

	WAVE_4(3, RecipeForDisasterConstants.DESSOURT_ID, new int[] { 130, 19, 198,
			99 });

	RecipeForDisasterTable(int waveId, int npcId, int[] combatAttributes) {
		this.waveId = waveId;
		this.npcId = npcId;
		this.combatAttributes = combatAttributes;
	}

	private int waveId;
	private int npcId;
	private int[] combatAttributes;

	public int getWaveId() {
		return this.waveId;
	}

	public int getNpcId() {
		return this.npcId;
	}

	public int[] getCombatAttributes() {
		return this.combatAttributes;
	}

	private static HashMap<Integer, RecipeForDisasterTable> recipeMap = new HashMap<Integer, RecipeForDisasterTable>();

	public static RecipeForDisasterTable forId(int waveId) {
		return recipeMap.get(waveId);
	}

	static {
		for (RecipeForDisasterTable r : RecipeForDisasterTable.values()) {
			recipeMap.put(r.getWaveId(), r);
		}
	}
}
