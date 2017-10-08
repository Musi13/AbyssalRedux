package rs2.abyssalps.content.skill.smithing;

import rs2.abyssalps.model.player.Client;

public class SmithingInterface {

	public SmithingInterface(Client client, int object, String type, int x,
			int y) {
		readInput(client.playerLevel[13], type, client, x, y, object);
	}

	public static boolean doaction(Client client, int toadd, int toremove,
			int toremove2, int x, int y, int objectid, int xp) {
		return false;
	}

	public static void readInput(int level, String type, Client client,
			int objectx, int objecty, int objectid) {
		if (type.equals("BRONZE")) {
			MakeBronzeInterface(client);
		} else if (type.equals("IRON")) {
			MakeIronInterface(client);
		} else if (type.equals("STEEL")) {
			MakeSteelInterface(client);
		} else if (type.equals("MITH")) {
			MakeMithInterface(client);
		} else if (type.equals("ADDY")) {
			MakeAddyInterface(client);
		} else if (type.equals("RUNE")) {
			MakeRuneInterface(client);
		} else if (type.equals("DRAGON")) {
			makeDraconicInterface(client);
		} else if (type.equals("SPIRIT")) {
			makeSpiritShieldInterface(client);
		}

	}

	private static void makeSpiritShieldInterface(Client client) {
		client.getPA().sendFrame126("", 1112);
		client.getPA().sendFrame126("", 1109);
		client.getPA().sendFrame126("", 1110);
		client.getPA().sendFrame126("", 1118);
		client.getPA().sendFrame126("", 1111);
		client.getPA().sendFrame126("", 1095);
		client.getPA().sendFrame126("", 1115);
		client.getPA().sendFrame126("", 1090);
		client.getPA().sendFrame126("", 1113);
		client.getPA().sendFrame126("", 1116);
		client.getPA().sendFrame126("", 1114);
		client.getPA().sendFrame126("", 1089);
		client.getPA().sendFrame126("", 8428);
		client.getPA().sendFrame126("", 1124);
		client.getPA().sendFrame126("", 1125);
		client.getPA().sendFrame126("", 1126);
		client.getPA().sendFrame126("", 1127);
		client.getPA().sendFrame126("", 1128);
		client.getPA().sendFrame126("", 1129);
		client.getPA().sendFrame126("", 1130);
		client.getPA().sendFrame126("", 1131);
		client.getPA().sendFrame126("", 13357);
		client.getPA().sendFrame126("", 11459);
		client.getPA().sendFrame126("", 1101);
		client.getPA().sendFrame126("", 1099);
		client.getPA().sendFrame126("", 1100);
		client.getPA().sendFrame126("", 1088);
		client.getPA().sendFrame126("", 8429);
		client.getPA().sendFrame126("", 1105);
		client.getPA().sendFrame126("", 1098);
		client.getPA().sendFrame126("", 1092);
		client.getPA().sendFrame126("", 1083);
		client.getPA().sendFrame126("", 1104);
		client.getPA().sendFrame126("", 1103);
		client.getPA().sendFrame126("", 1106);
		client.getPA().sendFrame126("", 1086);
		client.getPA().sendFrame126("", 1087);
		client.getPA().sendFrame126("", 1108);
		client.getPA().sendFrame126("", 1085);
		client.getPA().sendFrame126("", 1107);
		client.getPA().sendFrame126("", 13358);
		client.getPA().sendFrame126("", 1102);
		client.getPA().sendFrame126("", 1093);
		client.getPA().sendFrame126(
				GetForlvl(85, client) + "Arcane" + GetForlvl(1, client), 1094);
		client.getPA().sendFrame126(
				GetForlvl(85, client) + "Divine" + GetForlvl(2, client), 1091);
		client.getPA().sendFrame126(
				GetForlvl(85, client) + "Elysian" + GetForlvl(3, client), 1098);
		client.getPA()
				.sendFrame126(
						GetForlvl(85, client) + "Spectral"
								+ GetForlvl(3, client), 1102);
		client.getPA().sendFrame34(13738, 0, 1119, 1); // dagger
		client.getPA().sendFrame34(13740, 0, 1120, 1); // axe
		client.getPA().sendFrame34(13742, 0, 1121, 1); // chain body
		client.getPA().sendFrame34(13744, 0, 1122, 1); // med helm
		client.getPA().sendFrame34(-1, 0, 1123, 0); // dart tips
		client.getPA().sendFrame34(-1, 1, 1119, 0); // dagger
		client.getPA().sendFrame34(-1, 1, 1120, 0); // axe
		client.getPA().sendFrame34(-1, 1, 1121, 0); // chain body
		client.getPA().sendFrame34(-1, 1, 1122, 0); // med helm
		client.getPA().sendFrame34(-1, 1, 1123, 0); // dart tips
		client.getPA().sendFrame34(-1, 2, 1119, 0); // dagger
		client.getPA().sendFrame34(-1, 2, 1120, 0); // axe
		client.getPA().sendFrame34(-1, 2, 1121, 0); // chain body
		client.getPA().sendFrame34(-1, 2, 1122, 0); // med helm
		client.getPA().sendFrame34(-1, 2, 1123, 0); // dart tips
		client.getPA().sendFrame34(-1, 3, 1119, 0); // dagger
		client.getPA().sendFrame34(-1, 3, 1120, 0); // axe
		client.getPA().sendFrame34(-1, 3, 1121, 0); // chain body
		client.getPA().sendFrame34(-1, 3, 1122, 0); // med helm
		client.getPA().sendFrame34(-1, 3, 1123, 0); // dart tips
		client.getPA().sendFrame34(-1, 4, 1119, 0); // dagger
		client.getPA().sendFrame34(-1, 4, 1120, 0); // axe
		client.getPA().sendFrame34(-1, 4, 1121, 0); // chain body
		client.getPA().sendFrame34(-1, 4, 1122, 0); // med helm
		client.getPA().sendFrame34(-1, 4, 1123, 0); // dart tips
		client.getPA().sendFrame126("", 1135);
		client.getPA().sendFrame126("", 1134);
		client.getPA().sendFrame126("", 11461);
		client.getPA().sendFrame126("", 11459);
		client.getPA().sendFrame126("", 1132);
		client.getPA().sendFrame126("", 1096);
		client.getOutStream().createFrame(97);
		client.getOutStream().writeWord(994);
	}

	private static void makeDraconicInterface(Client client) {
		client.getPA().sendFrame126("", 1112);
		client.getPA().sendFrame126("", 1109);
		client.getPA().sendFrame126("", 1110);
		client.getPA().sendFrame126("", 1118);
		client.getPA().sendFrame126("", 1111);
		client.getPA().sendFrame126("", 1095);
		client.getPA().sendFrame126("", 1115);
		client.getPA().sendFrame126("", 1090);
		client.getPA().sendFrame126("", 1113);
		client.getPA().sendFrame126("", 1116);
		client.getPA().sendFrame126("", 1114);
		client.getPA().sendFrame126("", 1089);
		client.getPA().sendFrame126("", 8428);
		client.getPA().sendFrame126("", 1124);
		client.getPA().sendFrame126("", 1125);
		client.getPA().sendFrame126("", 1126);
		client.getPA().sendFrame126("", 1127);
		client.getPA().sendFrame126("", 1128);
		client.getPA().sendFrame126("", 1129);
		client.getPA().sendFrame126("", 1130);
		client.getPA().sendFrame126("", 1131);
		client.getPA().sendFrame126("", 13357);
		client.getPA().sendFrame126("", 11459);
		client.getPA().sendFrame126("", 1101);
		client.getPA().sendFrame126("", 1099);
		client.getPA().sendFrame126("", 1100);
		client.getPA().sendFrame126("", 1088);
		client.getPA().sendFrame126("", 8429);
		client.getPA().sendFrame126("", 1105);
		client.getPA().sendFrame126("", 1098);
		client.getPA().sendFrame126("", 1092);
		client.getPA().sendFrame126("", 1083);
		client.getPA().sendFrame126("", 1104);
		client.getPA().sendFrame126("", 1103);
		client.getPA().sendFrame126("", 1106);
		client.getPA().sendFrame126("", 1086);
		client.getPA().sendFrame126("", 1087);
		client.getPA().sendFrame126("", 1108);
		client.getPA().sendFrame126("", 1085);
		client.getPA().sendFrame126("", 1107);
		client.getPA().sendFrame126("", 13358);
		client.getPA().sendFrame126("", 1102);
		client.getPA().sendFrame126("", 1093);
		client.getPA().sendFrame126(
				GetForlvl(90, client) + "Dragonfire Shield"
						+ GetForlvl(1, client), 1094);
		client.getPA().sendFrame126("", 1091);
		client.getPA().sendFrame34(11283, 0, 1119, 1); // dagger
		client.getPA().sendFrame34(-1, 0, 1120, 0); // axe
		client.getPA().sendFrame34(-1, 0, 1121, 0); // chain body
		client.getPA().sendFrame34(-1, 0, 1122, 0); // med helm
		client.getPA().sendFrame34(-1, 0, 1123, 0); // dart tips
		client.getPA().sendFrame34(-1, 1, 1119, 0); // dagger
		client.getPA().sendFrame34(-1, 1, 1120, 0); // axe
		client.getPA().sendFrame34(-1, 1, 1121, 0); // chain body
		client.getPA().sendFrame34(-1, 1, 1122, 0); // med helm
		client.getPA().sendFrame34(-1, 1, 1123, 0); // dart tips
		client.getPA().sendFrame34(-1, 2, 1119, 0); // dagger
		client.getPA().sendFrame34(-1, 2, 1120, 0); // axe
		client.getPA().sendFrame34(-1, 2, 1121, 0); // chain body
		client.getPA().sendFrame34(-1, 2, 1122, 0); // med helm
		client.getPA().sendFrame34(-1, 2, 1123, 0); // dart tips
		client.getPA().sendFrame34(-1, 3, 1119, 0); // dagger
		client.getPA().sendFrame34(-1, 3, 1120, 0); // axe
		client.getPA().sendFrame34(-1, 3, 1121, 0); // chain body
		client.getPA().sendFrame34(-1, 3, 1122, 0); // med helm
		client.getPA().sendFrame34(-1, 3, 1123, 0); // dart tips
		client.getPA().sendFrame34(-1, 4, 1119, 0); // dagger
		client.getPA().sendFrame34(-1, 4, 1120, 0); // axe
		client.getPA().sendFrame34(-1, 4, 1121, 0); // chain body
		client.getPA().sendFrame34(-1, 4, 1122, 0); // med helm
		client.getPA().sendFrame34(-1, 4, 1123, 0); // dart tips
		client.getPA().sendFrame126("", 1135);
		client.getPA().sendFrame126("", 1134);
		client.getPA().sendFrame126("", 11461);
		client.getPA().sendFrame126("", 11459);
		client.getPA().sendFrame126("", 1132);
		client.getPA().sendFrame126("", 1096);
		client.getOutStream().createFrame(97);
		client.getOutStream().writeWord(994);
	}

	private static void MakeRuneInterface(Client client) {
		String fiveb = GetForBars(2363, 5, client);
		String threeb = GetForBars(2363, 3, client);
		String twob = GetForBars(2363, 2, client);
		String oneb = GetForBars(2363, 1, client);
		client.getPA().sendFrame126(fiveb + "5 Bars" + fiveb, 1112);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1109);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1110);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1118);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1111);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1095);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1115);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1090);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1113);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1116);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1114);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1089);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 8428);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1124);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1125);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1126);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1127);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1128);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1129);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1130);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1131);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 13357);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 11459);
		client.getPA().sendFrame126(
				GetForlvl(88, client) + "Plate Body" + GetForlvl(18, client),
				1101);
		client.getPA().sendFrame126(
				GetForlvl(99, client) + "Plate Legs" + GetForlvl(16, client),
				1099);
		client.getPA().sendFrame126(
				GetForlvl(99, client) + "Plate Skirt" + GetForlvl(16, client),
				1100);
		client.getPA().sendFrame126(
				GetForlvl(99, client) + "2 Hand Sword" + GetForlvl(14, client),
				1088);
		client.getPA().sendFrame126(
				GetForlvl(98, client) + "Claws" + GetForlvl(13, client), 8429);
		client.getPA().sendFrame126(
				GetForlvl(97, client) + "Kite Shield" + GetForlvl(12, client),
				1105);
		client.getPA().sendFrame126(
				GetForlvl(96, client) + "Chain Body" + GetForlvl(11, client),
				1098);
		client.getPA().sendFrame126(
				GetForlvl(95, client) + "Battle Axe" + GetForlvl(10, client),
				1092);
		client.getPA().sendFrame126(
				GetForlvl(94, client) + "Warhammer" + GetForlvl(9, client),
				1083);
		client.getPA().sendFrame126(
				GetForlvl(93, client) + "Square Shield" + GetForlvl(8, client),
				1104);
		client.getPA().sendFrame126(
				GetForlvl(92, client) + "Full Helm" + GetForlvl(7, client),
				1103);
		client.getPA().sendFrame126(
				GetForlvl(92, client) + "Throwing Knives"
						+ GetForlvl(7, client), 1106);
		client.getPA().sendFrame126(
				GetForlvl(91, client) + "Long Sword" + GetForlvl(6, client),
				1086);
		client.getPA()
				.sendFrame126(
						GetForlvl(90, client) + "Scimitar"
								+ GetForlvl(5, client), 1087);
		client.getPA().sendFrame126(
				GetForlvl(90, client) + "Arrowtips" + GetForlvl(5, client),
				1108);
		client.getPA().sendFrame126(
				GetForlvl(89, client) + "Sword" + GetForlvl(4, client), 1085);
		client.getPA().sendFrame126(
				GetForlvl(89, client) + "Dart Tips" + GetForlvl(4, client),
				1107);
		client.getPA().sendFrame126(
				GetForlvl(89, client) + "Nails" + GetForlvl(4, client), 13358);
		client.getPA().sendFrame126(
				GetForlvl(88, client) + "Medium Helm" + GetForlvl(3, client),
				1102);
		client.getPA().sendFrame126(
				GetForlvl(87, client) + "Mace" + GetForlvl(2, client), 1093);
		client.getPA().sendFrame126(
				GetForlvl(85, client) + "Dagger" + GetForlvl(1, client), 1094);
		client.getPA().sendFrame126(
				GetForlvl(86, client) + "Axe" + GetForlvl(1, client), 1091);
		client.getPA().sendFrame34(1213, 0, 1119, 1); // dagger
		client.getPA().sendFrame34(1359, 0, 1120, 1); // axe
		client.getPA().sendFrame34(1113, 0, 1121, 1); // chain body
		client.getPA().sendFrame34(1147, 0, 1122, 1); // med helm
		client.getPA().sendFrame34(824, 0, 1123, 10); // dart tips
		client.getPA().sendFrame34(1289, 1, 1119, 1); // s-sword
		client.getPA().sendFrame34(1432, 1, 1120, 1); // mace
		client.getPA().sendFrame34(1079, 1, 1121, 1); // platelegs
		client.getPA().sendFrame34(1163, 1, 1122, 1); // full helm
		client.getPA().sendFrame34(44, 1, 1123, 15); // arrowtips
		client.getPA().sendFrame34(1333, 2, 1119, 1); // scimmy
		client.getPA().sendFrame34(1347, 2, 1120, 1); // warhammer
		client.getPA().sendFrame34(1093, 2, 1121, 1); // plateskirt
		client.getPA().sendFrame34(1185, 2, 1122, 1); // Sq. Shield
		client.getPA().sendFrame34(868, 2, 1123, 5); // throwing-knives
		client.getPA().sendFrame34(1303, 3, 1119, 1); // longsword
		client.getPA().sendFrame34(1373, 3, 1120, 1); // battleaxe
		client.getPA().sendFrame34(1127, 3, 1121, 1); // platebody
		client.getPA().sendFrame34(1201, 3, 1122, 1); // kiteshield
		client.getPA().sendFrame34(1319, 4, 1119, 1); // 2h sword
		client.getPA().sendFrame34(3101, 4, 1120, 1); // claws
		client.getPA().sendFrame34(4824, 4, 1122, 15); // nails
		client.getPA().sendFrame34(-1, 3, 1123, 1);
		client.getPA().sendFrame126("", 1135);
		client.getPA().sendFrame126("", 1134);
		client.getPA().sendFrame126("", 11461);
		client.getPA().sendFrame126("", 11459);
		client.getPA().sendFrame126("", 1132);
		client.getPA().sendFrame126("", 1096);
		client.getOutStream().createFrame(97);
		client.getOutStream().writeWord(994);
	}

	private static void MakeAddyInterface(Client client) {
		String fiveb = GetForBars(2361, 5, client);
		String threeb = GetForBars(2361, 3, client);
		String twob = GetForBars(2361, 2, client);
		String oneb = GetForBars(2361, 1, client);
		client.getPA().sendFrame126(fiveb + "5 Bars" + fiveb, 1112);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1109);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1110);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1118);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1111);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1095);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1115);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1090);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1113);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1116);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1114);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1089);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 8428);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1124);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1125);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1126);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1127);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1128);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1129);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1130);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1131);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 13357);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 11459);
		client.getPA().sendFrame126(
				GetForlvl(88, client) + "Plate Body" + GetForlvl(18, client),
				1101);
		client.getPA().sendFrame126(
				GetForlvl(86, client) + "Plate Legs" + GetForlvl(16, client),
				1099);
		client.getPA().sendFrame126(
				GetForlvl(86, client) + "Plate Skirt" + GetForlvl(16, client),
				1100);
		client.getPA().sendFrame126(
				GetForlvl(84, client) + "2 Hand Sword" + GetForlvl(14, client),
				1088);
		client.getPA().sendFrame126(
				GetForlvl(83, client) + "Claws" + GetForlvl(13, client), 8429);
		client.getPA().sendFrame126(
				GetForlvl(82, client) + "Kite Shield" + GetForlvl(12, client),
				1105);
		client.getPA().sendFrame126(
				GetForlvl(81, client) + "Chain Body" + GetForlvl(11, client),
				1098);
		client.getPA().sendFrame126(
				GetForlvl(80, client) + "Battle Axe" + GetForlvl(10, client),
				1092);
		client.getPA().sendFrame126(
				GetForlvl(79, client) + "Warhammer" + GetForlvl(9, client),
				1083);
		client.getPA().sendFrame126(
				GetForlvl(78, client) + "Square Shield" + GetForlvl(8, client),
				1104);
		client.getPA().sendFrame126(
				GetForlvl(77, client) + "Full Helm" + GetForlvl(7, client),
				1103);
		client.getPA().sendFrame126(
				GetForlvl(77, client) + "Throwing Knives"
						+ GetForlvl(7, client), 1106);
		client.getPA().sendFrame126(
				GetForlvl(76, client) + "Long Sword" + GetForlvl(6, client),
				1086);
		client.getPA()
				.sendFrame126(
						GetForlvl(75, client) + "Scimitar"
								+ GetForlvl(5, client), 1087);
		client.getPA().sendFrame126(
				GetForlvl(75, client) + "Arrowtips" + GetForlvl(5, client),
				1108);
		client.getPA().sendFrame126(
				GetForlvl(74, client) + "Sword" + GetForlvl(4, client), 1085);
		client.getPA().sendFrame126(
				GetForlvl(74, client) + "Dart Tips" + GetForlvl(4, client),
				1107);
		client.getPA().sendFrame126(
				GetForlvl(74, client) + "Nails" + GetForlvl(4, client), 13358);
		client.getPA().sendFrame126(
				GetForlvl(73, client) + "Medium Helm" + GetForlvl(3, client),
				1102);
		client.getPA().sendFrame126(
				GetForlvl(72, client) + "Mace" + GetForlvl(2, client), 1093);
		client.getPA().sendFrame126(
				GetForlvl(70, client) + "Dagger" + GetForlvl(1, client), 1094);
		client.getPA().sendFrame126(
				GetForlvl(71, client) + "Axe" + GetForlvl(1, client), 1091);
		client.getPA().sendFrame34(1211, 0, 1119, 1); // dagger
		client.getPA().sendFrame34(1357, 0, 1120, 1); // axe
		client.getPA().sendFrame34(1111, 0, 1121, 1); // chain body
		client.getPA().sendFrame34(1145, 0, 1122, 1); // med helm
		client.getPA().sendFrame34(823, 0, 1123, 10); // dart tips
		client.getPA().sendFrame34(1287, 1, 1119, 1); // s-sword
		client.getPA().sendFrame34(1430, 1, 1120, 1); // mace
		client.getPA().sendFrame34(1073, 1, 1121, 1); // platelegs
		client.getPA().sendFrame34(1161, 1, 1122, 1); // full helm
		client.getPA().sendFrame34(43, 1, 1123, 15); // arrowtips
		client.getPA().sendFrame34(1331, 2, 1119, 1); // scimmy
		client.getPA().sendFrame34(1345, 2, 1120, 1); // warhammer
		client.getPA().sendFrame34(1091, 2, 1121, 1); // plateskirt
		client.getPA().sendFrame34(1183, 2, 1122, 1); // Sq. Shield
		client.getPA().sendFrame34(867, 2, 1123, 5); // throwing-knives
		client.getPA().sendFrame34(1301, 3, 1119, 1); // longsword
		client.getPA().sendFrame34(1371, 3, 1120, 1); // battleaxe
		client.getPA().sendFrame34(1123, 3, 1121, 1); // platebody
		client.getPA().sendFrame34(1199, 3, 1122, 1); // kiteshield
		client.getPA().sendFrame34(1317, 4, 1119, 1); // 2h sword
		client.getPA().sendFrame34(3100, 4, 1120, 1); // claws
		client.getPA().sendFrame34(4823, 4, 1122, 15); // nails
		client.getPA().sendFrame34(-1, 3, 1123, 1);
		client.getPA().sendFrame126("", 1135);
		client.getPA().sendFrame126("", 1134);
		client.getPA().sendFrame126("", 11461);
		client.getPA().sendFrame126("", 11459);
		client.getPA().sendFrame126("", 1132);
		client.getPA().sendFrame126("", 1096);
		client.getOutStream().createFrame(97);
		client.getOutStream().writeWord(994);
	}

	private static void MakeMithInterface(Client client) {
		String fiveb = GetForBars(2359, 5, client);
		String threeb = GetForBars(2359, 3, client);
		String twob = GetForBars(2359, 2, client);
		String oneb = GetForBars(2359, 1, client);
		client.getPA().sendFrame126(fiveb + "5 Bars" + fiveb, 1112);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1109);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1110);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1118);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1111);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1095);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1115);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1090);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1113);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1116);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1114);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1089);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 8428);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1124);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1125);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1126);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1127);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1128);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1129);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1130);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1131);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 13357);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 11459);
		client.getPA().sendFrame126(
				GetForlvl(68, client) + "Plate Body" + GetForlvl(18, client),
				1101);
		client.getPA().sendFrame126(
				GetForlvl(66, client) + "Plate Legs" + GetForlvl(16, client),
				1099);
		client.getPA().sendFrame126(
				GetForlvl(66, client) + "Plate Skirt" + GetForlvl(16, client),
				1100);
		client.getPA().sendFrame126(
				GetForlvl(64, client) + "2 Hand Sword" + GetForlvl(14, client),
				1088);
		client.getPA().sendFrame126(
				GetForlvl(63, client) + "Claws" + GetForlvl(13, client), 8429);
		client.getPA().sendFrame126(
				GetForlvl(62, client) + "Kite Shield" + GetForlvl(12, client),
				1105);
		client.getPA().sendFrame126(
				GetForlvl(61, client) + "Chain Body" + GetForlvl(11, client),
				1098);
		client.getPA().sendFrame126(
				GetForlvl(60, client) + "Battle Axe" + GetForlvl(10, client),
				1092);
		client.getPA().sendFrame126(
				GetForlvl(59, client) + "Warhammer" + GetForlvl(9, client),
				1083);
		client.getPA().sendFrame126(
				GetForlvl(58, client) + "Square Shield" + GetForlvl(8, client),
				1104);
		client.getPA().sendFrame126(
				GetForlvl(57, client) + "Full Helm" + GetForlvl(7, client),
				1103);
		client.getPA().sendFrame126(
				GetForlvl(57, client) + "Throwing Knives"
						+ GetForlvl(7, client), 1106);
		client.getPA().sendFrame126(
				GetForlvl(56, client) + "Long Sword" + GetForlvl(6, client),
				1086);
		client.getPA()
				.sendFrame126(
						GetForlvl(55, client) + "Scimitar"
								+ GetForlvl(5, client), 1087);
		client.getPA().sendFrame126(
				GetForlvl(55, client) + "Arrowtips" + GetForlvl(5, client),
				1108);
		client.getPA().sendFrame126(
				GetForlvl(54, client) + "Sword" + GetForlvl(4, client), 1085);
		client.getPA().sendFrame126(
				GetForlvl(54, client) + "Dart Tips" + GetForlvl(4, client),
				1107);
		client.getPA().sendFrame126(
				GetForlvl(54, client) + "Nails" + GetForlvl(4, client), 13358);
		client.getPA().sendFrame126(
				GetForlvl(53, client) + "Medium Helm" + GetForlvl(3, client),
				1102);
		client.getPA().sendFrame126(
				GetForlvl(52, client) + "Mace" + GetForlvl(2, client), 1093);
		client.getPA().sendFrame126(
				GetForlvl(50, client) + "Dagger" + GetForlvl(1, client), 1094);
		client.getPA().sendFrame126(
				GetForlvl(51, client) + "Axe" + GetForlvl(1, client), 1091);
		client.getPA().sendFrame34(1209, 0, 1119, 1); // dagger
		client.getPA().sendFrame34(1355, 0, 1120, 1); // axe
		client.getPA().sendFrame34(1109, 0, 1121, 1); // chain body
		client.getPA().sendFrame34(1143, 0, 1122, 1); // med helm
		client.getPA().sendFrame34(822, 0, 1123, 10); // dart tips
		client.getPA().sendFrame34(1285, 1, 1119, 1); // s-sword
		client.getPA().sendFrame34(1428, 1, 1120, 1); // mace
		client.getPA().sendFrame34(1071, 1, 1121, 1); // platelegs
		client.getPA().sendFrame34(1159, 1, 1122, 1); // full helm
		client.getPA().sendFrame34(42, 1, 1123, 15); // arrowtips
		client.getPA().sendFrame34(1329, 2, 1119, 1); // scimmy
		client.getPA().sendFrame34(1343, 2, 1120, 1); // warhammer
		client.getPA().sendFrame34(1085, 2, 1121, 1); // plateskirt
		client.getPA().sendFrame34(1181, 2, 1122, 1); // Sq. Shield
		client.getPA().sendFrame34(866, 2, 1123, 5); // throwing-knives
		client.getPA().sendFrame34(1299, 3, 1119, 1); // longsword
		client.getPA().sendFrame34(1369, 3, 1120, 1); // battleaxe
		client.getPA().sendFrame34(1121, 3, 1121, 1); // platebody
		client.getPA().sendFrame34(1197, 3, 1122, 1); // kiteshield
		client.getPA().sendFrame34(1315, 4, 1119, 1); // 2h sword
		client.getPA().sendFrame34(3099, 4, 1120, 1); // claws
		client.getPA().sendFrame34(4822, 4, 1122, 15); // nails
		client.getPA().sendFrame34(-1, 3, 1123, 1);
		client.getPA().sendFrame126("", 1135);
		client.getPA().sendFrame126("", 1134);
		client.getPA().sendFrame126("", 11461);
		client.getPA().sendFrame126("", 11459);
		client.getPA().sendFrame126("", 1132);
		client.getPA().sendFrame126("", 1096);
		client.getOutStream().createFrame(97);
		client.getOutStream().writeWord(994);
	}

	private static void MakeSteelInterface(Client client) {
		String fiveb = GetForBars(2353, 5, client);
		String threeb = GetForBars(2353, 3, client);
		String twob = GetForBars(2353, 2, client);
		String oneb = GetForBars(2353, 1, client);
		client.getPA().sendFrame126(fiveb + "5 Bars" + fiveb, 1112);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1109);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1110);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1118);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1111);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1095);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1115);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1090);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1113);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1116);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1114);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1089);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 8428);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1124);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1125);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1126);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1127);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1128);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1129);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1130);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1131);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 13357);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1132);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1135);
		client.getPA().sendFrame126("", 11459);
		client.getPA().sendFrame126(
				GetForlvl(48, client) + "Plate Body" + GetForlvl(18, client),
				1101);
		client.getPA().sendFrame126(
				GetForlvl(46, client) + "Plate Legs" + GetForlvl(16, client),
				1099);
		client.getPA().sendFrame126(
				GetForlvl(46, client) + "Plate Skirt" + GetForlvl(16, client),
				1100);
		client.getPA().sendFrame126(
				GetForlvl(44, client) + "2 Hand Sword" + GetForlvl(14, client),
				1088);
		client.getPA().sendFrame126(
				GetForlvl(43, client) + "Claws" + GetForlvl(13, client), 8429);
		client.getPA().sendFrame126(
				GetForlvl(42, client) + "Kite Shield" + GetForlvl(12, client),
				1105);
		client.getPA().sendFrame126(
				GetForlvl(41, client) + "Chain Body" + GetForlvl(11, client),
				1098);
		client.getPA().sendFrame126("", 11461);
		client.getPA().sendFrame126(
				GetForlvl(40, client) + "Battle Axe" + GetForlvl(10, client),
				1092);
		client.getPA().sendFrame126(
				GetForlvl(39, client) + "Warhammer" + GetForlvl(9, client),
				1083);
		client.getPA().sendFrame126(
				GetForlvl(38, client) + "Square Shield" + GetForlvl(8, client),
				1104);
		client.getPA().sendFrame126(
				GetForlvl(37, client) + "Full Helm" + GetForlvl(7, client),
				1103);
		client.getPA().sendFrame126(
				GetForlvl(37, client) + "Throwing Knives"
						+ GetForlvl(7, client), 1106);
		client.getPA().sendFrame126(
				GetForlvl(36, client) + "Long Sword" + GetForlvl(6, client),
				1086);
		client.getPA()
				.sendFrame126(
						GetForlvl(35, client) + "Scimitar"
								+ GetForlvl(5, client), 1087);
		client.getPA().sendFrame126(
				GetForlvl(35, client) + "Arrowtips" + GetForlvl(5, client),
				1108);
		client.getPA().sendFrame126(
				GetForlvl(34, client) + "Sword" + GetForlvl(4, client), 1085);
		client.getPA().sendFrame126(
				GetForlvl(34, client) + "Dart Tips" + GetForlvl(4, client),
				1107);
		client.getPA().sendFrame126(
				GetForlvl(34, client) + "Nails" + GetForlvl(4, client), 13358);
		client.getPA().sendFrame126(
				GetForlvl(33, client) + "Medium Helm" + GetForlvl(3, client),
				1102);
		client.getPA().sendFrame126(
				GetForlvl(32, client) + "Mace" + GetForlvl(2, client), 1093);
		client.getPA().sendFrame126(
				GetForlvl(30, client) + "Dagger" + GetForlvl(1, client), 1094);
		client.getPA().sendFrame126(
				GetForlvl(31, client) + "Axe" + GetForlvl(1, client), 1091);
		client.getPA().sendFrame126(
				GetForlvl(35, client) + "Cannon Ball" + GetForlvl(35, client),
				1096);
		client.getPA().sendFrame126(
				GetForlvl(36, client) + "Studs" + GetForlvl(36, client), 1134);
		client.getPA().sendFrame34(1207, 0, 1119, 1);
		client.getPA().sendFrame34(1353, 0, 1120, 1);
		client.getPA().sendFrame34(1105, 0, 1121, 1);
		client.getPA().sendFrame34(1141, 0, 1122, 1);
		client.getPA().sendFrame34(821, 0, 1123, 10);
		client.getPA().sendFrame34(1281, 1, 1119, 1);
		client.getPA().sendFrame34(1424, 1, 1120, 1);
		client.getPA().sendFrame34(1069, 1, 1121, 1);
		client.getPA().sendFrame34(1157, 1, 1122, 1);
		client.getPA().sendFrame34(41, 1, 1123, 15);
		client.getPA().sendFrame34(1325, 2, 1119, 1);
		client.getPA().sendFrame34(1339, 2, 1120, 1);
		client.getPA().sendFrame34(1083, 2, 1121, 1);
		client.getPA().sendFrame34(1177, 2, 1122, 1);
		client.getPA().sendFrame34(865, 2, 1123, 5);
		client.getPA().sendFrame34(1295, 3, 1119, 1);
		client.getPA().sendFrame34(1365, 3, 1120, 1);
		client.getPA().sendFrame34(1119, 3, 1121, 1);
		client.getPA().sendFrame34(1193, 3, 1122, 1);
		client.getPA().sendFrame34(1311, 4, 1119, 1);
		client.getPA().sendFrame34(3097, 4, 1120, 1);
		client.getPA().sendFrame34(1539, 4, 1122, 15);
		client.getPA().sendFrame34(2, 3, 1123, 4);
		client.getPA().sendFrame34(2370, 4, 1123, 1);
		client.getOutStream().createFrame(97);
		client.getOutStream().writeWord(994);
	}

	private static void MakeIronInterface(Client client) {
		String fiveb = GetForBars(2351, 5, client);
		String threeb = GetForBars(2351, 3, client);
		String twob = GetForBars(2351, 2, client);
		String oneb = GetForBars(2351, 1, client);
		client.getPA().sendFrame126(fiveb + "5 Bars" + fiveb, 1112);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1109);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1110);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1118);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1111);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1095);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1115);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1090);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1113);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1116);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1114);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1089);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 8428);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1124);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1125);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1126);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1127);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1128);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1129);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1130);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1131);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 13357);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 11459);
		client.getPA().sendFrame126(
				GetForlvl(33, client) + "Plate Body" + GetForlvl(18, client),
				1101);
		client.getPA().sendFrame126(
				GetForlvl(31, client) + "Plate Legs" + GetForlvl(16, client),
				1099);
		client.getPA().sendFrame126(
				GetForlvl(31, client) + "Plate Skirt" + GetForlvl(16, client),
				1100);
		client.getPA().sendFrame126(
				GetForlvl(29, client) + "2 Hand Sword" + GetForlvl(14, client),
				1088);
		client.getPA().sendFrame126(
				GetForlvl(28, client) + "Claws" + GetForlvl(13, client), 8429);
		client.getPA().sendFrame126(
				GetForlvl(27, client) + "Kite Shield" + GetForlvl(12, client),
				1105);
		client.getPA().sendFrame126(
				GetForlvl(26, client) + "Chain Body" + GetForlvl(11, client),
				1098);
		client.getPA().sendFrame126(
				GetForlvl(26, client) + "Oil Lantern Frame"
						+ GetForlvl(11, client), 11461);
		client.getPA().sendFrame126(
				GetForlvl(25, client) + "Battle Axe" + GetForlvl(10, client),
				1092);
		client.getPA().sendFrame126(
				GetForlvl(24, client) + "Warhammer" + GetForlvl(9, client),
				1083);
		client.getPA().sendFrame126(
				GetForlvl(23, client) + "Square Shield" + GetForlvl(8, client),
				1104);
		client.getPA().sendFrame126(
				GetForlvl(22, client) + "Full Helm" + GetForlvl(7, client),
				1103);
		client.getPA().sendFrame126(
				GetForlvl(21, client) + "Throwing Knives"
						+ GetForlvl(7, client), 1106);
		client.getPA().sendFrame126(
				GetForlvl(21, client) + "Long Sword" + GetForlvl(6, client),
				1086);
		client.getPA()
				.sendFrame126(
						GetForlvl(20, client) + "Scimitar"
								+ GetForlvl(5, client), 1087);
		client.getPA().sendFrame126(
				GetForlvl(20, client) + "Arrowtips" + GetForlvl(5, client),
				1108);
		client.getPA().sendFrame126(
				GetForlvl(19, client) + "Sword" + GetForlvl(4, client), 1085);
		client.getPA().sendFrame126(
				GetForlvl(19, client) + "Dart Tips" + GetForlvl(4, client),
				1107);
		client.getPA().sendFrame126(
				GetForlvl(19, client) + "Nails" + GetForlvl(4, client), 13358);
		client.getPA().sendFrame126(
				GetForlvl(18, client) + "Medium Helm" + GetForlvl(3, client),
				1102);
		client.getPA().sendFrame126(
				GetForlvl(17, client) + "Mace" + GetForlvl(2, client), 1093);
		client.getPA().sendFrame126(
				GetForlvl(15, client) + "Dagger" + GetForlvl(1, client), 1094);
		client.getPA().sendFrame126(
				GetForlvl(16, client) + "Axe" + GetForlvl(1, client), 1091);
		client.getPA().sendFrame34(1203, 0, 1119, 1);
		client.getPA().sendFrame34(1349, 0, 1120, 1);
		client.getPA().sendFrame34(1101, 0, 1121, 1);
		client.getPA().sendFrame34(1137, 0, 1122, 1);
		client.getPA().sendFrame34(820, 0, 1123, 10);
		client.getPA().sendFrame34(1279, 1, 1119, 1);
		client.getPA().sendFrame34(1420, 1, 1120, 1);
		client.getPA().sendFrame34(1067, 1, 1121, 1);
		client.getPA().sendFrame34(1153, 1, 1122, 1);
		client.getPA().sendFrame34(40, 1, 1123, 15);
		client.getPA().sendFrame34(1323, 2, 1119, 1);
		client.getPA().sendFrame34(1335, 2, 1120, 1);
		client.getPA().sendFrame34(1081, 2, 1121, 1);
		client.getPA().sendFrame34(1175, 2, 1122, 1);
		client.getPA().sendFrame34(863, 2, 1123, 5);
		client.getPA().sendFrame34(1293, 3, 1119, 1);
		client.getPA().sendFrame34(1363, 3, 1120, 1);
		client.getPA().sendFrame34(1115, 3, 1121, 1);
		client.getPA().sendFrame34(1191, 3, 1122, 1);
		client.getPA().sendFrame34(1309, 4, 1119, 1);
		client.getPA().sendFrame34(3096, 4, 1120, 1);
		client.getPA().sendFrame34(4820, 4, 1122, 15);
		client.getPA().sendFrame34(4540, 4, 1121, 1);
		client.getPA().sendFrame34(-1, 3, 1123, 1);
		client.getPA().sendFrame126("", 1135);
		client.getPA().sendFrame126("", 1134);
		client.getPA().sendFrame126("", 1132);
		client.getPA().sendFrame126("", 1096);
		client.getOutStream().createFrame(97);
		client.getOutStream().writeWord(994);
	}

	private static void MakeBronzeInterface(Client client) {
		String fiveb = GetForBars(2349, 5, client);
		String threeb = GetForBars(2349, 3, client);
		String twob = GetForBars(2349, 2, client);
		String oneb = GetForBars(2349, 1, client);
		client.getPA().sendFrame126(fiveb + "5 Bars" + fiveb, 1112);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1109);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1110);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1118);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1111);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1095);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1115);
		client.getPA().sendFrame126(threeb + "3 Bars" + threeb, 1090);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1113);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1116);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1114);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 1089);
		client.getPA().sendFrame126(twob + "2 Bars" + twob, 8428);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1124);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1125);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1126);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1127);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1128);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1129);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1130);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 1131);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 13357);
		client.getPA().sendFrame126(oneb + "1 Bar" + oneb, 11459);
		client.getPA().sendFrame126(
				GetForlvl(18, client) + "Plate Body" + GetForlvl(18, client),
				1101);
		client.getPA().sendFrame126(
				GetForlvl(16, client) + "Plate Legs" + GetForlvl(16, client),
				1099);
		client.getPA().sendFrame126(
				GetForlvl(16, client) + "Plate Skirt" + GetForlvl(16, client),
				1100);
		client.getPA().sendFrame126(
				GetForlvl(14, client) + "2 Hand Sword" + GetForlvl(14, client),
				1088);
		client.getPA().sendFrame126(
				GetForlvl(13, client) + "Claws" + GetForlvl(13, client), 8429);
		client.getPA().sendFrame126(
				GetForlvl(12, client) + "Kite Shield" + GetForlvl(12, client),
				1105);
		client.getPA().sendFrame126(
				GetForlvl(11, client) + "Chain Body" + GetForlvl(11, client),
				1098);
		client.getPA().sendFrame126(
				GetForlvl(10, client) + "Battle Axe" + GetForlvl(10, client),
				1092);
		client.getPA()
				.sendFrame126(
						GetForlvl(9, client) + "Warhammer"
								+ GetForlvl(9, client), 1083);
		client.getPA().sendFrame126(
				GetForlvl(8, client) + "Square Shield" + GetForlvl(8, client),
				1104);
		client.getPA()
				.sendFrame126(
						GetForlvl(7, client) + "Full Helm"
								+ GetForlvl(7, client), 1103);
		client.getPA()
				.sendFrame126(
						GetForlvl(7, client) + "Throwing Knives"
								+ GetForlvl(7, client), 1106);
		client.getPA().sendFrame126(
				GetForlvl(6, client) + "Long Sword" + GetForlvl(6, client),
				1086);
		client.getPA().sendFrame126(
				GetForlvl(5, client) + "Scimitar" + GetForlvl(5, client), 1087);
		client.getPA()
				.sendFrame126(
						GetForlvl(5, client) + "Arrowtips"
								+ GetForlvl(5, client), 1108);
		client.getPA().sendFrame126(
				GetForlvl(4, client) + "Sword" + GetForlvl(4, client), 1085);
		client.getPA()
				.sendFrame126(
						GetForlvl(4, client) + "Dart Tips"
								+ GetForlvl(4, client), 1107);
		client.getPA().sendFrame126(
				GetForlvl(4, client) + "Nails" + GetForlvl(4, client), 13358);
		client.getPA().sendFrame126(
				GetForlvl(3, client) + "Medium Helm" + GetForlvl(3, client),
				1102);
		client.getPA().sendFrame126(
				GetForlvl(2, client) + "Mace" + GetForlvl(2, client), 1093);
		client.getPA().sendFrame126(
				GetForlvl(1, client) + "Dagger" + GetForlvl(1, client), 1094);
		client.getPA().sendFrame126(
				GetForlvl(1, client) + "Axe" + GetForlvl(1, client), 1091);
		client.getPA().sendFrame34(1205, 0, 1119, 1);
		client.getPA().sendFrame34(1351, 0, 1120, 1);
		client.getPA().sendFrame34(1103, 0, 1121, 1);
		client.getPA().sendFrame34(1139, 0, 1122, 1);
		client.getPA().sendFrame34(819, 0, 1123, 10);
		client.getPA().sendFrame34(1277, 1, 1119, 1);
		client.getPA().sendFrame34(1422, 1, 1120, 1);
		client.getPA().sendFrame34(1075, 1, 1121, 1);
		client.getPA().sendFrame34(1155, 1, 1122, 1);
		client.getPA().sendFrame34(39, 1, 1123, 15);
		client.getPA().sendFrame34(1321, 2, 1119, 1);
		client.getPA().sendFrame34(1337, 2, 1120, 1);
		client.getPA().sendFrame34(1087, 2, 1121, 1);
		client.getPA().sendFrame34(1173, 2, 1122, 1);
		client.getPA().sendFrame34(864, 2, 1123, 5);
		client.getPA().sendFrame34(1291, 3, 1119, 1);
		client.getPA().sendFrame34(1375, 3, 1120, 1);
		client.getPA().sendFrame34(1117, 3, 1121, 1);
		client.getPA().sendFrame34(1189, 3, 1122, 1);
		client.getPA().sendFrame34(1307, 4, 1119, 1);
		client.getPA().sendFrame34(3095, 4, 1120, 1);
		client.getPA().sendFrame34(4819, 4, 1122, 15);
		client.getPA().sendFrame34(-1, 3, 1123, 1);
		client.getPA().sendFrame126("", 1135);
		client.getPA().sendFrame126("", 1134);
		client.getPA().sendFrame126("", 11461);
		client.getPA().sendFrame126("", 11459);
		client.getPA().sendFrame126("", 1132);
		client.getPA().sendFrame126("", 1096);
		client.getOutStream().createFrame(97);
		client.getOutStream().writeWord(994);

	}

	private static String GetForlvl(int i, Client client) {
		if (client.playerLevel[13] >= i)
			return "@whi@";

		return "@bla@";
	}

	private static String GetForBars(int i, int j, Client client) {
		if (client.getItems().playerHasItem(i, j))
			return "@gre@";

		return "@red@";
	}

}
