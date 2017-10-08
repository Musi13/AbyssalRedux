package rs2.abyssalps.model.npcs.combat.impl;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.npcs.combat.CombatType;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class TzTok_Jad extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC n) {
		int nX = n.getX() + 2;
		int nY = n.getY() + 2;
		int pX = client.getX();
		int pY = client.getY();
		int offX = (nY - pY) * -1;
		int offY = (nX - pX) * -1;
		int combatStyle = 2;
		int damage = Misc.random(n.maxHit);
		int distance = n.distanceTo(client.getX(), client.getY());
		int speed = 105 + (distance * 5);;
		if (n.distanceTo(client) == 1 && Misc.random(1) == 0) {
			n.startAnimation(NPCHandler.getAttackAnimation(n.npcId));
			n.getDamage().add(new Damage(client, damage, 3, CombatType.MELEE));
		} else {
			if (Misc.random(1) == 0) {
				n.endGfx = 157;
				client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, speed, 448, 83, 33, -client.getId() - 1, 95, 0, 36);
				n.startAnimation(2656);
			
				combatStyle = CombatType.MAGIC;
			} else {
				CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						client.gfx0(451);
						container.stop();
					}
				}, 2);
				n.startAnimation(2652);
				combatStyle = CombatType.RANGE;
			}
			n.getDamage().add(new Damage(client, damage, 4, combatStyle));
		}
	}

}
