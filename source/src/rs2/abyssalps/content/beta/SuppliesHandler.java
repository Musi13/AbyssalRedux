package rs2.abyssalps.content.beta;

import rs2.abyssalps.model.player.Client;

public class SuppliesHandler {

	private static final int[] items = { 385, 3144, 2550, 2436, 2442, 2444,
			3040, 6685, 3024, 554, 555, 556, 557, 558, 559, 560, 561, 562, 563,
			564, 565, 9075, 12791, 6666, 12851, 12899, 7158, 1434, 1249, 5698,
			4153, 11808, 11806, 11804, 11802, 1305, 11838, 11889, 4587, 13080,
			12808, 12006, 4151, 4212, 868, 12926, 892, 861, 11212, 11235, 4740,
			9245, 9244, 11785, 6914, 4675, 12904, 11791, 2550, 12601, 12603,
			12605, 11770, 11772, 10499, 13124, 2411, 2412, 2413, 6570, 1127,
			12002, 6585, 7462, 3105, 10689, 2577, 11840, 12931, 13197, 13199 };

	public static void openInterface(Client client) {
		client.getPA().sendFrame126("Combat Beta Supplies", 55105);
		client.getPA().sendFrame126("Search", 55120);
		client.getItems().resetItems(3823);
		for (int index = 0; index < 80; index++) {
			client.getPA().sendFrame34a(55121, 0, index, 0);
		}
		for (int index = 0; index < 80; index++) {
			client.getPA().sendFrame34a(55121, items[index], index, 1);
		}
		client.usingSupplies = true;
		client.getPA().sendFrame248(55100, 3822);
	}

}
