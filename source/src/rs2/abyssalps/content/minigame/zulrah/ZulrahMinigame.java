package rs2.abyssalps.content.minigame.zulrah;

import rs2.Server;
import rs2.abyssalps.content.minigame.Minigame;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.player.Client;
import rs2.util.CycledState;
import rs2.util.Misc;

public class ZulrahMinigame implements Minigame {

	private NPC zulrah;
	private int heighLevel;

	private int stateId;
	private CycledState[] states;
	private CycledState spawningState;

	private boolean initiated;

	@Override
	public boolean enter(Client client) {
		heighLevel = client.playerId * 4;
		int zulrahId = ZulrahConstants.GREEN_ZULRAH_ID;
		Position pos = ZulrahConstants.ZULRAH_NORTH_POSITION;
		zulrah = Server.npcHandler.spawnNpc(client, zulrahId, pos.getX(), pos.getY(), heighLevel, 0, 500, 41, 600, 300,
				true, false);
		zulrah.setCanAttack(false);
		zulrah.setVisible(false);
		states = ZulrahRotations.getRotationStates(zulrah);
		Position playerPos = ZulrahConstants.ZULRAH_ISLAND_POSITION;
		client.getPA().movePlayer(playerPos.getX(), playerPos.getY(), heighLevel);
		return true;
	}

	@Override
	public void exit(Client client) {
		if (NPCHandler.npcs[zulrah.npcId] == zulrah) {
			NPCHandler.npcs[zulrah.npcId] = null;
		}
		if (client == null) {
			return;
		}
		for (NPC npc : NPCHandler.npcs) {
			if (npc == null) {
				continue;
			}
			if (npc.npcType == ZulrahConstants.SNAKELING_ID && npc.spawnedBy == client.playerId) {
				NPCHandler.npcs[npc.npcId] = null;
			}
		}
	}

	@Override
	public void process(Client client) {
		if (!initiated) {
			initiated = true;
			spawningState = new ZulrahToxicClouds(zulrah, client);
			return;
		}

		if (client.distanceToPoint(zulrah.absX, zulrah.absY) > 30 || zulrah.needRespawn) {
			client.exitMinigame();
		}

		if (finishedRotating() && spawningState == null) {
			zulrah.setCanAttack(true);
		}

		if (spawningState != null && finishedRotating()) {
			if (!spawningState.isFinished()) {
				spawningState.process();
			} else {
				spawningState = null;
			}
			return;
		}

		if (zulrah.HP <= ZulrahConstants.HYBDRID_STATE_HITPOINTS && finishedRotating()) {
			stateId = states.length - 1;
		} else {
			int healthPerState = (ZulrahConstants.HITPOINTS - ZulrahConstants.HYBDRID_STATE_HITPOINTS)
					/ (states.length - 1);
			if (zulrah.HP - ZulrahConstants.HYBDRID_STATE_HITPOINTS < (ZulrahConstants.HITPOINTS
					- ZulrahConstants.HYBDRID_STATE_HITPOINTS) - (healthPerState * (stateId + 1))) {
				if (finishedRotating()) {
					stateId++;
				}
			}
		}

		if (!finishedRotating()) {
			zulrah.setCanAttack(false);
			states[stateId].process();
		} else if (zulrah.HP > ZulrahConstants.HYBDRID_STATE_HITPOINTS) {
			if (Misc.random(20) == 0) {
				if (Misc.random(10) < 5) {
					spawningState = new ZulrahSnakelings(zulrah, client);
				} else {
					spawningState = new ZulrahToxicClouds(zulrah, client);
				}
			}
		}

	}

	public boolean finishedRotating() {
		if (stateId >= states.length) {
			return true;
		}
		if (stateId < 0) {
			return true;
		}
		return states[stateId].isFinished();
	}

}