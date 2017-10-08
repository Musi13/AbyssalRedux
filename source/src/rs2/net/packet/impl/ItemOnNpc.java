package rs2.net.packet.impl;

import rs2.Server;
import rs2.abyssalps.model.items.UseItem;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;


public class ItemOnNpc implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();
		int i = c.getInStream().readSignedWordA();
		int slot = c.getInStream().readSignedWordBigEndian();
		int npcId = Server.npcHandler.npcs[i].npcType;
				if(!c.getItems().playerHasItem(itemId, 1, slot)) {
			return;
		}
		
		UseItem.ItemonNpc(c, itemId, npcId, slot);
	}
}
