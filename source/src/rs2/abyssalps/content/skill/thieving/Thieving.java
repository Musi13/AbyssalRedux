package rs2.abyssalps.content.skill.thieving;

import java.util.HashMap;

import rs2.abyssalps.content.skill.magic.TeleportType;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class Thieving {

	private static enum StallData {

		GEM_STALL(6162, 1, 500, new int[] { 3084, 3488 }, new int[] { 1623,
				1621, 1619, 1617, 995 }, new int[] { 1, 1, 1, 1, 1500 }),

		BAKERY_STALL(6163, 15, 1000, new int[] { 3084, 3490 }, new int[] {
				1891, 1897, 995 }, new int[] { 1, 1, 3000 }),

		SILVER_STALL(6164, 35, 2000, new int[] { 3084, 3492 }, new int[] {
				1796, 442, 995 }, new int[] { 1, 1, 6000 }),

		CLOTHES_STALL(6165, 50, 4000, new int[] { 3084, 3494 }, new int[] {
				950, 995 }, new int[] { 1, 7500 }),

		CRAFTING_STALL(6166, 65, 8000, new int[] { 3084, 3496 }, new int[] {
				1592, 1597, 995 }, new int[] { 1, 1, 9000 });

		StallData(int id, int lvl, int xp, int[] location, int[] rewardItems,
				int[] rewardAmounts) {
			this.id = id;
			this.level = lvl;
			this.experience = xp;
			this.location = location;
			this.rewardItems = rewardItems;
			this.rewardAmounts = rewardAmounts;
		}

		private int id;
		private int level;
		private int experience;
		private int[] location;
		private int[] rewardItems;
		private int[] rewardAmounts;

		public int getLevel() {
			return this.level;
		}

		public int getExperience() {
			return this.experience;
		}

		public int[] getLocation() {
			return this.location;
		}

		public int[] getRewardItems() {
			return this.rewardItems;
		}

		public int[] getRewardAmounts() {
			return this.rewardAmounts;
		}

		public static StallData forId(int id) {
			return stallMap.get(id);
		}

		private static HashMap<Integer, StallData> stallMap = new HashMap<Integer, StallData>();

		static {
			for (StallData s : StallData.values()) {
				stallMap.put(s.id, s);
			}
		}
	}

	public static void useStall(Client client, int[] objectData) {
		if (System.currentTimeMillis() - client.lastThieve < 1800) {
			return;
		}
		StallData stall = StallData.forId(objectData[0]);
		if (stall == null) {
			client.sendMessage("This object is not available for usage.");
			client.sendMessage("Contact a developer, if you feel that this is a mistake.");
			return;
		}
		if (objectData[1] != stall.getLocation()[0]
				|| objectData[2] != stall.getLocation()[1]) {
			return;
		}

		if (client.playerLevel[client.playerThieving] < stall.getLevel()) {
			client.sendMessage("You need a thieving level of "
					+ stall.getLevel() + " to do this.");
			return;
		}

		client.turnPlayerTo(objectData[1], objectData[2]);

		client.startAnimation(833);

		int index = Misc.random(stall.getRewardItems().length - 1);

		int item = stall.getRewardItems()[index];

		int amount = stall.getRewardAmounts()[index];

		client.sendMessage("You manage to steal @or2@" + amount + " x "
				+ ItemDefinitions.forId(item).getName() + "@bla@.");

		client.getItems().addItem(item, amount);
		client.getAchievements().onCollectItem(item, amount);

		client.getPA().addSkillXP(stall.getExperience(), client.playerThieving);

		client.getPA().refreshSkill(client.playerThieving);

		client.lastThieve = System.currentTimeMillis();
	}
}
