package rs2.net.packet.impl;

import java.util.Objects;

import rs2.Server;
import rs2.abyssalps.content.multiplayer.MultiplayerSession;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.net.packet.PacketType;
import rs2.util.Misc;

/**
 * Clicking stuff (interfaces)
 **/
public class ClickingStuff implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		if (c.tradeWith >= PlayerHandler.players.length || c.tradeWith < 0) {
			return;
		}
		if (!c.getCanWalk()) {
			return;
		}
		MultiplayerSession session = Server.getMultiplayerSessionListener()
				.getMultiplayerSession(c, MultiplayerSessionType.TRADE);
		if (session != null
				&& Server.getMultiplayerSessionListener().inSession(c,
						MultiplayerSessionType.TRADE)) {
			c.sendMessage("You have declined the trade.");
			session.getOther(c).sendMessage(
					c.playerName + " has declined the trade.");
			session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		DuelSession duelSession = (DuelSession) Server
				.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession)
				&& duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
			c.sendMessage("You have declined the duel.");
			duelSession.getOther(c).sendMessage(
					"The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		c.isBanking = false;
		c.isShopping = false;

	}

}
