package rs2.net.packet.impl;

import java.util.Objects;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.content.LootingBag;
import rs2.abyssalps.content.MaxCape;
import rs2.abyssalps.content.WildyTeleports;
import rs2.abyssalps.content.bank.BankItem;
import rs2.abyssalps.content.bank.BankTab;
import rs2.abyssalps.content.minigame.kraken.KrakenConstants;
import rs2.abyssalps.content.minigame.zulrah.ZulrahConstants;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.content.multiplayer.duel.DuelSessionRules.Rule;
import rs2.abyssalps.content.skill.cooking.Cooking;
import rs2.abyssalps.content.skill.magic.TeleportType;
import rs2.abyssalps.content.skill.slayer.Slayer;
import rs2.abyssalps.content.skill.slayer.SlayerConstants;
import rs2.abyssalps.content.skill.slayer.SlayerTeleport;
import rs2.abyssalps.content.skill.smithing.impl.SmeltAction;
import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.items.ItemAssistant;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;
import rs2.util.Misc;

/**
 * Clicking most buttons
 **/
public class ClickingButtons implements PacketType {

	private static void resetMagicBook(Client c) {
		if (c.playerMagicBook == 0) {
			c.setSidebarInterface(6, 1151); // modern
		} else if (c.playerMagicBook == 2) {
			c.setSidebarInterface(6, 29999); // lunar
		} else if (c.playerMagicBook == 1) {
			c.setSidebarInterface(6, 12855); // ancient
		}
		c.getPA().resetAutocast();
	}

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int actionButtonId = Misc.hexToInt(c.getInStream().buffer, 0,
				packetSize);
		if (!c.getCanWalk()) {
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0) {
			return;
		}
		if (c.getRank() == 8) {
			Misc.println(c.playerName + " - actionbutton: " + actionButtonId);
		}
		if (actionButtonId >= 113234 && actionButtonId <= 114080) {
			c.getQuestTab().select(actionButtonId - 113234);
			return;
		}

		if (c.getInterfaces().handleButton(actionButtonId)) {
			return;
		}

		TeleportType teleportType = c.playerMagicBook == 1 ? TeleportType.ANCIENT
				: TeleportType.MODERN;
		DuelSession duelSession = null;
		switch (actionButtonId) {

		case 55095:
			c.getItems().destroyItem();
			break;

		case 55096:
			c.getPA().removeAllWindows();
			break;

		case 29024:
			SmeltAction.execute(c, 10, 2363);
			break;

		case 29025:
			SmeltAction.execute(c, 5, 2363);
			break;

		case 29026:
			SmeltAction.execute(c, 1, 2363);
			break;

		case 29019:
			SmeltAction.execute(c, 10, 2361);
			break;

		case 29020:
			SmeltAction.execute(c, 5, 2361);
			break;

		case 29022:
			SmeltAction.execute(c, 1, 2361);
			break;

		case 24253:
			SmeltAction.execute(c, 10, 2359);
			break;

		case 29016:
			SmeltAction.execute(c, 5, 2359);
			break;

		case 29017:
			SmeltAction.execute(c, 1, 2359);
			break;

		case 15157:
			SmeltAction.execute(c, 10, 2353);
			break;

		case 15158:
			SmeltAction.execute(c, 5, 2353);
			break;

		case 15159:
			SmeltAction.execute(c, 1, 2353);
			break;

		case 15146:
			SmeltAction.execute(c, 5, 2349);
			break;

		case 15147:
			SmeltAction.execute(c, 1, 2349);
			break;

		case 10247:
			SmeltAction.execute(c, 10, 2349);
			break;

		case 15149:
			SmeltAction.execute(c, 10, 2351);
			break;

		case 15150:
			SmeltAction.execute(c, 5, 2351);
			break;

		case 15151:
			SmeltAction.execute(c, 1, 2351);
			break;

		case 58074:
			c.getBankPin().close();
			break;

		case 58025:
		case 58026:
		case 58027:
		case 58028:
		case 58029:
		case 58030:
		case 58031:
		case 58032:
		case 58033:
		case 58034:
			c.getBankPin().pinEnter(actionButtonId);
			break;

		case 226154:
			c.takeAsNote = !c.takeAsNote;// rerun that
			break;

		/**
		 * Cooking
		 */

		case 53149:
			if (c.skillActive()[c.playerCooking]) {
				c.setCookAmount(c.getItems().getItemCount(c.getCookItem()));
				Cooking.cookItem(c, c.getCookItem());
			}
			break;

		case 53151:
			if (c.skillActive()[c.playerCooking]) {
				c.setCookAmount(5);
				Cooking.cookItem(c, c.getCookItem());
			}
			break;

		case 53152:
			if (c.skillActive()[c.playerCooking]) {
				c.setCookAmount(1);
				Cooking.cookItem(c, c.getCookItem());
			}
			break;

		case 104078:
			if (c.lootBag) {
				LootingBag.close(c);
			}
			break;

		case 17111:
			if (c.playerMagicBook == 0) {
				c.setSidebarInterface(6, 1151); // modern
			} else if (c.playerMagicBook == 2) {
				c.setSidebarInterface(6, 29999); // lunar
			} else if (c.playerMagicBook == 1) {
				c.setSidebarInterface(6, 12855); // ancient
			}
			c.getPA().resetAutocast();
			break;

		case 4143:
		case 50245:
		case 117123:
			c.interfaceAction = 1;
			c.getPA().sendFrame126("Barrows", 15240);
			c.getPA().sendFrame126("Fight Caves", 15241);
			c.getPA().sendFrame126("Warrior Gld", 15239);
			c.getPA().sendFrame126("Assault", 15243);
			c.getPA().sendFrame126("Duel Arena", 15242);
			c.setSidebarInterface(6, 3209);
			break;

		case 4140:
		case 50235:
		case 117112:
			c.interfaceAction = 0;
			c.getPA().sendFrame126("Rock Crabs", 15240);
			c.getPA().sendFrame126("Dagannoths", 15241);
			c.getPA().sendFrame126("Island", 15239);
			c.getPA().sendFrame126("Experiments", 15243);
			c.getPA().sendFrame126("More Options", 15242);
			c.setSidebarInterface(6, 3209);
			break;

		case 4146:
		case 50253:
		case 117131:
			c.interfaceAction = 2;
			c.getDH().sendOption4("Varrock Multi", "Castles", "Graveyard",
					"Mage Bank");
			break;

		case 4150:
		case 51005:
		case 117154:
			c.getDH().sendOption2("Skiller Island", "Gnome Agility Course.");
			c.interfaceAction = 14;
			break;

		case 6004:
		case 51013:
		case 117162:
			c.interfaceAction = 3;
			c.getDH().sendOption5("King Black Dragon (Wildy)",
					"Corporeal Beast (Wildy)", "Cerberus Lair",
					"Smoke Devil Lair", "Next Page");
			break;

		case 59136:
			if (c.interfaceAction == 0) {
				c.getPA().startTeleport(2670, 3710, 0, teleportType);
			} else if (c.interfaceAction == 1) {
				c.getPA().startTeleport(3565, 3314, 0, teleportType);
			} else if (c.interfaceAction == 13) {
				c.getPA().startTeleport(3428, 3537, 0, teleportType);
			}
			resetMagicBook(c);
			break;

		case 59137:
			if (c.interfaceAction == 0) {
				c.getPA().startTeleport(2455, 10147, 0, teleportType);
			} else if (c.interfaceAction == 1) {
				c.getPA().startTeleport(2446, 5178, 0, teleportType);
			} else if (c.interfaceAction == 13) {
				c.getPA().startTeleport(2884, 9798, 0, teleportType);
			}
			resetMagicBook(c);
			break;

		case 59138:
			if (c.interfaceAction == 0) {
				c.interfaceAction = 13;
				c.getPA().sendFrame126("Slayer Tower", 15240);
				c.getPA().sendFrame126("Tav Dungeon", 15241);
				c.getPA().sendFrame126("Brim Dungeon", 15239);
				c.getPA().sendFrame126("-", 15243);
				c.getPA().sendFrame126("-", 15242);
				c.setSidebarInterface(6, 3209);
				return;
			}
			if (c.interfaceAction == 1) {
				c.getPA().startTeleport(3362, 3263, 0, teleportType);
			}
			resetMagicBook(c);
			break;

		case 59135:
			if (c.interfaceAction == 0) {
				c.getPA().startTeleport(2896, 2725, 0, teleportType);
			} else if (c.interfaceAction == 1) {
				c.getPA().startTeleport(2846, 3538, 0, teleportType);
			} else if (c.interfaceAction == 13) {
				c.getPA().startTeleport(2713, 9564, 0, teleportType);
			}
			resetMagicBook(c);
			break;

		case 59139:
			if (c.interfaceAction == 0) {
				c.sendMessage("Not available during the beta.");
				// c.getPA().startTeleport(3550, 9947, 0, teleportType);
			} else if (c.interfaceAction == 1) {
				c.getPA().startTeleport(2603, 3153, 0, teleportType);
			}
			resetMagicBook(c);
			break;

		case 215062:
			c.getPA().sendFrame126("", 58063);
			c.usingSupplies = false;
			c.getPA().removeAllWindows();
			break;

		case 226162:
			if (c.getPA().viewingOtherBank) {
				return;
			}
			if (!c.isBanking)
				return;
			if (System.currentTimeMillis() - c.lastBankDeposit < 3000)
				return;
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			c.lastBankDeposit = System.currentTimeMillis();
			for (int slot = 0; slot < c.playerItems.length; slot++) {
				if (c.playerItems[slot] > 0 && c.playerItemsN[slot] > 0) {
					c.getItems().addToBank(c.playerItems[slot] - 1,
							c.playerItemsN[slot], false);
				}
			}
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			break;

		case 226170:
			if (c.getPA().viewingOtherBank) {
				return;
			}
			if (!c.isBanking)
				return;
			if (System.currentTimeMillis() - c.lastBankDeposit < 3000)
				return;
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			c.lastBankDeposit = System.currentTimeMillis();
			for (int slot = 0; slot < c.playerEquipment.length; slot++) {
				if (c.playerEquipment[slot] > 0 && c.playerEquipmentN[slot] > 0) {
					c.getItems().addEquipmentToBank(c.playerEquipment[slot],
							slot, c.playerEquipmentN[slot], false);
					c.getItems().wearItem(-1, 0, slot);
				}
			}
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			break;

		case 226186:
		case 226198:
		case 226209:
		case 226220:
		case 226231:
		case 226242:
		case 226253:
		case 227008:
		case 227019:
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				return;
			}
			int tabId = actionButtonId == 226186 ? 0
					: actionButtonId == 226198 ? 1
							: actionButtonId == 226209 ? 2
									: actionButtonId == 226220 ? 3
											: actionButtonId == 226231 ? 4
													: actionButtonId == 226242 ? 5
															: actionButtonId == 226253 ? 6
																	: actionButtonId == 227008 ? 7
																			: actionButtonId == 227019 ? 8
																					: -1;
			if (tabId <= -1 || tabId > 8)
				return;
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset(tabId);
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			BankTab tab = c.getBank().getBankTab(tabId);
			if (tab.getTabId() == c.getBank().getCurrentBankTab().getTabId())
				return;
			if (tab.size() <= 0 && tab.getTabId() != 0) {
				c.sendMessage("Drag an item into the new tab slot to create a tab.");
				return;
			}
			c.getBank().setCurrentBankTab(tab);
			c.getPA().openUpBank();
			break;

		case 226197:
		case 226208:
		case 226219:
		case 226230:
		case 226241:
		case 226252:
		case 227007:
		case 227018:
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				return;
			}
			tabId = actionButtonId == 226197 ? 1
					: actionButtonId == 226208 ? 2
							: actionButtonId == 226219 ? 3
									: actionButtonId == 226230 ? 4
											: actionButtonId == 226241 ? 5
													: actionButtonId == 226252 ? 6
															: actionButtonId == 227007 ? 7
																	: actionButtonId == 227018 ? 8
																			: -1;
			tab = c.getBank().getBankTab(tabId);
			if (tab == null || tab.getTabId() == 0 || tab.size() == 0) {
				c.sendMessage("You cannot collapse this tab.");
				return;
			}
			if (tab.size() + c.getBank().getBankTab()[0].size() >= Config.BANK_SIZE) {
				c.sendMessage("You cannot collapse this tab. The contents of this tab and your");
				c.sendMessage("main tab are greater than " + Config.BANK_SIZE
						+ " unique items.");
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			for (BankItem item : tab.getItems()) {
				c.getBank().getBankTab()[0].add(item);
			}
			tab.getItems().clear();
			if (tab.size() == 0) {
				c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
			}
			c.getPA().openUpBank();
			break;

		case 226185:
		case 226196:
		case 226207:
		case 226218:
		case 226229:
		case 226240:
		case 226251:
		case 227006:
		case 227017:
			if (!c.isBanking) {
				c.getPA().removeAllWindows();
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().reset();
				return;
			}
			tabId = actionButtonId == 226185 ? 0
					: actionButtonId == 226196 ? 1
							: actionButtonId == 226207 ? 2
									: actionButtonId == 226218 ? 3
											: actionButtonId == 226229 ? 4
													: actionButtonId == 226240 ? 5
															: actionButtonId == 226251 ? 6
																	: actionButtonId == 227006 ? 7
																			: actionButtonId == 227017 ? 8
																					: -1;
			tab = c.getBank().getBankTab(tabId);
			long value = 0;
			if (tab == null || tab.size() == 0)
				return;
			for (BankItem item : tab.getItems()) {
				long tempValue = item.getId() - 1 == 995 ? 1 : c.getShops()
						.getItemShopValue(item.getId() - 1);
				value += tempValue * item.getAmount();
			}
			c.sendMessage("<col=255>The total networth of tab "
					+ tab.getTabId() + " is </col><col=600000>"
					+ Long.toString(value) + " gp</col>.");
			break;

		case 22024:
		case 86008:
			c.getPA().openUpBank();
			break;

		case 118098:
			c.getPA().castVeng();
			break;
		// crafting + fletching interface:
		case 150:
			if (c.autoRet == 0)
				c.autoRet = 1;
			else
				c.autoRet = 0;
			break;
		// 1st tele option
		case 9190:
			if (c.interfaceAction == 3) {
				c.getPA().startTeleport(3007, 3849, 0, teleportType);
			}
			if (c.interfaceAction == 4) {
				c.getPA().startTeleport(1909, 4367, 0, teleportType);
			}
			if (c.interfaceAction == 8) {
				WildyTeleports.startTeleport(c, 0, 0);
				c.interfaceAction = -1;
				return;
			}
			if (c.interfaceAction == 9) {
				WildyTeleports.startTeleport(c, 4, 0);
				c.interfaceAction = -1;
				return;
			}
			if (c.interfaceAction == 10) {
				c.getPA().startTeleport(
						KrakenConstants.KRAKEN_CAVE_POSITION.getX(),
						KrakenConstants.KRAKEN_CAVE_POSITION.getY(), 0,
						teleportType);
			}
			c.getPA().removeAllWindows();
			break;
		// mining - 3046,9779,0
		// smithing - 3079,9502,0

		// 2nd tele option
		case 9191:
			if (c.interfaceAction == 3) {
				c.getPA().startTeleport(3210, 3681, 0, teleportType);
			}
			if (c.interfaceAction == 4) {
				c.getDH().sendOption4("Bandos Lair", "Armadyl Lair",
						"Saradomin Lair", "Zamorak Lair");
				c.interfaceAction = 6;
				return;
			}
			if (c.interfaceAction == 8) {
				WildyTeleports.startTeleport(c, 1, 0);
				c.interfaceAction = -1;
				return;
			}
			c.getPA().removeAllWindows();
			break;

		case 9192:
			if (c.interfaceAction == 3) {
				c.getPA().startTeleport(1240, 1226, 0, teleportType);
			}
			if (c.interfaceAction == 4) {
				c.getPA().startTeleport(1476, 3689, 0, teleportType);
			}
			if (c.interfaceAction == 8) {
				WildyTeleports.startTeleport(c, 2, 0);
				c.interfaceAction = -1;
				return;
			}
			c.getPA().removeAllWindows();
			break;

		// 4th tele option
		case 9193:
			if (c.interfaceAction == 3) {
				c.getPA().startTeleport(3748, 5761, 0, teleportType);
			}
			if (c.interfaceAction == 4) {
				c.getPA().startTeleport(ZulrahConstants.BOAT_POSITION.getX(),
						ZulrahConstants.BOAT_POSITION.getY(), 0, teleportType);
			}
			if (c.interfaceAction == 8) {
				WildyTeleports.startTeleport(c, 3, 0);
				c.interfaceAction = -1;
				return;
			}
			c.getPA().removeAllWindows();
			break;
		// 5th tele option
		case 9194:
			if (c.interfaceAction == 3) {
				c.getDH().sendOption5("Dagannoth Kings", "Godwars",
						"Lizardman Shaman", "Zulrah", "Next Page");
				c.interfaceAction = 4;
				return;
			}
			if (c.interfaceAction == 8) {
				c.getDH().sendOption5("Callisto", "-", "-", "-", "-");
				c.interfaceAction = 9;
				return;
			}
			if (c.interfaceAction == 4) {
				c.getDH().sendOption5("Kraken Lair", "-", "-", "-", "-");
				c.interfaceAction = 10;
				return;
			}
			c.getPA().removeAllWindows();
			break;

		case 9167:
			if (c.interfaceAction == 5) {
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
			}
			if (c.interfaceAction == 11) {
				Slayer.getSlayerTask(c, SlayerConstants.TaskDifficulty.EASY);
				c.getDH().sendDialogues(20, -1);
				return;
			}
			c.dialogueAction = 0;
			c.getPA().removeAllWindows();
			break;

		case 9168:
			if (c.interfaceAction == 5) {
				c.setSidebarInterface(6, 12855);
				c.playerMagicBook = 1;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
			}
			if (c.interfaceAction == 11) {
				Slayer.getSlayerTask(c, SlayerConstants.TaskDifficulty.MEDIUM);
				c.getDH().sendDialogues(20, -1);
				return;
			}
			c.dialogueAction = 0;
			c.getPA().removeAllWindows();
			break;

		case 9169:
			if (c.interfaceAction == 5) {
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
			}
			if (c.interfaceAction == 11) {
				Slayer.getSlayerTask(c, SlayerConstants.TaskDifficulty.HARD);
				c.getDH().sendDialogues(20, -1);
				return;
			}
			c.dialogueAction = 0;
			c.getPA().removeAllWindows();
			break;

		case 58253:
			if (Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			c.getItems().writeBonus();
			break;

		case 59004:
			c.getPA().removeAllWindows();
			break;

		case 62137:
			if (c.clanId >= 0) {
				c.sendMessage("You are already in a clan.");
				break;
			}
			if (c.getOutStream() != null) {
				c.getOutStream().createFrame(187);
				c.flushOutStream();
			}
			break;

		case 9178:
			if (c.interfaceAction == 2) {
				c.getPA().startTeleport(3242, 3520, 0, teleportType);
			}
			if (c.interfaceAction == 6) {
				c.getPA().startTeleport(2861, 5354, 2, teleportType);
			}
			c.getPA().removeAllWindows();
			break;

		case 9179:
			if (c.interfaceAction == 2) {
				c.getPA().startTeleport(3006, 3631, 0, teleportType);
			}
			if (c.interfaceAction == 6) {
				c.getPA().startTeleport(2925, 5336, 2, teleportType);
			}
			c.getPA().removeAllWindows();
			break;

		case 9180:
			if (c.interfaceAction == 2) {
				c.getPA().startTeleport(3164, 3685, 0, teleportType);
			}
			if (c.interfaceAction == 6) {
				c.getPA().startTeleport(2911, 5266, 0, teleportType);
			}
			c.getPA().removeAllWindows();
			break;

		case 9181:
			if (c.interfaceAction == 2) {
				c.getPA().startTeleport(2539, 4717, 0, teleportType);
			}
			if (c.interfaceAction == 6) {
				c.sendMessage("Under construction - should be released soon.");
			}
			c.getPA().removeAllWindows();
			break;

		case 1093:
		case 1094:
		case 1097:
			if (c.autocastId > 0) {
				c.getPA().resetAutocast();
			} else {
				if (c.playerMagicBook == 1) {
					if (c.playerEquipment[c.playerWeapon] == 4675)
						c.setSidebarInterface(0, 1689);
					else
						c.sendMessage("You can't autocast ancients without an ancient staff.");
				} else if (c.playerMagicBook == 0) {
					if (c.playerEquipment[c.playerWeapon] == 4170) {
						c.setSidebarInterface(0, 12050);
					} else {
						c.setSidebarInterface(0, 1829);
					}
				}

			}
			break;

		case 9157:// barrows tele to tunnels
			if (c.dialogueAction == 29) {
				if (c.inBarrows()) {
					c.getBarrows().checkCoffins();
					c.getPA().removeAllWindows();
					return;
				} else {
					c.getPA().removeAllWindows();
					c.sendMessage("@blu@You can only do this while you're at barrows, fool.");
				}
			} else if (c.interfaceAction == 7) {
				c.getShops().openShop(9);
				c.interfaceAction = -1;
				return;
			} else if (c.interfaceAction == 12) {
				Slayer.resetSlayerTask(c);
				return;
			} else if (c.interfaceAction == 14) {
				c.getPA().startTeleport(2524, 4775, 0, teleportType);
			} else if (c.interfaceAction == 15) {
				SlayerTeleport.execute(c);
			} else if (c.interfaceAction == 16) {
				MaxCape.buyMaxCape(c);
				return;
			}
			c.dialogueAction = 0;
			c.getPA().removeAllWindows();
			break;

		case 9158:
			if (c.dialogueAction == 8) {
				c.getPA().fixAllBarrows();
			} else if (c.interfaceAction == 7) {
				c.getShops().openShop(10);
				c.interfaceAction = -1;
			} else if (c.interfaceAction == 14) {
				c.getPA().startTeleport(2474, 3440, 0, teleportType);
				c.getPA().removeAllWindows();
			} else {
				c.dialogueAction = 0;
				c.getPA().removeAllWindows();
			}
			break;

		/** Specials **/
		case 29188:
			c.specBarId = 7636; // the special attack text - sendframe126(S P E
								// C I A L A T T A C K, c.specBarId);
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29163:
			c.specBarId = 7611;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 33033:
			c.specBarId = 8505;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29038:
			if (c.playerEquipment[3] == 4153) {
				c.specBarId = 7486;
				c.getCombat().handleGmaulPlayer();
				c.getItems().updateSpecialBar();
				return;
			}
			if (c.playerEquipment[3] == 12848) {
				c.specBarId = 7486;
				c.getCombat().handleGmaulPlayer1();
				c.getItems().updateSpecialBar();
				return;
			}
			c.specBarId = 7486;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29063:
			if (c.getCombat()
					.checkSpecAmount(c.playerEquipment[c.playerWeapon])) {
				c.gfx0(246);
				c.forcedChat("Raarrrrrgggggghhhhhhh!");
				c.startAnimation(1056);
				c.playerLevel[2] = c.getLevelForXP(c.playerXP[2])
						+ (c.getLevelForXP(c.playerXP[2]) * 15 / 100);
				c.getPA().refreshSkill(2);
				c.getItems().updateSpecialBar();
			} else {
				c.sendMessage("You don't have the required special energy to use this attack.");
			}
			break;

		case 48023:
			c.specBarId = 12335;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29138:
			c.specBarId = 7586;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29113:
			c.specBarId = 7561;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29238:
			c.specBarId = 7686;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		/** Dueling **/
		case 26065:
		case 26040:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.FORFEIT);
			break;

		case 26066: // no movement
		case 26048:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			if (!duelSession.getRules().contains(Rule.FORFEIT)) {
				duelSession.toggleRule(c, Rule.FORFEIT);
			}
			duelSession.toggleRule(c, Rule.NO_MOVEMENT);
			break;

		case 26069: // no range
		case 26042:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_RANGE);
			break;

		case 26070: // no melee
		case 26043:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_MELEE);
			break;

		case 26071: // no mage
		case 26041:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_MAGE);
			break;

		case 26072: // no drinks
		case 26045:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_DRINKS);
			break;

		case 26073: // no food
		case 26046:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_FOOD);
			break;

		case 26074: // no prayer
		case 26047:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_PRAYER);
			break;

		case 26076: // obsticals
		case 26075:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.OBSTACLES);
			break;

		case 2158: // fun weapons
		case 2157:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			if (duelSession.getRules().contains(Rule.WHIP_AND_DDS)) {
				duelSession.toggleRule(c, Rule.WHIP_AND_DDS);
				return;
			}
			if (!Rule.WHIP_AND_DDS.getReq().get().meets(c)) {
				c.getPA()
						.sendFrame126(
								"You must have a whip and dragon dagger to select this.",
								6684);
				return;
			}
			if (!Rule.WHIP_AND_DDS.getReq().get()
					.meets(duelSession.getOther(c))) {
				c.getPA()
						.sendFrame126(
								"Your opponent does not have a whip and dragon dagger.",
								6684);
				return;
			}
			if (duelSession.getStage().getStage() != MultiplayerSessionStage.OFFER_ITEMS) {
				c.sendMessage("You cannot change rules whilst on the second interface.");
				return;
			}
			duelSession.getRules().reset();
			for (Rule rule : Rule.values()) {
				int index = rule.ordinal();
				if (index == 3 || index == 8 || index == 10 || index == 14) {
					continue;
				}
				duelSession.toggleRule(c, rule);
			}
			break;

		case 30136: // sp attack
		case 30137:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_SPECIAL_ATTACK);
			break;

		case 53245: // no helm
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_HELM);
			break;

		case 53246: // no cape
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_CAPE);
			break;

		case 53247: // no ammy
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_AMULET);
			break;

		case 53249: // no weapon
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_WEAPON);
			break;

		case 53250: // no body
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_BODY);
			break;

		case 53251: // no shield
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_SHIELD);
			break;

		case 53252: // no legs
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_LEGS);
			break;

		case 53255: // no gloves
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_GLOVES);
			break;

		case 53254: // no boots
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_BOOTS);
			break;

		case 53253: // no rings
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_RINGS);
			break;

		case 53248: // no arrows
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(c, Rule.NO_ARROWS);
			break;

		case 26018:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (System.currentTimeMillis() - c.getDuel().getLastAccept() < 1000) {
				return;
			}
			c.getTrade().setLastAccept(System.currentTimeMillis());
			if (Objects.nonNull(duelSession)
					&& duelSession instanceof DuelSession) {
				duelSession.accept(c, MultiplayerSessionStage.OFFER_ITEMS);
			}
			break;

		case 25120:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (System.currentTimeMillis() - c.getDuel().getLastAccept() < 1000) {
				return;
			}
			c.getTrade().setLastAccept(System.currentTimeMillis());
			if (Objects.nonNull(duelSession)
					&& duelSession instanceof DuelSession) {
				duelSession.accept(c, MultiplayerSessionStage.CONFIRM_DECISION);
			}
			break;

		case 4169: // god spell charge
			c.usingMagic = true;
			if (!c.getCombat().checkMagicReqs(48)) {
				break;
			}

			if (System.currentTimeMillis() - c.godSpellDelay < Config.GOD_SPELL_CHARGE) {
				c.sendMessage("You still feel the charge in your body!");
				break;
			}
			c.godSpellDelay = System.currentTimeMillis();
			c.sendMessage("You feel charged with a magical power!");
			c.gfx100(c.MAGIC_SPELLS[48][3]);
			c.startAnimation(c.MAGIC_SPELLS[48][2]);
			c.usingMagic = false;
			break;

		case 28164: // item kept on death
			break;

		case 152:
			c.isRunning2 = !c.isRunning2;
			int frame = c.isRunning2 == true ? 1 : 0;
			c.getPA().sendFrame36(173, frame);
			break;

		case 9154:
			c.logout();
			break;

		case 21010:
			c.takeAsNote = true;
			break;

		case 21011:
			c.takeAsNote = false;
			break;

		// home teleports
		case 4171:
		case 50056:
		case 117048:
			c.getPA().startTeleport(Config.START_LOCATION_X,
					Config.START_LOCATION_Y, 0, teleportType);
			break;

		case 9125: // Accurate
		case 6221: // range accurate
		case 22228: // punch (unarmed)
		case 48010: // flick (whip)
		case 21200: // spike (pickaxe)
		case 1080: // bash (staff)
		case 6168: // chop (axe)
		case 6236: // accurate (long bow)
		case 17102: // accurate (darts)
		case 8234: // stab (dagger)
			c.fightMode = 0;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		case 9126: // Defensive
		case 48008: // deflect (whip)
		case 22229: // block (unarmed)
		case 21201: // block (pickaxe)
		case 1078: // focus - block (staff)
		case 6169: // block (axe)
		case 33019: // fend (hally)
		case 18078: // block (spear)
		case 8235: // block (dagger)
			c.fightMode = 1;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		case 9127: // Controlled
		case 48009: // lash (whip)
		case 33018: // jab (hally)
		case 6234: // longrange (long bow)
		case 6219: // longrange
		case 18077: // lunge (spear)
		case 18080: // swipe (spear)
		case 18079: // pound (spear)
		case 17100: // longrange (darts)
			c.fightMode = 3;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		case 9128: // Aggressive
		case 6220: // range rapid
		case 22230: // kick (unarmed)
		case 21203: // impale (pickaxe)
		case 21202: // smash (pickaxe)
		case 1079: // pound (staff)
		case 6171: // hack (axe)
		case 6170: // smash (axe)
		case 33020: // swipe (hally)
		case 6235: // rapid (long bow)
		case 17101: // repid (darts)
		case 8237: // lunge (dagger)
		case 8236: // slash (dagger)
			c.fightMode = 2;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		/** Prayers **/
		case 21233: // thick skin
			c.getCombat().activatePrayer(0);
			break;
		case 21234: // burst of str
			c.getCombat().activatePrayer(1);
			break;
		case 21235: // charity of thought
			c.getCombat().activatePrayer(2);
			break;
		case 70080: // range
			c.getCombat().activatePrayer(3);
			break;
		case 70082: // mage
			c.getCombat().activatePrayer(4);
			break;
		case 21236: // rockskin
			c.getCombat().activatePrayer(5);
			break;
		case 21237: // super human
			c.getCombat().activatePrayer(6);
			break;
		case 21238: // improved reflexes
			c.getCombat().activatePrayer(7);
			break;
		case 21239: // hawk eye
			c.getCombat().activatePrayer(8);
			break;
		case 21240:
			c.getCombat().activatePrayer(9);
			break;
		case 21241: // protect Item
			c.getCombat().activatePrayer(10);
			break;
		case 70084: // 26 range
			c.getCombat().activatePrayer(11);
			break;
		case 70086: // 27 mage
			c.getCombat().activatePrayer(12);
			break;
		case 21242: // steel skin
			c.getCombat().activatePrayer(13);
			break;
		case 21243: // ultimate str
			c.getCombat().activatePrayer(14);
			break;
		case 21244: // incredible reflex
			c.getCombat().activatePrayer(15);
			break;
		case 21245: // protect from magic
			c.getCombat().activatePrayer(16);
			break;
		case 21246: // protect from range
			c.getCombat().activatePrayer(17);
			break;
		case 21247: // protect from melee
			c.getCombat().activatePrayer(18);
			break;
		case 70088: // 44 range
			c.getCombat().activatePrayer(19);
			break;
		case 70090: // 45 mystic
			c.getCombat().activatePrayer(20);
			break;
		case 2171: // retrui
			c.getCombat().activatePrayer(21);
			break;
		case 2172: // redem
			c.getCombat().activatePrayer(22);
			break;
		case 2173: // smite
			c.getCombat().activatePrayer(23);
			break;
		case 70092: // chiv
			c.getCombat().activatePrayer(24);
			break;
		case 70094: // piety
			c.getCombat().activatePrayer(25);
			break;

		case 13092:
			if (!Server.getMultiplayerSessionListener().inSession(c,
					MultiplayerSessionType.TRADE)) {
				c.sendMessage("You are not trading!");
				return;
			}
			if (System.currentTimeMillis() - c.getTrade().getLastAccept() < 1000) {
				return;
			}
			c.getTrade().setLastAccept(System.currentTimeMillis());
			Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.TRADE)
					.accept(c, MultiplayerSessionStage.OFFER_ITEMS);
			break;

		case 13218:
			if (!Server.getMultiplayerSessionListener().inSession(c,
					MultiplayerSessionType.TRADE)) {
				c.sendMessage("You are not trading!");
				return;
			}
			if (System.currentTimeMillis() - c.getTrade().getLastAccept() < 1000) {
				return;
			}
			c.getTrade().setLastAccept(System.currentTimeMillis());
			Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c, MultiplayerSessionType.TRADE)
					.accept(c, MultiplayerSessionStage.CONFIRM_DECISION);
			break;
		/* Rules Interface Buttons */
		case 125011: // Click agree
			if (!c.ruleAgreeButton) {
				c.ruleAgreeButton = true;
				c.getPA().sendFrame36(701, 1);
			} else {
				c.ruleAgreeButton = false;
				c.getPA().sendFrame36(701, 0);
			}
			break;
		case 125003:// Accept
			if (c.ruleAgreeButton) {
				c.getPA().showInterface(3559);
				c.newPlayer = false;
			} else if (!c.ruleAgreeButton) {
				c.sendMessage("You need to click on you agree before you can continue on.");
			}
			break;
		case 125006:// Decline
			c.sendMessage("You have chosen to decline, Client will be disconnected from the server.");
			break;
		/* End Rules Interface Buttons */
		/* Player Options */
		case 74176:
			if (!c.mouseButton) {
				c.mouseButton = true;
				c.getPA().sendFrame36(500, 1);
				c.getPA().sendFrame36(170, 1);
			} else if (c.mouseButton) {
				c.mouseButton = false;
				c.getPA().sendFrame36(500, 0);
				c.getPA().sendFrame36(170, 0);
			}
			break;
		case 74184:
			if (!c.splitChat) {
				c.splitChat = true;
				c.getPA().sendFrame36(502, 1);
				c.getPA().sendFrame36(287, 1);
			} else {
				c.splitChat = false;
				c.getPA().sendFrame36(502, 0);
				c.getPA().sendFrame36(287, 0);
			}
			break;
		case 74180:
			if (!c.chatEffects) {
				c.chatEffects = true;
				c.getPA().sendFrame36(501, 1);
				c.getPA().sendFrame36(171, 0);
			} else {
				c.chatEffects = false;
				c.getPA().sendFrame36(501, 0);
				c.getPA().sendFrame36(171, 1);
			}
			break;
		case 74188:
			if (!c.acceptAid) {
				c.acceptAid = true;
				c.getPA().sendFrame36(503, 1);
				c.getPA().sendFrame36(427, 1);
			} else {
				c.acceptAid = false;
				c.getPA().sendFrame36(503, 0);
				c.getPA().sendFrame36(427, 0);
			}
			break;
		case 74192:
			if (!c.isRunning2) {
				c.isRunning2 = true;
				c.getPA().sendFrame36(504, 1);
				c.getPA().sendFrame36(173, 1);
			} else {
				c.isRunning2 = false;
				c.getPA().sendFrame36(504, 0);
				c.getPA().sendFrame36(173, 0);
			}
			break;
		case 74201:// brightness1
			c.getPA().sendFrame36(505, 1);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 1);
			break;
		case 74203:// brightness2
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 1);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 2);
			break;

		case 74204:// brightness3
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 1);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 3);
			break;

		case 74205:// brightness4
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 1);
			c.getPA().sendFrame36(166, 4);
			break;
		case 74206:// area1
			c.getPA().sendFrame36(509, 1);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74207:// area2
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 1);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74208:// area3
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 1);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74209:// area4
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 1);
			break;
		case 168:
			c.startAnimation(855);
			break;
		case 169:
			c.startAnimation(856);
			break;
		case 162:
			c.startAnimation(857);
			break;
		case 164:
			c.startAnimation(858);
			break;
		case 165:
			c.startAnimation(859);
			break;
		case 161:
			c.startAnimation(860);
			break;
		case 170:
			c.startAnimation(861);
			break;
		case 171:
			c.startAnimation(862);
			break;
		case 163:
			c.startAnimation(863);
			break;
		case 167:
			c.startAnimation(864);
			break;
		case 172:
			c.startAnimation(865);
			break;
		case 166:
			c.startAnimation(866);
			break;
		case 52050:
			c.startAnimation(2105);
			break;
		case 52051:
			c.startAnimation(2106);
			break;
		case 52052:
			c.startAnimation(2107);
			break;
		case 52053:
			c.startAnimation(2108);
			break;
		case 52054:
			c.startAnimation(2109);
			break;
		case 52055:
			c.startAnimation(2110);
			break;
		case 52056:
			c.startAnimation(2111);
			break;
		case 52057:
			c.startAnimation(2112);
			break;
		case 52058:
			c.startAnimation(2113);
			break;
		case 43092:
			c.startAnimation(0x558);
			break;
		case 2155:
			c.startAnimation(0x46B);
			break;
		case 25103:
			c.startAnimation(0x46A);
			break;
		case 25106:
			c.startAnimation(0x469);
			break;
		case 2154:
			c.startAnimation(0x468);
			break;
		case 52071:
			c.startAnimation(0x84F);
			break;
		case 52072:
			c.startAnimation(0x850);
			break;
		case 59062:
			c.startAnimation(2836);
			break;
		case 72032:
			c.startAnimation(3544);
			break;
		case 72033:
			c.startAnimation(3543);
			break;
		case 72254:
			c.startAnimation(3866);
			break;
		/* END OF EMOTES */
		case 28166:

			break;

		case 24017:
			c.getPA().resetAutocast();
			c.getItems().sendWeapon(
					c.playerEquipment[c.playerWeapon],
					ItemAssistant
							.getItemName(c.playerEquipment[c.playerWeapon]));
			break;
		}
		if (c.isAutoButton(actionButtonId))
			c.assignAutocast(actionButtonId);
	}
}
