package rs2.abyssalps.content;

import java.util.HashMap;

import rs2.abyssalps.content.consumables.Potions.PotionData;
import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;

public class PotionDecanting {

	public static enum DecantingTable {

		SUPER_STRENGTH_POTION(new int[] { 161, 159, 157, 2440 });

		DecantingTable(int[] potions) {
			this.potions = potions;
		}

		private int[] potions;

		public int[] getPotions() {
			return potions;
		}

		private static HashMap<Integer, DecantingTable> table = new HashMap<Integer, DecantingTable>();

		public static DecantingTable forId(int id) {
			return table.get(id);
		}

		public static boolean potionExist(int id) {
			return table.containsKey(id);
		}

		static {
			for (DecantingTable d : DecantingTable.values()) {
				for (int index = 0; index < d.potions.length; index++) {
					table.put(d.getPotions()[index], d);
				}
			}
		}
	}

	public static void decantPotion(Client player, GameItem usedItem, int slot,
			GameItem withItem, int slot1) {
		PotionData drink1 = PotionData.forId(usedItem.id);
		PotionData drink2 = PotionData.forId(withItem.id);
		if (drink1 != null && drink2 != null) {
			if (drink1 != drink2) {
				player.sendMessage("You can't combine these two potions.");
				return;
			}
			int index1 = -1;
			int index2 = -1;
			for (int i = 0; i < drink1.getPotionIds().length; i++) {
				if (drink1.getPotionIds()[i] == usedItem.id) {
					index1 = i + 1;
					break;
				}
			}
			for (int i = 0; i < drink2.getPotionIds().length; i++) {
				if (drink2.getPotionIds()[i] == withItem.id) {
					index2 = i + 1;
					break;
				}
			}
			int doses = index1 + index2;
			int amount = 0;
			GameItem endPotion1 = null;
			GameItem endPotion2 = null;
			if (doses < 5) {
				endPotion1 = new GameItem(drink1.getPotionIds()[doses - 1], 1);
				endPotion2 = new GameItem(229, 1);
				amount = doses;
			} else {
				endPotion1 = new GameItem(drink1.getPotionIds()[3], 1);
				amount = 4;
				doses -= 4;
				endPotion2 = new GameItem(drink1.getPotionIds()[doses - 1], 1);
			}
			player.getItems().deleteItem(usedItem.id, 1);
			player.getItems().deleteItem(withItem.id, 1);
			player.getItems().addItem(endPotion1.id, endPotion1.amount);
			player.getItems().addItem(endPotion2.id, endPotion2.amount);
			player.sendMessage("You have combined the liquid into " + amount
					+ " doses.");
			return;
		}
	}
}
