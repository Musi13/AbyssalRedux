package rs2.abyssalps.content.minigame.fightpits;

import java.util.concurrent.CopyOnWriteArrayList;

import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.Misc;

public class FightPitsMinigame {

	public static CopyOnWriteArrayList<Client> playersInLobby = new CopyOnWriteArrayList<Client>();
	public static CopyOnWriteArrayList<Client> playersInGame = new CopyOnWriteArrayList<Client>();

	public static boolean inFightPits(Client player) {
		return playersInGame.contains(player)
				|| playersInLobby.contains(player);
	}

	public static boolean inGame(Client player) {
		return playersInGame.contains(player);
	}

	public static void destructGameForPlayer(Client player) {
		if (playersInLobby.contains(player)) {
			playersInLobby.remove(player);
		}
		if (playersInGame.contains(player)) {
			playersInGame.remove(player);
		}
		player.getPA().movePlayer(2399, 5177, 0);
	}

	public static void removeFromGame(Client player) {
		if (playersInGame.contains(player)) {
			playersInGame.remove(player);
			player.getPA().movePlayer(FightPitsConstants.LOBBY_AREA_POS.getX(),
					FightPitsConstants.LOBBY_AREA_POS.getY(),
					FightPitsConstants.LOBBY_AREA_POS.getZ());
		}
	}

	public static void enter(Client player) {
		if (playersInLobby.contains(player)) {
			player.getPA().movePlayer(2399, 5177, 0);
			playersInLobby.remove(player);
			return;
		}
		player.getPA().movePlayer(FightPitsConstants.LOBBY_AREA_POS.getX(),
				FightPitsConstants.LOBBY_AREA_POS.getY(),
				FightPitsConstants.LOBBY_AREA_POS.getZ());
		playersInLobby.add(player);
	}

	private static void destroyGame(String winner) {
		FightPitsConstants.gameStarted = false;
		System.out.println(winner);
		Client roundWinner = (Client) PlayerHandler.getPlayer(winner);
		for (Client player : playersInGame) {
			if (player == null) {
				continue;
			}
			player.getPA().movePlayer(FightPitsConstants.LOBBY_AREA_POS.getX(),
					FightPitsConstants.LOBBY_AREA_POS.getY(), 0);

			if (roundWinner != null) {
				roundWinner
						.sendMessage("@dre@Congratulations, you are this rounds' winner!");

				roundWinner.getItems().addItem(FightPitsConstants.TOKKUL_ID,
						roundWinner.combatLevel * 25);
				FightPitsConstants.lastWinner = roundWinner.playerName;

				roundWinner.getPA().globalYell(
						"[@blu@Fight Pits@bla@]@dre@"
								+ Misc.formatPlayerName(roundWinner.playerName)
								+ " won this Fight Pits round.");
			}

			if (winner == null) {
				player.sendMessage("@dre@No one won in this round.");
			}

			for (int index = 0; index < 6; index++) {
				player.playerLevel[index] = player
						.getLevelForXP(player.playerXP[index]);
				player.getPA().refreshSkill(index);
			}

			player.specAmount = 10;
			player.getItems().addSpecialBar(player.playerEquipment[3]);
			playersInLobby.add(player);
		}
		playersInGame.clear();
		FightPitsConstants.gameDurationTime = FightPitsConstants.GAME_DURATION_TIME;
	}

	private static void startMinigame() {
		FightPitsConstants.gameStarted = true;
		for (Client player : playersInLobby) {
			if (player == null) {
				continue;
			}
			int randomPos = Misc.random(3);
			player.getPA().movePlayer(
					FightPitsConstants.GAME_AREA_POS.getX() + randomPos,
					FightPitsConstants.GAME_AREA_POS.getY() + randomPos,
					FightPitsConstants.GAME_AREA_POS.getZ());

			for (int index = 0; index < 6; index++) {
				player.playerLevel[index] = player
						.getLevelForXP(player.playerXP[index]);
				player.getPA().refreshSkill(index);
			}

			player.specAmount = 10.0D;
			player.getItems().addSpecialBar(player.playerEquipment[3]);
			playersInGame.add(player);
			player.getDH().sendStatement("Prepare to fight for your life!");
			player.nextChat = 0;
		}
		playersInLobby.clear();
		FightPitsConstants.lobbyWaitTimer = FightPitsConstants.LOBBY_WAIT_TIME;
	}

	public static void tick() {

		if (!FightPitsConstants.gameStarted
				&& playersInLobby.size() > FightPitsConstants.MINIMUM_REQUIRED_PLAYERS) {
			if (FightPitsConstants.lobbyWaitTimer > 0) {
				FightPitsConstants.lobbyWaitTimer--;
			}
			if (FightPitsConstants.lobbyWaitTimer == 0) {
				startMinigame();
				FightPitsConstants.lobbyWaitTimer = FightPitsConstants.LOBBY_WAIT_TIME;
			}
		}

		if (FightPitsConstants.gameStarted) {

			if (playersInGame.size() > 0 && playersInGame.size() <= 1) {
				destroyGame(playersInGame.get(0).playerName);
			} else if (playersInGame.size() <= 0) {
				destroyGame(null);
			}

			if (FightPitsConstants.gameDurationTime > 0) {
				FightPitsConstants.gameDurationTime--;
			}

			if (FightPitsConstants.gameDurationTime <= 0) {
				destroyGame(null);
			}
		}
	}

}
