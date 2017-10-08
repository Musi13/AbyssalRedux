package rs2.abyssalps.content.achievement;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import rs2.abyssalps.content.PlayerContent;
import rs2.abyssalps.model.player.Client;

public class Achievements extends PlayerContent {

	private Map<Integer, Achievement> achievements = new HashMap<>();

	public Achievements(Client client) {
		super(client);

		/*
		 * Define achievements.
		 */
		achievements.put(0, new Achievement("Kill 20 Rock Crabs",
				AchievementType.MONSTER_KILLING, 100, 20, 1));

		achievements.put(1, new Achievement("Chop 20 normal logs",
				AchievementType.COLLECTING, 1511, 20, 1));

		achievements.put(2, new Achievement("Fletch 20 shortbows",
				AchievementType.COLLECTING, 50, 20, 1));

		achievements.put(3, new Achievement("String 20 shortbows",
				AchievementType.COLLECTING, 841, 20, 1));

		achievements.put(4, new Achievement("Catch 20 tunas",
				AchievementType.COLLECTING, 359, 20, 1));

		achievements.put(5, new Achievement("Craft 500 air runes",
				AchievementType.COLLECTING, 556, 500, 1));

		achievements.put(6, new Achievement("Mine 20 tin ores",
				AchievementType.COLLECTING, 438, 20, 1));

		achievements.put(7, new Achievement("Mine 20 copper ores",
				AchievementType.COLLECTING, 436, 20, 1));

		achievements.put(8, new Achievement("Cook 20 tunas",
				AchievementType.COLLECTING, 361, 20, 1));

		achievements.put(9, new Achievement("Cut 100 sapphire gems",
				AchievementType.COLLECTING, 1727, 100, 1));

		achievements.put(10, new Achievement("Kill 20 hill giants",
				AchievementType.MONSTER_KILLING, 2098, 20, 2));

		achievements.put(11, new Achievement("Chop 20 oak logs",
				AchievementType.COLLECTING, 1521, 20, 2));

		achievements.put(12, new Achievement("Fletch 20 oak shortbows",
				AchievementType.COLLECTING, 54, 20, 2));

		achievements.put(13, new Achievement("Catch 20 swordfish",
				AchievementType.COLLECTING, 371, 20, 2));

		achievements.put(14, new Achievement("Cook 20 swordfish",
				AchievementType.COLLECTING, 373, 20, 2));

		achievements.put(15, new Achievement("Craft 500 fire runes",
				AchievementType.COLLECTING, 554, 500, 2));

		achievements.put(16, new Achievement("Mine 20 iron ores",
				AchievementType.COLLECTING, 440, 20, 2));

		achievements.put(17, new Achievement("Smelt 20 iron bars",
				AchievementType.COLLECTING, 2351, 20, 2));

		achievements.put(18, new Achievement("Cut 100 diamond gems",
				AchievementType.COLLECTING, 1731, 100, 2));

		achievements.put(19, new Achievement("Chop 50 yew logs",
				AchievementType.COLLECTING, 1515, 50, 3));

		achievements.put(20, new Achievement("Fletch 50 yew shortbows",
				AchievementType.COLLECTING, 68, 50, 3));

		achievements.put(21, new Achievement("String 50 yew shortbows",
				AchievementType.COLLECTING, 857, 50, 3));

		achievements.put(22, new Achievement("Catch 100 sharks",
				AchievementType.COLLECTING, 383, 100, 3));

		achievements.put(23, new Achievement("Cook 100 sharks",
				AchievementType.COLLECTING, 385, 100, 3));

		achievements.put(24, new Achievement("Craft 1000 death runes",
				AchievementType.COLLECTING, 560, 1000, 3));

		achievements.put(25, new Achievement("Mine 100 adamantite ores",
				AchievementType.COLLECTING, 449, 100, 3));

		achievements.put(26, new Achievement("Smelt 100 adamantite bars",
				AchievementType.COLLECTING, 2361, 100, 3));

		achievements.put(27, new Achievement("Cut 500 dragonstones",
				AchievementType.COLLECTING, 1712, 500, 3));

		achievements.put(28, new Achievement("Kill 20 Dagannoth rex",
				AchievementType.MONSTER_KILLING, 2267, 20, 3));

		achievements.put(29, new Achievement("Kill 20 Dagannoth Prime",
				AchievementType.MONSTER_KILLING, 2266, 20, 3));

		achievements.put(30, new Achievement("Kill 20 Dagannoth Supreme",
				AchievementType.MONSTER_KILLING, 2267, 20, 3));
	}

	public void updateAchievements(AchievementType type, int subjectId,
			int amount) {
		for (Achievement achievement : getAchievementList()) {
			if (achievement.getType() != type
					|| achievement.getSubjectId() != subjectId) {
				continue;
			}
			boolean completed = achievement.isCompleted();
			achievement.increaseCount(amount);
			if (!completed && achievement.isCompleted()) {
				getClient().sendMessage(
						"@dre@You have completed \"" + achievement.getTitle()
								+ "\" achievement.");
				getClient().getPoints()[4] += achievement.getPoints();
			}
		}
	}

	public void onCollectItem(int itemId, int amount) {
		updateAchievements(AchievementType.COLLECTING, itemId, amount);
	}

	public void onKillNpc(int npcId) {
		updateAchievements(AchievementType.MONSTER_KILLING, npcId, 1);
	}

	public Map<Integer, Achievement> getAchievementMap() {
		return achievements;
	}

	public Achievement getAchievement(int id) {
		return achievements.get(id);
	}

	public Collection<Achievement> getAchievementList() {
		return achievements.values();
	}

}