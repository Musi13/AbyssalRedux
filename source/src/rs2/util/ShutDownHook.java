package rs2.util;

import rs2.Server;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerSave;

public class ShutDownHook extends Thread {

	@Override
	public void run() {
		System.out.println("Shutdown thread run.");
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client)Server.playerHandler.players[j];
				rs2.abyssalps.model.player.PlayerSave.saveGame(c);			
			}		
		}
		System.out.println("Shutting down...");
	}

}