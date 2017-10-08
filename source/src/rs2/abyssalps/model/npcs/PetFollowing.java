package rs2.abyssalps.model.npcs;

import rs2.Server;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.cache.region.Region;

public class PetFollowing {

	public static void followOwner(int npcId, int playerId) {
		if (PlayerHandler.players[playerId] == null) {
			return;
		}
		Client player = (Client) PlayerHandler.players[playerId];
		NPC pet = (NPC) NPCHandler.npcs[npcId];
		if (pet.distanceTo(player.getX(), player.getY()) <= 0) {
			return;
		}

		pet.facePlayer(playerId);

		switch (player.otherDirection) {
		case 0:
			pet.moveX = Server.npcHandler.GetMove(pet.absX, player.absX);
			pet.moveY = Server.npcHandler.GetMove(pet.absY, player.absY - 1);
			pet.getNextNPCMovement(npcId);
			break;
		case 1:
		case 2:
		case 3:
			pet.moveX = Server.npcHandler.GetMove(pet.absX, player.absX - 1);
			pet.moveY = Server.npcHandler.GetMove(pet.absY, player.absY - 1);
			pet.getNextNPCMovement(npcId);
			break;
		case 4:
			pet.moveX = Server.npcHandler.GetMove(pet.absX, player.absX - 1);
			pet.moveY = Server.npcHandler.GetMove(pet.absY, player.absY);
			pet.getNextNPCMovement(npcId);
			break;
		case 6:
		case 5:
		case 7:
			pet.moveX = Server.npcHandler.GetMove(pet.absX, player.absX - 1);
			pet.moveY = Server.npcHandler.GetMove(pet.absY, player.absY + 1);
			pet.getNextNPCMovement(npcId);
			break;
		case 8:
			pet.moveX = Server.npcHandler.GetMove(pet.absX, player.absX);
			pet.moveY = Server.npcHandler.GetMove(pet.absY, player.absY + 1);
			pet.getNextNPCMovement(npcId);
			break;

		case 10:
		case 11:
		case 9:
			pet.moveX = Server.npcHandler.GetMove(pet.absX, player.absX + 1);
			pet.moveY = Server.npcHandler.GetMove(pet.absY, player.absY + 1);
			pet.getNextNPCMovement(npcId);
			break;

		case 12:
			pet.moveX = Server.npcHandler.GetMove(pet.absX, player.absX + 1);
			pet.moveY = Server.npcHandler.GetMove(pet.absY, player.absY);
			pet.getNextNPCMovement(npcId);
			break;
		}
		pet.facePlayer(playerId);
		pet.updateRequired = true;
	}
}
