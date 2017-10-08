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

public class CorporealBeast extends SpecialNPCHandler {

	@Override
	public void execute(Client client, NPC npc) {
		int combatType = CombatType.MAGIC;

		if (npc.HP < (npc.MaxHP / 2)) {
			NPC[] darkCores = NPCHandler.getNpcsById(DARK_CORE_ID);
			if (darkCores.length <= 0) {
				client.getPA().createNpcProjectile(DARK_CORE_PROJECTILE_ID,
						npc, 2, 3, 50);
				CycleEventHandler.getSingleton().addEvent(client,
						new CycleEvent() {
							@Override
							public void execute(CycleEventContainer container) {
								NPC darkCoreNpc = Server.npcHandler.newNPC(
										DARK_CORE_ID, client.absX + 1,
										client.absY, client.heightLevel, 1, 25,
										13, 1, 20);
								darkCoreNpc.setRespawnable(false);
								container.stop();
							}
						}, 4);
			}
		} else {
			removeDarkCore();
		}

		if (client.distanceToPoint(npc.absX, npc.absY) < 3
				&& Misc.random(3) == 0) {
			combatType = CombatType.MELEE;
		}

		if (combatType == CombatType.MELEE) {
			npc.startAnimation(MELEE_ANIMATION);
			npc.endGfx = EXPLOSION_GFX_ID;
			npc.getDamage().add(
					new Damage(client, Misc.random(MAX_HIT_MELEE), 2,
							combatType));
		} else {
			int damage = Misc.random(MAX_HIT_MAGIC);
			npc.startAnimation(MAGIC_ANIMATION);
			int style = Misc.random(4);
			if (style == 0) {
				client.getPA().createNpcProjectile(BIG_MAGIC_PROJECTILE_ID,
						npc, 2, 3, 50);
			} else if (style == 1) {
				client.getPA().createNpcProjectile(REGULAR_MAGIC_PROJECTILE_ID,
						npc, 2, 3, 50);
				if (damage > 0) {
					createSplatter(client, npc);
				}
			} else {
				client.getPA().createNpcProjectile(SMALL_MAGIC_PROJECTILE_ID,
						npc, 2, 3, 50);
			}
			npc.getDamage().add(
					new Damage(client, damage, 4, combatType, style < 2));
		}

	}

	@Override
	public void onDeath(Client killer, NPC npc) {
		removeDarkCore();
	}

	private void createSplatter(Client client, NPC npc) {
		int splatters = 4 + Misc.random(3);
		for (int i = 0; i < splatters; i++) {
			createSplatterProjectile(client, -3 + Misc.random(6),
					-3 + Misc.random(6));
		}
	}

	private void createSplatterProjectile(Client client, int offsetX,
			int offsetY) {
		CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				client.getPA().createLocationPorjectile(
						REGULAR_MAGIC_PROJECTILE_ID, client.absX + offsetX,
						client.absY + offsetY, 0, 21);
				client.getPA().createPlayersStillGfx(SPLATTER_GFX_ID,
						client.absX + offsetX, client.absY + offsetY, 0, 21);
				container.stop();
			}
		}, 5);
	}

	private void removeDarkCore() {
		for (NPC darkCore : NPCHandler.getNpcsById(DARK_CORE_ID)) {
			darkCore.remove();
		}
	}

	public static final int CORPOREAL_BEAST_ID = 319;
	public static final int DARK_CORE_ID = 320;

	public static final int MAX_HIT_MELEE = 51;// 51;
	public static final int MAX_HIT_MAGIC = 65;// 65;

	public static final int MELEE_ANIMATION = 1682;
	public static final int MAGIC_ANIMATION = 1680;

	public static final int SMALL_MAGIC_PROJECTILE_ID = 314;
	public static final int REGULAR_MAGIC_PROJECTILE_ID = 315;
	public static final int BIG_MAGIC_PROJECTILE_ID = 316;

	public static final int SPLATTER_GFX_ID = 317;

	public static final int MAGIC_PROJECTILE_ID = 315;
	public static final int EXPLOSION_GFX_ID = 318;

	public static final int DARK_CORE_PROJECTILE_ID = 319;

}
