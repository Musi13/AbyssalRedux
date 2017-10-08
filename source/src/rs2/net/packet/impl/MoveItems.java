package rs2.net.packet.impl;

import java.util.Objects;

import rs2.Server;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;

/**
 * Move Items
 **/
public class MoveItems implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readUnsignedWordBigEndianA();
		boolean insertMode = c.getInStream().readSignedByteC() == 1;
		int from = c.getInStream().readUnsignedWordBigEndianA();
		int to = c.getInStream().readUnsignedWordBigEndian();
		if (Server.getMultiplayerSessionListener().inSession(c,
				MultiplayerSessionType.TRADE)) {
			Server.getMultiplayerSessionListener().finish(c,
					MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			c.sendMessage("You cannot move items whilst trading.");
			return;
		}
		DuelSession duelSession = (DuelSession) Server
				.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession)
				&& duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
			c.sendMessage("You cannot move items right now.");
			return;
		}
		c.getItems().moveItems(from, to, interfaceId, insertMode);

	}
}
