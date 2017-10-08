package rs2.abyssalps.content.minigame.abyssal_sire;

import rs2.Server;
import rs2.abyssalps.content.minigame.Minigame;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.player.Client;
import rs2.util.CycledState;

public class AbyssalSireMinigame implements Minigame {

	NPC abyssalSire;

	private CycledState sleepState;

	@Override
	public boolean enter(Client client) {
		int height = client.playerId * 4;
		
		int i =0 ;

		abyssalSire = Server.npcHandler.spawnNpc(client,
				AbyssalSireConstants.NORMAL_SIRE_ID,
				AbyssalSireConstants.ABYSSAL_SIRE_POS.getX(),
				AbyssalSireConstants.ABYSSAL_SIRE_POS.getY(), height, 0, 100,
				0, 0, 0, false, false);

		abyssalSire.setIsSleeping(true);
		abyssalSire.setCanAttack(false);
		sleepState = new AbyssalSireSleep(abyssalSire);

		client.getPA().movePlayer(
				AbyssalSireConstants.ABYSSAL_STARTER_POS.getX(),
				AbyssalSireConstants.ABYSSAL_STARTER_POS.getY(), height);
		return true;
	}

	@Override
	public void exit(Client client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(Client client) {
		if (sleepState != null && abyssalSire.isSleeping()) {
			sleepState.process();
		}
	}

}
