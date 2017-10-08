package rs2.abyssalps.content.goodwill;

import java.text.DecimalFormat;

import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;
import rs2.world.ItemHandler;

public class WellOfGoodwill {

	public static boolean experienceBoost = false;
	public static boolean dropChanceBoost = false;

	private static int experienceTick = 1800;
	private static int dropTick = 1800;

	private static long totalDonated;

	public static long getTotalDonated() {
		return totalDonated;
	}

	public static void increaseTotalDonated(int amount) {
		totalDonated += amount;
	}

	public static DecimalFormat format = new DecimalFormat("#,###.##");

	public static void donate(Client player, int item, int slot) {

		if (!player.getItems().playerHasItem(item)) {
			return;
		}

		int amount = 1;

		if (ItemDefinitions.forId(item).isStackable()) {
			amount = player.playerItemsN[slot];
		}

		player.getItems().deleteItem(item, slot, amount);
		int value = 0;
		if (item == 995)
			value = amount;
		else
			value = ItemHandler.getItemPrice()[item] * amount;

		boolean executeTick = false;

		if (value >= 300000000 && !dropChanceBoost && !experienceBoost) {
			dropChanceBoost = true;
			experienceBoost = true;
			executeTick = true;
			player.getPA().globalYell(
					"@red@" + Misc.formatPlayerName(player.playerName)
							+ "@bla@ has donated @blu@" + format.format(value)
							+ "@bla@gp to the Well of Goodwill.");
			player.getPA()
					.globalYell(
							"And therefore activated the @blu@drop chance boost @bla@ & @blu@double experience@bla@.");
		} else if (value >= 300000000 && dropChanceBoost && experienceBoost) {
			dropTick += 1800;
			experienceTick += 1800;
			player.getPA()
					.globalYell(
							"@red@"
									+ Misc.formatPlayerName(player.playerName)
									+ "@bla@ has increase the drop boost and double experience time by @blu@30@bla@ minutes.");
		} else if (value >= 300000000 && !dropChanceBoost && experienceBoost) {
			dropChanceBoost = true;
			experienceTick += 1800;
			player.getPA()
					.globalYell(
							"@red@"
									+ Misc.formatPlayerName(player.playerName)
									+ "@bla@ has activated drop boosts and increased the double xp time by @blu@30@bla@ minutes.");
		} else if (value >= 300000000 && dropChanceBoost && !experienceBoost) {
			dropTick += 1800;
			experienceBoost = true;
			player.getPA()
					.globalYell(
							"@red@"
									+ Misc.formatPlayerName(player.playerName)
									+ "@bla@ has activated double experience and increased the drop boost time by @blu@30@bla@ minutes.");
		} else if (value >= 200000000 && !dropChanceBoost) {
			dropChanceBoost = true;
			executeTick = true;
			player.getPA().globalYell(
					"@red@" + Misc.formatPlayerName(player.playerName)
							+ "@bla@ has activated the drop boost event.");
		} else if (value >= 200000000 && dropChanceBoost) {
			dropTick += 1800;
			player.getPA()
					.globalYell(
							"@red@"
									+ player.playerName
									+ "@bla@ has increased the drop boost event time by @blu@30@bla@ minutes.");
		} else if (value >= 100000000 && !experienceBoost) {
			experienceBoost = true;
			executeTick = true;
			player.getPA().globalYell(
					"@red@" + Misc.formatPlayerName(player.playerName)
							+ "@bla@ has enabled the double experience event.");
		} else if (value >= 100000000 && experienceBoost) {
			experienceTick += 1800;
			player.getPA()
					.globalYell(
							"@red@"
									+ Misc.formatPlayerName(player.playerName)
									+ "@bla@ has increased the double experience event time by @blu@30@bla@ minutes.");
		}

		if (executeTick) {
			executeTick();
		}

		player.sendMessage("You have successfully donated @blu@"
				+ format.format(value) + "@bla@ gp to the well of goodwill");

		increaseTotalDonated(value);

		player.sendMessage("A total of @blu@"
				+ format.format(getTotalDonated())
				+ "@bla@ gp has been donated to the well of goodwill.");
	}

	public static void executeTick() {
		CycleEventHandler.getSingleton().addEvent(30, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {

				if (dropTick > 0 && dropChanceBoost) {
					dropTick--;
				}
				if (dropTick <= 0) {
					dropChanceBoost = false;
					dropTick = 1800;
				}
				if (experienceTick > 0 && experienceBoost) {
					experienceTick--;
				}
				if (experienceTick <= 0) {
					experienceBoost = false;
					experienceTick = 1800;
				}

				if (!experienceBoost && !dropChanceBoost) {
					container.stop();
				}
			}

		}, 1);
	}
}
