package rs2.abyssalps.content.minigame.pestcontrol;

import java.util.concurrent.CopyOnWriteArrayList;

import rs2.Server;
import rs2.abyssalps.model.Boundary;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class PestControl {

	private static final int START_X = 2658;
	private static final int START_Y = 2611;

	public static int gameTimer = 600;

	public static void startGame() {
		gameStarted = true;
		lobbyTimer = 60;
		for (Client player : inBoat) {

			resetPlayerAttributes(player);

			player.sendMessage("Prepare to fight!");

			player.getPA().movePlayer(START_X + Misc.random(2),
					START_Y + Misc.random(2), 0);
		}
	}

	private static int lobbyTimer = 60;
	private static boolean gameStarted = false;

	private static CopyOnWriteArrayList<Client> inBoat = new CopyOnWriteArrayList<Client>();
	private static CopyOnWriteArrayList<Client> inGame = new CopyOnWriteArrayList<Client>();

	public static boolean playerInBoat(Client client) {
		return inBoat.contains(client);
	}

	public static boolean playerInGame(Client client) {
		return inGame.contains(client);
	}

	public static void sendInterface(Client client) {
		if (playerInBoat(client)) {
			client.getPA().sendFrame126(
					"Next Departure: " + lobbyTimer + " secs", 27120);
			client.getPA().sendFrame126("Players Ready: " + inBoat.size(),
					27121);
			client.getPA().sendFrame126("(Need 5 to 25 players)", 27122);
			client.getPA().sendFrame126("Pest Points: 0", 27123);
		}
	}

	public static void tick() {
		if (!gameStarted) {
			if (lobbyTimer > 0 && inBoat.size() >= 1) {
				lobbyTimer--;
			}
		}

		if (gameStarted) {
			if (gameTimer > 0 && inGame.size() > 0) {
				gameTimer--;
			}
			if (gameTimer <= 0 || inGame.size() <= 0) {

			}
		}
	}

	public static void handleBoat(Client client) {
		if (!Boundary.isIn(client, Boundary.PEST_CONTROL_AREA)) {
			return;
		}
		if (playerInGame(client)) {
			return;
		}
		if (client.combatLevel < 40) {
			client.sendMessage("You need a combat level of 40 to play this minigame.");
			return;
		}
		if (Server.UpdateServer) {
			client.sendMessage("You can't participate in minigames during a server update.");
			return;
		}
		if (playerInBoat(client)) {
			client.sendMessage("You leave the lander.");
			client.getPA().movePlayer(2657, 2639, 0);
			inBoat.remove(client);
			return;
		}
		if (!playerInBoat(client)) {
			client.sendMessage("You board the lander.");
			client.getPA().movePlayer(2661, 2639, 0);
			inBoat.add(client);
			return;
		}
	}

	private static void resetPlayerAttributes(Client player) {
		for (int index = 0; index < 6; index++) {
			player.playerLevel[index] = player
					.getLevelForXP(player.playerXP[index]);
			player.getPA().refreshSkill(index);
		}
		player.specAmount = 10;
		player.getItems().addSpecialBar(
				player.playerEquipment[player.playerWeapon]);
		player.poisonDamage = 0;
		player.venomDamage = 0;
		player.vengOn = false;
	}
}
