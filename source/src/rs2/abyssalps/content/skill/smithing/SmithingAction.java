package rs2.abyssalps.content.skill.smithing;

import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class SmithingAction {

	public static void execute(Client client, int itemId, int amount) {
		SmithingTable smithing = SmithingTable.forId(itemId);
		if (smithing == null) {
			return;
		}
		if (client.playerLevel[client.playerSmithing] < smithing.getLevel()) {
			client.sendMessage("You need a smithing level of "
					+ smithing.getLevel() + " to smith "
					+ ItemDefinitions.forId(itemId).getName() + ".");
			return;
		}
		if (itemId == 13738 || itemId == 13740 || itemId == 13742
				|| itemId == 13744) {
			if (!client.getItems().playerHasItem(13736)) {
				client.sendMessage("You need a "
						+ ItemDefinitions.forId(13736).getName()
						+ " to smith this item.");
				return;
			}
		}
		if (itemId == 11283) {
			if (!client.getItems().playerHasItem(1540)) {
				client.sendMessage("You need a "
						+ ItemDefinitions.forId(1540).getName()
						+ " to smith this item.");
				return;
			}
		}
		if (!client.getItems().playerHasItem(smithing.getBarId(),
				smithing.getBarAmount())) {
			client.sendMessage("You need atleast " + smithing.getBarAmount()
					+ " "
					+ ItemDefinitions.forId(smithing.getBarId()).getName()
					+ "'s, to do this.");
			return;
		}
		client.getPA().removeAllWindows();
		client.skillActive()[client.playerSmithing] = true;
		client.setItemSmithAmount(amount);
		client.startAnimation(898);
		CycleEventHandler.getSingleton().stopEvents(client,
				CycleEventHandler.SMITHING_ID);
		CycleEventHandler.getSingleton().addEvent(
				CycleEventHandler.SMITHING_ID, client, new CycleEvent() {

					@Override
					public void execute(CycleEventContainer container) {
						if (client == null || client.playerLevel[3] <= 0
								|| client.isDead
								|| !client.skillActive()[client.playerSmithing]) {
							container.stop();
							return;
						}
						client.startAnimation(898);
						if (itemId == 13738 || itemId == 13740
								|| itemId == 13742 || itemId == 13744) {
							client.getItems().deleteItem(13736,
									client.getItems().getItemSlot(13736), 1);
						}
						if (itemId == 11283) {
							client.getItems().deleteItem(1540,
									client.getItems().getItemSlot(1540), 1);
						}
						client.getItems().deleteItem(smithing.getBarId(),
								smithing.getBarAmount());
						client.getItems().addItem(smithing.getItemId(),
								smithing.getItemAmount());
						client.getAchievements().onCollectItem(smithing.getItemId(), smithing.getItemAmount());
						client.getPA().addSkillXP(
								smithing.getExperience() * 10,
								client.playerSmithing);
						client.getPA().refreshSkill(client.playerSmithing);
						client.setItemSmithAmount(client
								.getItemsToSmithAmount() - 1);
						if (client.getItemsToSmithAmount() <= 0) {
							container.stop();
							return;
						}
						if (!client.getItems().playerHasItem(
								smithing.getBarId(), smithing.getBarAmount())) {
							client.sendMessage("You have run out of bars.");
							container.stop();
							return;
						}
					}

					@Override
					public void stop() {
						client.startAnimation(65535);
						resetAction(client);
					}

				}, 4);
	}

	public static void resetAction(Client client) {
		client.skillActive()[client.playerSmithing] = false;
		client.setItemSmithAmount(0);
		client.startAnimation(65535);
	}

}
