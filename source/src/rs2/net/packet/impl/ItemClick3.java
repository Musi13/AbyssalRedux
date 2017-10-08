package rs2.net.packet.impl;

import rs2.abyssalps.content.BlowPipe;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;
import rs2.util.Misc;

/**
 * Item Click 3 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick3 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readSignedWordBigEndianA();
		int itemSlot = c.getInStream().readSignedWordA();
		int itemId = c.getInStream().readSignedWordA();
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}

		if (c.getPA().getDegradedGloryId(itemId) != itemId) {
			c.getPA().handleGlory(itemId, c.getItems().getItemSlot(itemId), false);
		}

		switch (itemId) {

		case 12926:
			BlowPipe.unloadBlowpipe(c);
			break;

		default:
			if (c.getRank() == 8)
				Misc.println(c.playerName + " - Item3rdOption: " + itemId + " : " + interfaceId + " : " + itemSlot);
			break;
		}

	}

}
