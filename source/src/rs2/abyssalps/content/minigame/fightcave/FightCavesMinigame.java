package rs2.abyssalps.content.minigame.fightcave;

import rs2.Server;
import rs2.abyssalps.content.minigame.Minigame;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.player.Client;

public class FightCavesMinigame implements Minigame {

	private int heightLevel;

	private int waveKillCount = 0;
	private int[] currentWaveNpcs;

	@Override
	public boolean enter(Client client) {
		heightLevel = client.heightLevel * 4;

		Position pos = FightCavesDefinitions.INSIDE_CAVE_POSITION;
		
		if (FightCavesDefinitions.inArea(client)) {
			pos = client.getPosition();
		}
		
		client.getPA().movePlayer(pos.getX(), pos.getY(), heightLevel);
		client.setTzhaarWaveId(0);
		return true;
	}

	@Override
	public void exit(Client client) {
		Position pos = FightCavesDefinitions.OUTSIDE_CAVE_POSITION;

		client.getItems().addItem(FightCavesDefinitions.TOKKULS_ID, client.getTzhaarWaveId() * client.getTzhaarWaveId() * 71);

		client.getPA().movePlayer(pos.getX(), pos.getY(), pos.getZ());
		client.setTzhaarWaveId(0);
	}

	@Override
	public void process(Client client) {
		if (!shouldSpawnNextWave(client)) {
			return;
		}

		int waveId = client.getTzhaarWaveId();
		Position[] spawnPositions = FightCavesDefinitions.SPAWN_POSITIONS;

		currentWaveNpcs = FightCavesDefinitions.WAVES[waveId];
		for (int i = 0; i < currentWaveNpcs.length; i++) {
			int npcId = currentWaveNpcs[i];
			Position spawnPosition = spawnPositions[0];
			if (i < spawnPositions.length) {
				spawnPosition = spawnPositions[i];
			}
			Server.npcHandler.spawnNpc(client, npcId, spawnPosition.getX(), spawnPosition.getY(), heightLevel, 1, FightCavesDefinitions.getHitpoints(npcId), FightCavesDefinitions.getMaxHit(npcId),
					FightCavesDefinitions.getAttack(npcId), FightCavesDefinitions.getDefence(npcId), true, false);
		}

		waveKillCount = 0;
		client.setTzhaarWaveId(waveId + 1);
	}

	@Override
	public boolean onDropItems(Client client, NPC npc) {
		if (npc.npcType == FightCavesDefinitions.TZ_TOK_JAD_ID) {
			finishMinigame(client);
			return false;
		}
		waveKillCount++;
		return false;

	}

	public void finishMinigame(Client client) {
		client.getItems().addItem(FightCavesDefinitions.FIRECAPE_ID, 1);
		client.exitMinigame();
	}
	
	public boolean shouldSpawnNextWave(Client client) {
		return currentWaveNpcs == null || waveKillCount >= currentWaveNpcs.length;
	}

}
