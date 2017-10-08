package rs2.abyssalps.content;

import rs2.Server;
import rs2.abyssalps.model.Boundary;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.objects.Object;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class FlowerPoking {

	private static final int[] FLOWERS = {

	2981, 2983, 2980, 2984, 2985, 2986, 2987, 2988

	};

	/**
	 * Plant mithril seeds which turns into a flower
	 * 
	 * @param player
	 */
	public static void plantSeed(Client player, int id, int slot) {

		if (!player.getItems().playerHasItem(id, 1)) {
			return;
		}

		if (!player.inGambleZone()) {
			player.sendMessage("You can't plant a seed here.");
			player.sendMessage("Please go to @blu@::gamble to plant your seeds.");
			return;
		}

		if (System.currentTimeMillis() - player.lastPlant < 2000) {
			return;
		}

		if (Server.objectManager.objectExists(player.getX(), player.getY())) {
			player.sendMessage("A flower is already planted here.");
			return;
		}

		player.getItems().deleteItem(id, slot, 1);

		player.startAnimation(827);

		int flower = FLOWERS[(int) (Math.random() * FLOWERS.length)];

		new Object(flower, player.getX(), player.getY(), player.heightLevel, 0,
				10, -1, 60);

		player.getPA().playerWalk(player.pathX - 1, player.pathY, false);

		player.lastPlant = System.currentTimeMillis();

	}
}
