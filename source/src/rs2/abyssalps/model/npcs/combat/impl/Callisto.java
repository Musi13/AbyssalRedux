package rs2.abyssalps.model.npcs.combat.impl;

import javafx.animation.Animation;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class Callisto extends SpecialNPCHandler {

	private static int attackStyle = 0;

	@Override
	public void execute(Client client, NPC n) {
		n.startAnimation(4925);
		int damage = Misc.random(n.maxHit);
		int chance = Misc.random(20);
		int combatStyle = 0;
		if (chance <= 13) {
			int chance1 = Misc.random(1);
			attackStyle = chance1;
		} else {
			attackStyle = 2;
		}
		if (attackStyle == 0) {
			combatStyle = 11;
			int offX = (n.getY() - client.getY()) * -1;
			int offY = (n.getX() - client.getX()) * -1;
			client.getPA().createPlayersProjectile(n.getX() + 1, n.getY() + 1,
					offX, offY, 50, 96, 395, 43, 31, -client.getId() - 1, 66,
					0, 36);
		}
		if (attackStyle == 1) {
			combatStyle = 0;
		}
		if (attackStyle == 2) {
			damage = 3;
			combatStyle = 12;
			callistoRoar(client, n.absX, n.absY);
		}
		n.getDamage().add(
				new Damage(client, damage, attackStyle == 1 ? 2 : 3,
						combatStyle));
	}

	private static int coordsX;
	private static int coordsY;

	private static void callistoRoar(Client client, int otherX, int otherY) {
		int x = client.absX - otherX;
		int y = client.absY - otherY;
		client.getCombat().resetPlayerAttack();
		client.attackTimer += 2;
		client.startAnimation(2109);
		coordsX = client.absX;
		coordsY = client.absY;
		if (x > 0) {
			client.setForceMovement(client.localX(), client.localY(),
					client.localX() + 3, client.localY(), 10, 60, 1);
			coordsX = client.absX + 3;
			coordsY = client.absY;
		} else if (x < 0) {
			client.setForceMovement(client.localX(), client.localY(),
					client.localX() - 3, client.localY(), 10, 60, -1);
			coordsX = client.absX - 3;
			coordsY = client.absY;
		}
		if (y > 0) {
			client.setForceMovement(client.localX(), client.localY(),
					client.localX(), client.localY() + 3, 10, 60, 1);
			coordsX = client.absX;
			coordsY = client.absY + 3;
		} else if (y < 0) {
			client.setForceMovement(client.localX(), client.localY(),
					client.localX(), client.localY() - 3, 10, 60, -1);
			coordsX = client.absX;
			coordsY = client.absY - 3;
		}

		CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				client.getPA().movePlayer(coordsX, coordsY, 0);
				container.stop();
			}

		}, 2);
	}

}
