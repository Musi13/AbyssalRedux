package rs2.net.packet.impl;

import rs2.abyssalps.content.bank.BankItem;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;

public class BankAllButOne implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		int interfaceId = player.getInStream().readSignedWordBigEndianA();
		int itemId = player.getInStream().readSignedWordBigEndianA();
		int itemSlot = player.getInStream().readSignedWordBigEndian();
		switch (interfaceId) {
		case 5382:
			int amount = player.getBank().getCurrentBankTab()
					.getItemAmount(new BankItem(itemId + 1));
			if (amount < 1)
				return;
			if (amount == 1) {
				player.sendMessage("Your bank only contains one of this item.");
				return;
			}
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().removeItem(itemId, amount - 1);
				return;
			}
			if ((player.getBank().getCurrentBankTab()
					.getItemAmount(new BankItem(itemId + 1)) - 1) > 1)
				player.getItems().removeFromBank(itemId, amount - 1, true);
			break;
		}
	}

}
