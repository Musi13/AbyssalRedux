package rs2.abyssalps.content.tab;

import rs2.abyssalps.content.achievement.Achievement;

public class AchievementsTab extends Tab {

	public AchievementsTab(QuestTab questTab) {
		super(questTab, "Achievements");
	}

	@Override
	public void redraw() {
		getQuestTab().setText("@whi@View Information", 1);
		int count = 3;
		for (Achievement achievement : getQuestTab().getClient().getAchievements().getAchievementList()) {
			String color = "@gre@", progress = "";
			if (!achievement.isCompleted()) {
				color = "@red@";
				progress = " (" + achievement.getCount() + "/" + achievement.getTargetCount() + ")";
			}
			getQuestTab().setText(color + achievement.getTitle() + progress, count);
			count++;
		}
	}

	@Override
	public void select(int id) {
		if (id == 1) {
			getQuestTab().open(new InformationTab(getQuestTab()));
		}
	}

}
