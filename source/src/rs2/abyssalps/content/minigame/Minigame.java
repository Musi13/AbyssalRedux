package rs2.abyssalps.content.minigame;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.combat.Damage;

public interface Minigame {

	boolean enter(Client client);
	
	void exit(Client client	);
	
	void process(Client client);
	
	default void onLogout(Client client) {
		exit(client);
	}
	
	default boolean onDamage(Client client, Damage damage) {
		return false;
	}
	
	default boolean onDropItems(Client client, NPC npc) {
		return false;
	}
	
}