package rs2.abyssalps.model.items;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.content.BlowPipe;
import rs2.abyssalps.content.ItemCombining;
import rs2.abyssalps.content.ItemSets;
import rs2.abyssalps.content.LootingBag;
import rs2.abyssalps.content.MaxCape;
import rs2.abyssalps.content.PotionDecanting;
import rs2.abyssalps.content.PotionDecanting.DecantingTable;
import rs2.abyssalps.content.goodwill.WellOfGoodwill;
import rs2.abyssalps.content.minigame.warriorsguild.MagicalAnimator;
import rs2.abyssalps.content.skill.cooking.CookData;
import rs2.abyssalps.content.skill.cooking.Cooking;
import rs2.abyssalps.content.skill.crafting.GemCutting;
import rs2.abyssalps.content.skill.firemaking.Firemaking;
import rs2.abyssalps.content.skill.prayer.BoneData;
import rs2.abyssalps.content.skill.prayer.BonesOnAltar;
import rs2.abyssalps.content.skill.smithing.SmithingInterface;
import rs2.abyssalps.content.skill.smithing.impl.SmeltAction;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.objects.Object;
import rs2.util.Misc;

/**
 * 
 * @author Ryan / Lmctruck30
 *
 */

public class UseItem {

	public static void ItemonObject(Client c, int objectID, int objectX,
			int objectY, int itemId) {
		if (!c.getItems().playerHasItem(itemId, 1))
			return;

		if (itemId == 11941 && objectID == 11744) {
			LootingBag.withdraw(c);
			return;
		}

		if (objectID == 11744 && ItemSets.setExist(itemId)) {
			ItemSets.exchange(c, itemId);
			return;
		}

		if (objectID == 2030) {
			SmeltAction.showInterface(c, itemId);
			return;
		}

		if (objectID == MagicalAnimator.OBJECT_ID) {
			MagicalAnimator.animateArmor(c, itemId, objectX, objectY);
			return;
		}

		if (objectID == 2031) {
			c.turnPlayerTo(objectX, objectY);
			if (c.getItems().playerHasItem(2347, 1)) {
				if (itemId == 2349)
					new SmithingInterface(c, objectID, "BRONZE", objectX,
							objectY);
				else if (itemId == 2351)
					new SmithingInterface(c, objectID, "IRON", objectX, objectY);
				else if (itemId == 2359)
					new SmithingInterface(c, objectID, "MITH", objectX, objectY);
				else if (itemId == 2353)
					new SmithingInterface(c, objectID, "STEEL", objectX,
							objectY);
				else if (itemId == 2361)
					new SmithingInterface(c, objectID, "ADDY", objectX, objectY);
				else if (itemId == 2363)
					new SmithingInterface(c, objectID, "RUNE", objectX, objectY);
				else if (itemId == 11286)
					new SmithingInterface(c, objectID, "DRAGON", objectX,
							objectY);
				else if (itemId == 13746 || itemId == 13748 || itemId == 13750
						|| itemId == 13752)
					new SmithingInterface(c, objectID, "SPIRIT", objectX,
							objectY);
				else
					c.sendMessage("You can't use this item on an anvil!");
			} else {
				c.sendMessage("You need a hammer to make items!");
			}
		}

		switch (objectID) {

		case 409:
			if (BoneData.boneExist(itemId)) {
				BonesOnAltar.useBoneOnAltar(c, itemId, objectX, objectY);
			}
			break;

		case 114:
			if (!CookData.cookable(itemId)) {
				return;
			}
			c.turnPlayerTo(objectX, objectY);
			Cooking.showInterface(c, itemId);
			break;

		default:
			if (c.getRank() == 3)
				Misc.println("Player At Object id: " + objectID
						+ " with Item id: " + itemId);
			break;
		}

	}

	public static void ItemonItem(Client c, int itemUsed, int useWith) {
		if (!c.getItems().playerHasItem(itemUsed, 1)
				|| !c.getItems().playerHasItem(useWith, 1)) {
			return;
		}
		if (ItemDefinitions.forId(itemUsed).getName()
				.equalsIgnoreCase("Max hood")
				|| ItemDefinitions.forId(itemUsed).getName()
						.equalsIgnoreCase("Max cape")
				|| ItemDefinitions.forId(useWith).getName()
						.equalsIgnoreCase("Max hood")
				|| ItemDefinitions.forId(useWith).getName()
						.equalsIgnoreCase("Max cape")) {
			MaxCape.combineCapes(c, itemUsed, useWith);
			return;
		}
		if (itemUsed == 12934 && useWith == 12924) {
			BlowPipe.chargeBlowpipe(c);
			return;
		}
		if ((itemUsed == 12934 || useWith == 12934)
				&& (itemUsed == 12929 || useWith == 12929)) {
			if (c.getItems().getItemCount(12934) < 11_000) {
				c.sendMessage("You need 11,000 Zulrah's Scales to charge this item.");
				return;
			}
			c.getItems().deleteItem(12934, c.getItems().getItemSlot(12934),
					11_000);
			c.getItems().deleteItem(12929, 1);
			c.getItems().addItem(12931, 1);
			c.sendMessage("Your helmet has been fully charged.");
			return;
		}
		if (ItemDefinitions.forId(itemUsed).getName().endsWith("dart")
				&& useWith == 12926) {
			BlowPipe.loadBlowpipe(c, itemUsed);
			return;
		}
		if (itemUsed == 12922 && useWith == 1755 || itemUsed == 1755
				&& useWith == 12922) {
			if (c.playerLevel[c.playerCrafting] < 53) {
				c.sendMessage("You need 53 crafting to do this.");
				return;
			}
			c.getItems().deleteItem(12922, 1);
			c.getItems().addItem(12925, 1);
			c.getPA().addSkillXP(120 * 25, c.playerCrafting);
			c.getPA().refreshSkill(c.playerCrafting);
			c.sendMessage("You create a Toxic Blowpipe (Empty).");
			return;
		}
		if (itemUsed == 12927 && useWith == 1755 || itemUsed == 1755
				&& useWith == 12927) {
			if (c.playerLevel[c.playerCrafting] < 52) {
				c.sendMessage("You need 52 crafting to do this.");
				return;
			}
			c.getItems().deleteItem(12927, 1);
			c.getItems().addItem(12929, 1);
			c.getPA().addSkillXP(120 * 25, c.playerCrafting);
			c.getPA().refreshSkill(c.playerCrafting);
			c.sendMessage("You create a Serpentine Helm (Uncharged).");
			return;
		}
		if (itemUsed == 590) {
			Firemaking.execute(c, useWith);
		}
		if (useWith == 11941) {
			LootingBag.deposit(c, itemUsed, c.getItems().getItemSlot(itemUsed));
		}

		ItemCombining.combineItems(c, itemUsed, useWith);

		ItemCombining.combineItems_2(c, itemUsed, useWith);

		if (itemUsed == 1755) {
			GemCutting.resetAction(c);
			int[] gems = { 1623, 1621, 1619, 1617, 1631, 6571 };
			for (int id : gems) {
				if (useWith == id) {
					c.outStream.createFrame(27);
					c.setGemInterfaceState(true);
					c.setGemId(useWith);
				}
			}
		}

		switch (itemUsed) {

		default:
			if (c.getRank() == 3)
				Misc.println("Player used Item id: " + itemUsed
						+ " with Item id: " + useWith);
			break;
		}
	}

	public static void ItemonNpc(Client c, int itemId, int npcId, int slot) {
		switch (itemId) {

		default:
			if (c.getRank() == 3)
				Misc.println("Player used Item id: " + itemId
						+ " with Npc id: " + npcId + " With Slot : " + slot);
			break;
		}

	}

}
