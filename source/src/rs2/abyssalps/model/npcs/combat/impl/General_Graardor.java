package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class General_Graardor extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {

		int attack = Misc.random(20);

		int combatStyle = 0;

		int hitDelay = 2;

		int damage = 0;

		int offX = (n.getY() - client.getY()) * -1;
		int offY = (n.getX() - client.getX()) * -1;
		int distance = n.distanceTo(client.getX(), client.getY());
		int speed = 75 + (distance * 5);
		if (attack <= 15) {
			damage = Misc.random(n.maxHit);
			n.startAnimation(7018);
			hitDelay = 2;
			combatStyle = 0;
		} else {
			damage = Misc.random(35);
			n.startAnimation(7021);
			client.getPA().createPlayersProjectile(n.getX() + 1, n.getY() + 1,
					offX, offY, 50, speed, 1202, 1, 0, -client.getId() - 1, 65,
					0, 36);
			hitDelay = 3;
			combatStyle = 1;
		}

		n.getDamage().add(new Damage(client, damage, hitDelay, combatStyle));
	}

}
