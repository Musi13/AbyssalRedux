package rs2.abyssalps.content.consumables;

import java.util.HashMap;
import java.util.Objects;

import rs2.Server;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.content.multiplayer.duel.DuelSessionRules.Rule;
import rs2.abyssalps.model.Boundary;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;

public class Food {

	private static enum FoodData {
		LOBSTER(379, 12),

		SWORDFISH(373, 14),

		MONKFISH(7946, 16),

		COOKED_KARAMBWAN(3144, 18),

		SHARK(385, 20),

		MUSHROOM_POTATO(7058, 20),

		TUNA_POTATO(7060, 22),

		DARK_CRAB(11936, 22),

		MANTA_RAY(391, 22),

		CHILLI_POTATO(7054, 14);

		FoodData(int id, int healAmount) {
			this.id = id;
			this.healAmount = healAmount;
		}

		private int id;
		private int healAmount;

		public int getHealAmount() {
			return this.healAmount;
		}

		public static FoodData forId(int id) {
			return foodMap.get(id);
		}

		private static HashMap<Integer, FoodData> foodMap = new HashMap<Integer, FoodData>();

		static {
			for (FoodData f : FoodData.values()) {
				foodMap.put(f.id, f);
			}
		}
	}

	public static boolean isFood(int id) {
		return FoodData.foodMap.containsKey(id);
	}

	public static void eatFood(Client client, int itemId, int itemSlot) {

		if (client.playerLevel[3] <= 0 || client.isDead) {
			return;
		}

		if (System.currentTimeMillis() - client.foodDelay < 1200
				&& itemId != 3144) {
			return;
		}

		if (System.currentTimeMillis() - client.subFoodDelay < 1200
				&& itemId == 3144) {
			return;
		}

		if (Boundary.isIn(client, Boundary.DUEL_ARENAS)) {
			DuelSession session = (DuelSession) Server
					.getMultiplayerSessionListener().getMultiplayerSession(
							client, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(session)) {
				if (session.getRules().contains(Rule.NO_FOOD)) {
					client.sendMessage("Food have been disabled for this duel.");
					return;
				}
			}
		}

		FoodData food = FoodData.forId(itemId);

		if (food == null) {
			return;
		}

		client.getCombat().resetPlayerAttack();

		client.startAnimation(829);

		client.sendMessage("You eat the "
				+ ItemDefinitions.forId(itemId).getName() + ".");
		if (client.playerLevel[3] < client.getLevelForXP(client.playerXP[3])) {
			client.playerLevel[3] += food.getHealAmount();
			if (client.playerLevel[3] > client
					.getLevelForXP(client.playerXP[3])) {
				client.playerLevel[3] = client
						.getLevelForXP(client.playerXP[3]);
			}
			client.sendMessage("It heals some health");
		}

		client.getPA().refreshSkill(3);

		client.getItems().deleteItem(itemId, itemSlot, 1);
		if (food == FoodData.COOKED_KARAMBWAN) {
			client.subFoodDelay = System.currentTimeMillis();
		} else {
			client.foodDelay = System.currentTimeMillis();
		}
	}

	public static void eatAnglerFish(Client client, int itemId, int itemSlot) {
		if (client.playerLevel[3] <= 0 || client.isDead) {
			return;
		}
		if (System.currentTimeMillis() - client.foodDelay < 1200) {
			return;
		}
		if (Boundary.isIn(client, Boundary.DUEL_ARENAS)) {
			DuelSession session = (DuelSession) Server
					.getMultiplayerSessionListener().getMultiplayerSession(
							client, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(session)) {
				if (session.getRules().contains(Rule.NO_FOOD)) {
					client.sendMessage("Food have been disabled for this duel.");
					return;
				}
			}
		}
		int c = 0;
		if (client.getLevelForXP(client.playerXP[3]) >= 0
				&& client.getLevelForXP(client.playerXP[3]) <= 24) {
			c = 2;
		} else if (client.getLevelForXP(client.playerXP[3]) > 24
				&& client.getLevelForXP(client.playerXP[3]) <= 49) {
			c = 4;
		} else if (client.getLevelForXP(client.playerXP[3]) > 49
				&& client.getLevelForXP(client.playerXP[3]) <= 74) {
			c = 6;
		} else if (client.getLevelForXP(client.playerXP[3]) > 74
				&& client.getLevelForXP(client.playerXP[3]) <= 92) {
			c = 8;
		} else if (client.getLevelForXP(client.playerXP[3]) > 92
				&& client.getLevelForXP(client.playerXP[3]) <= 99) {
			c = 13;
		}
		int healingFactor = (int) Math.floor(client
				.getLevelForXP(client.playerXP[3]) / 10) + c;

		client.getCombat().resetPlayerAttack();

		client.startAnimation(829);

		client.sendMessage("You eat the "
				+ ItemDefinitions.forId(itemId).getName() + ".");
		if (client.playerLevel[3] < (client.getLevelForXP(client.playerXP[3]) + healingFactor)) {
			client.playerLevel[3] += healingFactor;
			if (client.playerLevel[3] > (client
					.getLevelForXP(client.playerXP[3]) + healingFactor)) {
				client.playerLevel[3] = (client
						.getLevelForXP(client.playerXP[3]) + healingFactor);
			}
			client.sendMessage("It heals some health");
		}

		client.getPA().refreshSkill(3);

		client.getItems().deleteItem(itemId, itemSlot, 1);
		client.foodDelay = System.currentTimeMillis();
	}
}
