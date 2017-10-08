package rs2.abyssalps.model.npcs.combat.impl;

import rs2.Server;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.Misc;

public class Vetion extends SpecialNPCHandler {

	private static int coolDown = 6;
	public static int x = 0, x1, x2;
	public static int y = 0, y1, y2;

	public static boolean minionsSpawned = false;
	public static int minionsDead = 0;

	@Override
	public void execute(Client client, NPC n) {

		int nX = n.getX();
		int nY = n.getY();

		int damage = 0;

		if (n.HP <= 127 && !minionsSpawned) {
			Server.npcHandler.spawnNpc2(6613, nX + 2, nY, 0, 0, 110, 26, 220,
					220);
			Server.npcHandler.spawnNpc2(6613, nX - 2, nY, 0, 0, 110, 26, 220,
					220);
			Server.npcHandler.spawnNpc2(6613, nX, nY + 2, 0, 0, 110, 26, 220,
					220);
			minionsSpawned = true;
		}

		coolDown--;
		if (coolDown == 0) {
			n.startAnimation(5507);
			for (int index = 0; index < PlayerHandler.players.length; index++) {
				if (PlayerHandler.players[index] == null) {
					continue;
				}
				damage = Misc.random(45);
				if (n.distanceTo(PlayerHandler.players[index].getX(),
						PlayerHandler.players[index].getY()) <= 11) {
					n.getDamage().add(
							new Damage((Client) PlayerHandler.players[index],
									damage, 2, 9));
				}
			}
			coolDown = 6;
			return;
		}

		n.startAnimation(5499);

		damage = Misc.random(34);

		x = client.getX();
		y = client.getY();
		x1 = client.getX() + 1 + Misc.random(3);
		y1 = client.getY();
		x2 = client.getX();
		y2 = client.getY() - 1 - Misc.random(3);
		int offY1 = (n.getX() - x) * -1;
		int offX1 = (n.getY() - y) * -1;
		int offY2 = (n.getX() - x1) * -1;
		int offX2 = (n.getY() - y1) * -1;
		int offY3 = (n.getX() - x2) * -1;
		int offX3 = (n.getY() - x2) * -1;

		client.getPA().createPlayersProjectile(nX, nY, offX1, offY1, 50, 95,
				280, 43, 0, 0, 45, 30, 36);
		client.getPA().createPlayersProjectile(nX, nY, offX2, offY2, 50, 95,
				280, 43, 0, 0, 45, 30, 36);
		client.getPA().createPlayersProjectile(nX, nY, offX3, offY3, 50, 95,
				280, 43, 0, 0, 45, 30, 36);
		client.getPA().createPlayersStillGfx(281, x, y, 0, 95);
		client.getPA().createPlayersStillGfx(281, x1, y1, 0, 95);
		client.getPA().createPlayersStillGfx(281, x2, y2, 0, 95);
		for (int index = 0; index < PlayerHandler.players.length; index++) {
			if (PlayerHandler.players[index] == null) {
				continue;
			}
			n.getDamage().add(new Damage(client, damage, 4, 10));
		}
	}

}
