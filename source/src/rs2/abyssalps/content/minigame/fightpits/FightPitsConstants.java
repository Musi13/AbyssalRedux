package rs2.abyssalps.content.minigame.fightpits;

import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.player.Client;

public class FightPitsConstants {

	public static final int MINIMUM_REQUIRED_PLAYERS = 5;

	public static void gameInterface(Client player) {
		String winner = lastWinner == null ? "Tzhaar-Xil-Huz" : lastWinner;
		player.getPA().sendFrame126("Current Champion: " + winner, 2805);
		player.getPA().sendFrame126(
				"@red@Foes remaining: "
						+ FightPitsMinigame.playersInGame.size(), 2806);
		player.getPA().sendFrame36(560, 1);
		player.getPA().walkableInterface(2804);
	}

	public static final int TOKKUL_ID = 6529;

	public static final int TOKKUL_REWARD_AMOUNT = 15000;

	public static final int LOBBY_WAIT_TIME = 300;
	public static final int GAME_DURATION_TIME = 1200;

	public static int lobbyWaitTimer = LOBBY_WAIT_TIME;
	public static int gameDurationTime = GAME_DURATION_TIME;

	public static String lastWinner = null;

	public static boolean gameStarted = false;

	public static final Position LOBBY_AREA_POS = new Position(2399, 5175, 0);
	public static final Position GAME_AREA_POS = new Position(2397, 5157, 0);

}
