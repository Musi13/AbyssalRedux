package rs2.abyssalps.content.skill.prayer;

import rs2.abyssalps.model.player.Client;

public class BuryBones {

	public static void buryBones(Client client, int itemId, int itemSlot) {

		if (client.playerLevel[client.playerHitpoints] <= 0 || client.isDead) {
			return;
		}

		if (System.currentTimeMillis() - client.buryDelay < 1200) {
			return;
		}

		BoneData bone = BoneData.getBoneData(itemId);
		if (bone == null) {
			return;
		}

		client.getCombat().resetPlayerAttack();

		client.sendMessage("You dig a hole in the ground...");

		client.startAnimation(827);

		client.getItems().deleteItem(itemId, itemSlot, 1);

		client.getPA().addSkillXP((int) bone.getExperience()[0] * 25,
				client.playerPrayer);

		client.getPA().refreshSkill(client.playerPrayer);

		client.sendMessage("And bury the bones.");

		client.buryDelay = System.currentTimeMillis();
	}

}
