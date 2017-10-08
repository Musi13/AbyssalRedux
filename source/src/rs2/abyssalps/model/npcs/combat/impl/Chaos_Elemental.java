package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.CombatType;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class Chaos_Elemental extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		n.startAnimation(3146);

		int combatStyle;

		if (Misc.random(1) == 0) {
			combatStyle = CombatType.MELEE;
		} else {
			if (Misc.random(1) == 0) {
				combatStyle = CombatType.RANGE;
			} else {
				combatStyle = CombatType.MAGIC;
			}
		}

		int offX = (n.getY() - client.getY()) * -1;
		int offY = (n.getX() - client.getX()) * -1;

		int distance = n.distanceTo(client.getX(), client.getY());

		int speed = 75 + (distance * 5);

		switch (combatStyle) {
		case CombatType.MELEE:
			n.gfx100(556);
			client.getPA().createPlayersProjectile(n.getX() + 1, n.getY() + 1,
					offX, offY, 50, speed, 557, 43, 33, -client.getId() - 1,
					65, 0);
			n.endGfx = 558;
			break;

		case CombatType.RANGE:
			n.gfx100(550);
			client.getPA().createPlayersProjectile(n.getX() + 1, n.getY() + 1,
					offX, offY, 50, speed, 551, 43, 33, -client.getId() - 1,
					65, 0);
			n.endGfx = 552;
			break;

		case CombatType.MAGIC:
			n.gfx100(553);
			client.getPA()
					.createPlayersProjectile(n.getX() + 1, n.getY() + 1, offX,
							offY, 50, 86, 554, 43, 33, -client.getId() - 1, 65,
							0);
			n.endGfx = 555;
			break;
		}

		n.getDamage().add(
				new Damage(client, Misc.random(MAX_HIT), 4, combatStyle));
	}

	private static final int MAX_HIT = 28;
}
