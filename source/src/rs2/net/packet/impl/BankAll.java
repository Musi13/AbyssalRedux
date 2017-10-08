package rs2.net.packet.impl;

import java.util.Objects;

import rs2.Server;
import rs2.abyssalps.content.bank.BankItem;
import rs2.abyssalps.content.multiplayer.MultiplayerSession;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.content.multiplayer.trade.TradeSession;
import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.items.Item;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;

/**
 * Bank All Items
 **/
public class BankAll implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int removeSlot = c.getInStream().readUnsignedWordA();
		int interfaceId = c.getInStream().readUnsignedWord();
		int removeId = c.getInStream().readUnsignedWordA();
		if (c.getRank() >= 3) {
			System.out.println("Bank All: " + interfaceId);
		}
		switch (interfaceId) {

		case 55121:
			if (!c.usingSupplies) {
				return;
			}
			c.getSupplies().get(removeId, 50);
			break;

		case 3900:
			c.getShops().buyItem(removeId, removeSlot, 10);
			break;

		case 3823:
			c.getShops().sellItem(removeId, removeSlot, 10);
			break;

		case 5064:
			if (c.inTrade) {
				return;
			}
			DuelSession duelSession = (DuelSession) Server
					.getMultiplayerSessionListener().getMultiplayerSession(c,
							MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession)
					&& duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
					&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
				c.sendMessage("You have declined the duel.");
				duelSession.getOther(c).sendMessage(
						"The challenger has declined the duel.");
				duelSession
						.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				return;
			}
			if (c.isBanking) {
				c.getItems().addToBank(removeId,
						c.getItems().getItemAmount(removeId), true);
			}
			break;

		case 5382:
			if (!c.isBanking) {
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank()
						.getBankSearch()
						.removeItem(
								removeId,
								c.getBank()
										.getCurrentBankTab()
										.getItemAmount(
												new BankItem(removeId + 1)));
				return;
			}
			c.getItems().removeFromBank(
					removeId,
					c.getBank().getCurrentBankTab()
							.getItemAmount(new BankItem(removeId + 1)), true);
			break;

		case 3322:
			MultiplayerSession session = Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession
					|| session instanceof DuelSession) {
				session.addItem(c, new GameItem(removeId, c.getItems()
						.getItemAmount(removeId)));
			}
			break;

		case 3415:
			session = Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.removeItem(c, removeSlot, new GameItem(removeId,
						Integer.MAX_VALUE));
			}
			break;

		case 6669:
			session = Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof DuelSession) {
				session.removeItem(c, removeSlot, new GameItem(removeId,
						Integer.MAX_VALUE));
			}
			break;

		}
	}

}
