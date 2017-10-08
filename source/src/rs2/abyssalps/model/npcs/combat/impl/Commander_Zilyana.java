package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class Commander_Zilyana extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		int attack = Misc.random(10);
		int combatStyle = 0;
		if (attack <= 5) {
			n.startAnimation(6967);
			combatStyle = 0;
		} else {
			combatStyle = 2;
			n.startAnimation(6970);
		}
		n.getDamage().add(
				new Damage(client, Misc.random(n.maxHit), 2, combatStyle));
		if (combatStyle == 2) {
			n.endGfx = 1196;
			n.getDamage().add(
					new Damage(client, Misc.random(n.maxHit), 3, combatStyle));
		}
	}

}
