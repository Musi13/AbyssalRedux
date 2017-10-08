package rs2.abyssalps.content.pets;

import rs2.Server;
import rs2.abyssalps.content.PlayerContent;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.player.Client;

public class Pets extends PlayerContent {

	public Pets(Client client) {
		super(client);
	}

	private void spawnPet(int petId) {
		NPC npc = Server.npcHandler.spawnNpc(getClient(), petId, getClient().absX, getClient().absY + 1, getClient().heightLevel, 0, 0, 0, 0, 0, false, false);
		getClient().setPetIndex(npc.npcId);
	}
	
	public boolean dropPet(int itemId) {
		PetDefinitions def = PetDefinitions.getByItemId(itemId);
		if (def == null) {
			return false;
		}
		if (getClient().getPetId() > 0) {
			getClient().sendMessage("You may only have one pet at a time.");
			return true;
		}
		getClient().setPetId(def.getNpcId());
		getClient().getItems().deleteItem(itemId, 1);
		spawnPet(def.getNpcId());
		return true;
	}

	public boolean pickupPet(NPC npc) {
		PetDefinitions def = PetDefinitions.getByNpcId(npc.npcType);
		if (def == null) {
			return false;
		}
		if (getClient().getPetId() <= 0 || npc.spawnedBy != getClient().getId()) {
			getClient().sendMessage("This pet is not yours to pick up.");
			return true;
		}
		if (getClient().getItems().freeSlots() == 0) {
			getClient().sendMessage("Not enough space in your inventory.");
			return true;
		}
		getClient().setPetId(0);
		getClient().getItems().addItem(def.getItemId(), 1);
		npc.remove();
		return true;
	}

	public void process() {
		NPC npc = NPCHandler.npcs[getClient().getPetIndex()];
		if (npc == null || npc.spawnedBy != getClient().getId()) {
			spawnPet(getClient().getPetId());
			return;
		}
		
		if (!npc.isVisible()) {
			npc.setVisible(true);
		}
		
		if (npc.distanceTo(getClient()) > 10 || npc.heightLevel != getClient().heightLevel) {
			npc.setVisible(false);
			npc.absX = getClient().absX;
			npc.absY = getClient().absY + 1;
			npc.makeX = npc.absX;
			npc.makeY = npc.absY;
			npc.heightLevel = getClient().heightLevel;
		}
		
		Server.npcHandler.followPlayer(npc.npcId, getClient().getId());
	}

}
