package rs2.abyssalps.content.tab;

import rs2.abyssalps.content.bosslog.BossLog;

public class BossCountTab extends Tab {

	public BossCountTab(QuestTab questTab) {
		super(questTab, "Boss Log");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void redraw() {
		getQuestTab().setText("@whi@View Information", 1);
		int count = 3;
		for (BossLog log : getQuestTab().getClient().getBossLogs()
				.getBossLogList()) {
			getQuestTab().setText(
					"@or2@" + log.getTitle() + ": @whi@" + log.getCount(),
					count);
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
