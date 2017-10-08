package rs2.net.packet.impl;

import rs2.abyssalps.content.PotionDecanting;
import rs2.abyssalps.content.PotionDecanting.DecantingTable;
import rs2.abyssalps.content.consumables.Potions;
import rs2.abyssalps.content.skill.fletching.Fletching;

/**
 * @author Ryan / Lmctruck30
 */

import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.items.UseItem;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;

public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int usedWithSlot = c.getInStream().readUnsignedWord();
		int itemUsedSlot = c.getInStream().readUnsignedWordA();
		int useWith = c.playerItems[usedWithSlot] - 1;
		int itemUsed = c.playerItems[itemUsedSlot] - 1;
		if (!c.getItems().playerHasItem(useWith, 1, usedWithSlot)
				|| !c.getItems().playerHasItem(itemUsed, 1, itemUsedSlot)) {
			return;
		}
		if (Fletching.fletch(c, itemUsedSlot, usedWithSlot)) {
			return;
		}
		if (Potions.isPotion(itemUsed) && Potions.isPotion(useWith)) {
			GameItem lol = new GameItem(itemUsed, 1);
			GameItem lol1 = new GameItem(useWith, 1);
			PotionDecanting.decantPotion(c, lol, itemUsedSlot, lol1,
					usedWithSlot);
			return;
		}
		UseItem.ItemonItem(c, itemUsed, useWith);
	}

}
