package rs2.abyssalps.content.tab;

public abstract class Tab {

	private final QuestTab questTab;
	private final String title;
	
	public Tab(QuestTab questTab, String title) {
		this.questTab = questTab;
		this.title = title;
	}
	
	public abstract void redraw();
	
	public abstract void select(int id);
	
	public QuestTab getQuestTab() {
		return questTab;
	}
	
	public String getTitle() {
		return title;
	}
	
}