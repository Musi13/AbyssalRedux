package rs2.abyssalps.content.skill.smithing;

import java.util.HashMap;

public enum SmithingTable {

	/**
	 * Bronze
	 */
	bronzeDagger(1205, 1, (int) 12.5, 2349, 1, 1), bronzeHatchet(1351, 1,
			(int) 12.5, 2349, 1, 1), bronzeSword(1277, 4, (int) 12.5, 2349, 1,
			1), bronzeScimitar(1321, 5, 25, 2349, 2, 1), bronzeLongsword(1291,
			6, 25, 2349, 2, 1), bronze2HSword(1307, 14, (int) 37.5, 2349, 3, 1), bronzeMace(
			1422, 2, (int) 12.5, 2349, 1, 1), bronzeWarhammer(1337, 9,
			(int) 37.5, 2349, 3, 1), bronzeBattleaxe(1375, 10, (int) 37.5,
			2349, 3, 1), bronzeClaws(3095, 13, 25, 2349, 2, 1), bronzeChainbody(
			1103, 11, (int) 37.5, 2349, 3, 1), bronzePlatelegs(1075, 16,
			(int) 37.5, 2349, 3, 1), bronzePlateskirt(1087, 16, (int) 37.5,
			2349, 3, 1), bronzePlatebody(1117, 18, (int) 62.5, 2349, 5, 1), bronzeMedhelm(
			1139, 3, (int) 12.5, 2349, 1, 1), bronzeFullhelm(1155, 7, 25, 2349,
			2, 1), bronzeSquareShield(1173, 8, 25, 2349, 2, 1), BronzeKiteshield(
			1189, 12, (int) 37.5, 2349, 3, 1), bronzeNails(4819, 4, (int) 12.5,
			2349, 1, 15), bronzeDarttip(819, 4, (int) 12.5, 2349, 1, 10), bronzeArrowtip(
			39, 5, (int) 12.5, 2349, 1, 15), bronzeThrownknife(864, 7,
			(int) 12.5, 2349, 1, 5),
	/**
	 * Iron
	 */
	ironDagger(1203, 15, 25, 2351, 1, 1), ironSword(1279, 19, 25, 2351, 1, 1), ironScimitar(
			1323, 20, 50, 2351, 2, 1), ironLongsword(1293, 21, 50, 2351, 2, 1), iron2HSword(
			1309, 29, 75, 2351, 3, 1), ironHatchet(1349, 16, 25, 2351, 1, 1), ironMace(
			1420, 17, 25, 2351, 1, 1), ironWarhammer(1335, 24, 75, 2351, 3, 1), ironBattleaxe(
			1363, 25, 75, 2351, 3, 1), ironClaws(3096, 28, 50, 2351, 2, 1), ironChainbody(
			1101, 26, 75, 2351, 3, 1), ironPlatelegs(1067, 31, 75, 2351, 3, 1), ironPlateskirt(
			1081, 31, 75, 2351, 3, 1), ironPlatebody(1115, 33, 125, 2351, 5, 1), ironLantern(
			4540, 26, 25, 2351, 1, 1), ironMedhelm(1137, 18, 25, 2351, 1, 1), ironFullhelm(
			1153, 22, 50, 2351, 2, 1), ironSquareShield(1175, 23, 50, 2351, 2,
			1), ironKiteshield(1191, 27, 75, 2351, 3, 1), ironNails(4820, 19,
			25, 2351, 1, 15), ironDarttips(820, 19, 25, 2351, 1, 10), ironArrowtips(
			40, 20, 25, 2351, 1, 15), ironKnifes(863, 22, 25, 2351, 1, 5),
	/**
	 * Steel
	 */
	steelDagger(1207, 30, (int) 37.5, 2353, 1, 1), steelSword(1281, 34,
			(int) 37.5, 2353, 1, 1), steelScimitar(1324, 35, 75, 2353, 2, 1), steelLongsword(
			1295, 36, 75, 2353, 2, 1), steel2HSword(1311, 44, (int) 112.5,
			2353, 3, 1), steelHatchet(1353, 31, (int) 37.5, 2353, 1, 1), steelMace(
			1424, 32, (int) 37.5, 2353, 1, 1), steelWarhammer(1339, 39,
			(int) 112.5, 2353, 3, 1), steelBattleaxe(1365, 40, (int) 112.5,
			2353, 3, 1), steelClaws(3097, 43, 75, 2353, 2, 1), steelChainbody(
			1105, 41, (int) 112.5, 2353, 3, 1), steelPlatelegs(1069, 46,
			(int) 112.5, 2353, 3, 1), steelPlateskirt(1083, 46, (int) 112.5,
			2353, 3, 1), steelPlatebody(1119, 48, (int) 187.5, 2353, 5, 1), steelMedhelm(
			1141, 33, (int) 37.5, 2353, 1, 1), steelFullhelm(1157, 37, 75,
			2353, 2, 1), steelSquareShield(1177, 38, 75, 2353, 2, 1), steelKiteshield(
			1193, 42, (int) 112.5, 2353, 3, 1), steelNails(1539, 34,
			(int) 37.5, 2353, 1, 15), steelDarttips(821, 34, (int) 37.5, 2353,
			1, 10), steelArrowtips(41, 35, (int) 37.5, 2353, 1, 15), steelKnife(
			865, 37, (int) 37.5, 2353, 1, 5), cannonBalls(2, 35, (int) 25.5,
			2353, 1, 4), steelStuds(2370, 36, (int) 37.5, 2353, 1, 5),
	/**
	 * Mithril
	 */
	mithrilDagger(1209, 50, 50, 2359, 1, 1), mithrilSword(1285, 54, 50, 2359,
			1, 1), mithrilScimitar(1329, 55, 100, 2359, 2, 1), mithrilLongsword(
			1299, 56, 100, 2359, 2, 1), mithril2HSword(1315, 64, 150, 2359, 3,
			1), mithrilHatchet(1355, 51, 50, 2359, 1, 1), mithrilMace(1428, 52,
			50, 2359, 1, 1), mithrilWarhammer(1343, 59, 150, 2359, 3, 1), mithrilBattleaxe(
			1369, 60, 150, 2359, 3, 1), mithrilClaws(3099, 63, 100, 2359, 2, 1), mithrilChainbody(
			1109, 61, 150, 2359, 3, 1), mithrilPlatelegs(1071, 66, 150, 2359,
			3, 1), mithrilPlateskirt(1085, 66, 150, 2359, 3, 1), mithrilPlatebody(
			1121, 68, 250, 2359, 5, 1), mithrilMedhelm(1143, 53, 50, 2359, 1, 1), mithrilFullhelm(
			1159, 57, 100, 2359, 2, 1), mithrilSquareShield(1181, 58, 100,
			2359, 2, 1), mithrilKiteshield(1197, 62, 150, 2359, 3, 1), mithrilNails(
			4822, 54, 50, 2359, 1, 15), mithrilDarttips(822, 54, 50, 2359, 1,
			10), mithrilArrowtips(42, 55, 50, 2359, 1, 15), mithrilKnifes(866,
			57, 50, 2359, 1, 5),

	/**
	 * Adamantite
	 */
	adamantDagger(1211, 70, (int) 62.5, 2361, 1, 1), adamantSword(1287, 74,
			(int) 62.5, 2361, 1, 1), adamantScimitar(1331, 75, 125, 2361, 2, 1), adamantLongsword(
			1301, 76, 125, 2361, 2, 1), adamant2HSword(1317, 84, (int) 187.5,
			2361, 3, 1), adamantHatchet(1357, 71, (int) 62.5, 2361, 1, 1), adamantMace(
			1430, 72, (int) 62.5, 2361, 1, 1), adamantWarhammer(1345, 79,
			(int) 187.5, 2361, 3, 1), adamantBattleaxe(1371, 80, (int) 187.5,
			2361, 3, 1), adamantClaws(3100, 83, 125, 2361, 2, 1), adamantChainbody(
			1111, 81, (int) 187.5, 2361, 3, 1), adamantPlatelegs(1073, 86,
			(int) 187.5, 2361, 3, 1), adamantPlateskirt(1091, 86, (int) 187.5,
			2361, 3, 1), adamantPlatebody(1123, 88, (int) 312.5, 2361, 5, 1), adamantMedhelm(
			1145, 73, (int) 62.5, 2361, 1, 1), adamantFullhelm(1161, 77, 125,
			2361, 2, 1), adamantSquareShield(1183, 78, 125, 2361, 2, 1), adamantKiteshield(
			1199, 82, (int) 187.5, 2361, 3, 1), adamantNails(4823, 74,
			(int) 62.5, 2361, 1, 15), adamantDarttips(823, 74, (int) 62.5,
			2361, 1, 10), adamantArrowtips(43, 75, (int) 62.5, 2361, 1, 15), adamantKnifes(
			867, 77, (int) 62.5, 2361, 1, 5),

	/**
	 * Rune
	 */
	runeDagger(1213, 85, 75, 2363, 1, 1), runeSword(1289, 89, 75, 2363, 1, 1), runeScimitar(
			1333, 90, 150, 2363, 2, 1), runeLongsword(1303, 91, 150, 2363, 2, 1), rune2HSword(
			1319, 99, 225, 2363, 3, 1), runeHatchet(1359, 86, 75, 2363, 1, 1), runeMace(
			1432, 87, 75, 2363, 1, 1), runeWarhammer(1347, 94, 225, 2363, 3, 1), runeBattleaxe(
			1373, 95, 225, 2363, 3, 1), runeClaws(3101, 98, 150, 2363, 2, 1), runeChainbody(
			1113, 96, 225, 2363, 3, 1), runePlatelegs(1079, 99, 225, 2363, 3, 1), runePlateskirt(
			1093, 99, 225, 2363, 3, 1), runePlatebody(1127, 99, 375, 2363, 5, 1), runeMedhelm(
			1147, 88, 75, 2363, 1, 1), runeFullhelm(1163, 92, 150, 2363, 2, 1), runeSquareShield(
			1185, 93, 150, 2363, 2, 1), runeKiteshield(1201, 97, 225, 2363, 3,
			1), runeNails(4824, 89, 75, 2363, 1, 15), runeDarttips(824, 89, 75,
			2363, 1, 10), runeArrowtips(44, 90, 75, 2363, 1, 15), runeKnife(
			868, 92, 75, 2363, 1, 5),

	dragonfireShield(11283, 90, 2000, 11286, 1, 1), arcaneSpiritShield(13738,
			85, 4000, 13746, 1, 1), divineSpiritShield(13740, 85, 4000, 13748,
			1, 1), elysianSpiritShield(13742, 85, 4000, 13750, 1, 1), spectralSpiritShield(
			13744, 85, 4000, 13752, 1, 1);
	private SmithingTable(int id, int lvl, int xp, int bar, int amount,
			int itemAmount) {
		this.itemId = id;
		this.level = lvl;
		this.exp = xp;
		this.bar = bar;
		this.barAmount = amount;
		this.itemAmount = itemAmount;
	}

	private int itemId;
	private int level;
	private int exp;
	private int bar;
	private int barAmount;
	private int itemAmount;

	public int getItemId() {
		return this.itemId;
	}

	public int getLevel() {
		return this.level;
	}

	public int getExperience() {
		return this.exp;
	}

	public int getBarId() {
		return this.bar;
	}

	public int getBarAmount() {
		return this.barAmount;
	}

	public int getItemAmount() {
		return this.itemAmount;
	}

	private static HashMap<Integer, SmithingTable> smithMap = new HashMap<Integer, SmithingTable>();

	public static SmithingTable forId(int itemId) {
		return smithMap.get(itemId);
	}

	static {
		for (SmithingTable s : SmithingTable.values()) {
			smithMap.put(s.itemId, s);
		}
	}
}