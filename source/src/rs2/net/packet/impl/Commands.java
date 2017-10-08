package rs2.net.packet.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import rs2.Config;
import rs2.Connection;
import rs2.Server;
import rs2.abyssalps.content.minigame.abyssal_sire.AbyssalSireMinigame;
import rs2.abyssalps.content.minigame.fightcave.FightCavesMinigame;
import rs2.abyssalps.content.minigame.kraken.KrakenMinigame;
import rs2.abyssalps.content.minigame.zombies.ZombiesDefinitions;
import rs2.abyssalps.content.minigame.zombies.ZombiesLobbyMinigame;
import rs2.abyssalps.content.minigame.zombies.ZombiesMinigame;
import rs2.abyssalps.content.minigame.zulrah.ZulrahMinigame;
import rs2.abyssalps.content.skill.magic.TeleportType;
import rs2.abyssalps.content.tab.InformationTab;
import rs2.abyssalps.content.vote.RewardParser;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.npcs.drop.Drops;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.Player;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.abyssalps.model.objects.Object;
import rs2.net.packet.PacketType;
import rs2.sanction.SanctionHandler;
import rs2.util.Misc;
import rs2.util.cache.region.Region;
import rs2.util.mysql.Donation;
import rs2.util.tools.log.LogSystem;
import rs2.util.tools.log.LogTypes;
import rs2.world.ShopHandler;

/**
 * Commands
 **/

public class Commands implements PacketType {

	public static String getFilteredInput(String input) {
		return input.replaceAll("\r", "");
	}

	private static void playerCommands(Client player, String command) {
		if (command.startsWith("/")) {
			if (SanctionHandler.isMuted(player)) {
				player.sendMessage("You are muted for breaking a rule.");
				return;
			}
			if (player.clan != null) {
				player.clan.sendChat(player, command.substring(1));
				return;
			}
			player.sendMessage("You can only do this in a clan chat..");
			return;
		}

		if (command.equalsIgnoreCase("vote")) {
			player.getPA().sendFrame126("www.abyssalps.com/vote/", 12000);
		}

		if (command.equalsIgnoreCase("store")) {
			player.getPA().sendFrame126("www.abyssalps.com/store/", 12000);
		}

		if (command.equalsIgnoreCase("highscores")) {
			player.getPA().sendFrame126("www.abyssalps.com/highscores.php",
					12000);
		}

		if (command.startsWith("claim")) {

			if (System.currentTimeMillis() - player.lastMYSQLConnection < 30000) {
				player.sendMessage("You can only use this command every once 30 seconds.");
				return;
			}
			new Thread(new Donation(player)).start();
			player.lastMYSQLConnection = System.currentTimeMillis();

		}

		if (command.equalsIgnoreCase("players")) {
			player.sendMessage("There are currently "
					+ PlayerHandler.getPlayerCount() + " people playing.");
		}

		if (command.startsWith("changepassword")) {
			String input = command.substring(15);
			if (input.length() > 20) {
				player.sendMessage("Passwords cannot contain more than 20 characters.");
				player.sendMessage("The password you tried had "
						+ input.length() + " characters.");
				return;
			}
			if (input.contains("character-rights")
					|| input.contains("[CHARACTER]")) {
				player.sendMessage("Your password contains illegal characters.");
				return;
			}
			player.playerPass = command.substring(15);
			player.sendMessage("Your password is now: " + player.playerPass);
		}

		if (command.equalsIgnoreCase("gamble")) {
			if (player.inWild()) {
				player.sendMessage("You can't do this in the wilderness.");
				return;
			}
			if (player.underAttackBy > 0 || player.underAttackBy2 > 0) {
				player.sendMessage("You can't do this while in combat.");
				return;
			}
			player.getPA().startTeleport(
					2086,
					4466,
					0,
					player.playerMagicBook == 1 ? TeleportType.ANCIENT
							: TeleportType.MODERN);
			player.sendMessage("Gambling is at your own risk.");
		}
	}

	private static void premiumCommands(Client player, String command) {
		if (command.startsWith("yell")) {
			if (System.currentTimeMillis() - player.lastYell < 10000) {
				player.sendMessage("You need to wait 10 seconds between each yell.");
				return;
			}
			if (SanctionHandler.isMuted(player)
					|| SanctionHandler.isIPMuted(player)) {
				player.sendMessage("You are currently muted and can't yell.");
				return;
			}
			String text = command.substring(5);
			if (text.length() <= 0) {
				player.sendMessage("You can't yell nothing.");
				return;
			}
			String startLine = "";
			switch (player.getRank()) {
			case 1:
				startLine = "[@red@Premium@bla@]<img=0>@blu@"
						+ Misc.formatPlayerName(player.playerName)
						+ "@bla@:@red@";
				break;

			case 2:
				startLine = "[@blu@Super@bla@]<img=1>@blu@"
						+ Misc.formatPlayerName(player.playerName)
						+ "@bla@:@red@";
				break;

			case 3:
				startLine = "[@gre@Extreme@bla@]<img=2>@blu@"
						+ Misc.formatPlayerName(player.playerName)
						+ "@bla@:@red@";
				break;

			case 4:
				startLine = "[@red@YouTuber@bla@]<img=3>@blu@"
						+ Misc.formatPlayerName(player.playerName)
						+ "@bla@:@red@";
				break;

			case 5:
				startLine = "[@blu@Supporter@bla@]<img=4>@blu@"
						+ Misc.formatPlayerName(player.playerName)
						+ "@bla@:@red@";
				break;

			case 6:
				startLine = "[@dre@Moderator@bla@]<img=5>@blu@"
						+ Misc.formatPlayerName(player.playerName)
						+ "@bla@:@red@";
				break;

			case 7:
				startLine = "[@yel@Administrator@bla@]<img=6>@blu@"
						+ Misc.formatPlayerName(player.playerName)
						+ "@bla@:@red@";
				break;

			case 8:
				startLine = "[@dre@Owner@bla@]<img=6>@blu@"
						+ Misc.formatPlayerName(player.playerName)
						+ "@bla@:@red@";
				break;
			}
			player.getPA().globalYell(startLine + " " + text);
			player.lastYell = System.currentTimeMillis();
		}
	}

	private static void superCommands(Client player, String command) {

	}

	private static void extremeCommands(Client player, String command) {

	}

	private static void youtuberCommands(Client player, String command) {

	}

	private static void supporterCommands(Client player, String command) {

		if (command.startsWith("kick")) {
			String playerName = command.substring(5);
			if (playerName.length() > 0) {
				for (int index = 0; index < PlayerHandler.players.length; index++) {
					if (PlayerHandler.players[index] == null) {
						continue;
					}
					if (!PlayerHandler.players[index].playerName.toLowerCase()
							.equalsIgnoreCase(playerName)) {
						continue;
					}
					if (PlayerHandler.players[index].underAttackBy > 0
							|| PlayerHandler.players[index].underAttackBy2 > 0) {
						player.sendMessage("You can't kick a player in combat.");
						continue;
					}
					if (PlayerHandler.players[index].inDuelArena()) {
						player.sendMessage("You can't kick a a person in the duel area.");
						continue;
					}
					PlayerHandler.players[index].disconnected = true;
					player.sendMessage("You kicked @blu@"
							+ Misc.formatPlayerName(PlayerHandler.players[index].playerName)
							+ "@bla@.");
				}
			}
		}

		if (command.startsWith("xteletome")) {
			String playerName = command.substring(10);
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				if (PlayerHandler.players[i] == null) {
					continue;
				}
				if (!PlayerHandler.players[i].playerName
						.equalsIgnoreCase(playerName)) {
					continue;
				}
				player.sendMessage("You have teleported to @blu@"
						+ PlayerHandler.players[i].playerName + "@bla@ to you.");
				((Client) PlayerHandler.players[i])
						.sendMessage("You have been teleported to @blu@"
								+ player.playerName + "@bla@.");
				((Client) PlayerHandler.players[i]).getPA().movePlayer(
						player.getX(), player.getY(), player.heightLevel);
			}
		}

		if (command.startsWith("xteleto")) {
			String playerName = command.substring(8);
			for (int i = 0; i < PlayerHandler.players.length; i++) {
				if (PlayerHandler.players[i] == null) {
					continue;
				}
				if (!PlayerHandler.players[i].playerName
						.equalsIgnoreCase(playerName)) {
					continue;
				}
				player.sendMessage("You have teleported to @blu@"
						+ PlayerHandler.players[i].playerName + "@bla@.");
				player.getPA().movePlayer(PlayerHandler.players[i].getX(),
						PlayerHandler.players[i].getY(),
						PlayerHandler.players[i].heightLevel);
			}
		}

	}

	private static void moderatorCommands(Client player, String command) {

		if (command.startsWith("checkinv")) {
			String input = command.substring(9);
			Optional<Player> optionalPlayer = PlayerHandler
					.getOptionalPlayer(input);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				player.getPA().otherInv(player, (Client) c2);
				player.getDH().sendDialogues(206, 0);
			} else {
				player.sendMessage(input
						+ " is not online. You can only check the inventory of online players.");
			}
		}

		if (command.startsWith("checkbank")) {
			String input = command.substring(10);
			if (PlayerHandler.updateRunning) {
				player.sendMessage("You cannot view a bank whilst the server is updating.");
				return;
			}
			Optional<Player> optionalPlayer = PlayerHandler
					.getOptionalPlayer(input);
			if (optionalPlayer.isPresent()) {
				player.getPA().openOtherBank((Client) optionalPlayer.get());
			} else {
				player.sendMessage(input
						+ " is not online. You can only view the bank of online players.");
			}
		}

		if (command.startsWith("jail")) {
			String playerName = command.substring(5);
			if (playerName.length() > 0) {
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] == null) {
						continue;
					}
					if (!PlayerHandler.players[i].playerName
							.equalsIgnoreCase(playerName)) {
						continue;
					}
					PlayerHandler.players[i].setJailed(true);
					((Client) PlayerHandler.players[i]).getPA().movePlayer(
							3104, 9517, 0);
					((Client) PlayerHandler.players[i]).sendMessage("@red@"
							+ Misc.formatPlayerName(player.playerName)
							+ "@bla@ jailed you.");
				}
			}
		}

		if (command.startsWith("unjail")) {
			String playerName = command.substring(7);
			if (playerName.length() > 0) {
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] == null) {
						continue;
					}
					if (!PlayerHandler.players[i].playerName
							.equalsIgnoreCase(playerName)) {
						continue;
					}
					PlayerHandler.players[i].setJailed(false);
					((Client) PlayerHandler.players[i]).getPA()
							.movePlayer(Config.START_LOCATION_X,
									Config.START_LOCATION_Y, 0);
					((Client) PlayerHandler.players[i]).sendMessage("@red@"
							+ Misc.formatPlayerName(player.playerName)
							+ "@bla@ unjailed you.");
				}
			}
		}

		if (command.startsWith("ipmute")) {
			String playerName = command.substring(7);
			if (playerName.length() > 0) {
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] == null) {
						continue;
					}
					if (!PlayerHandler.players[i].playerName
							.equalsIgnoreCase(playerName)) {
						continue;
					}
					((Client) PlayerHandler.players[i])
							.sendMessage("Your account has been IP muted by @blu@"
									+ player.playerName + "@bla@.");
					SanctionHandler
							.IPMutePlayer(PlayerHandler.players[i].connectedFrom);
				}
			}
		}

		if (command.startsWith("unmute")) {
			String playerName = command.substring(7);
			if (playerName.length() > 0) {
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] == null) {
						continue;
					}
					if (!PlayerHandler.players[i].playerName
							.equalsIgnoreCase(playerName)) {
						continue;
					}
					((Client) PlayerHandler.players[i])
							.sendMessage("Your account has been unmuted by @blu@"
									+ player.playerName + "@bla@.");
				}
				SanctionHandler.deleteFromSactionList(SanctionHandler.LOCATION
						+ "mutes.txt", playerName);
				player.sendMessage("You have unmuted: @blu@" + playerName
						+ "@bla@.");
			}
		}

		if (command.startsWith("mute")) {
			String playerName = command.substring(5);
			if (playerName.length() > 0) {
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] == null) {
						continue;
					}
					if (!PlayerHandler.players[i].playerName
							.equalsIgnoreCase(playerName)) {
						continue;
					}
					((Client) PlayerHandler.players[i])
							.sendMessage("Your account has temporarily been muted by @blu@"
									+ player.playerName + "@bla@.");
				}
				SanctionHandler.mutePlayer(playerName);
				player.sendMessage("You have muted: @blu@" + playerName
						+ "@bla@.");
			}
		}

	}

	private static void administratorCommands(Client player, String command) {

		if (command.startsWith("unban")) {
			String playerName = command.substring(6);
			if (playerName.length() > 0) {
				SanctionHandler.deleteFromSactionList(SanctionHandler.LOCATION
						+ "bans.txt", playerName);
				player.sendMessage("You have unbanned: @blu@" + playerName
						+ "@bla@.");
			}
		}

		if (command.startsWith("ban")) {
			String playerName = command.substring(4);
			if (playerName.length() > 0) {
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] == null) {
						continue;
					}
					if (!PlayerHandler.players[i].playerName
							.equalsIgnoreCase(playerName)) {
						continue;
					}
					PlayerHandler.players[i].disconnected = true;
				}
				SanctionHandler.banPlayer(playerName);
				player.sendMessage("You have banned: @blu@" + playerName
						+ "@bla@.");
			}
		}
		if (command.startsWith("macban")) {
			String playerName = command.substring(7);
			if (playerName.length() > 0) {
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] == null) {
						continue;
					}
					if (!PlayerHandler.players[i].playerName
							.equalsIgnoreCase(playerName)) {
						continue;
					}
					player.sendMessage("You have banned mac "
							+ ((Client) PlayerHandler.players[i])
									.getMacAddress());
					SanctionHandler
							.macBanPlayer(((Client) PlayerHandler.players[i])
									.getMacAddress());
					PlayerHandler.players[i].disconnected = true;
				}
			}
		}
		if (command.startsWith("ipban")) {
			String playerName = command.substring(6);
			if (playerName.length() > 0) {
				for (int i = 0; i < PlayerHandler.players.length; i++) {
					if (PlayerHandler.players[i] == null) {
						continue;
					}
					if (!PlayerHandler.players[i].playerName
							.equalsIgnoreCase(playerName)) {
						continue;
					}
					player.sendMessage("You have banned IP "
							+ PlayerHandler.players[i].connectedFrom);
					SanctionHandler
							.IPBanPlayer(PlayerHandler.players[i].connectedFrom);
					PlayerHandler.players[i].disconnected = true;
				}
			}
		}

	}

	private static void ownerCommands(Client player, String command) {

		if (command.startsWith("spec")) {
			String[] parameters = command.split(" ");
			if (parameters.length > 0) {
				player.specAmount = Double.valueOf(parameters[1]);
				player.getItems().addSpecialBar(player.playerEquipment[3]);
			}
		}
		if (command.startsWith("kraken")) {
			player.enterMinigame(new KrakenMinigame());
		}
		if (command.equalsIgnoreCase("reloadshops")) {
			Server.shopHandler = new ShopHandler();
			Server.shopHandler.loadShops("shops.cfg");
		}

		if (command.startsWith("update")) {
			String[] parameters = command.split(" ");
			if (parameters.length > 0) {
				PlayerHandler.updateSeconds = Integer.valueOf(parameters[1]);
				PlayerHandler.updateAnnounced = false;
				PlayerHandler.updateRunning = true;
				PlayerHandler.updateStartTime = System.currentTimeMillis();
			}
		}

		if (command.equals("sire")) {
			player.enterMinigame(new AbyssalSireMinigame());
		}

		if (command.startsWith("tele")) {
			String[] parameters = command.split(" ");
			int x = player.absX;
			int y = player.absY;
			int z = player.heightLevel;
			if (parameters.length > 1) {
				x = Integer.parseInt(parameters[1]);
			}
			if (parameters.length > 2) {
				y = Integer.parseInt(parameters[2]);
			}
			if (parameters.length > 3) {
				z = Integer.parseInt(parameters[3]);
			}
			player.getPA().movePlayer(x, y, z);
		}

		if (command.startsWith("interface")) {
			String[] parameters = command.split(" ");
			if (parameters.length > 0) {
				player.getPA().showInterface(Integer.valueOf(parameters[1]));
				player.getPA().walkableInterface(
						Integer.parseInt(parameters[1]));
			}
		}

		if (command.startsWith("anim")) {
			String[] parameters = command.split(" ");
			if (parameters.length > 0) {
				player.startAnimation(Integer.valueOf(parameters[1]));
			}
		}

		if (command.startsWith("gfx")) {
			String[] parameters = command.split(" ");
			if (parameters.length > 0) {
				player.gfx100(Integer.valueOf(parameters[1]));
			}
		}

		if (command.equalsIgnoreCase("pos")) {
			player.sendMessage("Position: " + player.getX() + " "
					+ player.getY() + " " + player.heightLevel + "");
		}

		if (command.startsWith("move")) {
			String[] parameters = command.split(" ");
			if (parameters.length > 0) {
				if (parameters.length > 3) {
					player.getPA().movePlayer(Integer.valueOf(parameters[1]),
							Integer.valueOf(parameters[2]),
							Integer.valueOf(parameters[3]));
				} else {
					player.getPA().movePlayer(Integer.valueOf(parameters[1]),
							Integer.valueOf(parameters[2]), 0);
				}
			}
		}

		if (command.equalsIgnoreCase("cannon")) {
			player.getCannon().assembleCannon();
		}

		if (command.startsWith("object")) {
			int objectId = Integer.valueOf(command.substring(7));
			if (objectId > 0) {
				new Object(objectId, player.getX(), player.getY(),
						player.heightLevel, 1, 10, -1, 10);
			}
		}

		if (command.startsWith("mb")) {
			String parameters[] = command.split(" ");

			if (parameters.length > 0) {

				int amount = Integer.valueOf(parameters[1]);

				for (int i = 0; i < amount; i++) {
					for (GameItem item : RewardParser.getRandomDrop(6199)) {
						player.getItems()
								.sendItemToAnyTab(item.id, item.amount);
					}
				}
				player.getItems().resetBank();
				player.getItems().resetTempItems();
				player.sendMessage(amount
						+ " Mystery Box rewards has been added to your bank.");

			}
		}

		if (command.startsWith("drop")) {
			String parameters[] = command.split(" ");

			if (parameters.length > 0) {
				int id = Integer.valueOf(parameters[1]);
				int amount = Integer.valueOf(parameters[2]);
				for (int i = 0; i < amount; i++) {
					for (GameItem item : Drops.getRandomDrop(player, id)) {
						player.getItems()
								.sendItemToAnyTab(item.id, item.amount);
					}
				}
				player.getItems().resetBank();
				player.getItems().resetTempItems();
				player.sendMessage(amount + " drops for NPC: " + id
						+ " has been added to your bank.");
			}
		}

		if (command.equalsIgnoreCase("rollback")) {
			player.disconnected = true;
			player.rollback = true;
		}

		if (command.startsWith("item")) {
			String[] parameters = command.split(" ");
			if (parameters.length > 0) {
				int itemId = Integer.valueOf(parameters[1]);
				int amount = Integer.valueOf(parameters[2]);
				if (amount > Integer.MAX_VALUE) {
					player.sendMessage("You cannot spawn more than "
							+ Integer.MAX_VALUE + " in amounts.");
					return;
				}
				if (itemId < 0 || itemId >= Config.ITEM_LIMIT) {
					player.sendMessage("This item is null.");
					return;
				}
				player.getItems().addItem(itemId, amount);
				player.sendMessage("You successfully spawn " + amount + " "
						+ ItemDefinitions.forId(itemId).getName() + "'s.");
			}
		}

		if (command.startsWith("pnpc")) {
			int npc = Integer.parseInt(command.substring(5));
			if (npc < 9999) {
				player.npcId2 = npc;
				player.isNpc = true;
				player.updateRequired = true;
				player.appearanceUpdateRequired = true;
			}
		}
		if (command.startsWith("unpc")) {
			player.isNpc = false;
			player.updateRequired = true;
			player.appearanceUpdateRequired = true;
		}

		if (command.startsWith("spawn")) {
			int newNPC = Integer.parseInt(command.substring(6));
			if (newNPC > 0) {
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							"./Data/cfg/spawn-config.cfg", true));
					writer.write("spawn = " + newNPC + "	" + player.getX()
							+ "	" + player.getY() + "	" + player.heightLevel
							+ "	1	0	0	0");
					writer.newLine();
					writer.close();
					Server.npcHandler.spawnNpc(player, newNPC, player.absX,
							player.absY, player.heightLevel, 0, 0, 0, 0, 0,
							false, false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (command.startsWith("test")) {
			player.getRecipe().enter();
		}
		if (command.startsWith("npc")) {
			try {
				int newNPC = Integer.parseInt(command.substring(4));
				if (newNPC > 0) {
					Server.npcHandler.spawnNpc(player, newNPC, player.absX,
							player.absY, player.heightLevel, 0, 2000, 0, 70,
							70, false, false);
					player.sendMessage("You spawn a Npc.");
				} else {
					player.sendMessage("No such NPC.");
				}
			} catch (Exception e) {

			}
		}

		if (command.equalsIgnoreCase("master")) {
			for (int slot = 0; slot < player.playerLevel.length; slot++) {
				player.playerLevel[slot] = 99;
				player.playerXP[slot] = player.getPA().getXPForLevel(100);
				player.getPA().refreshSkill(slot);
				player.getPA().requestUpdates();
			}
		}
	}

	private static void economyCleanerCommands(Client player, String command) {
		if (command.startsWith("lockhp")) {
			String[] parameters = command.split(" ");

			if (parameters.length > 0) {
				player.setConstitutionState(Boolean.parseBoolean(parameters[1]));
				if (player.isConstitutionLocked()) {
					player.sendMessage("You have locked your hitpoints.");
				} else {
					player.sendMessage("You have unlocked your hitpoints.");
				}
			}
		}
	}

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		String playerCommand = c.getInStream().readString();

		if (c.getRank() >= 0) {
			playerCommands(c, playerCommand);
		}
		if (c.getRank() >= 1) {
			premiumCommands(c, playerCommand);
		}
		if (c.getRank() >= 2) {
			superCommands(c, playerCommand);
		}
		if (c.getRank() >= 3) {
			extremeCommands(c, playerCommand);
		}
		if (c.getRank() >= 4) {
			youtuberCommands(c, playerCommand);
		}
		if (c.getRank() >= 5) {
			supporterCommands(c, playerCommand);
		}
		if (c.getRank() >= 6) {
			moderatorCommands(c, playerCommand);
		}
		if (c.getRank() >= 7) {
			administratorCommands(c, playerCommand);
		}
		if (c.getRank() >= 8) {
			ownerCommands(c, playerCommand);
		}
		if (c.getRank() >= 9) {
			economyCleanerCommands(c, playerCommand);
		}

		if (c.getRank() >= 5 && c.getRank() <= 9) {
			LogSystem.writeToFile(c.playerName,
					Misc.formatPlayerName(c.playerName)
							+ " used the command ; " + playerCommand + "",
					LogTypes.COMMANDS);
		}
	}
}