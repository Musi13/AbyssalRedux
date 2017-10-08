package rs2.abyssalps.content.skill.prayer;

import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;

public class BoneCrusher {

	public static void crushBones(Client player, int itemId) {
		BoneData bone = BoneData.getBoneData(itemId);

		if (bone == null) {
			return;
		}

		if (!player.getItems().playerHasItem(13116)) {
			return;
		}

		player.getPA().addSkillXP((int) (bone.getExperience()[0] * 25),
				player.playerPrayer);
		player.getPA().refreshSkill(player.playerPrayer);

		player.sendMessage("@dre@The " + ItemDefinitions.forId(13116).getName()
				+ " crushed some " + ItemDefinitions.forId(itemId).getName()
				+ " for you.");
	}

}
