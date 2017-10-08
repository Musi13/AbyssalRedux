package rs2.abyssalps.model.npcs.combat.impl;

import rs2.Server;
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

public class Cerberus extends SpecialNPCHandler {

	private int RANGED_GHOST = 5867;
	private int MAGIC_GHOST = 5868;
	private int MELEE_GHOST = 5869;

	@Override
	public void execute(Client client, NPC n) {
		int attacks = 0;
		int hitDelay = 2;
		int combatType = 0;
		int damage = Misc.random(23);

		int nX = n.getX();
		int nY = n.getY();
		int pX = client.getX();
		int pY = client.getY();
		int offX = (nY - pY) * -1;
		int offY = (nX - pX) * -1;
		if (Misc.random(25) == 0
				&& !CycleEventHandler.getSingleton().isAlive(n, 999)) {
			n.forceChat("Aaarrroooooooo");
			NPC meleeGhost = Server.npcHandler.newNPC(MELEE_GHOST, 1239, 1264,
					0, 0, 1, 10, 10, 10);
			NPC mageGhost = Server.npcHandler.newNPC(MAGIC_GHOST, 1240, 1264,
					0, 0, 1, 10, 10, 10);
			NPC rangeGhost = Server.npcHandler.newNPC(RANGED_GHOST, 1241, 1264,
					0, 0, 1, 10, 10, 10);
			meleeGhost.walkTo(meleeGhost.absX, 1256);
			mageGhost.walkTo(mageGhost.absX, 1256);
			rangeGhost.walkTo(rangeGhost.absX, 1256);
			CycleEventHandler.getSingleton().addEvent(999, n, new CycleEvent() {
				int state = 0;
				boolean reached = false;

				@Override
				public void execute(CycleEventContainer container) {
					if (meleeGhost.absY == 1256 && mageGhost.absY == 1256
							&& rangeGhost.absY == 1256) {
						reached = true;
					}
					if (reached) {
						state++;
						int offX, offY = 0;
						switch (state) {
						case 1:
							offX = (meleeGhost.getY() - pY) * -1;
							offY = (meleeGhost.getX() - pX) * -1;
							client.getPA().createPlayersProjectile(
									meleeGhost.getX(), meleeGhost.getY(), offX,
									offY, 50, 95, 1248, 43, 31,
									-client.getId() - 1, 65, 0, 36);
							n.getDamage().add(
									new Damage(client, 30,
											(int) SpecialNPCHandler.getDelay(n,
													client, 50, 95),
											CombatType.MELEE));
							break;
						case 2:
							meleeGhost.walkTo(meleeGhost.absX, 1264);
							offX = (mageGhost.getY() - pY) * -1;
							offY = (mageGhost.getX() - pX) * -1;
							client.getPA().createPlayersProjectile(
									mageGhost.getX(), mageGhost.getY(), offX,
									offY, 50, 95, 100, 43, 31,
									-client.getId() - 1, 65, 0, 36);
							n.getDamage().add(
									new Damage(client, 30,
											(int) SpecialNPCHandler.getDelay(n,
													client, 50, 95),
											CombatType.MAGIC));
							break;
						case 3:
							mageGhost.walkTo(mageGhost.absX, 1264);
							offX = (rangeGhost.getY() - pY) * -1;
							offY = (rangeGhost.getX() - pX) * -1;
							client.getPA().createPlayersProjectile(
									rangeGhost.getX(), rangeGhost.getY(), offX,
									offY, 50, 95, 1116, 43, 31,
									-client.getId() - 1, 65, 0, 36);
							n.getDamage().add(
									new Damage(client, 30,
											(int) SpecialNPCHandler.getDelay(n,
													client, 50, 95),
											CombatType.RANGE));
							break;
						case 4:
							rangeGhost.walkTo(rangeGhost.absX, 1264);
							break;
						}

						if (state >= 4)
							if (meleeGhost.absY == 1264
									&& mageGhost.absY == 1264
									&& rangeGhost.absY == 1264) {
								meleeGhost.setVisible(false);
								rangeGhost.setVisible(false);
								mageGhost.setVisible(false);
								container.stop();
							}

					}
				}
			}, 5);
			return;

		} else if (n.HP < 200 && Misc.random(3) == 0) {
			n.forceChat("Grrrrrrrrrrrrrr");
			n.forcedChatRequired = true;
			n.updateRequired = true;
			int x = client.getX();
			int y = client.getY();
			int x1 = client.getX() + 1 + Misc.random(2);
			int y1 = client.getY();
			int x2 = client.getX();
			int y2 = client.getY() - 1 - Misc.random(2);
			n.startAnimation(4494);
			client.getPA().createPlayersStillGfx(1246, x, y, 0, 10);
			client.getPA().createPlayersStillGfx(1246, x1, y1, 0, 10);
			client.getPA().createPlayersStillGfx(1246, x2, y2, 0, 10);
			CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if (client.getX() == x || client.getY() == y
							|| client.getX() == x1 || client.getY() == y1
							|| client.getX() == x2 || client.getY() == y2) {
						client.dealDamage(damage);
						client.handleHitMask(damage);
						client.getPA().refreshSkill(3);
						client.updateRequired = true;
					}
					client.getPA().createPlayersStillGfx(1247, x, y, 0, 10);
					client.getPA().createPlayersStillGfx(1247, x1, y1, 0, 10);
					client.getPA().createPlayersStillGfx(1247, x2, y2, 0, 10);
					container.stop();
				}

			}, 4);
			return;
		}

		attacks = Misc.random(100);
		if (attacks <= 40) {
			n.startAnimation(4491);
			combatType = 0;
		} else if (attacks > 40 && attacks <= 60) {
			client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, 95,
					1245, 43, 31, -client.getId() - 1, 65, 0, 36);
			n.startAnimation(4489);
			hitDelay = (int) SpecialNPCHandler.getDelay(n, client, 65, 95);
			combatType = 1;
		} else if (attacks > 60) {
			client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, 95,
					1242, 43, 31, -client.getId() - 1, 65, 0, 36);
			n.startAnimation(4489);
			hitDelay = (int) SpecialNPCHandler.getDelay(n, client, 65, 95);
			combatType = 2;
		}
		n.getDamage().add(new Damage(client, damage, hitDelay, combatType));
	}

}
