package rs2.net.packet.impl;

import java.util.Objects;

import rs2.Server;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSession;
import rs2.abyssalps.content.skill.slayer.SlayerConstants;
import rs2.abyssalps.model.items.ItemAssistant;
import rs2.abyssalps.model.player.Client;
import rs2.net.packet.PacketType;

/**
 * Wear Item
 **/
public class WearItem implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.wearId = c.getInStream().readUnsignedWord();
		c.wearSlot = c.getInStream().readUnsignedWordA();
		c.interfaceId = c.getInStream().readUnsignedWordA();
		if (!c.getCanWalk()) {
			return;
		}
		if (c.isDead || c.playerLevel[3] <= 0) {
			return;
		}

		if (c.wearId == SlayerConstants.ENCHANTED_GEM_ID) {
			SlayerConstants.useEnchantedGem(c);
			return;
		}

		if (!c.getItems().playerHasItem(c.wearId, 1, c.wearSlot)) {
			return;
		}

		if (Server.getMultiplayerSessionListener().inSession(c,
				MultiplayerSessionType.TRADE)) {
			Server.getMultiplayerSessionListener().finish(c,
					MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			c.sendMessage("You cannot remove items from your equipment whilst trading, trade declined.");
			return;
		}
		DuelSession duelSession = (DuelSession) Server
				.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession)
				&& duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
			c.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(c).sendMessage(
					"The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}

		if (c.canChangeAppearance) {
			c.sendMessage("You can't wear an item while changing appearence.");
			return;
		}

		if (c.playerIndex > 0 || c.npcIndex > 0) {
			if (c.wearId != 4153)
				c.getCombat().resetPlayerAttack();
		}

		if (!Server.getMultiplayerSessionListener().inSession(c,
				MultiplayerSessionType.TRADE)) {
			ItemAssistant.wearItem(c, c.wearId, c.wearSlot);
		}

		// c.getItems().wearItem(c.wearId, c.wearSlot);
	}

}
