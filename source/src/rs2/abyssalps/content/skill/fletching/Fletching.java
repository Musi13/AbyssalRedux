package rs2.abyssalps.content.skill.fletching;

import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class Fletching {

	public static boolean fletch(Client client, int itemSlotA, int itemSlotB) {
		if (fletchBows(client, itemSlotA, itemSlotB)) {
			return true;
		}
		if (stringBows(client, itemSlotA, itemSlotB)) {
			return true;
		}
		return false;
	}

	private static boolean fletchBows(Client client, int itemSlotA,
			int itemSlotB) {
		int itemIdA = client.playerItems[itemSlotA] - 1;
		int itemIdB = client.playerItems[itemSlotB] - 1;

		if (!Misc.contains(FletchingConstants.KNIFE_ID, itemIdA, itemIdB)) {
			return false;
		}

		int logsId = Misc.getExcept(FletchingConstants.KNIFE_ID, itemIdA,
				itemIdB);

		BowDefinitions def = BowDefinitions.getDefinition(logsId);

		if (def == null) {
			return false;
		}

		client.getInterfaces().openItemSelection("Select a bow", def.getBows()[0], def.getBows()[1], (id, amount) -> {
			if (client.playerLevel[client.playerFletching] < def.getLevelsRequired()[id]) {
				client.sendMessage("You need level " + def.getLevelsRequired()[id] + " Fletching to fletch this bow.");
				return;
			}
			startFletching(client);
			for (int i = 0; i < amount; i++) {
				fletchBow(client, def.getLogsId(), def.getBows()[id], def.getExperience()[id], i * 4);
			}
		});


		return true;
	}
	
	private static void fletchBow(Client client, int logsId, int bowId, int experience, int delay) {
		CycleEventHandler.getSingleton().addEvent(CycleEventHandler.FLETCHING_ID, client, new CycleEvent() {
			boolean animating = false;
			@Override
			public void execute(CycleEventContainer container) {
				if (!client.skillActive()[client.playerFletching] || !client.getItems().playerHasItem(logsId) || !client.getItems().playerHasItem(FletchingConstants.KNIFE_ID)) {
					resetFletching(client);
					return;
				}
				if (!animating) {
					animating = true;
					client.startAnimation(FletchingConstants.CUTTING_ANIMATION);
					container.setTick(3);
					return;
				}
				int logsSlot = client.getItems().getItemSlot(logsId);
				client.getAchievements().onCollectItem(bowId, 1);
				client.playerItems[logsSlot] = bowId + 1;
				client.getItems().resetInventory();
				client.getPA().addSkillXP(experience * 25, client.playerFletching);
				client.startAnimation(65535);
				container.stop();
			}
		}, delay);
	}

	private static boolean stringBows(Client client, int itemSlotA, int itemSlotB) {
		int itemIdA = client.playerItems[itemSlotA] - 1;
		int itemIdB = client.playerItems[itemSlotB] - 1;

		if (!Misc.contains(FletchingConstants.BOWSTRING_ID, itemIdA, itemIdB)) {
			return false;
		}
		
		int unstrungId = Misc.getExcept(FletchingConstants.BOWSTRING_ID, itemIdA, itemIdB);
		
		StringDefinitions def = StringDefinitions.getDefinition(unstrungId);
		
		if (def == null) {
			return false;
		}
		
		final int amount = Math.min(client.getItems().getItemCount(itemIdA), client.getItems().getItemCount(itemIdB));
		
		if (client.playerLevel[client.playerFletching] < def.getLevelRequired()) {
			client.sendMessage("You need level " + def.getLevelRequired() + " Fletching to string this bow.");
			return true;
		}
		
		startFletching(client);
		for (int i = 0; i < amount; i++) {
			stringBow(client, def.getUnstrungId(), def.getStrungId(), def.getExperience(), def.getAnimationId(), i * 3);
		}
		
		return true;
	}
	
	private static void stringBow(Client client, int unstrungId, int strungId, int experience, int animationId, int delay) {
		CycleEventHandler.getSingleton().addEvent(CycleEventHandler.FLETCHING_ID, client, new CycleEvent() {
			boolean animating = false;
			@Override
			public void execute(CycleEventContainer container) {
				if (!client.skillActive()[client.playerFletching] || !client.getItems().playerHasItem(unstrungId) || !client.getItems().playerHasItem(FletchingConstants.BOWSTRING_ID)) {
					resetFletching(client);
					return;
				}
				if (!animating) {
					animating = true;
					client.startAnimation(animationId);
					container.setTick(1);
					return;
				}
				int unstrungSlot = client.getItems().getItemSlot(unstrungId);
				client.getAchievements().onCollectItem(strungId, 1);
				client.playerItems[unstrungSlot] = strungId + 1;
				client.getItems().deleteItem(FletchingConstants.BOWSTRING_ID, 1);
				client.getPA().addSkillXP(experience * 25, client.playerFletching);
				container.stop();
			}
		}, delay);
	}
	
	public static void startFletching(Client client) {
		resetFletching(client);
		client.skillActive()[client.playerFletching] = true;
	}
	
	public static void resetFletching(Client client) {
		client.skillActive()[client.playerFletching] = false;
		CycleEventHandler.getSingleton().stopEvents(client,
				CycleEventHandler.FLETCHING_ID);
	}

}