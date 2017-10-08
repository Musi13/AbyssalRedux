package rs2.net.packet.impl;

import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;

public class BankModifiableX implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		int slot = player.getInStream().readUnsignedWordA();
		int component = player.getInStream().readUnsignedWord();
		int item = player.getInStream().readUnsignedWordA();
		int amount = player.getInStream().readDWord();
		if (amount <= 0)
			return;
		switch (component) {
		case 5382:
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().removeItem(item, amount);
				return;
			}
			player.getItems().removeFromBank(item, amount, true);
			break;
		}
	}

}