package rs2.abyssalps.content.beta;

import java.util.ArrayList;
import java.util.Collection;

import rs2.Config;
import rs2.abyssalps.model.items.defs.BetaDefinitions;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;

public class SuppliesSearch {

	private Client player;

	public SuppliesSearch(Client player) {
		this.player = player;
	}

	private int[] searchResults = new int[BetaDefinitions.ALLOCATED_SIZE];

	public void resetInterface(String name) {
		name = name.toLowerCase();
		int lol = 0;
		for (int index = 0; index < BetaDefinitions.ALLOCATED_SIZE; index++) {
			if (BetaDefinitions.instance == null) {
				continue;
			}
			if (!BetaDefinitions.forId(index).getName().toLowerCase()
					.contains(name)) {
				continue;
			}
			if (BetaDefinitions.forId(index).getName().endsWith("100")
					|| BetaDefinitions.forId(index).getName().endsWith("75")
					|| BetaDefinitions.forId(index).getName().endsWith("50")
					|| BetaDefinitions.forId(index).getName().endsWith("25")) {
				continue;
			}
			searchResults[lol] = BetaDefinitions.forId(index).getId();
			lol++;
			if (lol >= 70)
				lol = 0;
		}
		for (int index = 0; index < 70; index++) {
			if (searchResults[index] < 1) {
				player.getPA().sendFrame34a(55121, -1, index, 0);
				continue;
			}
			player.getPA().sendFrame34a(55121, searchResults[index], index, 1);
			searchResults[index] = -1;
		}
	}

	public void get(int itemId, int amount) {
		if (amount <= 0) {
			return;
		}
		if (player.getItems().freeSlots() <= 0
				&& ItemDefinitions.forId(itemId).isStackable()
				&& !player.getItems().playerHasItem(itemId)
				|| player.getItems().freeSlots() <= 0
				&& !ItemDefinitions.forId(itemId).isStackable()) {
			player.sendMessage("I can't carry anymore items.");
			return;
		}
		
		if (!ItemDefinitions.forId(itemId).isStackable() && amount >= 2) {
			if (ItemDefinitions.forId(itemId + 1).isNoted()) {
				itemId++;
			}
		}
		
		if (amount >= player.getItems().freeSlots()
				&& !ItemDefinitions.forId(itemId).isStackable()) {
			amount = player.getItems().freeSlots();
		}

		player.getItems().addItem(itemId, amount);
		player.getItems().resetItems(3823);
	}
}
