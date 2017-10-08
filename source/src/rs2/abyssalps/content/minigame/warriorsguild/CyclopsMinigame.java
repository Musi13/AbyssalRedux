package rs2.abyssalps.content.minigame.warriorsguild;

import rs2.Server;
import rs2.abyssalps.content.minigame.Minigame;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class CyclopsMinigame implements Minigame {

	private int defenderId;

	@Override
	public boolean enter(Client client) {
		if (!client.getItems().playerHasItem(CyclopsConstants.TOKEN_ID,
				CyclopsConstants.ENTRY_FEE)) {
			client.sendMessage("You need atleast 100 "
					+ ItemDefinitions.forId(CyclopsConstants.TOKEN_ID)
							.getName() + " to enter.");
			return false;
		}
		client.getItems().deleteItem(CyclopsConstants.TOKEN_ID,
				client.getItems().getItemSlot(CyclopsConstants.TOKEN_ID),
				CyclopsConstants.ENTRY_FEE);
		client.setLastCyclopSubstract(System.currentTimeMillis());
		defenderId = getDefenderId(client);
		return true;
	}

	@Override
	public void exit(Client client) {

	}

	@Override
	public void process(Client client) {
		if (client.heightLevel != 2
				|| client.distanceToPoint(
						CyclopsConstants.CYCLOP_AREA_POSITION.getX(),
						CyclopsConstants.CYCLOP_AREA_POSITION.getY()) > 40) {
			client.exitMinigame();
		}
		if (client.getItems().getItemAmount(CyclopsConstants.TOKEN_ID) < CyclopsConstants.AMOUNT_TO_DECREASE
				&& canKick(client)) {
			client.sendMessage("You ran out of tokens.");
			client.getPA().movePlayer(2846, 3541, 2);
			client.exitMinigame();
		}
		if (System.currentTimeMillis() - client.getLastCyclopSubstract() >= 60000) {
			client.getItems().deleteItem(CyclopsConstants.TOKEN_ID,
					client.getItems().getItemSlot(CyclopsConstants.TOKEN_ID),
					10);
			client.setLastCyclopSubstract(System.currentTimeMillis());
		}
	}

	@Override
	public boolean onDropItems(Client client, NPC npc) {
		if (npc.npcType < 2463 || npc.npcType > 2468) {
			return false;
		}

		if (Misc.random(CyclopsConstants.DEFENDER_RARITY) != 0) {
			return false;
		}

		if (defenderId < CyclopsConstants.DEFENDER_IDS.length - 1) {
			defenderId++;
		}

		Server.itemHandler.createGroundItem(client,
				CyclopsConstants.DEFENDER_IDS[defenderId], npc.absX, npc.absY,
				client.heightLevel, 1, client.playerId);
		return false;
	}

	public int getDefenderId(Client client) {
		int defenderId = -1;

		for (int i = (CyclopsConstants.DEFENDER_IDS.length - 1); i >= 0; i--) {
			if (client.getItems().getTotalCount(
					CyclopsConstants.DEFENDER_IDS[i]) > 0) {
				defenderId = i;
				break;
			}
		}

		return defenderId;
	}

	private boolean canKick(Client player) {
		if (System.currentTimeMillis() - player.getLastCyclopSubstract() >= 60000) {
			return true;
		}
		return false;
	}

}
