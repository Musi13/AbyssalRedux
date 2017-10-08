package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class Wingman_Skree extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		n.startAnimation(6956);
		int offX = (n.getY() - client.getY()) * -1;
		int offY = (n.getX() - client.getX()) * -1;
		client.getPA().createPlayersProjectile(n.getX() + 1, n.getY() + 1,
				offX, offY, 50, 106, 1192, 66, 31, -client.getId() - 1, 76, 15,
				36);
		n.getDamage().add(new Damage(client, Misc.random(n.maxHit), 3, 1));
	}

}
