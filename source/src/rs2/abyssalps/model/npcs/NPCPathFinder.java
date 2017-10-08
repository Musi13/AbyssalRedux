package rs2.abyssalps.model.npcs;

import rs2.abyssalps.model.player.Player;
import rs2.util.cache.region.Region;

public class NPCPathFinder {

	public static void walkTowards(NPC npc, Player player) {
		npc.randomWalk = false;
		if (npc.distanceTo(player) > 1) {
			_walkTowards(npc, player.absX, player.absY);
		}
		npc.facePlayer(player.getId());
	}

	public static void walkTowards(NPC npc, int destX, int destY) {
		if (npc.absX == destX && npc.absY == destY) {
			return;
		}

		/*
		 * East direction.
		 */
		if (destX > npc.absX) {
			/*
			 * Walk east
			 */
			if (destY == npc.absY) {
				if (!Region.blockedEastNPC(npc.absX, npc.absY, npc.heightLevel,
						npc.getSize())) {
					npc.moveX = 1;
					npc.moveY = 0;
				}
			}
			/*
			 * Walk north east.
			 */
			if (destY > npc.absY) {
				if (!Region.blockedNorthEastNPC(npc.absX, npc.absY,
						npc.heightLevel, npc.getSize())) {
					npc.moveX = 1;
					npc.moveY = 1;
				}
			}
			/*
			 * Walk south east.
			 */
			if (destY < npc.absY) {
				if (!Region.blockedSouthEastNPC(npc.absX, npc.absY,
						npc.heightLevel, npc.getSize())) {
					npc.moveX = 1;
					npc.moveY = -1;
				}
			}
		}
		/*
		 * West direction.
		 */
		if (destX < npc.absX) {
			/*
			 * Walk west.
			 */
			if (destY == npc.absY) {
				if (!Region.blockedWestNPC(npc.absX, npc.absY, npc.heightLevel,
						npc.getSize())) {
					npc.moveX = -1;
					npc.moveY = 0;
				}
			}
			/*
			 * Walk north west.
			 */
			if (destY > npc.absY) {
				if (!Region.blockedNorthWestNPC(npc.absX, npc.absY,
						npc.heightLevel, npc.getSize())) {
					npc.moveX = -1;
					npc.moveY = 1;
				}
			}
			/*
			 * Walk south west.
			 */
			if (destY < npc.absY) {
				if (!Region.blockedSouthWestNPC(npc.absX, npc.absY,
						npc.heightLevel, npc.getSize())) {
					npc.moveX = -1;
					npc.moveY = -1;
				}
			}
		}
		/*
		 * Vertical movement.
		 */
		if (destX == npc.absX) {
			/*
			 * Walk north.
			 */
			if (destY > npc.absY) {
				if (!Region.blockedNorthNPC(npc.absX, npc.absY,
						npc.heightLevel, npc.getSize())) {
					npc.moveX = 0;
					npc.moveY = 1;
				}
			}
			/*
			 * Walk south.
			 */
			if (destY < npc.absY) {
				if (!Region.blockedSouthNPC(npc.absX, npc.absY,
						npc.heightLevel, npc.getSize())) {
					npc.moveX = 0;
					npc.moveY = -1;
				}
			}
		}

		npc.getNextNPCMovement(npc.npcId);
	}

	private static int getCenterX(NPC npc, int destX) {
		int size = npc.getSize();
		int middlePoint = size / 2;
		if (npc.absX < destX) {
			return npc.absX + size - 1 - middlePoint;
		}
		return npc.absX + middlePoint;
	}

	private static int getCenterY(NPC npc, int destY) {
		int size = npc.getSize();
		int middlePoint = size / 2;
		if (npc.absY < destY) {
			return npc.absY + size - 1 - middlePoint;
		}
		return npc.absY + middlePoint;
	}

	public static void _walkTowards(NPC npc, int destX, int destY) {
		// System.out.println(npc.absX + " " + npc.absY + " " + destX + " " +
		// destY);
		if (npc.absX == destX && npc.absY == destY) {
			return;
		}
		int npcX = getCenterX(npc, destX);
		int npcY = getCenterY(npc, destY);
		if (npcX > destX
				&& !Region.blockedWest(npc.absX, npc.absY, npc.heightLevel,
						npc.getSize())) {
			npc.moveX = -1;
		}
		if (npcX < destX
				&& !Region.blockedEast(npc.absX, npc.absY, npc.heightLevel,
						npc.getSize())) {
			npc.moveX = 1;
		}
		if (npcY > destY
				&& !Region.blockedSouth(npc.absX, npc.absY, npc.heightLevel,
						npc.getSize())) {
			npc.moveY = -1;
		}
		if (npcY < destY
				&& !Region.blockedNorth(npc.absX, npc.absY, npc.heightLevel,
						npc.getSize())) {
			npc.moveY = 1;
		}
		npc.getNextNPCMovement(npc.npcId);
	}

}
