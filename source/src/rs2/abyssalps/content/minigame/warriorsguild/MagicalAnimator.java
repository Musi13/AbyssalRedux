package rs2.abyssalps.content.minigame.warriorsguild;

import rs2.Server;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class MagicalAnimator {

	public static void animateArmor(Client client, int itemId, int objectX,
			int objectY) {
		AnimatorDefinitions def = AnimatorDefinitions
				.getDefinitionByItem(itemId);
		if (def == null) {
			return;
		}

		if (!hasRequiredItems(client, def)) {
			return;
		}

		for (NPC npc : NPCHandler.npcs) {
			if (npc == null) {
				continue;
			}

			if (npc.spawnedBy != client.getId()) {
				continue;
			}

			if (npc.npcType == def.getNpcId() && !npc.isDead) {
				return;
			}
		}

		client.setBlockWalking(true);

		CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
			int index = 0;

			@Override
			public void execute(CycleEventContainer container) {
				if (container.getTick() == 0) {
					client.startAnimation(SET_ANIMATION_ID);
					container.setTick(2);
					return;
				}
				if (index > 2) {
					NPC npc = Server.npcHandler.spawnNpc(client,
							def.getNpcId(), objectX, objectY,
							client.heightLevel, 0, def.getHitpoints(),
							def.getMaxHit(), def.getAttack(), def.getDefence(),
							true, true);
					client.setBlockWalking(false);
					client.faceUpdate(npc.npcId);
					container.stop();
					return;
				} else if (index < 2) {
					client.startAnimation(SET_ANIMATION_ID);
				} else {
					client.setRun(false);
					client.getPA().walkTo(0, 2);
				}
				client.getItems().deleteItem(def.getItems()[index++], 1);
			}
		}, 0);
	}

	public static boolean dropItems(Client client, NPC npc) {
		AnimatorDefinitions def = AnimatorDefinitions
				.getDefinitionByNpc(npc.npcType);
		if (def == null) {
			return false;
		}

		for (int itemId : def.getItems()) {
			Server.itemHandler.createGroundItem(client, itemId, npc.absX,
					npc.absY, npc.heightLevel, 1, client.getId());
		}

		Server.itemHandler.createGroundItem(client, CyclopsConstants.TOKEN_ID,
				npc.absX, npc.absY, npc.heightLevel, def.getRewardAmount(),
				client.getId());
		return true;
	}

	public static boolean hasRequiredItems(Client client,
			AnimatorDefinitions def) {
		for (int itemId : def.getItems()) {
			if (!client.getItems().playerHasItem(itemId)) {
				return false;
			}
		}
		return true;
	}

	public static final int OBJECT_ID = 23955;

	public static final int SET_ANIMATION_ID = 827;

}