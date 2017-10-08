package rs2.net.packet.impl;

import java.util.Objects;

import rs2.Server;
import rs2.abyssalps.content.multiplayer.MultiplayerSession;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.content.multiplayer.trade.TradeSession;
import rs2.abyssalps.content.skill.crafting.GemCutting;
import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;

/**
 * Bank X Items
 **/
public class BankX2 implements PacketType {
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int Xamount = c.getInStream().readDWord();
		if (Xamount < 0)// this should work fine
		{
			Xamount = c.getItems().getItemAmount(c.xRemoveId);
		}
		if (Xamount == 0) {
			Xamount = 1;
		}
		if (c.getRank() >= 8) {
			System.out.println("Bank X: " + c.xInterfaceId);
		}

		if (c.getInterfaces().handleAmount(Xamount)) {
			return;
		}

		if (c.usingGemInterface()) {
			c.setGemsToCut(Xamount);
			c.setGemInterfaceState(false);
			GemCutting.execute(c);
			return;
		}

		switch (c.xInterfaceId) {

		case 5064:
			if (Server.getMultiplayerSessionListener().inSession(c,
					MultiplayerSessionType.TRADE)) {
				c.sendMessage("You cannot bank items whilst trading.");
				return;
			}
			if (!c.getItems().playerHasItem(c.xRemoveId, Xamount))
				return;
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
			c.getItems().addToBank(c.playerItems[c.xRemoveSlot] - 1, Xamount,
					true);
			break;
		case 5382:
			if (Server.getMultiplayerSessionListener().inSession(c,
					MultiplayerSessionType.TRADE)) {
				c.sendMessage("You cannot remove items from the bank whilst trading.");
				return;
			}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank()
						.getBankSearch()
						.removeItem(
								c.getBank().getCurrentBankTab()
										.getItem(c.xRemoveSlot).getId() - 1,
								Xamount);
				return;
			}
			c.getItems().removeFromBank(
					c.getBank().getCurrentBankTab().getItem(c.xRemoveSlot)
							.getId() - 1, Xamount, true);
			break;

		case 3322:
			MultiplayerSession session = Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession
					|| session instanceof DuelSession) {
				session.addItem(c, new GameItem(c.xRemoveId, Xamount));
			}
			break;

		case 3415:
			session = Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.removeItem(c, c.xRemoveSlot, new GameItem(c.xRemoveId,
						Xamount));
			}
			break;

		case 6669:
			session = Server.getMultiplayerSessionListener()
					.getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof DuelSession) {
				session.removeItem(c, c.xRemoveSlot, new GameItem(c.xRemoveId,
						Xamount));
			}
			break;

		}
	}
}