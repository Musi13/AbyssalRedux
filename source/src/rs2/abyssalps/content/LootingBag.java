package rs2.abyssalps.content;

import java.text.DecimalFormat;

import rs2.Config;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerSave;
import rs2.world.ItemHandler;

public class LootingBag {

	public static DecimalFormat format = new DecimalFormat("#,###.##");

	public static void withdraw(Client player) {
		player.setSidebarInterface(3, 3213);
		for (int index = 0; index < player.getLootItems().length; index++) {
			if (player.getLootItems()[index] > 0
					&& player.getLootItemAmount()[index] > 0) {
				player.getItems().sendItemToAnyTab(
						player.getLootItems()[index],
						player.getLootItemAmount()[index]);
				player.getLootItems()[index] = -1;
				player.getLootItemAmount()[index] = -1;
			}
		}
		player.getItems().resetBank();
		player.getItems().resetTempItems();
		PlayerSave.saveGame(player);
	}

	public static void check(Client player) {
		player.setSidebarInterface(3, 26700);
		int totalWorth = 0;
		for (int index = 0; index < player.getLootItems().length; index++) {
			player.getPA().sendFrame34a(26706, -1, index, 0);
			if (player.getLootItems()[index] > 0) {
				player.getPA().sendFrame34a(26706,
						player.getLootItems()[index], index,
						player.getLootItemAmount()[index]);
				totalWorth += ItemHandler.getItemPrice()[player.getLootItems()[index]];
			}
		}
		player.getPA().sendFrame126(
				"Value: " + format.format(totalWorth) + " coins", 26707);
		player.lootBag = true;
	}

	public static void close(Client player) {
		player.setSidebarInterface(3, 3213);
		player.lootBag = false;
	}

	public static int freeSlots(Client player) {
		int freeSlots = 0;
		for (int index = 0; index < player.getLootItems().length; index++) {
			if (player.getLootItems()[index] <= 0) {
				freeSlots++;
			}
		}
		return freeSlots;
	}

	public static void deposit(Client player, int itemId, int slot) {
		if (!player.inWild() && player.getRank() < 8) {
			player.sendMessage("You can only deposit items in the wilderness.");
			return;
		}
		if ((freeSlots(player) >= 1 || player.getItems().playerHasItem(itemId,
				1)
				&& ItemDefinitions.forId(itemId).isStackable())
				|| freeSlots(player) > 0
				&& !ItemDefinitions.forId(itemId).isStackable()) {
			for (int index = 0; index < player.getLootItems().length; index++) {
				if (player.getLootItems()[index] == itemId
						&& ItemDefinitions.forId(itemId).isStackable()
						&& (player.getLootItems()[index] > 0)) {
					player.getLootItems()[index] = itemId;
					player.getItems().deleteItem(itemId, slot,
							player.playerItemsN[slot]);
					if (player.playerItemsN[slot] < Integer.MAX_VALUE) {
						player.getLootItemAmount()[index] = player.playerItemsN[slot];
					} else {
						player.getLootItemAmount()[index] = Integer.MAX_VALUE;
					}
				}
			}
			for (int index = 0; index < player.getLootItems().length; index++) {
				if (player.getLootItems()[index] <= 0) {
					player.getLootItems()[index] = itemId;
					if ((player.playerItemsN[slot] < Config.MAXITEM_AMOUNT)
							&& (player.playerItemsN[slot] > -1)) {
						player.getLootItemAmount()[index] = 1;
						player.getItems().deleteItem(itemId, slot, 1);
						if (player.playerItemsN[slot] > 1) {
							deposit(player, itemId,
									player.playerItemsN[slot] - 1);
							player.getItems().deleteItem(itemId, slot,
									player.playerItemsN[slot]);
							continue;
						}
					} else {
						player.getLootItemAmount()[index] = Config.MAXITEM_AMOUNT;
					}
					index = 30;
					continue;
				}
			}
		}
	}

}
