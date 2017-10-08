package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.Misc;
import rs2.util.cache.region.Region;

public class Kree_Arra extends SpecialNPCHandler {

	private static int attackStyle = 0, projectileId;

	@Override
	public void execute(Client client, NPC n) {
		n.startAnimation(6980);
		int attack = Misc.random(2);
		int damage = Misc.random(n.maxHit);
		if (attack == 0) {
			damage = Misc.random(26);
			projectileId = 1199;
			attackStyle = 0;
		} else if (attack == 1) {
			damage = Misc.random(71);
			attackStyle = 1;
			projectileId = 1198;
		} else {
			damage = Misc.random(21);
			projectileId = 1200;
			attackStyle = 2;
		}
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			Client players = (Client) PlayerHandler.players[i];
			if (players == null)
				continue;
			if (n.distanceTo(players.getX(), players.getY()) < 16
					&& Region.canAttack(n, players)) {
				int offX = (n.getY() - players.getY()) * -1;
				int offY = (n.getX() - players.getX()) * -1;
				players.getPA().createPlayersProjectile(n.getX() + 1,
						n.getY() + 1, offX, offY, 50, 106, projectileId, 0, 0,
						-players.getId() - 1, 76, 0, 36);
				n.getDamage().add(new Damage(players, damage, 3, attackStyle));
			}
		}

	}
}
