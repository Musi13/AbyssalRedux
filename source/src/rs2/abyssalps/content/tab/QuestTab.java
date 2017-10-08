package rs2.abyssalps.content.tab;

import rs2.abyssalps.content.PlayerContent;
import rs2.abyssalps.model.player.Client;

public class QuestTab extends PlayerContent {

	private int timer = 0;
	private Tab activeTab;

	public QuestTab(Client client) {
		super(client);
		activeTab = new InformationTab(this);
	}

	public void open(Tab tab) {
		activeTab = tab;
		for (int i = 0; i < 103; i++) {
			setText("", i);
		}
		tab.redraw();
	}

	public void setText(String text, int id) {
		getClient().getPA().sendFrame126(text, 29162 + id);
	}

	public void redraw() {
		if (activeTab == null) {
			return;
		}
		if (timer > 0) {
			timer--;
			return;
		}
		getClient().getPA().sendFrame126("AbyssalPS", TITLE_ID);
		getClient().getPA().sendFrame126(" " + activeTab.getTitle(),
				SUB_TITLE_ID);

		activeTab.redraw();
		timer = REDRAW_TIMER;
	}

	public void select(int id) {
		if (activeTab == null) {
			return;
		}
		activeTab.select(id);
	}

	public static final int TITLE_ID = 29155;
	public static final int SUB_TITLE_ID = 29161;
	public static final int REDRAW_TIMER = 0;

}