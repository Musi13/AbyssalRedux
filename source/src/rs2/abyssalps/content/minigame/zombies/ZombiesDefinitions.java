package rs2.abyssalps.content.minigame.zombies;

import java.util.ArrayList;
import java.util.List;

import rs2.abyssalps.model.Position;
import rs2.util.cache.region.Region;

public final class ZombiesDefinitions {

	private ZombiesDefinitions() {

	}

	public static final Position LOBBY_POSITION = new Position(3696, 3520, 1);
	public static final Position MINIGAME_POSITION = new Position(3691, 3513);

	public static final Position NORTH_EAST_CORNER_POSITION = new Position(3685, 3500);
	public static final Position SOUTH_WEST_CORNER_POSITION = new Position(3660, 3465);

	public static final int[] EASY_ZOMBIE_IDS = { 2501, 2502, 2503 };
	public static final int[] MEDIUM_ZOMBIE_IDS = { 2504, 2505, 2506 };
	public static final int[] HARD_ZOMBIE_IDS = { 2507, 2508, 2509 };

	public static int getAttack(int zombieId) {
		if (true) {
			return 1;
		}
		switch (zombieId) {
		case 2501:
		case 2502:
		case 2503:
			return 25;
		case 2504:
		case 2505:
		case 2506:
			return 50;
		case 2507:
		case 2508:
		case 2509:
			return 100;
		}
		return 1;
	}
	
	public static int getDefence(int zombieId) {
		if (true) {
			return 1;
		}
		switch (zombieId) {
		case 2501:
		case 2502:
		case 2503:
			return 25;
		case 2504:
		case 2505:
		case 2506:
			return 50;
		case 2507:
		case 2508:
		case 2509:
			return 100;
		}
		return 1;
	}
	
	public static int getHitpoints(int zombieId) {
		if (true) {
			return 1;
		}
		switch (zombieId) {
		case 2501:
		case 2502:
		case 2503:
			return 50;
		case 2504:
		case 2505:
		case 2506:
			return 100;
		case 2507:
		case 2508:
		case 2509:
			return 200;
		}
		return 1;
	}
	
	public static int getMaxHit(int zombieId) {
		if (true) {
			return 1;
		}
		switch (zombieId) {
		case 2501:
		case 2502:
		case 2503:
			return 10;
		case 2504:
		case 2505:
		case 2506:
			return 25;
		case 2507:
		case 2508:
		case 2509:
			return 50;
		}
		return 1;
	}
	
	public static boolean inMinigame(int x, int y) {
		int x1 = SOUTH_WEST_CORNER_POSITION.getX() - 10;
		int x2 = NORTH_EAST_CORNER_POSITION.getX() + 10;
		int y1 = SOUTH_WEST_CORNER_POSITION.getY() - 10;
		int y2 = NORTH_EAST_CORNER_POSITION.getY() + 10;
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}
	
	public static final Position[] ZOMBIE_SPAWN_POSITIONS;

	public static final Position[] DOOR_POSITIONS = {
		new Position(3659, 3471), new Position(3674, 3472), new Position(3676, 3476),  new Position(3670, 3497), 
	};
	
	static {
		List<Position> positions = new ArrayList<>();
		for (int x = SOUTH_WEST_CORNER_POSITION.getX(); x <= NORTH_EAST_CORNER_POSITION.getX(); x++) {
			for (int y = SOUTH_WEST_CORNER_POSITION.getY(); y <= NORTH_EAST_CORNER_POSITION.getY(); y++) {
				if (Region.getClipping(x, y, 0) != 0) {
					continue;
				}
				positions.add(new Position(x, y));
			}
		}
		ZOMBIE_SPAWN_POSITIONS = positions.toArray(new Position[positions.size()]);
	}

}