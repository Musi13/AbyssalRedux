package rs2.abyssalps.content.vote;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

public class RewardParser {

	private static final Random random = new Random();
	private static final Comparator<RewardDefinitions> randomDropComparator = new RewardComparator();
	private static final Map<Integer, List<RewardDefinitions>> drops = new HashMap<>();

	public static void parse() {
		int boxId = 0;
		int itemId = 0;
		int itemAmount = 0;
		int rarity = 0;
		int random = 0;
		try {
			drops.clear();
			JsonArray npcArray = Json.parse(
					new FileReader("./data/cfg/mysterybox.json")).asArray();
			for (JsonValue npc : npcArray) {
				JsonObject npcObject = npc.asObject();
				boxId = npcObject.get("boxId").asInt();
				JsonArray boxDropArray = npcObject.get("rewardTable").asArray();
				List<RewardDefinitions> boxDrops = new ArrayList<>();
				for (JsonValue drop : boxDropArray) {
					JsonObject dropObject = drop.asObject();
					itemId = dropObject.get("itemId").asInt();
					itemAmount = dropObject.get("itemAmount").asInt();
					rarity = dropObject.get("rarity").asInt();
					random = dropObject.get("random").asInt();
					boxDrops.add(new RewardDefinitions(itemId, itemAmount,
							rarity, random));
				}
				drops.put(boxId, boxDrops);
			}
		} catch (Exception e) {
			System.out.println("Failed to process box " + boxId + ", " + itemId
					+ ", " + itemAmount + ", " + rarity + ", " + random + ".");
			e.printStackTrace();
		}
	}

	public static void open(Client player, int id, int itemSlot) {
		if (!player.getItems().playerHasItem(id, 1, itemSlot)) {
			return;
		}
		int itemId = 0;
		int amount = 0;
		player.sendMessage("You open the Mystery Box...");
		for (GameItem item : getRandomDrop(id)) {
			itemId = item.id;
			amount = item.amount;
		}
		player.getItems().deleteItem(id, itemSlot, 1);
		player.getItems().addItem(itemId, amount);
		String endMsg = amount > 1 ? "'s." : ".";
		if (id == 995) {
			player.sendMessage("You find @blu@x" + amount + " @red@"
					+ ItemDefinitions.forId(itemId).getName() + ".");
		} else {
			player.sendMessage("You find @blu@x" + amount + " @red@"
					+ ItemDefinitions.forId(itemId).getName() + "" + endMsg);
		}
	}

	public static List<GameItem> getRandomDrop(int id) {
		/*
		 * Current drops will contain items that will be dropped, rare drops are
		 * used to determine which rare item we drop.
		 */
		List<RewardDefinitions> rareDrops = new ArrayList<>();
		List<GameItem> itemsToDrop = new ArrayList<>();
		/*
		 * Return an empty list if this npc has no drop table.
		 */
		if (drops.get(id) == null) {
			return itemsToDrop;
		}
		/*
		 * Find highest rarity in the drop table, we will use that as a unit for
		 * random generator. While looking for highest rarity, add drops without
		 * rarity to the list.
		 */
		int highestRarity = 1;
		for (RewardDefinitions drop : drops.get(id)) {
			if (drop.getRarity() > 1) {
				rareDrops.add(drop);
			} else {
				itemsToDrop.add(new GameItem(drop.getItem().id,
						drop.getItem().amount + Misc.random(drop.getRandom())));
			}
			if (drop.getRarity() > highestRarity) {
				highestRarity = drop.getRarity();
			}
		}
		/*
		 * Sort the rare drop list in descending order, scramble drops with the
		 * same rarity.
		 */
		try {
			Collections.sort(rareDrops, randomDropComparator);
		} catch (IllegalArgumentException e) {

		}
		/*
		 * Roll a random number and drop item only if the random number is in
		 * range of drop rarity proportion to highest rarity.
		 */
		boolean match = false;
		int seed = random.nextInt(highestRarity);
		for (RewardDefinitions drop : rareDrops) {
			int proportion = (int) (highestRarity * (1D / drop.getRarity()));
			if (seed < proportion) {
				itemsToDrop.add(new GameItem(drop.getItem().id,
						drop.getItem().amount + Misc.random(drop.getRandom())));
				match = true;
				break;
			}
		}
		/*
		 * If no drops are selected, select the one with lowest rarity.
		 */
		if (!match) {
			RewardDefinitions selectedDrop = null;
			int rarity = Integer.MAX_VALUE;
			for (RewardDefinitions drop : rareDrops) {
				if (drop.getRarity() < rarity) {
					selectedDrop = drop;
					rarity = drop.getRarity();
				}
			}
			if (selectedDrop != null) {
				itemsToDrop.add(selectedDrop.getItem());
			}
		}
		/*
		 * Return a list of items to drops.
		 */
		return itemsToDrop;
	}
}
