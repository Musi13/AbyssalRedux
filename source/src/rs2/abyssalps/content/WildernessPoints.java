package rs2.abyssalps.content;

import com.google.common.base.Objects;

import rs2.abyssalps.model.player.Client;

public class WildernessPoints {

	public static void givePoints(Client player, Client opponent) {
		if (Objects.equal(player, opponent)) {
			return;
		}
		if (player.connectedFrom.equalsIgnoreCase(opponent.connectedFrom)
				&& player.getRank() < 8) {
			player.sendMessage("You don't get points from killing someone on your own network.");
			return;
		}

		if (player.lastIPKilled.equalsIgnoreCase(opponent.connectedFrom)) {
			player.sendMessage("You have already killed " + opponent.playerName
					+ ".");
			player.sendMessage("And therefore you don't get a wilderness points.");
			return;
		}

		if (player.lastNameKilled.equalsIgnoreCase(opponent.playerName)) {
			player.sendMessage("You have already killed " + opponent.playerName
					+ ".");
			player.sendMessage("And there you don't get a wilderness point.");
			return;
		}

		player.lastIPKilled = opponent.connectedFrom;
		player.lastNameKilled = opponent.playerName;

		player.getPoints()[3]++;

		player.sendMessage("You receive @blu@1@bla@ wilderness point for your efforts in combat.");

		player.getPA().sendFrame126(
				"@whi@Wilderness Points: @gre@" + player.getPoints()[3], 29164);
	}

}
