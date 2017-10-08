package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class Sergeant_Steelwill extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		n.startAnimation(6154);
		int offX = (n.getY() - client.getY()) * -1;
		int offY = (n.getX() - client.getX()) * -1;
		int distance = n.distanceTo(client.getX(), client.getY());
		int speed = 75 + (distance * 5);
		client.getPA().createPlayersProjectile(n.getX(), n.getY(), offX, offY,
				50, speed, 1217, 43, 31, -client.getId() - 1, 65, 0, 36);

		n.getDamage().add(new Damage(client, Misc.random(n.maxHit), 3, 2));
	}

}
