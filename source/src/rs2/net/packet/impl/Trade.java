package rs2.net.packet.impl;

import java.util.Objects;

import rs2.Config;
import rs2.abyssalps.model.Boundary;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.net.packet.PacketType;
import rs2.util.Misc;

/**
 * Trading
 */
public class Trade implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int tradeId = c.getInStream().readSignedWordBigEndian();
		if (tradeId < 1)
			return;
		Client requested = (Client) PlayerHandler.players[tradeId];
		c.getPA().resetFollow();
		if (c.startTimer > 0) {
			c.sendMessage("Please wait 30 minutes before trading items.");
			return;
		}
		/**
		 * Thanks ama for the fix
		 */
		if (c.distanceToPoint(requested.getX(), requested.getY()) > 1) {
			c.sendMessage("You are too far away to trade "
					+ Misc.formatPlayerName(requested.playerName)
					+ " right now.");
			return;
		}
		if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
			c.sendMessage("You cannot trade whilst inside the duel arena.");
			return;
		}
		if (Objects.equals(requested, c)) {
			c.sendMessage("You cannot trade yourself.");
			return;
		}
		if (c.getTrade().requestable(requested)) {
			c.getTrade().request(requested);
			return;
		}
	}

}
