package rs2.abyssalps.content.skill.slayer;

import java.util.Random;

import rs2.Server;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.player.Client;

/**
 * 
 * @author Jack All needed constants for slayer
 */

public class SlayerConstants {

	public static final int ENCHANTED_GEM_ID = 4155;

	public static void useEnchantedGem(Client player) {
		player.getDH().sendStatement(
				"Your task is to kill "
						+ player.getSlayerData()[1]
						+ " "
						+ Server.npcHandler.getNpcListName(player
								.getSlayerData()[0]) + "s.");
		player.nextChat = 0;
	}

	public static void getSlayerExperience(Client player, int index) {
		int experience = NPCHandler.npcs[index].MaxHP * 25;
		player.getPA().addSkillXP(experience, player.playerSlayer);
		player.getPA().refreshSkill(player.playerSlayer);

		player.getSlayerData()[1]--;
		if (player.getSlayerData()[1] <= 0) {
			player.sendMessage("@dre@You have successfully finisehd your Slayer task.");
			player.getSlayerData()[2] += getSlayerPoints(getTaskDifficulty(player
					.getSlayerData()[0]));
			player.getSlayerData()[0] = -1;
			player.getSlayerData()[1] = -1;
		}
	}

	public static int getSlayerPoints(TaskDifficulty difficulty) {

		if (difficulty == null) {
			return 0;
		}

		switch (difficulty) {
		case EASY:
			return 2;

		case MEDIUM:
			return 4;

		case HARD:
			return 10;

		default:
			return 0;
		}
	}

	public static final int TASK_RESET_FEE = 1000000;

	public static boolean hasTask(Client player) {
		if (player.getSlayerData()[0] > 0 || player.getSlayerData()[1] > 0) {
			return true;
		}
		return false;
	}

	public static boolean canResetTask(Client player) {
		if (player.getItems().playerHasItem(995, TASK_RESET_FEE)) {
			return true;
		}
		player.getDH().sendDialogues(24, -1);
		return false;
	}

	private static Random random = new Random();

	public static final int MINIMUM_AMOUNT = 15;

	public static final int MAX_ADDTIONAL_AMOUNT = 35;

	public static enum TaskDifficulty {
		EASY, MEDIUM, HARD
	}

	private static final int[] EASY_SLAYER_TASKS = { 2827, 2527, 2480, 70 };

	private static final int[] MEDIUM_SLAYER_TASKS = { 265, 2250, 260, 2098,
			2005, 2090 };

	private static final int[] HARD_SLAYER_TASKS = { 5874, 265, 259, 2250,
			3133, 2167, 319, 2265, 2266, 2267, 6609, 6618, 6619 };

	public static TaskDifficulty getTaskDifficulty(int npcId) {
		TaskDifficulty difficulty = null;
		for (int index = 0; index < EASY_SLAYER_TASKS.length; index++) {
			if (npcId == EASY_SLAYER_TASKS[index]) {
				difficulty = TaskDifficulty.EASY;
			}
		}
		for (int index = 0; index < MEDIUM_SLAYER_TASKS.length; index++) {
			if (npcId == MEDIUM_SLAYER_TASKS[index]) {
				difficulty = TaskDifficulty.MEDIUM;
			}
		}
		for (int index = 0; index < HARD_SLAYER_TASKS.length; index++) {
			if (npcId == HARD_SLAYER_TASKS[index]) {
				difficulty = TaskDifficulty.HARD;
			}
		}
		return difficulty;
	}

	public static int getRandomEasySlayerTask() {
		return EASY_SLAYER_TASKS[(int) (Math.random() * EASY_SLAYER_TASKS.length)];
	}

	public static int getRandomMediumSlayerTask() {
		return MEDIUM_SLAYER_TASKS[(int) (Math.random() * MEDIUM_SLAYER_TASKS.length)];
	}

	public static int getRandomHardSlayerTask() {
		return HARD_SLAYER_TASKS[(int) (Math.random() * HARD_SLAYER_TASKS.length)];
	}

	public static int getRandomTaskAmount() {
		return MINIMUM_AMOUNT + random.nextInt(MAX_ADDTIONAL_AMOUNT);
	}
}
