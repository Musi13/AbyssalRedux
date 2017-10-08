package rs2.net.packet.impl;

import rs2.abyssalps.content.FlowerPoking;
import rs2.abyssalps.content.LootingBag;
import rs2.abyssalps.content.cluescroll.ClueScroll;
import rs2.abyssalps.content.consumables.Food;
import rs2.abyssalps.content.consumables.Potions;
import rs2.abyssalps.content.skill.prayer.BoneData;
import rs2.abyssalps.content.skill.prayer.BuryBones;
import rs2.abyssalps.content.skill.slayer.SlayerConstants;
import rs2.abyssalps.content.skill.slayer.SlayerTeleport;
import rs2.abyssalps.content.vote.RewardParser;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;

/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ClickItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int junk = c.getInStream().readSignedWordBigEndianA();
		int itemSlot = c.getInStream().readUnsignedWordA();
		int itemId = c.getInStream().readUnsignedWordBigEndian();
		if (itemId != c.playerItems[itemSlot] - 1) {
			return;
		}
		if (!c.getItems().playerHasItem(itemId, 1)) {
			return;
		}

		if (itemId == SlayerConstants.ENCHANTED_GEM_ID) {
			SlayerTeleport.activateEnchantedGem(c);
			return;
		}

		if (itemId == 6199) {
			RewardParser.open(c, itemId, itemSlot);
			return;
		}

		if (itemId == 299) {
			FlowerPoking.plantSeed(c, itemId, itemSlot);
			return;
		}

		if (itemId == 10228 || itemId == 2801 || itemId == 2722) {
			ClueScroll.open(c, itemId, itemSlot);
			return;
		}

		if (itemId == 11941) {
			LootingBag.check(c);
			return;
		}
		if (itemId == 13441) {
			Food.eatAnglerFish(c, itemId, itemSlot);
			return;
		}
		if (Food.isFood(itemId)) {
			Food.eatFood(c, itemId, itemSlot);
		}
		if (Potions.isPotion(itemId)) {
			Potions.drinkPotion(c, itemId, itemSlot);
		}
		if (BoneData.boneExist(itemId)) {
			BuryBones.buryBones(c, itemId, itemSlot);
			return;
		}
		if (itemId == 952) {
			c.getBarrows().spadeDigging();
			return;
		}
	}
}
