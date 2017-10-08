package rs2.abyssalps.content.skill.slayer;

import java.util.HashMap;

import rs2.abyssalps.model.Position;

public enum SlayerTeleportTable {

	BATS(2827, new Position(2915, 9846, 0)),

	GHOST(2527, new Position(2893, 9848, 0)),

	SCORPION(2480, new Position(2940, 9778, 0)),

	SKELETON(70, new Position(2884, 9810, 0)),

	BLUE_DRAGONS(265, new Position(2921, 9802, 0)),

	DAGANNOTH(2250, new Position(2445, 10147, 0)),

	GREEN_DRAGON(260, new Position(3145, 3678, 0)),

	HILl_GIANTS(2098, new Position(2896, 2725, 0)),

	LESSER_DEMON(2005, new Position(2935, 9784, 0)),

	MOSS_GIANTS(2090, new Position(3145, 2725, 0)),

	BLAC_DEMONS(5874, new Position(2873, 9776, 0)),

	BLACK_DRAGONS(259, new Position(2821, 9827, 0)),

	HELL_HOUNDS(3133, new Position(2860, 9852, 0)),

	TZHAAR(2167, new Position(2445, 5174, 0)),

	CORPOREAL_BEAST(319, new Position(2968, 4384, 2));

	SlayerTeleportTable(int id, Position position) {
		this.id = id;
		this.position = position;
	}

	private int id;

	private Position position;

	public Position getPosition() {
		return this.position;
	}

	private static HashMap<Integer, SlayerTeleportTable> slayerMap = new HashMap<Integer, SlayerTeleportTable>();

	public static SlayerTeleportTable forId(int id) {
		return slayerMap.get(id);
	}

	public static boolean teleportExist(int id) {
		return slayerMap.containsKey(id);
	}

	static {
		for (SlayerTeleportTable s : SlayerTeleportTable.values()) {
			slayerMap.put(s.id, s);
		}
	}
}
