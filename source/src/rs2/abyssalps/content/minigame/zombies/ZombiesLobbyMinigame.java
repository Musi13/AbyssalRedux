package rs2.abyssalps.content.minigame.zombies;

import java.util.ArrayList;
import java.util.List;

import rs2.abyssalps.content.minigame.Minigame;
import rs2.abyssalps.model.player.Client;

public class ZombiesLobbyMinigame implements Minigame {

	private long startTime = -1;
	private List<Client> players = new ArrayList<>();

	@Override
	public boolean enter(Client client) {
		if (players.isEmpty()) {
			startTime = System.currentTimeMillis() + 120000;
		}
		players.add(client);
		return true;
	}

	@Override
	public void exit(Client client) {
		players.remove(client);
	}

	@Override
	public void process(Client client) {
		if (startTime - System.currentTimeMillis() <= 0) {
			ZombiesMinigame minigame = new ZombiesMinigame(players);
			for (Client player : players) {
				player.enterMinigame(minigame);
			}
			players.clear();
			return;
		}
		
		showInterface(client);
	}

	private void showInterface(Client client) {
		client.getPA().sendFrame126("", 27120);
		int secondsRemaining = (int) ((startTime - System.currentTimeMillis()) / 1000);
		int minutes = secondsRemaining / 60;
		int seconds = secondsRemaining - (minutes * 60);
		client.getPA().sendFrame126("Time remaining: " + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds, 27121);
		client.getPA().sendFrame126("Player count: " + players.size(), 27122);
		client.getPA().sendFrame126("", 27123);
		client.getPA().walkableInterface(27119);
	}

	private static ZombiesLobbyMinigame instance = null;

	public static ZombiesLobbyMinigame get() {
		if (instance == null) {
			instance = new ZombiesLobbyMinigame();
		}
		return instance;
	}

}
