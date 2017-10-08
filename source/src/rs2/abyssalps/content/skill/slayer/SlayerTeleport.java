package rs2.abyssalps.content.skill.slayer;

import rs2.Server;
import rs2.abyssalps.content.minigame.fightpits.FightPitsMinigame;
import rs2.abyssalps.model.player.Client;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class SlayerTeleport {

	private static boolean taskInWildy(int npcType) {
		switch (npcType) {
		case 260:
			return true;

		default:
			return false;
		}
	}

	public static void activateEnchantedGem(Client player) {
		if (!SlayerConstants.hasTask(player)) {
			player.sendMessage("You need a slayer task to activate the gem.");
			return;
		}

		if (taskInWildy(player.getSlayerData()[0])) {
			player.getDH().sendStatement(
					Server.npcHandler.getNpcListName(player.getSlayerData()[0])
							+ "s are in wild, sure you wanna go?");
		} else {
			player.getDH().sendStatement(
					"Are you sure you wanna teleport to your slayer task?");
		}
		player.nextChat = 26;
	}

	public static void execute(Client player) {

		/**
		 * Fetches slayer teleport table / data
		 */
		SlayerTeleportTable teleport = SlayerTeleportTable.forId(player
				.getSlayerData()[0]);

		if (teleport == null) {
			player.sendMessage("No teleport exist for this task.");
			return;
		}

		/**
		 * Checks if @player is allowed to teleport right now.
		 */
		if (!canTeleport(player)) {
			return;
		}

		player.setCanWalk(false);

		/**
		 * Removes interface windows & resets player variables
		 */
		player.getPA().removeAllWindows();

		/**
		 * Reset movement queue
		 */
		player.stopMovement();

		/**
		 * Reset players combat actions
		 */
		if (player.inCombat()) {
			player.getCombat().resetPlayerAttack();
		}

		player.gfx0(342);
		player.startAnimation(1816);

		CycleEventHandler.getSingleton().addEvent(
				CycleEventHandler.SLAYER_TELEPORT_ID, player, new CycleEvent() {
					int timer = 5;

					@Override
					public void execute(CycleEventContainer container) {
						if (timer == 2) {
							player.getPA().movePlayer(
									teleport.getPosition().getX(),
									teleport.getPosition().getY(),
									teleport.getPosition().getZ());
						} else if (timer == 1) {
							player.startAnimation(715);
						} else if (timer == 0) {
							player.setCanWalk(true);
							container.stop();
						}
						timer--;
					}

				}, 1);
	}

	private static boolean canTeleport(Client player) {
		if (FightPitsMinigame.inFightPits(player)) {
			player.sendMessage("I can't do this right now.");
			return false;
		}
		if (CycleEventHandler.getSingleton().isAlive(player,
				CycleEventHandler.SLAYER_TELEPORT_ID)) {
			return false;
		}

		if (player.isDead || player.playerLevel[3] <= 0 || player.teleTimer > 0) {
			return false;
		}

		if (!player.getCanWalk()) {
			return false;
		}

		if (player.isJailed()) {
			player.sendMessage("You can't teleport right now.");
			return false;
		}

		if (player.wildLevel > 20 && player.inWild()) {
			player.sendMessage("You can't teleport above level 20 in the wilderness.");
			return false;
		}

		if (System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return false;
		}

		return true;
	}

}
