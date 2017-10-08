package rs2.abyssalps.content;

import rs2.abyssalps.model.player.Client;

public abstract class PlayerContent {

	private final Client client;

	public PlayerContent(Client client) {
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

}
