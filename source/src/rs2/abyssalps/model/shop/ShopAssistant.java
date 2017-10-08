package rs2.abyssalps.model.shop;

import java.util.ArrayList;
import java.util.List;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.items.Item;
import rs2.abyssalps.model.items.ItemAssistant;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.Misc;
import rs2.world.ItemHandler;
import rs2.world.ShopHandler;

public class ShopAssistant {

	private Client c;

	public ShopAssistant(Client client) {
		this.c = client;
	}

	public boolean shopSellsItem(int itemID) {
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Shops
	 **/

	public void openShop(int ShopID) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.getItems().resetItems(3823);
		resetShop(ShopID);
		c.isShopping = true;
		c.myShopId = ShopID;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendFrame126(ShopHandler.ShopName[ShopID], 3901);
	}

	public void updatePlayerShop() {
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].isShopping == true
						&& PlayerHandler.players[i].myShopId == c.myShopId
						&& i != c.playerId) {
					PlayerHandler.players[i].updateShop = true;
				}
			}
		}
	}

	public void updateshop(int i) {
		resetShop(i);
	}

	public void resetShop(int ShopID) {

		int TotalItems = 0;
		for (int i = 0; i < ShopHandler.MaxShopItems; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0) {
				TotalItems++;
			}
		}
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(3900);
		c.getOutStream().writeWord(TotalItems);
		int TotalCount = 0;
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0
					|| i <= ShopHandler.ShopItemsStandard[ShopID]) {
				if (ShopHandler.ShopItemsN[ShopID][i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(
							ShopHandler.ShopItemsN[ShopID][i]);
				} else {
					c.getOutStream().writeByte(
							ShopHandler.ShopItemsN[ShopID][i]);
				}
				if (ShopHandler.ShopItems[ShopID][i] > Config.ITEM_LIMIT
						|| ShopHandler.ShopItems[ShopID][i] < 0) {
					ShopHandler.ShopItems[ShopID][i] = Config.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(
						ShopHandler.ShopItems[ShopID][i]);
				TotalCount++;
			}
			if (TotalCount > TotalItems) {
				break;
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public double getItemShopValue(int ItemID, int Type, int fromSlot) {
		double ShopValue = 1;
		ShopValue = ItemDefinitions.forId(ItemID).getPrice();
		ShopValue -= ShopValue * .25;
		return ShopValue;
	}

	public int getItemShopValue(int itemId) {
		if (itemId > -1) {
			return ItemDefinitions.forId(itemId).getPrice();
		}
		return 0;
	}

	/**
	 * buy item from shop (Shop Price)
	 **/

	public void buyFromShopPrice(int removeId, int removeSlot) {
		int ShopValue = (int) Math.floor(getItemShopValue(removeId, 0,
				removeSlot));
		ShopValue *= 1.15;
		String ShopAdd = "";
		if (c.myShopId == 2) {
			c.sendMessage(ItemAssistant.getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " assault points.");
			return;
		}
		if (c.myShopId == 8) {
			c.sendMessage(ItemAssistant.getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " wilderness points.");
			return;
		}
		if (c.myShopId == 9 || c.myShopId == 10) {
			c.sendMessage(ItemAssistant.getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " AbyssalPS Tokens.");
			return;
		}
		if (c.myShopId == 12) {
			c.sendMessage(ItemAssistant.getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " slayer points.");
			return;
		}
		if (c.myShopId == 13) {
			c.sendMessage(ItemAssistant.getItemName(removeId)
					+ ": currently costs " + getSpecialItemValue(removeId)
					+ " achievement points.");
			return;
		}
		c.sendMessage(ItemAssistant.getItemName(removeId)
				+ ": currently costs "
				+ Misc.formatNumbers(c.getShops().getItemShopValue(removeId))
				+ " coins" + ShopAdd);
	}

	public int getSpecialItemValue(int id) {
		if (c.myShopId == 13) {
			switch (id) {
			case 13116:
				return 30;

			case 12441:
			case 12443:
			case 12351:
				return 15;

			case 12249:
			case 12251:
			case 12361:
			case 12428:
				return 20;

			case 12608:
			case 12610:
			case 12612:
				return 25;
			}
		}
		if (c.myShopId == 12) {
			switch (id) {

			case 11864:
			case 11824:
				return 600;

			case 2572:
				return 800;

			case 11866:
				return 75;

			case 11770:
			case 11771:
			case 11772:
			case 11773:
				return 350;

			default:
				return 0;
			}
		}
		if (c.myShopId == 9 || c.myShopId == 10) {
			switch (id) {

			case 11919:
			case 12956:
			case 12957:
			case 12958:
			case 12959:
			case 12848:
				return 3;

			case 4151:
			case 11235:
			case 12771:
			case 12769:
				return 5;

			case 10551:
			case 10548:
				return 6;

			case 12954:
				return 7;

			case 12873:
			case 12875:
			case 12877:
			case 12879:
			case 12881:
				return 8;

			case 12363:
			case 11836:
				return 10;

			case 12365:
			case 11826:
			case 9731:
			case 2890:
			case 13235:
			case 13237:
			case 13239:
			case 10336:
			case 10344:
			case 12806:
			case 12807:
				return 15;

			case 10334:
			case 10342:
			case 10350:
			case 10352:
				return 17;

			case 12367:
			case 11834:
			case 11832:
			case 11828:
			case 11830:
			case 10330:
			case 10332:
			case 10338:
			case 10340:
			case 10348:
			case 10346:
			case 11283:
				return 20;

			case 1050:
			case 11838:
				return 30;

			case 11804:
			case 11806:
			case 11808:
			case 11785:
				return 40;

			case 11802:
				return 50;

			case 1053:
			case 1055:
			case 1057:
			case 12422:
			case 12424:
			case 12426:
				return 40;

			case 13576:
			case 12369:
			case 13265:
				return 60;

			case 1038:
			case 1040:
			case 1042:
			case 1044:
			case 1046:
			case 1048:
				return 50;

			case 11862:
			case 11863:
			case 12399:
				return 70;

			case 13175:
				return 120;

			case 13173:
				return 300;
			}
		}
		if (c.myShopId == 8) {
			switch (id) {
			case 12695:
				return 1;

			case 12848:
			case 12530:
			case 12528:
				return 10;

			case 4151:
			case 12877:
			case 12879:
			case 12881:
			case 12883:
				return 15;

			case 12771:
			case 12769:
			case 12802:
			case 11773:
			case 11772:
			case 11771:
			case 11770:
			case 10887:
				return 20;

			case 12851:
				return 40;

			case 12804:
				return 50;

			case 11804:
			case 11806:
			case 11808:
				return 75;

			case 11802:
				return 85;

			case 13576:
				return 100;
			}
		}
		if (c.myShopId == 2) {
			switch (id) {

			case 7458:
			case 8848:
				return 20;

			case 7461:
			case 8849:
			case 10499:
				return 25;

			case 7462:
			case 8850:
				return 30;

			case 10551:
			case 10548:
				return 35;

			case 10887:
			case 12954:
				return 40;

			case 13124:
				return 50;

			default:
				return 0;
			}
		}
		switch (id) {
		}
		return 0;
	}

	/**
	 * Sell item to shop (Shop Price)
	 **/
	public void sellToShopPrice(int removeId, int removeSlot) {
		for (int i : Config.ITEM_SELLABLE) {
			if (i == removeId) {
				c.sendMessage("You can't sell "
						+ ItemAssistant.getItemName(removeId).toLowerCase()
						+ ".");
				return;
			}
		}
		boolean IsIn = false;
		if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
			for (int j = 0; j <= ShopHandler.ShopItemsStandard[c.myShopId]; j++) {
				if (removeId == (ShopHandler.ShopItems[c.myShopId][j] - 1)) {
					IsIn = true;
					break;
				}
			}
		} else {
			IsIn = true;
		}
		if (IsIn == false) {
			c.sendMessage("You can't sell "
					+ ItemAssistant.getItemName(removeId).toLowerCase()
					+ " to this store.");
		} else {
			int ShopValue = (int) Math.floor(getItemShopValue(removeId, 1,
					removeSlot));
			String ShopAdd = "";
			if (ShopValue >= 1000 && ShopValue < 1000000) {
				ShopAdd = " (" + (ShopValue / 1000) + "K)";
			} else if (ShopValue >= 1000000) {
				ShopAdd = " (" + (ShopValue / 1000000) + " million)";
			}
			c.sendMessage(ItemAssistant.getItemName(removeId)
					+ ": shop will buy for " + Misc.formatNumbers(ShopValue)
					+ " coins" + ShopAdd);
		}
	}

	public boolean sellItem(int itemID, int fromSlot, int amount) {
		/*if (!c.isShopping) {
			return false;
		}*/
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return false;
		}
		if (c.myShopId == 2 || c.myShopId == 8 || c.myShopId == 9
				|| c.myShopId == 10 || c.myShopId == 12 || c.myShopId == 13
				|| c.myShopId == 14) {
			return false;
		}
		for (int i : Config.ITEM_SELLABLE) {
			if (i == itemID) {
				c.sendMessage("You can't sell "
						+ ItemAssistant.getItemName(itemID).toLowerCase() + ".");
				return false;
			}
		}
		if (c.getRank() == 2 && !Config.ADMIN_CAN_SELL_ITEMS) {
			c.sendMessage("Selling items as an admin has been disabled.");
			return false;
		}

		if (amount > 0 && itemID == (c.playerItems[fromSlot] - 1)) {
			if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
				boolean IsIn = false;
				for (int i = 0; i <= ShopHandler.ShopItemsStandard[c.myShopId]; i++) {
					if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
						IsIn = true;
						break;
					}
				}
				if (IsIn == false) {
					c.sendMessage("You can't sell "
							+ ItemAssistant.getItemName(itemID).toLowerCase()
							+ " to this store.");
					return false;
				}
			}

			if (amount > c.playerItemsN[fromSlot]
					&& (Item.itemIsNote[(c.playerItems[fromSlot] - 1)] == true || Item.itemStackable[(c.playerItems[fromSlot] - 1)] == true)) {
				amount = c.playerItemsN[fromSlot];
			} else if (amount > c.getItems().getItemAmount(itemID)
					&& Item.itemIsNote[(c.playerItems[fromSlot] - 1)] == false
					&& Item.itemStackable[(c.playerItems[fromSlot] - 1)] == false) {
				amount = c.getItems().getItemAmount(itemID);
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			// int Overstock;
			for (int i = amount; i > 0; i--) {
				if (c.getItems().freeSlots() > 0
						|| c.getItems().playerHasItem(995)) {
					if (Item.itemIsNote[itemID] == false) {
						c.getItems().deleteItem(itemID,
								c.getItems().getItemSlot(itemID), 1);
					} else {
						c.getItems().deleteItem(itemID, fromSlot, 1);
					}
					c.getItems().addItem(995,
							(int) getItemShopValue(itemID, 1, fromSlot));
					addShopItem(itemID, 1);
				} else {
					c.sendMessage("You don't have enough space in your inventory.");
					break;
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return true;
	}

	public boolean addShopItem(int itemID, int amount) {
		boolean Added = false;
		if (amount <= 0) {
			return false;
		}
		if (c.myShopId == 1) {
			return false;
		}
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if ((ShopHandler.ShopItems[c.myShopId][i] - 1) == itemID) {
				ShopHandler.ShopItemsN[c.myShopId][i] += amount;
				Added = true;
			}
		}
		if (Added == false) {
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[c.myShopId][i] == 0) {
					ShopHandler.ShopItems[c.myShopId][i] = (itemID + 1);
					ShopHandler.ShopItemsN[c.myShopId][i] = amount;
					ShopHandler.ShopItemsDelay[c.myShopId][i] = 0;
					break;
				}
			}
		}
		return true;
	}

	public boolean buyItem(int itemID, int fromSlot, int amount) {
		/*if (!c.isShopping) {
			return false;
		}*/
		if (Server.getMultiplayerSessionListener().inAnySession(c))
			return false;
		if (c.myShopId == 14) {
			skillBuy(itemID);
			return false;
		}
		if (c.myShopId == 16) {
			c.getShops().buyFromZulrahExchange(itemID, amount);
			return false;
		}
		if (!shopSellsItem(itemID))
			return false;
		if (amount > 0) {
			if (amount > ShopHandler.ShopItemsN[c.myShopId][fromSlot]) {
				amount = ShopHandler.ShopItemsN[c.myShopId][fromSlot];
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			// int Overstock;
			int Slot = 0;
			int Slot1 = 0;// Tokkul
			int Slot2 = 0;// Pking Points
			if (c.myShopId == 2 || c.myShopId == 8 || c.myShopId == 9
					|| c.myShopId == 10 || c.myShopId == 12 || c.myShopId == 13) {
				handleOtherShop(itemID);
				return false;
			}
			for (int i = amount; i > 0; i--) {
				Slot = c.getItems().getItemSlot(995);
				Slot1 = c.getItems().getItemSlot(6529);
				if (Slot == -1 && c.myShopId != 29 && c.myShopId != 30
						&& c.myShopId != 31) {
					c.sendMessage("You don't have enough coins.");
					break;
				}
				if (Slot1 == -1 && c.myShopId == 29 || c.myShopId == 30
						|| c.myShopId == 31) {
					c.sendMessage("You don't have enough tokkul.");
					break;
				}
				if (c.myShopId != 29 || c.myShopId != 30 || c.myShopId != 31) {
					if (c.playerItemsN[Slot] >= c.getShops().getItemShopValue(
							itemID)) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(995,
									c.getItems().getItemSlot(995),
									c.getShops().getItemShopValue(itemID));
							c.getItems().addItem(itemID, 1);
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough coins.");
						break;
					}
				}
				if (c.myShopId == 29 || c.myShopId == 30 || c.myShopId == 31) {
					if (c.playerItemsN[Slot1] >= TotPrice2) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(6529,
									c.getItems().getItemSlot(6529), TotPrice2);
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
								ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough tokkul.");
						break;
					}
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return false;
	}

	public void handleOtherShop(int itemID) {
		if (c.myShopId == 2) {
			if (c.getPoints()[0] >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getPoints()[0] -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough assault points to buy this item.");
			}
		} else if (c.myShopId == 8) {
			if (c.getPoints()[3] >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getPoints()[3] -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough wilderness points to buy this item.");
			}
		} else if (c.myShopId == 9 || c.myShopId == 10) {
			if (c.getPoints()[2] >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getPoints()[2] -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough AbyssalPS Tokens to buy this item.");
			}
		} else if (c.myShopId == 12) {
			if (c.getSlayerData()[2] >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getSlayerData()[2] -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough slayer points to buy this item.");
			}
		} else if (c.myShopId == 13) {
			if (c.getPoints()[4] >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getPoints()[4] -= getSpecialItemValue(itemID);
					c.getItems().addItem(itemID, 1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough achievement points to buy this item.");
			}
		}
	}

	public void openSkillCape() {
		int capes = get99Count();
		if (capes > 1)
			capes = 1;
		else
			capes = 0;
		c.myShopId = 14;
		setupSkillCapes(capes, get99Count());
	}

	public int[] skillCapes = { 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801,
			9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786,
			9810, 9765 };

	public int get99Count() {
		int count = 0;
		for (int j = 0; j < c.playerLevel.length; j++) {
			if (c.getLevelForXP(c.playerXP[j]) >= 99) {
				count++;
			}
		}
		return count;
	}

	private List<GameItem> zulrahItems = new ArrayList<>();

	public void setupZulrahExchange() {
		c.getItems().resetItems(3823);
		c.isShopping = true;
		c.myShopId = 16;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendFrame126("Priestess Zul-Gwenwynig' Exchange", 3901);
		int totalItems = 0;
		for (int i = 0; i < c.lostItems.length; i++) {
			if (c.lostItems[i] > 0) {
				if (zulrahItems != null && zulrahItems.get(i) != null) {
					continue;
				}
				zulrahItems.add(new GameItem(c.lostItems[i], c.lostItemsN[i]));
			}
		}
		for (int i = 0; i < c.lostEquipment.length; i++) {
			if (c.lostEquipment[i] > 0) {
				if (zulrahItems != null && zulrahItems.get(i) != null) {
					continue;
				}
				zulrahItems.add(new GameItem(c.lostEquipment[i],
						c.lostEquipmentN[i]));
			}
		}
		for (int i = 0; i < zulrahItems.size(); i++) {
			if (zulrahItems.get(i) == null) {
				continue;
			}
			totalItems++;
		}
		if (totalItems > ShopHandler.MaxShopItems) {
			totalItems = ShopHandler.MaxShopItems;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(3900);
		c.getOutStream().writeWord(totalItems);
		for (int i = 0; i < zulrahItems.size(); i++) {
			System.out.println(ItemDefinitions.forId(zulrahItems.get(i).id)
					.getName() + " - " + zulrahItems.get(i).amount);

			if (zulrahItems.get(i).amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(zulrahItems.get(i).amount);
			} else {
				c.getOutStream().writeByte(zulrahItems.get(i).amount);
			}
			if (zulrahItems.get(i).id > Config.ITEM_LIMIT
					|| zulrahItems.get(i).id < 0) {
				zulrahItems.get(i).id = Config.ITEM_LIMIT;
			}
			c.getOutStream().writeWordBigEndianA(zulrahItems.get(i).id);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public void setupSkillCapes(int capes, int capes2) {

		c.getItems().resetItems(3823);
		c.isShopping = true;
		c.myShopId = 14;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendFrame126("Skillcape Shop", 3901);

		int TotalItems = 0;
		TotalItems = capes2;
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(3900);
		c.getOutStream().writeWord(TotalItems);
		for (int i = 0; i < 21; i++) {
			if (c.getLevelForXP(c.playerXP[i]) < 99)
				continue;
			c.getOutStream().writeByte(1);
			c.getOutStream().writeWordBigEndianA(skillCapes[i] + 2);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public void buyFromZulrahExchange(int item, int amount) {
		int slot = c.getItems().getItemSlot(995);
		for (int index = 0; index < zulrahItems.size(); index++) {
			if (amount > zulrahItems.get(index).amount) {
				amount = zulrahItems.get(index).amount;
			}
			if (slot == -1) {
				c.sendMessage("You don't have enough coins.");
				break;
			}
			if (zulrahItems.get(index).id == item) {
				for (int i = amount; i > 0; i--) {
					if (c.playerItemsN[slot] >= c.getShops().getItemShopValue(
							item)) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(995,
									c.getItems().getItemSlot(995),
									c.getShops().getItemShopValue(item));
							c.getItems().addItem(item, 1);
							zulrahItems.remove(index);
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough coins.");
						break;
					}
				}
			}
		}
		c.getItems().resetItems(3823);
	}

	public void skillBuy(int item) {
		int nn = get99Count();
		if (nn > 1)
			nn = 1;
		else
			nn = 0;
		for (int j = 0; j < skillCapes.length; j++) {
			if (skillCapes[j] == item || skillCapes[j] + 1 == item) {
				if (c.getItems().freeSlots() > 1) {
					if (c.getItems().playerHasItem(995,
							ItemDefinitions.forId(item).getPrice())) {
						if (c.getLevelForXP(c.playerXP[j]) >= 99) {
							c.getItems().deleteItem(995,
									c.getItems().getItemSlot(995), 99000);
							c.getItems().addItem(skillCapes[j] + nn, 1);
							c.getItems().addItem(skillCapes[j] + 2, 1);
						} else {
							c.sendMessage("You must have 99 in the skill of the cape you're trying to buy.");
						}
					} else {
						c.sendMessage("You need "
								+ ItemDefinitions.forId(item).getPrice()
								+ " to buy this item.");
					}
				} else {
					c.sendMessage("You must have at least 1 inventory spaces to buy this item.");
				}
			}
		}
		c.getItems().resetItems(3823);
	}
}
