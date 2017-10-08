package rs2.abyssalps.model.npcs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.content.minigame.fightcave.FightCavesDefinitions;
import rs2.abyssalps.content.minigame.kraken.KrakenConstants;
import rs2.abyssalps.content.minigame.warriorsguild.MagicalAnimator;
import rs2.abyssalps.content.minigame.zulrah.ZulrahConstants;
import rs2.abyssalps.content.skill.prayer.BoneCrusher;
import rs2.abyssalps.content.skill.prayer.BoneData;
import rs2.abyssalps.content.skill.slayer.SlayerConstants;
import rs2.abyssalps.content.skill.slayer.SlayerTable;
import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.items.defs.WeaponAnimations;
import rs2.abyssalps.model.npcs.combat.Damage;
import rs2.abyssalps.model.npcs.combat.SpecialNPCHandler;
import rs2.abyssalps.model.npcs.combat.impl.CorporealBeast;
import rs2.abyssalps.model.npcs.combat.impl.King_Black_Dragon;
import rs2.abyssalps.model.npcs.combat.impl.Venenatis;
import rs2.abyssalps.model.npcs.combat.impl.Vetion;
import rs2.abyssalps.model.npcs.drop.Drops;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.Player;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.Misc;
import rs2.util.cache.region.Region;

public class NPCHandler {

	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];
	public static NPCList NpcList[] = new NPCList[maxListedNPCs];

	public int getNpcIndex(NPC n) {
		for (int i = 0; i < npcs.length; i++) {
			if (npcs[i] == n)
				return i;
		}
		return -1;

	}

	public NPCHandler() {
		for (int i = 0; i < maxNPCs; i++) {
			npcs[i] = null;
		}
		for (int i = 0; i < maxListedNPCs; i++) {
			NpcList[i] = null;
		}
		loadNPCList("./Data/CFG/npc.cfg");
		loadAutoSpawn("./Data/CFG/spawn-config.cfg");
	}

	public void multiAttackGfx(int i, int gfx) {
		if (npcs[i].projectileId < 0)
			return;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.heightLevel != npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY,
						npcs[i].absX, npcs[i].absY, 15)) {
					int nX = NPCHandler.npcs[i].getX() + offset(i);
					int nY = NPCHandler.npcs[i].getY() + offset(i);
					int pX = c.getX();
					int pY = c.getY();
					int offX = (nY - pY) * -1;
					int offY = (nX - pX) * -1;
					c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50,
							getProjectileSpeed(i), npcs[i].projectileId, 43,
							31, -c.getId() - 1, 65);
				}
			}
		}
	}

	public static NPC[] getNpcsById(int npcType) {
		List<NPC> npcList = new ArrayList<>();
		for (NPC npc : npcs) {
			if (npc == null) {
				continue;
			}
			if (npc.npcType != npcType) {
				continue;
			}
			npcList.add(npc);
		}
		return npcList.toArray(new NPC[npcList.size()]);
	}

	public boolean switchesAttackers(int i) {
		switch (npcs[i].npcType) {
		case 3164:
		case 3163:
		case 3165:
			return true;
		}

		return false;
	}

	public void multiAttackDamage(int i) {
		int max = getMaxHit(i);
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Client c = (Client) PlayerHandler.players[j];
				if (c.isDead || c.heightLevel != npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY,
						npcs[i].absX, npcs[i].absY, 15)) {
					if (npcs[i].attackType == 2) {
						if (!c.prayerActive[16]) {
							if (Misc.random(500) + 200 > Misc.random(c
									.getCombat().mageDef())) {
								int dam = Misc.random(max);
								c.dealDamage(dam);
								c.handleHitMask(dam);
							} else {
								c.dealDamage(0);
								c.handleHitMask(0);
							}
						} else {
							c.dealDamage(0);
							c.handleHitMask(0);
						}
					} else if (npcs[i].attackType == 1) {
						if (!c.prayerActive[17]) {
							int dam = Misc.random(max);
							if (Misc.random(500) + 200 > Misc.random(c
									.getCombat().calculateRangeDefence())) {
								c.dealDamage(dam);
								c.handleHitMask(dam);
							} else {
								c.dealDamage(0);
								c.handleHitMask(0);
							}
						} else {
							c.dealDamage(0);
							c.handleHitMask(0);
						}
					}
					if (npcs[i].endGfx > 0) {
						c.gfx0(npcs[i].endGfx);
					}
				}
				c.getPA().refreshSkill(3);
			}
		}
	}

	public int getClosePlayer(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (j == npcs[i].spawnedBy)
					return j;
				if (goodDistance(PlayerHandler.players[j].absX,
						PlayerHandler.players[j].absY, npcs[i].absX,
						npcs[i].absY, 2 + distanceRequired(i)
								+ followDistance(i))
						|| isFightCaveNpc(i)) {
					System.out.println("lol");
					if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
							|| PlayerHandler.players[j].inMulti()) {

						System.out.println("lol1");
						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							return j;
					}
				}
			}
		}
		return 0;
	}

	public int getCloseRandomPlayer(int i) {
		ArrayList<Integer> players = new ArrayList<Integer>();
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (npcs[i].distanceTo(PlayerHandler.players[j]) <= (distanceRequired(i) + followDistance(i))
						|| isFightCaveNpc(i)) {
					if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
							|| PlayerHandler.players[j].inMulti()
							|| npcs[i].npcType == 5535
							|| npcs[i].npcType == 494)

						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							players.add(j);
				}
			}
		}
		if (players.size() > 0)
			return players.get(Misc.random(players.size() - 1));
		else
			return 0;
	}

	public int npcSize(int i) {
		switch (npcs[i].npcType) {
		case 2883:
		case 2882:
		case 2881:
			return 3;
		}
		return 0;
	}

	public boolean canWalk(int i) {
		switch (npcs[i].npcType) {
		case ZulrahConstants.BLUE_ZULRAH_ID:
		case ZulrahConstants.GREEN_ZULRAH_ID:
		case ZulrahConstants.RED_ZULRAH_ID:
			return false;
		}
		return true;
	}

	public boolean isAggressive(int i) {
		switch (npcs[i].npcType) {
		case 2501:
		case 2502:
		case 2503:
		case 2504:
		case 2505:
		case 2506:
		case 2507:
		case 2508:
		case 2509:
		case 2206:
		case 2215:
		case 2216:
		case 2217:
		case 2218:
		case 2265:
		case 2266:
		case 2267:
		case 5961:
		case 5947:
		case 5535:
		case 494:
		case 3164:
		case 3163:
		case 3165:
		case 3162:
		case 2205:
		case 6615:
		case 6616:
		case 2207:
		case 6766:
		case ZulrahConstants.BLUE_ZULRAH_ID:
		case ZulrahConstants.GREEN_ZULRAH_ID:
		case ZulrahConstants.RED_ZULRAH_ID:
		case ZulrahConstants.SNAKELING_ID:
		case CorporealBeast.CORPOREAL_BEAST_ID:
		case CorporealBeast.DARK_CORE_ID:
		case FightCavesDefinitions.TZ_TOK_JAD_ID:
		case FightCavesDefinitions.KET_ZEK_ID:
		case FightCavesDefinitions.YT_MEJ_KOT_ID:
		case FightCavesDefinitions.TOK_XIL_ID:
		case FightCavesDefinitions.TZ_KEK_ID:
		case FightCavesDefinitions.TZ_KIH_ID:
			return true;
		}
		if (npcs[i].inWild() && npcs[i].MaxHP > 0)
			return true;
		return false;
	}

	private boolean isKrakenMobs(int i) {
		switch (npcs[i].npcType) {
		case KrakenConstants.KRAKEN_POOL_ID:
		case KrakenConstants.TENTACLE_POOL_ID:
			return true;
		}
		return false;
	}

	public boolean isFightCaveNpc(int i) {
		switch (npcs[i].npcType) {
		case 2627:
		case 2630:
		case 2631:
		case 2741:
		case 2743:
		case 2745:
			return true;
		}
		return false;
	}

	/**
	 * Summon npc, barrows, etc
	 **/
	public NPC spawnNpc(Client c, int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence,
			boolean attackPlayer, boolean headIcon) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {

				newNPC.killerId = c.playerId;
			}
		}
		npcs[slot] = newNPC;
		return newNPC;
	}

	public void spawnNpc2(int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
	}

	/**
	 * Emotes
	 **/

	public static int getAttackAnimation(int i) {
		switch (NPCHandler.npcs[i].npcType) {

		case 112:
			return 6562;

		case FightCavesDefinitions.TZ_TOK_JAD_ID:
			return 2655;
		case FightCavesDefinitions.KET_ZEK_ID:
			return 2647;
		case FightCavesDefinitions.YT_MEJ_KOT_ID:
			return 2637;
		case FightCavesDefinitions.TOK_XIL_ID:
			return 2633;
		case FightCavesDefinitions.TZ_KEK_ID:
			return 2625;
		case FightCavesDefinitions.TZ_KIH_ID:
			return 2621;
		case 2827:
			return 4915;

		case 2527:
			return 5540;

		case ZulrahConstants.SNAKELING_ID:
			return -1;
		case 2206:
			return 6376;

		case 498:
			return -1;

		case 2167:
			return 2609;

		case 70:
			return 5485;

		case 1673: // dharok
			return 2067;

		case 1674: // guthan
			return 2080;

		case 1676: // torag
			return 0x814;

		case 1677: // verac
			return 2062;

		case 419:
			return 1562;

		case 414:
			return 1523;

		case 448:
			return 1592;

		case 3165:
			return 6957;

		case 2090:
		case 2098:
		case 2463:
		case 2464:
		case 2465:
		case 2466:
		case 2467:
		case 2468:
			return 4658;

		case 2250:
			return 1341;

		case 253:
			return 451;

		case 1213:
			return 407;

		case 2267:
			return 2851;

		case 6613:
			return 6581;

		case 2216:
			return 6154;

		case 100:
			return 1312;

		case 2550:
			if (npcs[i].attackType == 0)
				return 7060;
			else
				return 7063;
		case 2892:
		case 2894:
			return 2868;
		case 2627:
			return 2621;
		case 2630:
			return 2625;
		case 2631:
			return 2633;
		case 2741:
			return 2637;
		case 2746:
			return 2637;
		case 2607:
			return 2611;
		case 2743:// 360
			return 2647;
			// bandos gwd
		case 2551:
		case 2552:
		case 2553:
			return 6154;
			// end of gwd
			// arma gwd
		case 2558:
			return 3505;
		case 2560:
			return 6953;
		case 2559:
			return 6952;
		case 2561:
			return 6954;
			// end of arma gwd
			// sara gwd
		case 2562:
			return 6964;
		case 2563:
			return 6376;
		case 2564:
			return 7018;
		case 2565:
			return 7009;
			// end of sara gwd
		case 13: // wizards
			return 711;

		case 103:
		case 655:
			return 123;

		case 1624:
			return 1557;

		case 1648:
			return 1590;

		case 2783: // dark beast
			return 2733;

		case 1615: // abby demon
			return 1537;

		case 1613: // nech
			return 1528;

		case 1610:
		case 1611: // garg
			return 1519;

		case 1616: // basilisk
			return 1546;

		case 90: // skele
			return 260;

		case 50:// drags
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
			return 80;

		case 124: // earth warrior
			return 390;

		case 803: // monk
			return 422;

		case 52: // baby drag
			return 25;

		case 58: // Shadow Spider
		case 59: // Giant Spider
		case 60: // Giant Spider
		case 61: // Spider
		case 62: // Jungle Spider
		case 63: // Deadly Red Spider
		case 64: // Ice Spider
		case 134:
			return 143;

		case 105: // Bear
		case 106: // Bear
			return 41;

		case 412:
		case 78:
			return 30;

		case 2033: // rat
			return 138;

		case 2031: // bloodworm
			return 2070;

		case 101: // goblin
			return 309;

		case 81: // cow
			return 0x03B;

		case 21: // hero
			return 451;

		case 41: // chicken
			return 55;

		case 9: // guard
		case 32: // guard
		case 20: // paladin
			return 451;

		case 1338: // dagannoth
		case 1340:
		case 1342:
			return 1341;

		case 19: // white knight
			return 406;

		case 110:
		case 111: // ice giant
		case 117:
			return 128;

		case 2452:
			return 1312;

		case 2889:
			return 2859;

		case 118:
		case 119:
			return 99;

		case 82:// Lesser Demon
		case 83:// Greater Demon
		case 84:// Black Demon
		case 1472:// jungle demon
			return 64;

		case 1267:
		case 1265:
			return 1312;

		case 125: // ice warrior
		case 178:
			return 451;

		case 1153: // Kalphite Worker
		case 1154: // Kalphite Soldier
		case 1155: // Kalphite guardian
		case 1156: // Kalphite worker
		case 1157: // Kalphite guardian
			return 1184;

		case 123:
		case 122:
			return 164;

		case 2028: // karil
			return 2075;

		case 2025: // ahrim
			return 729;

		case 2026: // dharok
			return 2067;

		case 2027: // guthan
			return 2080;

		case 2029: // torag
			return 0x814;

		case 2030: // verac
			return 2062;

		case 2881: // supreme
			return 2855;

		case 2882: // prime
			return 2854;

		case 2883: // rex
			return 2851;

		case 3200:
			return 3146;

		case 2745:
			if (npcs[i].attackType == 2)
				return 2656;
			else if (npcs[i].attackType == 1)
				return 2652;
			else if (npcs[i].attackType == 0)
				return 2655;

		case 443:
			return 414;

		case 426:
			return 1595;

		case 423:
			return 1557;

		case 11:
			return 1528;

		case 415:
			return 1537;

		case 4005:
			return 2731;

		case 2480:
			return 6254;

		case 291:
			return 99;

		case 2005:
		case 5874:
			return 64;

		case 3133:
			return 6579;

		case 2075:
			return 4666;

		default:
			return 0x326;
		}
	}

	public int getDeathAnimation(int i) {
		switch (npcs[i].npcType) {

		case 2075:
			return 4668;

		case FightCavesDefinitions.TZ_TOK_JAD_ID:
			return 2654;
		case FightCavesDefinitions.KET_ZEK_ID:
			return 2646;
		case FightCavesDefinitions.YT_MEJ_KOT_ID:
			return 2638;
		case FightCavesDefinitions.TOK_XIL_ID:
			return 2630;
		case FightCavesDefinitions.TZ_KEK_ID:
			return 2627;
		case FightCavesDefinitions.TZ_KIH_ID:
			return 2620;
		case 3133:
			return 6576;

		case 2005:
		case 5874:
			return 67;

		case 291:
			return 102;

		case 2480:
			return 6256;

		case 2827:
			return 4917;

		case 2527:
			return 5542;

		case 6504:
			return Venenatis.DEAD_ANIMATION;

		case 112:
			return 6564;

		case 2054:
			return 3147;

		case ZulrahConstants.BLUE_ZULRAH_ID:
		case ZulrahConstants.GREEN_ZULRAH_ID:
		case ZulrahConstants.RED_ZULRAH_ID:
			return ZulrahConstants.DEATH_ANIMATION;
		case ZulrahConstants.SNAKELING_ID:
			return -1;
		case 6766:
			return 7196;

		case 6609:
			return 4929;

		case 2207:
			return 7034;

		case 2206:
			return 6377;

		case 498:
			return -1;

		case 2167:
			return -1;

		case 70:
			return 5491;

		case 6616:
			return 6260;

		case 6615:
			return 6256;

		case 1612:
			return 196;

		case 4005:
			return 2733;

		case 415:
			return 1538;

		case 11:
			return 1530;

		case 423:
			return 1558;

		case 426:
			return 1597;

		case 419:
			return 1563;

		case 414:
			return 1524;

		case 448:
			return 1590;

		case 2205:
			return 6968;

		case 3162:
			return 6979;

		case 3163:
		case 3164:
		case 3165:
			return 6959;

		case 2090:
			return 4658;

		case 2098:
		case 2463:
		case 2464:
		case 2465:
		case 2466:
		case 2467:
		case 2468:
			return 4653;

		case 494:
			return 3993;

		case 5535:
			return 3620;

		case 2250:
			return 1342;

		case 2265:
		case 2266:
		case 2267:
			return 2856;

		case 6613:
			return 6576;

		case 6611:
		case 6612:
			return 5509;

		case 2215:
			return 7020;

		case 2217:
		case 2216:
		case 2218:
			return 6156;

		case 319:
			return 1676;

		case 239:
			return 92;

		case 5862:
			return 4495;

		case 100:
			return 1314;

		case 260:
		case 265:
		case 259:
		case 270:
			return 92;

			// sara gwd
		case 2562:
			return 6965;
		case 2563:
			return 6377;
		case 2564:
			return 7016;
		case 2565:
			return 7011;
			// bandos gwd
		case 2551:
		case 2552:
		case 2553:
			return 6156;
		case 2550:
			return 7062;
		case 2892:
		case 2894:
			return 2865;
		case 2558:
			return 3503;
		case 2559:
		case 2560:
		case 2561:
			return 6956;
		case 2607:
			return 2607;
		case 2627:
			return 2620;
		case 2630:
			return 2627;
		case 2631:
			return 2630;
		case 2738:
			return 2627;
		case 2741:
			return 2638;
		case 2746:
			return 2638;
		case 2743:
			return 2646;
		case 2745:
			return 2654;

		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return -1;

		case 3200:
			return 3147;

		case 2035: // spider
			return 146;

		case 2033: // rat
			return 141;

		case 2031: // bloodvel
			return 2073;

		case 101: // goblin
			return 313;

		case 81: // cow
			return 0x03E;

		case 41: // chicken
			return 57;

		case 1338: // dagannoth
		case 1340:
		case 1342:
			return 1342;

		case 2881:
		case 2882:
		case 2883:
			return 2856;

		case 111: // ice giant
			return 131;

		case 125: // ice warrior
			return 843;

		case 751:// Zombies!!
			return 302;

		case 1626:
		case 1627:
		case 1628:
		case 1629:
		case 1630:
		case 1631:
		case 1632: // turoth!
			return 1597;

		case 1616: // basilisk
			return 1548;

		case 1653: // hand
			return 1590;

		case 82:// demons
		case 83:
		case 84:
			return 67;

		case 1605:// abby spec
			return 1508;

		case 51:// baby drags
		case 52:
		case 1589:
		case 3376:
			return 28;

		case 1610:
		case 1611:
			return 1518;

		case 1618:
		case 1619:
			return 1553;

		case 1620:
		case 1621:
			return 1563;

		case 2783:
			return 2732;

		case 1615:
			return 1538;

		case 1624:
			return 1558;

		case 1613:
			return 1530;

		case 1633:
		case 1634:
		case 1635:
		case 1636:
			return 1580;

		case 1648:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1654:
		case 1655:
		case 1656:
		case 1657:
			return 1590;

		case 105:
		case 106:
			return 44;

		case 412:
		case 78:
			return 36;

		case 122:
		case 123:
			return 167;

		case 58:
		case 59:
		case 60:
		case 61:
		case 62:
		case 63:
		case 64:
		case 134:
			return 146;

		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
			return 1190;

		case 103:
		case 104:
			return 123;

		case 118:
		case 119:
			return 102;

		case 50:// drags
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
			return 92;

		default:
			return 2304;
		}
	}

	/**
	 * Attack delays
	 **/
	public int getNpcDelay(int i) {
		switch (npcs[i].npcType) {
		case ZulrahConstants.GREEN_ZULRAH_ID:
		case ZulrahConstants.BLUE_ZULRAH_ID:
			return 4;
		case 2215:
		case 6611:
		case 6612:
		case 2205:
		case 3162:
			return 7;

		case 3127:
			return 8;

		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2550:
			return 6;
			// saradomin gw boss
		case 2562:
			return 2;

		case 6619:
			return 3;

		default:
			return 5;
		}
	}

	/**
	 * Hit delays
	 **/
	public int getHitDelay(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 3200:
		case 2892:
		case 2894:
			return 3;

		case 2743:
		case 2631:
		case 2558:
		case 2559:
		case 2560:
			return 3;

		case 2745:
			if (npcs[i].attackType == 1 || npcs[i].attackType == 2)
				return 5;
			else
				return 2;

		case 2025:
			return 4;
		case 2028:
			return 3;

		default:
			return 2;
		}
	}

	/**
	 * Npc respawn time
	 **/
	public int getRespawnTime(int i) {
		switch (npcs[i].npcType) {

		case 2215:
		case 3162:
			return 300;

		case 6619:
		case 2218:
		case 2217:
		case 2216:
		case 3164:
		case 3165:
		case 3163:
			return 100;

		default:
			return 25;

		}
	}

	public int getFreeSlot() {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		return slot;
	}

	public NPC newNPC(int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence) {
		int slot = getFreeSlot();
		if (slot == -1)
			return null; // no free slot found

		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
		return newNPC;
	}

	public void newNPCList(int npcType, String npcName, int combat, int HP) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 0; i < maxListedNPCs; i++) {
			if (NpcList[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1)
			return; // no free slot found

		NPCList newNPCList = new NPCList(npcType);
		newNPCList.npcName = npcName;
		newNPCList.npcCombat = combat;
		newNPCList.npcHealth = HP;
		NpcList[slot] = newNPCList;
	}

	private static String[] partyPeteShouts = {

	"Type ::clain to claim your AbyssalPS Tokens!",

	"Spend your AbyssalPS tokens here!",

	"I sell a varity of awesome items!",

	"You can buy AbyssalPS Tokens at ::store",

	"You can buy AbyssalPS Tokens at ::store",

	"You can buy AbyssalPS Tokens at ::store",

	};

	public void process() {
		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] == null)
				continue;
			npcs[i].clearUpdateFlags();

		}

		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] != null) {

				npcs[i].getDamage().execute();

				if (npcs[i].npcType == 5792) {
					if (System.currentTimeMillis() - npcs[i].lastChat < 5000) {
						continue;
					}
					npcs[i].forceChat(partyPeteShouts[(int) (Math.random() * partyPeteShouts.length)]);
					npcs[i].lastChat = System.currentTimeMillis();
				}

				if (npcs[i].actionTimer > 0) {
					npcs[i].actionTimer--;
				}

				if (npcs[i].freezeTimer > 0) {
					npcs[i].freezeTimer--;
				}

				if (npcs[i].hitDelayTimer > 0) {
					npcs[i].hitDelayTimer--;
				}

				if (npcs[i].hitDelayTimer == 1) {
					npcs[i].hitDelayTimer = 0;
					applyDamage(i);
				}

				if (npcs[i].attackTimer > 0) {
					npcs[i].attackTimer--;
				}

				if (npcs[i].spawnedBy > -1 && isKrakenMobs(i)) {
					if (PlayerHandler.players[npcs[i].spawnedBy] == null
							|| PlayerHandler.players[npcs[i].spawnedBy].heightLevel != npcs[i].heightLevel
							|| PlayerHandler.players[npcs[i].spawnedBy].isDead
							|| PlayerHandler.players[npcs[i].spawnedBy].playerLevel[3] <= 0) {
						npcs[i] = null;
					}
				}

				if (npcs[i] == null) {
					continue;
				}
				if (npcs[i].spawnedBy > 0 && !isKrakenMobs(i)) { // delete
					// summons
					// npc
					if (PlayerHandler.players[npcs[i].spawnedBy] == null
							|| PlayerHandler.players[npcs[i].spawnedBy].heightLevel != npcs[i].heightLevel
							|| PlayerHandler.players[npcs[i].spawnedBy].respawnTimer > 0
							|| !PlayerHandler.players[npcs[i].spawnedBy]
									.goodDistance(
											npcs[i].getX(),
											npcs[i].getY(),
											PlayerHandler.players[npcs[i].spawnedBy]
													.getX(),
											PlayerHandler.players[npcs[i].spawnedBy]
													.getY(), 40)) {

						if (PlayerHandler.players[npcs[i].spawnedBy] != null) {
							for (int o = 0; o < PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs.length; o++) {
								if (npcs[i].npcType == PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][0]) {
									if (PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] == 1)
										PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] = 0;
								}
							}
						}
						npcs[i] = null;
					}
				}
				if (npcs[i] == null)
					continue;

				/**
				 * Attacking player
				 **/
				if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead
						&& !switchesAttackers(i)) {
					int randomPlayerId = getCloseRandomPlayer(i);
					npcs[i].killerId = randomPlayerId;
					npcs[i].underAttack = true;
					npcs[i].setFollowId(randomPlayerId);
				} else if (isAggressive(i) && !npcs[i].underAttack
						&& !npcs[i].isDead && switchesAttackers(i)) {
					int randomPlayerId = getCloseRandomPlayer(i);
					npcs[i].killerId = randomPlayerId;
					npcs[i].underAttack = true;
					npcs[i].setFollowId(randomPlayerId);
				}

				if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000) {
					npcs[i].underAttackBy = 0;
					npcs[i].underAttack = false;
				}
				if (npcs[i].getFollowId() > 0) {
					if (!npcs[i].isDead) {
						int p = npcs[i].getFollowId();
						if (PlayerHandler.players[p] != null) {
							Client player = (Client) PlayerHandler.players[p];
							followPlayer(i, player.playerId);
						} else {
							npcs[i].resetFollowing();
						}
					}
				}

				if (npcs[i].killerId > 0 && !npcs[i].walkingHome) {
					if (!npcs[i].isDead) {
						int p = npcs[i].killerId;
						if (PlayerHandler.players[p] != null) {
							Client c = (Client) PlayerHandler.players[p];
							if (npcs[i] == null)
								continue;
							if (npcs[i].attackTimer == 0) {
								if (c != null) {
									attackPlayer(c, i);
								}
							}
						} else {
							npcs[i].killerId = 0;
							npcs[i].underAttack = false;
							npcs[i].facePlayer(0);
						}
					}
				}

				/**
				 * Random walking and walking home
				 **/
				if (npcs[i] == null)
					continue;
				if ((!npcs[i].underAttack || npcs[i].walkingHome)
						&& npcs[i].randomWalk && !npcs[i].isDead) {
					npcs[i].facePlayer(0);
					npcs[i].killerId = 0;
					if (npcs[i].spawnedBy == 0) {
						if ((npcs[i].absX > npcs[i].makeX
								+ Config.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absX < npcs[i].makeX
										- Config.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absY > npcs[i].makeY
										+ Config.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absY < npcs[i].makeY
										- Config.NPC_RANDOM_WALK_DISTANCE)) {
							System.out.println("Walking home.");
							npcs[i].walkingHome = true;
						}
					}

					if (npcs[i].walkingHome && npcs[i].absX == npcs[i].makeX
							&& npcs[i].absY == npcs[i].makeY) {
						npcs[i].walkingHome = false;
					} else if (npcs[i].walkingHome) {
						npcs[i].moveX = GetMove(npcs[i].absX, npcs[i].makeX);
						npcs[i].moveY = GetMove(npcs[i].absY, npcs[i].makeY);
						npcs[i].getNextNPCMovement(i);
						npcs[i].updateRequired = true;
					}
					if (npcs[i].walkingType == 1337
							&& ((npcs[i].absX != npcs[i].walkX) || (npcs[i].absY != npcs[i].walkY))) {
						npcs[i].moveX = GetMove(npcs[i].absX, npcs[i].walkX);
						npcs[i].moveY = GetMove(npcs[i].absY, npcs[i].walkY);
						npcs[i].getNextNPCMovement(i);
						npcs[i].updateRequired = true;
					}
					if (npcs[i].walkingType == 1) {
						if (Misc.random(3) == 1 && !npcs[i].walkingHome) {
							int MoveX = 0;
							int MoveY = 0;
							int Rnd = Misc.random(9);
							if (Rnd == 1) {
								MoveX = 1;
								MoveY = 1;
							} else if (Rnd == 2) {
								MoveX = -1;
							} else if (Rnd == 3) {
								MoveY = -1;
							} else if (Rnd == 4) {
								MoveX = 1;
							} else if (Rnd == 5) {
								MoveY = 1;
							} else if (Rnd == 6) {
								MoveX = -1;
								MoveY = -1;
							} else if (Rnd == 7) {
								MoveX = -1;
								MoveY = 1;
							} else if (Rnd == 8) {
								MoveX = 1;
								MoveY = -1;
							}

							if (MoveX == 1) {
								if (npcs[i].absX + MoveX < npcs[i].makeX + 1) {
									npcs[i].moveX = MoveX;
								} else {
									npcs[i].moveX = 0;
								}
							}

							if (MoveX == -1) {
								if (npcs[i].absX - MoveX > npcs[i].makeX - 1) {
									npcs[i].moveX = MoveX;
								} else {
									npcs[i].moveX = 0;
								}
							}

							if (MoveY == 1) {
								if (npcs[i].absY + MoveY < npcs[i].makeY + 1) {
									npcs[i].moveY = MoveY;
								} else {
									npcs[i].moveY = 0;
								}
							}

							if (MoveY == -1) {
								if (npcs[i].absY - MoveY > npcs[i].makeY - 1) {
									npcs[i].moveY = MoveY;
								} else {
									npcs[i].moveY = 0;
								}
							}
							handleClipping(i);
							npcs[i].getNextNPCMovement(i);
							npcs[i].updateRequired = true;
						}
					}
				}

				if (npcs[i].isDead == true) {
					if (npcs[i].actionTimer == 0 && npcs[i].applyDead == false
							&& npcs[i].needRespawn == false) {
						npcs[i].updateRequired = true;
						npcs[i].facePlayer(0);
						npcs[i].killedBy = getNpcKillerId(i);
						npcs[i].animNumber = getDeathAnimation(i); // dead emote
						npcs[i].animUpdateRequired = true;
						npcs[i].freezeTimer = 0;
						npcs[i].applyDead = true;
						killedBarrow(i);
						npcs[i].actionTimer = 4; // delete time
						resetPlayersInCombat(i);
					} else if (npcs[i].actionTimer == 0
							&& npcs[i].applyDead == true
							&& npcs[i].needRespawn == false) {
						npcs[i].needRespawn = true;
						npcs[i].actionTimer = getRespawnTime(i); // respawn time
						dropItems(i); // npc drops items!
						npcs[i].absX = npcs[i].makeX;
						npcs[i].absY = npcs[i].makeY;
						npcs[i].HP = npcs[i].MaxHP;
						npcs[i].animNumber = 0x328;
						npcs[i].updateRequired = true;
						npcs[i].animUpdateRequired = true;
						if (npcs[i].npcType >= 2440 && npcs[i].npcType <= 2446) {
							Server.objectManager.removeObject(npcs[i].absX,
									npcs[i].absY, npcs[i].heightLevel);
						}
					} else if (npcs[i].actionTimer == 0
							&& npcs[i].needRespawn == true) {
						if (npcs[i].spawnedBy > -1 || !npcs[i].isRespawnable()) {
							npcs[i] = null;
						} else {
							if (npcs[i].npcType == 6613) {
								Vetion.minionsDead++;
								npcs[i] = null;
							}
							if (npcs[i] == null) {
								continue;
							}
							int old1 = npcs[i].npcType;
							int old2 = npcs[i].makeX;
							int old3 = npcs[i].makeY;
							int old4 = npcs[i].heightLevel;
							int old5 = npcs[i].walkingType;
							int old6 = npcs[i].MaxHP;
							int old7 = npcs[i].maxHit;
							int old8 = npcs[i].attack;
							int old9 = npcs[i].defence;

							npcs[i] = null;
							newNPC(old1, old2, old3, old4, old5, old6, old7,
									old8, old9);
						}
					}
				}
			}
		}
	}

	public boolean getsPulled(int i) {
		switch (npcs[i].npcType) {
		case 2550:
			if (npcs[i].firstAttacker > 0)
				return false;
			break;
		}
		return true;
	}

	public boolean multiAttacks(int i) {
		switch (npcs[i].npcType) {
		case 2558:
			return true;
		case 2562:
			if (npcs[i].attackType == 2)
				return true;
		case 2550:
			if (npcs[i].attackType == 1)
				return true;
		default:
			return false;
		}

	}

	/**
	 * Npc killer id?
	 **/

	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int p = 1; p < Config.MAX_PLAYERS; p++) {
			if (PlayerHandler.players[p] != null) {
				if (PlayerHandler.players[p].lastNpcAttacked == npcId) {
					if (PlayerHandler.players[p].totalDamageDealt > oldDamage) {
						oldDamage = PlayerHandler.players[p].totalDamageDealt;
						killerId = p;
					}
					PlayerHandler.players[p].totalDamageDealt = 0;
				}
			}
		}
		return killerId;
	}

	/**
	 * 
	 */

	private void killedBarrow(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			for (int o = 0; o < c.barrowsNpcs.length; o++) {
				if (npcs[i].npcType == c.barrowsNpcs[o][0]) {
					c.barrowsNpcs[o][1] = 2; // 2 for dead
					c.getPoints()[1]++;
				}
			}
		}
	}

	public void dropItems(int i) {
		Client c = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {

			if (npcs[i].npcType == c.getSlayerData()[0]) {
				SlayerConstants.getSlayerExperience(c, i);
			}

			c.getAchievements().onKillNpc(npcs[i].npcType);
			c.getBossLogs().onKillNPC(npcs[i].npcType);
			SpecialNPCHandler specialNpc = SpecialNPCHandler
					.forId(npcs[i].npcType);

			if (specialNpc != null) {
				specialNpc.onDeath(c, npcs[i]);
			}

			if (c.getRecipe().inGame()) {
				c.getRecipe().increaseWaveId();
				c.getRecipe().startWave();
			}

			if (npcs[i].npcType == 1208) {
				c.getPoints()[0]++;
			}

			if (npcs[i].npcType == 1213) {
				c.getPoints()[0] += 3;
			}

			int x = npcs[i].absX;
			int y = npcs[i].absY;

			if (npcs[i].npcType == ZulrahConstants.BLUE_ZULRAH_ID
					|| npcs[i].npcType == ZulrahConstants.RED_ZULRAH_ID) {
				npcs[i].npcType = ZulrahConstants.GREEN_ZULRAH_ID;
			}

			if (npcs[i].npcType == ZulrahConstants.GREEN_ZULRAH_ID) {
				x = ZulrahConstants.ZULRAH_ISLAND_POSITION.getX();
				y = ZulrahConstants.ZULRAH_ISLAND_POSITION.getY();
			}

			if (npcs[i].npcType == KrakenConstants.KRAKEN_ID) {
				x = KrakenConstants.KRAKEN_AREA_POSITION.getX();
				y = KrakenConstants.KRAKEN_AREA_POSITION.getY();
			}

			if (MagicalAnimator.dropItems(c, npcs[i])) {
				return;
			}

			for (GameItem item : Drops.getRandomDrop(c, npcs[i].npcType)) {
				if (BoneData.boneExist(item.id)
						&& c.getItems().playerHasItem(13116)) {
					BoneCrusher.crushBones(c, item.id);
					continue;
				}
				Server.itemHandler.createGroundItem(c, item.id, x, y,
						c.heightLevel, item.amount, c.playerId);
			}

			if (c.getMinigame() != null) {
				if (c.getMinigame().onDropItems(c, npcs[i])) {
					return;
				}
			}
		}
	}

	/**
	 * Resets players in combat
	 */

	public void resetPlayersInCombat(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null)
				if (PlayerHandler.players[j].underAttackBy2 == i)
					PlayerHandler.players[j].underAttackBy2 = 0;
		}
	}

	public static boolean ignoreClipping(int i) {
		switch (npcs[i].npcType) {
		case 494:
		case 5535:
		case 5947:
		case 5961:
		case 3162:
		case 5862:
		case 6618:
		case ZulrahConstants.GREEN_ZULRAH_ID:
		case ZulrahConstants.BLUE_ZULRAH_ID:
		case ZulrahConstants.RED_ZULRAH_ID:
		case ZulrahConstants.SNAKELING_ID:
			return true;
		}
		return false;
	}

	/**
	 * Npc Follow Player
	 **/

	public int GetMove(int Place1, int Place2) {
		if ((Place1 - Place2) == 0) {
			return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean stopFollowing(int i) {
		switch (npcs[i].npcType) {
		case 5947:
		case 5961:
			return true;
		}
		return false;
	}

	public void followPlayer(int i, int playerId) {
		if (PlayerHandler.players[playerId] == null) {
			npcs[i].resetFollowing();
			return;
		}
		if (PlayerHandler.players[playerId].respawnTimer > 0) {
			npcs[i].resetFollowing();
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
			return;
		}
		if (stopFollowing(i)) {
			return;
		}
		Client c = (Client) PlayerHandler.players[playerId];

		if (c == null) {
			return;
		}

		npcs[i].randomWalk = false;
		if (npcs[i].heightLevel != c.heightLevel) {
			return;
		}
		npcs[i].facePlayer(playerId);
		if (npcs[i].distanceTo(c) > 16) {
			System.out.println("We get here.");
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
			npcs[i].resetFollowing();
			return;
		}
		if (npcs[i].distanceTo(c) <= distanceRequired(i)) {
			return;
		}
		NPCPathFinder.walkTowards(npcs[i], c);
	}

	public boolean goodDistance(int npcX, int npcY, int npcSize, int playerX,
			int playerY, int distance) {
		return playerX >= (npcX - distance)
				&& playerX <= (npcX + npcSize + distance)
				&& playerY >= (npcY - distance)
				&& playerY <= (npcY + npcSize + distance);
	}

	/**
	 * load spell
	 **/
	public void loadSpell2(int i) {
		npcs[i].attackType = 3;
		int random = Misc.random(3);
		if (random == 0) {
			npcs[i].projectileId = 393; // red
			npcs[i].endGfx = 430;
		} else if (random == 1) {
			npcs[i].projectileId = 394; // green
			npcs[i].endGfx = 429;
		} else if (random == 2) {
			npcs[i].projectileId = 395; // white
			npcs[i].endGfx = 431;
		} else if (random == 3) {
			npcs[i].projectileId = 396; // blue
			npcs[i].endGfx = 428;
		}
	}

	public void loadSpell(int i) {
		switch (npcs[i].npcType) {
		case 2892:
			npcs[i].projectileId = 94;
			npcs[i].attackType = 2;
			npcs[i].endGfx = 95;
			break;
		case 2894:
			npcs[i].projectileId = 298;
			npcs[i].attackType = 1;
			break;
		case 50:
			int random = Misc.random(4);
			if (random == 0) {
				npcs[i].projectileId = 393; // red
				npcs[i].endGfx = 430;
				npcs[i].attackType = 3;
			} else if (random == 1) {
				npcs[i].projectileId = 394; // green
				npcs[i].endGfx = 429;
				npcs[i].attackType = 3;
			} else if (random == 2) {
				npcs[i].projectileId = 395; // white
				npcs[i].endGfx = 431;
				npcs[i].attackType = 3;
			} else if (random == 3) {
				npcs[i].projectileId = 396; // blue
				npcs[i].endGfx = 428;
				npcs[i].attackType = 3;
			} else if (random == 4) {
				npcs[i].projectileId = -1; // melee
				npcs[i].endGfx = -1;
				npcs[i].attackType = 0;
			}
			break;
		// arma npcs
		case 2561:
			npcs[i].attackType = 0;
			break;
		case 2560:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1190;
			break;
		case 2559:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2558:
			random = Misc.random(1);
			npcs[i].attackType = 1 + random;
			if (npcs[i].attackType == 1) {
				npcs[i].projectileId = 1197;
			} else {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1198;
			}
			break;
		// sara npcs
		case 2562: // sara
			random = Misc.random(1);
			if (random == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 1224;
				npcs[i].projectileId = -1;
			} else if (random == 1)
				npcs[i].attackType = 0;
			break;
		case 2563: // star
			npcs[i].attackType = 0;
			break;
		case 2564: // growler
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2565: // bree
			npcs[i].attackType = 1;
			npcs[i].projectileId = 9;
			break;
		// bandos npcs
		case 2550:
			random = Misc.random(2);
			if (random == 0 || random == 1)
				npcs[i].attackType = 0;
			else {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 1211;
				npcs[i].projectileId = 288;
			}
			break;
		case 2551:
			npcs[i].attackType = 0;
			break;
		case 2552:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2553:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1206;
			break;
		case 2025:
			npcs[i].attackType = 2;
			int r = Misc.random(3);
			if (r == 0) {
				npcs[i].gfx100(158);
				npcs[i].projectileId = 159;
				npcs[i].endGfx = 160;
			}
			if (r == 1) {
				npcs[i].gfx100(161);
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			}
			if (r == 2) {
				npcs[i].gfx100(164);
				npcs[i].projectileId = 165;
				npcs[i].endGfx = 166;
			}
			if (r == 3) {
				npcs[i].gfx100(155);
				npcs[i].projectileId = 156;
			}
			break;
		case 2881:// supreme
			npcs[i].attackType = 1;
			npcs[i].projectileId = 298;
			break;

		case 2882:// prime
			npcs[i].attackType = 2;
			npcs[i].projectileId = 162;
			npcs[i].endGfx = 477;
			break;

		case 2028:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 27;
			break;

		case 3200:
			int r2 = Misc.random(1);
			if (r2 == 0) {
				npcs[i].attackType = 1;
				npcs[i].gfx100(550);
				npcs[i].projectileId = 551;
				npcs[i].endGfx = 552;
			} else {
				npcs[i].attackType = 2;
				npcs[i].gfx100(553);
				npcs[i].projectileId = 554;
				npcs[i].endGfx = 555;
			}
			break;
		case 2745:
			int r3 = 0;
			if (goodDistance(npcs[i].absX, npcs[i].absY,
					PlayerHandler.players[npcs[i].spawnedBy].absX,
					PlayerHandler.players[npcs[i].spawnedBy].absY, 1))
				r3 = Misc.random(2);
			else
				r3 = Misc.random(1);
			if (r3 == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 157;
				npcs[i].projectileId = 448;
			} else if (r3 == 1) {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 451;
				npcs[i].projectileId = -1;
			} else if (r3 == 2) {
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
			}
			break;
		case 2743:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 445;
			npcs[i].endGfx = 446;
			break;

		case 2631:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 443;
			break;
		}
	}

	/**
	 * Distanced required to attack
	 **/
	public int distanceRequired(int i) {
		switch (npcs[i].npcType) {
		case ZulrahConstants.GREEN_ZULRAH_ID:
		case ZulrahConstants.BLUE_ZULRAH_ID:
		case ZulrahConstants.RED_ZULRAH_ID:
			return 20;
		case 6616:
			return 4;

		case 6615:
			return 2;

		case 6766:
			return 3;

		case 6611:
		case 6612:
		case 2265:
		case 3428:

		case 5535:
		case 494:
		case 5961:
		case 5947:
			return 12;

		case 239:
			return King_Black_Dragon.getCombatStyle() > 0 ? 6 : 1;

		case 2218:
		case 2217:
		case 6618:
		case 3127:
		case 3164:
		case 3163:
		case 3162:
		case 3125:
		case 3121:
		case 319:
		case 2207:
			return 4;

		case 2054:
			return 6;

		case 3165:
			return 2;

		default:
			return 1;
		}
	}

	public int followDistance(int i) {
		switch (npcs[i].npcType) {
		case 2550:
		case 2551:
		case 2562:
		case 2563:
		case 6618:
		case 6611:
		case 6612:
		case 6613:
			return 8;

		case 239:
			return King_Black_Dragon.getCombatStyle() > 0 ? 6 : 1;

		case 2218:
		case 2217:
		case 2215:
		case 2216:
		case 3164:
		case 3163:
		case 3165:
		case 3162:
		case FightCavesDefinitions.TZ_TOK_JAD_ID:
		case FightCavesDefinitions.KET_ZEK_ID:
		case FightCavesDefinitions.YT_MEJ_KOT_ID:
		case FightCavesDefinitions.TOK_XIL_ID:
		case FightCavesDefinitions.TZ_KEK_ID:
		case FightCavesDefinitions.TZ_KIH_ID:
			return 16;

		case 319:
		case 6616:
		case 6615:
		case 2206:
		case 2205:
		case 2207:
			return 40;

		case 2265:
		case 2267:
		case 2266:
			return 4;

		case 6619:
		case 3428:
			return 6;
		case ZulrahConstants.SNAKELING_ID:
			return 30;

		}
		return 0;

	}

	public int getProjectileSpeed(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 3200:
			return 85;

		case 2745:
			return 130;

		case 50:
			return 90;

		case 2025:
			return 85;

		case 2028:
			return 80;

		default:
			return 85;
		}
	}

	public void handleClipping(int i) {
		NPC npc = npcs[i];
		if (npc.moveX == 1 && npc.moveY == 1) {
			if ((Region
					.getClipping(npc.absX + 1, npc.absY + 1, npc.heightLevel) & 0x12801e0) != 0) {
				npc.moveX = 0;
				npc.moveY = 0;
				if ((Region
						.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) == 0)
					npc.moveY = 1;
				else
					npc.moveX = 1;
			}
		} else if (npc.moveX == -1 && npc.moveY == -1) {
			if ((Region
					.getClipping(npc.absX - 1, npc.absY - 1, npc.heightLevel) & 0x128010e) != 0) {
				npc.moveX = 0;
				npc.moveY = 0;
				if ((Region
						.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) == 0)
					npc.moveY = -1;
				else
					npc.moveX = -1;
			}
		} else if (npc.moveX == 1 && npc.moveY == -1) {
			if ((Region
					.getClipping(npc.absX + 1, npc.absY - 1, npc.heightLevel) & 0x1280183) != 0) {
				npc.moveX = 0;
				npc.moveY = 0;
				if ((Region
						.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) == 0)
					npc.moveY = -1;
				else
					npc.moveX = 1;
			}
		} else if (npc.moveX == -1 && npc.moveY == 1) {
			if ((Region
					.getClipping(npc.absX - 1, npc.absY + 1, npc.heightLevel) & 0x128013) != 0) {
				npc.moveX = 0;
				npc.moveY = 0;
				if ((Region
						.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) == 0)
					npc.moveY = 1;
				else
					npc.moveX = -1;
			}
		} // Checking Diagonal movement.

		if (npc.moveY == -1) {
			if ((Region.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) & 0x1280102) != 0)
				npc.moveY = 0;
		} else if (npc.moveY == 1) {
			if ((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) & 0x1280120) != 0)
				npc.moveY = 0;
		} // Checking Y movement.
		if (npc.moveX == 1) {
			if ((Region.getClipping(npc.absX + 1, npc.absY, npc.heightLevel) & 0x1280180) != 0)
				npc.moveX = 0;
		} else if (npc.moveX == -1) {
			if ((Region.getClipping(npc.absX - 1, npc.absY, npc.heightLevel) & 0x1280108) != 0)
				npc.moveX = 0;
		} // Checking X movement.
	}

	/**
	 * NPC Attacking Player
	 **/

	public void attackPlayer(Client c, int i) {
		if (npcs[i] != null) {
			if (!npcs[i].canAttack()) {
				return;
			}
			if (npcs[i].isDead) {
				return;
			}
			if (Region.pathBlocked(npcs[i], c) && !NPCHandler.ignoreClipping(i)
					&& npcs[i].distanceTo(c) > 1) {
				return;
			}
			if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0
					&& npcs[i].underAttackBy != c.playerId) {
				npcs[i].killerId = 0;
				return;
			}
			if (!npcs[i].inMulti()
					&& (c.underAttackBy > 0 || (c.underAttackBy2 > 0 && c.underAttackBy2 != i))) {
				npcs[i].killerId = 0;
				return;
			}
			if (npcs[i].heightLevel != c.heightLevel) {
				npcs[i].killerId = 0;
				return;
			}
			npcs[i].facePlayer(c.playerId);
			if (npcs[i].distanceTo(c) <= distanceRequired(i)) {
				if (c.respawnTimer <= 0) {
					npcs[i].killerId = c.playerId;
					npcs[i].setFollowId(c.playerId);
					npcs[i].facePlayer(c.playerId);
					npcs[i].attackTimer = getNpcDelay(i);
					if (SpecialNPCHandler.forId(npcs[i].npcType) != null) {
						c.underAttackBy2 = i;
						c.singleCombatDelay2 = System.currentTimeMillis();
						c.getPA().removeAllWindows();
						SpecialNPCHandler.forId(npcs[i].npcType).execute(c,
								npcs[i]);
						return;
					}
					npcs[i].attackType = 0;
					c.underAttackBy2 = i;
					c.singleCombatDelay2 = System.currentTimeMillis();
					npcs[i].oldIndex = c.playerId;
					startAnimation(getAttackAnimation(i), i);
					c.getPA().removeAllWindows();
					npcs[i].getDamage().add(
							new Damage(c, Misc.random(npcs[i].maxHit),
									getHitDelay(i), npcs[i].attackType));
				}
			}
		}
	}

	public int offset(int i) {
		switch (npcs[i].npcType) {
		case 50:
			return 2;
		case 2881:
		case 2882:
			return 1;
		case 2745:
		case 2743:
			return 1;
		}
		return 0;
	}

	public boolean specialCase(Client c, int i) { // responsible for npcs that
													// much
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(), c.getY(), 8)
				&& !goodDistance(npcs[i].getX(), npcs[i].getY(), c.getX(),
						c.getY(), distanceRequired(i)))
			return true;
		return false;
	}

	public boolean retaliates(int npcType) {
		return npcType < 3777 || npcType > 3780
				&& !(npcType >= 2440 && npcType <= 2446);
	}

	public void applyDamage(int i) {
		if (npcs[i] != null) {
			if (PlayerHandler.players[npcs[i].oldIndex] == null) {
				return;
			}
			if (npcs[i].isDead)
				return;
			Client c = (Client) PlayerHandler.players[npcs[i].oldIndex];
			if (multiAttacks(i)) {
				multiAttackDamage(i);
				return;
			}
			if (c.playerIndex <= 0 && c.npcIndex <= 0)
				if (c.autoRet == 1)
					c.npcIndex = i;
			if (c.attackTimer <= 3 || c.attackTimer == 0 && c.npcIndex == 0
					&& c.oldNpcIndex == 0) {
				c.startAnimation(WeaponAnimations.blockAnimation(c));
			}
			if (c.respawnTimer <= 0) {
				int damage = 0;
				if (npcs[i].attackType == 0) {
					damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
					}
					if (c.prayerActive[18]) { // protect from melee
						damage = 0;
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
				}

				if (npcs[i].attackType == 1) { // range
					damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(c.getCombat().calculateRangeDefence()) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
					}
					if (c.prayerActive[17]) { // protect from range
						damage = 0;
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
				}

				if (npcs[i].attackType == 2) { // magic
					damage = Misc.random(npcs[i].maxHit);
					boolean magicFailed = false;
					if (10 + Misc.random(c.getCombat().mageDef()) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
						magicFailed = true;
					}
					if (c.prayerActive[16]) { // protect from magic
						damage = 0;
						magicFailed = true;
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
					if (npcs[i].endGfx > 0
							&& (!magicFailed || isFightCaveNpc(i))) {
						c.gfx100(npcs[i].endGfx);
					} else {
						c.gfx100(85);
					}
				}

				if (npcs[i].attackType == 3) { // fire breath
					int anti = c.getPA().antiFire();
					if (anti == 0) {
						damage = Misc.random(30) + 10;
						c.sendMessage("You are badly burnt by the dragon fire!");
					} else if (anti == 1)
						damage = Misc.random(20);
					else if (anti == 2)
						damage = Misc.random(5);
					if (c.playerLevel[3] - damage < 0)
						damage = c.playerLevel[3];
					c.gfx100(npcs[i].endGfx);
				}
				handleSpecialEffects(c, i, damage);
				c.logoutDelay = System.currentTimeMillis(); // logout delay
				// c.setHitDiff(damage);
				c.handleHitMask(damage);
				c.playerLevel[3] -= damage;
				c.getPA().refreshSkill(3);
				c.updateRequired = true;
				// c.setHitUpdateRequired(true);
			}
		}
	}

	public void handleSpecialEffects(Client c, int i, int damage) {
		if (npcs[i].npcType == 2892 || npcs[i].npcType == 2894) {
			if (damage > 0) {
				if (c != null) {
					if (c.playerLevel[5] > 0) {
						c.playerLevel[5]--;
						c.getPA().refreshSkill(5);
						c.getPA().appendPoison(12);
					}
				}
			}
		}

	}

	public void startAnimation(int animId, int i) {
		npcs[i].animNumber = animId;
		npcs[i].animUpdateRequired = true;
		npcs[i].updateRequired = true;
	}

	public boolean goodDistance(int objectX, int objectY, int playerX,
			int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY
								|| (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY
								|| (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY
								|| (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public int getMaxHit(int i) {
		switch (npcs[i].npcType) {
		case 2558:
			if (npcs[i].attackType == 2)
				return 28;
			else
				return 68;
		case 2562:
			return 31;
		case 2550:
			return 36;
		}
		return 1;
	}

	public boolean loadAutoSpawn(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					newNPC(Integer.parseInt(token3[0]),
							Integer.parseInt(token3[1]),
							Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]),
							Integer.parseInt(token3[4]),
							getNpcListHP(Integer.parseInt(token3[0])),
							Integer.parseInt(token3[5]),
							Integer.parseInt(token3[6]),
							Integer.parseInt(token3[7]));

				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
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
		return false;
	}

	public int getNpcListHP(int npcId) {
		for (int i = 0; i < maxListedNPCs; i++) {
			if (NpcList[i] != null) {
				if (NpcList[i].npcId == npcId) {
					return NpcList[i].npcHealth;
				}
			}
		}
		return 0;
	}

	public String getNpcListName(int npcId) {
		for (int i = 0; i < maxListedNPCs; i++) {
			if (NpcList[i] != null) {
				if (NpcList[i].npcId == npcId) {
					return NpcList[i].npcName.replace("_", " ");
				}
			}
		}
		return "nothing";
	}

	public boolean loadNPCList(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("npc")) {
					newNPCList(Integer.parseInt(token3[0]), token3[1],
							Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]));
				}
			} else {
				if (line.equals("[ENDOFNPCLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
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
		return false;
	}

}
