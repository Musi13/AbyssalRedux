package rs2.abyssalps.content.minigame.zulrah;

import rs2.abyssalps.model.Position;

public interface ZulrahConstants {

	public static final int MAX_HIT = 41;
	public static final int ATTACK = 600;
	public static final int DEFENCE = 600;
	public static final int HITPOINTS = 500;
	public static final int HYBDRID_STATE_HITPOINTS = 100;

	public static final int GREEN_ZULRAH_ID = 2042;
	public static final int RED_ZULRAH_ID = 2043;
	public static final int BLUE_ZULRAH_ID = 2044;
	public static final int SNAKELING_ID = 2045;

	public static final int FARCAST_ANIMATION = 5069;
	public static final int DIVE_ANIMATION = 5072;
	public static final int EMERGE_ANIMATION = 5073;
	public static final int DEATH_ANIMATION = 5804;
	public static final int MELEE_TARGET_ANIMATION = 5806;
	public static final int MELEE_ATTACK_ANIMATION = 5807;

	public static final int RANGE_PROJECTILE_ID = 1044;
	public static final int TOXIC_CLOUD_PROJECTILE_ID = 1045;
	public static final int MAGE_PROJECTILE_ID = 1046;
	public static final int SNAKELING_PROJECTILE_ID = 1047;

	public static final int TOXIC_CLOUD_ID = 11700;

	public static final Position ZUL_ANDRA_POSITION = new Position(2200, 3056);

	public static final Position BOAT_POSITION = new Position(2203, 3056);

	public static final Position ZULRAH_ISLAND_POSITION = new Position(2268,
			3069);

	public static final Position ZULRAH_NORTH_POSITION = new Position(2266,
			3072);
	public static final Position ZULRAH_WEST_POSITION = new Position(2256, 3071);
	public static final Position ZULRAH_SOUTH_POSITION = new Position(2266,
			3062);
	public static final Position ZULRAH_EAST_POSITION = new Position(2276, 3072);

	public static final Position[] TOXIC_CLOUD_POSITIONS = {
			new Position(2262, 3075), new Position(2262, 3072),
			new Position(2263, 3069), new Position(2266, 3068),
			new Position(2269, 3068), new Position(2272, 3070),
			new Position(2272, 3073), new Position(2272, 3076) };

}
