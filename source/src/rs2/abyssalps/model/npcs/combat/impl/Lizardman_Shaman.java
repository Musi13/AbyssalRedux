package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class Lizardman_Shaman extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		int attack = Misc.random(100);

		int combatStyle = 0;

		if (attack <= 50) {
			n.startAnimation(7192);
			combatStyle = 0;
		} else {
			n.startAnimation(7193);
			combatStyle = 2;
			n.endGfx = 1294;
		}

		n.getDamage().add(
				new Damage(client, Misc.random(n.maxHit), 2, combatStyle));
	}

}
