package rs2.util.tools.event.impl;

import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;

public class GlobalShoutEvent extends CycleEvent {

	private static String[] shouts = {
			"You can help the server by voting at @blu@::vote",

			"There are currently @blu@" + PlayerHandler.getPlayers().size()
					+ "@bla@ players online!",

			"If you find any bugs make sure to report them at @blu@www.abyssalps.com",

			"You can visit our website at @blu@www.abyssalps.com",

			"Vote for rewards by typing @blu@::vote",

			"You can buy AbyssalPS tokens at @blu@::store",

			"You can visit the highscores by typing @blu@::highscores",

			"Did you know? We have most wildy bosses spawned in the wilderness.",

			"Buy AbyssalPS points helps out the server greatly @blu@::store",

			"Voting increases AbyssalPS player count! @blu@::vote"

	};

	@Override
	public void execute(CycleEventContainer container) {
		for (int index = 0; index < PlayerHandler.players.length; index++) {
			if (PlayerHandler.players[index] == null) {
				continue;
			}
			Client players = (Client) PlayerHandler.players[index];

			players.sendMessage("[@blu@Global@bla@]"
					+ shouts[(int) (Math.random() * shouts.length)]);
		}
	}

}
