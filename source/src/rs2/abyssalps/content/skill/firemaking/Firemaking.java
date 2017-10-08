package rs2.abyssalps.content.skill.firemaking;

import rs2.Server;
import rs2.abyssalps.model.objects.Object;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;
import rs2.util.cache.region.Region;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class Firemaking {

	public static void execute(Client client, int itemId) {
		FiremakingTable fire = FiremakingTable.forId(itemId);
		if (fire == null) {
			return;
		}
		if (!client.getItems().playerHasItem(fire.getLogId())) {
			return;
		}
		if (!client.getItems().playerHasItem(590)) {
			client.sendMessage("You need a tinderbox to light a fire.");
			return;
		}
		if (client.playerLevel[client.playerFiremaking] < fire.getLevel()) {
			client.sendMessage("You need a firemaking level of atleast "
					+ fire.getLevel() + " to light this log on fire.");
			return;
		}
		if (Server.objectManager.objectExists(client.getX(), client.getY())) {
			client.sendMessage("You can't light a fire here.");
			return;
		}
		client.skillActive()[client.playerFiremaking] = true;
		client.startAnimation(733);
		client.getItems().deleteItem(fire.getLogId(), 1);
		client.sendMessage("You attempt to light the logs.");
		Server.itemHandler.createGroundItem(client, fire.getLogId(),
				client.getX(), client.getY(), client.heightLevel, 1,
				client.getId());

		client.stopMovement();
		CycleEventHandler.getSingleton().stopEvents(client,
				CycleEventHandler.FIREMAKING_ID);
		CycleEventHandler.getSingleton().addEvent(
				CycleEventHandler.FIREMAKING_ID, client, new CycleEvent() {

					@Override
					public void execute(CycleEventContainer container) {
						if (client == null
								|| !client.skillActive()[client.playerFiremaking]
								|| client.playerLevel[3] <= 0 || client.isDead) {
							container.stop();
							return;
						}
						Server.itemHandler.removeGroundItem(client,
								fire.getLogId(), client.getX(), client.getY(),
								client.heightLevel, false);
						client.getPA().playerWalk(client.pathX - 1,
								client.pathY, false);
						new Object(4266, client.getX(), client.getY(), 0, 0,
								10, -1, 60 + Misc.random(30));
						client.sendMessage("The fire catches and the logs begin to burn.");
						client.getPA().addSkillXP(fire.getExperience() * 25,
								client.playerFiremaking);
						client.getPA().refreshSkill(client.playerFiremaking);
						container.stop();
					}

					@Override
					public void stop() {
						/**
						 * Resets player @animation
						 */
						client.startAnimation(65535);
					}

				}, 5 + Misc.random(5));
	}

	public static void resetAction(Client client) {
		client.skillActive()[client.playerFiremaking] = false;
		/**
		 * Resets player @animation
		 */
		client.startAnimation(65535);
	}

}
