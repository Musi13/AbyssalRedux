package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class Chaos_Fanatic extends SpecialNPCHandler {

	public static int x = 0, x1, x2;
	public static int y = 0, y1, y2;

	@Override
	public void execute(Client client, NPC n) {
		n.startAnimation(811);
		int attack = Misc.random(100);
		int combatStyle = 2;
		int hitDelay = 0;
		int damage = Misc.random(31);
		int nX = n.getX();
		int nY = n.getY();
		int pX = client.getX();
		int pY = client.getY();
		int offX = (nY - pY) * -1;
		int offY = (nX - pX) * -1;

		String[] messages = { "Burn!", "WEUGH!", "Develish Oxen Roll!",
				"All your wilderness are belong to them!",
				"AhehHeheuhHhahueHuUEehEahAH",
				"I shall call him squidgy and he shall be my squidgy!" };

		n.forceChat(messages[(int) (Math.random() * messages.length)]);
		n.forcedChatRequired = true;
		n.updateRequired = true;

		if (attack <= 70) {
			client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, 65,
					554, 43, 31, -client.getId() - 1, 45, 0, 36);
			combatStyle = 2;
			hitDelay = (int) SpecialNPCHandler.getDelay(n, client, 45, 65);
		} else {
			x = client.getX();
			y = client.getY();
			x1 = client.getX() + 1 + Misc.random(2);
			y1 = client.getY();
			x2 = client.getX();
			y2 = client.getY() - 1 - Misc.random(2);
			int offY1 = (n.getX() - x) * -1;
			int offX1 = (n.getY() - y) * -1;
			int offY2 = (n.getX() - x1) * -1;
			int offX2 = (n.getY() - y1) * -1;
			int offY3 = (n.getX() - x2) * -1;
			int offX3 = (n.getY() - x2) * -1;
			client.getPA().createPlayersProjectile(nX, nY, offX1, offY1, 50,
					95, 551, 43, 0, 0, 45, 30, 36);
			client.getPA().createPlayersProjectile(nX, nY, offX2, offY2, 50,
					95, 551, 43, 0, 0, 45, 30, 36);
			client.getPA().createPlayersProjectile(nX, nY, offX3, offY3, 50,
					95, 551, 43, 0, 0, 45, 30, 36);
			client.getPA().createPlayersStillGfx(157, x, y, 0, 95);
			client.getPA().createPlayersStillGfx(157, x1, y1, 0, 95);
			client.getPA().createPlayersStillGfx(157, x2, y2, 0, 95);
			n.getDamage().add(
					new Damage(client, damage, (int) SpecialNPCHandler
							.getDelay(n, client, 45, 95), 4));
			return;
		}
		n.getDamage().add(new Damage(client, damage, hitDelay, combatStyle));
	}

}
