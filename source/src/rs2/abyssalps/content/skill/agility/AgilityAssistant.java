package rs2.abyssalps.content.skill.agility;

import java.util.HashMap;
import java.util.Map;

import rs2.abyssalps.content.skill.agility.impl.GnomeCourseBalancingRope;
import rs2.abyssalps.content.skill.agility.impl.GnomeCourseLog;
import rs2.abyssalps.content.skill.agility.impl.GnomeCourseObstacleNet;
import rs2.abyssalps.content.skill.agility.impl.GnomeCourseObstaclePipe;
import rs2.abyssalps.content.skill.agility.impl.GnomeCourseObtacleNet2;
import rs2.abyssalps.content.skill.agility.impl.GnomeCourseTopTreeBranch;
import rs2.abyssalps.content.skill.agility.impl.GnomeCourseTreeBranch;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.player.Client;

public abstract class AgilityAssistant {

	private static Map<Integer, AgilityAssistant> agilityMap = new HashMap<Integer, AgilityAssistant>();

	public static AgilityAssistant forId(int id) {
		return agilityMap.get(id);
	}

	static {

		agilityMap.put(AgilityConstants.GNOME_COURSE_LOG_ID,
				new GnomeCourseLog());

		agilityMap.put(AgilityConstants.GNOME_COURSE_OBSTACLE_NET_ID,
				new GnomeCourseObstacleNet());

		agilityMap.put(AgilityConstants.GNOME_COURSE_TREE_BRANCH,
				new GnomeCourseTreeBranch());

		agilityMap.put(AgilityConstants.GNOME_COURSE_BALANCING_ROPE,
				new GnomeCourseBalancingRope());

		agilityMap.put(AgilityConstants.GNOME_COURSE_TOP_TREE_BRANCH,
				new GnomeCourseTopTreeBranch());

		agilityMap.put(AgilityConstants.GNOME_COURSE_OBSTACLE_NET_2,
				new GnomeCourseObtacleNet2());

		agilityMap.put(AgilityConstants.GNOME_COURSE_OBSTACLE_PIPE,
				new GnomeCourseObstaclePipe());
	}

	public abstract void execute(Client player);

	public abstract int[] animations();

	public abstract int levelRequired();

	public abstract int experience();

	public abstract String[] messages();

	public static void handleAgility(Client player) {

		if (!agilityMap.containsKey(player.objectId)) {
			return;
		}

		AgilityAssistant agilityA = AgilityAssistant.forId(player.objectId);

		if (agilityA == null) {
			return;
		}

		player.skillActive()[player.playerAgility] = true;
		player.setCanWalk(false);
		agilityA.execute(player);
	}

	public static void end(Client player, Position position) {
		AgilityAssistant agilityA = AgilityAssistant.forId(player.objectId);

		if (agilityA == null) {
			return;
		}

		if (agilityA.messages()[1] != null) {
			player.sendMessage(agilityA.messages()[1]);
		}

		player.getPA().movePlayer(position.getX(), position.getY(),
				position.getZ());

		int experience = agilityA.experience() * 25;

		if (AgilityConstants.totalGnomeCourseDone(player) >= 6) {
			experience += (AgilityConstants.GNOME_COURSE_END_BONUS_EXPERIENCE * 25);
			AgilityConstants.resetGnomeCourse(player);
		}

		player.getPA().addSkillXP(experience, player.playerAgility);
		player.getPA().refreshSkill(player.playerAgility);

		player.getCombat().getPlayerAnimIndex();
		player.setAppearanceUpdateRequired(true);
		player.updateRequired = true;

		if (!player.getCanWalk()) {
			player.setCanWalk(true);
		}
		player.skillActive()[player.playerAgility] = false;
		player.setCanWalk(true);
		player.setRun(true);
	}
}
