package rs2.abyssalps.content.skill.runecrafting;

import java.util.HashMap;

import rs2.abyssalps.model.Position;

public enum RiftTable {

	CHAOS_RIFT(24976, new Position(2281, 4837)),

	LAW_RIFT(25034, new Position(2464, 4818)),

	DEATH_RIFT(25035, new Position(2208, 4830)),

	WATER_RIFT(25376, new Position(2725, 4832)),

	AIR_RIFT(25378, new Position(2841, 4829)),

	MINE_RIFT(25379, new Position(2793, 4828)),

	BODY_RIFT(24973, new Position(2521, 4834)),

	EARTH_RIFT(24972, new Position(2655, 4830)),

	FIRE_RIFT(24971, new Position(2574, 4848)),

	COSMIC_RIFT(24974, new Position(2162, 4833)),

	NATURE_RIFT(24975, new Position(2400, 4835));

	RiftTable(int id, Position coords) {
		this.id = id;
		this.coords = coords;
	}

	private int id;
	private Position coords;

	public Position getCoords() {
		return coords;
	}

	private static HashMap<Integer, RiftTable> riftMap = new HashMap<Integer, RiftTable>();

	public static RiftTable forId(int id) {
		return riftMap.get(id);
	}

	public static boolean isRift(int id) {
		return riftMap.containsKey(id);
	}

	static {
		for (RiftTable r : RiftTable.values()) {
			riftMap.put(r.id, r);
		}
	}
}
