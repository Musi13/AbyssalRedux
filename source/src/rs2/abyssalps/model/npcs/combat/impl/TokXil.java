package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class TokXil extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		n.startAnimation(2633);
		int offX = (n.getY() - client.getY()) * -1;
		int offY = (n.getX() - client.getX()) * -1;
		client.getPA().createPlayersProjectile(n.getX() + 1, n.getY() + 1,
				offX, offY, 50, 86, 443, 73, 31, -client.getId() - 1, 66);
		n.getDamage().add(new Damage(client, Misc.random(n.maxHit), 3, 1));

	}

}
