package rs2.abyssalps.content.skill.agility;

import rs2.abyssalps.model.player.Client;

public class AgilityConstants {

	public static final int GNOME_COURSE_LOG_ID = 23145;
	public static final int GNOME_COURSE_OBSTACLE_NET_ID = 23134;
	public static final int GNOME_COURSE_TREE_BRANCH = 23559;
	public static final int GNOME_COURSE_BALANCING_ROPE = 23557;
	public static final int GNOME_COURSE_TOP_TREE_BRANCH = 23560;
	public static final int GNOME_COURSE_OBSTACLE_NET_2 = 23135;
	public static final int GNOME_COURSE_OBSTACLE_PIPE = 23138;

	public static final int GNOME_COURSE_END_BONUS_EXPERIENCE = 86;

	public static int totalGnomeCourseDone(Client player) {
		int total = 0;
		for (int index = 0; index < player.getGnomeCourseDone().length; index++) {
			if (player.getGnomeCourseDone()[index]) {
				total++;
			}
		}
		return total;
	}

	public static void resetGnomeCourse(Client player) {
		for (int index = 0; index < player.getGnomeCourseDone().length; index++) {
			player.getGnomeCourseDone()[index] = false;
		}
	}

	public static void setPlayerAnimationIndex(Client player, int stand,
			int walk, int run) {
		player.playerStandIndex = stand;
		player.playerWalkIndex = walk;
		player.playerRunIndex = run;
		player.setAppearanceUpdateRequired(true);
		player.updateRequired = true;
	}

}
