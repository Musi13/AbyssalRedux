package rs2.abyssalps.content;

import java.util.HashMap;

import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.player.Client;

public class MaxCape {

	public static final int MAX_CAPE_ID = 13280;
	public static final int MAX_HOOD_ID = 13281;
	public static final int FIRE_MAX_CAPE_ID = 13329;
	public static final int FIRE_MAX_HOOD_ID = 13330;
	public static final int AVAS_MAX_CAPE_ID = 13337;
	public static final int AVAS_MAX_HOOD_ID = 13338;
	public static final int SARADOMIN_MAX_CAPE_ID = 13331;
	public static final int SARADOMIN_MAX_HOOD_ID = 13332;
	public static final int GUTHIX_MAX_CAPE_ID = 13335;
	public static final int GUTHIX_MAX_HOOD_ID = 13336;
	public static final int ZAMORAK_MAX_CAPE_ID = 13333;
	public static final int ZAMORAK_MAX_HOOD_ID = 13334;

	public static boolean maxItems(int itemId) {
		switch (itemId) {

		case MAX_CAPE_ID:
		case MAX_HOOD_ID:
		case FIRE_MAX_CAPE_ID:
		case FIRE_MAX_HOOD_ID:
		case AVAS_MAX_CAPE_ID:
		case AVAS_MAX_HOOD_ID:
		case SARADOMIN_MAX_CAPE_ID:
		case SARADOMIN_MAX_HOOD_ID:
		case GUTHIX_MAX_CAPE_ID:
		case GUTHIX_MAX_HOOD_ID:
		case ZAMORAK_MAX_CAPE_ID:
		case ZAMORAK_MAX_HOOD_ID:
			return true;

		default:
			return false;
		}
	}

	private static enum CombineTable {

		MAX_FIRE_CAPE(new GameItem[] { new GameItem(MAX_CAPE_ID, 1),
				new GameItem(MAX_HOOD_ID, 1), new GameItem(6570, 1) },
				new GameItem[] { new GameItem(FIRE_MAX_CAPE_ID, 1),
						new GameItem(FIRE_MAX_HOOD_ID, 1) }),

		AVAS_MAX_CAPE(new GameItem[] { new GameItem(MAX_CAPE_ID, 1),
				new GameItem(MAX_HOOD_ID, 1), new GameItem(10499, 1) },
				new GameItem[] { new GameItem(AVAS_MAX_CAPE_ID, 1),
						new GameItem(AVAS_MAX_HOOD_ID, 1) }),

		SARADOMIN_MAX_CAPE(new GameItem[] { new GameItem(MAX_CAPE_ID, 1),
				new GameItem(MAX_HOOD_ID, 1), new GameItem(2412, 1) },
				new GameItem[] { new GameItem(SARADOMIN_MAX_CAPE_ID, 1),
						new GameItem(SARADOMIN_MAX_HOOD_ID, 1) }),

		GUTHIX_MAX_CAPE(new GameItem[] { new GameItem(MAX_CAPE_ID, 1),
				new GameItem(MAX_HOOD_ID, 1), new GameItem(2413, 1) },
				new GameItem[] { new GameItem(GUTHIX_MAX_CAPE_ID, 1),
						new GameItem(GUTHIX_MAX_HOOD_ID, 1) }),

		ZAMORAK_MAX_CAPE(new GameItem[] { new GameItem(MAX_CAPE_ID, 1),
				new GameItem(MAX_HOOD_ID, 1), new GameItem(2414, 1) },
				new GameItem[] { new GameItem(ZAMORAK_MAX_CAPE_ID, 1),
						new GameItem(ZAMORAK_MAX_HOOD_ID, 1) });

		private CombineTable(GameItem[] requiredItems, GameItem[] combinedItems) {
			this.requiredItems = requiredItems;
			this.combinedItems = combinedItems;
		}

		private GameItem[] requiredItems;
		private GameItem[] combinedItems;

		private static HashMap<Integer, CombineTable> combineTable = new HashMap<Integer, CombineTable>();

		public static CombineTable forId(int id) {
			return combineTable.get(id);
		}

		public GameItem[] getRequiredItems() {
			return requiredItems;
		}

		public GameItem[] getCombinedItems() {
			return combinedItems;
		}

		static {
			for (CombineTable table : CombineTable.values()) {
				for (int index = 0; index < table.requiredItems.length; index++) {
					combineTable.put(table.requiredItems[index].id, table);
				}
			}
		}
	}

	public static void combineCapes(Client player, int itemUsed, int usedWith) {
		CombineTable table = CombineTable.forId(itemUsed);

		if (table == null) {
			player.sendMessage("Nothing interesting happens.");
			return;
		}

		if (!player.getItems().playerHasItem(table.getRequiredItems()[0].id, 1)
				|| !player.getItems().playerHasItem(
						table.getRequiredItems()[1].id, 1)
				|| !player.getItems().playerHasItem(
						table.getRequiredItems()[2].id, 1)) {
			player.sendMessage("You don't have all the required items to upgrade this cape.");
			return;
		}

		for (int index = 0; index < table.getRequiredItems().length; index++) {
			player.getItems().deleteItem(table.getRequiredItems()[index].id, 1);
			if (index < 2) {
				player.getItems()
						.addItem(table.getCombinedItems()[index].id, 1);
			}
		}
		player.sendMessage("You successfully combine the items and upgrade your cape.");
	}

	public static final int REQUIRED_MAX_SKILLS = 19;
	public static final int REQUIRED_GP = 50000000;

	private static int totalMaxSkills(Client player) {
		int total = 0;
		for (int index = 0; index < player.playerLevel.length; index++) {
			if (player.getLevelForXP(player.playerXP[index]) >= 99) {
				total++;
			}
		}
		return total;
	}

	public static boolean isMax(Client player) {
		if (totalMaxSkills(player) >= REQUIRED_MAX_SKILLS) {
			return true;
		}
		return false;
	}

	public static void buyMaxCape(Client player) {
		if (!isMax(player)) {
			player.getDH().sendDialogues(31, -1);
			return;
		}
		if (player.getItems().getTotalCount(13280) > 0) {
			player.getPA().removeAllWindows();
			player.sendMessage("@or2@You already have a max cape.");
			return;
		}
		if (!player.getItems().playerHasItem(995, REQUIRED_GP)) {
			player.getDH().sendDialogues(32, -1);
			return;
		}
		if (player.getItems().freeSlots() < 2) {
			player.getPA().removeAllWindows();
			player.sendMessage("@or2@You need 2 available slots in your inventory to do this.");
			return;
		}

		player.getDH().sendDialogues(33, -1);
	}
}
