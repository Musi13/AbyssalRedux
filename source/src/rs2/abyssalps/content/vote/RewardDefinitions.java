package rs2.abyssalps.content.vote;

import rs2.abyssalps.model.items.GameItem;

public class RewardDefinitions {

	RewardDefinitions(int itemId, int itemAmount, int rarity, int random) {
		this.item = new GameItem(itemId, itemAmount);
		this.rarity = rarity;
		this.random = random;
	}

	private int id;
	private GameItem item;
	private int rarity;
	private int random;

	public GameItem getItem() {
		return this.item;
	}

	public int getRarity() {
		return this.rarity;
	}

	public int getRandom() {
		return this.random;
	}

}
