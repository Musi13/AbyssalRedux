package rs2.abyssalps.content.bosslog;

public class BossLog {

	public BossLog(String title, int subjectId) {
		this.title = title;
		this.subjectId = subjectId;
	}

	private String title;
	private int subjectId;
	private int count;

	public String getTitle() {
		return this.title;
	}

	public int getSubjectId() {
		return this.subjectId;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int amount) {
		this.count = amount;
	}

	public void increaseCount() {
		count++;
	}

}
