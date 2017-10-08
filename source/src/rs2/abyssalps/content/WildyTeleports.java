package rs2.abyssalps.content;

import java.util.HashMap;

import rs2.abyssalps.content.skill.magic.TeleportType;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.player.Client;

public enum WildyTeleports {

	SCORPIA(0, new Position(3233, 3950, 0), "Scorpia"),

	CHAOS_FANATIC(1, new Position(2980, 3865, 0), "Chaos Fanatic"),

	VENENATIS(2, new Position(3340, 3731, 0), "Venenatis"),

	CRAZY_ARCHAEOLOGIST(3, new Position(2987, 3700, 0), "Crazy archaeologist"),

	CALLISTO(4, new Position(3308, 3848, 0), "Callisto");

	WildyTeleports(int id, Position pos, String name) {
		this.id = id;
		this.position = pos;
		this.teleportName = name;
	}

	private int id;
	private Position position;
	private String teleportName;

	public Position getPosition() {
		return this.position;
	}

	public String getTeleportName() {
		return this.teleportName;
	}

	private static HashMap<Integer, WildyTeleports> wildyMap = new HashMap<Integer, WildyTeleports>();

	public static WildyTeleports forId(int id) {
		return wildyMap.get(id);
	}

	public static void startTeleport(Client player, int id, int stage) {
		WildyTeleports wildy = WildyTeleports.forId(id);
		if (wildy == null) {
			player.getPA().removeAllWindows();
			return;
		}
		player.interfaceAction = -1;
		player.wildyTeleport = id;
		switch (stage) {

		case 0:
			if (!player.getItems().playerHasItem(995, 500000)) {
				player.getDH().npcTalk(
						"You need 500,000 gp to use the "
								+ wildy.getTeleportName() + " teleport.",
						"Wizard", 1160);
				player.nextChat = 0;
				return;
			}
			player.getDH().npcTalk(
					"It'll cost you 500,000 gp to use this teleport.",
					"wizard", 1160);
			player.nextChat = 13;
			break;

		case 1:
			if (!player.getItems().playerHasItem(995, 500000)) {
				player.getPA().removeAllWindows();
				return;
			}
			player.getItems().deleteItem(995,
					player.getItems().getItemSlot(995), 500000);
			player.getPA().startTeleport(wildy.getPosition().getX(),
					wildy.getPosition().getY(), wildy.getPosition().getZ(),
					TeleportType.MODERN);
			break;
		}
	}

	static {
		for (WildyTeleports w : WildyTeleports.values()) {
			wildyMap.put(w.id, w);
		}
	}
}
