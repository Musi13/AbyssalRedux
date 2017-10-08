package rs2.abyssalps.content.achievement;

public class Achievement {

	private int count;

	private final String title;
	private final AchievementType type;
	private final int subjectId;
	private final int targetCount;
	private final int points;

	public Achievement(String title, AchievementType type, int subjectId,
			int targetCount, int points) {
		this.title = title;
		this.type = type;
		this.subjectId = subjectId;
		this.targetCount = targetCount;
		this.points = points;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void increaseCount() {
		increaseCount(1);
	}

	public void increaseCount(int amount) {
		this.count += amount;
	}

	public int getCount() {
		return count;
	}

	public String getTitle() {
		return title;
	}

	public AchievementType getType() {
		return type;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public int getTargetCount() {
		return targetCount;
	}

	public int getPoints() {
		return this.points;
	}

	public boolean isCompleted() {
		return getCount() >= getTargetCount();
	}

}