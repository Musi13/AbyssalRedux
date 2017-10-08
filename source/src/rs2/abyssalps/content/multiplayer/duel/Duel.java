package rs2.abyssalps.content.multiplayer.duel;

import java.util.Arrays;
import java.util.Objects;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.content.multiplayer.Multiplayer;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;

public class Duel extends Multiplayer {

	public Duel(Client player) {
		super(player);
	}

	@Override
	public boolean requestable(Client requested) {
		if (!Config.duelOpen) {
			return false;
		}
		if (player.startTimer > 0) {
			player.sendMessage("You have to play for 30 minutes, before you can duel.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().requestAvailable(requested,
				player, MultiplayerSessionType.DUEL) != null) {
			player.sendMessage("You have already sent a request to this player.");
			return false;
		}
		if (Server.UpdateServer) {
			player.sendMessage("You cannot request or accept a duel request at this time.");
			player.sendMessage("The server is currently being updated.");
			return false;
		}
		if (Misc.distanceToPoint(player.getX(), player.getY(),
				requested.getX(), requested.getY()) > 3) {
			player.sendMessage("You are not close enough to the other player to request or accept.");
			return false;
		}
		if (!player.inDuelArena()) {
			player.sendMessage("You must be in the duel arena area to do this.");
			return false;
		}
		if (!requested.inDuelArena()) {
			player.sendMessage("The challenger must be in the duel arena area to do this.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			player.sendMessage("You cannot request a duel whilst in a session.");
			return false;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(requested)) {
			player.sendMessage("This player is currently is a session with another player.");
			return false;
		}
		if (player.teleTimer > 0 || requested.teleTimer > 0) {
			player.sendMessage("You cannot request or accept whilst you, or the other player are teleporting.");
			return false;
		}
		return true;
	}

	@Override
	public void request(Client requested) {
		if (!Config.duelOpen) {
			return;
		}
		if (Objects.isNull(requested)) {
			player.sendMessage("The player cannot be found, try again shortly.");
			return;
		}
		if (player.startTimer > 0) {
			player.sendMessage("You have to play for 30 minutes, before you can duel.");
			return;
		}
		if (Objects.equals(player, requested)) {
			player.sendMessage("You cannot trade yourself.");
			return;
		}
		player.faceUpdate(requested.playerId);
		DuelSession session = (DuelSession) Server
				.getMultiplayerSessionListener().requestAvailable(player,
						requested, MultiplayerSessionType.DUEL);
		if (session != null) {
			session.getStage().setStage(MultiplayerSessionStage.OFFER_ITEMS);
			session.populatePresetItems();
			session.updateMainComponent();
			session.sendDuelEquipment();
			Server.getMultiplayerSessionListener().removeOldRequests(player);
			Server.getMultiplayerSessionListener().removeOldRequests(requested);
			session.getStage().setAttachment(null);
		} else {
			session = new DuelSession(Arrays.asList(player, requested),
					MultiplayerSessionType.DUEL);
			if (Server.getMultiplayerSessionListener().appendable(session)) {
				player.sendMessage("Sending duel request...");
				requested.sendMessage(player.playerName + ":duelreq:");
				session.getStage().setAttachment(player);
				Server.getMultiplayerSessionListener().add(session);
			}
		}
	}

}
