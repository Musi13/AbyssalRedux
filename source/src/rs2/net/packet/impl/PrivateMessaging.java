package rs2.net.packet.impl;

import java.util.Objects;

import rs2.Config;
import rs2.Connection;
import rs2.Server;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.abyssalps.model.player.PlayerSave;
import rs2.net.packet.PacketType;
import rs2.sanction.SanctionHandler;
import rs2.util.Misc;

/**
 * Private messaging, friends etc
 **/
public class PrivateMessaging implements PacketType {

	public final int ADD_FRIEND = 188, SEND_PM = 126, REMOVE_FRIEND = 215,
			CHANGE_PM_STATUS = 95, REMOVE_IGNORE = 59, ADD_IGNORE = 133;

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		switch (packetType) {

		case ADD_FRIEND:
			c.getFriends().add(c.getInStream().readQWord());
			break;

		case SEND_PM:
			final long recipient = c.getInStream().readQWord();
			int pm_message_size = packetSize - 8;
			final byte pm_chat_message[] = new byte[pm_message_size];
			if (SanctionHandler.isMuted(c) || SanctionHandler.isIPMuted(c)) {
				return;
			}
			c.getInStream().readBytes(pm_chat_message, pm_message_size, 0);
			c.getFriends().sendPrivateMessage(recipient, pm_chat_message);
			break;

		case REMOVE_FRIEND:
			c.getFriends().remove(c.getInStream().readQWord());
			PlayerSave.saveGame(c);
			break;

		case REMOVE_IGNORE:
			c.getIgnores().remove(c.getInStream().readQWord());
			break;

		case CHANGE_PM_STATUS:
			c.getInStream().readUnsignedByte();
			c.setPrivateChat(c.getInStream().readUnsignedByte());
			c.getInStream().readUnsignedByte();
			c.getFriends().notifyFriendsOfUpdate();
			break;

		case ADD_IGNORE:
			c.getIgnores().add(c.getInStream().readQWord());
			break;

		}
	}
}
