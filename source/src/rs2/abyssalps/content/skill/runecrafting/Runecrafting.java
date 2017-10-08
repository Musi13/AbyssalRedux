package rs2.abyssalps.content.skill.runecrafting;

import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;

public class Runecrafting {

	public static void handleRifts(Client player, int objectId) {
		RiftTable rift = RiftTable.forId(objectId);

		if (rift == null) {
			return;
		}

		player.getPA().movePlayer(rift.getCoords().getX(),
				rift.getCoords().getY(), rift.getCoords().getZ());
	}

	public static void craftRunes(Client player, int objectId) {
		AltarTable altar = AltarTable.forId(objectId);

		if (altar == null) {
			player.sendMessage("This altar does not work.");
			return;
		}

		if (!player.getItems().playerHasItem(1436)) {
			player.sendMessage("I need "
					+ ItemDefinitions.forId(1436).getName() + " to do this.");
			return;
		}

		player.turnPlayerTo(player.objectX, player.objectY);

		if (player.playerLevel[player.playerRunecrafting] < altar
				.getLevelRequired()) {
			player.sendMessage("You need a runecrafting level of "
					+ altar.getLevelRequired() + " to do this.");
			return;
		}

		int amountToCraft = player.getItems().getItemAmount(1436);

		player.gfx100(186);
		player.startAnimation(791);

		player.getItems().deleteItem(1436, amountToCraft);
		player.getItems().addItem(altar.getItem().id, amountToCraft);
		player.getAchievements().onCollectItem(altar.getItem().id, amountToCraft);
		
		player.getPA().addSkillXP(
				(int) ((altar.getExperience() * amountToCraft) * 25),
				player.playerRunecrafting);
		player.getPA().refreshSkill(player.playerRunecrafting);

		player.sendMessage("You manage to craft " + amountToCraft + " "
				+ ItemDefinitions.forId(altar.getItem().id).getName() + "'s.");
	}

}
