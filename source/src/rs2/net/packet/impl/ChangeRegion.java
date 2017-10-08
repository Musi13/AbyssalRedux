package rs2.net.packet.impl;

import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;

public class ChangeRegion implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.getPA().removeObjects();
		//Server.objectManager.loadObjects(c);
	}

}
