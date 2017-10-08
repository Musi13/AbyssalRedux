package rs2.abyssalps.content.vote;

import java.util.Comparator;

public class RewardComparator implements Comparator<RewardDefinitions> {

	@Override
	public int compare(RewardDefinitions dropA, RewardDefinitions dropB) {
		if (dropA.getRarity() < dropB.getRarity()) {
			return 1;
		} else if (dropA.getRarity() > dropB.getRarity()) {
			return -1;
		}
		return Math.random() < 0.5 ? 1 : -1;
	}

}
