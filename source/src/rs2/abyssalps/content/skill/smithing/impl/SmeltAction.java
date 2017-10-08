package rs2.abyssalps.content.skill.smithing.impl;

import rs2.abyssalps.model.player.Client;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class SmeltAction {

	private final static int[] SMELT_FRAME = { 2405, 2406, 2407, 2409, 2410,
			2411, 2412, 2413 };
	private final static int[] SMELT_BARS = { 2349, 2351, 2355, 2353, 2357,
			2359, 2361, 2363 };

	public static void showInterface(Client client, int object) {
		for (int j = 0; j < SMELT_FRAME.length; j++) {
			client.getPA().sendFrame246(SMELT_FRAME[j], 150, SMELT_BARS[j]);
		}
		client.getPA().sendFrame164(2400);
	}

	public static void execute(Client client, int amount, int barId) {
		SmeltTable smelt = SmeltTable.forId(barId);
		if (smelt == null) {
			return;
		}
		client.getPA().removeAllWindows();
		if (client.playerLevel[client.playerSmithing] < smelt.getLevel()) {
			client.sendMessage("You need a smithing level of atleast "
					+ smelt.getLevel() + " to smelt this bar.");
			return;
		}
		if (!client.getItems().playerHasItem(smelt.getOreId(),
				smelt.getAmount())
				|| !client.getItems().playerHasItem(smelt.getOreId2(),
						smelt.getAmount2())) {
			client.sendMessage("You don't have the ores required to smelt this bar.");
			return;
		}
		client.skillActive()[client.playerSmithing] = true;
		client.setAmountToSmelt(amount);
		client.startAnimation(899);
		CycleEventHandler.getSingleton().stopEvents(client,
				CycleEventHandler.SMELTING_ID);
		CycleEventHandler.getSingleton().addEvent(
				CycleEventHandler.SMELTING_ID, client, new CycleEvent() {

					@Override
					public void execute(CycleEventContainer container) {
						if (client == null
								|| !client.skillActive()[client.playerSmithing]
								|| client.playerLevel[3] <= 0 || client.isDead) {
							container.stop();
							return;
						}
						client.startAnimation(899);
						client.getItems().deleteItem(smelt.getOreId(),
								smelt.getAmount());
						if (smelt.getOreId2() > 0)
							client.getItems().deleteItem(smelt.getOreId2(),
									smelt.getAmount2());
						client.getAchievements().onCollectItem(smelt.getBarId(), 1);
						client.getItems().addItem(smelt.getBarId(), 1);
						client.getPA().addSkillXP(smelt.getExperience() * 25,
								client.playerSmithing);

						client.setAmountToSmelt(client.getAmountToSmelt() - 1);

						if (client.getAmountToSmelt() <= 0) {
							container.stop();
							return;
						}

						if (!client.getItems().playerHasItem(smelt.getOreId(),
								smelt.getAmount())
								|| !client.getItems().playerHasItem(
										smelt.getOreId2(), smelt.getAmount2())) {
							client.sendMessage("You have run out of ores.");
							container.stop();
							return;
						}
					}

					@Override
					public void stop() {
						/**
						 * Resets player @animation
						 */
						client.startAnimation(65535);
					}

				}, 5);
	}

	public static void resetAction(Client client) {
		client.skillActive()[client.playerSmithing] = false;
		client.startAnimation(65535);
	}

}
