package rs2.net.packet.impl;

import java.util.Objects;

import rs2.Server;
import rs2.abyssalps.content.multiplayer.MultiplayerSession;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.content.multiplayer.trade.TradeSession;
import rs2.abyssalps.content.skill.smithing.SmithingAction;
import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;

/**
 * Bank 10 Items
 **/
public class Bank10 implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int interfaceId = c.getInStream().readUnsignedWordBigEndian();
		int removeId = c.getInStream().readUnsignedWordA();
		int removeSlot = c.getInStream().readUnsignedWordA();
		if (c.getRank() >= 3) {
			System.out.println("Bank 10: " + interfaceId);
		}
		switch (interfaceId) {

		case 55121:
			if (!c.usingSupplies) {
				return;
			}
			c.getSupplies().get(removeId, 10);
			break;

		case 1688:
			c.getPA().useOperate(removeId);
			break;

		case 3900:
			c.getShops().buyItem(removeId, removeSlot, 5);
			break;

		case 3823:
			c.getShops().sellItem(removeId, removeSlot, 5);
			break;

		case 5064:
			if (Server.getMultiplayerSessionListener().inSession(c,
					MultiplayerSessionType.TRADE)) {
				Server.getMultiplayerSessionListener().finish(c,
						MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				c.sendMessage("You cannot do this whilst trading.");
				return;
			}
			DuelSession duelSession = (DuelSession) Server
					.getMultiplayerSessionListener().getMultiplayerSession(c,
							MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession)
					&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
				c.sendMessage("You have declined the duel.");
				duelSession.getOther(c).sendMessage(
						"The challenger has declined the duel.");
				duelSession
						.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				return;
			}
			if (c.isBanking) {
				c.getItems().addToBank(removeId, 10, true);
			}
			break;

		case 5382:
			if (Server.getMultiplayerSessionListener().inSession(c,
					MultiplayerSessionType.TRADE)) {
				Server.getMultiplayerSessionListener().finish(c,
						MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				c.sendMessage("You cannot do this whilst trading.");
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().removeItem(removeId, 10);
				return;
			}
			c.getItems().removeFromBank(removeId, 10, true);
			break;

		case 3322:
			MultiplayerSession session = Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession
					|| session instanceof DuelSession) {
				session.addItem(c, new GameItem(removeId, 10));
			}
			break;

		case 3415:
			session = Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.removeItem(c, removeSlot, new GameItem(removeId, 10));
			}
			break;

		case 6669:
			session = Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof DuelSession) {
				session.removeItem(c, removeSlot, new GameItem(removeId, 10));
			}
			break;

		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			SmithingAction.execute(c, removeId, 10);
			break;
		}
	}

}
