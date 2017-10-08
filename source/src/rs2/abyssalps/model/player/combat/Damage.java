package rs2.abyssalps.model.player.combat;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.player.Client;

public class Damage {

	private int damage;
	private int combatType;
	private int ticks;
	private boolean isPlayer = false;
	private boolean pid = false;

	private int target;

	public int getDamage() {
		return this.damage;
	}

	public int getCombatType() {
		return combatType;
	}

	public int getTicks() {
		return ticks;
	}

	public int getTarget() {
		return target;
	}

	public boolean isPlayer() {
		return isPlayer;
	}

	public boolean getPid() {
		return this.pid;
	}

	public void decreaseTicks() {
		ticks--;
	}

	public Damage(int target, int damage, int combatType, int ticks,
			boolean isPlayer, boolean pid) {
		this.target = target;
		this.damage = damage;
		this.combatType = combatType;
		this.ticks = ticks;
		this.isPlayer = isPlayer;
		this.pid = pid;
	}

}
