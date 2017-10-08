package rs2.abyssalps.content.skill.cooking;

import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;
import rs2.world.ItemHandler;

public class Cooking {

	public static void showInterface(Client player, int itemId) {
		player.getPA().sendFrame164(1743);
		player.getPA().sendFrame246(13716, 250, itemId);
		player.getPA().sendFrame126(ItemDefinitions.forId(itemId).getName(), 13717);
		player.skillActive()[player.playerCooking] = true;
		player.setCookItem(itemId);
	}

	public static void cookItem(Client player, int itemId) {
		if (!player.getItems().playerHasItem(itemId, 1)) {
			return;
		}

		player.getPA().closeAllWindows();

		if (!player.skillActive()[player.playerCooking]) {
			return;
		}

		CookData cook = CookData.forId(itemId);

		if (cook == null) {
			return;
		}

		if (player.playerLevel[player.playerCooking] < cook.getLevel()) {
			player.sendMessage("I need " + cook.getLevel() + " cooking to do this.");
			return;
		}

		player.startAnimation(883);

		CycleEventHandler.getSingleton().stopEvents(player, CycleEventHandler.COOKING_ID);

		CycleEventHandler.getSingleton().addEvent(CycleEventHandler.COOKING_ID, player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {

				if (player == null || container.getOwner() == null) {
					container.stop();
					return;
				}
				if (!player.skillActive()[player.playerCooking]) {
					container.stop();
					return;
				}

				player.startAnimation(883);

				boolean success = Misc.random((int)(player.playerLevel[player.playerCooking] * 1.5)) > Misc.random(cook.getLevel());

				int rawSlot = player.getItems().getItemSlot(cook.getRawId());

				if (!success) {
					player.sendMessage("You accidentally burnt the " + ItemDefinitions.forId(cook.getRawId()).getName() + ".");
					player.playerItems[rawSlot] = cook.getBurntId() + 1;
				}
				if (success) {
					player.sendMessage("You successfully cook the " + ItemDefinitions.forId(cook.getRawId()).getName() + ".");
					player.getAchievements().onCollectItem(cook.getCookedId(), 1);
					player.playerItems[rawSlot] = cook.getCookedId() + 1;
					player.getPA().addSkillXP(cook.getExperience() * 25, player.playerCooking);
					player.getPA().refreshSkill(player.playerCooking);
					if (Misc.random(4) >= 3 && player.getItems().freeSlots() > 0) {
						player.getItems().addItem(995, ItemHandler.getItemPrice()[cook.getCookedId()]);
						player.sendMessage("You manage to profit @blu@" + ItemHandler.getItemPrice()[cook.getCookedId()] + "@bla@ coins, from the cook session.");
					}
				}
				
				player.getItems().resetInventory();

				player.setCookAmount(player.getCookAmount() - 1);

				if (player.getCookAmount() <= 0) {
					container.stop();
					return;
				}

				if (!player.getItems().playerHasItem(cook.getRawId(), 1)) {
					player.sendMessage("You have run out of " + ItemDefinitions.forId(cook.getRawId()).getName() + ".");
					container.stop();
					return;
				}
			}

		}, 3);
	}

	public static void resetCooking(Client player) {
		player.startAnimation(65535);
		player.skillActive()[player.playerCooking] = false;
		player.setCookAmount(0);
	}
}
