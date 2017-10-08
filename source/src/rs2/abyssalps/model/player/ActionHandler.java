package rs2.abyssalps.model.player;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.content.doors.DoorsManager;
import rs2.abyssalps.content.minigame.fightpits.FightPitsMinigame;
import rs2.abyssalps.content.minigame.kraken.KrakenMinigame;
import rs2.abyssalps.content.minigame.pestcontrol.PestControl;
import rs2.abyssalps.content.minigame.warriorsguild.CyclopsMinigame;
import rs2.abyssalps.content.minigame.zulrah.ZulrahMinigame;
import rs2.abyssalps.content.skill.agility.AgilityAssistant;
import rs2.abyssalps.content.skill.fishing.Fishing;
import rs2.abyssalps.content.skill.magic.TeleportType;
import rs2.abyssalps.content.skill.mining.Mining;
import rs2.abyssalps.content.skill.mining.RockData;
import rs2.abyssalps.content.skill.runecrafting.AltarTable;
import rs2.abyssalps.content.skill.runecrafting.RiftTable;
import rs2.abyssalps.content.skill.runecrafting.Runecrafting;
import rs2.abyssalps.content.skill.slayer.Slayer;
import rs2.abyssalps.content.skill.slayer.SlayerConstants;
import rs2.abyssalps.content.skill.thieving.Thieving;
import rs2.abyssalps.content.skill.woodcutting.TreeData;
import rs2.abyssalps.content.skill.woodcutting.Woodcutting;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.objects.Object;
import rs2.util.Misc;
import rs2.util.ScriptManager;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class ActionHandler {

	private Client c;

	public ActionHandler(Client Client) {
		this.c = Client;
	}

	public void firstClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		if (TreeData.treeExist(objectType)) {
			Woodcutting.chop(c, objectType, obX, obY);
			return;
		}
		if (RockData.rockExist(objectType)) {
			Mining.mine(c, objectType);
			return;
		}
		if (RiftTable.isRift(objectType)) {
			Runecrafting.handleRifts(c, objectType);
			return;
		}
		if (AltarTable.isAltar(objectType)) {
			Runecrafting.craftRunes(c, objectType);
			return;
		}
		AgilityAssistant.handleAgility(c);
		if (c.getRank() >= 8) {
			c.sendMessage(objectType + " - " + obX + " - " + obY);
		}
		switch (objectType) {

		case 21731:
			if (c.getX() < obX) {
				Woodcutting.chopVine(c, -1, 2, 0);
			} else {
				Woodcutting.chopVine(c, -1, -2, 0);
			}
			break;

		case 21732:
			if (c.getY() < obY) {
				Woodcutting.chopVine(c, 4, 0, 2);
			} else {
				Woodcutting.chopVine(c, 4, 0, -2);
			}
			break;

		case 21734:
		case 21735:
			if (c.getX() < obX) {
				Woodcutting.chopVine(c, -1, 2, 0);
			} else {
				Woodcutting.chopVine(c, -1, -2, 0);
			}
			break;

		case 21738:
			c.getPA().movePlayer(2647, 9557, 0);
			break;

		case 21739:
			c.getPA().movePlayer(2649, 9562, 0);
			break;

		case 9:
			c.getCannon().pickupCannon(objectType, obX, obY);
			break;

		case 11846:
			FightPitsMinigame.enter(c);
			break;

		case 2623:
			if (c.getX() >= 2924) {
				c.getPA().movePlayer(c.getX() - 1, c.getY(), 0);
			} else {
				c.getPA().movePlayer(c.getX() + 1, c.getY(), 0);
			}
			break;

		case 24306:
		case 11834:
			c.exitMinigame();
			break;
		case 24309:
			if (c.heightLevel != 0) {
				if (c.getY() == 3541 || c.getY() == 3540) {
					if (c.getX() < obX) {
						if (c.enterMinigame(new CyclopsMinigame())) {
							c.getPA().movePlayer(c.getX() + 1, c.getY(),
									c.heightLevel);
						}
					} else {
						c.exitMinigame();
						c.getPA().movePlayer(c.getX() - 1, c.getY(),
								c.heightLevel);
					}
				}
			} else {
				if (c.getX() == 2855 || c.getX() == 2854) {
					if (c.getY() < obY) {
						c.getPA().movePlayer(c.getX(), c.getY() + 1,
								c.heightLevel);
					} else {
						c.getPA().movePlayer(c.getX(), c.getY() - 1,
								c.heightLevel);
					}
				}
			}
			break;
		case 16671:
		case 16672:
			c.getPA().movePlayer(c.getX(), c.getY(), c.heightLevel + 1);
			break;
		case 24303:
			c.getPA().movePlayer(c.getX(), c.getY(), c.heightLevel - 1);
			break;
		case 26711:
			c.enterMinigame(new KrakenMinigame());
			break;

		case 10068:
			c.enterMinigame(new ZulrahMinigame());
			break;

		case 26762:
			c.getPA().movePlayer(3233, 10332, 0);
			break;

		case 26763:
			c.getPA().movePlayer(3233, 3950, 0);
			break;

		case 678:
			c.getPA().movePlayer(2945, 4384, 2);
			break;

		case 679:
			c.getPA().movePlayer(3206, 3682, 0);
			break;

		case 677:
			if (c.getX() < obX)
				c.getPA().movePlayer(c.getX() + 4, c.getY(), 2);
			else
				c.getPA().movePlayer(c.getX() - 4, c.getY(), 2);

			break;

		case 23271:
			if (c.goodDistance(c.getX(), c.getY(), c.objectX, c.objectY, 2)) {
				c.turnPlayerTo(c.objectX, c.objectY);
				if (c.isBusy()) {
					return;
				}
				c.setBusyState(true);
				c.startAnimation(6132);
				c.isRunning = false;
				c.isRunning2 = false;
				c.getPA().sendFrame36(173, 0);
				if (c.getY() <= c.objectY) {
					c.setForceMovement(c.localX(), c.localY(), c.localX(),
							c.localY() + 3, 33, 60, 0);
				} else {
					c.setForceMovement(c.localX(), c.localY(), c.localX(),
							c.localY() - 3, 33, 60, 4);
				}
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

					@Override
					public void execute(CycleEventContainer container) {
						int newY = c.absY >= 3523 ? c.absY - 3 : c.absY + 3;
						c.getPA().movePlayer(c.absX, newY, 0);
						c.isRunning = true;
						c.isRunning2 = true;
						c.setBusyState(false);
						c.clearUpdateFlags();
						container.stop();
					}

				}, 3);
			}
			break;

		case 26258:
			c.getDH().sendOption3("Modern Spellbook", "Ancients Spellbook",
					"Lunar Spellbook");
			c.interfaceAction = 5;
			break;

		case 18987:
			c.getPA().movePlayer(3069, 10255, 0);
			break;

		case 1817:
			c.getPA().startTeleport(Config.START_LOCATION_X,
					Config.START_LOCATION_Y, 0, TeleportType.MODERN);
			break;

		case 7408:
		case 7407:
			if (c.getX() <= 3007) {
				c.getPA().movePlayer(c.getX() + 1, c.getY(), 0);
			} else {
				c.getPA().movePlayer(c.getX() - 1, c.getY(), 0);
			}
			break;

		case 26502:
			if (c.getY() < 5296) {
				if (!c.getItems().playerHasItem(11942, 1)) {
					c.getDH().sendDialogues(4, -1);
					return;
				}
				c.getItems().deleteItem(11942, c.getItems().getItemSlot(11942),
						1);
				c.getPA().movePlayer(c.getX(), c.getY() + 2, 2);
			} else {
				c.getPA().movePlayer(c.getX(), c.getY() - 2, 2);
			}
			break;

		case 26505:
			if (c.getY() >= 5332) {
				if (!c.getItems().playerHasItem(11942, 1)) {
					c.getDH().sendDialogues(4, -1);
					return;
				}
				c.getItems().deleteItem(11942, c.getItems().getItemSlot(11942),
						1);
				c.getPA().movePlayer(c.getX(), c.getY() - 2, 2);
			} else {
				c.getPA().movePlayer(c.getX(), c.getY() + 2, 2);
			}
			break;

		case 26504:
			if (c.getX() >= obX) {
				if (!c.getItems().playerHasItem(11942, 1)) {
					c.getDH().sendDialogues(4, -1);
					return;
				}
				c.getItems().deleteItem(11942, c.getItems().getItemSlot(11942),
						1);
				c.getPA().movePlayer(c.getX() - 2, c.getY(), 0);
			} else {
				c.getPA().movePlayer(c.getX() + 2, c.getY(), 0);
			}
			break;

		case 26503:
			if (c.getX() >= 2864) {
				c.getPA().movePlayer(c.getX() - 2, c.getY(), 2);
			} else {
				if (!c.getItems().playerHasItem(11942, 1)) {
					c.getDH().sendDialogues(4, -1);
					return;
				}
				c.getItems().deleteItem(11942, c.getItems().getItemSlot(11942),
						1);
				c.getPA().movePlayer(c.getX() + 2, c.getY(), 2);
			}
			break;

		case 26645:
			if (obX == 3350 && obY == 3162) {
				c.getPA().movePlayer(3328, 4751, 0);
			}
			break;

		case 26646:
			if (obX == 3326 && obY == 4749) {
				c.getPA().movePlayer(3352, 3164, 0);
			}
			break;

		case 26707:
		case 11744:
			c.getPA().openUpBank();
			break;

		case 2492:
			if (c.killCount >= 20) {
				c.getDH().sendOption4("Armadyl", "Bandos", "Saradomin",
						"Zamorak");
				c.dialogueAction = 20;
			} else {
				c.sendMessage("You need 20 kill count before teleporting to a boss chamber.");
			}
			break;

		case 1765:
			c.getPA().movePlayer(3067, 10256, 0);
			break;
		case 2882:
		case 2883:
			if (c.objectX == 3268) {
				if (c.absX < c.objectX) {
					c.getPA().walkTo(1, 0);
				} else {
					c.getPA().walkTo(-1, 0);
				}
			}
			break;
		case 272:
			c.getPA().movePlayer(c.absX, c.absY, 1);
			break;

		case 273:
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;
		case 245:
			c.getPA().movePlayer(c.absX, c.absY + 2, 2);
			break;
		case 246:
			c.getPA().movePlayer(c.absX, c.absY - 2, 1);
			break;
		case 1766:
			c.getPA().movePlayer(3016, 3849, 0);
			break;
		case 6552:
			if (c.playerMagicBook == 0) {
				c.playerMagicBook = 1;
				c.setSidebarInterface(6, 12855);
				c.sendMessage("An ancient wisdomin fills your mind.");
				c.getPA().resetAutocast();
			} else {
				c.setSidebarInterface(6, 1151); // modern
				c.playerMagicBook = 0;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
			}
			break;

		case 410:
			if (c.playerMagicBook == 0) {
				c.playerMagicBook = 2;
				c.setSidebarInterface(6, 29999);
				c.sendMessage("Lunar wisdom fills your mind.");
				c.getPA().resetAutocast();
			} else {
				c.setSidebarInterface(6, 1151); // modern
				c.playerMagicBook = 0;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
			}
			break;

		case 1816:
			c.getPA().startTeleport2(2271, 4680, 0);
			break;
		case 1814:
			// ardy lever
			c.getPA().startTeleport(3153, 3923, 0, TeleportType.MODERN);
			break;

		case 9356:
			// c.getPA().enterCaves();
			c.sendMessage("Temporarily removed due to bugs.");
			break;
		case 1733:
			c.getPA().movePlayer(c.absX, c.absY + 6393, 0);
			break;

		case 1734:
			c.getPA().movePlayer(c.absX, c.absY - 6396, 0);
			break;

		case 8959:
			if (c.getX() == 2490 && (c.getY() == 10146 || c.getY() == 10148)) {
				if (c.getPA().checkForPlayer(2490,
						c.getY() == 10146 ? 10148 : 10146)) {
					new Object(6951, c.objectX, c.objectY, c.heightLevel, 1,
							10, 8959, 15);
				}
			}
			break;

		case 2213:
		case 14367:
		case 11758:
		case 3193:
			c.getPA().openUpBank();
			break;

		case 10177:
			c.getPA().movePlayer(1890, 4407, 0);
			break;
		case 10230:
			c.getPA().movePlayer(2900, 4449, 0);
			break;
		case 10229:
			c.getPA().movePlayer(1912, 4367, 0);
			break;

		// pc boat
		case 14315:
		case 14314:
			PestControl.handleBoat(c);
			break;

		case 1596:
		case 1597:
			if (c.getY() >= c.objectY)
				c.getPA().walkTo(0, -1);
			else
				c.getPA().walkTo(0, 1);
			break;

		case 14235:
		case 14233:
			if (c.objectX == 2670)
				if (c.absX <= 2670)
					c.absX = 2671;
				else
					c.absX = 2670;
			if (c.objectX == 2643)
				if (c.absX >= 2643)
					c.absX = 2642;
				else
					c.absX = 2643;
			if (c.absX <= 2585)
				c.absY += 1;
			else
				c.absY -= 1;
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;

		case 14829:
		case 14830:
		case 14827:
		case 14828:
		case 14826:
		case 14831:
			// Server.objectHandler.startObelisk(objectType);
			Server.objectManager.startObelisk(objectType);
			break;

		case 9369:
			if (c.getY() > 5175)
				c.getPA().movePlayer(2399, 5175, 0);
			else
				c.getPA().movePlayer(2399, 5177, 0);
			break;

		// coffins
		case 6702:
		case 6703:
		case 6704:
		case 6705:
		case 6706:
		case 6707:
		case 20672:
		case 20667:
		case 20668:
		case 20670:
		case 20671:
		case 20669:
			c.getBarrows().useStairs();
			break;

		case 10284:
			c.getBarrows().useChest();
			break;

		case 20772:
			if (c.barrowsNpcs[0][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1677, c.getX(), c.getY() - 1, 3,
						0, 120, 25, 200, 200, true, true);
				c.barrowsNpcs[0][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20721:
			if (c.barrowsNpcs[1][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1676, c.getX() + 1, c.getY(), 3,
						0, 120, 20, 200, 200, true, true);
				c.barrowsNpcs[1][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20771:
			if (c.barrowsNpcs[2][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1675, c.getX(), c.getY() - 1, 3,
						0, 90, 17, 200, 200, true, true);
				c.barrowsNpcs[2][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20722:
			if (c.barrowsNpcs[3][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1674, c.getX(), c.getY() - 1, 3,
						0, 120, 23, 200, 200, true, true);
				c.barrowsNpcs[3][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20770:
			c.getDH().sendDialogues(2900, 2026);
			break;

		case 20720:
			if (c.barrowsNpcs[5][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1673, c.getX() - 1, c.getY(), 3,
						0, 90, 19, 200, 200, true, true);
				c.barrowsNpcs[5][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		// DOORS
		case 1516:
		case 1519:
			if (c.objectY == 9698) {
				if (c.absY >= c.objectY)
					c.getPA().walkTo(0, -1);
				else
					c.getPA().walkTo(0, 1);
				break;
			}
		case 1530:
		case 1531:
		case 1533:
		case 1534:
		case 11712:
		case 11711:
		case 11707:
		case 11708:
		case 6725:
		case 3198:
		case 3197:
			Server.objectHandler.doorHandling(objectType, c.objectX, c.objectY,
					0);
			break;

		case 9319:
			if (c.heightLevel == 0)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			else if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 2);
			break;

		case 9320:
			if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 0);
			else if (c.heightLevel == 2)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			break;

		case 4496:
		case 4494:
			if (c.heightLevel == 2) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 0);
			}
			break;

		case 4493:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 4495:
			if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 5126:
			if (c.absY == 3554)
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;

		case 1755:
			if (c.objectX == 2884 && c.objectY == 9797)
				c.getPA().movePlayer(c.absX, c.absY - 6400, 0);
			break;
		case 1759:
			if (c.objectX == 2884 && c.objectY == 3397)
				c.getPA().movePlayer(c.absX, c.absY + 6400, 0);
			break;
		/*
		 * case 3203: //dueling forfeit if (c.duelCount > 0) {
		 * c.sendMessage("You may not forfeit yet."); break; } Client o =
		 * (Client) Server.playerHandler.players[c.duelingWith]; if(o == null) {
		 * c.getTradeAndDuel().resetDuel();
		 * c.getPA().movePlayer(Config.DUELING_RESPAWN_X
		 * +(Misc.random(Config.RANDOM_DUELING_RESPAWN)),
		 * Config.DUELING_RESPAWN_Y
		 * +(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0); break; }
		 * if(c.duelRule[0]) {
		 * c.sendMessage("Forfeiting the duel has been disabled!"); break; }
		 * if(o != null) {
		 * o.getPA().movePlayer(Config.DUELING_RESPAWN_X+(Misc.random
		 * (Config.RANDOM_DUELING_RESPAWN)),
		 * Config.DUELING_RESPAWN_Y+(Misc.random
		 * (Config.RANDOM_DUELING_RESPAWN)), 0);
		 * c.getPA().movePlayer(Config.DUELING_RESPAWN_X
		 * +(Misc.random(Config.RANDOM_DUELING_RESPAWN)),
		 * Config.DUELING_RESPAWN_Y
		 * +(Misc.random(Config.RANDOM_DUELING_RESPAWN)), 0); o.duelStatus = 6;
		 * o.getTradeAndDuel().duelVictory(); c.getTradeAndDuel().resetDuel();
		 * c.getTradeAndDuel().resetDuelItems();
		 * o.sendMessage("The other player has forfeited the duel!");
		 * c.sendMessage("You forfeit the duel!"); break; }
		 * 
		 * break;
		 */

		case 409:
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
			}
			break;

		case 2879:
			c.getPA().movePlayer(2538, 4716, 0);
			break;
		case 2878:
			c.getPA().movePlayer(2509, 4689, 0);
			break;
		case 5960:
			c.getPA().startTeleport2(3090, 3956, 0);
			break;

		case 1815:
			c.getPA().startTeleport2(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0);
			break;

		case 9706:
			c.getPA().startTeleport2(3105, 3951, 0);
			break;
		case 9707:
			c.getPA().startTeleport2(3105, 3956, 0);
			break;

		case 5959:
			c.getPA().startTeleport2(2539, 4712, 0);
			break;

		case 2558:
			c.sendMessage("This door is locked.");
			break;

		case 9294:
			if (c.absX < c.objectX) {
				c.getPA().movePlayer(c.objectX + 1, c.absY, 0);
			} else if (c.absX > c.objectX) {
				c.getPA().movePlayer(c.objectX - 1, c.absY, 0);
			}
			break;

		case 9293:
			if (c.absX < c.objectX) {
				c.getPA().movePlayer(2892, 9799, 0);
			} else {
				c.getPA().movePlayer(2886, 9799, 0);
			}
			break;
		case 10529:
		case 10527:
			if (c.absY <= c.objectY)
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;

		case 733:
			if (obX == 3092 && obY == 3957) {
				if (c.getX() <= 3092) {
					c.getPA().movePlayer(c.getX() + 1, c.getY(), 0);
				} else {
					c.getPA().movePlayer(c.getX() - 1, c.getY(), 0);
				}
			} else if (obX == 3095 && obY == 3957) {
				if (c.getX() <= 3094) {
					c.getPA().movePlayer(c.getX() + 1, c.getY(), 0);
				} else {
					c.getPA().movePlayer(c.getX() - 1, c.getY(), 0);
				}
			} else if (obX == 3105 && obY == 3958) {
				if (c.getY() >= obY) {
					c.getPA().movePlayer(c.getX(), c.getY() - 1, 0);
				} else {
					c.getPA().movePlayer(c.getX(), c.getY() + 1, 0);
				}
			}
			break;

		default:
			ScriptManager.callFunc("objectClick1_" + objectType, c, objectType,
					obX, obY);
			break;

		}
	}

	public void secondClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		if (c.getRank() >= 8) {
			c.sendMessage("Object type: " + objectType);
		}
		switch (objectType) {

		case 11744:
			c.getPA().openUpBank();
			break;

		case 26645:
			if (obX == 3350 && obY == 3162) {
				c.getPA().movePlayer(3328, 4751, 0);
			}
			break;

		case 26646:
			if (obX == 3326 && obY == 4749) {
				c.getPA().movePlayer(3352, 3164, 0);
			}
			break;
		case 16672:
			c.getPA().movePlayer(c.getX(), c.getY(), c.heightLevel - 1);
			break;
		case 6162:
		case 6163:
		case 6164:
		case 6165:
		case 6166:
			Thieving.useStall(c, new int[] { objectType, obX, obY });
			break;

		case 2213:
		case 14367:
		case 11758:
			c.getPA().openUpBank();
			break;

		case 2558:
			if (System.currentTimeMillis() - c.lastLockPick < 3000
					|| c.freezeTimer > 0)
				break;
			if (c.getItems().playerHasItem(1523, 1)) {
				c.lastLockPick = System.currentTimeMillis();
				if (Misc.random(10) <= 3) {
					c.sendMessage("You fail to pick the lock.");
					break;
				}
				if (c.objectX == 3044 && c.objectY == 3956) {
					if (c.absX == 3045) {
						c.getPA().walkTo2(-1, 0);
					} else if (c.absX == 3044) {
						c.getPA().walkTo2(1, 0);
					}

				} else if (c.objectX == 3038 && c.objectY == 3956) {
					if (c.absX == 3037) {
						c.getPA().walkTo2(1, 0);
					} else if (c.absX == 3038) {
						c.getPA().walkTo2(-1, 0);
					}
				} else if (c.objectX == 3041 && c.objectY == 3959) {
					if (c.absY == 3960) {
						c.getPA().walkTo2(0, -1);
					} else if (c.absY == 3959) {
						c.getPA().walkTo2(0, 1);
					}
				}
			} else {
				c.sendMessage("I need a lockpick to pick this lock.");
			}
			break;
		default:
			ScriptManager.callFunc("objectClick2_" + objectType, c, objectType,
					obX, obY);
			break;
		}
	}

	public void thirdClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		c.sendMessage("Object type: " + objectType);
		switch (objectType) {
		default:
			ScriptManager.callFunc("objectClick3_" + objectType, c, objectType,
					obX, obY);
			break;
		}
	}

	public void firstClickNpc(int i) {

		if (Fishing.fishExist(i)) {
			Fishing.startFishing(c, i);
		}

		c.clickNpcType = 0;
		c.npcClickIndex = 0;

		switch (i) {

		case 2033:
			c.getShops().setupZulrahExchange();
			break;

		case 3193:
			c.getShops().openShop(15);
			break;

		case 6481:
			c.getDH().sendDialogues(29, -1);
			break;

		case 3077:
			c.getDH().sendDialogues(27, i);
			break;

		case 6649:
			c.getShops().openShop(13);
			break;

		case 402:
			c.getDH().sendDialogues(18, i);
			break;

		case 1160:
			c.getDH().sendDialogues(10, i);
			break;

		case 2580:
			c.getPA().startTeleport(3039, 4834, 0, TeleportType.MODERN);
			break;

		case 5792:
			c.getDH().sendDialogues(9, i);
			break;

		case 394:
			c.getDH().sendDialogues(7, i);
			break;

		case 506:
			c.getShops().openSkillCape();
			break;

		case 2182:
			c.getPA().openUpBank();
			break;

		case 1011:
			c.getShops().openShop(11);
			break;

		case 1158:
			c.getShops().openShop(8);
			break;

		case 534:
			c.getShops().openShop(7);
			break;

		case 505:
			c.getShops().openShop(6);
			break;

		case 1024:
			c.getShops().openShop(1);
			break;

		case 5721:
			c.getDH().sendDialogues(3, i);
			break;

		case 1833:
			c.getShops().openShop(3);
			break;

		case 6059:
			c.getShops().openShop(4);
			break;

		case 637:
			c.getShops().openShop(5);
			break;

		case 706:
			c.getDH().sendDialogues(9, i);
			break;

		case 2258:
			c.getDH().sendDialogues(17, i);
			break;

		case 1304:
			c.getDH().sendOption5("Home", "Edgeville", "Island",
					"Dagannoth Kings", "Next Page");
			c.teleAction = 1;
			break;

		case 528:
			c.getShops().openShop(1);
			break;

		case 461:
			c.getShops().openShop(2);
			break;

		case 683:
			c.getShops().openShop(3);
			break;

		case 586:
			c.getShops().openShop(4);
			break;

		case 555:
			c.getShops().openShop(6);
			break;

		case 519:
			c.getShops().openShop(8);
			break;

		case 1700:
			c.getShops().openShop(19);
			break;

		case 251:
			c.getShops().openShop(60);
			break;

		case 1282:
			c.getShops().openShop(7);
			break;

		case 1152:
			c.getDH().sendDialogues(16, i);
			break;

		case 494:
			c.getPA().openUpBank();
			break;

		case 460:
			c.getDH().sendDialogues(3, i);
			break;

		case 462:
			c.getDH().sendDialogues(7, i);
			break;

		case 1307:
			c.getPA().showInterface(3559);
			c.canChangeAppearance = true;
			break;

		case 904:
			c.sendMessage((new StringBuilder()).append("You have ")
					.append(c.magePoints).append(" points.").toString());
			break;
		}
	}

	public void secondClickNpc(int i) {
		if (c.getPets().pickupPet(NPCHandler.npcs[c.npcClickIndex])) {
			c.clickNpcType = 0;
			c.npcClickIndex = 0;
			return;
		}
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		switch (i) {

		case 402:
			c.getDH().sendDialogues(19, i);
			break;

		case 3077:
			c.getDH().sendDialogues(28, i);
			break;

		case 1117:
			c.getPA().showInterface(3559);
			c.canChangeAppearance = true;
			break;

		case 1282:
			c.getShops().openShop(7);
			break;

		case 494:
			c.getPA().openUpBank();
			break;

		case 904:
			c.getShops().openShop(17);
			break;

		case 522:
		case 523:
			c.getShops().openShop(1);
			break;

		case 541:
			c.getShops().openShop(5);
			break;

		case 461:
			c.getShops().openShop(2);
			break;

		case 683:
			c.getShops().openShop(3);
			break;

		case 549:
			c.getShops().openShop(4);
			break;

		case 2538:
			c.getShops().openShop(6);
			break;

		case 519:
			c.getShops().openShop(8);
			break;

		case 3789:
			c.getShops().openShop(18);
			break;
		}
	}

	public void thirdClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		switch (npcType) {

		case 402:
			c.getShops().openShop(12);
			break;

		case 5721:
			c.getShops().openShop(2);
			break;

		default:
			ScriptManager.callFunc("npcClick3_" + npcType, c, npcType);
			if (c.getRank() == 3)
				Misc.println("Third Click NPC : " + npcType);
			break;

		}
	}

}