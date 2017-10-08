package rs2.net.packet.impl;

import rs2.Connection;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;
import rs2.sanction.SanctionHandler;

/**
 * Chat
 **/
public class Chat implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		if (SanctionHandler.isMuted(c) || SanctionHandler.isIPMuted(c)) {
			return;
		}
		c.setChatTextEffects(c.getInStream().readUnsignedByteS());
		c.setChatTextColor(c.getInStream().readUnsignedByteS());
		c.setChatTextSize((byte) (c.packetSize - 2));
		c.inStream.readBytes_reverseA(c.getChatText(), c.getChatTextSize(), 0);
		c.setChatTextUpdateRequired(true);
	}
}
