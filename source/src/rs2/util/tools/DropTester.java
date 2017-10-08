package rs2.util.tools;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.npcs.drop.Drops;

public class DropTester {

	public static void main(String[] parameters) throws FileNotFoundException {
		ItemDefinitions.init();
		Drops.parse();
		if (parameters.length > 0) {
			try {
				int dropAmount = Integer.valueOf(parameters[0]);
				int npcType = Integer.valueOf(parameters[1]);
				System.out.println("Testing " + dropAmount
						+ " drops for NPC type: " + npcType);
				for (int amount = 0; amount < dropAmount; amount++) {
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							"drops.txt", true));
					for (GameItem item : Drops.getRandomDrop(null, npcType)) {
						if (item == null) {
							System.out.println("lol");
							continue;
						}
						System.out.println(item);
						writer.write(ItemDefinitions.forId(item.id).getName()
								+ " " + item.amount);
						writer.newLine();
					}
					writer.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
