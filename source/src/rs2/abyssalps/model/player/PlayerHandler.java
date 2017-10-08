package rs2.abyssalps.model.player;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.model.Boundary;
import rs2.abyssalps.model.items.GroundItem;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.util.Misc;
import rs2.util.Stream;

public class PlayerHandler {

	public static Player players[] = new Player[Config.MAX_PLAYERS];
	public static String messageToAll = "";
	public static int playerCount = 0;
	public static String playersCurrentlyOn[] = new String[Config.MAX_PLAYERS];
	public static boolean updateAnnounced;
	public static boolean updateRunning;
	public static int updateSeconds;
	public static long updateStartTime;
	private boolean kickAllPlayers = false;

	static {
		Runtime.getRuntime().addShutdownHook(new Thread());
		for (int i = 0; i < Config.MAX_PLAYERS; i++)
			players[i] = null;
	}

	public static Player getPlayer(String name) {
		for (int d = 0; d < Config.MAX_PLAYERS; d++) {
			if (PlayerHandler.players[d] != null) {
				Player o = PlayerHandler.players[d];
				if (o.playerName.equalsIgnoreCase(name)) {
					return o;
				}
			}
		}
		return null;
	}

	public static List<Player> getPlayers() {
		Player[] clients = new Player[players.length];
		System.arraycopy(players, 0, clients, 0, players.length);
		return Arrays.asList(clients).stream().filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	public static Optional<Player> getOptionalPlayer(String name) {
		return getPlayers().stream().filter(Objects::nonNull)
				.filter(client -> client.playerName.equalsIgnoreCase(name))
				.findFirst();
	}

	public static Client getPlayerByLongName(long name) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] == null)
				continue;
			if (PlayerHandler.players[i].getNameAsLong() == name)
				return (Client) PlayerHandler.players[i];
		}
		return null;
	}

	public boolean newPlayerClient(Client client1) {
		int slot = -1;
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if ((players[i] == null) || players[i].disconnected) {
				slot = i;
				break;
			}
		}
		if (slot == -1)
			return false;
		client1.handler = this;
		client1.playerId = slot;
		players[slot] = client1;
		players[slot].isActive = true;
		try {
			players[slot].connectedFrom = ((InetSocketAddress) client1
					.getSession().getRemoteAddress()).getAddress()
					.getHostAddress();
			players[slot].connectedLocal = ((InetSocketAddress) client1
					.getSession().getRemoteAddress()).getAddress()
					.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Config.SERVER_DEBUG)
			Misc.println("Player Slot " + slot + " slot 0 " + players[0]
					+ " Player Hit " + players[slot]);
		return true;
	}

	public void destruct() {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null)
				continue;
			players[i].destruct();
			players[i] = null;
		}
	}

	public static int getPlayerCount() {
		return playerCount;
	}

	public void updatePlayerNames() {
		playerCount = 0;
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] != null) {
				playersCurrentlyOn[i] = players[i].playerName;
				playerCount++;
			} else {
				playersCurrentlyOn[i] = "";
			}
		}
	}

	public static boolean isPlayerOn(String playerName) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (playersCurrentlyOn[i] != null) {
				if (playersCurrentlyOn[i].equalsIgnoreCase(playerName)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void globalMessage(String message) {
		for (Player player : players) {
			if (player == null) {
				continue;
			}
			((Client) player).sendMessage("[@blu@Global@bla@] " + message);
		}
	}

	public void process() {
		updatePlayerNames();

		if (kickAllPlayers) {
			for (int i = 1; i < Config.MAX_PLAYERS; i++) {
				if (players[i] != null) {
					players[i].disconnected = true;
				}
			}
		}

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive)
				continue;
			try {

				if (players[i].disconnected
						&& System.currentTimeMillis() - players[i].logoutDelay > 10000
						|| players[i].properLogout || kickAllPlayers) {
					Client player = (Client) players[i];
					if (Server.getMultiplayerSessionListener().inSession(
							player, MultiplayerSessionType.TRADE)) {
						Server.getMultiplayerSessionListener().finish(player,
								MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
					}
					DuelSession duelSession = (DuelSession) Server
							.getMultiplayerSessionListener()
							.getMultiplayerSession(player,
									MultiplayerSessionType.DUEL);
					if (Objects.nonNull(duelSession)
							&& duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST) {
						if (duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
							duelSession
									.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
						} else {
							Client winner = duelSession.getOther(player);
							duelSession.setWinner(winner);
							duelSession
									.finish(MultiplayerSessionFinalizeType.GIVE_ITEMS);
						}
					}

					Client o = (Client) PlayerHandler.players[i];
					if (o.getMinigame() != null) {
						o.getMinigame().onLogout(o);
					}
					if (PlayerSave.saveGame(o)) {
						System.out.println("Game saved for player "
								+ players[i].playerName);
					} else {
						System.out.println("Could not save for "
								+ players[i].playerName);
					}
					removePlayer(players[i]);
					players[i] = null;
					continue;
				}

				players[i].processQueuedPackets();
				((Client) players[i]).getPA().handleRealFollowing();
				players[i].process();
				players[i].getNextPlayerMovement();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive)
				continue;
			try {
				if (!players[i].initialized) {
					players[i].initialize();
					players[i].initialized = true;
				} else {
					players[i].update();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (updateRunning && !updateAnnounced) {
			updateAnnounced = true;
			Server.UpdateServer = true;
		}
		if (updateRunning
				&& (System.currentTimeMillis() - updateStartTime > (updateSeconds * 1000))) {
			kickAllPlayers = true;
		}

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive)
				continue;
			try {
				players[i].clearUpdateFlags();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void updateNPC(Player plr, Stream str) {
		updateBlock.currentOffset = 0;

		str.createFrameVarSizeWord(65);
		str.initBitAccess();

		str.writeBits(8, plr.npcListSize);
		int size = plr.npcListSize;
		plr.npcListSize = 0;
		for (int i = 0; i < size; i++) {
			if (plr.RebuildNPCList == false
					&& plr.withinDistance(plr.npcList[i]) == true) {
				plr.npcList[i].updateNPCMovement(str);
				plr.npcList[i].appendNPCUpdateBlock(updateBlock);
				plr.npcList[plr.npcListSize++] = plr.npcList[i];
			} else {
				int id = plr.npcList[i].npcId;
				plr.npcInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}

		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				int id = NPCHandler.npcs[i].npcId;
				if (plr.RebuildNPCList == false
						&& (plr.npcInListBitmap[id >> 3] & (1 << (id & 7))) != 0) {

				} else if (plr.withinDistance(NPCHandler.npcs[i]) == false) {

				} else {
					plr.addNewNPC(NPCHandler.npcs[i], str, updateBlock);
				}
			}
		}

		plr.RebuildNPCList = false;

		if (updateBlock.currentOffset > 0) {
			str.writeBits(14, 16383);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();
	}

	private Stream updateBlock = new Stream(new byte[Config.BUFFER_SIZE]);

	public void updatePlayer(Player plr, Stream str) {
		updateBlock.currentOffset = 0;
		if (updateRunning && !updateAnnounced) {
			str.createFrame(114);
			str.writeWordBigEndian(updateSeconds * 50 / 30);
		}
		plr.updateThisPlayerMovement(str);
		boolean saveChatTextUpdate = plr.isChatTextUpdateRequired();
		plr.setChatTextUpdateRequired(false);
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setChatTextUpdateRequired(saveChatTextUpdate);
		str.writeBits(8, plr.playerListSize);
		int size = plr.playerListSize;
		if (size > 255)
			size = 255;
		plr.playerListSize = 0;
		for (int i = 0; i < size; i++) {
			if (!plr.didTeleport && !plr.playerList[i].didTeleport
					&& plr.withinDistance(plr.playerList[i])) {
				plr.playerList[i].updatePlayerMovement(str);
				plr.playerList[i].appendPlayerUpdateBlock(updateBlock);
				plr.playerList[plr.playerListSize++] = plr.playerList[i];
			} else {
				int id = plr.playerList[i].playerId;
				plr.playerInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive || players[i] == plr)
				continue;
			int id = players[i].playerId;
			if ((plr.playerInListBitmap[id >> 3] & (1 << (id & 7))) != 0)
				continue;
			if (!plr.withinDistance(players[i]))
				continue;
			plr.addNewPlayer(players[i], str, updateBlock);
		}

		if (updateBlock.currentOffset > 0) {
			str.writeBits(11, 2047);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else
			str.finishBitAccess();

		str.endFrameVarSizeWord();
	}

	private void removePlayer(Player plr) {
		if (plr.privateChat != 2) {
			for (int i = 1; i < Config.MAX_PLAYERS; i++) {
				if (players[i] == null || players[i].isActive == false)
					continue;
				Client o = (Client) PlayerHandler.players[i];
				if (o != null) {
					o.getPA().updatePM(plr.playerId, 0);
				}
			}
		}
		plr.destruct();
	}

	public static java.util.stream.Stream<Player> stream() {
		return Arrays.stream(players);
	}

}
