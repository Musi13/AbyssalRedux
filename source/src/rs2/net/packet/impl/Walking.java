package rs2.net.packet.impl;

import rs2.Server;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.content.multiplayer.duel.DuelSessionRules.Rule;
import rs2.abyssalps.model.Boundary;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.net.packet.PacketType;
import rs2.util.Misc;

/**
 * Walking packet
 **/
public class Walking implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		if (c.isDead || c.playerLevel[3] <= 0) {
			c.sendMessage("You are dead you cannot walk.");
			return;
		}
		if (!c.getCanWalk()) {
			return;
		}
		if (Server.getMultiplayerSessionListener().inSession(c,
				MultiplayerSessionType.TRADE)) {
			c.sendMessage("You must decline the trade to start walking.");
			return;
		}
		DuelSession session = (DuelSession) Server
				.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
		if (session != null
				&& session.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERACTION
				&& !Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
			if (session.getRules().contains(Rule.NO_MOVEMENT)) {
				Client opponent = session.getOther(c);
				if (Boundary.isIn(opponent, session.getArenaBoundary())) {
					c.getPA().movePlayer(opponent.getX(), opponent.getY() - 1,
							0);
				} else {
					int x = session.getArenaBoundary().getMinimumX() + 6
							+ Misc.random(12);
					int y = session.getArenaBoundary().getMinimumY() + 1
							+ Misc.random(11);
					c.getPA().movePlayer(x, y, 0);
					opponent.getPA().movePlayer(x, y - 1, 0);
				}
			} else {
				c.getPA().movePlayer(
						session.getArenaBoundary().getMinimumX() + 6
								+ Misc.random(12),
						session.getArenaBoundary().getMinimumY() + 1
								+ Misc.random(11), 0);
			}
			return;
		}
		if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
			if (session == null) {
				c.getPA().movePlayer(3362, 3264, 0);
				return;
			}
			if (session.getRules().contains(Rule.NO_MOVEMENT)) {
				c.sendMessage("Movement has been disabled for this duel.");
				return;
			}
		}
		if (session != null
				&& session.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& session.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
			c.sendMessage("You have declined the duel.");
			session.getOther(c).sendMessage(
					"The challenger has declined the duel.");
			session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}

		if (packetType == 248 || packetType == 164) {
			c.faceUpdate(0);
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.followingX = 0;
			c.followingY = 0;
			if (c.followId > 0 || c.followId2 > 0) {
				c.getPA().resetFollow();
			}
		}

		c.clickNpcType = 0;
		c.clickObjectType = 0;
		if (c.isBanking)
			c.isBanking = false;
		c.getPA().removeAllWindows();
		if (c.duelRule[1] && c.duelStatus == 5) {
			if (PlayerHandler.players[c.duelingWith] != null) {
				if (!c.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.duelingWith].getX(),
						PlayerHandler.players[c.duelingWith].getY(), 1)
						|| c.attackTimer == 0) {
					c.sendMessage("Walking has been disabled in this duel!");
				}
			}
			c.playerIndex = 0;
			return;
		}

		if (c.freezeTimer > 0) {
			if (PlayerHandler.players[c.playerIndex] != null) {
				if (c.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.playerIndex].getX(),
						PlayerHandler.players[c.playerIndex].getY(), 1)
						&& packetType != 98) {
					c.playerIndex = 0;
					return;
				}
			}
			if (packetType != 98) {
				c.sendMessage("A magical force stops you from moving.");
				c.playerIndex = 0;
			}
			return;
		}

		if (System.currentTimeMillis() - c.lastSpear < 4000) {
			c.sendMessage("You have been stunned.");
			c.playerIndex = 0;
			return;
		}

		if (packetType == 98) {
			c.mageAllowed = true;
		}

		if (c.respawnTimer > 3) {
			return;
		}
		if (c.inTrade) {
			return;
		}
		if (c.blockWalking()) {
			return;
		}
		if (packetType == 98) {
			c.mageAllowed = true;
		}
		if (c.respawnTimer > 3) {
			return;
		}
		if (packetType == 248) {
			packetSize -= 14;
		}

		int newWalkCmdSteps = (packetSize - 5) / 2;
		if (++newWalkCmdSteps > c.walkingQueueSize) {
			newWalkCmdSteps = 0;
			return;
		}

		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;

		int firstStepX = c.getInStream().readSignedWordBigEndianA()
				- c.getMapRegionX() * 8;
		for (int i = 1; i < newWalkCmdSteps; i++) {
			c.getNewWalkCmdX()[i] = c.getInStream().readSignedByte();
			c.getNewWalkCmdY()[i] = c.getInStream().readSignedByte();
		}

		int firstStepY = c.getInStream().readSignedWordBigEndian()
				- c.getMapRegionY() * 8;
		c.setNewWalkCmdIsRunning(c.getInStream().readSignedByteC() == 1);
		for (int i1 = 0; i1 < newWalkCmdSteps; i1++) {
			c.getNewWalkCmdX()[i1] += firstStepX;
			c.getNewWalkCmdY()[i1] += firstStepY;
		}
		c.pathX = c.getNewWalkCmdX()[newWalkCmdSteps - 1] + c.mapRegionX * 8;
		c.pathY = c.getNewWalkCmdY()[newWalkCmdSteps - 1] + c.mapRegionY * 8;

		c.otherDirection = Misc.direction1(c.absX, c.absY, c.pathX, c.pathY);

		c.getPA().playerWalk(c.pathX, c.pathY, false);
	}
}
