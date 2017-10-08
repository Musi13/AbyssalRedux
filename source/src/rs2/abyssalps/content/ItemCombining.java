package rs2.abyssalps.content;

import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;

public class ItemCombining {

	private static final int[][] COMBINE_DATA = { { 11818, 11820, 11794 },
			{ 11818, 11822, 11796 }, { 11820, 11822, 11800 },
			{ 11818, 11800, 11798 }, { 11822, 11794, 11798 },
			{ 11820, 11796, 11798 }, { 11810, 11798, 11802 },
			{ 11812, 11798, 11804 }, { 11814, 11798, 11806 },
			{ 11816, 11798, 11808 }, { 12004, 4151, 12006 },
			{ 1540, 11286, 11283 }, { 12833, 12829, 12831 },
			{ 12831, 12819, 12817 }, { 12831, 12823, 12821 },
			{ 12831, 12827, 12825 }, { 13227, 6920, 13235 },
			{ 13229, 2577, 13237 }, { 13231, 11840, 13239 },
			{ 12771, 4151, 12773 }, { 12769, 4151, 12774 },
			{ 13200, 12931, 13197 }, { 13201, 12931, 13199 },

	};

	private static final int[][] COMBINE_DATA_2 = {

	{ 11933, 11932, 11931, 11924 },

	{ 11928, 11929, 11930, 11926 },

	};

	public static void combineItems_2(Client client, int itemUsed, int useWith) {
		for (int i = 0; i < COMBINE_DATA_2.length; i++) {
			if (useWith == COMBINE_DATA_2[i][0]
					&& itemUsed == COMBINE_DATA_2[i][1]
					|| useWith == COMBINE_DATA_2[i][0]
					&& itemUsed == COMBINE_DATA_2[i][2]
					|| useWith == COMBINE_DATA_2[i][1]
					&& itemUsed == COMBINE_DATA_2[i][2]
					|| useWith == COMBINE_DATA_2[i][1]
					&& itemUsed == COMBINE_DATA_2[i][0]
					|| useWith == COMBINE_DATA_2[i][2]
					&& itemUsed == COMBINE_DATA_2[i][1]
					|| useWith == COMBINE_DATA_2[i][2]
					&& itemUsed == COMBINE_DATA_2[i][0]) {
				if (!client.getItems().playerHasItem(COMBINE_DATA_2[i][0])
						|| !client.getItems().playerHasItem(
								COMBINE_DATA_2[i][1])
						|| !client.getItems().playerHasItem(
								COMBINE_DATA_2[i][2])) {
					client.sendMessage("You don't have all the needed parts to do this.");
					return;
				}

				client.getItems().deleteItem(COMBINE_DATA_2[i][0],
						client.getItems().getItemSlot(COMBINE_DATA_2[i][0]), 1);
				client.getItems().deleteItem(COMBINE_DATA_2[i][1],
						client.getItems().getItemSlot(COMBINE_DATA_2[i][1]), 1);
				client.getItems().deleteItem(COMBINE_DATA_2[i][2],
						client.getItems().getItemSlot(COMBINE_DATA_2[i][2]), 1);

				client.getItems().addItem(COMBINE_DATA_2[i][3], 1);

				client.sendMessage("You create a "
						+ ItemDefinitions.forId(COMBINE_DATA_2[i][2]).getName()
						+ ".");
				return;
			}
		}
	}

	public static void combineItems(Client client, int itemUsed, int useWith) {
		for (int i = 0; i < COMBINE_DATA.length; i++) {
			if (itemUsed == COMBINE_DATA[i][0] && useWith == COMBINE_DATA[i][1]
					|| useWith == COMBINE_DATA[i][0]
					&& itemUsed == COMBINE_DATA[i][1]) {
				if (!client.getItems().playerHasItem(COMBINE_DATA[i][0], 1)
						|| !client.getItems().playerHasItem(COMBINE_DATA[i][1],
								1)) {
					return;
				}
				int item0 = COMBINE_DATA[i][0];
				int item1 = COMBINE_DATA[i][1];
				client.getItems().deleteItem(item0,
						client.getItems().getItemSlot(item0), 1);
				client.getItems().deleteItem(item1,
						client.getItems().getItemSlot(item1), 1);
				int item2 = COMBINE_DATA[i][2];
				client.getItems().addItem(item2, 1);
				client.getDH().sendItemChat2(
						ItemDefinitions.forId(item2).getName(),
						"You combine a @blu@"
								+ ItemDefinitions.forId(item0).getName()
								+ " @bla@with a @blu@"
								+ ItemDefinitions.forId(item1).getName()
								+ "@bla@,",
						"and form a @blu@"
								+ ItemDefinitions.forId(item2).getName()
								+ "@bla@.", item2, 250);
				client.nextChat = 0;
				return;
			}
		}
	}

}
