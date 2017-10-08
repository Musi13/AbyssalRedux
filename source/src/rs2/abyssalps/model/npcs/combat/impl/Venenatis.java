package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class Venenatis extends SpecialNPCHandler {

	public static final int DEAD_ANIMATION = 5321;

	public static final int MAGIC_ANIMATION = 5322;

	public static final int WEB_STUN_ANIMATION = 5319;

	@Override
	public void execute(Client client, NPC n) {

		int nX = n.getX() + 2;
		int nY = n.getY() + 2;
		int pX = client.getX();
		int pY = client.getY();
		int offX = (nY - pY) * -1;
		int offY = (nX - pX) * -1;

		int distance = n.distanceTo(client.getX(), client.getY());

		int speed = 75 + (distance * 5);

		int damage = Misc.random(50);

		int attack = Misc.random(100);

		int combatStyle = 2;

		if (attack <= 70) {
			combatStyle = 2;
		} else {
			combatStyle = 11;
		}

		n.endGfx = -1;

		if (combatStyle == 2) {
			n.gfx100(164);
			n.startAnimation(MAGIC_ANIMATION);
			n.endGfx = 166;
			client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50,
					speed, 165, 43, 31, -client.getId() - 1, 65, 0, 46);
		} else {
			combatStyle = 0;
			damage = Misc.random(20);
			n.startAnimation(WEB_STUN_ANIMATION);
			n.endGfx = 254;
		}

		n.getDamage().add(
				new Damage(client, damage, combatStyle == 2 ? 3 : 2,
						combatStyle));
		if (combatStyle == 0 && Misc.random(10) <= 5) {
			n.getDamage().add(
					new Damage(client, Misc.random(10), 2, combatStyle));
		}
	}

}
