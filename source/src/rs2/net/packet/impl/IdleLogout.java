package rs2.net.packet.impl;


import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;


public class IdleLogout implements PacketType {
	
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		//if (!c.playerName.equalsIgnoreCase("Sanity"))
			//c.logout();
	}
}