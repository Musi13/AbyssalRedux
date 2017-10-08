package rs2.abyssalps.model.npcs.combat;

import rs2.abyssalps.model.player.Client;

public class Damage {
	
	public Damage(Client client, int damage, int ticks, int combatType) {
		this(client, damage, ticks, combatType, false);
	}
	
	public Damage(Client client, int damage, int ticks, int combatType, boolean ignorePrayer) {
		this.target = client;
		this.damage = damage;
		this.ticks = ticks;
		this.combatType = combatType;
		this.ignorePrayer = ignorePrayer;
	}

	private Client target;
	
	private int damage;
	
	private int ticks;
	
	private int combatType;
	
	public boolean ignorePrayer;
	
	public Client getTarget() {
		return target;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public int getTicks(){
		return ticks;
	}
	
	public int getCombatType() {
		return combatType;
	}
	
	public boolean ignorePrayer() {
		return ignorePrayer;
	}
	
	public void decreaseTicks() {
		ticks--;
	}
}
