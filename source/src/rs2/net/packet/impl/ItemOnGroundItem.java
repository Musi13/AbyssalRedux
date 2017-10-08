package rs2.net.packet.impl;

import rs2.Server;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;
import rs2.util.Misc;

public class ItemOnGroundItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int a1 = c.getInStream().readSignedWordBigEndian();
		int itemUsed = c.getInStream().readSignedWordA();
		int groundItem = c.getInStream().readUnsignedWord();
		int gItemY = c.getInStream().readSignedWordA();
		int itemUsedSlot = c.getInStream().readSignedWordBigEndianA();
		int gItemX = c.getInStream().readUnsignedWord();
		if (!c.getItems().playerHasItem(itemUsed, 1, itemUsedSlot)) {
			return;
		}
		if (!Server.itemHandler.itemExists(groundItem, gItemX, gItemY,
				c.heightLevel)) {
			return;
		}

		switch (itemUsed) {

		default:
			if (c.getRank() == 3)
				Misc.println("ItemUsed " + itemUsed + " on Ground Item "
						+ groundItem);
			break;
		}
	}

}
