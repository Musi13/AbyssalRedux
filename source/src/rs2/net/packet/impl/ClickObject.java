package rs2.net.packet.impl;

import java.util.Objects;

import rs2.Server;
import rs2.abyssalps.content.minigame.fightcave.FightCavesMinigame;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.content.skill.mining.RockData;
import rs2.abyssalps.content.skill.runecrafting.AltarTable;
import rs2.abyssalps.content.skill.woodcutting.TreeData;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;
import rs2.util.Misc;
import rs2.util.cache.region.Region;

public class ClickObject implements PacketType {

	public static final int FIRST_CLICK = 132;
	public static final int SECOND_CLICK = 252;
	public static final int THIRD_CLICK = 70;

	public ClickObject() {
	}

	private static boolean ignore(int objectType) {
		switch (objectType) {
		case 20772:
		case 20771:
		case 20720:
		case 20722:
		case 20770:
		case 20721:
		case 20672:
		case 20667:
		case 20668:
		case 20670:
		case 20671:
		case 20669:
			return true;
		}
		return false;
	}

	public void processPacket(Client client, int i, int j) {
		if (!client.getCanWalk()) {
			return;
		}
		client.clickObjectType = client.objectX = client.objectId = client.objectY = 0;
		client.objectYOffset = client.objectXOffset = 0;
		client.getPA().resetFollow();
		switch (i) {
		default:
			break;

		case 132:
			client.objectX = client.getInStream().readSignedWordBigEndianA();
			client.objectId = client.getInStream().readUnsignedWord();
			client.objectY = client.getInStream().readUnsignedWordA();
			client.objectDistance = 1;
			if (!Region.isWorldObject(client.objectId, client.objectX,
					client.objectY, client.heightLevel)
					&& client.objectId != 11834
					&& !Server.objectManager.objects.contains(client.objectId)) {
				System.out.println("Object not configured.");
				if (client.getRank() >= 8) {
					client.sendMessage("Object ID: " + client.objectId + ", "
							+ client.objectX + ", " + client.objectY);
				}
				return;
			}
			if (client.getRank() >= 8
					&& client.playerName.equalsIgnoreCase("Sanity")) {
				Misc.println((new StringBuilder()).append("objectId: ")
						.append(client.objectId).append("  ObjectX: ")
						.append(client.objectX).append("  objectY: ")
						.append(client.objectY).append(" Xoff: ")
						.append(client.getX() - client.objectX)
						.append(" Yoff: ")
						.append(client.getY() - client.objectY).toString());
			}

			if (Math.abs(client.getX() - client.objectX) > 25
					|| Math.abs(client.getY() - client.objectY) > 25) {
				client.resetWalkingQueue();
			} else {

				DuelSession duelSession = (DuelSession) Server
						.getMultiplayerSessionListener().getMultiplayerSession(
								client, MultiplayerSessionType.DUEL);
				if (Objects.nonNull(duelSession)
						&& duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
						&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
					client.sendMessage("Your actions have declined the duel.");
					duelSession.getOther(client).sendMessage(
							"The challenger has declined the duel.");
					duelSession
							.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
					return;
				}

				switch (client.objectId) {

				case 677:
					client.objectDistance = 3;
					break;

				case 678:
					client.objectDistance = 5;
					break;

				case 23271:
					if (client.getY() > client.objectY) {
						client.objectDistance = 3;
					}
					break;

				case 26258:
					client.objectDistance = 3;
					break;

				case 11833:
					client.enterMinigame(new FightCavesMinigame());
					break;

				case 26646:
				case 26645:
					client.objectDistance = 3;
					break;

				case 20772:
				case 20771:
				case 20720:
				case 20722:
				case 20770:
				case 20721:
				case 20672:
				case 20667:
				case 20668:
				case 20670:
				case 20671:
				case 20699:
					client.objectYOffset = 0;
					client.objectDistance = 3;
					break;

				case 410:
					if (client.playerMagicBook == 0) {
						client.setSidebarInterface(6, 29999);
						client.playerMagicBook = 2;
						client.autocasting = false;
						client.sendMessage("An ancient wisdomin fills your mind.");
						client.autocastId = -1;
						client.getPA().resetAutocast();
					} else {
						client.setSidebarInterface(6, 1151);
						client.playerMagicBook = 0;
						client.autocasting = false;
						client.sendMessage("You feel a drain on your memory.");
						client.autocastId = -1;
						client.getPA().resetAutocast();
					}
					break;

				case 1733:
					client.objectYOffset = 2;
					break;

				case 16671:
				case 16672:
				case 3044:
					client.objectDistance = 3;
					break;

				case 245:
					client.objectYOffset = -1;
					client.objectDistance = 0;
					break;

				case 272:
					client.objectYOffset = 1;
					client.objectDistance = 0;
					break;

				case 273:
					client.objectYOffset = 1;
					client.objectDistance = 0;
					break;

				case 246:
					client.objectYOffset = 1;
					client.objectDistance = 0;
					break;

				case 4493:
				case 4494:
				case 4495:
				case 4496:
					client.objectDistance = 5;
					break;

				case 6522:
				case 10229:
					client.objectDistance = 2;
					break;

				case 8959:
					client.objectYOffset = 1;
					break;

				case 4417:
					if (client.objectX == 2425 && client.objectY == 3074) {
						client.objectYOffset = 2;
					}
					break;

				case 4420:
					if (client.getX() >= 2383 && client.getX() <= 2385) {
						client.objectYOffset = 1;
					} else {
						client.objectYOffset = -2;
					}
				case 409:
				case 6552:
					client.objectDistance = 2;
					break;

				case 2878:
				case 2879:
					client.objectDistance = 3;
					break;

				case 2558:
					client.objectDistance = 0;
					if (client.absX > client.objectX && client.objectX == 3044) {
						client.objectXOffset = 1;
					}
					if (client.absY > client.objectY) {
						client.objectYOffset = 1;
					}
					if (client.absX < client.objectX && client.objectX == 3038) {
						client.objectXOffset = -1;
					}
					break;

				case 9356:
					client.objectDistance = 2;
					break;

				case 1815:
				case 1816:
				case 5959:
				case 5960:
					client.objectDistance = 0;
					break;

				case 9293:
					client.objectDistance = 2;
					break;

				case 4418:
					if (client.objectX == 2374 && client.objectY == 3131) {
						client.objectYOffset = -2;
					} else if (client.objectX == 2369 && client.objectY == 3126) {
						client.objectXOffset = 2;
					} else if (client.objectX == 2380 && client.objectY == 3127) {
						client.objectYOffset = 2;
					} else if (client.objectX == 2369 && client.objectY == 3126) {
						client.objectXOffset = 2;
					} else if (client.objectX == 2374 && client.objectY == 3131) {
						client.objectYOffset = -2;
					}
					break;

				case 9706:
					client.objectDistance = 0;
					client.objectXOffset = 1;
					break;

				case 9707:
					client.objectDistance = 0;
					client.objectYOffset = -1;
					break;

				case 4419:
				case 6707:
					client.objectYOffset = 3;
					break;

				case 6823:
					client.objectDistance = 2;
					client.objectYOffset = 1;
					break;

				case 6706:
					client.objectXOffset = 2;
					break;

				case 6772:
					client.objectDistance = 2;
					client.objectYOffset = 1;
					break;

				case 6705:
					client.objectYOffset = -1;
					break;

				case 6822:
					client.objectDistance = 2;
					client.objectYOffset = 1;
					break;

				case 6704:
					client.objectYOffset = -1;
					break;

				case 6773:
					client.objectDistance = 2;
					client.objectXOffset = 1;
					client.objectYOffset = 1;
					break;

				case 6703:
					client.objectXOffset = -1;
					break;

				case 6771:
					client.objectDistance = 2;
					client.objectXOffset = 1;
					client.objectYOffset = 1;
					break;

				case 6702:
					client.objectXOffset = -1;
					break;

				case 6821:
					client.objectDistance = 2;
					client.objectXOffset = 1;
					client.objectYOffset = 1;
					break;

				case 1276:
				case 1278:
				case 1281:
				case 1306:
				case 1307:
				case 1308:
				case 1309:
					client.objectDistance = 3;
					break;

				default:
					client.objectDistance = 1;
					break;
				}
				if (TreeData.treeExist(client.objectId)) {
					client.objectDistance = 5;
				}
				if (RockData.rockExist(client.objectId)) {
					client.objectDistance = 2;
				}
				if (AltarTable.isAltar(client.objectId)) {
					client.objectDistance = 5;
				}
				if (client.goodDistance(client.objectX + client.objectXOffset,
						client.objectY + client.objectYOffset, client.getX(),
						client.getY(), client.objectDistance)) {
					client.getActions().firstClickObject(client.objectId,
							client.objectX, client.objectY);
				} else {
					client.clickObjectType = 1;
				}
			}
			break;

		case 252:
			client.objectId = client.getInStream().readUnsignedWordBigEndianA();
			client.objectY = client.getInStream().readSignedWordBigEndian();
			client.objectX = client.getInStream().readUnsignedWordA();

			client.objectDistance = 1;
			if (client.getRank() >= 3) {
				Misc.println((new StringBuilder()).append("objectId: ")
						.append(client.objectId).append("  ObjectX: ")
						.append(client.objectX).append("  objectY: ")
						.append(client.objectY).append(" Xoff: ")
						.append(client.getX() - client.objectX)
						.append(" Yoff: ")
						.append(client.getY() - client.objectY).toString());
			}
			switch (client.objectId) {
			case 6162:
			case 6163:
			case 6164:
			case 6165:
			case 6166:
				client.objectDistance = 2;
				break;
			case 16671:
			case 16672:
				client.objectDistance = 3;
				break;
			default:
				client.objectDistance = 1;
				client.objectXOffset = 0;
				client.objectYOffset = 0;
				break;
			}
			if (client.goodDistance(client.objectX + client.objectXOffset,
					client.objectY + client.objectYOffset, client.getX(),
					client.getY(), client.objectDistance)) {
				client.getActions().secondClickObject(client.objectId,
						client.objectX, client.objectY);
			} else {
				client.clickObjectType = 2;
			}
			break;

		case 70: // 'F'
			client.objectX = client.getInStream().readSignedWordBigEndian();
			client.objectY = client.getInStream().readUnsignedWord();
			client.objectId = client.getInStream().readUnsignedWordBigEndianA();

			if (client.getRank() >= 3) {
				Misc.println((new StringBuilder()).append("objectId: ")
						.append(client.objectId).append("  ObjectX: ")
						.append(client.objectX).append("  objectY: ")
						.append(client.objectY).append(" Xoff: ")
						.append(client.getX() - client.objectX)
						.append(" Yoff: ")
						.append(client.getY() - client.objectY).toString());
			}
			switch (client.objectId) {
			case 16671:
			case 16672:
				client.objectDistance = 3;
				break;
			default:
				client.objectDistance = 1;
				break;
			}
			client.objectXOffset = 0;
			client.objectYOffset = 0;
			if (client.goodDistance(client.objectX + client.objectXOffset,
					client.objectY + client.objectYOffset, client.getX(),
					client.getY(), client.objectDistance)) {
				client.getActions().secondClickObject(client.objectId,
						client.objectX, client.objectY);
			} else {
				client.clickObjectType = 3;
			}
			break;
		}
	}

	public void handleSpecialCase(Client client, int i, int j, int k) {
	}
}
