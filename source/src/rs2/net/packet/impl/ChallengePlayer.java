package rs2.net.packet.impl;

import java.util.Objects;

import rs2.Server;
import rs2.abyssalps.model.Boundary;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.net.packet.PacketType;

/**
 * Challenge Player
 **/
public class ChallengePlayer implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		switch (packetType) {
		case 128:
			int answerPlayer = c.getInStream().readUnsignedWord();
			if (answerPlayer >= PlayerHandler.players.length
					|| answerPlayer < 0) {
				return;
			}
			if (PlayerHandler.players[answerPlayer] == null) {
				return;
			}
			Client requested = (Client) PlayerHandler.players[answerPlayer];
			if (Objects.isNull(requested)) {
				return;
			}
			if (Boundary.isIn(c, Boundary.DUEL_ARENAS)
					|| Boundary.isIn(requested, Boundary.DUEL_ARENAS)) {
				c.sendMessage("You cannot do this inside of the duel arena.");
				return;
			}
			if (c.getDuel().requestable(requested)) {
				c.getDuel().request(requested);
			}
			break;
		}
	}
}
