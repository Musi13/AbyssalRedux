package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.npcs.combat.CombatType;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class King_Black_Dragon extends SpecialNPCHandler {

	private static int combatStyle = 1;

	public static int getCombatStyle() {
		return combatStyle;
	}

	@Override
	public void execute(Client client, NPC n) {

		int damage = 0;
		int hitDelay = 2;
		int attack = 5;

		int nX = n.getX() + 2;
		int nY = n.getY() + 2;
		int pX = client.getX();
		int pY = client.getY();
		int offX = (nY - pY) * -1;
		int offY = (nX - pX) * -1;

		if (n.distanceTo(client) > 2) {
			combatStyle = 1 + Misc.random(3);
		} else {
			combatStyle = Misc.random(4);
		}

		if (combatStyle > 0) {
			damage = Misc.random(65);
		} else {
			damage = Misc.random(25);
		}

		switch (combatStyle) {
		case 0:
			n.startAnimation(91);
			attack = 0;
			break;

		case 1:
		case 2:
		case 3:
		case 4:
			n.startAnimation(84);
			if (combatStyle == 1) {
				client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50,
						80, 393, 43, 33, -client.getId() - 1, 50, 0, 36);
				attack = CombatType.DRAGON_FIRE;
			} else if (combatStyle == 2) {
				client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50,
						80, 394, 43, 33, -client.getId() - 1, 50, 15, 36);
				attack = CombatType.POISON_BREATH;
			} else if (combatStyle == 3) {
				client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50,
						80, 395, 43, 33, -client.getId() - 1, 50, 15, 36);
				attack = CombatType.SHOCK_BREATH;
			} else if (combatStyle == 4) {
				client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50,
						80, 396, 43, 33, -client.getId() - 1, 50, 15, 36);
				attack = CombatType.ICE_BREATH;
			}
			hitDelay = (int) SpecialNPCHandler.getDelay(n, client, 50, 80);
			break;
		}
		n.getDamage().add(new Damage(client, damage, hitDelay, attack));
	}

}
