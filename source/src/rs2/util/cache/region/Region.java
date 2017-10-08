package rs2.util.cache.region;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

import rs2.Server;
import rs2.abyssalps.content.minigame.kraken.KrakenConstants;
import rs2.abyssalps.content.minigame.zulrah.ZulrahConstants;
import rs2.abyssalps.fs.Archive;
import rs2.abyssalps.fs.FileSystem;
import rs2.abyssalps.fs.RegionDefinition;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.objects.Objects;
import rs2.abyssalps.model.objects.WorldObject;
import rs2.abyssalps.model.player.Client;
import rs2.util.cache.Buffer;
import rs2.util.cache.ByteStream;
import rs2.util.cache.FileOperations;
import rs2.util.cache.def.ObjectDef;
import rs2.util.cache.def.ObjectType;
import rs2.world.TileControl;

public class Region {

	/**
	 * An array of {@link WorldObject} objects that will be added after the maps
	 * have been loaded.
	 */
	private static final WorldObject[] EXISTANT_OBJECTS = { new WorldObject(25808, 2729, 3494, 0, 0), new WorldObject(25808, 2728, 3494, 0, 0), new WorldObject(25808, 2727, 3494, 0, 0),
			new WorldObject(25808, 2724, 3494, 0, 0), new WorldObject(25808, 2722, 3494, 0, 0), new WorldObject(25808, 2721, 3494, 0, 0), new WorldObject(11764, 2513, 4771, 0, 0),
			new WorldObject(11758, 2511, 4773, 0, 0), new WorldObject(11762, 2512, 4778, 0, 0), new WorldObject(11755, 2512, 4780, 0, 0), new WorldObject(11756, 2513, 4782, 0, 0),
			new WorldObject(1276, 2516, 4785, 0, 0), new WorldObject(11744, 2525, 4777, 0, 0), new WorldObject(13712, 2542, 4780, 0, 0), new WorldObject(13708, 2542, 4779, 0, 0),
			new WorldObject(13710, 2542, 4778, 0, 0), new WorldObject(13714, 2542, 4777, 0, 0), new WorldObject(13718, 2542, 4776, 0, 0), new WorldObject(14168, 2542, 4775, 0, 0),
			new WorldObject(2104, 2542, 4774, 0, 0), new WorldObject(2030, 2539, 4782, 0, 0),

			new WorldObject(409, 3085, 3508, 0, 0), new WorldObject(26258, 3085, 3511, 0, 0), new WorldObject(2031, 2539, 4773, 0, 0),

	};

	private static final int[] PASSABLE_OBJECTS = {
			5244
	};
	
	public static boolean blockedNorthNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + i, y + size, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x, y + 1, z) != 0);
	}

	public static boolean blockedEastNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + size, y + i, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x + 1, y, z) != 0);
	}

	public static boolean blockedSouthNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + i, y - 1, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x, y - 1, z) != 0);
	}

	public static boolean blockedWestNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x - 1, y + i, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x - 1, y, z) != 0);
	}

	public static boolean blockedNorthEastNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + size + 1, y + i + 1, z) != 0);
				boolean clipped2 = (getClipping(x + i + 1, y + size + 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x + 1, y + 1, z) != 0);
	}

	public static boolean blockedNorthWestNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x - 1, y + i + 1, z) != 0);
				boolean clipped2 = (getClipping(x + i - 1, y + size + 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x - 1, y + 1, z) != 0);
	}

	public static boolean blockedSouthEastNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + size + 1, y + i - 1, z) != 0);
				boolean clipped2 = (getClipping(x + i + 1, y - 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x + 1, y - 1, z) != 0);
	}

	public static boolean blockedSouthWestNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x - 1, y + i - 1, z) != 0);
				boolean clipped2 = (getClipping(x + i - 1, y - 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x - 1, y - 1, z) != 0);
	}

	public static boolean pathBlocked(NPC attacker, Client victim) {

		double offsetX = Math.abs(attacker.absX - victim.absX);
		double offsetY = Math.abs(attacker.absY - victim.absY);

		int distance = TileControl.calculateDistance(attacker, victim);

		if (distance == 0) {
			return true;
		}

		offsetX = offsetX > 0 ? offsetX / distance : 0;
		offsetY = offsetY > 0 ? offsetY / distance : 0;

		int[][] path = new int[distance][5];

		int curX = attacker.absX;
		int curY = attacker.absY;
		int next = 0;
		int nextMoveX = 0;
		int nextMoveY = 0;

		double currentTileXCount = 0.0;
		double currentTileYCount = 0.0;

		while (distance > 0) {
			distance--;
			nextMoveX = 0;
			nextMoveY = 0;
			if (curX > victim.absX) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX--;
					curX--;
					currentTileXCount -= offsetX;
				}
			} else if (curX < victim.absX) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX++;
					curX++;
					currentTileXCount -= offsetX;
				}
			}
			if (curY > victim.absY) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY--;
					curY--;
					currentTileYCount -= offsetY;
				}
			} else if (curY < victim.absY) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY++;
					curY++;
					currentTileYCount -= offsetY;
				}
			}
			path[next][0] = curX;
			path[next][1] = curY;
			path[next][2] = attacker.heightLevel;// getHeightLevel();
			path[next][3] = nextMoveX;
			path[next][4] = nextMoveY;
			next++;
		}
		for (int i = 0; i < path.length; i++) {
			if (!Region.getClipping(path[i][0], path[i][1], path[i][2], path[i][3], path[i][4])) { // clipped projectiles by
																										// aleksandr
				return true;
			}
		}
		return false;
	}

	/**
	 * A map containing each region as the key, and a Collection of real world
	 * objects as the value.
	 */
	private static HashMap<Integer, ArrayList<WorldObject>> worldObjects = new HashMap<>();

	/**
	 * Determines if an object is real or not. If the Collection of regions and
	 * real objects contains the properties passed in the parameters then the
	 * object will be determined real
	 * 
	 * @param id
	 *            the id of the object
	 * @param x
	 *            the x coordinate of the object
	 * @param y
	 *            the y coordinate of the object
	 * @param height
	 *            the height of the object
	 * @return
	 */
	public static boolean isWorldObject(int id, int x, int y, int height) {
		Region region = getRegion(x, y);
		if (region == null) {
			return true;
		}
		Collection<WorldObject> regionObjects = worldObjects.get(region.id);
		if (regionObjects == null) {
			return true;
		}
		Optional<WorldObject> exists = regionObjects.stream().filter(object -> object.id == id && object.x == x && object.y == y && object.z == height).findFirst();
		return exists.isPresent();
	}

	/**
	 * Determines if an object is real or not. If the Collection of regions and
	 * real objects contains the properties passed in the parameters then the
	 * object will be determined real
	 * 
	 * @param x
	 *            the x coordinate of the object
	 * @param y
	 *            the y coordinate of the object
	 * @param height
	 *            the height of the object
	 * @return
	 */
	public static boolean solidObjectExists(int x, int y, int height) {
		Region region = getRegion(x, y);
		if (region == null) {
			return false;
		}
		Collection<WorldObject> regionObjects = worldObjects.get(region.id);
		if (regionObjects == null) {
			return false;
		}
		Optional<WorldObject> exists = regionObjects.stream().filter(object -> object.x == x && object.y == y && object.z == height).findFirst();
		return exists.isPresent();
	}

	public static Optional<WorldObject> getWorldObject(int id, int x, int y, int height) {
		Region region = getRegion(x, y);
		if (region == null) {
			return Optional.empty();
		}
		Collection<WorldObject> regionObjects = worldObjects.get(region.id);
		if (regionObjects == null) {
			return Optional.empty();
		}
		Optional<WorldObject> exists = regionObjects.stream().filter(object -> object.id == id && object.x == x && object.y == y && object.z == height).findFirst();
		return exists;
	}

	public static WorldObject getWorldObject(ObjectType type, int x, int y, int height) {
		Region region = getRegion(x, y);
		if (region == null) {
			return null;
		}
		Collection<WorldObject> regionObjects = worldObjects.get(region.id);
		if (regionObjects == null) {
			return null;
		}
		Optional<WorldObject> optional = regionObjects.stream().filter(object -> object.type == type.getId() && object.x == x && object.y == y && object.z == height).findFirst();
		return optional.isPresent() ? optional.get() : null;
	}

	/**
	 * Determines if an object exists in a region
	 * 
	 * @param region
	 *            the region
	 * @param id
	 *            the object id
	 * @param x
	 *            ` the object x pos
	 * @param y
	 *            the object y pos
	 * @param height
	 *            the object z pos
	 * @return true if the object exists in the region, otherwise false
	 */
	private static boolean objectExists(int region, int id, int x, int y, int height) {
		List<WorldObject> objects = worldObjects.get(region);
		for (WorldObject object : objects) {
			if (object == null) {
				continue;
			}
			if (object.getId() == id && object.getX() == x && object.getY() == y && object.getZ() == height) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds a {@link WorldObject} to the {@link worldObjects} map based on the
	 * x, y, height, and identification of the object.
	 * 
	 * @param id
	 *            the id of the object
	 * @param x
	 *            the x position of the object
	 * @param y
	 *            the y position of the object
	 * @param height
	 *            the height of the object
	 */
	public static void removeWorldObject(int id, int x, int y, int height) {
		Region region = getRegion(x, y);
		if (region == null) {
			return;
		}
		int regionId = region.id;
		ArrayList<WorldObject> object = new ArrayList<>(1);
		object.add(new WorldObject(id, x, y, height, 1));
		worldObjects.put(regionId, object);
	}

	public static void addWorldObject(int id, int x, int y, int height, int face) {
		Region region = getRegion(x, y);
		if (region == null) {
			return;
		}
		int regionId = region.id;
		if (worldObjects.containsKey(regionId)) {
			if (objectExists(regionId, id, x, y, height)) {
				return;
			}
			worldObjects.get(regionId).add(new WorldObject(id, x, y, height, face));
		} else {
			ArrayList<WorldObject> object = new ArrayList<>(1);
			object.add(new WorldObject(id, x, y, height, face));
			worldObjects.put(regionId, object);
		}
	}

	/**
	 * A convenience method for lamda expressions
	 * 
	 * @param object
	 *            the world object being added
	 */
	private static void addWorldObject(WorldObject object) {
		addWorldObject(object.getId(), object.getX(), object.getY(), object.getZ(), object.getFace());
	}

	public static boolean checkClip(Client c, int endX, int endY) {
		int x = c.getX(), y = c.getY();
		int xDistance = endX - x;
		int yDistance = endY - y;

		int currentX = 0, currentY = 0;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;

		if (xDistance < 0) {
			dx1 = -1;
		} else if (xDistance > 0) {
			dx1 = 1;
		}
		if (yDistance < 0) {
			dy1 = -1;
		} else if (yDistance > 0) {
			dy1 = 1;
		}
		if (xDistance < 0) {
			dx2 = -1;
		} else if (xDistance > 0) {
			dx2 = 1;
		}
		int longest = Math.abs(xDistance);
		int shortest = Math.abs(yDistance);
		if (!(longest > shortest)) {
			longest = Math.abs(yDistance);
			shortest = Math.abs(xDistance);
			if (yDistance < 0) {
				dy2 = -1;
			} else if (yDistance > 0) {
				dy2 = 1;
			}
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			currentX = x;
			currentY = y;
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
			if (!getClipping(currentX, currentY, c.heightLevel, x - currentX, y - currentY, true)) {
				return false;
			}
			if (x == endX && y == endY) {
				return true;
			}
		}
		return true;
	}

	public static Region getRegion(int x, int y) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;
		for (Region region : regions) {
			if (region.id() == regionId) {
				return region;
			}
		}
		return null;
	}

	private void addClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips[height] == null) {
			clips[height] = new int[64][64];
		}
		clips[height][x - regionAbsX][y - regionAbsY] |= shift;
	}

	/*
	 * Deleting more objects at the same time
	 * 
	 * @param toDelete, 2D array for what needs to be deleted
	 */
	public static void removeClipping(int[][] toDelete, int height) {
		int[] regionDetails = new int[3];
		for (int i = 0; i < toDelete.length; i++) {
			regionDetails[i] = (((toDelete[i][0] >> 3) / 8) << 8) + ((toDelete[i][1] >> 3) / 8);
		}
		int alreadyFound = 0;
		for (Region r : regions) {
			for (int i = 0; i < toDelete.length; i++) {
				if (alreadyFound == toDelete.length)
					return;
				if (r.id() == regionDetails[i]) {
					r.removeClip(toDelete[i][0], toDelete[i][1], height);
					alreadyFound++;
				}
			}
		}
	}

	private int getClip(int x, int y, int height) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (height == -1)
			return 0;
		if (clips[height] == null) {
			return 0;
		}
		return clips[height][x - regionAbsX][y - regionAbsY];
	}

	public void removeClip(int x, int y, int height) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (height > 3)
			height %= 4;
		if (height == -1)
			return;
		if (clips[height] == null) {
			return;
		}
		clips[height][x - regionAbsX][y - regionAbsY] = 0;
	}
	
	public static boolean blockedNorth(int x, int y, int z, int size) {
		for (int i = 0; i < size; i++) {
			if (blockedNorth(x + i, y + size - 1, z)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean blockedNorth(int x, int y, int z) {
		return (getClipping(x, y + 1, z) & 0x1280120) != 0;
	}

	public static boolean blockedEast(int x, int y, int z, int size) {
		for (int i = 0; i < size; i++) {
			if (blockedEast(x + size - 1, y + i, z)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean blockedEast(int x, int y, int z) {
		return (getClipping(x + 1, y, z) & 0x1280180) != 0;
	}

	public static boolean blockedSouth(int x, int y, int z, int size) {
		for (int i = 0; i < size; i++) {
			if (blockedSouth(x + i, y, z)) {
				return true;
			}
		}
		return false;
	}

	public static boolean blockedSouth(int x, int y, int z) {
		return (getClipping(x, y - 1, z) & 0x1280102) != 0;
	}

	public static boolean blockedWest(int x, int y, int z, int size) {
		for (int i = 0; i < size; i++) {
			if (blockedWest(x, y + i, z)) {
				return true;
			}
		}
		return false;
	}

	public static boolean blockedWest(int x, int y, int z) {
		return (getClipping(x - 1, y, z) & 0x1280108) != 0;
	}

	public boolean blockedNorthEast(int x, int y, int z) {
		return (getClipping(x + 1, y + 1, z) & 0x12801e0) != 0;
	}

	public boolean blockedNorthWest(int x, int y, int z) {
		return (getClipping(x - 1, y + 1, z) & 0x1280138) != 0;
	}

	public boolean blockedSouthEast(int x, int y, int z) {
		return (getClipping(x + 1, y - 1, z) & 0x1280183) != 0;
	}

	public boolean blockedSouthWest(int x, int y, int z) {
		return (getClipping(x - 1, y - 1, z) & 0x128010e) != 0;
	}

	private static void addClipping(int x, int y, int height, int shift) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				r.addClip(x, y, height, shift);
				break;
			}
		}
	}

	private static Region[] regions;
	private int id;
	private int[][][] clips = new int[4][][];
	private boolean members = false;

	public Region(int id, boolean members) {
		this.id = id;
		this.members = members;
	}

	public int id() {
		return id;
	}

	public boolean members() {
		return members;
	}

	public static boolean isMembers(int x, int y, int height) {
		if (x >= 3272 && x <= 3320 && y >= 2752 && y <= 2809)
			return false;
		if (x >= 2640 && x <= 2677 && y >= 2638 && y <= 2679)
			return false;
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				return r.members();
			}
		}
		return false;
	}

	private static void addClippingForVariableObject(int x, int y, int height, int type, int direction, boolean flag) {
		if (type == 0) {
			if (direction == 0) {
				addClipping(x, y, height, 128);
				addClipping(x - 1, y, height, 8);
			} else if (direction == 1) {
				addClipping(x, y, height, 2);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 2) {
				addClipping(x, y, height, 8);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 3) {
				addClipping(x, y, height, 32);
				addClipping(x, y - 1, height, 2);
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				addClipping(x, y, height, 1);
				addClipping(x - 1, y, height, 16);
			} else if (direction == 1) {
				addClipping(x, y, height, 4);
				addClipping(x + 1, y + 1, height, 64);
			} else if (direction == 2) {
				addClipping(x, y, height, 16);
				addClipping(x + 1, y - 1, height, 1);
			} else if (direction == 3) {
				addClipping(x, y, height, 64);
				addClipping(x - 1, y - 1, height, 4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				addClipping(x, y, height, 130);
				addClipping(x - 1, y, height, 8);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 1) {
				addClipping(x, y, height, 10);
				addClipping(x, y + 1, height, 32);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 2) {
				addClipping(x, y, height, 40);
				addClipping(x + 1, y, height, 128);
				addClipping(x, y - 1, height, 2);
			} else if (direction == 3) {
				addClipping(x, y, height, 160);
				addClipping(x, y - 1, height, 2);
				addClipping(x - 1, y, height, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addClipping(x, y, height, 65536);
					addClipping(x - 1, y, height, 4096);
				} else if (direction == 1) {
					addClipping(x, y, height, 1024);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 2) {
					addClipping(x, y, height, 4096);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 3) {
					addClipping(x, y, height, 16384);
					addClipping(x, y - 1, height, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addClipping(x, y, height, 512);
					addClipping(x - 1, y + 1, height, 8192);
				} else if (direction == 1) {
					addClipping(x, y, height, 2048);
					addClipping(x + 1, y + 1, height, 32768);
				} else if (direction == 2) {
					addClipping(x, y, height, 8192);
					addClipping(x + 1, y + 1, height, 512);
				} else if (direction == 3) {
					addClipping(x, y, height, 32768);
					addClipping(x - 1, y - 1, height, 2048);
				}
			} else if (type == 2) {
				if (direction == 0) {
					addClipping(x, y, height, 66560);
					addClipping(x - 1, y, height, 4096);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 1) {
					addClipping(x, y, height, 5120);
					addClipping(x, y + 1, height, 16384);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 2) {
					addClipping(x, y, height, 20480);
					addClipping(x + 1, y, height, 65536);
					addClipping(x, y - 1, height, 1024);
				} else if (direction == 3) {
					addClipping(x, y, height, 81920);
					addClipping(x, y - 1, height, 1024);
					addClipping(x - 1, y, height, 4096);
				}
			}
		}
	}

	private static void addClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag) {
		int clipping = 256;
		if (flag) {
			clipping += 0x20000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				addClipping(i, i2, height, clipping);
			}
		}
	}

	public static void addObject(int objectId, int x, int y, int height, int type, int direction, boolean startUp) {
		ObjectDef def = ObjectDef.getObjectDef(objectId);

		if (def == null) {
			return;
		}

		int xLength;
		int yLength;
		if (direction != 1 && direction != 3) {
			xLength = def.xLength();
			yLength = def.yLength();
		} else {
			xLength = def.yLength();
			yLength = def.xLength();
		}
		if (type == 22) {
			if (def.hasActions() && def.aBoolean767()) {
				addClipping(x, y, height, 0x200000);
			}
		} else if (type >= 9) {
			if (def.aBoolean767()) {
				addClippingForSolidObject(x, y, height, xLength, yLength, def.solid());
			}
		} else if (type >= 0 && type <= 3) {
			if (def.aBoolean767()) {
				addClippingForVariableObject(x, y, height, type, direction, def.solid());
			}
		}
	}

	public static Region getRegion(int x, int y, int height) {
		if (height > 3)
			height %= 4;
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				return r;
			}
		}
		return null;
	}

	public static int getClipping(int x, int y, int height) {
		if (height > 3)
			height %= 4;
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				return r.getClip(x, y, height);
			}
		}
		return 0;
	}

	public static void removeClipping(int x, int y, int height) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				r.removeClip(x, y, height);
				return;
			}
		}
	}
	
	public static boolean getClipping(int x, int y, int height, int moveTypeX, int moveTypeY) {
		try {
			if (height > 3)
				height = 0;
			int checkX = (x + moveTypeX);
			int checkY = (y + moveTypeY);
			if (moveTypeX == -1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280108) == 0;
			else if (moveTypeX == 1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280180) == 0;
			else if (moveTypeX == 0 && moveTypeY == -1)
				return (getClipping(x, y, height) & 0x1280102) == 0;
			else if (moveTypeX == 0 && moveTypeY == 1)
				return (getClipping(x, y, height) & 0x1280120) == 0;
			else if (moveTypeX == -1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x128010e) == 0 && (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0
						&& (getClipping(checkX - 1, checkY, height) & 0x1280102) == 0);
			else if (moveTypeX == 1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x1280183) == 0 && (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0
						&& (getClipping(checkX, checkY - 1, height) & 0x1280102) == 0);
			else if (moveTypeX == -1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x1280138) == 0 && (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0
						&& (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0);
			else if (moveTypeX == 1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x12801e0) == 0 && (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0
						&& (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0);
			else {
				System.err.println("[FATAL ERROR]: At getClipping: " + x + ", " + y + ", " + height + ", " + moveTypeX + ", " + moveTypeY);
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

	public static Map<Integer, RegionDefinition> mapDefinitionList = new HashMap<>();

	private static void load(Position pos) {
		int hash = pos.getRegion();
		load(hash);
	}

	public static void load(Client player) {
		load(player.getPosition());
	}

	private static void load(int id) {
		RegionDefinition def = mapDefinitionList.get(id);
		if (def == null || def.loaded())
			return;
		try {
			byte[] file1 = Server.fileSystem.getDecompressedFile(FileSystem.MAP_INDEX, def.getObjectFile());
			byte[] file2 = Server.fileSystem.getDecompressedFile(FileSystem.MAP_INDEX, def.getTerrainFile());
			if (file1 == null || file2 == null) {
				return;
			}
			try {
				loadMaps(id, new ByteStream(file1), new ByteStream(file2));
			} catch (Exception e) {
				// e.printStackTrace();
				System.err.println("Error loading map region: " + id + "-" + def.getTerrainFile() + "~" + def.getObjectFile());
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		def.setLoaded(true);
	}

	/**
	 * Parses {@link RegionDefinition}s from the specified {@link FileSystem}.
	 * 
	 * @param fs
	 *            The file system.
	 * @return A {@link Map} of parsed map definitions.
	 * @throws IOException
	 *             If some I/O error occurs.
	 */
	public static Map<Integer, RegionDefinition> parseMapIndex(FileSystem fs) throws IOException {
		System.out.println("Loading map definitions...");
		Archive archive = fs.getArchive(FileSystem.MANIFEST_ARCHIVE);
		ByteBuffer buffer = archive.getData("map_index");
		Map<Integer, RegionDefinition> defs = new HashMap<>();

		int count = buffer.getShort() & 0xFFFF;
		regions = new Region[count];
		for (int i = 0; i < count; i++) {
			int hash = buffer.getShort() & 0xFFFF;
			int terrainFile = buffer.getShort() & 0xFFFF;
			int objectFile = buffer.getShort() & 0xFFFF;
			defs.put(hash, new RegionDefinition(hash, terrainFile, objectFile));
			regions[i] = new Region(hash, false);
		}
		System.out.println("Loaded " + count + " map definitions.");
		return defs;
	}

	public static void load() {
		try {
			mapDefinitionList = parseMapIndex(Server.fileSystem);
			/*
			 * Buffer buffer = new Buffer(
			 * FileOperations.readFile("./Data/cache/world/map_index.dat")); int
			 * size = buffer.readUnsignedWord(); regions = new Region[size];
			 * int[] regionIds = new int[size]; int[] mapGroundFileIds = new
			 * int[size]; int[] mapObjectsFileIds = new int[size]; boolean[]
			 * isMembers = new boolean[size]; for (int i = 0; i < size; i++) {
			 * regionIds[i] = buffer.readUnsignedWord(); mapGroundFileIds[i] =
			 * buffer.readUnsignedWord(); mapObjectsFileIds[i] =
			 * buffer.readUnsignedWord(); // isMembers[i] =
			 * in.readUnsignedWord() == 0; mapAmount++; regions[i] = new
			 * Region(regionIds[i], isMembers[i]); } System.err.println(
			 * "Loaded " + mapAmount + " maps"); for (int i = 0; i < size; i++)
			 * { byte[] file1 =
			 * FileOperations.readFile("./Data/cache/world/map/" +
			 * mapObjectsFileIds[i] + ".dat"); byte[] file2 =
			 * FileOperations.readFile("./Data/cache/world/map/" +
			 * mapGroundFileIds[i] + ".dat"); if (file1 == null || file2 ==
			 * null) { continue; } if (mapObjectsFileIds[i] == 890)
			 * System.out.println("reg: "+regionIds[i]); try {
			 * loadMaps(regionIds[i], new ByteStream(file1), new
			 * ByteStream(file2)); } catch (Exception e) { e.printStackTrace();
			 * System.err.println("Error loading map region: " + regionIds[i]);
			 * } }
			 */
			Arrays.asList(EXISTANT_OBJECTS).forEach(o -> addWorldObject(o));
			System.err.println("Loading clipping - please wait.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadMaps(int regionId, ByteStream str1, ByteStream str2) {
		int absX = (regionId >> 8) * 64;
		int absY = (regionId & 0xff) * 64;
		int[][][] someArray = new int[4][64][64];
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					while (true) {
						int v = str2.getUByte();
						if (v == 0) {
							break;
						} else if (v == 1) {
							str2.skip(1);
							break;
						} else if (v <= 49) {
							str2.skip(1);
						} else if (v <= 81) {
							someArray[i][i2][i3] = v - 49;
						}
					}
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					if ((someArray[i][i2][i3] & 1) == 1) {
						int height = i;
						if ((someArray[1][i2][i3] & 2) == 2) {
							height--;
						}
						if (height >= 0 && height <= 3) {
							addClipping(absX + i2, absY + i3, height, 0x200000);
						}
					}
				}
			}
		}
		int objectId = -1;
		int incr;
		while ((incr = str1.getUSmart()) != 0) {
			objectId += incr;
			int location = 0;
			int incr2;
			MainLoop: while ((incr2 = str1.getUSmart()) != 0) {
				location += incr2 - 1;
				int localX = (location >> 6 & 0x3f);
				int localY = (location & 0x3f);
				int height = location >> 12;
				int objectData = str1.getUByte();
				int type = objectData >> 2;
				int direction = objectData & 0x3;
				for (int id : PASSABLE_OBJECTS) {
					if (objectId == id) {
						continue MainLoop;
					}
				}
				if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
					continue;
				}
				if ((someArray[1][localX][localY] & 2) == 2) {
					height--;
				}
				if (height >= 0 && height <= 3) {
					addObject(objectId, absX + localX, absY + localY, height, type, direction, true);
					addWorldObject(objectId, absX + localX, absY + localY, height, direction);
				}
			}
		}
	}

	public static byte[] getBuffer(File f) throws Exception {
		if (!f.exists())
			return null;
		byte[] buffer = new byte[(int) f.length()];
		DataInputStream dis = new DataInputStream(new FileInputStream(f));
		dis.readFully(buffer);
		dis.close();
		byte[] gzipInputBuffer = new byte[999999];
		int bufferlength = 0;
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(buffer));
		do {
			if (bufferlength == gzipInputBuffer.length) {
				System.out.println("Error inflating data.\nGZIP buffer overflow.");
				break;
			}
			int readByte = gzip.read(gzipInputBuffer, bufferlength, gzipInputBuffer.length - bufferlength);
			if (readByte == -1)
				break;
			bufferlength += readByte;
		} while (true);
		byte[] inflated = new byte[bufferlength];
		System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferlength);
		buffer = inflated;
		if (buffer.length < 10)
			return null;
		return buffer;
	}

	public static int[] getNextStep(int baseX, int baseY, int toX, int toY, int height, int xLength, int yLength) {
		int moveX = 0;
		int moveY = 0;
		if (baseX - toX > 0) {
			moveX--;
		} else if (baseX - toX < 0) {
			moveX++;
		}
		if (baseY - toY > 0) {
			moveY--;
		} else if (baseY - toY < 0) {
			moveY++;
		}
		if (canMove(baseX, baseY, baseX + moveX, baseY + moveY, height, xLength, yLength)) {
			return new int[] { baseX + moveX, baseY + moveY };
		} else if (moveX != 0 && canMove(baseX, baseY, baseX + moveX, baseY, height, xLength, yLength)) {
			return new int[] { baseX + moveX, baseY };
		} else if (moveY != 0 && canMove(baseX, baseY, baseX, baseY + moveY, height, xLength, yLength)) {
			return new int[] { baseX, baseY + moveY };
		}
		return new int[] { baseX, baseY };
	}

	public static boolean canMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
		int diffX = endX - startX;
		int diffY = endY - startY;
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int ii = 0; ii < max; ii++) {
			int currentX = endX - diffX;
			int currentY = endY - diffY;
			for (int i = 0; i < xLength; i++) {
				for (int i2 = 0; i2 < yLength; i2++) {
					if (diffX < 0 && diffY < 0) {
						if ((getClipping(currentX + i - 1, currentY + i2 - 1, height) & 0x128010e) != 0 || (getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0
								|| (getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY > 0) {
						if ((getClipping(currentX + i + 1, currentY + i2 + 1, height) & 0x12801e0) != 0 || (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
								|| (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY > 0) {
						if ((getClipping(currentX + i - 1, currentY + i2 + 1, height) & 0x1280138) != 0 || (getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0
								|| (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY < 0) {
						if ((getClipping(currentX + i + 1, currentY + i2 - 1, height) & 0x1280183) != 0 || (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
								|| (getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY == 0) {
						if ((getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY == 0) {
						if ((getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY > 0) {
						if ((getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY < 0) {
						if ((getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					}
				}
			}
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
		}
		return true;
	}

	private static boolean ignoreCanAttack(int npcType) {
		switch (npcType) {
		case ZulrahConstants.GREEN_ZULRAH_ID:
		case ZulrahConstants.BLUE_ZULRAH_ID:
		case ZulrahConstants.RED_ZULRAH_ID:
		case ZulrahConstants.SNAKELING_ID:
		case KrakenConstants.KRAKEN_ID:
		case KrakenConstants.KRAKEN_POOL_ID:
		case KrakenConstants.TENTACLE_POOL_ID:
		case KrakenConstants.TENTACLE_ID:
		case 5947:
		case 5961:
			return true;

		default:
			return false;
		}
	}

	public static boolean canAttack(NPC a, Client b) {
		if (a.heightLevel != b.heightLevel) {
			return false;
		}
		if (!canMove(a.getX(), a.getY(), b.getX(), b.getY(), a.heightLevel % 4, 1, 1)) {
			return false;
		}
		return true;
	}

	public static void findRoute(Client c, int destX, int destY, boolean moveNear, int xLength, int yLength) {
		long start = System.currentTimeMillis();
		/*
		 * if (destX == c.pathFinalX && destY == c.pathFinalY) { return; }
		 */
		if (destX == c.getX() && destY == c.getY() && !moveNear || !c.goodDistance(c.getX(), c.getY(), destX, destY, 20)) {
			return;
		}
		destX = destX - 8 * c.mapRegionX;
		destY = destY - 8 * c.mapRegionY;
		int[][] via = new int[104][104];
		int[][] cost = new int[104][104];
		LinkedList<Integer> tileQueueX = new LinkedList<Integer>();
		LinkedList<Integer> tileQueueY = new LinkedList<Integer>();
		for (int xx = 0; xx < 104; xx++) {
			for (int yy = 0; yy < 104; yy++) {
				cost[xx][yy] = 99999999;
			}
		}
		int curX = c.absX - c.mapRegionX * 8;
		int curY = c.absY - c.mapRegionY * 8;
		via[curX][curY] = 99;
		cost[curX][curY] = 0;
		int head = 0;
		int tail = 0;
		tileQueueX.add(curX);
		tileQueueY.add(curY);
		boolean foundPath = false;
		int pathLength = 4000;
		while (tail != tileQueueX.size() && tileQueueX.size() < pathLength) {
			curX = tileQueueX.get(tail);
			curY = tileQueueY.get(tail);
			int curAbsX = c.getMapRegionX() * 8 + curX;
			int curAbsY = c.getMapRegionY() * 8 + curY;
			if (curX == destX && curY == destY) {
				foundPath = true;
				break;
			}
			/*
			 * if (xLength != 0 && yLength != 0 && method422(curAbsX, curAbsY,
			 * c.heightLevel % 4, absDestX, absDestY, xLength, yLength)) {
			 * foundPath = true; break; }
			 */
			tail = (tail + 1) % pathLength;
			int thisCost = cost[curX][curY] + 1;
			if (curY > 0 && via[curX][curY - 1] == 0 && (Region.getClipping(curAbsX, curAbsY - 1, c.heightLevel % 4) & 0x1280102) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY - 1);
				via[curX][curY - 1] = 1;
				cost[curX][curY - 1] = thisCost;
			}
			if (curX > 0 && via[curX - 1][curY] == 0 && (Region.getClipping(curAbsX - 1, curAbsY, c.heightLevel % 4) & 0x1280108) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY);
				via[curX - 1][curY] = 2;
				cost[curX - 1][curY] = thisCost;
			}
			if (curY < 104 - 1 && via[curX][curY + 1] == 0 && (Region.getClipping(curAbsX, curAbsY + 1, c.heightLevel % 4) & 0x1280120) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY + 1);
				via[curX][curY + 1] = 4;
				cost[curX][curY + 1] = thisCost;
			}
			if (curX < 104 - 1 && via[curX + 1][curY] == 0 && (Region.getClipping(curAbsX + 1, curAbsY, c.heightLevel % 4) & 0x1280180) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY);
				via[curX + 1][curY] = 8;
				cost[curX + 1][curY] = thisCost;
			}
			if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0 && (Region.getClipping(curAbsX - 1, curAbsY - 1, c.heightLevel % 4) & 0x128010e) == 0
					&& (Region.getClipping(curAbsX - 1, curAbsY, c.heightLevel % 4) & 0x1280108) == 0 && (Region.getClipping(curAbsX, curAbsY - 1, c.heightLevel % 4) & 0x1280102) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY - 1);
				via[curX - 1][curY - 1] = 3;
				cost[curX - 1][curY - 1] = thisCost;
			}
			if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0 && (Region.getClipping(curAbsX - 1, curAbsY + 1, c.heightLevel % 4) & 0x1280138) == 0
					&& (Region.getClipping(curAbsX - 1, curAbsY, c.heightLevel % 4) & 0x1280108) == 0 && (Region.getClipping(curAbsX, curAbsY + 1, c.heightLevel % 4) & 0x1280120) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY + 1);
				via[curX - 1][curY + 1] = 6;
				cost[curX - 1][curY + 1] = thisCost;
			}
			if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0 && (Region.getClipping(curAbsX + 1, curAbsY - 1, c.heightLevel % 4) & 0x1280183) == 0
					&& (Region.getClipping(curAbsX + 1, curAbsY, c.heightLevel % 4) & 0x1280180) == 0 && (Region.getClipping(curAbsX, curAbsY - 1, c.heightLevel % 4) & 0x1280102) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY - 1);
				via[curX + 1][curY - 1] = 9;
				cost[curX + 1][curY - 1] = thisCost;
			}
			if (curX < 104 - 1 && curY < 104 - 1 && via[curX + 1][curY + 1] == 0 && (Region.getClipping(curAbsX + 1, curAbsY + 1, c.heightLevel % 4) & 0x12801e0) == 0
					&& (Region.getClipping(curAbsX + 1, curAbsY, c.heightLevel % 4) & 0x1280180) == 0 && (Region.getClipping(curAbsX, curAbsY + 1, c.heightLevel % 4) & 0x1280120) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY + 1);
				via[curX + 1][curY + 1] = 12;
				cost[curX + 1][curY + 1] = thisCost;
			}
		}
		if (!foundPath) {
			if (moveNear) {
				int i_223_ = 1000;
				int thisCost = 100;
				int i_225_ = 10;
				for (int x = destX - i_225_; x <= destX + i_225_; x++) {
					for (int y = destY - i_225_; y <= destY + i_225_; y++) {
						if (x >= 0 && y >= 0 && x < 104 && y < 104 && cost[x][y] < 100) {
							int i_228_ = 0;
							if (x < destX) {
								i_228_ = destX - x;
							} else if (x > destX + xLength - 1) {
								i_228_ = x - (destX + xLength - 1);
							}
							int i_229_ = 0;
							if (y < destY) {
								i_229_ = destY - y;
							} else if (y > destY + yLength - 1) {
								i_229_ = y - (destY + yLength - 1);
							}
							int i_230_ = i_228_ * i_228_ + i_229_ * i_229_;
							if (i_230_ < i_223_ || (i_230_ == i_223_ && (cost[x][y] < thisCost))) {
								i_223_ = i_230_;
								thisCost = cost[x][y];
								curX = x;
								curY = y;
							}
						}
					}
				}
				if (i_223_ == 1000) {
					return;
				}
			} else {
				// c.lastRouteX = destX;
				// c.lastRouteY = destY;
				c.followId = -1;
				c.followId2 = -1;
				return;
			}
		}
		tail = 0;
		tileQueueX.set(tail, curX);
		tileQueueY.set(tail++, curY);
		int l5;
		for (int j5 = l5 = via[curX][curY]; curX != c.getLocalX() || curY != c.getLocalY(); j5 = via[curX][curY]) {
			if (j5 != l5) {
				l5 = j5;
				tileQueueX.set(tail, curX);
				tileQueueY.set(tail++, curY);
			}
			if ((j5 & 2) != 0) {
				curX++;
			} else if ((j5 & 8) != 0) {
				curX--;
			}
			if ((j5 & 1) != 0) {
				curY++;
			} else if ((j5 & 4) != 0) {
				curY--;
			}
		}
		int size = tail--;
		c.resetWalkingQueue();
		c.addToWalkingQueue(tileQueueX.get(tail), tileQueueY.get(tail));
		for (int i = 1; i < size; i++) {
			tail--;
			c.addToWalkingQueue(tileQueueX.get(tail), tileQueueY.get(tail));
		}
		/*
		 * if (c.wQueueWritePtr - 1 >= 0) { c.lastRouteX =
		 * c.walkingQueueX[c.wQueueWritePtr - 1]; c.lastRouteY =
		 * c.walkingQueueY[c.wQueueWritePtr - 1]; }
		 */
		long end = System.currentTimeMillis();
		// System.out.println((end - start));
	}

	public static boolean canAttack(Client a, NPC b) {
		if (a.heightLevel != b.heightLevel) {
			return false;
		}
		if (ignoreCanAttack(b.npcType)) {
			return true;
		}
		if (!canMove(a.getX(), a.getY(), b.getX(), b.getY(), a.heightLevel % 4, 1, 1)) {
			return false;
		}
		return true;
	}

	public static boolean checkNormalClip(int x, int y, int endX, int endY, int heightLevel) {
		if (getClipping(x, y, heightLevel, false) == 2228480 || getClipping(x, y, heightLevel, false) == 198018 || getClipping(x, y, heightLevel, false) == 131328) {
			return false;
		}
		int xDistance = endX - x;
		int yDistance = endY - y;

		int currentX = 0, currentY = 0;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;

		if (xDistance < 0) {
			dx1 = -1;
		} else if (xDistance > 0) {
			dx1 = 1;
		}
		if (yDistance < 0) {
			dy1 = -1;
		} else if (yDistance > 0) {
			dy1 = 1;
		}
		if (xDistance < 0) {
			dx2 = -1;
		} else if (xDistance > 0) {
			dx2 = 1;
		}
		int longest = Math.abs(xDistance);
		int shortest = Math.abs(yDistance);
		if (!(longest > shortest)) {
			longest = Math.abs(yDistance);
			shortest = Math.abs(xDistance);
			if (yDistance < 0) {
				dy2 = -1;
			} else if (yDistance > 0) {
				dy2 = 1;
			}
			dx2 = 0;
		}
		int numerator = longest >> 1;
		for (int i = 0; i <= longest; i++) {
			currentX = x;// 1 naast opponent -1
			currentY = y;// zelfde
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
			if (!getClipping(currentX, currentY, heightLevel, x - currentX, y - currentY, false)) {
				return false;
			}
			if (x == endX && y == endY) {
				return true;
			}
		}
		return true;
	}

	public static boolean getClipping(int x, int y, int height, int moveTypeX, int moveTypeY, boolean projectile) {
		try {
			height %= 4;
			int checkX = (x + moveTypeX);
			int checkY = (y + moveTypeY);
			// System.out.println("Projectile: "+getClipping(x, y, height,
			// true)+" - Non-projectile: "+getClipping(x, y, height, false));
			if (moveTypeX == -1 && moveTypeY == 0)
				return (getClipping(checkX, checkY, height, projectile) & 0x1280108) == 0;
			else if (moveTypeX == 1 && moveTypeY == 0)
				return (getClipping(checkX, checkY, height, projectile) & 0x1280180) == 0;
			else if (moveTypeX == 0 && moveTypeY == -1)
				return (getClipping(checkX, checkY, height, projectile) & 0x1280102) == 0;
			else if (moveTypeX == 0 && moveTypeY == 1)
				return (getClipping(checkX, checkY, height, projectile) & 0x1280120) == 0;
			else if (moveTypeX == -1 && moveTypeY == -1)
				return ((getClipping(checkX, checkY, height, projectile) & 0x128010e) == 0 && (getClipping(checkX, y, height, projectile) & 0x1280108) == 0
						&& (getClipping(x, checkY, height, projectile) & 0x1280102) == 0);
			else if (moveTypeX == 1 && moveTypeY == -1)
				return ((getClipping(checkX, checkY, height, projectile) & 0x1280183) == 0 && (getClipping(checkX, y, height, projectile) & 0x1280180) == 0
						&& (getClipping(x, checkY, height, projectile) & 0x1280102) == 0);
			else if (moveTypeX == -1 && moveTypeY == 1)
				return ((getClipping(checkX, checkY, height, projectile) & 0x1280138) == 0 && (getClipping(checkX, y, height, projectile) & 0x1280108) == 0
						&& (getClipping(x, checkY, height, projectile) & 0x1280120) == 0);
			else if (moveTypeX == 1 && moveTypeY == 1)
				return ((getClipping(checkX, checkY, height, projectile) & 0x12801e0) == 0 && (getClipping(checkX, y, height, projectile) & 0x1280180) == 0
						&& (getClipping(x, checkY, height, projectile) & 0x1280120) == 0);
			else {
				// System.out.println("[FATAL ERROR]: At getClipping: " + x +
				// ", " + y + ", " + height + ", " + moveTypeX + ", " +
				// moveTypeY);
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

	private static boolean checkTile(int clipData, int objectX, int objectY) {
		if (clipData == 2228480)
			return true;
		if (clipData == 2097152) {
			if ((objectX > 2950 && objectX < 3380) && (objectY > 3962 && objectY < 3972))
				return true;
			if ((objectX > 2375 && objectX < 2382) && (objectY > 3081 && objectY < 3092))
				return true;
			if ((objectX > 2409 && objectX < 2428) && (objectY > 3119 && objectY < 3128))
				return true;
		}
		if (clipData == 2097408) {
			if ((objectX > 2375 && objectX < 2382) && (objectY > 3081 && objectY < 3092))
				return true;
			if ((objectX > 2409 && objectX < 2428) && (objectY > 3119 && objectY < 3128))
				return true;
		}
		if (clipData == 4104) {
			if (objectX == 3111 && (objectY == 3514 || objectY == 3515))
				return true;
		}
		if (clipData == 131328) {
			if (objectX < 3386 && objectX > 3345 && objectY > 3917 && objectY < 3961)
				return true;
			if ((objectX == 3095 || objectX == 3096) && (objectY == 3523 || objectY == 3524))
				return true;
			if ((objectX == 3099 || objectX == 3100) && (objectY == 3525 || objectY == 3526))
				return true;
			if ((objectX == 3093 && objectY == 3541) || (objectX == 3082 && objectY == 3537) || (objectX == 3072 && objectY == 3541) || (objectX == 3088 && objectY == 3554)
					|| (objectX == 3083 && objectY == 3553) || (objectX == 3078 && objectY == 3552) || (objectX == 3070 && objectY == 3554) || (objectX == 3064 && objectY == 3551)
					|| (objectX == 3058 && objectY == 3551) || (objectX == 3057 && objectY == 3556) || (objectX == 3054 && objectY == 3562) || (objectX == 3051 && objectY == 3561)
					|| (objectX == 3050 && objectY == 3565) || (objectX == 3044 && objectY == 3566) || (objectX == 3043 && objectY == 3569) || (objectX == 3045 && objectY == 3572)
					|| (objectX == 3045 && objectY == 3577) || (objectX == 3045 && objectY == 3578) || (objectX == 3045 && objectY == 3579) || (objectX == 3044 && objectY == 3579)
					|| (objectX == 3044 && objectY == 3578) || (objectX == 3044 && objectY == 3577) || (objectX == 3336 && objectY == 3665) || (objectX == 3336 && objectY == 3666)
					|| (objectX == 3335 && objectY == 3665) || (objectX == 3337 && objectY == 3664) || (objectX == 3337 && objectY == 3665) || (objectX == 3337 && objectY == 3666)
					|| (objectX == 3337 && objectY == 3667) || (objectX == 3340 && objectY == 3659) || (objectX == 3340 && objectY == 3660) || (objectX == 3341 && objectY == 3659)
					|| (objectX == 3341 && objectY == 3660) || (objectX == 3342 && objectY == 3672) || (objectX == 3344 && objectY == 3672) || (objectX == 3345 && objectY == 3672)
					|| (objectX == 3344 && objectY == 3671) || (objectX == 3345 && objectY == 3671) || (objectX == 3350 && objectY == 3674) || (objectX == 3348 && objectY == 3677)
					|| (objectX == 3348 && objectY == 3678) || (objectX == 3348 && objectY == 3679) || (objectX == 3348 && objectY == 3680) || (objectX == 3347 && objectY == 3678)
					|| (objectX == 3347 && objectY == 3679) || (objectX == 3346 && objectY == 3678) || (objectX == 3342 && objectY == 3691) || (objectX == 3341 && objectY == 3691)
					|| (objectX == 3341 && objectY == 3692) || (objectX == 3342 && objectY == 3692))
				return true;
		}
		return false;
	}

	public static int getClipping(int x, int y, int height, boolean projectile) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		if (regionId < 0 || regionId >= regions.length)
			return 0;
		Region r = regions[regionId];
		if (r == null || r.id() != regionId)
			return 0;
		if (projectile) {
			if (r.getClip(x, y, height) == 2228480 || r.getClip(x, y, height) == 4104 || r.getClip(x, y, height) == 131328 || r.getClip(x, y, height) == 2097152
					|| r.getClip(x, y, height) == 2097408) {
				if (checkTile(r.getClip(x, y, height), x, y))
					return 0;
			}
		}
		return r.getClip(x, y, height);
	}

	public static boolean canAttack(Client a, Client b) {

		if (a.heightLevel != b.heightLevel) {
			return false;
		}
		if (!canMove(a.getX(), a.getY(), b.getX(), b.getY(), a.heightLevel % 4, 1, 1)) {
			return false;
		}
		return true;
	}
}
