package rs2.abyssalps.content.minigame.kraken;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rs2.Server;
import rs2.abyssalps.content.minigame.Minigame;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.combat.Damage;

public class KrakenMinigame implements Minigame {

	private NPC kraken;
	private NPC[] tentacles = new NPC[4];

	private List<KrakenTransformation> transformations = new ArrayList<>();

	private long killTime = Long.MAX_VALUE;

	@Override
	public boolean enter(Client client) {
		if (client.playerLevel[client.playerSlayer] < 87) {
			client.sendMessage("You need a slayer level of 87 to enter this area.");
			return false;
		}
		Position playerPos = KrakenConstants.KRAKEN_AREA_POSITION;
		client.getPA().movePlayer(playerPos.getX(), playerPos.getY(),
				client.getId() * 4);

		spawnNpcs(client);
		return true;
	}

	@Override
	public void exit(Client client) {
		removeNpcs();
	}

	@Override
	public void process(Client client) {
		if (client.playerLevel[3] <= 0 || client == null
				|| (kraken != null && client.heightLevel != kraken.heightLevel)) {
			client.exitMinigame();
		}

		if (kraken == null) {
			if (System.currentTimeMillis() - killTime >= KrakenConstants.RESPAWN_TIMER * 1000) {
				spawnNpcs(client);
			}
			return;
		}

		if (kraken.needRespawn) {
			removeNpcs();
			return;
		} else if (kraken.isDead) {
			killTime = System.currentTimeMillis();
			for (NPC tenacle : tentacles) {
				if (!tenacle.isVisible()) {
					NPCHandler.npcs[tenacle.npcId] = null;
				} else {
					tenacle.setVisible(false);
				}
			}
		}

		Iterator<KrakenTransformation> iterator = transformations.iterator();
		while (iterator.hasNext()) {
			KrakenTransformation transformation = iterator.next();
			if (!transformation.isFinished()) {
				transformation.process();
			} else {
				iterator.remove();
			}
		}
	}

	@Override
	public boolean onDamage(Client client, Damage damage) {
		if (damage.isPlayer()) {
			return false;
		}
		if (kraken.npcType == KrakenConstants.KRAKEN_ID) {
			return false;
		}
		for (NPC tenacle : tentacles) {
			if (tenacle.npcType != KrakenConstants.TENTACLE_POOL_ID) {
				continue;
			}
			if (tenacle.npcId != damage.getTarget()) {
				continue;
			}
			transformations.add(new KrakenTransformation(tenacle,
					KrakenConstants.TENTACLE_ID, -1, 2));
			client.getCombat().resetPlayerAttack();
			return true;
		}
		if (damage.getTarget() == kraken.npcId) {
			if (allTenaclesDisturbed()) {
				transformations.add(new KrakenTransformation(kraken,
						KrakenConstants.KRAKEN_ID, -1, 2));
			} else {
				client.sendMessage("Try disturbing other whirpools before disturbing this one.");
			}
			client.getCombat().resetPlayerAttack();
			return true;
		}
		return false;
	}

	public boolean allTenaclesDisturbed() {
		for (NPC tenacle : tentacles) {
			if (tenacle.npcType == KrakenConstants.TENTACLE_POOL_ID) {
				return false;
			}
		}
		return true;
	}

	public void spawnNpcs(Client client) {
		int heightLevel = client.getId() * 4;
		Position krakenPoolPos = KrakenConstants.KRAKEN_POOL_POSITION;

		kraken = Server.npcHandler.spawnNpc(client,
				KrakenConstants.KRAKEN_POOL_ID, krakenPoolPos.getX(),
				krakenPoolPos.getY(), heightLevel, 0,
				KrakenConstants.KRAKEN_HITPOINTS,
				KrakenConstants.KRAKEN_MAX_HIT, KrakenConstants.KRAKEN_ATTACK,
				KrakenConstants.KRAKEN_DEFENCE, false, false);

		for (int index = 0; index < tentacles.length; index++) {
			Position tentaclePos = KrakenConstants.TENTACLE_POOL_POSITIONS[index];
			tentacles[index] = Server.npcHandler.spawnNpc(client,
					KrakenConstants.TENTACLE_POOL_ID, tentaclePos.getX(),
					tentaclePos.getY(), heightLevel, 0,
					KrakenConstants.TENACLE_HITPOINTS,
					KrakenConstants.TENACLE_MAX_HIT,
					KrakenConstants.TENACLE_ATTACK,
					KrakenConstants.TENACLE_DEFENCE, false, false);

		}
	}

	public void removeNpcs() {
		if (kraken != null) {
			kraken.remove();
			kraken = null;
		}
		for (int i = 0; i < tentacles.length; i++) {
			if (tentacles[i] == null) {
				continue;
			}
			tentacles[i].remove();
			tentacles[i] = null;
		}

	}

}
