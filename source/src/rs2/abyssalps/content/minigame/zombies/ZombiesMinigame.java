package rs2.abyssalps.content.minigame.zombies;

import java.util.ArrayList;
import java.util.List;

import rs2.Server;
import rs2.abyssalps.content.minigame.Minigame;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class ZombiesMinigame implements Minigame {

	private final int heightLevel;
	private final List<Client> players;
	private final List<NPC> zombies;

	private int currentWave;

	public ZombiesMinigame(List<Client> players) {
		this.players = new ArrayList<>(players);
		this.zombies = new ArrayList<>();
		if (!players.isEmpty()) {
			this.heightLevel = players.get(0).getId() * 4;
		} else {
			this.heightLevel = 0;
		}
	}

	@Override
	public boolean enter(Client client) {
		if (!players.contains(client)) {
			return false;
		}
		Position pos = ZombiesDefinitions.MINIGAME_POSITION;
		client.getPA().movePlayer(pos.getX(), pos.getY(), heightLevel);
		return true;
	}

	@Override
	public void exit(Client client) {
		players.remove(client);
	}

	@Override
	public void process(Client client) {
		if (zombies.isEmpty()) {
			currentWave++;
			spawnAllZombies();
		}
		
		sendInterface(client);
	}

	@Override
	public boolean onDropItems(Client client, NPC npc) {
		if (zombies.remove(npc)) {
			return true;
		}
		return false;
	}

	private void sendInterface(Client client) {
		client.getPA().sendFrame126("", 27120);
		client.getPA().sendFrame126("Wave " + currentWave, 27121);
		client.getPA().sendFrame126("Zombies left: " + zombies.size(), 27122);
		client.getPA().sendFrame126("", 27123);
		client.getPA().walkableInterface(27119);
	}
	
	private void spawnAllZombies() {
		for (Client client : players) {
			spawnZombies(client);
			removeDoors(client);
		}
	}

	private void spawnZombies(Client client) {
		int[] zombieIds = ZombiesDefinitions.EASY_ZOMBIE_IDS;
		if (client.combatLevel > 100) {
			zombieIds = ZombiesDefinitions.HARD_ZOMBIE_IDS;
		} else if (client.combatLevel > 70) {
			zombieIds = ZombiesDefinitions.MEDIUM_ZOMBIE_IDS;
		}
		for (int i = 0; i < currentWave * 3; i++) {
			int zombieId = zombieIds[Misc.random(zombieIds.length - 1)];
			Position position = ZombiesDefinitions.ZOMBIE_SPAWN_POSITIONS[Misc.random(ZombiesDefinitions.ZOMBIE_SPAWN_POSITIONS.length)];
			NPC zombie = Server.npcHandler.newNPC(zombieId, position.getX(), position.getY(), client.heightLevel, 1, ZombiesDefinitions.getHitpoints(zombieId),
					ZombiesDefinitions.getMaxHit(zombieId), ZombiesDefinitions.getAttack(zombieId), ZombiesDefinitions.getDefence(zombieId));
			zombie.setRespawnable(false);
			zombies.add(zombie);
		}
	}

	private void removeDoors(Client client) {
		for (Position pos : ZombiesDefinitions.DOOR_POSITIONS) {
			client.getPA().object(-1, pos.getX(), pos.getY(), 0, 0);
		}
	}
	
}