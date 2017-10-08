package rs2.abyssalps.content;

import rs2.Server;
import rs2.abyssalps.model.items.defs.ItemBonuses;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;

public class BlowPipe {

	public static void destroyBlowpipe(Client player) {
		player.getItems().deleteEquipment(12926, 3);
		if (player.getItems().freeSlots() > 0) {
			player.getItems().addItem(12924, 1);
		} else {
			Server.itemHandler.createGroundItem(player, 12924, player.getX(),
					player.getY(), player.heightLevel, 1, player.getId());
			player.sendMessage("Your inventory is out of space and your blowpipe is on the ground.");
		}
		for (int index = 0; index < player.getPipeData().length; index++) {
			player.getPipeData()[index] = 0;
		}
		player.getCombat().resetPlayerAttack();
	}

	public static void unchargeBlowpipe(Client player) {
		if (!player.getItems().playerHasItem(12926)) {
			return;
		}
		if (player.getPipeData()[2] <= 0) {
			return;
		}
		if (player.getItems().freeSlots() <= 0) {
			player.sendMessage("You don't have enough space to do this right now.");
			return;
		}
		player.getItems().deleteItem(12926, 1);
		player.getItems().addItem(12924, 1);
		player.getItems().addItem(12934, player.getPipeData()[2]);
		player.getPipeData()[2] = 0;
		player.sendMessage("You have successfully uncharged your blowpipe.");
	}

	public static void chargeBlowpipe(Client player) {
		if (!player.getItems().playerHasItem(12924)) {
			return;
		}
		if (!player.getItems().playerHasItem(12934)) {
			return;
		}
		if (player.getItems().getItemAmount(12934) < 16383) {
			player.sendMessage("I need atleast 16,383 Zulrah scales to do this.");
			return;
		}
		player.getItems().deleteItem(12934,
				player.getItems().getItemSlot(12934), 16383);
		player.getItems().deleteItem(12924, 1);
		player.getItems().addItem(12926, 1);
		player.getPipeData()[2] += 16383;
		player.sendMessage("You have successfully charged your blowpipe.");
	}

	public static int getBlowPipeProjectile(Client player) {
		switch (player.getPipeData()[0]) {
		case 806:
			return 232;

		case 807:
			return 233;

		case 808:
			return 234;

		case 809:
			return 235;

		case 810:
			return 236;

		case 811:
			return 237;

		default:
			return -1;
		}
	}

	public static int getBlowpipeMaxHit(Client player) {
		int a = player.playerLevel[4];
		int d = getBlowpipeStrength(player);
		double b = 1.00;
		if (player.prayerActive[3]) {
			b *= 1.05;
		} else if (player.prayerActive[11]) {
			b *= 1.10;
		} else if (player.prayerActive[19]) {
			b *= 1.15;
		}
		if (player.fullVoidRange()) {
			b *= 1.20;
		}
		double e = Math.floor(a * b);
		if (player.fightMode == 0) {
			e = (e + 3.0);
		}
		double max = (1.3 + e / 10 + d / 80 + e * d / 640);
		return (int) max;
	}

	public static int ammoLeft(Client player) {
		return player.getPipeData()[1];
	}

	public static void deleteBlowpipeAmmo(Client player) {
		if (player.getPipeData()[1] > 0) {
			player.getPipeData()[1]--;
			player.getPipeData()[2] -= 1;
			if (player.getPipeData()[1] <= 0) {
				player.getPipeData()[0] = 0;
				player.sendMessage("Your blowpipe has run out of ammo.");
			}
			if (player.getPipeData()[2] <= 0) {
				destroyBlowpipe(player);
				player.sendMessage("Your blowpipe has run out of scales.");
			}
			return;
		}
		player.sendMessage("Your blowpipe has run out of darts.");
	}

	public static int getBlowpipeStrength(Client player) {
		if (!usingBlowPipe(player)) {
			return 0;
		}
		return 40 + ItemBonuses.forId(player.getPipeData()[0]).getBonuses()[11];
	}

	public static boolean usingBlowPipe(Client player) {
		if (player.playerEquipment[3] == 12926) {
			return true;
		}
		return false;
	}

	public static void unloadBlowpipe(Client player) {
		if (player.getItems().freeSlots() <= 0) {
			player.sendMessage("You don't have enough space to unload it right now.");
			return;
		}
		player.getItems().addItem(player.getPipeData()[0],
				player.getPipeData()[1]);
		for (int index = 0; index < player.getPipeData().length - 1; index++) {
			player.getPipeData()[index] = 0;
		}
		player.sendMessage("You have successfully unloaded your blowpipe.");
	}

	public static void loadBlowpipe(Client player, int dart) {
		if (!player.getItems().playerHasItem(dart)) {
			return;
		}
		if (player.getPipeData()[0] > 0 || ammoLeft(player) > 0) {
			player.sendMessage("You need to empty your pipe before adding more ammo to it.");
			return;
		}
		player.getPipeData()[0] = dart;
		player.getPipeData()[1] += player.getItems().getItemAmount(dart);

		player.sendMessage("You successfully added "
				+ player.getItems().getItemAmount(dart) + " "
				+ ItemDefinitions.forId(dart).getName()
				+ "'s to your blowpipe.");

		player.getItems().deleteItem(dart,
				player.getItems().getItemAmount(dart));

		player.sendMessage("Your blowpipe now contains "
				+ player.getPipeData()[1] + " "
				+ ItemDefinitions.forId(player.getPipeData()[0]).getName()
				+ "'s.");
	}

}
