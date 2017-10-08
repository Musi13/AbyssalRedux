package rs2.abyssalps.model.player;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Future;

import org.apache.mina.common.IoSession;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.content.TotalTime;
import rs2.abyssalps.content.achievement.Achievements;
import rs2.abyssalps.content.bank.Bank;
import rs2.abyssalps.content.bank.pin.Pins;
import rs2.abyssalps.content.beta.SuppliesSearch;
import rs2.abyssalps.content.bosslog.BossLogs;
import rs2.abyssalps.content.cannon.Cannon;
import rs2.abyssalps.content.interfaces.Interfaces;
import rs2.abyssalps.content.minigame.Minigame;
import rs2.abyssalps.content.minigame.barrows.Barrows;
import rs2.abyssalps.content.minigame.fightcave.FightCavesDefinitions;
import rs2.abyssalps.content.minigame.fightcave.FightCavesMinigame;
import rs2.abyssalps.content.minigame.fightpits.FightPitsConstants;
import rs2.abyssalps.content.minigame.fightpits.FightPitsMinigame;
import rs2.abyssalps.content.minigame.pestcontrol.PestControl;
import rs2.abyssalps.content.minigame.recipe_for_disaster.RecipeForDisasterMinigame;
import rs2.abyssalps.content.minigame.zulrah.ZulrahConstants;
import rs2.abyssalps.content.minigame.zulrah.ZulrahMinigame;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.Duel;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.content.multiplayer.trade.Trade;
import rs2.abyssalps.content.pets.Pets;
import rs2.abyssalps.content.tab.QuestTab;
import rs2.abyssalps.model.Boundary;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.items.ItemAssistant;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.player.combat.CombatAssistant;
import rs2.abyssalps.model.player.combat.DamageQueue;
import rs2.abyssalps.model.shop.ShopAssistant;
import rs2.net.HostList;
import rs2.net.Packet;
import rs2.net.StaticPacketBuilder;
import rs2.net.packet.PacketHandler;
import rs2.util.Misc;
import rs2.util.Stream;
import rs2.util.mysql.Highscores;
import rs2.util.tools.event.CycleEventHandler;
import rs2.world.Clan;

public class Client extends Player {

	public byte buffer[] = null;
	public Stream inStream = null, outStream = null;
	private IoSession session;
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Queue<Packet> queuedPackets = new LinkedList<Packet>();
	private Interfaces interfaces = new Interfaces(this);
	private Pins pins = new Pins(this);
	private Barrows barrows = new Barrows(this);
	private Friends friend = new Friends(this);
	private Ignores ignores = new Ignores(this);
	private DamageQueue damageQueue = new DamageQueue(this);
	private Trade trade = new Trade(this);
	private Duel duelSession = new Duel(this);
	private SuppliesSearch supplies = new SuppliesSearch(this);
	private Achievements achievements = new Achievements(this);
	private QuestTab questTab = new QuestTab(this);
	private BossLogs bossLogs = new BossLogs(this);
	private TotalTime totalTime = new TotalTime(this);
	private Cannon cannon = new Cannon(this);

	private RecipeForDisasterMinigame recipe = new RecipeForDisasterMinigame(
			this);

	public RecipeForDisasterMinigame getRecipe() {
		return this.recipe;
	}

	private Pets pets = new Pets(this);

	public Pets getPets() {
		return pets;
	}

	public Cannon getCannon() {
		return this.cannon;
	}

	private Minigame minigame;

	public Interfaces getInterfaces() {
		return interfaces;
	}

	public QuestTab getQuestTab() {
		return questTab;
	}

	public BossLogs getBossLogs() {
		return this.bossLogs;
	}

	public Pins getBankPin() {
		return pins;
	}

	public Barrows getBarrows() {
		return barrows;
	}

	public Friends getFriends() {
		return friend;
	}

	public Ignores getIgnores() {
		return ignores;
	}

	public DamageQueue getDamage() {
		return damageQueue;
	}

	public Trade getTrade() {
		return trade;
	}

	public Achievements getAchievements() {
		return achievements;
	}

	public Duel getDuel() {
		return duelSession;
	}

	public TotalTime getTotalTime() {
		return this.totalTime;
	}

	private Bank bank;

	public Bank getBank() {
		if (bank == null)
			bank = new Bank(this);
		return bank;
	}

	public SuppliesSearch getSupplies() {
		return supplies;
	}

	public Minigame getMinigame() {
		return minigame;
	}

	public boolean enterMinigame(Minigame minigame) {
		this.minigame = minigame;
		return minigame.enter(this);
	}

	public void exitMinigame() {
		if (minigame == null) {
			return;
		}
		minigame.exit(this);
		minigame = null;
	}

	public String getMacAddress() {
		try {
			InetAddress a = connectedLocal;
			NetworkInterface n = NetworkInterface.getByInetAddress(a);
			byte[] m = n.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < m.length; i++) {
				sb.append(String.format("%02X%s", m[i],
						(i < m.length - 1) ? "-" : ""));
			}
			connectedMac = sb.toString();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return connectedMac;
	}

	/**
	 * Skill instances
	 */

	private int somejunk;
	public int lowMemoryVersion = 0;
	public int timeOutCounter = 0;
	public int returnCode = 2;
	private Future<?> currentTask;

	public Client(IoSession s, int _playerId) {
		super(_playerId);
		this.session = s;
		outStream = new Stream(new byte[Config.BUFFER_SIZE]);
		outStream.currentOffset = 0;
		inStream = new Stream(new byte[Config.BUFFER_SIZE]);
		inStream.currentOffset = 0;
		buffer = new byte[Config.BUFFER_SIZE];
	}

	public void flushOutStream() {
		if (disconnected || outStream.currentOffset == 0)
			return;
		StaticPacketBuilder out = new StaticPacketBuilder().setBare(true);
		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		out.addBytes(temp);
		session.write(out.toPacket());
		outStream.currentOffset = 0;
	}

	public void sendClan(String name, String message, String clan, int rights) {
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		message = message.substring(0, 1).toUpperCase() + message.substring(1);
		clan = clan.substring(0, 1).toUpperCase() + clan.substring(1);
		outStream.createFrameVarSizeWord(217);
		outStream.writeString(name);
		outStream.writeString(message);
		outStream.writeString(clan);
		outStream.writeWord(rights);
		outStream.endFrameVarSize();
	}

	public static final int PACKET_SIZES[] = { 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 16, 0, -1, -1, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			6, 10, -1, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, /* 0 */4, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};

	public void destruct() {
		if (session == null)
			return;
		getFriends().notifyFriendsOfUpdate();
		CycleEventHandler.getSingleton().stopEvents(this);
		if (clan != null) {
			clan.removeMember(this);
		}
		if (getPA().viewingOtherBank) {
			getPA().resetOtherBank();
		}
		if (underAttackBy > 0 || underAttackBy2 > 0) {
			return;
		}
		if (disconnected == true) {
			saveCharacter = true;
		}
		if (FightPitsMinigame.inFightPits(this)) {
			FightPitsMinigame.destructGameForPlayer(this);
		}
		Server.getMultiplayerSessionListener().removeOldRequests(this);
		Misc.println("[DEREGISTERED]: " + playerName + "");
		HostList.getHostList().remove(session);
		disconnected = true;
		session.close();
		session = null;
		inStream = null;
		outStream = null;
		isActive = false;
		buffer = null;
		super.destruct();
	}

	public void joinHelpClan() {
		if (clan == null) {
			Clan localClan = Server.clanManager.getClan("help");
			if (localClan != null)
				localClan.addMember(this);
			else if ("help".equalsIgnoreCase(this.playerName))
				Server.clanManager.create(this);
			else {
				sendMessage(Misc.formatPlayerName("OSPvP")
						+ " has temporarily disabled the help chat.");
			}
			getPA().refreshSkill(21);
			getPA().refreshSkill(22);
			getPA().refreshSkill(23);
			// inArdiCC = true;
		}
	}

	public void sendMessage(String s) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}
	}

	public void setSidebarInterface(int menuId, int form) {
		if (getOutStream() != null) {
			outStream.createFrame(71);
			outStream.writeWord(form);
			outStream.writeByteA(menuId);
		}
	}

	private void changeText() {
		getPA().sendFrame126("Training Teleports", 1300);
		getPA().sendFrame126("Various Training Teleports", 1301);
		getPA().sendFrame126("Minigame Teleports", 1325);
		getPA().sendFrame126("Various Minigame Teleports", 1326);
		getPA().sendFrame126("Pking Teleports", 1350);
		getPA().sendFrame126("Various PK location teleports", 1351);
		getPA().sendFrame126("Skilling Area", 1382);
		getPA().sendFrame126("Skilling Area Teleport", 1383);
		getPA().sendFrame126("Boss Teleports", 1415);
		getPA().sendFrame126("Various boss location teleports", 1416);

		getPA().sendFrame126("Training Teleports", 13037); // paddewwa
		getPA().sendFrame126("Various Training Teleports", 13038); // paddewwa
																	// description
		getPA().sendFrame126("Minigame Teleports", 13047); // senntisten
		getPA().sendFrame126("Various Minigame Teleports", 13048); // senntisten
																	// description
		getPA().sendFrame126("Pking Teleports", 13055); // kharyll
		getPA().sendFrame126("Various PK location teleports", 13056); // kharyll
																		// description
		getPA().sendFrame126("Skilling Area", 13063); // lassar
		getPA().sendFrame126("Skilling Area Teleport", 13064); // lassar
																// description
		getPA().sendFrame126("Boss Teleports", 13071); // dareeyak
		getPA().sendFrame126("Various boss location teleports", 13072); // dareeyak
																		// description
	}

	public void initialize() {
		outStream.createFrame(249);
		outStream.writeByteA(1); // 1 for members, zero for free
		outStream.writeWordBigEndianA(playerId);
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == playerId)
				continue;
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName
						.equalsIgnoreCase(playerName))
					disconnected = true;
			}
		}
		for (int i = 0; i < 25; i++) {
			getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
			getPA().refreshSkill(i);
		}
		for (int p = 0; p < PRAYER.length; p++) { // reset prayer glows
			prayerActive[p] = false;
			getPA().sendFrame36(PRAYER_GLOW[p], 0);
		}
		if (!playerIPs.contains(connectedFrom)) {
			playerIPs.add(connectedFrom);
		}
		changeText();
		getPA().handleWeaponStyle();
		accountFlagged = getPA().checkForFlags();
		getPA().sendFrame36(108, 0);
		getPA().sendFrame36(172, 1);
		getPA().sendFrame107();
		getPA().setChatOptions(0, 0, 0);
		setSidebarInterface(1, 3917);
		setSidebarInterface(2, 638);
		setSidebarInterface(3, 3213);
		setSidebarInterface(4, 1644);
		setSidebarInterface(5, 5608);
		if (playerMagicBook == 0) {
			setSidebarInterface(6, 1151); // modern
		} else {
			if (playerMagicBook == 2) {
				setSidebarInterface(6, 29999); // lunar
			} else {
				setSidebarInterface(6, 12855); // ancient
			}
		}
		sendMessage("Welcome to AbyssalPS.");
		sendMessage("Site: @blu@http://www.abyssalps.com/");
		sendMessage("Buy AbyssalPS Tokens, type @blu@::store");
		sendMessage("Vote for AbyssalPS, type @blu@::vote");
		setSidebarInterface(7, 18128);
		setSidebarInterface(8, 5065);
		setSidebarInterface(9, 5715);
		setSidebarInterface(10, 2449);
		setSidebarInterface(11, 904); // wrench tab
		setSidebarInterface(12, 147); // run tab
		setSidebarInterface(13, -1);
		setSidebarInterface(0, 2423);
		getPA().showOption(4, 0, "Trade With", 3);
		getPA().showOption(5, 0, "Follow", 4);
		getItems().resetItems(3214);
		getItems().sendWeapon(playerEquipment[playerWeapon],
				ItemAssistant.getItemName(playerEquipment[playerWeapon]));
		getItems().resetBonus();
		getItems().getBonus();
		getItems().writeBonus();
		getItems().setEquipment(playerEquipment[playerHat], 1, playerHat);
		getItems().setEquipment(playerEquipment[playerCape], 1, playerCape);
		getItems().setEquipment(playerEquipment[playerAmulet], 1, playerAmulet);
		getItems().setEquipment(playerEquipment[playerArrows],
				playerEquipmentN[playerArrows], playerArrows);
		getItems().setEquipment(playerEquipment[playerChest], 1, playerChest);
		getItems().setEquipment(playerEquipment[playerShield], 1, playerShield);
		getItems().setEquipment(playerEquipment[playerLegs], 1, playerLegs);
		getItems().setEquipment(playerEquipment[playerHands], 1, playerHands);
		getItems().setEquipment(playerEquipment[playerFeet], 1, playerFeet);
		getItems().setEquipment(playerEquipment[playerRing], 1, playerRing);
		getItems().setEquipment(playerEquipment[playerWeapon],
				playerEquipmentN[playerWeapon], playerWeapon);
		getCombat().getPlayerAnimIndex();

		if (getPrivateChat() > 2) {
			setPrivateChat(0);
		}

		outStream.createFrame(221);
		outStream.writeByte(2);
		outStream.createFrame(206);
		outStream.writeByte(0);
		outStream.writeByte(getPrivateChat());
		outStream.writeByte(0);
		getFriends().sendList();
		getIgnores().sendList();
		getItems().addSpecialBar(playerEquipment[playerWeapon]);
		saveTimer = Config.SAVE_TIMER;
		saveCharacter = true;
		Misc.println("[REGISTERED]: " + playerName + "");
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();
		getPA().clearClanChat();
		getPA().resetFollow();
		getPA().setClanData();
		if (addStarter)
			getPA().addStarter();
		if (autoRet == 1)
			getPA().sendFrame36(172, 1);
		else
			getPA().sendFrame36(172, 0);
		if (FightCavesDefinitions.inArea(this)) {
			enterMinigame(new FightCavesMinigame());
		}
	}

	public void update() {
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();
	}

	public void logout() {
		if (this.clan != null) {
			this.clan.removeMember(this);
		}
		if (skillActive()[playerAgility]) {
			return;
		}
		if (FightPitsMinigame.inFightPits(this)) {
			return;
		}
		if (getPA().viewingOtherBank) {
			getPA().resetOtherBank();
		}
		DuelSession duelSession = (DuelSession) Server
				.getMultiplayerSessionListener().getMultiplayerSession(this,
						MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession)
				&& duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST) {
			if (duelSession.getStage().getStage() >= MultiplayerSessionStage.FURTHER_INTERACTION) {
				sendMessage("You are not permitted to logout during a duel. If you forcefully logout you will");
				sendMessage("lose all of your staked items, if any, to your opponent.");
			}
		}
		if (underAttackBy > 0 || underAttackBy2 > 0) {
			return;
		}
		if (System.currentTimeMillis() - logoutDelay > 10000) {
			CycleEventHandler.getSingleton().stopEvents(this);
			outStream.createFrame(109);
			properLogout = true;
		} else {
			sendMessage("You must wait a few seconds from being out of combat to logout.");
		}
	}

	public int packetSize = 0, packetType = -1;
	public int donatorPoints = 0;

	public void process() {
		if (getPetId() > 0) {
			getPets().process();
		}

		getQuestTab().redraw();
		getTotalTime().tick();

		if (minigame != null) {
			minigame.process(this);
		}

		if (startTimer > 0) {
			startTimer--;
		}

		if (gfxsToLoop > 0 && gfxsToLoop < 1296) {
			lastGfx--;
			if (lastGfx == 0) {
				sendMessage("gfx: " + gfxsToLoop);
				gfx100(gfxsToLoop);
				gfxsToLoop++;
				lastGfx = 3;
			}
		}

		if (animsToLoop < 7207) {
			lastAnim--;
			if (lastAnim == 2) {
				startAnimation(65535);

			}
			if (lastAnim == 0) {
				sendMessage("Anim: " + animsToLoop);
				startAnimation(animsToLoop);
				animsToLoop++;
				lastAnim = 4;
			}
		}

		getDamage().execute();

		if (minigame != null && minigame instanceof ZulrahMinigame) {
			if (Server.objectManager.objectExists(
					ZulrahConstants.TOXIC_CLOUD_ID, absX, absY, 2, 2,
					heightLevel)) {
				lastVenom = System.currentTimeMillis() / 2;
				venomDamage = Misc.random2(5);
			}
		}

		if (isDead) {
			lastVenom = System.currentTimeMillis();
			venomDamage = 0;
		}

		if (playerEquipment[playerHat] == 12931) {
			poisonDamage = 0;
			venomDamage = 0;
		}

		if ((System.currentTimeMillis() - lastVenom) >= 20000
				&& venomDamage > 0) {
			int damage = venomDamage;
			if (!getHitUpdateRequired()) {
				setHitUpdateRequired(true);
				setHitDiff(damage);
				poisonMask = 1;
				updateRequired = true;
			} else if (!getHitUpdateRequired2()) {
				setHitUpdateRequired2(true);
				setHitDiff2(damage);
				poisonMask = 2;
				updateRequired = true;
			}
			if (venomDamage < 20) {
				venomDamage += 2;
			}
			if (damage > playerLevel[3]) {
				damage = playerLevel[3];
			}
			dealDamage(damage);
			getPA().refreshSkill(3);
			lastVenom = System.currentTimeMillis();
		}

		if (System.currentTimeMillis() - lastPoison > 20000 && poisonDamage > 0) {
			int damage = poisonDamage / 2;
			if (damage > 0) {
				if (!getHitUpdateRequired()) {
					setHitUpdateRequired(true);
					setHitDiff(damage);
					updateRequired = true;
					poisonMask = 1;
				} else if (!getHitUpdateRequired2()) {
					setHitUpdateRequired2(true);
					setHitDiff2(damage);
					updateRequired = true;
					poisonMask = 2;
				}
				lastPoison = System.currentTimeMillis();
				poisonDamage--;
				dealDamage(damage);
			} else {
				poisonDamage = -1;
				sendMessage("You are no longer poisoned.");
			}
		}

		if (System.currentTimeMillis() - specDelay > Config.INCREASE_SPECIAL_AMOUNT) {
			specDelay = System.currentTimeMillis();
			if (specAmount < 10) {
				specAmount += .5;
				if (specAmount > 10)
					specAmount = 10;
				getItems().addSpecialBar(playerEquipment[playerWeapon]);
			}
		}

		if (clickObjectType > 0
				&& goodDistance(objectX + objectXOffset, objectY
						+ objectYOffset, getX(), getY(), objectDistance)) {
			if (clickObjectType == 1) {
				getActions().firstClickObject(objectId, objectX, objectY);
			}
			if (clickObjectType == 2) {
				getActions().secondClickObject(objectId, objectX, objectY);
			}
			if (clickObjectType == 3) {
				getActions().thirdClickObject(objectId, objectX, objectY);
			}
		}

		if ((clickNpcType > 0) && NPCHandler.npcs[npcClickIndex] != null) {
			if (goodDistance(getX(), getY(),
					NPCHandler.npcs[npcClickIndex].getX(),
					NPCHandler.npcs[npcClickIndex].getY(), 1)) {
				if (clickNpcType == 1) {
					turnPlayerTo(NPCHandler.npcs[npcClickIndex].getX(),
							NPCHandler.npcs[npcClickIndex].getY());
					NPCHandler.npcs[npcClickIndex].facePlayer(playerId);
					getActions().firstClickNpc(npcType);
				}
				if (clickNpcType == 2) {
					turnPlayerTo(NPCHandler.npcs[npcClickIndex].getX(),
							NPCHandler.npcs[npcClickIndex].getY());
					NPCHandler.npcs[npcClickIndex].facePlayer(playerId);
					getActions().secondClickNpc(npcType);
				}
				if (clickNpcType == 3) {
					turnPlayerTo(NPCHandler.npcs[npcClickIndex].getX(),
							NPCHandler.npcs[npcClickIndex].getY());
					NPCHandler.npcs[npcClickIndex].facePlayer(playerId);
					getActions().thirdClickNpc(npcType);
				}
			}
		}

		if (walkingToItem) {
			if (getX() == pItemX && getY() == pItemY
					|| goodDistance(getX(), getY(), pItemX, pItemY, 1)) {
				walkingToItem = false;
				Server.itemHandler.removeGroundItem(this, pItemId, pItemX,
						pItemY, heightLevel, true);
			}
		}

		getCombat().handlePrayerDrain();

		if (System.currentTimeMillis() - singleCombatDelay > 3300) {
			underAttackBy = 0;
		}
		if (System.currentTimeMillis() - singleCombatDelay2 > 3300) {
			underAttackBy2 = 0;
		}

		if (System.currentTimeMillis() - restoreStatsDelay > 60000) {
			restoreStatsDelay = System.currentTimeMillis();
			for (int level = 0; level < playerLevel.length; level++) {
				if (playerLevel[level] < getLevelForXP(playerXP[level])) {
					if (level != 5) { // prayer doesn't restore
						playerLevel[level] += 1;
						getPA().setSkillLevel(level, playerLevel[level],
								playerXP[level]);
						getPA().refreshSkill(level);
					}
				} else if (playerLevel[level] > getLevelForXP(playerXP[level])) {
					playerLevel[level] -= 1;
					getPA().setSkillLevel(level, playerLevel[level],
							playerXP[level]);
					getPA().refreshSkill(level);
				}
			}
		}

		if (System.currentTimeMillis() - teleGrabDelay > 1550 && usingMagic) {
			usingMagic = false;
			if (Server.itemHandler.itemExists(teleGrabItem, teleGrabX,
					teleGrabY, heightLevel)) {
				Server.itemHandler.removeGroundItem(this, teleGrabItem,
						teleGrabX, teleGrabY, heightLevel, true);
			}
		}

		if (inWild()) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			getPA().walkableInterface(197);
			if (Config.SINGLE_AND_MULTI_ZONES) {
				if (inMulti()) {
					getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
				} else {
					getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
				}
			} else {
				getPA().multiWay(-1);
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			}
			getPA().showOption(3, 0, "Attack", 1);
		} else if (FightPitsMinigame.inGame(this)) {
			FightPitsConstants.gameInterface(this);
			getPA().showOption(3, 0, "Attack", 1);
		} else if (Boundary.isIn(this, Boundary.PVP_ZONE)) {
			getPA().showOption(3, 0, "Attack", 1);
		} else if ((inDuelArena() || Boundary.isIn(this, Boundary.DUEL_ARENAS))) {
			getPA().walkableInterface(201);
			if (Boundary.isIn(this, Boundary.DUEL_ARENAS)) {
				getPA().showOption(3, 0, "Attack", 1);
			} else {
				getPA().showOption(3, 0, "Challenge", 1);
			}
			wildLevel = 126;
		} else if (inBarrows()) {
			getPA().sendFrame126("Kill Count: " + getPoints()[1], 4536);
			getPA().walkableInterface(4535);
		} else if (PestControl.playerInBoat(this)) {
			PestControl.sendInterface(this);
			getPA().walkableInterface(27119);
		} else if (!inCwWait && getMinigame() == null) {
			getPA().sendFrame99(0);
			getPA().walkableInterface(-1);
			getPA().showOption(3, 0, "Null", 1);
		}

		if (!hasMultiSign && inMulti()) {
			hasMultiSign = true;
			getPA().multiWay(1);
		}

		if (hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getPA().multiWay(-1);
		}

		if (skullTimer > 0) {
			skullTimer--;
			if (skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				getPA().requestUpdates();
			}
		}

		if (isDead && respawnTimer == -6) {
			getPA().applyDead();
		}

		if (respawnTimer == 7) {
			respawnTimer = -6;
			getPA().giveLife();
		} else if (respawnTimer == 12) {
			respawnTimer--;
			startAnimation(836);
			poisonDamage = -1;
		}

		if (respawnTimer > -6) {
			respawnTimer--;
		}
		if (freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (PlayerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!goodDistance(absX, absY,
						PlayerHandler.players[frozenBy].absX,
						PlayerHandler.players[frozenBy].absY, 20)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}

		if (teleTimer > 0) {
			teleTimer--;
			if (!isDead) {
				if (teleTimer == 1 && newLocation > 0) {
					teleTimer = 0;
					getPA().changeLocation();
				}
				if (teleTimer == 5) {
					teleTimer--;
					getPA().processTeleport();
				}
				if (teleTimer == 8 && teleGfx > 0) {
					teleTimer--;
					gfx100(teleGfx);
				}
			} else {
				teleTimer = 0;
			}
		}

		if (attackTimer > 0) {
			attackTimer--;
		}

		if (attackTimer == 1) {
			if (npcIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcIndex);
			}
			if (playerIndex > 0) {
				getCombat().attackPlayer(playerIndex);
			}
		} else if (attackTimer <= 0 && (npcIndex > 0 || playerIndex > 0)) {
			if (npcIndex > 0) {
				attackTimer = 0;
				getCombat().attackNpc(npcIndex);
			} else if (playerIndex > 0) {
				attackTimer = 0;
				getCombat().attackPlayer(playerIndex);
			}
		}
	}

	public void setCurrentTask(Future<?> task) {
		currentTask = task;
	}

	public Future<?> getCurrentTask() {
		return currentTask;
	}

	public synchronized Stream getInStream() {
		return inStream;
	}

	public synchronized int getPacketType() {
		return packetType;
	}

	public synchronized int getPacketSize() {
		return packetSize;
	}

	public synchronized Stream getOutStream() {
		return outStream;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}

	public ShopAssistant getShops() {
		return shopAssistant;
	}

	public CombatAssistant getCombat() {
		return combatAssistant;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}

	public IoSession getSession() {
		return session;
	}

	public void queueMessage(Packet arg1) {
		packetsReceived.incrementAndGet();
		queuedPackets.add(arg1);
	}

	public void processQueuedPackets() {
		packetsReceived.set(0);
		for (;;) {
			Packet next = queuedPackets.poll();
			if (next == null) {
				break;
			}
			inStream.currentOffset = 0;
			packetType = next.getId();
			packetSize = next.getLength();
			inStream.buffer = next.getData();
			if (packetType > 0) {// but
									// where
				PacketHandler.processPacket(this, packetType, packetSize);
			}
			timeOutCounter = 0;
		}
	}

	public Position getPosition() {
		return new Position(getX(), getY(), heightLevel);
	}

}
