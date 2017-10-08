package rs2.abyssalps.content.skill.slayer;

import rs2.abyssalps.content.skill.slayer.SlayerConstants.TaskDifficulty;
import rs2.abyssalps.model.player.Client;

/**
 * 
 * @author Jack Class represents slayer.
 */

public class Slayer {

	/**
	 * Method represents slayer task reset
	 * 
	 * @param player
	 */
	public static void resetSlayerTask(Client player) {
		if (!SlayerConstants.canResetTask(player)) { // 1 million gp needed.
			return;
		}

		player.getItems().deleteItem(995, player.getItems().getItemSlot(995),
				1000000);

		player.getSlayerData()[0] = -1;
		player.getSlayerData()[1] = -1;

		player.getDH().sendDialogues(25, -1);
	}

	public static void getSlayerTask(Client player, TaskDifficulty difficulty) {

		int slayerTask = -1;
		int taskAmount = 0;

		switch (difficulty) {

		case EASY:
			slayerTask = SlayerConstants.getRandomEasySlayerTask();
			break;

		case MEDIUM:
			slayerTask = SlayerConstants.getRandomMediumSlayerTask();
			break;

		case HARD:
			slayerTask = SlayerConstants.getRandomHardSlayerTask();
			break;

		default:
			slayerTask = -1;
			taskAmount = -1;
			break;

		}

		taskAmount = SlayerConstants.getRandomTaskAmount();

		player.getSlayerData()[0] = slayerTask;
		player.getSlayerData()[1] = taskAmount;

	}

}
