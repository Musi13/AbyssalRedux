package rs2.abyssalps.content.minigame.kraken;

import rs2.abyssalps.model.Position;

public class KrakenConstants {

	public static final int KRAKEN_MAX_HIT = 28;
	public static final int KRAKEN_HITPOINTS = 255;
	public static final int KRAKEN_ATTACK = 10;
	public static final int KRAKEN_DEFENCE = 10;

	public static final int TENACLE_MAX_HIT = 2;
	public static final int TENACLE_HITPOINTS = 120;
	public static final int TENACLE_ATTACK = 10;
	public static final int TENACLE_DEFENCE = 10;

	public static final int RESPAWN_TIMER = 15;

	public static final int KRAKEN_POOL_ID = 496;
	public static final int TENTACLE_POOL_ID = 5531;

	public static final int KRAKEN_ID = 494;
	public static final int TENTACLE_ID = 5535;

	public static final Position KRAKEN_AREA_POSITION = new Position(3696, 5807);
	public static final Position KRAKEN_POOL_POSITION = new Position(3694, 5810);
	public static final Position KRAKEN_CAVE_POSITION = new Position(2436, 9824);

	public static final Position[] TENTACLE_POOL_POSITIONS = {

	new Position(3690, 5810),

	new Position(3690, 5814),

	new Position(3701, 5814),

	new Position(3701, 5810)

	};

}
