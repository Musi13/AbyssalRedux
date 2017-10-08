package rs2.util.mysql;

import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.Player;
import rs2.abyssalps.model.player.PlayerHandler;

import com.rspserver.motivote.MotivoteHandler;
import com.rspserver.motivote.Vote;

public class RewardHandler extends MotivoteHandler<Vote> {

	public static int totalVotes = 0;

	@Override
	public void onCompletion(Vote reward) {
		int itemID = -1;
		itemID = 6199;
		if (PlayerHandler.isPlayerOn(reward.username())) {
			Player p = PlayerHandler.getPlayer(reward.username());

			if (p != null && p.isActive == true) {
				synchronized (p) {
					Client c = (Client) p;
					if (c.getItems().addItem(itemID, 1)) {
						totalVotes++;
						if (totalVotes >= 29) {
							c.getPA().globalYell(
									"[@blu@Vote@bla@]Another " + totalVotes
											+ " votes has been placed.");
							c.getPA()
									.globalYell(
											"[@blu@Vote@bla@]You can vote by typing @blu@::vote");
							totalVotes = 0;
						}
						c.sendMessage("You've received your vote reward! Congratulations!");
						reward.complete();
					} else {
						c.sendMessage("Could not give you your reward item, try creating space.");
					}
				}
			}
		}
	}
}