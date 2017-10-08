package rs2.net.packet;

import rs2.abyssalps.model.player.Client;


	
public interface PacketType {
	public void processPacket(Client c, int packetType, int packetSize);
}

