package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class Battlemage extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		n.startAnimation(n.npcType == 1612 ? 198 : 811);

		int attack = Misc.random(2);

		if (attack == 0) {
			n.endGfx = 76;
		} else if (attack == 1) {
			n.endGfx = 77;
		} else if (attack == 2) {
			n.endGfx = 78;
		}

		n.getDamage().add(new Damage(client, Misc.random(n.maxHit), 3, 2));
	}

}
