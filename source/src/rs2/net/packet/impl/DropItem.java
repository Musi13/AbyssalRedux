package rs2.net.packet.impl;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.content.BlowPipe;
import rs2.abyssalps.model.items.ItemAssistant;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerSave;
import rs2.net.packet.PacketType;
import rs2.util.Misc;
import rs2.util.tools.log.LogSystem;
import rs2.util.tools.log.LogTypes;

/**
 * Drop Item
 **/
public class DropItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int itemId = c.getInStream().readUnsignedWordA();
		c.getInStream().readUnsignedByte();
		c.getInStream().readUnsignedByte();
		int slot = c.getInStream().readUnsignedWordA();

		if (!c.getItems().playerHasItem(itemId)) {
			return;
		}

		if (c.isDead || c.playerLevel[3] <= 0) {
			return;
		}

		if (System.currentTimeMillis() - c.alchDelay < 1800) {
			return;
		}

		if (c.startTimer > 0) {
			c.sendMessage("Please wait 30 minutes before dropping items.");
			return;
		}

		if (c.arenas()) {
			c.sendMessage("You can't drop items inside the arena!");
			return;
		}
		if (c.inTrade) {
			c.sendMessage("You can't drop items while trading!");
			return;
		}

		if (itemId == 12926) {
			BlowPipe.unchargeBlowpipe(c);
			return;
		}

		if (c.getPets().dropPet(itemId)) {
			return;
		}

		boolean droppable = true;
		for (int i : Config.UNDROPPABLE_ITEMS) {
			if (i == itemId) {
				droppable = false;
				break;
			}
		}
		if (!c.getItems().tradeable(itemId)) {
			c.getItems().destroyItemInterface(itemId);
			c.interfaceAction = 17;
			return;
		}
		if (c.playerItemsN[slot] != 0 && itemId != -1
				&& c.playerItems[slot] == itemId + 1) {
			if (droppable) {
				Server.itemHandler.createGroundItem(c, itemId, c.getX(),
						c.getY(), c.heightLevel, c.playerItemsN[slot],
						c.getId());
				LogSystem.writeToFile(c.playerName,
						Misc.formatNumbers(c.playerItemsN[slot]) + " "
								+ ItemAssistant.getItemName(itemId),
						LogTypes.DROP);
				c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
			} else {
				c.sendMessage("This items cannot be dropped.");
			}
		}

	}
}
