package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.content.minigame.zulrah.ZulrahConstants;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.combat.CombatType;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class ZulrahGreen extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		n.turnNpc(client.absX, client.absY);
		n.endGfx = 0;
		int nX = n.getX() + 2;
		int nY = n.getY() + 2;
		int pX = client.getX();
		int pY = client.getY();
		int offX = (nY - pY) * -1;
		int offY = (nX - pX) * -1;
		int speed = 105 + (n.distanceTo(client.getX(), client.getY()) * 5);
		client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, speed, ZulrahConstants.RANGE_PROJECTILE_ID, 83, 33, -client.getId() - 1, 95, 0, 36);
		n.startAnimation(ZulrahConstants.FARCAST_ANIMATION);
		n.getDamage().add(new Damage(client, Misc.random(ZulrahConstants.MAX_HIT), 4, CombatType.RANGE));
		if (n.HP <= ZulrahConstants.HYBDRID_STATE_HITPOINTS) {
			n.npcType = ZulrahConstants.BLUE_ZULRAH_ID;
		}
	}

}