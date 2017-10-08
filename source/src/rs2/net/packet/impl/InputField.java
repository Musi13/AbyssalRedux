package rs2.net.packet.impl;

import rs2.abyssalps.content.beta.ItemIDFinder;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;

public class InputField implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		int id = player.inStream.readDWord();
		String text = player.inStream.readString();
		System.out.println(id + " - " + text);
		switch (id) {

		case 55301:
			if (player.isBanking || player.usingSupplies) {
				return;
			}
			if (text.length() < 3) {
				return;
			}
			ItemIDFinder.resetList(player, text);
			break;

		case 55120:
			if (player.isBanking || !player.usingSupplies) {
				return;
			}
			player.getPA().sendFrame126(
					"Combat Beta Supplies ('" + text + "')", 55105);
			if (text.length() < 5) {
				return;
			}
			player.getSupplies().resetInterface(text);
			break;

		case 58063:
			if (player.isBanking) {
				player.getBank().getBankSearch().setText(text);
				player.getBank().setLastSearch(System.currentTimeMillis());
				if (text.length() > 2) {
					player.getBank().getBankSearch().updateItems();
					player.getBank().setCurrentBankTab(
							player.getBank().getBankSearch().getTab());
					player.getItems().resetBank();
					player.getBank().getBankSearch().setSearching(true);
				} else {
					if (player.getBank().getBankSearch().isSearching())
						player.getBank().getBankSearch().reset();
					player.getBank().getBankSearch().setSearching(false);
				}
			}
			break;

		default:
			break;
		}
	}

}
