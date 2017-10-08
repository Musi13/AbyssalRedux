package rs2.abyssalps.content.minigame.warriorsguild;

public enum AnimatorTable {

	IRON_ANIMATOR(2451, new int[] { 25, 20 });

	AnimatorTable(int id, int[] combatStats) {
		this.id = id;
		this.combatStats = combatStats;
	}

	private int id;
	private int[] combatStats;

	public int[] getCombatStats() {
		return this.combatStats;
	}

}
