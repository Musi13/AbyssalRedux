package rs2.abyssalps.model.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.content.bank.BankTab;
import rs2.abyssalps.content.minigame.fightpits.FightPitsMinigame;
import rs2.abyssalps.content.minigame.pestcontrol.PestControl;
import rs2.abyssalps.content.minigame.zulrah.ZulrahConstants;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.content.multiplayer.duel.DuelSessionRules.Rule;
import rs2.abyssalps.content.skill.cooking.Cooking;
import rs2.abyssalps.content.skill.crafting.GemCutting;
import rs2.abyssalps.content.skill.firemaking.Firemaking;
import rs2.abyssalps.content.skill.fishing.Fishing;
import rs2.abyssalps.content.skill.fletching.Fletching;
import rs2.abyssalps.content.skill.magic.TeleportType;
import rs2.abyssalps.content.skill.mining.Mining;
import rs2.abyssalps.content.skill.smithing.SmithingAction;
import rs2.abyssalps.content.skill.smithing.impl.SmeltAction;
import rs2.abyssalps.content.skill.woodcutting.Woodcutting;
import rs2.abyssalps.model.Boundary;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.util.Misc;
import rs2.util.cache.region.Region;
import rs2.util.tools.event.CycleEventHandler;
import rs2.world.Clan;

public class PlayerAssistant {

	private Client c;

	public PlayerAssistant(Client Client) {
		this.c = Client;
	}

	public int CraftInt, Dcolor, FletchInt;

	public void globalYell(String txt) {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] == null)
				continue;
			((Client) PlayerHandler.players[i]).sendMessage(txt);
		}
	}

	/**
	 * MulitCombat icon
	 * 
	 * @param i1
	 *            0 = off 1 = on
	 */
	public void multiWay(int i1) {

		c.outStream.createFrame(61);
		c.outStream.writeByte(i1);
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	/*
	 * Vengeance
	 */
	public void castVeng() {
		if (c.playerLevel[6] < 94) {
			c.sendMessage("You need a magic level of 94 to cast this spell.");
			return;
		}
		if (c.playerLevel[1] < 40) {
			c.sendMessage("You need a defence level of 40 to cast this spell.");
			return;
		}
		if (!c.getItems().playerHasItem(9075, 4)
				|| !c.getItems().playerHasItem(557, 10)
				|| !c.getItems().playerHasItem(560, 2)) {
			c.sendMessage("You don't have the required runes to cast this spell.");
			return;
		}
		if (System.currentTimeMillis() - c.lastCast < 30000) {
			c.sendMessage("You can only cast vengeance every 30 seconds.");
			return;
		}
		DuelSession session = (DuelSession) Server
				.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
		if (!Objects.isNull(session)) {
			if (session.getRules().contains(Rule.NO_MAGE)) {
				c.sendMessage("You can't cast this spell because magic has been disabled.");
				return;
			}
		}
		if (c.vengOn) {
			c.sendMessage("You already have vengeance casted.");
			return;
		}
		c.startAnimation(4410);
		c.gfx100(726);// Just use c.gfx100
		c.getItems().deleteItem2(9075, 4);
		c.getItems().deleteItem2(557, 10);// For these you need to change to
											// deleteItem(item, itemslot,
											// amount);.
		c.getItems().deleteItem2(560, 2);
		addSkillXP(112, 6);
		refreshSkill(6);
		c.vengOn = true;
		c.lastCast = System.currentTimeMillis();
	}

	public Clan getClan() {
		if (Server.clanManager.clanExists(c.playerName)) {
			return Server.clanManager.getClan(c.playerName);
		}
		return null;
	}

	public void sendClan(String name, String message, String clan, int rights) {
		c.outStream.createFrameVarSizeWord(217);
		c.outStream.writeString(name);
		c.outStream.writeString(Misc.formatPlayerName(message));
		c.outStream.writeString(clan);
		c.outStream.writeWord(rights);
		c.outStream.endFrameVarSize();
	}

	public void clearClanChat() {
		c.clanId = -1;
		c.getPA().sendFrame126("Talking in: ", 18139);
		c.getPA().sendFrame126("Owner: ", 18140);
		for (int j = 18144; j < 18244; j++) {
			c.getPA().sendFrame126("", j);
		}
	}

	public void setClanData() {
		boolean exists = Server.clanManager.clanExists(c.playerName);
		if (!exists || c.clan == null) {
			sendFrame126("Join chat", 18135);
			sendFrame126("Talking in: Not in chat", 18139);
			sendFrame126("Owner: None", 18140);
		}
		if (!exists) {
			sendFrame126("Chat Disabled", 18306);
			String title = "";
			for (int id = 18307; id < 18317; id += 3) {
				if (id == 18307) {
					title = "Anyone";
				} else if (id == 18310) {
					title = "Anyone";
				} else if (id == 18313) {
					title = "General+";
				} else if (id == 18316) {
					title = "Only Me";
				}
				sendFrame126(title, id + 2);
			}
			for (int index = 0; index < 100; index++) {
				sendFrame126("", 18323 + index);
			}
			for (int index = 0; index < 100; index++) {
				sendFrame126("", 18424 + index);
			}
			return;
		}
		Clan clan = Server.clanManager.getClan(c.playerName);
		sendFrame126(clan.getTitle(), 18306);
		String title = "";
		for (int id = 18307; id < 18317; id += 3) {
			if (id == 18307) {
				title = clan.getRankTitle(clan.whoCanJoin)
						+ (clan.whoCanJoin > Clan.Rank.ANYONE
								&& clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18310) {
				title = clan.getRankTitle(clan.whoCanTalk)
						+ (clan.whoCanTalk > Clan.Rank.ANYONE
								&& clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18313) {
				title = clan.getRankTitle(clan.whoCanKick)
						+ (clan.whoCanKick > Clan.Rank.ANYONE
								&& clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18316) {
				title = clan.getRankTitle(clan.whoCanBan)
						+ (clan.whoCanBan > Clan.Rank.ANYONE
								&& clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
			}
			sendFrame126(title, id + 2);
		}
		if (clan.rankedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.rankedMembers.size()) {
					sendFrame126("<clan=" + clan.ranks.get(index) + ">"
							+ clan.rankedMembers.get(index), 18323 + index);
				} else {
					sendFrame126("", 18323 + index);
				}
			}
		}
		if (clan.bannedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.bannedMembers.size()) {
					sendFrame126(clan.bannedMembers.get(index), 18424 + index);
				} else {
					sendFrame126("", 18424 + index);
				}
			}
		}
	}

	public void resetAutocast() {
		c.autocastId = 0;
		c.autocasting = false;
		c.getPA().sendFrame36(108, 0);
	}

	public void sendFrame126(String s, int id) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(126);
			c.getOutStream().writeString(s);
			c.getOutStream().writeWordA(id);
			c.getOutStream().endFrameVarSizeWord();
			c.flushOutStream();
		}
	}

	public void sendLink(String s) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrameVarSizeWord(187);
			c.getOutStream().writeString(s);
		}
	}

	public void setSkillLevel(int skillNum, int currentLevel, int XP) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(134);
			c.getOutStream().writeByte(skillNum);
			c.getOutStream().writeDWord_v1(XP);
			c.getOutStream().writeByte(currentLevel);
			c.flushOutStream();
		}
	}

	public void sendFrame106(int sideIcon) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(106);
			c.getOutStream().writeByteC(sideIcon);
			c.flushOutStream();
			requestUpdates();
		}
	}

	public void sendFrame107() {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(107);
			c.flushOutStream();
		}
	}

	public void sendFrame36(int id, int state) {
		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(36);
			c.getOutStream().writeWordBigEndian(id);
			c.getOutStream().writeByte(state);
			c.flushOutStream();
		}
	}

	public void sendFrame185(int Frame) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(185);
			c.getOutStream().writeWordBigEndianA(Frame);
		}
	}

	public void showInterface(int interfaceid) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(97);
			c.getOutStream().writeWord(interfaceid);
			c.flushOutStream();
		}
	}

	public void sendFrame248(int MainFrame, int SubFrame) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(246);
			c.getOutStream().writeWordBigEndian(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.getOutStream().writeWord(SubFrame2);
			c.flushOutStream();
		}
	}

	public void sendFrame171(int MainFrame, int SubFrame) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(171);
			c.getOutStream().writeByte(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame200(int MainFrame, int SubFrame) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(200);
			c.getOutStream().writeWord(MainFrame);
			c.getOutStream().writeWord(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame70(int i, int o, int id) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(70);
			c.getOutStream().writeWord(i);
			c.getOutStream().writeWordBigEndian(o);
			c.getOutStream().writeWordBigEndian(id);
			c.flushOutStream();
		}
	}

	public void sendFrame75(int MainFrame, int SubFrame) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(75);
			c.getOutStream().writeWordBigEndianA(MainFrame);
			c.getOutStream().writeWordBigEndianA(SubFrame);
			c.flushOutStream();
		}
	}

	public void sendFrame164(int Frame) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(164);
			c.getOutStream().writeWordBigEndian_dup(Frame);
			c.flushOutStream();
		}
	}

	public void sendFriend(long friend, int world) {
		if (world != 0) {
			world += 9;
		} else if (!Config.WORLD_LIST_FIX) {
			world += 1;
		}
		c.getOutStream().createFrame(50);
		c.getOutStream().writeQWord(friend);
		c.getOutStream().writeByte(world);
	}

	public void setPrivateMessaging(int i) { // friends and ignore list status

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(221);
			c.getOutStream().writeByte(i);
			c.flushOutStream();
		}
	}

	public void setChatOptions(int publicChat, int privateChat, int tradeBlock) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(206);
			c.getOutStream().writeByte(publicChat);
			c.getOutStream().writeByte(privateChat);
			c.getOutStream().writeByte(tradeBlock);
			c.flushOutStream();
		}
	}

	public void sendFrame87(int id, int state) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(87);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.getOutStream().writeDWord_v1(state);
			c.flushOutStream();
		}

	}

	public void sendPM(long name, int rights, byte[] chatMessage) {
		c.getOutStream().createFrameVarSize(196);
		c.getOutStream().writeQWord(name);
		c.getOutStream().writeDWord(new Random().nextInt());
		c.getOutStream().writeByte(rights);
		c.getOutStream().writeBytes(chatMessage, chatMessage.length, 0);
		c.getOutStream().endFrameVarSize();
	}

	public void createPlayerHints(int type, int id) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(type);
			c.getOutStream().writeWord(id);
			c.getOutStream().write3Byte(0);
			c.flushOutStream();
		}
	}

	public void createObjectHints(int x, int y, int height, int pos) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(254);
			c.getOutStream().writeByte(pos);
			c.getOutStream().writeWord(x);
			c.getOutStream().writeWord(y);
			c.getOutStream().writeByte(height);
			c.flushOutStream();
		}
	}

	public void loadPM(long playerName, int world) {

		if (c.getOutStream() != null && c != null) {
			if (world != 0) {
				world += 9;
			} else if (!Config.WORLD_LIST_FIX) {
				world += 1;
			}
			c.getOutStream().createFrame(50);
			c.getOutStream().writeQWord(playerName);
			c.getOutStream().writeByte(world);
			c.flushOutStream();
		}
	}

	public void removeAllWindows() {

		if (c.getOutStream() != null && c != null) {
			c.getPA().resetVariables();
			c.getOutStream().createFrame(219);
			c.flushOutStream();
		}
	}

	public void closeAllWindows() {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(219);
			c.flushOutStream();
		}
	}

	public void sendFrame34(int id, int slot, int column, int amount) {

		if (c.getOutStream() != null && c != null) {
			c.outStream.createFrameVarSizeWord(34); // init item to smith
													// screen
			c.outStream.writeWord(column); // Column Across Smith Screen
			c.outStream.writeByte(4); // Total Rows?
			c.outStream.writeDWord(slot); // Row Down The Smith Screen
			c.outStream.writeWord(id + 1); // item
			c.outStream.writeByte(amount); // how many there are?
			c.outStream.endFrameVarSizeWord();
		}
	}

	public void walkableInterface(int id) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(208);
			c.getOutStream().writeWordBigEndian_dup(id);
			c.flushOutStream();
		}
	}

	public int mapStatus = 0;

	public void sendFrame99(int state) { // used for disabling map

		if (c.getOutStream() != null && c != null) {
			if (mapStatus != state) {
				mapStatus = state;
				c.getOutStream().createFrame(99);
				c.getOutStream().writeByte(state);
				c.flushOutStream();
			}
		}
	}

	public void sendCrashFrame() { // used for crashing cheat clients

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(123);
			c.flushOutStream();
		}
	}

	/**
	 * Reseting animations for everyone
	 **/

	public void frame1() {

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Client person = (Client) PlayerHandler.players[i];
				if (person != null) {
					if (person.getOutStream() != null && !person.disconnected) {
						if (c.distanceToPoint(person.getX(), person.getY()) <= 25) {
							person.getOutStream().createFrame(1);
							person.flushOutStream();
							person.getPA().requestUpdates();
						}
					}
				}
			}
		}
	}

	/**
	 * Creating projectile
	 **/
	public void createProjectile(int x, int y, int offX, int offY, int angle,
			int speed, int gfxMoving, int startHeight, int endHeight,
			int lockon, int time) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(16);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}
	}

	public void createProjectile(int x, int y, int offX, int offY, int angle,
			int speed, int gfxMoving, int startHeight, int endHeight,
			int lockon, int time, int slope, int lol) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(slope);
			c.getOutStream().writeByte(lol);
			c.flushOutStream();
		}
	}

	public void createProjectile(int x, int y, int offX, int offY, int angle,
			int speed, int gfxMoving, int startHeight, int endHeight,
			int lockon, int time, int slope) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(slope);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}
	}

	public void createNpcProjectile(int projectileId, NPC npc, int offsetX,
			int offsetY, int delay) {
		int nX = npc.getX();
		int nY = npc.getY();
		if (c.absX > npc.absX) {
			nX += offsetX;
		}
		if (c.absY > npc.absY) {
			nY += offsetY;
		}
		int pX = c.getX();
		int pY = c.getY();
		int offX = (nY - pY) * -1;
		int offY = (nX - pX) * -1;
		int speed = 90 + (npc.distanceTo(c.getX(), c.getY()) * 5);
		c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, speed,
				projectileId, 83, 33, -c.getId() - 1, delay, 0, 36);

	}

	public void createLocationPorjectile(int projectileId, int locationX,
			int locationY, int startTime, int endTime) {
		int nX = c.getX();
		int nY = c.getY();
		int pX = locationX;
		int pY = locationY;
		int offX = (nY - pY) * -1;
		int offY = (nX - pX) * -1;
		c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, endTime,
				projectileId, 33, 0, -1, startTime, 0, 36);

	}

	public void createPlayersProjectile(int x, int y, int offX, int offY,
			int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time, int slope, int lol) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							if (p.heightLevel == c.heightLevel)
								person.getPA().createProjectile(x, y, offX,
										offY, angle, speed, gfxMoving,
										startHeight, endHeight, lockon, time,
										slope, lol);
						}
					}
				}
			}
		}
	}

	public void createPlayersProjectile(int x, int y, int offX, int offY,
			int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time, int slope) {

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							if (p.teleportToHeight == c.teleportToHeight)
								person.getPA().createProjectile(x, y, offX,
										offY, angle, speed, gfxMoving,
										startHeight, endHeight, lockon, time,
										slope);
						}
					}
				}
			}
		}
	}

	public void createProjectile2(int x, int y, int offX, int offY, int angle,
			int speed, int gfxMoving, int startHeight, int endHeight,
			int lockon, int time, int slope) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC((y - (c.getMapRegionY() * 8)) - 2);
			c.getOutStream().writeByteC((x - (c.getMapRegionX() * 8)) - 3);
			c.getOutStream().createFrame(117);
			c.getOutStream().writeByte(angle);
			c.getOutStream().writeByte(offY);
			c.getOutStream().writeByte(offX);
			c.getOutStream().writeWord(lockon);
			c.getOutStream().writeWord(gfxMoving);
			c.getOutStream().writeByte(startHeight);
			c.getOutStream().writeByte(endHeight);
			c.getOutStream().writeWord(time);
			c.getOutStream().writeWord(speed);
			c.getOutStream().writeByte(slope);
			c.getOutStream().writeByte(64);
			c.flushOutStream();
		}
	}

	// projectiles for everyone within 25 squares
	public void createPlayersProjectile(int x, int y, int offX, int offY,
			int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time) {

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							if (p.heightLevel == c.heightLevel)
								person.getPA().createProjectile(x, y, offX,
										offY, angle, speed, gfxMoving,
										startHeight, endHeight, lockon, time);
						}
					}
				}
			}
		}
	}

	public void createPlayersProjectile2(int x, int y, int offX, int offY,
			int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time, int slope) {

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().createProjectile2(x, y, offX, offY,
									angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time, slope);
						}
					}
				}
			}
		}
	}

	/**
	 ** GFX
	 **/
	public void stillGfx(int id, int x, int y, int height, int time) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(y - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(x - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(4);
			c.getOutStream().writeByte(0);
			c.getOutStream().writeWord(id);
			c.getOutStream().writeByte(height);
			c.getOutStream().writeWord(time);
			c.flushOutStream();
		}
	}

	// creates gfx for everyone
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Client person = (Client) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().stillGfx(id, x, y, height, time);
						}
					}
				}
			}
		}
	}

	/**
	 * Objects, add and remove
	 **/
	public void object(int objectId, int objectX, int objectY, int face,
			int objectType) {

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}
	}

	public void checkObjectSpawn(int objectId, int objectX, int objectY,
			int face, int objectType) {
		if (c.distanceToPoint(objectX, objectY) > 60)
			return;

		if (c.getOutStream() != null && c != null) {
			c.getOutStream().createFrame(85);
			c.getOutStream().writeByteC(objectY - (c.getMapRegionY() * 8));
			c.getOutStream().writeByteC(objectX - (c.getMapRegionX() * 8));
			c.getOutStream().createFrame(101);
			c.getOutStream().writeByteC((objectType << 2) + (face & 3));
			c.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				c.getOutStream().createFrame(151);
				c.getOutStream().writeByteS(0);
				c.getOutStream().writeWordBigEndian(objectId);
				c.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			c.flushOutStream();
		}
	}

	/**
	 * Show option, attack, trade, follow etc
	 **/
	public String optionType = "null";

	public void showOption(int i, int l, String s, int a) {

		if (c.getOutStream() != null && c != null) {
			if (!optionType.equalsIgnoreCase(s)) {
				optionType = s;
				c.getOutStream().createFrameVarSize(104);
				c.getOutStream().writeByteC(i);
				c.getOutStream().writeByteA(l);
				c.getOutStream().writeString(s);
				c.getOutStream().endFrameVarSize();
				c.flushOutStream();
			}
		}
	}

	/**
	 * Open bank
	 **/
	public void openUpBank() {
		if (c.getBankPin().getFullPin().equalsIgnoreCase("") && c.setPin
				&& !c.getPA().viewingOtherBank) {
			c.getBankPin().open();
			return;
		}
		resetVariables();
		if (c.takeAsNote)
			sendFrame36(115, 1);
		else
			sendFrame36(115, 0);

		if (c.inWild()) {
			c.sendMessage("You can't bank in the wilderness!");
			return;
		}
		if (c.getBank().getBankSearch().isSearching()) {
			c.getBank().getBankSearch().reset();
		}
		c.getPA().sendFrame126("Search", 58063);
		if (c.getOutStream() != null && c != null) {
			c.isBanking = true;
			c.getItems().resetItems(5064);
			c.getItems().resetBank();
			c.getItems().resetTempItems();
			c.getOutStream().createFrame(248);
			c.getOutStream().writeWordA(5292);// ok perfect
			c.getOutStream().writeWord(5063);
			c.flushOutStream();
		}
	}

	public boolean viewingOtherBank;

	public void resetOtherBank() {
		for (int i = 0; i < backupTabs.length; i++)
			c.getBank().setBankTab(i, backupTabs[i]);
		viewingOtherBank = false;
		removeAllWindows();
		c.isBanking = false;
		c.getBank().setCurrentBankTab(c.getBank().getBankTab()[0]);
		c.getItems().resetBank();
		c.sendMessage("You are no longer viewing another players bank.");
	}

	public void otherInv(Client c, Client o) {
		if (o == c || o == null || c == null)
			return;
		int[] backupItems = c.playerItems;
		int[] backupItemsN = c.playerItemsN;
		c.playerItems = o.playerItems;
		c.playerItemsN = o.playerItemsN;
		c.playerItems = backupItems;
		c.playerItemsN = backupItemsN;
		c.getItems().resetItems(3214);
	}

	public void openOtherBank(Client otherPlayer) {
		if (otherPlayer == null)
			return;

		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
			return;
		}
		if (otherPlayer.getPA().viewingOtherBank) {
			c.sendMessage("That player is viewing another players bank, please wait.");
			return;
		}
		for (int i = 0; i < backupTabs.length; i++)
			backupTabs[i] = c.getBank().getBankTab(i);
		for (int i = 0; i < otherPlayer.getBank().getBankTab().length; i++)
			c.getBank().setBankTab(i, otherPlayer.getBank().getBankTab(i));
		c.getBank().setCurrentBankTab(c.getBank().getBankTab(0));
		viewingOtherBank = true;
		openUpBank();
	}

	public void sendFrame34a(int frame, int item, int slot, int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	BankTab[] backupTabs = new BankTab[9];

	/**
	 * Private Messaging
	 **/
	public void logIntoPM() {
		setPrivateMessaging(2);
		for (int i1 = 0; i1 < Config.MAX_PLAYERS; i1++) {
			Player p = PlayerHandler.players[i1];
			if (p != null && p.isActive) {
				Client o = (Client) p;
				if (o != null) {
					o.getPA().updatePM(c.playerId, 1);
				}
			}
		}
		boolean pmLoaded = false;

		for (int i = 0; i < c.friends.length; i++) {
			if (c.friends[i] != 0) {
				for (int i2 = 1; i2 < Config.MAX_PLAYERS; i2++) {
					Player p = PlayerHandler.players[i2];
					if (p != null
							&& p.isActive
							&& Misc.playerNameToInt64(p.playerName) == c.friends[i]) {
						Client o = (Client) p;
						if (o != null) {
							if (c.getRank() >= 2
									|| p.privateChat == 0
									|| (p.privateChat == 1 && o
											.getPA()
											.isInPM(Misc
													.playerNameToInt64(c.playerName)))) {
								loadPM(c.friends[i], 1);
								pmLoaded = true;
							}
							break;
						}
					}
				}
				if (!pmLoaded) {
					loadPM(c.friends[i], 0);
				}
				pmLoaded = false;
			}
			for (int i1 = 1; i1 < Config.MAX_PLAYERS; i1++) {
				Player p = PlayerHandler.players[i1];
				if (p != null && p.isActive) {
					Client o = (Client) p;
					if (o != null) {
						o.getPA().updatePM(c.playerId, 1);
					}
				}
			}
		}
	}

	public void updatePM(int pID, int world) { // used for private chat updates
		Player p = PlayerHandler.players[pID];
		if (p == null || p.playerName == null || p.playerName.equals("null")) {
			return;
		}
		Client o = (Client) p;
		if (o == null) {
			return;
		}
		long l = Misc.playerNameToInt64(PlayerHandler.players[pID].playerName);

		if (p.privateChat == 0) {
			for (int i = 0; i < c.friends.length; i++) {
				if (c.friends[i] != 0) {
					if (l == c.friends[i]) {
						loadPM(l, world);
						return;
					}
				}
			}
		} else if (p.privateChat == 1) {
			for (int i = 0; i < c.friends.length; i++) {
				if (c.friends[i] != 0) {
					if (l == c.friends[i]) {
						if (o.getPA().isInPM(
								Misc.playerNameToInt64(c.playerName))) {
							loadPM(l, world);
							return;
						} else {
							loadPM(l, 0);
							return;
						}
					}
				}
			}
		} else if (p.privateChat == 2) {
			for (int i = 0; i < c.friends.length; i++) {
				if (c.friends[i] != 0) {
					if (l == c.friends[i] && c.getRank() < 2) {
						loadPM(l, 0);
						return;
					}
				}
			}
		}
	}

	public boolean isInPM(long l) {
		for (int i = 0; i < c.friends.length; i++) {
			if (c.friends[i] != 0) {
				if (l == c.friends[i]) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Drink AntiPosion Potions
	 * 
	 * @param itemId
	 *            The itemId
	 * @param itemSlot
	 *            The itemSlot
	 * @param newItemId
	 *            The new item After Drinking
	 * @param healType
	 *            The type of poison it heals
	 */
	public void potionPoisonHeal(int itemId, int itemSlot, int newItemId,
			int healType) {
		c.attackTimer = c.getCombat().getAttackDelay(
				c.getItems().getItemName(c.playerEquipment[c.playerWeapon])
						.toLowerCase());
		if (c.duelRule[5]) {
			c.sendMessage("Potions has been disabled in this duel!");
			return;
		}
		if (!c.isDead && System.currentTimeMillis() - c.foodDelay > 2000) {
			if (c.getItems().playerHasItem(itemId, 1, itemSlot)) {
				c.sendMessage("You drink the "
						+ c.getItems().getItemName(itemId).toLowerCase() + ".");
				c.foodDelay = System.currentTimeMillis();
				// Actions
				if (healType == 1) {
					// Cures The Poison
				} else if (healType == 2) {
					// Cures The Poison + protects from getting poison again
				}
				c.startAnimation(0x33D);
				c.getItems().deleteItem(itemId, itemSlot, 1);
				c.getItems().addItem(newItemId, 1);
				requestUpdates();
			}
		}
	}

	/**
	 * Magic on items
	 **/

	public void magicOnItems(int slot, int itemId, int spellId) {
		if (!c.getItems().playerHasItem(itemId, 1, slot)) {
			// add a method here for logging cheaters(If you want)
			return;
		}

		switch (spellId) {
		case 1162: // low alch
			if (System.currentTimeMillis() - c.alchDelay > 1000) {
				if (!c.getCombat().checkMagicReqs(49)) {
					break;
				}
				if (itemId == 995) {
					c.sendMessage("You can't alch coins");
					break;
				}
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995,
						c.getShops().getItemShopValue(itemId) / 3);
				c.startAnimation(c.MAGIC_SPELLS[49][2]);
				c.gfx100(c.MAGIC_SPELLS[49][3]);
				c.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(c.MAGIC_SPELLS[49][7] * Config.MAGIC_EXP_RATE, 6);
				refreshSkill(6);
			}
			break;

		case 1178: // high alch
			if (System.currentTimeMillis() - c.alchDelay > 2000) {
				if (!c.getCombat().checkMagicReqs(50)) {
					break;
				}
				if (itemId == 995) {
					c.sendMessage("You can't alch coins");
					break;
				}
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995,
						(int) (c.getShops().getItemShopValue(itemId) * .75));
				c.startAnimation(c.MAGIC_SPELLS[50][2]);
				c.gfx100(c.MAGIC_SPELLS[50][3]);
				c.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(c.MAGIC_SPELLS[50][7] * Config.MAGIC_EXP_RATE, 6);
				refreshSkill(6);
			}
			break;
		case 1155:
			handleAlt(itemId);
			break;
		}
	}

	/**
	 * Dieing
	 **/

	public void applyDead() {
		c.respawnTimer = 15;
		c.isDead = false;
		DuelSession duelSession = (DuelSession) Server
				.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession)
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
			duelSession = null;
		}
		if (Objects.isNull(duelSession)) {
			c.killerId = findKiller();
			Client o = (Client) PlayerHandler.players[c.killerId];
			if (o != null) {
				if (c.killerId != c.playerId) {
					o.sendMessage("You have defeated " + c.playerName + "!");
					c.sendMessage("Oh dear you are dead!");
				}
			}
			PlayerSave.saveGame(c);
			PlayerSave.saveGame(o);
		}
		c.faceUpdate(0);
		c.npcIndex = 0;
		c.playerIndex = 0;
		c.stopMovement();
		if (duelSession != null
				&& duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERACTION) {
			if (!duelSession.getWinner().isPresent()) {
				c.sendMessage("You have lost the duel!");
				Client opponent = duelSession.getOther(c);
				if (!duelSession.getWinner().isPresent()) {
					duelSession.setWinner(opponent);
				}
				PlayerSave.saveGame(opponent);
			} else {
				c.sendMessage("Congratulations, you have won the duel.");
			}
		}
		PlayerSave.saveGame(c);
		resetDamageDone();
		c.specAmount = 10;
		c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
		c.lastVeng = 0;
		c.vengOn = false;
		resetFollowers();
		c.attackTimer = 10;
		removeAllWindows();
		c.tradeResetNeeded = true;
	}

	public void resetDamageDone() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				PlayerHandler.players[i].damageTaken[c.playerId] = 0;
			}
		}
	}

	public void vengMe() {
		if (System.currentTimeMillis() - c.lastVeng > 30000) {
			if (c.getItems().playerHasItem(557, 10)
					&& c.getItems().playerHasItem(9075, 4)
					&& c.getItems().playerHasItem(560, 2)) {
				c.vengOn = true;
				c.lastVeng = System.currentTimeMillis();
				c.startAnimation(4410);
				c.gfx100(726);
				c.getItems().deleteItem(557, c.getItems().getItemSlot(557), 10);
				c.getItems().deleteItem(560, c.getItems().getItemSlot(560), 2);
				c.getItems()
						.deleteItem(9075, c.getItems().getItemSlot(9075), 4);
			} else {
				c.sendMessage("You do not have the required runes to cast this spell. (9075 for astrals)");
			}
		} else {
			c.sendMessage("You must wait 30 seconds before casting this again.");
		}
	}

	public void resetTb() {
		c.teleBlockLength = 0;
		c.teleBlockDelay = 0;
	}

	public void handleStatus(int i, int i2, int i3) {
		// Sanity u so smart
	}

	public void resetFollowers() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].followId == c.playerId) {
					Client c = (Client) PlayerHandler.players[j];
					c.getPA().resetFollow();
				}
			}
		}
	}

	public void giveLife() {
		c.isDead = false;
		c.faceUpdate(-1);
		c.freezeTimer = 0;
		if (!c.inDuelArena() && !Boundary.isIn(c, Boundary.DUEL_ARENAS)
				&& !Boundary.isIn(c, Boundary.PVP_ZONE) && c.startTimer <= 0
				&& !FightPitsMinigame.inGame(c) && !c.getRecipe().inGame()) {
			if (!c.inFightCaves()) {
				c.getItems().resetKeepItems();
				if (!c.isSkulled) { // what items to keep
					c.getItems().keepItem(0, true);
					c.getItems().keepItem(1, true);
					c.getItems().keepItem(2, true);
				}
				if (c.prayerActive[10]) {
					c.getItems().keepItem(3, true);
				}
				c.getItems().dropAllItems(); // drop all items
				c.getItems().deleteAllItems(); // delete all items

				if (!c.isSkulled) { // add the kept items once we finish
									// deleting and dropping them
					for (int i1 = 0; i1 < 3; i1++) {
						if (c.itemKeptId[i1] > 0) {
							c.getItems().addItem(c.itemKeptId[i1], 1);
						}
					}
				}
				if (c.prayerActive[10]) { // if we have protect items
					if (c.itemKeptId[3] > 0) {
						c.getItems().addItem(c.itemKeptId[3], 1);
					}
				}
				c.getItems().resetKeepItems();
			}
		}
		c.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			c.playerLevel[i] = getLevelForXP(c.playerXP[i]);
			c.getPA().refreshSkill(i);
		}
		if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
			DuelSession duelSession = (DuelSession) Server
					.getMultiplayerSessionListener().getMultiplayerSession(c,
							MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession)
					&& duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERACTION) {
				Client opponent = duelSession.getWinner().get();
				if (opponent != null) {
					opponent.getPA().createPlayerHints(10, -1);
					duelSession
							.finish(MultiplayerSessionFinalizeType.GIVE_ITEMS);
				}
			}
		} else if (c.getRecipe().inGame()) {
			c.getRecipe().endGame(false);
		} else if (FightPitsMinigame.inGame(c)) {
			FightPitsMinigame.removeFromGame(c);
		} else {
			movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
			c.isSkulled = false;
			c.skullTimer = 0;
			c.attackedPlayers.clear();
		}
		// PlayerSaving.getSingleton().requestSave(c.playerId);
		PlayerSave.saveGame(c);
		c.getCombat().resetPlayerAttack();
		resetAnimation();
		c.startAnimation(65535);
		frame1();
		resetTb();
		c.isSkulled = false;
		c.attackedPlayers.clear();
		c.headIconPk = -1;
		c.skullTimer = -1;
		c.damageTaken = new int[Config.MAX_PLAYERS];
		c.getPA().requestUpdates();
		removeAllWindows();
		c.tradeResetNeeded = true;
	}

	/**
	 * Location change for digging, levers etc
	 **/

	public void changeLocation() {
		switch (c.newLocation) {
		case 1:
			sendFrame99(2);
			movePlayer(3578, 9706, -1);
			break;
		case 2:
			sendFrame99(2);
			movePlayer(3568, 9683, -1);
			break;
		case 3:
			sendFrame99(2);
			movePlayer(3557, 9703, -1);
			break;
		case 4:
			sendFrame99(2);
			movePlayer(3556, 9718, -1);
			break;
		case 5:
			sendFrame99(2);
			movePlayer(3534, 9704, -1);
			break;
		case 6:
			sendFrame99(2);
			movePlayer(3546, 9684, -1);
			break;
		}
		c.newLocation = 0;
	}

	/**
	 * Teleporting
	 **/
	public void spellTeleport(int x, int y, int height) {
		c.getPA().startTeleport(
				x,
				y,
				height,
				c.playerMagicBook == 1 ? TeleportType.ANCIENT
						: TeleportType.MODERN);
	}

	public boolean canTeleport() {
		if (FightPitsMinigame.inFightPits(c)) {
			c.sendMessage("I can't do this right now.");
			return false;
		}
		if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
			DuelSession session = (DuelSession) Server
					.getMultiplayerSessionListener().getMultiplayerSession(c,
							MultiplayerSessionType.DUEL);
			if (Objects.nonNull(session)) {
				c.sendMessage("You cannot teleport whilst in a duel.");
				return false;
			}
		}
		if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
			c.sendMessage("You cannot teleport out of fight caves.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			c.sendMessage("You must finish what you're doing before you can teleport.");
			return false;
		}
		return true;
	}

	public boolean startTeleport(int x, int y, int height,
			TeleportType teleportType) {
		return startTeleport(x, y, height, teleportType, false);
	}

	public boolean startTeleport(int x, int y, int height,
			TeleportType teleportType, boolean ignoreCombat) {

		if (!canTeleport()) {
			return false;
		}

		if (c.isJailed()) {
			c.sendMessage("You can't teleport during your visit in jail.");
			return false;
		}

		if (c.duelStatus == 5) {
			c.sendMessage("You can't teleport during a duel!");
			return false;
		}
		if (PestControl.playerInBoat(c) || PestControl.playerInGame(c)) {
			c.sendMessage("I'm way too busy to teleport away right now.");
			return false;
		}
		if (c.inWild() && c.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
			c.sendMessage("You can't teleport above level "
					+ Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return false;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return false;
		}
		if ((c.underAttackBy > 0 || c.underAttackBy2 > 0) && !ignoreCombat) {
			c.sendMessage("You can't teleport out during combat");
			return false;
		}
		if (c.isDead || c.teleTimer > 0 || c.respawnTimer != -6) {
			return false;
		}
		if (c.playerIndex > 0 || c.npcIndex > 0)
			c.getCombat().resetPlayerAttack();
		c.stopMovement();
		removeAllWindows();
		c.teleX = x;
		c.teleY = y;
		c.npcIndex = 0;
		c.playerIndex = 0;
		c.faceUpdate(0);
		c.teleHeight = height;
		if (teleportType == TeleportType.MODERN
				|| teleportType == TeleportType.LUNAR) {
			c.startAnimation(714);
			c.teleTimer = 11;
			c.teleGfx = 308;
			c.teleEndAnimation = 715;
		}
		if (teleportType == TeleportType.ANCIENT) {
			c.startAnimation(1979);
			c.teleGfx = 0;
			c.teleTimer = 9;
			c.teleEndAnimation = 0;
			c.gfx0(392);
		}
		return true;
	}

	public void startTeleport2(int x, int y, int height) {
		if (!canTeleport()) {
			return;
		}
		if (c.isJailed()) {
			c.sendMessage("You can't teleport during your visit in jail.");
			return;
		}
		if (c.duelStatus == 5) {
			c.sendMessage("You can't teleport during a duel!");
			return;
		}
		if (System.currentTimeMillis() - c.teleBlockDelay < c.teleBlockLength) {
			c.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (!c.isDead && c.teleTimer == 0) {
			c.stopMovement();
			removeAllWindows();
			c.teleX = x;
			c.teleY = y;
			c.npcIndex = 0;
			c.playerIndex = 0;
			c.faceUpdate(0);
			c.teleHeight = height;
			c.startAnimation(714);
			c.teleTimer = 11;
			c.teleGfx = 308;
			c.teleEndAnimation = 715;

		}
	}

	public void processTeleport() {
		c.teleportToX = c.teleX;
		c.teleportToY = c.teleY;
		c.heightLevel = c.teleHeight;
		c.teleportToHeight = c.teleHeight;
		if (c.teleEndAnimation > 0) {
			c.startAnimation(c.teleEndAnimation);
		}
	}

	public void movePlayer(int x, int y, int h) {
		if (Server.getMultiplayerSessionListener().inSession(c,
				MultiplayerSessionType.TRADE)) {
			Server.getMultiplayerSessionListener().finish(c,
					MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
		}
		if (c.heightLevel != h) {
			c.checkHeight = true;
		}
		c.resetWalkingQueue();
		c.teleportToX = x;
		c.teleportToY = y;
		c.heightLevel = h;
		c.teleportToHeight = h;
		if (c.teleportToHeight != c.heightLevel) {
			c.RebuildNPCList = true;
		}
		requestUpdates();
	}

	/**
	 * Following
	 **/

	public boolean normalFollow(int i) {
		if (c.followId == 0 || PlayerHandler.players[c.followId] == null
				|| PlayerHandler.players[c.followId].isDead
				|| (c.duelRule[1] && c.duelStatus == 5)) {
			return false;
		}

		Client o = (Client) PlayerHandler.players[c.followId];
		int otherX = PlayerHandler.players[c.followId].getX();
		int otherY = PlayerHandler.players[c.followId].getY();

		if (c.isDead || c.playerLevel[3] <= 0
				|| !c.goodDistance(otherX, otherY, c.getX(), c.getY(), 17)
				|| c.freezeTimer > 0) {
			return false;
		}

		c.followId2 = 0;
		c.faceUpdate(c.followId + 32768);
		if (c.followingX == otherX && c.followingY == otherY) {
			return true;
		}
		c.followingX = otherX;
		c.followingY = otherY;
		boolean checkClip = Region.checkClip(o, o.oldX, o.oldY);
		if (!checkClip) {
			if (Region.checkClip(o, o.absX - 1, o.absY)) {
				o.oldX = o.absX - 1;
				o.oldY = o.absY;
			} else if (Region.checkClip(o, o.absX + 1, o.absY)) {
				o.oldX = o.absX + 1;
				o.oldY = o.absY;
			} else if (Region.checkClip(o, o.absX, o.absY - 1)) {
				o.oldX = o.absX;
				o.oldY = o.absY - 1;
			}
		}
		c.stopMovement();
		setOpponentCoordinates(otherX, otherY);
		playerWalk(o.oldX, o.oldY, true);
		return true;
	}

	public void handleEntityFollowing() {
		if (c.followId > 0) {
			if (c.playerIndex == 0 && !c.mageFollow) {
			} else {
				combatFollowPlayer(c.playerIndex);
			}
		} else if (c.followId2 > 0) {
			combatFollowNPC(c.npcIndex);
		}
	}

	public void combatFollowPlayer(int i) {
		if (c.followId == 0 || PlayerHandler.players[c.followId] == null
				|| PlayerHandler.players[c.followId].isDead
				|| (c.duelRule[1] && c.duelStatus == 5)) {
			c.followId = 0;
			return;
		}
		Client otherPlayer = (Client) PlayerHandler.players[c.followId];
		int otherX = otherPlayer.getX();
		int otherY = otherPlayer.getY();

		if (c.inTrade || c.isDead || c.playerLevel[3] <= 0 || inPitsWait()
				|| !Misc.goodDistance(otherX, otherY, c.getX(), c.getY(), 25)) {
			c.followId = 0;
			return;
		}

		if (c.freezeTimer > 0) {
			return;
		}

		if (checkStopMovement(otherPlayer)) {
			return;
		}

		c.followId2 = 0;
		c.faceUpdate(c.followId + 32768);
		// System.out.println("x: "+c.getX()+" - y: "+c.getY()
		// +" - clipped: "+checkClip);
		/**
		 * Walking to the assigned player.
		 */

		if (c.followingX == otherX && c.followingY == otherY) {
			return;
		}
		c.followingX = otherX;
		c.followingY = otherY;
		walkTowards(otherX, otherY);
	}

	public Position getClosestNpcPosition(NPC npc) {
		List<Position> positions = new ArrayList<Position>();
		for (int x = npc.absX - 1; x < npc.absX + npc.getSize(); x++) {
			for (int y = npc.absY - 1; y < npc.absY + npc.getSize(); y++) {
				if ((x == npc.absX - 1 || x == npc.absX + npc.getSize())
						&& (y == npc.absY - 1 || y == npc.absY + npc.getSize()))
					continue;
				positions.add(new Position(x, y));
			}
		}

		positions.sort(new Comparator<Position>() {
			@Override
			public int compare(Position posA, Position posB) {
				double distanceA = Math.hypot(posA.getX() - c.absX, posA.getY()
						- c.absY);
				double distanceB = Math.hypot(posB.getX() - c.absX, posB.getY()
						- c.absY);
				return distanceA > distanceB ? 1 : distanceA < distanceB ? -1
						: 0;
			}
		});

		for (Position position : positions) {
			if (Region.getClipping(position.getX(), position.getY(),
					c.heightLevel) == 0) {
				return position;
			}
		}

		return new Position(npc.absX, npc.absY);
	}

	public void combatFollowNPC(int i) {
		NPC npc = NPCHandler.npcs[i];
		if (npc == null || npc.isDead) {
			return;
		}

		if (checkStopMovementNPC(npc)) {
			return;
		}

		Position position = getClosestNpcPosition(npc);

		c.followId = 0;
		c.followId2 = i;
		c.faceUpdate(c.followId2);

		if (c.followingX == position.getX() && c.followingY == position.getY()) {
			return;
		}

		c.followingX = position.getX();
		c.followingY = position.getY();
		playerWalk(position.getX(), position.getY(), true);
	}

	public boolean checkStopMovement(Client otherPlayer) {
		if (otherPlayer == null) {
			return false;
		}

		int otherX = otherPlayer.getX();
		int otherY = otherPlayer.getY();

		boolean hallyDistance = Misc.goodDistance(otherX, otherY, c.getX(),
				c.getY(), 2);
		boolean bowDistance = Misc.goodDistance(otherX, otherY, c.getX(),
				c.getY(), 8)
				&& Region.canAttack(c, otherPlayer);
		boolean rangeWeaponDistance = Misc.goodDistance(otherX, otherY,
				c.getX(), c.getY(), 4)
				&& Region.canAttack(c, otherPlayer);
		boolean sameSpot = c.absX == otherX && c.absY == otherY;
		boolean checkClip = Region.checkClip(c, otherX, otherY);

		if ((c.usingBow || c.autocasting || c.usingMagic || c.mageFollow || (c.playerIndex > 0 && c.autocastId > 0))
				&& bowDistance && !sameSpot) {
			if (checkClip) {
				if (c.usingMagic) {
					c.followId = 0;
				}
				c.stopMovement();
				return true;
			}
		}

		if (c.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return true;
		}

		if (c.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			if (checkClip) {
				c.stopMovement();
				return true;
			}
		}

		if (Misc.goodDistance(otherX, otherY, c.getX(), c.getY(), 1)) {
			if (checkClip) {
				if (otherX != c.getX() && otherY != c.getY()) {
					stopDiagonal(otherX, otherY);
				}
				return true;
			}
		}

		return false;
	}

	public boolean checkStopMovementNPC(NPC npc) {
		if (npc == null) {
			return false;
		}

		int otherX = npc.getX();
		int otherY = npc.getY();

		boolean goodDistance = npc.distanceTo(c) <= 1;
		boolean hallyDistance = npc.distanceTo(c) <= 2;
		boolean bowDistance = npc.distanceTo(c) <= 8
				&& Region.canAttack(c, npc);
		boolean rangeWeaponDistance = npc.distanceTo(c) <= 4
				&& Region.canAttack(c, npc);
		boolean sameSpot = c.absX == otherX && c.absY == otherY;
		boolean checkClip = Region.checkClip(c, otherX, otherY);

		if ((c.usingBow || c.autocasting || c.usingMagic || c.mageFollow || (c.npcIndex > 0 && c.autocastId > 0))
				&& bowDistance && !sameSpot) {
			if (checkClip) {
				if (c.usingMagic) {
					c.followId2 = 0;
				}
				c.stopMovement();
				return true;
			}
		}

		if (c.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return true;
		}

		if (c.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			if (checkClip) {
				c.stopMovement();
				return true;
			}
		}

		if (goodDistance) {
			if (checkClip) {
				if (otherX != c.getX() && otherY != c.getY()) {
					stopDiagonal(otherX, otherY);
				}
				return true;
			}
		}

		return false;
	}

	public boolean walkTowards(int otherX, int otherY) {
		setOpponentCoordinates(otherX, otherY);
		boolean foundPath = false;
		if (otherY == c.getY() && otherX == c.getX()) {
			foundPath = combatWalk(otherX, otherY - 1);
		} else {
			if (otherY > c.getY() && otherX == c.getX()) {
				foundPath = combatWalk(otherX, otherY - 1);
			} else if (otherY < c.getY() && otherX == c.getX()) {
				foundPath = combatWalk(otherX, otherY + 1);
			} else if (otherX > c.getX() && otherY == c.getY()) {
				foundPath = combatWalk(otherX - 1, otherY);
			} else if (otherX < c.getX() && otherY == c.getY()) {
				foundPath = combatWalk(otherX + 1, otherY);
			} else if (otherX < c.getX() && otherY < c.getY()) {
				foundPath = combatWalk(otherX + 1, otherY);
			} else if (otherX > c.getX() && otherY > c.getY()) {
				foundPath = combatWalk(otherX - 1, otherY);
			} else if (otherX < c.getX() && otherY > c.getY()) {
				foundPath = combatWalk(otherX + 1, otherY);
			} else if (otherX > c.getX() && otherY < c.getY()) {
				foundPath = combatWalk(otherX - 1, otherY);
			}
		}
		return foundPath;
	}

	public void setOpponentCoordinates(int absX, int absY) {
		opponentX = absX;
		opponentY = absY;
	}

	private int opponentX;
	private int opponentY;

	public boolean combatWalk(int x, int y) {
		if (c.duelRule[1] && c.duelStatus == 5) {
			return false;
		}
		boolean east = opponentX - c.getX() > 0;
		boolean west = c.getX() - opponentX > 0;
		boolean north = opponentY - c.getY() > 0;
		boolean south = c.getY() - opponentY > 0;
		boolean foundPath = false;
		if (south) {
			if (east) {
				if (Region.checkNormalClip(x, y, opponentX, opponentY,
						c.heightLevel)
						&& Region.getClipping(x, y, c.heightLevel, false) != 2228480) {
					foundPath = playerWalk(x, y, true);
				} else if (Region.checkNormalClip(opponentX, opponentY,
						opponentX, opponentY + 1, c.heightLevel)) {
					foundPath = playerWalk(opponentX, opponentY + 1, true);
				} else if (Region.checkNormalClip(opponentX, opponentY,
						opponentX - 1, opponentY, c.heightLevel)) {
					foundPath = playerWalk(opponentX + 1, opponentY, true);
				}
				return foundPath;
			}
			if (west) {
				if (Region.checkNormalClip(x, y, opponentX, opponentY,
						c.heightLevel)
						&& Region.getClipping(x, y, c.heightLevel, false) != 2228480) {
					foundPath = playerWalk(x, y, true);
				} else if (Region.checkNormalClip(opponentX, opponentY,
						opponentX, opponentY + 1, c.heightLevel)) {
					foundPath = playerWalk(opponentX, opponentY + 1, true);
				} else if (Region.checkNormalClip(opponentX, opponentY,
						opponentX - 1, opponentY, c.heightLevel)) {
					foundPath = playerWalk(opponentX - 1, opponentY, true);
				} else if (Region.checkNormalClip(opponentX, opponentY,
						opponentX, opponentY - 1, c.heightLevel)) {
					foundPath = playerWalk(opponentX, opponentY - 1, true);
				}
				return foundPath;
			}
			if (Region.checkNormalClip(x, y, opponentX, opponentY,
					c.heightLevel)
					&& Region.getClipping(x, y, c.heightLevel, false) != 2228480) {
				foundPath = playerWalk(x, y, true);
			} else if (Region.checkNormalClip(opponentX, opponentY,
					opponentX - 1, opponentY, c.heightLevel)) {
				foundPath = playerWalk(opponentX - 1, opponentY, true);
			} else if (Region.checkNormalClip(opponentX, opponentY,
					opponentX + 1, opponentY, c.heightLevel)) {
				foundPath = playerWalk(opponentX + 1, opponentY, true);
			} else if (Region.checkNormalClip(opponentX, opponentY, opponentX,
					opponentY - 1, c.heightLevel)) {
				foundPath = playerWalk(opponentX, opponentY - 1, true);
			}
			return foundPath;
		}
		if (north) {
			if (east) {
				if (Region.checkNormalClip(x, y, opponentX, opponentY,
						c.heightLevel)
						&& Region.getClipping(x, y, c.heightLevel, false) != 2228480) {
					foundPath = playerWalk(x, y, true);
				} else if (Region.checkNormalClip(opponentX, opponentY,
						opponentX, opponentY - 1, c.heightLevel)) {
					foundPath = playerWalk(opponentX, opponentY - 1, true);
				} else if (Region.checkNormalClip(opponentX, opponentY,
						opponentX + 1, opponentY, c.heightLevel)) {
					foundPath = playerWalk(opponentX + 1, opponentY, true);
				} else if (Region.checkNormalClip(opponentX, opponentY,
						opponentX, opponentY + 1, c.heightLevel)) {
					foundPath = playerWalk(opponentX, opponentY + 1, true);
				}
				return foundPath;
			}
			if (west) {
				if (Region.checkNormalClip(opponentX, opponentY, x, y,
						c.heightLevel)
						&& Region.getClipping(x, y, c.heightLevel, false) != 2228480) {
					foundPath = playerWalk(x, y, true);
				} else if (Region.checkNormalClip(opponentX, opponentY,
						opponentX, opponentY - 1, c.heightLevel)) {
					foundPath = playerWalk(opponentX, opponentY - 1, true);
				} else if (Region.checkNormalClip(opponentX, opponentY,
						opponentX, opponentY + 1, c.heightLevel)) {
					foundPath = playerWalk(opponentX, opponentY + 1, true);
				}
				return foundPath;
			}
			if (Region.checkNormalClip(opponentX, opponentY, x, y,
					c.heightLevel)) {
				foundPath = playerWalk(x, y, true);
			} else if (Region.checkNormalClip(opponentX, opponentY, opponentX,
					opponentY - 1, c.heightLevel)) {
				foundPath = playerWalk(opponentX, opponentY - 1, true);
			} else if (Region.checkNormalClip(opponentX, opponentY,
					opponentX - 1, opponentY, c.heightLevel)) {
				foundPath = playerWalk(opponentX - 1, opponentY, true);
			} else if (Region.checkNormalClip(opponentX, opponentY,
					opponentX + 1, opponentY, c.heightLevel)) {
				foundPath = playerWalk(opponentX + 1, opponentY, true);
			}
			return foundPath;
		}
		if (east) {
			if (Region.checkNormalClip(opponentX, opponentY, x, y,
					c.heightLevel)
					&& Region.getClipping(x, y, c.heightLevel, false) != 2228480) {
				foundPath = playerWalk(x, y, true);
			} else if (Region.checkNormalClip(opponentX, opponentY, opponentX,
					opponentY - 1, c.heightLevel)) {
				foundPath = playerWalk(opponentX, opponentY - 1, true);
			} else if (Region.checkNormalClip(opponentX, opponentY, opponentX,
					opponentY + 1, c.heightLevel)) {
				foundPath = playerWalk(opponentX, opponentY + 1, true);
			} else if (Region.checkNormalClip(opponentX, opponentY,
					opponentX + 1, opponentY, c.heightLevel)) {
				foundPath = playerWalk(opponentX + 1, opponentY, true);
			}
			return foundPath;
		}
		if (west) {
			if (Region.checkNormalClip(opponentX, opponentY, x, y,
					c.heightLevel)
					&& Region.getClipping(x, y, c.heightLevel, false) != 2228480) {
				foundPath = playerWalk(x, y, true);
			} else if (Region.checkNormalClip(opponentX, opponentY, opponentX,
					opponentY - 1, c.heightLevel)) {
				foundPath = playerWalk(opponentX, opponentY - 1, true);
			} else if (Region.checkNormalClip(opponentX, opponentY, opponentX,
					opponentY + 1, c.heightLevel)) {
				foundPath = playerWalk(opponentX, opponentY - 1, true);
			}
			return foundPath;
		}
		if (Region.checkNormalClip(opponentX, opponentY, x, y, c.heightLevel)) {
			foundPath = playerWalk(x, y, true);
		} else if (Region.checkNormalClip(opponentX, opponentY, opponentX,
				opponentY - 1, c.heightLevel)) {
			foundPath = playerWalk(opponentX, opponentY - 1, true);
		} else if (Region.checkNormalClip(opponentX, opponentY, opponentX,
				opponentY + 1, c.heightLevel)) {
			foundPath = playerWalk(opponentX, opponentY - 1, true);
		}
		return foundPath;
	}

	public boolean playerWalk(int x, int y, boolean combatWalk) {
		// System.out.println("playerwalk - X: "+x+" - Y: "+y);
		if ((c.duelRule[1] && c.duelStatus == 5) || c.freezeTimer > 0) {
			return false;
		}
		c.pathX = x;
		c.pathY = y;
		try {
			return PathFinder.findRoute(c, x, y, true, 1, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public void clippedWalkTo() {
		if (Region.getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
			c.getPA().walkTo(-1, 0);
		} else if (Region.getClipping(c.getX() + 1, c.getY(), c.heightLevel, 1,
				0)) {
			c.getPA().walkTo(1, 0);
		} else if (Region.getClipping(c.getX(), c.getY() - 1, c.heightLevel, 0,
				-1)) {
			c.getPA().walkTo(0, -1);
		} else if (Region.getClipping(c.getX(), c.getY() + 1, c.heightLevel, 0,
				1)) {
			c.getPA().walkTo(0, 1);
		}
		c.postProcessing();
	}

	public void handleRealFollowing() {
		if (c.followId > 0 && c.playerIndex == 0 && !c.mageFollow) {
			if (!normalFollow(c.followId)) {
				c.followId = 0;
				c.stopMovement();
			}
		}
	}

	public int getRunningMove(int i, int j) {
		if (j - i > 2)
			return 2;
		else if (j - i < -2)
			return -2;
		else
			return j - i;
	}

	public void resetFollow() {
		c.followId = 0;
		c.followId2 = 0;
		c.mageFollow = false;
		c.outStream.createFrame(174);
		c.outStream.writeWord(0);
		c.outStream.writeByte(0);
		c.outStream.writeWord(1);
	}

	public void walkTo(int i, int j) {
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
		c.postProcessing();
	}

	public void walkTo2(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public void stopDiagonal(int otherX, int otherY) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 1;
		int xMove = otherX - c.getX();
		int yMove = 0;
		if (xMove == 0)
			yMove = otherY - c.getY();
		/*
		 * if (!clipHor) { yMove = 0; } else if (!clipVer) { xMove = 0; }
		 */

		int k = c.getX() + xMove;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + yMove;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}

	}

	public void walkToCheck(int i, int j) {
		if (c.freezeDelay > 0)
			return;
		c.newWalkCmdSteps = 0;
		if (++c.newWalkCmdSteps > 50)
			c.newWalkCmdSteps = 0;
		int k = c.getX() + i;
		k -= c.mapRegionX * 8;
		c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
		int l = c.getY() + j;
		l -= c.mapRegionY * 8;

		for (int n = 0; n < c.newWalkCmdSteps; n++) {
			c.getNewWalkCmdX()[n] += k;
			c.getNewWalkCmdY()[n] += l;
		}
	}

	public int getMove(int place1, int place2) {
		if (System.currentTimeMillis() - c.lastSpear < 4000)
			return 0;
		if ((place1 - place2) == 0) {
			return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean fullVeracs() {
		return c.playerEquipment[c.playerHat] == 4753
				&& c.playerEquipment[c.playerChest] == 4757
				&& c.playerEquipment[c.playerLegs] == 4759
				&& c.playerEquipment[c.playerWeapon] == 4755;
	}

	public boolean fullGuthans() {
		return c.playerEquipment[c.playerHat] == 4724
				&& c.playerEquipment[c.playerChest] == 4728
				&& c.playerEquipment[c.playerLegs] == 4730
				&& c.playerEquipment[c.playerWeapon] == 4726;
	}

	/**
	 * reseting animation
	 **/
	public void resetAnimation() {
		c.startAnimation(65535);
	}

	public void requestUpdates() {
		c.updateRequired = true;
		c.setAppearanceUpdateRequired(true);
	}

	public void handleAlt(int id) {
		if (!c.getItems().playerHasItem(id)) {
			c.getItems().addItem(id, 1);
		}
	}

	public void levelUp(int skill) {
		int totalLevel = (getLevelForXP(c.playerXP[0])
				+ getLevelForXP(c.playerXP[1]) + getLevelForXP(c.playerXP[2])
				+ getLevelForXP(c.playerXP[3]) + getLevelForXP(c.playerXP[4])
				+ getLevelForXP(c.playerXP[5]) + getLevelForXP(c.playerXP[6])
				+ getLevelForXP(c.playerXP[7]) + getLevelForXP(c.playerXP[8])
				+ getLevelForXP(c.playerXP[9]) + getLevelForXP(c.playerXP[10])
				+ getLevelForXP(c.playerXP[11]) + getLevelForXP(c.playerXP[12])
				+ getLevelForXP(c.playerXP[13]) + getLevelForXP(c.playerXP[14])
				+ getLevelForXP(c.playerXP[15]) + getLevelForXP(c.playerXP[16])
				+ getLevelForXP(c.playerXP[17]) + getLevelForXP(c.playerXP[18])
				+ getLevelForXP(c.playerXP[19]) + getLevelForXP(c.playerXP[20]));
		sendFrame126("Total Lvl: " + totalLevel, 3984);
		switch (skill) {
		case 0:
			sendFrame126("Congratulations, you just advanced an attack level!",
					6248);
			sendFrame126("Your attack level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6249);
			c.sendMessage("Congratulations, you just advanced an attack level.");
			sendFrame164(6247);
			break;

		case 1:
			sendFrame126("Congratulations, you just advanced a defence level!",
					6254);
			sendFrame126("Your defence level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6255);
			c.sendMessage("Congratulations, you just advanced a defence level.");
			sendFrame164(6253);
			break;

		case 2:
			sendFrame126(
					"Congratulations, you just advanced a strength level!",
					6207);
			sendFrame126("Your strength level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6208);
			c.sendMessage("Congratulations, you just advanced a strength level.");
			sendFrame164(6206);
			break;

		case 3:
			sendFrame126(
					"Congratulations, you just advanced a hitpoints level!",
					6217);
			sendFrame126("Your hitpoints level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6218);
			c.sendMessage("Congratulations, you just advanced a hitpoints level.");
			sendFrame164(6216);
			break;

		case 4:
			sendFrame126("Congratulations, you just advanced a ranged level!",
					5453);
			sendFrame126("Your ranged level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6114);
			c.sendMessage("Congratulations, you just advanced a ranging level.");
			sendFrame164(4443);
			break;

		case 5:
			sendFrame126("Congratulations, you just advanced a prayer level!",
					6243);
			sendFrame126("Your prayer level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6244);
			c.sendMessage("Congratulations, you just advanced a prayer level.");
			sendFrame164(6242);
			break;

		case 6:
			sendFrame126("Congratulations, you just advanced a magic level!",
					6212);
			sendFrame126("Your magic level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6213);
			c.sendMessage("Congratulations, you just advanced a magic level.");
			sendFrame164(6211);
			break;

		case 7:
			sendFrame126("Congratulations, you just advanced a cooking level!",
					6227);
			sendFrame126("Your cooking level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6228);
			c.sendMessage("Congratulations, you just advanced a cooking level.");
			sendFrame164(6226);
			break;

		case 8:
			sendFrame126(
					"Congratulations, you just advanced a woodcutting level!",
					4273);
			sendFrame126("Your woodcutting level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 4274);
			c.sendMessage("Congratulations, you just advanced a woodcutting level.");
			sendFrame164(4272);
			break;

		case 9:
			sendFrame126(
					"Congratulations, you just advanced a fletching level!",
					6232);
			sendFrame126("Your fletching level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6233);
			c.sendMessage("Congratulations, you just advanced a fletching level.");
			sendFrame164(6231);
			break;

		case 10:
			sendFrame126("Congratulations, you just advanced a fishing level!",
					6259);
			sendFrame126("Your fishing level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6260);
			c.sendMessage("Congratulations, you just advanced a fishing level.");
			sendFrame164(6258);
			break;

		case 11:
			sendFrame126(
					"Congratulations, you just advanced a fire making level!",
					4283);
			sendFrame126("Your firemaking level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 4284);
			c.sendMessage("Congratulations, you just advanced a fire making level.");
			sendFrame164(4282);
			break;

		case 12:
			sendFrame126(
					"Congratulations, you just advanced a crafting level!",
					6264);
			sendFrame126("Your crafting level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6265);
			c.sendMessage("Congratulations, you just advanced a crafting level.");
			sendFrame164(6263);
			break;

		case 13:
			sendFrame126(
					"Congratulations, you just advanced a smithing level!",
					6222);
			sendFrame126("Your smithing level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6223);
			c.sendMessage("Congratulations, you just advanced a smithing level.");
			sendFrame164(6221);
			break;

		case 14:
			sendFrame126("Congratulations, you just advanced a mining level!",
					4417);
			sendFrame126("Your mining level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 4438);
			c.sendMessage("Congratulations, you just advanced a mining level.");
			sendFrame164(4416);
			break;

		case 15:
			sendFrame126(
					"Congratulations, you just advanced a herblore level!",
					6238);
			sendFrame126("Your herblore level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 6239);
			c.sendMessage("Congratulations, you just advanced a herblore level.");
			sendFrame164(6237);
			break;

		case 16:
			sendFrame126("Congratulations, you just advanced a agility level!",
					4278);
			sendFrame126("Your agility level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 4279);
			c.sendMessage("Congratulations, you just advanced an agility level.");
			sendFrame164(4277);
			break;

		case 17:
			sendFrame126(
					"Congratulations, you just advanced a thieving level!",
					4263);
			sendFrame126("Your theiving level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 4264);
			c.sendMessage("Congratulations, you just advanced a thieving level.");
			sendFrame164(4261);
			break;

		case 18:
			sendFrame126("Congratulations, you just advanced a slayer level!",
					12123);
			sendFrame126("Your slayer level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 12124);
			c.sendMessage("Congratulations, you just advanced a slayer level.");
			sendFrame164(12122);
			break;

		case 20:
			sendFrame126(
					"Congratulations, you just advanced a runecrafting level!",
					4268);
			sendFrame126("Your runecrafting level is now "
					+ getLevelForXP(c.playerXP[skill]) + ".", 4269);
			c.sendMessage("Congratulations, you just advanced a runecrafting level.");
			sendFrame164(4267);
			break;
		}
		c.dialogueAction = 0;
		c.nextChat = 0;
	}

	public void refreshSkill(int i) {
		switch (i) {
		case 0:
			sendFrame126("" + c.playerLevel[0] + "", 4004);
			sendFrame126("" + getLevelForXP(c.playerXP[0]) + "", 4005);
			sendFrame126("" + c.playerXP[0] + "", 4044);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[0]) + 1)
					+ "", 4045);
			break;

		case 1:
			sendFrame126("" + c.playerLevel[1] + "", 4008);
			sendFrame126("" + getLevelForXP(c.playerXP[1]) + "", 4009);
			sendFrame126("" + c.playerXP[1] + "", 4056);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[1]) + 1)
					+ "", 4057);
			break;

		case 2:
			sendFrame126("" + c.playerLevel[2] + "", 4006);
			sendFrame126("" + getLevelForXP(c.playerXP[2]) + "", 4007);
			sendFrame126("" + c.playerXP[2] + "", 4050);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[2]) + 1)
					+ "", 4051);
			break;

		case 3:
			sendFrame126("" + c.playerLevel[3] + "", 4016);
			sendFrame126("" + getLevelForXP(c.playerXP[3]) + "", 4017);
			sendFrame126("" + c.playerXP[3] + "", 4080);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[3]) + 1)
					+ "", 4081);
			break;

		case 4:
			sendFrame126("" + c.playerLevel[4] + "", 4010);
			sendFrame126("" + getLevelForXP(c.playerXP[4]) + "", 4011);
			sendFrame126("" + c.playerXP[4] + "", 4062);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[4]) + 1)
					+ "", 4063);
			break;

		case 5:
			sendFrame126("" + c.playerLevel[5] + "", 4012);
			sendFrame126("" + getLevelForXP(c.playerXP[5]) + "", 4013);
			sendFrame126("" + c.playerXP[5] + "", 4068);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[5]) + 1)
					+ "", 4069);
			sendFrame126("" + c.playerLevel[5] + "/"
					+ getLevelForXP(c.playerXP[5]) + "", 687);// Prayer
																// frame
			break;

		case 6:
			sendFrame126("" + c.playerLevel[6] + "", 4014);
			sendFrame126("" + getLevelForXP(c.playerXP[6]) + "", 4015);
			sendFrame126("" + c.playerXP[6] + "", 4074);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[6]) + 1)
					+ "", 4075);
			break;

		case 7:
			sendFrame126("" + c.playerLevel[7] + "", 4034);
			sendFrame126("" + getLevelForXP(c.playerXP[7]) + "", 4035);
			sendFrame126("" + c.playerXP[7] + "", 4134);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[7]) + 1)
					+ "", 4135);
			break;

		case 8:
			sendFrame126("" + c.playerLevel[8] + "", 4038);
			sendFrame126("" + getLevelForXP(c.playerXP[8]) + "", 4039);
			sendFrame126("" + c.playerXP[8] + "", 4146);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[8]) + 1)
					+ "", 4147);
			break;

		case 9:
			sendFrame126("" + c.playerLevel[9] + "", 4026);
			sendFrame126("" + getLevelForXP(c.playerXP[9]) + "", 4027);
			sendFrame126("" + c.playerXP[9] + "", 4110);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[9]) + 1)
					+ "", 4111);
			break;

		case 10:
			sendFrame126("" + c.playerLevel[10] + "", 4032);
			sendFrame126("" + getLevelForXP(c.playerXP[10]) + "", 4033);
			sendFrame126("" + c.playerXP[10] + "", 4128);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[10]) + 1)
					+ "", 4129);
			break;

		case 11:
			sendFrame126("" + c.playerLevel[11] + "", 4036);
			sendFrame126("" + getLevelForXP(c.playerXP[11]) + "", 4037);
			sendFrame126("" + c.playerXP[11] + "", 4140);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[11]) + 1)
					+ "", 4141);
			break;

		case 12:
			sendFrame126("" + c.playerLevel[12] + "", 4024);
			sendFrame126("" + getLevelForXP(c.playerXP[12]) + "", 4025);
			sendFrame126("" + c.playerXP[12] + "", 4104);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[12]) + 1)
					+ "", 4105);
			break;

		case 13:
			sendFrame126("" + c.playerLevel[13] + "", 4030);
			sendFrame126("" + getLevelForXP(c.playerXP[13]) + "", 4031);
			sendFrame126("" + c.playerXP[13] + "", 4122);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[13]) + 1)
					+ "", 4123);
			break;

		case 14:
			sendFrame126("" + c.playerLevel[14] + "", 4028);
			sendFrame126("" + getLevelForXP(c.playerXP[14]) + "", 4029);
			sendFrame126("" + c.playerXP[14] + "", 4116);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[14]) + 1)
					+ "", 4117);
			break;

		case 15:
			sendFrame126("" + c.playerLevel[15] + "", 4020);
			sendFrame126("" + getLevelForXP(c.playerXP[15]) + "", 4021);
			sendFrame126("" + c.playerXP[15] + "", 4092);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[15]) + 1)
					+ "", 4093);
			break;

		case 16:
			sendFrame126("" + c.playerLevel[16] + "", 4018);
			sendFrame126("" + getLevelForXP(c.playerXP[16]) + "", 4019);
			sendFrame126("" + c.playerXP[16] + "", 4086);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[16]) + 1)
					+ "", 4087);
			break;

		case 17:
			sendFrame126("" + c.playerLevel[17] + "", 4022);
			sendFrame126("" + getLevelForXP(c.playerXP[17]) + "", 4023);
			sendFrame126("" + c.playerXP[17] + "", 4098);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[17]) + 1)
					+ "", 4099);
			break;

		case 18:
			sendFrame126("" + c.playerLevel[18] + "", 12166);
			sendFrame126("" + getLevelForXP(c.playerXP[18]) + "", 12167);
			sendFrame126("" + c.playerXP[18] + "", 12171);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[18]) + 1)
					+ "", 12172);
			break;

		case 19:
			sendFrame126("" + c.playerLevel[19] + "", 13926);
			sendFrame126("" + getLevelForXP(c.playerXP[19]) + "", 13927);
			sendFrame126("" + c.playerXP[19] + "", 13921);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[19]) + 1)
					+ "", 13922);
			break;

		case 20:
			sendFrame126("" + c.playerLevel[20] + "", 4152);
			sendFrame126("" + getLevelForXP(c.playerXP[20]) + "", 4153);
			sendFrame126("" + c.playerXP[20] + "", 4157);
			sendFrame126("" + getXPForLevel(getLevelForXP(c.playerXP[20]) + 1)
					+ "", 4158);
			break;
		}
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public boolean addSkillXP(int amount, int skill) {
		if (amount + c.playerXP[skill] < 0 || c.playerXP[skill] > 200000000) {
			if (c.playerXP[skill] > 200000000) {
				c.playerXP[skill] = 200000000;
			}
			return false;
		}
		int oldLevel = getLevelForXP(c.playerXP[skill]);
		c.playerXP[skill] += amount;
		if (oldLevel < getLevelForXP(c.playerXP[skill])) {
			if (c.playerLevel[skill] < c.getLevelForXP(c.playerXP[skill])
					&& skill != 3 && skill != 5)
				c.playerLevel[skill] = c.getLevelForXP(c.playerXP[skill]);
			levelUp(skill);
			c.gfx100(199);
			requestUpdates();
		}
		setSkillLevel(skill, c.playerLevel[skill], c.playerXP[skill]);
		refreshSkill(skill);
		return true;
	}

	public void resetBarrows() {
		c.barrowsNpcs[0][1] = 0;
		c.barrowsNpcs[1][1] = 0;
		c.barrowsNpcs[2][1] = 0;
		c.barrowsNpcs[3][1] = 0;
		c.barrowsNpcs[4][1] = 0;
		c.barrowsNpcs[5][1] = 0;
		c.getPoints()[1] = 0;
		c.randomCoffin = Misc.random(3) + 1;
	}

	public static int Barrows[] = { 4708, 4710, 4712, 4714, 4716, 4718, 4720,
			4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747,
			4749, 4751, 4753, 4755, 4757, 4759 };
	public static int Runes[] = { 4740, 558, 560, 565 };
	public static int Pots[] = {};

	public int randomBarrows() {
		return Barrows[(int) (Math.random() * Barrows.length)];
	}

	public int randomRunes() {
		return Runes[(int) (Math.random() * Runes.length)];
	}

	public int randomPots() {
		return Pots[(int) (Math.random() * Pots.length)];
	}

	/**
	 * Show an arrow icon on the selected player.
	 * 
	 * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
	 * @Param j - The player/Npc that the arrow will be displayed above.
	 * @Param k - Keep this set as 0
	 * @Param l - Keep this set as 0
	 */
	public void drawHeadicon(int i, int j, int k, int l) {
		c.outStream.createFrame(254);
		c.outStream.writeByte(i);

		if (i == 1 || i == 10) {
			c.outStream.writeWord(j);
			c.outStream.writeWord(k);
			c.outStream.writeByte(l);
		} else {
			c.outStream.writeWord(k);
			c.outStream.writeWord(l);
			c.outStream.writeByte(j);
		}
	}

	public int getNpcId(int id) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.npcs[i].npcId == id) {
					return i;
				}
			}
		}
		return -1;
	}

	public void removeObject(int x, int y) {
		object(-1, x, x, 10, 10);
	}

	private void objectToRemove(int X, int Y) {
		object(-1, X, Y, 10, 10);
	}

	private void objectToRemove2(int X, int Y) {
		object(-1, X, Y, -1, 0);
	}

	public void removeObjects() {
		objectToRemove(2638, 4688);
		objectToRemove2(2635, 4693);
		objectToRemove2(2634, 4693);
	}

	public int getDegradedGloryId(int gloryId) {
		switch (gloryId) {
		/*
		 * Regular Glory
		 */
		case 11978:
			return 11976;
		case 11976:
			return 1712;
		case 1712:
			return 1710;
		case 1710:
			return 1708;
		case 1708:
			return 1706;
		case 1706:
			return 1704;
		}
		return gloryId;
	}

	public void handleGlory(int gloryId, int itemSlot, boolean operate) {
		final Position[] positions = {
		/*
		 * Edgeville
		 */
		new Position(3108, 3499),
		/*
		 * Al Kharid
		 */
		new Position(3293, 3181),
		/*
		 * Draynor Village
		 */
		new Position(3093, 3244),
		/*
		 * Mage Bank
		 */
		new Position(2539, 4716)
		/*
				 * 
				 */
		};
		c.getInterfaces()
				.openOptions(
						(id) -> {
							if (startTeleport(positions[id].getX(),
									positions[id].getY(), positions[id].getZ(),
									TeleportType.MODERN, true)) {
								if (operate) {
									c.playerEquipment[itemSlot] = getDegradedGloryId(c.playerEquipment[itemSlot]);
									c.getItems().updateSlot(itemSlot);
								} else {
									c.playerItems[itemSlot] = getDegradedGloryId(c.playerItems[itemSlot] - 1) + 1;
									c.getItems().resetInventory();
								}
							}
						}, "Select Location", "Edgeville", "Al Kharid",
						"Draynor Village", "Mage Bank");

	}

	public void resetVariables() {
		if (c.skillActive()[c.playerWoodcutting]) {
			Woodcutting.resetWoodcutting(c);
		}
		if (c.skillActive()[c.playerFishing]) {
			Fishing.resetFishing(c);
		}
		if (c.skillActive()[c.playerMining]) {
			Mining.resetMining(c);
		}
		if (c.skillActive()[c.playerCooking]) {
			Cooking.resetCooking(c);
		}
		if (c.skillActive()[c.playerFiremaking]) {
			Firemaking.resetAction(c);
		}
		if (c.skillActive()[c.playerCrafting]) {
			GemCutting.resetAction(c);
		}
		if (c.skillActive()[c.playerSmithing]) {
			SmeltAction.resetAction(c);
			SmithingAction.resetAction(c);
		}
		if (c.skillActive()[c.playerFletching]) {
			Fletching.resetFletching(c);
		}
		if (c.isDicing()) {
			c.setDicingStatus(false);
		}
		if (c.canChangeAppearance) {
			c.canChangeAppearance = false;
		}
		if (c.itemToDestroy > -1) {
			c.itemToDestroy = -1;
		}
		c.isShopping = false;
		c.wildyTeleport = -1;
		c.interfaceAction = -1;
	}

	public boolean inPitsWait() {
		return c.getX() <= 2404 && c.getX() >= 2394 && c.getY() <= 5175
				&& c.getY() >= 5169;
	}

	public void castleWarsObjects() {
		object(-1, 2373, 3119, -3, 10);
		object(-1, 2372, 3119, -3, 10);
	}

	public int antiFire() {
		int toReturn = 0;
		if (System.currentTimeMillis() - c.lastAntiFire < 360000) {
			toReturn++;
		}
		if (c.playerEquipment[c.playerShield] == 1540 || c.prayerActive[12]
				|| c.playerEquipment[c.playerShield] == 11283) {
			toReturn++;
		}
		return toReturn;
	}

	public boolean checkForFlags() {
		int[][] itemsToCheck = { { 995, 100000000 }, { 35, 5 }, { 667, 5 },
				{ 2402, 5 }, { 746, 5 }, { 4151, 150 }, { 565, 100000 },
				{ 560, 100000 }, { 555, 300000 }, { 11235, 10 } };
		for (int j = 0; j < itemsToCheck.length; j++) {
			if (itemsToCheck[j][1] < c.getItems().getTotalCount(
					itemsToCheck[j][0]))
				return true;
		}
		return false;
	}

	public void addStarter() {
		c.getItems().addItem(995, 500000);
		c.getItems().addItem(380, 150);
		c.getItems().addItem(1153, 1);
		c.getItems().addItem(1712, 1);
		c.getItems().addItem(1101, 1);
		c.getItems().addItem(1067, 1);
		c.getItems().addItem(3105, 1);
		c.getItems().addItem(1323, 1);
		c.getItems().addItem(1309, 1);
		c.getItems().addItem(841, 1);
		c.getItems().addItem(884, 250);
		c.startTimer = 1800;
		Server.clanManager.getHelpClan().addMember(c);
	}

	public int getWearingAmount() {
		int count = 0;
		for (int j = 0; j < c.playerEquipment.length; j++) {
			if (c.playerEquipment[j] > 0)
				count++;
		}
		return count;
	}

	public void useOperate(int itemId) {
		switch (itemId) {
		case 1712:
		case 1710:
		case 1708:
		case 1706:
			handleGlory(itemId, c.playerAmulet, true);
			break;
		case 11283:
		case 11284:
			if (c.playerIndex > 0) {
				c.getCombat().handleDfs();
			} else if (c.npcIndex > 0) {
				c.getCombat().handleDfsNPC();
			}
			break;
		}
	}

	public void getSpeared(int otherX, int otherY) {
		int x = c.absX - otherX;
		int y = c.absY - otherY;
		if (x > 0)
			x = 1;
		else if (x < 0)
			x = -1;
		if (y > 0)
			y = 1;
		else if (y < 0)
			y = -1;
		moveCheck(x, y);
		c.lastSpear = System.currentTimeMillis();
	}

	public void moveCheck(int xMove, int yMove) {
		movePlayer(c.absX + xMove, c.absY + yMove, c.heightLevel);
	}

	public int findKiller() {
		int killer = c.playerId;
		int damage = 0;
		for (int j = 0; j < Config.MAX_PLAYERS; j++) {
			if (PlayerHandler.players[j] == null)
				continue;
			if (j == c.playerId)
				continue;
			if (c.goodDistance(c.absX, c.absY, PlayerHandler.players[j].absX,
					PlayerHandler.players[j].absY, 40)
					|| c.goodDistance(c.absX, c.absY + 9400,
							PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY, 40)
					|| c.goodDistance(c.absX, c.absY,
							PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY + 9400, 40))
				if (c.damageTaken[j] > damage) {
					damage = c.damageTaken[j];
					killer = j;
				}
		}
		return killer;
	}

	public void appendVenom(int damage) {
		if (System.currentTimeMillis() - c.lastPoisonSip > c.poisonImmune) {
			c.sendMessage("You have been poisoned!");
			c.venomDamage = damage;
		}
	}

	public void appendPoison(int damage) {
		if (System.currentTimeMillis() - c.lastPoisonSip > c.poisonImmune) {
			c.sendMessage("You have been poisoned.");
			c.poisonDamage = damage;
		}
	}

	public boolean checkForPlayer(int x, int y) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.getX() == x && p.getY() == y)
					return true;
			}
		}
		return false;
	}

	public void checkPouch(int i) {
		if (i < 0)
			return;
		c.sendMessage("This pouch has " + c.pouches[i] + " rune ess in it.");
	}

	public void fillPouch(int i) {
		if (i < 0)
			return;
		int toAdd = c.POUCH_SIZE[i] - c.pouches[i];
		if (toAdd > c.getItems().getItemAmount(1436)) {
			toAdd = c.getItems().getItemAmount(1436);
		}
		if (toAdd > c.POUCH_SIZE[i] - c.pouches[i])
			toAdd = c.POUCH_SIZE[i] - c.pouches[i];
		if (toAdd > 0) {
			c.getItems().deleteItem(1436, toAdd);
			c.pouches[i] += toAdd;
		}
	}

	public void emptyPouch(int i) {
		if (i < 0)
			return;
		int toAdd = c.pouches[i];
		if (toAdd > c.getItems().freeSlots()) {
			toAdd = c.getItems().freeSlots();
		}
		if (toAdd > 0) {
			c.getItems().addItem(1436, toAdd);
			c.pouches[i] -= toAdd;
		}
	}

	public void fixAllBarrows() {
		int totalCost = 0;
		int cashAmount = c.getItems().getItemAmount(995);
		for (int j = 0; j < c.playerItems.length; j++) {
			boolean breakOut = false;
			for (int i = 0; i < c.getItems().brokenBarrows.length; i++) {
				if (c.playerItems[j] - 1 == c.getItems().brokenBarrows[i][1]) {
					if (totalCost + 80000 > cashAmount) {
						breakOut = true;
						c.sendMessage("You have run out of money.");
						break;
					} else {
						totalCost += 80000;
					}
					c.playerItems[j] = c.getItems().brokenBarrows[i][0] + 1;
				}
			}
			if (breakOut)
				break;
		}
		if (totalCost > 0)
			c.getItems().deleteItem(995, c.getItems().getItemSlot(995),
					totalCost);
	}

	public void handleWeaponStyle() {
		if (c.fightMode == 0) {
			c.getPA().sendFrame36(43, c.fightMode);
		} else if (c.fightMode == 1) {
			c.getPA().sendFrame36(43, 3);
		} else if (c.fightMode == 2) {
			c.getPA().sendFrame36(43, 1);
		} else if (c.fightMode == 3) {
			c.getPA().sendFrame36(43, 2);
		}
	}

}
