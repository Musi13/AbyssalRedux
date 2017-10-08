package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class Scorpia extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		n.startAnimation(6254);

		if (client.poisonDamage <= 0 && Misc.random(4) == 0) {
			client.getPA().appendPoison(1 + Misc.random(19));
		}

		n.getDamage().add(new Damage(client, Misc.random(n.maxHit), 2, 0));
	}

}
