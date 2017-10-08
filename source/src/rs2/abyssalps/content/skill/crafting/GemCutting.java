package rs2.abyssalps.content.skill.crafting;

import rs2.abyssalps.model.player.Client;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class GemCutting {

	public static void execute(Client client) {
		GemTable gem = GemTable.forId(client.getGemId());
		if (gem == null) {
			return;
		}
		if (client.playerLevel[client.playerCrafting] < gem.getLevel()) {
			client.sendMessage("You need a crafting level of atleast "
					+ gem.getLevel() + " to cut this stone.");
			return;
		}
		client.skillActive()[client.playerCrafting] = true;
		CycleEventHandler.getSingleton().stopEvents(client,
				CycleEventHandler.CRAFTING_ID);
		CycleEventHandler.getSingleton().addEvent(
				CycleEventHandler.CRAFTING_ID, client, new CycleEvent() {

					@Override
					public void execute(CycleEventContainer container) {
						if (client == null || container.getOwner() == null) {
							container.stop();
							return;
						}
						if (!client.skillActive()[client.playerCrafting]) {
							return;
						}
						client.startAnimation(gem.getEmote());
						client.getItems().deleteItem(
								client.getGemId(),
								client.getItems()
										.getItemSlot(client.getGemId()), 1);
						client.getItems().addItem(gem.getAmuletId(), 1);
						client.getAchievements().onCollectItem(gem.getAmuletId(), 1);
						
						client.getPA().addSkillXP(gem.getExperience() * 25,
								client.playerCrafting);
						client.getPA().refreshSkill(client.playerCrafting);
						client.setGemsToCut(client.getAmountToCut() - 1);
						if (client.getAmountToCut() <= 0) {
							container.stop();
							return;
						}
						if (!client.getItems().playerHasItem(client.getGemId())) {
							client.sendMessage("I have run out of gems to cut.");
							container.stop();
							return;
						}
					}

					@Override
					public void stop() {
						resetAction(client);
					}

				}, 2);
	}

	public static void resetAction(Client client) {
		client.skillActive()[client.playerCrafting] = false;
		client.setGemInterfaceState(false);
		client.setGemsToCut(0);
		client.setGemId(0);
		client.startAnimation(65535);
	}

}
