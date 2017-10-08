package rs2.abyssalps.content.skill.prayer;

import rs2.abyssalps.model.player.Client;

public class BonesOnAltar {

	public static void useBoneOnAltar(Client player, int itemId, int x, int y) {
		if (player.playerLevel[player.playerHitpoints] <= 0 || player.isDead) {
			return;
		}

		if (System.currentTimeMillis() - player.buryDelay < 1200) {
			return;
		}

		BoneData bone = BoneData.getBoneData(itemId);

		if (bone == null) {
			return;
		}

		player.getCombat().resetPlayerAttack();

		player.startAnimation(896);

		player.sendMessage("The gods are pleased with your offering.");

		player.getItems().deleteItem(itemId,
				player.getItems().getItemSlot(itemId), 1);

		player.getPA().addSkillXP((int) bone.getExperience()[1] * 25,
				player.playerPrayer);

		player.getPA().createPlayersStillGfx(624, x, y, 0, 0);

		player.getPA().refreshSkill(player.playerPrayer);

		player.buryDelay = System.currentTimeMillis();

	}

}
