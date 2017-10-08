package rs2.abyssalps.model.player;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.content.achievement.Achievement;
import rs2.abyssalps.content.bank.BankItem;
import rs2.abyssalps.content.bank.BankTab;
import rs2.abyssalps.content.bosslog.BossLog;
import rs2.util.Misc;

public class PlayerSave {

	/**
	 * Tells us whether or not the player exists for the specified name.
	 * 
	 * @param name
	 * @return
	 */
	public static boolean playerExists(String name) {
		File file = new File("./Data/characters/" + name + ".txt");
		return file.exists();
	}

	public static void addItemToFile(String name, int itemId, int itemAmount) {
		if (itemId < 0 || itemAmount < 0) {
			Misc.println("Illegal operation: Item id or item amount cannot be negative.");
			return;
		}
		BankItem item = new BankItem(itemId + 1, itemAmount);
		if (!playerExists(name)) {
			Misc.println("Illegal operation: Player account does not exist, validate name.");
			return;
		}
		if (PlayerHandler.isPlayerOn(name)) {
			Misc.println("Illegal operation: Attempted to modify the account of a player online.");
			return;
		}
		File character = new File("./Data/characters/" + name + ".txt");
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(character))) {
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			reader.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		BankTab[] tabs = new BankTab[] { new BankTab(0), new BankTab(1), new BankTab(2), new BankTab(3), new BankTab(4), new BankTab(5), new BankTab(6), new BankTab(7), new BankTab(8), };
		String token, token2;
		String[] token3 = new String[3];
		int spot = 0;
		for (String line : lines) {
			if (line == null) {
				continue;
			}
			line = line.trim();
			spot = line.indexOf("=");
			if (spot == -1) {
				continue;
			}
			token = line.substring(0, spot);
			token = token.trim();
			token2 = line.substring(spot + 1);
			token2 = token2.trim();
			token3 = token2.split("\t");
			if (token.equals("bank-tab")) {
				int tabId = Integer.parseInt(token3[0]);
				int id = Integer.parseInt(token3[1]);
				int amount = Integer.parseInt(token3[2]);
				tabs[tabId].add(new BankItem(id, amount));
			}
		}
		boolean inserted = false;
		for (BankTab tab : tabs) {
			if (tab.contains(item) && tab.spaceAvailable(item)) {
				tab.add(item);
				inserted = true;
				break;
			}
		}
		if (!inserted) {
			for (BankTab tab : tabs) {
				if (tab.freeSlots() > 0) {
					tab.add(item);
					inserted = true;
					break;
				}
			}
		}
		if (!inserted) {
			Misc.println("Item could not be added to offline account, no free space in bank.");
			return;
		}
		int startIndex = Misc.indexOfPartialString(lines, "bank-tab");
		int lastIndex = Misc.lastIndexOfPartialString(lines, "bank-tab");
		if (lastIndex != startIndex && startIndex > 0 && lastIndex > 0) {
			List<String> cutout = lines.subList(startIndex, lastIndex);
			List<String> bankData = new ArrayList<>(lastIndex - startIndex);
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < Config.BANK_SIZE; j++) {
					if (j > tabs[i].size() - 1)
						break;
					BankItem bankItem = tabs[i].getItem(j);
					if (bankItem == null)
						continue;
					bankData.add("bank-tab = " + i + "\t" + bankItem.getId() + "\t" + bankItem.getAmount());
				}
			}
			lines.removeAll(cutout);
			lines.addAll(startIndex, bankData);
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(character))) {
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
			writer.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Tells us whether or not the specified name has the friend added.
	 * 
	 * @param name
	 * @param friend
	 * @return
	 */
	public static boolean isFriend(String name, String friend) {
		long nameLong = Misc.playerNameToInt64(friend);
		long[] friends = getFriends(name);
		if (friends != null && friends.length > 0) {
			for (int index = 0; index < friends.length; index++) {
				if (friends[index] == nameLong) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isFriend2(String name, String friend) {
		long nameLong = Misc.playerNameToInt64(friend);
		long[] friends = getFriends2(name);
		if (friends != null && friends.length > 0) {
			for (int index = 0; index < friends.length; index++) {
				if (friends[index] == nameLong) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns a characters friends in the form of a long array.
	 * 
	 * @param name
	 * @return
	 */
	public static long[] getFriends(String name) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		long[] readFriends = new long[200];
		long[] friends = null;
		int totalFriends = 0;
		int index = 0;
		File input = new File("./Data/characters/" + name + ".txt");
		if (!input.exists()) {
			return null;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				int spot = line.indexOf("=");
				if (spot > -1) {
					token = line.substring(0, spot);
					token = token.trim();
					token2 = line.substring(spot + 1);
					token2 = token2.trim();
					token3 = token2.split("\t");
					if (token.equals("character-friend")) {
						try {
							readFriends[index] = Long.parseLong(token2);
							totalFriends++;
						} catch (NumberFormatException nfe) {
						}
					}
				}
			}
			reader.close();
			if (totalFriends > 0) {
				friends = new long[totalFriends];
				for (int i = 0; i < totalFriends; i++) {
					friends[i] = readFriends[i];
				}
				return friends;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	public static long[] getFriends2(String name) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		long[] readFriends = new long[200];
		long[] friends = null;
		int totalFriends = 0;
		int index = 0;
		File input = new File("./Data/characters2/" + name + ".txt");
		if (!input.exists()) {
			return null;
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				int spot = line.indexOf("=");
				if (spot > -1) {
					token = line.substring(0, spot);
					token = token.trim();
					token2 = line.substring(spot + 1);
					token2 = token2.trim();
					token3 = token2.split("\t");
					if (token.equals("character-friend")) {
						try {
							readFriends[index] = Long.parseLong(token2);
							totalFriends++;
						} catch (NumberFormatException nfe) {
						}
					}
				}
			}
			reader.close();
			if (totalFriends > 0) {
				friends = new long[totalFriends];
				for (int i = 0; i < totalFriends; i++) {
					friends[i] = readFriends[i];
				}
				return friends;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	/**
	 * Loading
	 **/
	public static int loadGame(Client p, String playerName, String playerPass) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		boolean File1 = false;

		try {
			characterfile = new BufferedReader(new FileReader("./Data/characters/" + playerName + ".txt"));
			File1 = true;
		} catch (FileNotFoundException fileex1) {
		}

		if (File1) {
			// new File ("./characters/"+playerName+".txt");
		} else {
			Misc.println(playerName + ": character file not found.");
			p.newPlayer = false;
			return 0;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(playerName + ": error loading file.");
			return 3;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				switch (ReadMode) {
				case 1:
					if (token.equals("character-password")) {
						if (playerPass.equalsIgnoreCase(token2)) {
							playerPass = token2;
						} else {
							return 3;
						}
					}
					break;
				case 2:
					if (token.equals("character-height")) {
						p.heightLevel = Integer.parseInt(token2);
					} else if (token.equals("character-height1")) {
						p.teleportToHeight = Integer.parseInt(token2);
					} else if (token.equals("character-posx")) {
						p.teleportToX = (Integer.parseInt(token2) <= 0 ? 3210 : Integer.parseInt(token2));
					} else if (token.equals("character-posy")) {
						p.teleportToY = (Integer.parseInt(token2) <= 0 ? 3424 : Integer.parseInt(token2));
					} else if (token.equals("character-rights")) {
						p.setPlayerRank(Integer.parseInt(token2));
					} else if (token.equals("skull-timer")) {
						p.skullTimer = Integer.parseInt(token2);
					} else if (token.equals("magic-book")) {
						p.playerMagicBook = Integer.parseInt(token2);
					} else if (token.equals("brother-info")) {
						p.barrowsNpcs[Integer.parseInt(token3[0])][1] = Integer.parseInt(token3[1]);
					} else if (token.equals("special-amount")) {
						p.specAmount = Double.parseDouble(token2);
					} else if (token.equals("selected-coffin")) {
						p.randomCoffin = Integer.parseInt(token2);
					} else if (token.equals("teleblock-length")) {
						p.teleBlockDelay = System.currentTimeMillis();
						p.teleBlockLength = Integer.parseInt(token2);
					} else if (token.equals("autoRet")) {
						p.autoRet = Integer.parseInt(token2);
					} else if (token.equals("fightMode")) {
						p.fightMode = Integer.parseInt(token2);
					} else if (token.equals("jailed")) {
						p.setJailed(Boolean.parseBoolean(token2));
					} else if (token.equals("starter")) {
						p.startTimer = Integer.parseInt(token2);
					} else if (token.equals("last-ip")) {
						p.lastIPKilled = token2;
					} else if (token.equals("last-name")) {
						p.lastNameKilled = token2;
					} else if (token.equals("bankPin")) {
						p.bankPin = token2;
					} else if (token.equals("setPin")) {
						p.setPin = Boolean.parseBoolean(token2);
					} else if (token.equals("IP")) {
						for (int index = 0; index < p.playerIPs.size(); index++) {
							if (p.playerIPs.get(index) != null) {
								p.playerIPs.add(token2);
							}
						}
					} else if (token.equals("dart")) {
						p.getPipeData()[0] = Integer.parseInt(token2);
					} else if (token.equals("dart-amount")) {
						p.getPipeData()[1] = Integer.parseInt(token2);
					} else if (token.equals("pipe-charges")) {
						p.getPipeData()[2] = Integer.parseInt(token2);
					} else if (token.equals("pet-id")) {
						p.setPetId(Integer.parseInt(token2));
					} else if (token.equals("task-id")) {
						p.getSlayerData()[0] = Integer.parseInt(token2);
					} else if (token.equals("task-amount")) {
						p.getSlayerData()[1] = Integer.parseInt(token2);
					} else if (token.equals("slayer-points")) {
						p.getSlayerData()[2] = Integer.parseInt(token2);
					} else if (token.equals("days")) {
						p.getTotalTime().getTime()[3] = Integer.parseInt(token2);
					} else if (token.equals("hours")) {
						p.getTotalTime().getTime()[2] = Integer.parseInt(token2);
					} else if (token.equals("minutes")) {
						p.getTotalTime().getTime()[1] = Integer.parseInt(token2);
					}
					break;
				case 3:
					if (token.equals("character-equip")) {
						p.playerEquipment[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.playerEquipmentN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 4:
					if (token.equals("character-look")) {
						p.playerAppearance[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
					}
					break;
				case 5:
					if (token.equals("character-skill")) {
						p.playerLevel[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.playerXP[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 6:
					if (token.equals("character-item")) {
						p.playerItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.playerItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 7:
					if (token.equals("character-bank")) {
						p.bankItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.bankItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
						p.getBank().getBankTab()[0].add(new BankItem(Integer.parseInt(token3[1]), Integer.parseInt(token3[2])));
					} else if (token.equals("bank-tab")) {
						int tabId = Integer.parseInt(token3[0]);
						int itemId = Integer.parseInt(token3[1]);
						int itemAmount = Integer.parseInt(token3[2]);
						p.getBank().getBankTab()[tabId].add(new BankItem(itemId, itemAmount));
					}
					break;
				case 8:
					if (token.equals("character-friend")) {
						p.getFriends().add(Long.parseLong(token3[0]));
					}
					break;

				case 9:
					if (token.equals("character-ignore")) {
						p.getIgnores().add(Long.parseLong(token3[0]));
					}
					break;

				case 10:
					if (token.equals("character-points")) {
						p.getPoints()[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
					}
					break;

				case 11:
					if (token.equals("character-lootbag")) {
						p.getLootItems()[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						p.getLootItemAmount()[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 12:
					if (token.equals("achievement")) {
						Achievement achievement = p.getAchievements().getAchievement(Integer.parseInt(token3[0]));
						if (achievement != null) {
							achievement.setCount(Integer.parseInt(token3[1]));
						}
					}
					break;

				case 13:
					if (token.equals("boss-logs")) {
						BossLog logs = p.getBossLogs().getBossLog(Integer.parseInt(token3[0]));
						if (logs != null) {
							logs.setCount(Integer.parseInt(token3[1]));
						}
					}
					break;
				}
			} else {
				if (line.equals("[ACCOUNT]")) {
					ReadMode = 1;
				} else if (line.equals("[CHARACTER]")) {
					ReadMode = 2;
				} else if (line.equals("[EQUIPMENT]")) {
					ReadMode = 3;
				} else if (line.equals("[LOOK]")) {
					ReadMode = 4;
				} else if (line.equals("[SKILLS]")) {
					ReadMode = 5;
				} else if (line.equals("[ITEMS]")) {
					ReadMode = 6;
				} else if (line.equals("[BANK]")) {
					ReadMode = 7;
				} else if (line.equals("[FRIENDS]")) {
					ReadMode = 8;
				} else if (line.equals("[IGNORES]")) {
					ReadMode = 9;
				} else if (line.equals("[POINTS]")) {
					ReadMode = 10;
				} else if (line.equals("[LOOTING_BAG]")) {
					ReadMode = 11;
				} else if (line.equals("[ACHIEVEMENTS]")) {
					ReadMode = 12;
				} else if (line.equals("[BOSS_LOGS]")) {
					ReadMode = 13;
				} else if (line.equals("[EOF]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return 1;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return 13;
	}

	/**
	 * Saving
	 **/
	public static boolean saveGame(Client p) {
		if (!p.saveFile || p.newPlayer || !p.saveCharacter) {
			// System.out.println("first");
			return false;
		}
		if (p.playerName == null || PlayerHandler.players[p.playerId] == null) {
			// System.out.println("second");
			return false;
		}
		p.playerName = p.playerName2;
		int tbTime = (int) (p.teleBlockDelay - System.currentTimeMillis() + p.teleBlockLength);
		if (tbTime > 300000 || tbTime < 0) {
			tbTime = 0;
		}

		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter("./Data/characters/" + p.playerName + ".txt"));

			/* ACCOUNT */
			characterfile.write("[ACCOUNT]", 0, 9);
			characterfile.newLine();
			characterfile.write("character-username = ", 0, 21);
			characterfile.write(p.playerName, 0, p.playerName.length());
			characterfile.newLine();
			characterfile.write("character-password = ", 0, 21);
			characterfile.write(p.playerPass, 0, p.playerPass.length());
			characterfile.newLine();
			characterfile.newLine();

			/* CHARACTER */
			characterfile.write("[CHARACTER]", 0, 11);
			characterfile.newLine();
			characterfile.write("character-height = ", 0, 19);
			characterfile.write(Integer.toString(p.heightLevel), 0, Integer.toString(p.heightLevel).length());
			characterfile.newLine();
			characterfile.write("character-height1 = ", 0, 20);
			characterfile.write(Integer.toString(p.teleportToHeight), 0, Integer.toString(p.teleportToHeight).length());
			characterfile.newLine();
			characterfile.write("character-posx = ", 0, 17);
			characterfile.write(Integer.toString(p.absX), 0, Integer.toString(p.absX).length());
			characterfile.newLine();
			characterfile.write("character-posy = ", 0, 17);
			characterfile.write(Integer.toString(p.absY), 0, Integer.toString(p.absY).length());
			characterfile.newLine();
			characterfile.write("character-rights = ", 0, 19);
			characterfile.write(Integer.toString(p.getRank()), 0, Integer.toString(p.getRank()).length());
			characterfile.newLine();
			characterfile.write("skull-timer = ", 0, 14);
			characterfile.write(Integer.toString(p.skullTimer), 0, Integer.toString(p.skullTimer).length());
			characterfile.newLine();
			characterfile.write("magic-book = ", 0, 13);
			characterfile.write(Integer.toString(p.playerMagicBook), 0, Integer.toString(p.playerMagicBook).length());
			characterfile.newLine();
			for (int b = 0; b < p.barrowsNpcs.length; b++) {
				characterfile.write("brother-info = ", 0, 15);
				characterfile.write(Integer.toString(b), 0, Integer.toString(b).length());
				characterfile.write("	", 0, 1);
				characterfile.write(p.barrowsNpcs[b][1] <= 1 ? Integer.toString(0) : Integer.toString(p.barrowsNpcs[b][1]), 0, Integer.toString(p.barrowsNpcs[b][1]).length());
				characterfile.newLine();
			}
			characterfile.write("special-amount = ", 0, 17);
			characterfile.write(Double.toString(p.specAmount), 0, Double.toString(p.specAmount).length());
			characterfile.newLine();
			characterfile.write("selected-coffin = ", 0, 18);
			characterfile.write(Integer.toString(p.randomCoffin), 0, Integer.toString(p.randomCoffin).length());
			characterfile.newLine();
			characterfile.write("teleblock-length = ", 0, 19);
			characterfile.write(Integer.toString(tbTime), 0, Integer.toString(tbTime).length());
			characterfile.newLine();
			characterfile.write("autoRet = ", 0, 10);
			characterfile.write(Integer.toString(p.autoRet), 0, Integer.toString(p.autoRet).length());
			characterfile.newLine();
			characterfile.write("fightMode = ", 0, 12);
			characterfile.write(Integer.toString(p.fightMode), 0, Integer.toString(p.fightMode).length());
			characterfile.newLine();
			characterfile.write("jailed = ", 0, 9);
			characterfile.write(Boolean.toString(p.isJailed()), 0, Boolean.toString(p.isJailed()).length());
			characterfile.newLine();
			characterfile.write("starter = ", 0, 10);
			characterfile.write(Integer.toString(p.startTimer), 0, Integer.toString(p.startTimer).length());
			characterfile.newLine();
			characterfile.write("last-ip = ", 0, 10);
			characterfile.write(p.lastIPKilled, 0, p.lastIPKilled.length());
			characterfile.newLine();
			characterfile.write("last-name = ", 0, 12);
			characterfile.write(p.lastNameKilled, 0, p.lastNameKilled.length());
			characterfile.newLine();
			characterfile.write("bankPin = ", 0, 10);
			characterfile.write(p.bankPin, 0, p.bankPin.length());
			characterfile.newLine();
			characterfile.write("setPin = ", 0, 9);
			characterfile.write(Boolean.toString(p.setPin), 0, Boolean.toString(p.setPin).length());
			characterfile.newLine();
			characterfile.write("dart = ", 0, 7);
			characterfile.write(Integer.toString(p.getPipeData()[0]), 0, Integer.toString(p.getPipeData()[0]).length());
			characterfile.newLine();
			characterfile.write("dart-amount = ", 0, 14);
			characterfile.write(Integer.toString(p.getPipeData()[1]), 0, Integer.toString(p.getPipeData()[1]).length());
			characterfile.newLine();
			characterfile.write("pipe-charges = ", 0, 15);
			characterfile.write(Integer.toString(p.getPipeData()[2]), 0, Integer.toString(p.getPipeData()[2]).length());
			characterfile.newLine();
			for (int index = 0; index < p.playerIPs.size(); index++) {
				if (p.playerIPs.get(index) == null) {
					continue;
				}
				characterfile.write("IP = ", 0, 5);
				characterfile.write(p.playerIPs.get(index), 0, p.playerIPs.get(index).length());
				characterfile.newLine();
			}
			characterfile.write("pet-id = ", 0, 9);
			characterfile.write(Integer.toString(p.getPetId()), 0, Integer.toString(p.getPetId()).length());
			characterfile.newLine();
			characterfile.write("task-id = ", 0, 10);
			characterfile.write(Integer.toString(p.getSlayerData()[0]), 0, Integer.toString(p.getSlayerData()[0]).length());
			characterfile.newLine();
			characterfile.write("task-amount = ", 0, 14);
			characterfile.write(Integer.toString(p.getSlayerData()[1]), 0, Integer.toString(p.getSlayerData()[1]).length());
			characterfile.newLine();
			characterfile.write("slayer-points = ", 0, 16);
			characterfile.write(Integer.toString(p.getSlayerData()[2]), 0, Integer.toString(p.getSlayerData()[2]).length());
			characterfile.newLine();
			characterfile.write("days = ", 0, 7);
			characterfile.write(Integer.toString(p.getTotalTime().getTime()[3]), 0, Integer.toString(p.getTotalTime().getTime()[3]).length());
			characterfile.newLine();
			characterfile.write("hours = ", 0, 8);
			characterfile.write(Integer.toString(p.getTotalTime().getTime()[2]), 0, Integer.toString(p.getTotalTime().getTime()[2]).length());
			characterfile.newLine();
			characterfile.write("minutes = ", 0, 10);
			characterfile.write(Integer.toString(p.getTotalTime().getTime()[1]), 0, Integer.toString(p.getTotalTime().getTime()[1]).length());
			characterfile.newLine();
			characterfile.newLine();

			/* EQUIPMENT */
			characterfile.write("[EQUIPMENT]", 0, 11);
			characterfile.newLine();
			for (int i = 0; i < p.playerEquipment.length; i++) {
				characterfile.write("character-equip = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipment[i]), 0, Integer.toString(p.playerEquipment[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerEquipmentN[i]), 0, Integer.toString(p.playerEquipmentN[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.newLine();
			}
			characterfile.newLine();

			/* LOOK */
			characterfile.write("[LOOK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < p.playerAppearance.length; i++) {
				characterfile.write("character-look = ", 0, 17);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerAppearance[i]), 0, Integer.toString(p.playerAppearance[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* SKILLS */
			characterfile.write("[SKILLS]", 0, 8);
			characterfile.newLine();
			for (int i = 0; i < p.playerLevel.length; i++) {
				characterfile.write("character-skill = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerLevel[i]), 0, Integer.toString(p.playerLevel[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.playerXP[i]), 0, Integer.toString(p.playerXP[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* ITEMS */
			characterfile.write("[ITEMS]", 0, 7);
			characterfile.newLine();
			for (int i = 0; i < p.playerItems.length; i++) {
				if (p.playerItems[i] > 0) {
					characterfile.write("character-item = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.playerItems[i]), 0, Integer.toString(p.playerItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.playerItemsN[i]), 0, Integer.toString(p.playerItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			characterfile.newLine();

			/* BANK */
			/* BANK */
			characterfile.write("[BANK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < Config.BANK_SIZE; j++) {
					if (j > p.getBank().getBankTab()[i].size() - 1)
						break;
					BankItem item = p.getBank().getBankTab()[i].getItem(j);
					if (item == null)
						continue;
					characterfile.write("bank-tab = " + i + "\t" + item.getId() + "\t" + item.getAmount());
					characterfile.newLine();
				}
			}

			characterfile.newLine();
			characterfile.newLine();

			/* FRIENDS */
			characterfile.write("[FRIENDS]", 0, 9);
			characterfile.newLine();
			for (Long friend : p.getFriends().getFriends()) {
				characterfile.write("character-friend = ", 0, 19);
				characterfile.write(Long.toString(friend), 0, Long.toString(friend).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			characterfile.newLine();

			/* IGNORES */
			characterfile.write("[IGNORES]", 0, 9);
			characterfile.newLine();
			for (Long ignore : p.getIgnores().getIgnores()) {
				characterfile.write("character-ignore = ", 0, 19);
				characterfile.write(Long.toString(ignore), 0, Long.toString(ignore).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			characterfile.newLine();

			characterfile.write("[POINTS]", 0, 8);
			characterfile.newLine();
			for (int index = 0; index < p.getPoints().length; index++) {
				characterfile.write("character-points = ", 0, 19);
				characterfile.write(Integer.toString(index), 0, Integer.toString(index).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(p.getPoints()[index]), 0, Integer.toString(p.getPoints()[index]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			characterfile.newLine();

			characterfile.write("[LOOTING_BAG]", 0, 13);
			characterfile.newLine();
			for (int i = 0; i < p.getLootItems().length; i++) {
				if (p.getLootItems()[i] > 0) {
					characterfile.write("character-lootbag = ", 0, 20);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.getLootItems()[i]), 0, Integer.toString(p.getLootItems()[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(p.getLootItemAmount()[i]), 0, Integer.toString(p.getLootItemAmount()[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			characterfile.write("[ACHIEVEMENTS]", 0, 14);
			characterfile.newLine();
			for (int key : p.getAchievements().getAchievementMap().keySet()) {
				Achievement achievement = p.getAchievements().getAchievement(key);
				characterfile.write("achievement = ", 0, 14);
				characterfile.write(Integer.toString(key), 0, Integer.toString(key).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(achievement.getCount()), 0, Integer.toString(achievement.getCount()).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			characterfile.write("[BOSS_LOGS]", 0, 11);
			characterfile.newLine();
			for (int key : p.getBossLogs().getBossLogtMap().keySet()) {
				BossLog logs = p.getBossLogs().getBossLog(key);
				characterfile.write("boss-logs = ", 0, 12);
				characterfile.write(Integer.toString(key), 0, Integer.toString(key).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(logs.getCount()), 0, Integer.toString(logs.getCount()).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* IGNORES */
			/*
			 * characterfile.write("[IGNORES]", 0, 9); characterfile.newLine();
			 * for (int i = 0; i < ignores.length; i++) { if (ignores[i] > 0) {
			 * characterfile.write("character-ignore = ", 0, 19);
			 * characterfile.write(Integer.toString(i), 0,
			 * Integer.toString(i).length()); characterfile.write("	", 0, 1);
			 * characterfile.write(Long.toString(ignores[i]), 0,
			 * Long.toString(ignores[i]).length()); characterfile.newLine(); } }
			 * characterfile.newLine();
			 */
			/* EOF */
			characterfile.write("[EOF]", 0, 5);
			characterfile.newLine();
			characterfile.newLine();
			characterfile.close();
		} catch (IOException ioexception) {
			Misc.println(p.playerName + ": error writing file.");
			return false;
		}
		return true;
	}

}