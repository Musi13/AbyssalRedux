package rs2.abyssalps.content.skill.fishing;

import java.util.HashMap;

import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;
import rs2.world.ItemHandler;

public class Fishing {

	private static enum FishData {

		SHRIMPS(1506, 1, 10, 317, 303, 621),

		TROUT(1507, 20, 50, 335, 309, 2575),

		TUNA(1508, 35, 80, 359, 311, 618),

		SWORDFISH(1509, 50, 100, 371, 311, 618),

		KARAMBWAN(1510, 65, 105, 3142, 311, 618),

		SHARK(1511, 76, 110, 383, 311, 618),

		ANGLERFISH(1512, 82, 120, 13439, 307, 622);

		FishData(int id, int lvl, int xp, int fish, int itemId, int emote) {
			this.id = id;
			this.level = lvl;
			this.experience = xp;
			this.fishId = fish;
			this.itemId = itemId;
			this.emote = emote;
		}

		private int id;
		private int level;
		private int experience;
		private int fishId;
		private int itemId;
		private int emote;

		public int getLevel() {
			return level;
		}

		public int getExperience() {
			return experience;
		}

		public int getFishId() {
			return fishId;
		}

		public int getItemId() {
			return itemId;
		}

		public int getEmote() {
			return emote;
		}

		private static HashMap<Integer, FishData> fishMap = new HashMap<Integer, FishData>();

		public static FishData forId(int id) {
			return fishMap.get(id);
		}

		static {
			for (FishData f : FishData.values()) {
				fishMap.put(f.id, f);
			}
		}
	}

	public static boolean fishExist(int id) {
		return FishData.fishMap.containsKey(id);
	}

	public static void startFishing(Client player, int npcId) {
		FishData fish = FishData.forId(npcId);

		if (fish == null) {
			return;
		}

		if (player.playerLevel[player.playerFishing] < fish.getLevel()) {
			player.sendMessage("I need " + fish.getLevel() + " to do this.");
			return;
		}

		if (!player.getItems().playerHasItem(fish.getItemId())) {
			player.sendMessage("I need a "
					+ ItemDefinitions.forId(fish.getItemId()).getName()
					+ " to do this.");
			return;
		}

		if (npcId == 1496) {
			if (!player.getItems().playerHasItem(314)) {
				player.sendMessage("I need "
						+ ItemDefinitions.forId(314).getName() + " to do this.");
				return;
			}
		}

		if (npcId == 1512) {
			if (!player.getItems().playerHasItem(13431)) {
				player.sendMessage("I need "
						+ ItemDefinitions.forId(13431).getName()
						+ " to do this.");
				return;
			}
		}

		if (player.getItems().freeSlots() <= 0) {
			player.sendMessage("I can't carry anymore fish.");
			return;
		}

		player.turnPlayerTo(NPCHandler.npcs[player.npcClickIndex].getX(),
				NPCHandler.npcs[player.npcClickIndex].getY());

		player.skillActive()[player.playerFishing] = true;

		player.startAnimation(fish.getEmote());

		player.sendMessage("You attempt to catch a fish...");

		CycleEventHandler.getSingleton().stopEvents(player,
				CycleEventHandler.FISHING_ID);

		CycleEventHandler.getSingleton().addEvent(CycleEventHandler.FISHING_ID,
				player, new CycleEvent() {

					@Override
					public void execute(CycleEventContainer container) {
						if (player == null || container.getOwner() == null) {
							container.stop();
							return;
						}
						if (!player.skillActive()[player.playerFishing]) {
							container.stop();
							return;
						}

						if (player.getItems().freeSlots() <= 0) {
							player.sendMessage("I can't carry anymore fish.");
							container.stop();
							return;
						}

						if (npcId == 1496) {
							if (!player.getItems().playerHasItem(314)) {
								player.sendMessage("I need "
										+ ItemDefinitions.forId(314).getName()
										+ " to do this.");
								container.stop();
								return;
							}
							player.getItems().deleteItem(314,
									player.getItems().getItemSlot(314), 1);
						}

						if (npcId == 1512) {
							if (!player.getItems().playerHasItem(13431)) {
								player.sendMessage("I need "
										+ ItemDefinitions.forId(13431)
												.getName() + " to do this.");
								container.stop();
								return;
							}
							player.getItems().deleteItem(13431,
									player.getItems().getItemSlot(13431), 1);
						}

						boolean successful = Misc
								.random(player.playerLevel[player.playerFishing] + 10) > Misc
								.random(fish.getLevel() + 10);

						player.startAnimation(fish.getEmote());

						if (successful) {

							player.sendMessage("You manage to catch some "
									+ ItemDefinitions.forId(fish.getFishId())
											.getName() + ".");

							player.getItems().addItem(fish.getFishId(), 1);

							player.getAchievements().onCollectItem(fish.getFishId(), 1);
							
							player.getPA().addSkillXP(
									fish.getExperience() * 25,
									player.playerFishing);

							player.getPA().refreshSkill(player.playerFishing);

							if (Misc.random(4) >= 3
									&& player.getItems().freeSlots() > 0) {
								player.getItems().addItem(
										995,
										ItemHandler.getItemPrice()[fish
												.getFishId()]);
								player.sendMessage("You manage to profit @blu@"
										+ ItemHandler.getItemPrice()[fish
												.getFishId()]
										+ "@bla@ coins, from the fish spot.");
							}
						}
					}

				}, 4);
	}

	public static void resetFishing(Client player) {
		player.startAnimation(65535);
		player.skillActive()[player.playerFishing] = false;
	}
}
