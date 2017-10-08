package rs2.net.packet.impl;

import rs2.Server;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;
import rs2.util.Misc;

/**
 * Chat
 **/
public class ClanChat implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		String textSent = Misc.longToPlayerName2(c.getInStream().readQWord());
		textSent = textSent.replaceAll("_", " ");
	}
}
