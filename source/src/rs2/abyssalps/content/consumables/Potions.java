package rs2.abyssalps.content.consumables;

import java.util.HashMap;
import java.util.Objects;

import rs2.Server;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.content.multiplayer.duel.DuelSessionRules.Rule;
import rs2.abyssalps.model.Boundary;
import rs2.abyssalps.model.items.ItemAssistant;
import rs2.abyssalps.model.player.Client;

public class Potions {

	public static enum PotionData {

		SUPER_STRENGTH(new int[] { 161, 159, 157, 2440 }, new int[] { 2 },
				"super"), SUPER_ATTACK(new int[] { 149, 147, 145, 2436 },
				new int[] { 0 }, "super"), SUPER_DEFENCE(new int[] { 167, 165,
				163, 2442 }, new int[] { 1 }, "super"), SUPER_RESTORE(
				new int[] { 3030, 3028, 3026, 3024 }, new int[] { 0, 1, 2, 4,
						5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
						20 }, "super_restore"), MAGIC_POTION(new int[] { 3046,
				3044, 3042, 3040 }, new int[] { 6 }, "raise_by_4"), RANGING_POTION(
				new int[] { 173, 171, 169, 2444 }, new int[] { 4 }, "ranging"), SUPER_COMBAT(
				new int[] { 12701, 12699, 12697, 12695 },
				new int[] { 0, 1, 2 }, "super"), ANTI_VENOM(new int[] { 12911,
				12909, 12907, 12905 }, new int[] {}, "venom"), PRAYER_POTION(
				new int[] { 143, 141, 139, 2434 }, new int[] { 5 }, "prayer"), SARADOMIN_BREW(
				new int[] { 6691, 6689, 6687, 6685 }, new int[] {}, "sara_brew"), ANTI_FIRE(
				new int[] { 2458, 2456, 2454, 2452 }, new int[] {}, "antifire"), OVERLOAD(
				new int[] { 15335, 15334, 15333, 15332 }, new int[] { 0, 1, 2,
						4, 6 }, "overload"), ZAMORAK_BREW(new int[] { 193, 191,
				189, 2450 }, new int[] {}, "zamorak");

		private PotionData(int[] potionIds, int[] skills, String potionType) {
			this.potionIds = potionIds;
			this.skills = skills;
			this.potionType = potionType;
		}

		private int[] potionIds;
		private int[] skills;
		private String potionType;

		public int[] getPotionIds() {
			return this.potionIds;
		}

		public int[] getSkills() {
			return this.skills;
		}

		private static HashMap<Integer, PotionData> potionMap = new HashMap<Integer, PotionData>();

		public static PotionData forId(int id) {
			return potionMap.get(id);
		}

		static {
			for (PotionData d : PotionData.values()) {
				for (int i = 0; i < d.getPotionIds().length; i++) {
					potionMap.put(d.getPotionIds()[i], d);
				}
			}
		}

	}

	public static boolean isPotion(int id) {
		return PotionData.forId(id) == null ? false : true;
	}

	public static void drinkPotion(Client client, int itemId, int itemSlot) {
		PotionData p = PotionData.forId(itemId);
		if (p == null) {
			return;
		}
		if (System.currentTimeMillis() - client.potDelay <= 1200
				|| client.playerLevel[3] <= 0 || client.isDead) {
			return;
		}
		if (Boundary.isIn(client, Boundary.DUEL_ARENAS)) {
			DuelSession session = (DuelSession) Server
					.getMultiplayerSessionListener().getMultiplayerSession(
							client, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(session)) {
				if (session.getRules().contains(Rule.NO_DRINKS)) {
					client.sendMessage("Drinks have been disabled for this duel.");
					return;
				}
			}
		}

		client.getCombat().resetPlayerAttack();
		client.attackTimer += 1;

		client.startAnimation(829);

		client.getItems().deleteItem(itemId, itemSlot, 1);

		client.sendMessage("You drink some of your "
				+ ItemAssistant.getItemName(itemId).toLowerCase()
						.replaceAll(" potion", "").replaceAll("(0)", "")
						.replaceAll("(1)", "").replaceAll("(2)", "")
						.replaceAll("(3)", "").replaceAll("(4)", "")
						.replace("()", "") + " potion.");
		switch (p.potionType) {

		case "venom":
			if (client.venomDamage >= 6) {
				client.venomDamage = 0;
			}
			break;

		case "super":
			for (int i = 0; i < p.getSkills().length; i++) {
				int increase = (int) (5 + client
						.getLevelForXP(client.playerXP[p.getSkills()[i]]) * .15);
				if (client.playerLevel[p.getSkills()[i]] <= client
						.getLevelForXP(client.playerXP[p.getSkills()[i]])
						+ increase) {
					client.playerLevel[p.getSkills()[i]] += increase;
					if (client.playerLevel[p.getSkills()[i]] > client
							.getLevelForXP(client.playerXP[p.getSkills()[i]])
							+ increase)
						client.playerLevel[p.getSkills()[i]] = client
								.getLevelForXP(client.playerXP[p.getSkills()[i]])
								+ increase;
				}
				client.getPA().refreshSkill(p.getSkills()[i]);
			}
			break;

		case "prayer":
			client.playerLevel[5] += (client.getLevelForXP(client.playerXP[5]) * .33);
			if (client.playerLevel[5] > client
					.getLevelForXP(client.playerXP[5]))
				client.playerLevel[5] = client
						.getLevelForXP(client.playerXP[5]);
			client.getPA().refreshSkill(5);
			break;

		case "super_restore":
			for (int i = 0; i < p.getSkills().length; i++) {
				int increase = (int) (8 + client
						.getLevelForXP(client.playerXP[p.getSkills()[i]]) * .25);
				if (client.playerLevel[p.getSkills()[i]] <= client
						.getLevelForXP(client.playerXP[p.getSkills()[i]])) {
					client.playerLevel[p.getSkills()[i]] += increase;
					if (client.playerLevel[p.getSkills()[i]] >= client
							.getLevelForXP(client.playerXP[p.getSkills()[i]]))
						client.playerLevel[p.getSkills()[i]] = client
								.getLevelForXP(client.playerXP[p.getSkills()[i]]);
				}
				client.getPA().refreshSkill(p.getSkills()[i]);
			}
			break;

		case "raise_by_4":
			for (int i = 0; i < p.getSkills().length; i++) {
				int increase = 4;
				if (client.playerLevel[p.getSkills()[i]] <= client
						.getLevelForXP(client.playerXP[p.getSkills()[i]])
						+ increase) {
					client.playerLevel[p.getSkills()[i]] += increase;
					if (client.playerLevel[p.getSkills()[i]] >= client
							.getLevelForXP(client.playerXP[p.getSkills()[i]])
							+ increase) {
						client.playerLevel[p.getSkills()[i]] = client
								.getLevelForXP(client.playerXP[p.getSkills()[i]])
								+ increase;
					}
				}
				client.getPA().refreshSkill(p.getSkills()[i]);
			}
			break;

		case "ranging":
			for (int i = 0; i < p.getSkills().length; i++) {
				int increase = (int) (4 + client
						.getLevelForXP(client.playerXP[p.getSkills()[i]]) * .10);
				if (client.playerLevel[p.getSkills()[i]] <= client
						.getLevelForXP(client.playerXP[p.getSkills()[i]])
						+ increase) {
					client.playerLevel[p.getSkills()[i]] += increase;
					if (client.playerLevel[p.getSkills()[i]] >= client
							.getLevelForXP(client.playerXP[p.getSkills()[i]])
							+ increase) {
						client.playerLevel[p.getSkills()[i]] = client
								.getLevelForXP(client.playerXP[p.getSkills()[i]])
								+ increase;
					}
				}
				client.getPA().refreshSkill(p.getSkills()[i]);
			}
			break;

		case "overload":
			for (int i = 0; i < p.getSkills().length; i++) {
				int increase = (int) (5 + client
						.getLevelForXP(client.playerXP[p.getSkills()[i]]) * .27);
				if (client.playerLevel[p.getSkills()[i]] <= client
						.getLevelForXP(client.playerXP[p.getSkills()[i]])
						+ increase) {
					client.playerLevel[p.getSkills()[i]] += increase;
					if (client.playerLevel[p.getSkills()[i]] > client
							.getLevelForXP(client.playerXP[p.getSkills()[i]])
							+ increase)
						client.playerLevel[p.getSkills()[i]] = client
								.getLevelForXP(client.playerXP[p.getSkills()[i]])
								+ increase;
				}
				client.getPA().refreshSkill(p.getSkills()[i]);
			}
			break;

		case "sara_brew":
			int[] toDecrease = { 0, 2, 4, 6 };

			for (int tD : toDecrease) {
				client.playerLevel[tD] -= getBrewStat(client, tD, .10);
				if (client.playerLevel[tD] < 0)
					client.playerLevel[tD] = 1;
				client.getPA().refreshSkill(tD);
				client.getPA().setSkillLevel(tD, client.playerLevel[tD],
						client.playerXP[tD]);
			}
			client.playerLevel[1] += getBrewStat(client, 1, .20);
			if (client.playerLevel[1] > (client
					.getLevelForXP(client.playerXP[1]) * 1.2 + 1)) {
				client.playerLevel[1] = (int) (client
						.getLevelForXP(client.playerXP[1]) * 1.2);
			}
			client.getPA().refreshSkill(1);

			client.playerLevel[3] += getBrewStat(client, 3, .15);
			if (client.playerLevel[3] > (client
					.getLevelForXP(client.playerXP[3]) * 1.17 + 1)) {
				client.playerLevel[3] = (int) (client
						.getLevelForXP(client.playerXP[3]) * 1.17);
			}
			client.getPA().refreshSkill(3);
			break;

		case "antifire":
			if (System.currentTimeMillis() - client.lastAntiFire > 360000) {
				client.sendMessage("You are now immune to dragonfire.");
				client.lastAntiFire = System.currentTimeMillis();
			}
			break;

		case "zamorak":
			client.dealDamage(10);
			client.handleHitMask(10);
			client.getPA().refreshSkill(3);
			client.hitUpdateRequired = true;
			client.updateRequired = true;
			break;
		}
		int currentPotionDose = 0;
		for (int i = 0; i < p.getPotionIds().length; i++) {
			if (itemId == p.getPotionIds()[i]) {
				currentPotionDose = i + 1;
				break;
			}
		}
		client.sendMessage(currentPotionDose > 1 ? ("You have "
				+ (currentPotionDose - 1) + " dose"
				+ (currentPotionDose > 2 ? "s" : "") + " of potion left.")
				: "You have finished your potion.");
		int newPotion = 229;
		if (currentPotionDose > 1) {
			newPotion = p.getPotionIds()[currentPotionDose - 2];
		}
		client.getItems().addItem(newPotion, itemSlot, 1);
		client.potDelay = System.currentTimeMillis();
	}

	private static int getBrewStat(Client client, int skill, double amount) {
		return (int) (client.getLevelForXP(client.playerXP[skill]) * amount);
	}

}
