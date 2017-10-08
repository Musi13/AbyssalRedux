package rs2.abyssalps.content.minigame.recipe_for_disaster;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.model.player.Client;

public class RecipeForDisasterMinigame {

	Client player;

	public RecipeForDisasterMinigame(Client player) {
		this.player = player;
	}

	public boolean enter() {

		if (player.getRecipe().getWaveId() > 3) {
			player.sendMessage("You have already completed this game.");
			return false;
		}

		if (player.playerLevel[player.playerMagic] < 54) {
			player.sendMessage("You need 54 magic to play this game.");
			return false;
		}

		int plane = player.getId() * 4;

		player.getPA().movePlayer(
				RecipeForDisasterConstants.RECIPE_FOR_DISASTER_AREA.getX(),
				RecipeForDisasterConstants.RECIPE_FOR_DISASTER_AREA.getY(),
				RecipeForDisasterConstants.RECIPE_FOR_DISASTER_AREA.getZ()
						+ plane);

		player.getRecipe().setInGame(true);

		startWave();
		return true;
	}

	public void endGame(boolean win) {
		if (win) {
			player.sendMessage("@or1@You have successfully completed the @or2@'Recipe for Disaster'@or1@ quest.");
			player.getRecipe().increaseWaveId();
		}
		player.getPA().movePlayer(Config.START_LOCATION_X,
				Config.START_LOCATION_Y, 0);
		player.getRecipe().setInGame(false);
	}

	public void startWave() {

		if (player.getRecipe().getWaveId() > 3) {
			endGame(true);
			return;
		}

		RecipeForDisasterTable table = RecipeForDisasterTable.forId(player
				.getRecipe().getWaveId());

		if (table == null) {
			return;
		}

		Server.npcHandler.spawnNpc(player, table.getNpcId(),
				RecipeForDisasterConstants.NPC_SPAWN_POSITION.getX(),
				RecipeForDisasterConstants.NPC_SPAWN_POSITION.getY(),
				player.heightLevel, 0, table.getCombatAttributes()[0],
				table.getCombatAttributes()[1], table.getCombatAttributes()[2],
				table.getCombatAttributes()[3], true, true);

	}

	private int waveId;

	public int getWaveId() {
		return this.waveId;
	}

	public void increaseWaveId() {
		waveId++;
	}

	private boolean inGame = false;

	public boolean inGame() {
		return this.inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
}
