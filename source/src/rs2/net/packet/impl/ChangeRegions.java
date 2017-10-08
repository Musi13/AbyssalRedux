package rs2.net.packet.impl;

import rs2.Server;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;
import rs2.util.cache.region.Region;

/**
 * Change Regions
 */
public class ChangeRegions implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		if (packetType == 121)
			Region.load(c);
		
		//Server.objectHandler.updateObjects(c);
		
		Server.itemHandler.reloadItems(c);
		
		c.saveFile = true;
		
		if(c.skullTimer > 0) {
			c.isSkulled = true;	
			c.headIconPk = 0;
			c.getPA().requestUpdates();
		}

	}
		
}
