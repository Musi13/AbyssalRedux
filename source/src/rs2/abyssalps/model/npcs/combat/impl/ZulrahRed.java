package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.content.minigame.zulrah.ZulrahConstants;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.CombatType;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class ZulrahRed extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		n.facePlayer(0);
		n.endGfx = 0;
		n.turnNpc(client.absX, client.absY);
		n.startAnimation(ZulrahConstants.FARCAST_ANIMATION);
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			final int playerX = client.absX;
			final int playerY = client.absY;

			@Override
			public void execute(CycleEventContainer container) {
				n.turnNpc(playerX, playerY);
				n.startAnimation(ZulrahConstants.MELEE_TARGET_ANIMATION);
				if (playerX == client.absX && playerY == client.absY) {
					n.getDamage().add(
							new Damage(client, Misc
									.random(ZulrahConstants.MAX_HIT), 2,
									CombatType.MELEE));
				}
				container.stop();
			}
		}, 5);
	}

}
