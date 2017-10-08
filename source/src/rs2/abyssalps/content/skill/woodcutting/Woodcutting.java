package rs2.abyssalps.content.skill.woodcutting;

import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.objects.Object;
import rs2.util.Misc;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;
import rs2.world.ItemHandler;

public class Woodcutting {

	public static void chopVine(Client player, int face, int moveX, int moveY) {

		if (player.playerLevel[player.playerWoodcutting] < 10) {
			player.sendMessage("I need a woodcutting level of 10 to do this.");
			return;
		}

		player.turnPlayerTo(player.objectX, player.objectY);

		HatchetData hatchet = HatchetData.forId(HatchetData
				.getPlayerHatchetId(player));

		if (hatchet == null) {
			player.sendMessage("You need an axe to chop this vine.");
			return;
		}

		player.skillActive()[player.playerWoodcutting] = true;

		player.sendMessage("You swing your axe at the vine.");

		player.startAnimation(hatchet.getEmote());

		player.setRun(false);

		player.setCanWalk(false);

		CycleEventHandler.getSingleton().stopEvents(player,
				CycleEventHandler.WOODCUTTING_ID);

		CycleEventHandler.getSingleton().addEvent(
				CycleEventHandler.WOODCUTTING_ID, player, new CycleEvent() {
					final int objectX = player.objectX;
					final int objectY = player.objectY;

					@Override
					public void execute(CycleEventContainer container) {
						if (player == null || container.getOwner() == null) {
							container.stop();
							return;
						}
						if (!player.skillActive()[player.playerWoodcutting]) {
							container.stop();
							return;
						}
						new Object(-1, objectX, objectY, player.heightLevel,
								face, 10, player.objectId, 2);

						player.getPA().walkTo(moveX, moveY);

						player.setRun(true);

						player.setCanWalk(true);

						container.stop();
					}
				}, 4);
	}

	public static void chop(Client player, int objectId, int objectX,
			int objectY) {

		player.turnPlayerTo(objectX, objectY);

		TreeData tree = TreeData.forId(objectId);

		if (tree == null) {
			player.sendMessage("This tree is not configurated.");
			return;
		}

		if (player.getItems().freeSlots() <= 0) {
			player.sendMessage("I can't carry anymore logs.");
			return;
		}

		HatchetData hatchet = HatchetData.forId(HatchetData
				.getPlayerHatchetId(player));

		if (hatchet == null) {
			player.sendMessage("You need an axe to chop this tree.");
			return;
		}

		if (player.playerLevel[player.playerWoodcutting] < tree.getLevel()) {
			player.sendMessage("You need a woodcutting level of "
					+ tree.getLevel() + " to chop this tree.");
			return;
		}

		player.skillActive()[player.playerWoodcutting] = true;

		player.sendMessage("You swing your axe at the tree.");

		player.startAnimation(hatchet.getEmote());

		CycleEventHandler.getSingleton().stopEvents(player,
				CycleEventHandler.WOODCUTTING_ID);

		CycleEventHandler.getSingleton().addEvent(
				CycleEventHandler.WOODCUTTING_ID, player, new CycleEvent() {

					@Override
					public void execute(CycleEventContainer container) {
						if (player == null || container.getOwner() == null) {
							container.stop();
							return;
						}
						if (!player.skillActive()[player.playerWoodcutting]) {
							container.stop();
							return;
						}
						if (player.getItems().freeSlots() <= 0) {
							player.sendMessage("I can't carry anymore logs.");
							container.stop();
							return;
						}
						boolean successful = Misc
								.random(player.playerLevel[player.playerWoodcutting] + 10) > Misc
								.random(tree.getLevel() + 10);

						player.startAnimation(hatchet.getEmote());

						if (successful) {

							player.sendMessage("You manage to chop some "
									+ ItemDefinitions.forId(tree.getLog())
											.getName() + ".");

							player.getItems().addItem(tree.getLog(), 1);
							player.getAchievements().onCollectItem(
									tree.getLog(), 1);

							player.getPA().addSkillXP(
									tree.getExperience() * 10,
									player.playerWoodcutting);

							player.getPA().refreshSkill(
									player.playerWoodcutting);

							if (Misc.random(4) >= 3
									&& player.getItems().freeSlots() > 0) {
								player.getItems().addItem(
										995,
										ItemHandler.getItemPrice()[tree
												.getLog()]);
								player.sendMessage("You manage to profit @blu@"
										+ ItemHandler.getItemPrice()[tree
												.getLog()]
										+ "@bla@ coins, from the tree.");
							}
						}

					}

				}, 4);
	}

	public static void resetWoodcutting(Client player) {
		player.startAnimation(65535);
		player.skillActive()[player.playerWoodcutting] = false;
		CycleEventHandler.getSingleton().stopEvents(player,
				CycleEventHandler.WOODCUTTING_ID);
	}
}
