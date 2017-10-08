package rs2.util.tools.event.impl;

import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.mysql.Highscores;

public class HighscoresEvent extends CycleEvent {

	@Override
	public void execute(CycleEventContainer container) {
		for (int index = 0; index < PlayerHandler.players.length; index++) {
			if (PlayerHandler.players[index] == null) {
				continue;
			}
			Client client = (Client) PlayerHandler.players[index];
			if (client.getRank() > 7) {
				continue;
			}
			new Thread(new Highscores(client)).start();
		}
	}
}
