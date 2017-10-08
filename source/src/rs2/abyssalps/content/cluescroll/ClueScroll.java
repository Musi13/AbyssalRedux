package rs2.abyssalps.content.cluescroll;

import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class ClueScroll {

	public static void getInterface(Client player, int[] items, int[] amounts) {
		player.outStream.createFrameVarSizeWord(53);
		player.outStream.writeWord(6963);
		player.outStream.writeWord(items.length);
		for (int i = 0; i < items.length; i++) {
			if (player.playerItemsN[i] > 254) {
				player.outStream.writeByte(255);
				player.outStream.writeDWord_v2(amounts[i]);
			} else {
				player.outStream.writeByte(amounts[i]);
			}
			if (items[i] > 0) {
				player.outStream.writeWordBigEndianA(items[i] + 1);
			} else {
				player.outStream.writeWordBigEndianA(0);
			}
		}
		player.outStream.endFrameVarSizeWord();
		player.flushOutStream();
		for (int index = 0; index < items.length; index++) {
			if (items[index] > 0) {
				player.getItems().addItem(items[index], amounts[index]);
			}
		}
		player.getPA().showInterface(6960);
	}

	public static void open(Client player, int itemId, int itemSlot) {
		ClueScrollTable clue = ClueScrollTable.forId(itemId);
		if (clue == null) {
			return;
		}
		int amount = 1 + Misc.random(2);

		player.getItems().deleteItem(itemId, itemSlot, 1);

		int item1 = 0;

		int item2 = -1;

		int item3 = -1;

		int amounts1 = 0;

		int amounts2 = 0;

		int amounts3 = 0;

		item1 = clue.getItems()[(int) (Math.random() * clue.getItems().length)];
		amounts1 = ItemDefinitions.forId(item1).isStackable() ? Misc
				.random(200) : 1;

		if (amount > 1) {
			item2 = clue.getItems()[(int) (Math.random() * clue.getItems().length)];
			amounts2 = ItemDefinitions.forId(item2).isStackable() ? Misc
					.random(200) : 1;
		}

		if (amount > 2) {
			item3 = clue.getItems()[(int) (Math.random() * clue.getItems().length)];
			amounts3 = ItemDefinitions.forId(item3).isStackable() ? Misc
					.random(200) : 1;
		}

		getInterface(player, new int[] { item1, item2, item3 }, new int[] {
				amounts1, amounts2, amounts3 });
	}
}
