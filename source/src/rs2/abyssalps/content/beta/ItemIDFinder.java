package rs2.abyssalps.content.beta;

import rs2.abyssalps.model.items.defs.BetaDefinitions;
import rs2.abyssalps.model.player.Client;

public class ItemIDFinder {

	private static int line = 55321;

	public static void resetList(Client client, String name) {
		name = name.toLowerCase();
		for (int index = line; index < 55821; index++) {
			client.getPA().sendFrame126("", index);
		}
		for (int index = 0; index < BetaDefinitions.ALLOCATED_SIZE; index++) {
			if (BetaDefinitions.instance == null) {
				continue;
			}
			if (!BetaDefinitions.forId(index).getName().toLowerCase()
					.contains(name)) {
				continue;
			}
			if (BetaDefinitions.forId(index).getName().endsWith("100")
					|| BetaDefinitions.forId(index).getName().endsWith("75")
					|| BetaDefinitions.forId(index).getName().endsWith("50")
					|| BetaDefinitions.forId(index).getName().endsWith("25")) {
				continue;
			}
			client.getPA().sendFrame126(
					BetaDefinitions.forId(index).getName() + " - " + index,
					line);
			line++;
			if (line >= 55821)
				line = 55321;
		}
	}

}
