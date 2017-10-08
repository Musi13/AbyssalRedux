package rs2.abyssalps.model.items;

import rs2.abyssalps.model.items.defs.ItemDefinitions;

public class GameItem {

	public int id, amount;
	public boolean stackable = false;

	public GameItem(int id, int amount) {
		if (ItemDefinitions.forId(id) != null
				&& ItemDefinitions.forId(id).isStackable()) {
			stackable = true;
		}
		this.id = id;
		this.amount = amount;
	}
}