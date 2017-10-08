package rs2.abyssalps.model.items.defs;

import rs2.abyssalps.model.items.ItemAssistant;
import rs2.abyssalps.model.player.Client;

public class WeaponAnimations {

	public static int attackAnimation(Client client) {
		String name = ItemAssistant.getItemName(client.playerEquipment[3])
				.toLowerCase();
		if (name.contains("staff") || name.contains("wand")) {
			return 414;
		} else if (name.startsWith("trident")) {
			if (client.npcIndex > 0) {
				client.gfx100(1251);
			}
			return 414;
		} else if (name.contains("2h") || name.endsWith("godsword")
				|| name.contains("aradomin sword")
				|| name.contains("blessed sword")) {
			return 7045;
		} else if (name.contains("torag")) {
			return 2068;
		} else if (name.contains("sword") || name.contains("scimitar")
				|| name.contains("hammer") || name.contains("battleaxe")) {
			return 451;
		} else if (name.contains("flag") || name.contains("banner")
				|| name.contains("pole")) {
			return 1427;
		} else if (name.contains("whip") || name.contains("tentacle")) {
			return 1658;
		} else if (name.equalsIgnoreCase("granite maul")) {
			return 1665;
		} else if (name.contains("dharok")) {
			return 2066;
		} else if (name.contains("verac")) {
			return 2062;
		} else if (name.contains("guthan")) {
			return 2080;
		} else if (name.contains("ket-om")) {
			return 2661;
		} else if (name.contains("anchor")) {
			return 5865;
		} else if (name.contains("karil")) {
			return 2075;
		} else if (name.endsWith("crossbow")) {
			return 4230;
		} else if (name.contains("dart") || name.contains("knife")
				|| name.contains("javelin") || name.contains("thrownaxe")) {
			return 806;
		} else if (name.endsWith("shortbow") || name.endsWith("ark bow")
				|| name.endsWith("longbow")) {
			return 426;
		} else if (name.contains("spear")) {
			return 412;
		} else if (name.contains("claws")) {
			return 393;
		} else if (name.contains("halberd") || name.contains("cane")) {
			return 440;
		} else if (name.contains("banner") || name.contains("flag")
				|| name.contains("pole")) {
			return 1428;
		} else if (name.contains("xil-ul")) {
			return 3353;
		} else if (name.endsWith("blowpipe")) {
			return 5061;
		} else if (name.contains("dagger")) {
			return 1064;
		}
		if (client.fightMode == 1)
			return 423;
		else
			return 422;
	}

	public static int standAnimation(Client client) {

		String name = ItemAssistant.getItemName(client.playerEquipment[3])
				.toLowerCase();
		if (name.contains("staff") || name.contains("wand")
				|| name.equalsIgnoreCase("dragon longsword")
				|| name.startsWith("trident")) {
			return 813;
		} else if (name.contains("2h") || name.contains("godsword")
				|| name.contains("aradomin sword")
				|| name.contains("blessed sword")) {
			return 7053;
		} else if (name.contains("scythe")) {
			return 847;
		} else if (name.contains("flag") || name.contains("banner")
				|| name.contains("pole")) {
			return 1421;
		} else if (name.equalsIgnoreCase("granite maul")) {
			return 1662;
		} else if (name.contains("dharok")) {
			return 2065;
		} else if (name.contains("verac")) {
			return 2061;
		} else if (name.contains("-ket-om")) {
			return 2065;
		} else if (name.endsWith("anchor")) {
			return 5869;
		}
		return 808;
	}

	public static int walkAnimation(Client client) {
		String name = ItemAssistant.getItemName(client.playerEquipment[3])
				.toLowerCase();
		if (name.contains("staff") || name.contains("wand")
				|| name.startsWith("trident")) {
			return 1146;
		} else if (name.contains("2h") || name.contains("godsword")
				|| name.contains("aradomin sword")
				|| name.contains("blessed sword")) {
			return 7052;
		} else if (name.contains("scythe")) {
			return 847;
		} else if (name.contains("flag") || name.contains("banner")
				|| name.contains("pole")) {
			return 1422;
		} else if (name.contains("whip") || name.contains("tentacle")) {
			return 1660;
		} else if (name.equalsIgnoreCase("granite maul")) {
			return 1663;
		} else if (name.contains("dharok")) {
			return 0x67F;
		} else if (name.contains("verac")) {
			return 2060;
		} else if (name.contains("ket-om")) {
			return 2064;
		} else if (name.contains("anchor")) {
			return 5867;
		}
		return 819;
	}

	public static int runAnimation(Client client) {
		String name = ItemAssistant.getItemName(client.playerEquipment[3])
				.toLowerCase();
		if (name.contains("staff") || name.contains("wand")
				|| name.startsWith("trident")) {
			return 1210;
		} else if (name.contains("2h") || name.contains("godsword")
				|| name.contains("aradomin sword")
				|| name.contains("blessed sword")) {
			return 7043;
		} else if (name.contains("flag") || name.contains("banner")
				|| name.contains("pole")) {
			return 1427;
		} else if (name.contains("whip") || name.contains("tentacle")) {
			return 1661;
		} else if (name.equalsIgnoreCase("granite maul")) {
			return 1664;
		} else if (name.contains("dharok")) {
			return 0x680;
		} else if (name.contains("verac")) {
			return 2060;
		} else if (name.contains("ket-om")) {
			return 1664;
		} else if (name.contains("anchor")) {
			return 5868;
		}

		return 824;
	}

	public static int blockAnimation(Client client) {
		if (client.playerEquipment[client.playerShield] > 0
				&& !ItemAssistant.getItemName(
						client.playerEquipment[client.playerShield]).endsWith(
						"book")) {
			if (ItemAssistant.getItemName(
					client.playerEquipment[client.playerShield]).endsWith(
					"defender")) {
				return 4177;
			}
			return 1156;
		}
		if (client.playerEquipment[3] > 0) {
			String name = ItemAssistant.getItemName(client.playerEquipment[3])
					.toLowerCase();
			if (name.contains("whip") || name.contains("tentacle")) {
				return 1659;
			} else if (name.equalsIgnoreCase("granite maul")) {
				return 1666;
			} else if (name.contains("verac")) {
				return 2063;
			} else if (name.contains("ahrim")) {
				return 2079;
			} else if (name.endsWith("godsword") || name.contains("2h")
					|| name.contains("aradomin sword")
					|| name.contains("blessed sword")) {
				return 7056;
			} else if (name.contains("sword") || name.contains("scimitar")) {
				return 403;
			} else if (name.contains("staff") || name.contains("wand")
					|| name.startsWith("trident")) {
				return 415;
			}
		}
		return 424;
	}

	public static void setTurnAnimations(Client client) {
		String name = ItemAssistant.getItemName(client.playerEquipment[3])
				.toLowerCase();
		if (name.endsWith("godsword") || name.contains("2h")
				|| name.endsWith("blessed sword")
				|| name.contains("aradomin sword")) {
			setTurnIndexes(client, 7044, 7047, 7048, 7049);
			return;
		}
		if (name.contains("staff") || name.contains("wand")
				|| name.startsWith("trident")) {
			setTurnIndexes(client, 1207, 1208, 1209, 1206);
			return;
		}
		setTurnIndexes(client, 820, 823, 822, 821);
	}

	private static void setTurnIndexes(Client client, int turn, int turn180,
			int turn90CW, int turn90CCW) {
		client.playerTurnIndex = turn;
		client.playerTurn180Index = turn180;
		client.playerTurn90CWIndex = turn90CW;
		client.playerTurn90CCWIndex = turn90CCW;
	}

}
