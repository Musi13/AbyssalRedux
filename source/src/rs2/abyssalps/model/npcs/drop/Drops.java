package rs2.abyssalps.model.npcs.drop;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import rs2.Server;
import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.Misc;

public class Drops {

	private static final Random random = new Random();
	private static final Comparator<Drop> randomDropComparator = new DropComparator();
	private static final Map<Integer, List<Drop>> drops = new HashMap<>();

	public static void parse() {
		int npcId = 0;
		int itemId = 0;
		int itemAmount = 0;
		int rarity = 0;
		int random = 0;
		try {
			drops.clear();
			JsonArray npcArray = Json.parse(
					new FileReader("./data/cfg/npcdrops.json")).asArray();
			for (JsonValue npc : npcArray) {
				JsonObject npcObject = npc.asObject();
				npcId = npcObject.get("npcId").asInt();
				JsonArray npcDropArray = npcObject.get("npcDrops").asArray();
				List<Drop> npcDrops = new ArrayList<>();
				for (JsonValue drop : npcDropArray) {
					JsonObject dropObject = drop.asObject();
					itemId = dropObject.get("itemId").asInt();
					itemAmount = dropObject.get("itemAmount").asInt();
					rarity = dropObject.get("rarity").asInt();
					random = dropObject.get("random").asInt();
					npcDrops.add(new Drop(itemId, itemAmount, rarity, random));
				}
				drops.put(npcId, npcDrops);
			}
		} catch (Exception e) {
			System.out.println("Failed to process npc " + npcId + ", " + itemId
					+ ", " + itemAmount + ", " + rarity + ", " + random + ".");
			e.printStackTrace();
		}
	}

	public static List<GameItem> getRandomDrop(Client client, int npcId) {
		/*
		 * Current drops will contain items that will be dropped, rare drops are
		 * used to determine which rare item we drop.
		 */
		List<Drop> rareDrops = new ArrayList<>();
		List<GameItem> itemsToDrop = new ArrayList<>();
		/*
		 * Return an empty list if this npc has no drop table.
		 */
		if (drops.get(npcId) == null) {
			System.out.println("Null");
			return itemsToDrop;
		}
		/*
		 * Find highest rarity in the drop table, we will use that as a unit for
		 * random generator. While looking for highest rarity, add drops without
		 * rarity to the list.
		 */
		int highestRarity = 1;
		for (Drop drop : drops.get(npcId)) {
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
		} catch (Exception e) {
		}

		/*
		 * Roll a random number and drop item only if the random number is in
		 * range of drop rarity proportion to highest rarity.
		 */
		boolean match = false;
		int seed = random.nextInt(highestRarity);
		for (Drop drop : rareDrops) {
			int proportion = (int) (highestRarity * (1D / drop.getRarity()));
			if (seed < proportion) {
				itemsToDrop.add(new GameItem(drop.getItem().id,
						drop.getItem().amount + Misc.random(drop.getRandom())));
				match = true;
				announceDrop(client, drop);
				break;
			}
		}
		/*
		 * If no drops are selected, select the one with lowest rarity.
		 */
		if (!match) {
			Drop selectedDrop = null;
			int rarity = Integer.MAX_VALUE;
			for (Drop drop : rareDrops) {
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

	public static void announceDrop(Client client, Drop drop) {
		if (drop.getRarity() < 200) {
			return;
		}

		ItemDefinitions def = ItemDefinitions.forId(drop.getItem().id);
		if (def == null) {
			return;
		}

		PlayerHandler.globalMessage("@dre@" + client.playerName
				+ " has received " + def.getName() + " drop!");
	}

}
