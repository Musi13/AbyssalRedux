package rs2.abyssalps.content.tab;

import rs2.abyssalps.model.player.PlayerHandler;

public class InformationTab extends Tab {

	public InformationTab(QuestTab questTab) {
		super(questTab, "Information");
	}

	@Override
	public void redraw() {
		getQuestTab().setText("@whi@View achievements", 1);
		getQuestTab().setText("@whi@View Boss Logs", 2);
		getQuestTab().setText(
				"@or2@Players online: @gre@" + PlayerHandler.getPlayerCount(),
				3);
		getQuestTab().setText(
				"@or2@Wilderness points: @gre@"
						+ getQuestTab().getClient().getPoints()[3], 4);
		getQuestTab().setText(
				"@or2@AbyssalPS tokens: @gre@"
						+ getQuestTab().getClient().getPoints()[2], 5);
		getQuestTab().setText(
				"@or2@Achievement points: @gre@"
						+ getQuestTab().getClient().getPoints()[4], 6);

		getQuestTab().setText(
				"@or2@Slayer points: @gre@"
						+ getQuestTab().getClient().getSlayerData()[2], 7);
	}

	@Override
	public void select(int id) {
		switch (id) {
		case 1:
			getQuestTab().open(new AchievementsTab(getQuestTab()));
			break;

		case 2:
			getQuestTab().open(new BossCountTab(getQuestTab()));
			break;
		}
	}
}
