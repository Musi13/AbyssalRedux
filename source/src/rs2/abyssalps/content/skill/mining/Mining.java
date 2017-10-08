package rs2.abyssalps.content.skill.mining;

import rs2.abyssalps.content.skill.woodcutting.HatchetData;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;
import rs2.world.ItemHandler;

public class Mining {

	public static void mine(Client player, int objectId) {

		RockData rock = RockData.forId(objectId);

		if (rock == null) {
			return;
		}

		if (player.getItems().freeSlots() <= 0) {
			player.sendMessage("I can't carry anymore rocks.");
			return;
		}

		if (player.playerLevel[player.playerMining] < rock.getLevel()) {
			player.sendMessage("I need " + rock.getLevel()
					+ " mining to do this.");
			return;
		}

		PickData pick = PickData.forId(PickData.getPlayerHatchetId(player));

		if (pick == null) {
			player.sendMessage("You need a pickaxe to mine this rock.");
			return;
		}

		player.turnPlayerTo(player.objectX, player.objectY);

		player.skillActive()[player.playerMining] = true;

		player.sendMessage("You swing your pick axe the rock...");

		player.startAnimation(pick.getEmote());

		CycleEventHandler.getSingleton().stopEvents(player,
				CycleEventHandler.MINING_ID);

		CycleEventHandler.getSingleton().addEvent(CycleEventHandler.MINING_ID,
				player, new CycleEvent() {

					@Override
					public void execute(CycleEventContainer container) {

						if (player == null || container.getOwner() == null) {
							container.stop();
							return;
						}
						if (!player.skillActive()[player.playerMining]) {
							container.stop();
							return;
						}

						if (player.getItems().freeSlots() <= 0) {
							player.sendMessage("I can't carry anymore ores.");
							container.stop();
							return;
						}

						player.startAnimation(pick.getEmote());

						boolean success = Misc
								.random(player.playerLevel[player.playerMining] + 10) > Misc
								.random(rock.getLevel() + 10);

						if (success) {

							player.sendMessage("You manage to mine some "
									+ ItemDefinitions.forId(rock.getOreId())
											.getName() + ".");

							player.getItems().addItem(rock.getOreId(), 1);

							player.getAchievements().onCollectItem(
									rock.getOreId(), 1);

							player.getPA().addSkillXP(
									rock.getExperience() * 25,
									player.playerMining);

							player.getPA().refreshSkill(player.playerMining);

							if (Misc.random(4) >= 3
									&& player.getItems().freeSlots() > 0) {
								player.getItems().addItem(
										995,
										ItemHandler.getItemPrice()[rock
												.getOreId()]);
								player.sendMessage("You manage to profit @blu@"
										+ ItemHandler.getItemPrice()[rock
												.getOreId()]
										+ "@bla@ coins, from the rock.");
							}

						}
					}

				}, 4);
	}

	public static void resetMining(Client player) {
		player.startAnimation(65535);
		player.skillActive()[player.playerMining] = false;
	}
}
