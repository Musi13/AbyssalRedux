package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class Magic_Spinolyp extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		n.startAnimation(2868);

		int nX = n.getX();
		int nY = n.getY();
		int pX = client.getX();
		int pY = client.getY();
		int offX = (nY - pY) * -1;
		int offY = (nX - pX) * -1;

		int distance = n.distanceTo(client.getX(), client.getY());

		int speed = 75 + (distance * 5);

		client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, speed,
				94, 33, 33, -client.getId() - 1, 65, 15, 36);

		n.endGfx = 95;

		n.getDamage().add(new Damage(client, Misc.random(n.maxHit), 3, 2));
	}

}
