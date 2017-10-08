package rs2.abyssalps.content.minigame.fightcave;

import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.player.Player;

public class FightCavesDefinitions {

	public static final int TOKKULS_ID = 6529;
	public static final int FIRECAPE_ID = 6570;
	
	public static final int TZ_KIH_ID = 3116;
	public static final int TZ_KEK_ID = 3119;
	public static final int TOK_XIL_ID = 3121;
	public static final int YT_MEJ_KOT_ID = 3123;
	public static final int KET_ZEK_ID = 3125;
	public static final int TZ_TOK_JAD_ID = 3127;

	public static final Position INSIDE_CAVE_POSITION = new Position(2413, 5117);
	public static final Position OUTSIDE_CAVE_POSITION = new Position(2438, 5168);

	public static final Position[] SPAWN_POSITIONS = { new Position(2391, 5088), new Position(2407, 5092) };

	public static boolean inArea(Player player) {
		return player.absX > 2360 && player.absX < 2440 && player.absY > 5050 && player.absY < 5130;
	}
	
	public static int getHitpoints(int npcId) {
		switch (npcId) {
		case TZ_TOK_JAD_ID:
			return 255;
		case KET_ZEK_ID:
			return 160;
		case YT_MEJ_KOT_ID:
			return 80;
		case TOK_XIL_ID:
			return 40;
		case TZ_KEK_ID:
			return 20;
		case TZ_KIH_ID:
			return 10;
		}
		return 0;
	}

	public static int getMaxHit(int npcId) {
		switch (npcId) {
		case TZ_TOK_JAD_ID:
			return 97;
		case KET_ZEK_ID:
			return 50;
		case YT_MEJ_KOT_ID:
			return 25;
		case TOK_XIL_ID:
			return 13;
		case TZ_KEK_ID:
			return 7;
		case TZ_KIH_ID:
			return 4;
		}
		return 0;
	}

	public static int getDefence(int npcId) {
		switch (npcId) {
		case TZ_TOK_JAD_ID:
			return 250;
		case KET_ZEK_ID:
			return 160;
		case YT_MEJ_KOT_ID:
			return 80;
		case TOK_XIL_ID:
			return 20;
		case TZ_KEK_ID:
			return 10;
		case TZ_KIH_ID:
			return 5;
		}
		return 0;
	}

	public static int getAttack(int npcId) {
		switch (npcId) {
		case TZ_TOK_JAD_ID:
			return 500;
		case KET_ZEK_ID:
			return 250;
		case YT_MEJ_KOT_ID:
			return 140;
		case TOK_XIL_ID:
			return 100;
		case TZ_KEK_ID:
			return 10;
		case TZ_KIH_ID:
			return 5;
		}
		return 0;
	}

	public static int[][] WAVES = {
			/*
			 * wave 1
			 */
			{ TZ_KIH_ID },
			/*
			 * wave 2
			 */
			{ TZ_KIH_ID, TZ_KIH_ID },
			/*
			 * wave 3
			 */
			{ TZ_KEK_ID },
			/*
			 * wave 4
			 */
			{ TZ_KEK_ID, TZ_KIH_ID },
			/*
			 * wave 5
			 */
			{ TZ_KEK_ID, TZ_KEK_ID },
			/*
			 * wave 6
			 */
			{ TOK_XIL_ID },
			/*
			 * wave 7
			 */
			{ TOK_XIL_ID, TZ_KEK_ID },
			/*
			 * wave 8
			 */
			{ TOK_XIL_ID, TOK_XIL_ID },
			/*
			 * wave 9
			 */
			{ YT_MEJ_KOT_ID },
			/*
			 * wave 10
			 */
			{ YT_MEJ_KOT_ID, TOK_XIL_ID },
			/*
			 * wave 11
			 */
			{ YT_MEJ_KOT_ID, YT_MEJ_KOT_ID },
			/*
			 * wave 12
			 */
			{ KET_ZEK_ID },
			/*
			 * wave 13
			 */
			{ KET_ZEK_ID, YT_MEJ_KOT_ID },
			/*
			 * wave 14
			 */
			{ KET_ZEK_ID, KET_ZEK_ID },
			/*
			 * wave 15
			 */
			{ TZ_TOK_JAD_ID }
			/*
			 * 
			 */
	};

}