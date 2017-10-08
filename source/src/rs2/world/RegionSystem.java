package rs2.world;

import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.player.Player;


public class RegionSystem {
	/**
	 * Creates a RegionSystem with default sizes.
	 */
	public RegionSystem() {
		this(1024 * 16, 1024 * 16, 60, 16, 16);
	}

	/**
	 * Creates a RegionSystem witsh custom sizes.
	 *
	 * @param worldAbsWidth
	 * @param worldAbsHeight
	 * @param worldAbsDepth
	 * @param regionAbsWidth
	 * @param regionAbsHeight
	 */
	public RegionSystem(int worldAbsWidth, int worldAbsHeight,
			int worldAbsDepth, int regionAbsWidth, int regionAbsHeight) {
		this.regions = new Region[worldAbsWidth / regionAbsWidth][worldAbsHeight
				/ regionAbsHeight][worldAbsDepth];
		this.worldAbsWidth = worldAbsWidth;
		this.worldAbsHeight = worldAbsHeight;
		this.worldAbsDepth = worldAbsDepth;
		this.regionAbsWidth = regionAbsWidth;
		this.regionAbsHeight = regionAbsHeight;
	}

	// World sizes
	public final int worldAbsWidth;
	public final int worldAbsHeight;
	public final int worldAbsDepth;
	public final int regionAbsWidth;
	public final int regionAbsHeight;
	// Actual regions
	private final Region[][][] regions;

	/**
	 * Gets the contents at the specified X/Y/Z location.
	 *
	 * @param absX
	 *            The X coordinate.
	 * @param absY
	 *            The Y coordinate.
	 * @param absZ
	 *            The Z coordinate.
	 * @return An UNMODIFYABLE ListIterator with the contents of the requested
	 *         tile, or null if the parent region is not initialized.
	 */
	public NPC[] getTileNpcs(int absX, int absY, int absZ) {
		Tile tile = getTile(absX, absY, absZ);
		if (tile == null || tile.activeNpcTiles <= 0)
			return null;
		// sSystem.out.println(region.tiles[absX & regionAbsWidth - 1][absY &
		// regionAbsHeight -
		// 1].activeNpcTiles+" npcs at tile: "+absX+" - "+absY);
		return tile.npcs;
	}

	/**
	 * Gets the contents at the specified X/Y/Z location.
	 *
	 * @param absX
	 *            The X coordinate.
	 * @param absY
	 *            The Y coordinate.
	 * @param absZ
	 *            The Z coordinate.
	 * @return An UNMODIFYABLE ListIterator with the contents of the requested
	 *         tile, or null if the parent region is not initialized.
	 */
	public Player[] getTilePlayers(int absX, int absY, int absZ) {
		Tile tile = getTile(absX, absY, absZ);
		if (tile == null || tile.activePlayerTiles <= 0)
			return null;
		return tile.players;
	}

	public Tile getTile(int absX, int absY, int absZ) {
		int regX = absX / regionAbsWidth, regY = absY / regionAbsHeight;
		if (absZ > 7)// 8
			absZ = (absZ - (absZ / 7)) + 4;// 8-7+4
		if (regX >= regions.length || regX <= 0) {
			return null;
		} else if (regY >= regions[0].length || regY <= 0) {
			return null;
		} else if (absZ >= regions[0][0].length || absZ < 0) {
			return null;
		}
		Region region = regions[regX][regY][absZ];
		if (regions[regX][regY][absZ] != null) {
			// System.out.println("HEREEE");
			return region.tiles[absX & regionAbsWidth - 1][absY
					& regionAbsHeight - 1];
		}
		return null;
	}

	public int getTileCount(int absX, int absY, int absZ, boolean player) {
		Tile tile = getTile(absX, absY, absZ);
		if (tile == null)
			return 0;
		if (player) {
			if (tile.activePlayerTiles > 0)
				return tile.highestIndexPlayer;
		} else {
			if (tile.activeNpcTiles > 0)
				return tile.highestIndexNpcs;
		}
		return 0;
	}

	/**
	 * Adds a TileContent to the specified X/Y/Z.
	 *
	 * @param content
	 *            The TileContent to add.
	 * @param absX
	 *            The X coordinate.
	 * @param absY
	 *            The Y coordinate.
	 * @param absZ
	 *            The Z coordinate.
	 * @return If the operation was successful.
	 */
	public boolean add(Player content, int absX, int absY, int absZ) {
		int regX = absX / regionAbsWidth, regY = absY / regionAbsHeight;
		if (regX >= regions.length || regX <= 0) {
			return false;
		} else if (regY >= regions[0].length || regY <= 0) {
			return false;
		} else if (absZ >= regions[0][0].length || absZ < 0) {
			return false;
		}
		Region region = regions[regX][regY][absZ];
		if (region == null) {
			region = (regions[regX][regY][absZ] = new Region(regX, regY, absZ));
		}
		return region.add(content, absX, absY);
	}

	/**
	 * Removes the specified TileContent only from the specified X/Y/Z.
	 *
	 * @param content
	 *            The TileContent to remove.
	 * @param absX
	 *            The X coordinate.
	 * @param absY
	 *            The Y Coordinate.
	 * @param absZ
	 *            The Z coordinate.
	 * @return If the operation was successful.
	 */
	public boolean remove(Player content, int absX, int absY, int absZ) {
		int regX = absX / regionAbsWidth, regY = absY / regionAbsHeight;
		// System.out.println(""+regions.length+" - "+regions[0].length+" -
		// "+regions[0][0].length);
		if (regX >= regions.length || regX <= 0) {
			return false;
		} else if (regY >= regions[0].length || regY <= 0) {
			return false;
		} else if (absZ >= regions[0][0].length || absZ < 0) {
			return false;
		}
		Region region = regions[regX][regY][absZ];
		if (region == null) {
			region = (regions[regX][regY][absZ] = new Region(regX, regY, absZ));
		}
		return region.remove(content, absX, absY);
	}

	/**
	 * Adds a TileContent to the specified X/Y/Z.
	 *
	 * @param content
	 *            The TileContent to add.
	 * @param absX
	 *            The X coordinate.
	 * @param absY
	 *            The Y coordinate.
	 * @param absZ
	 *            The Z coordinate.
	 * @return If the operation was successful.
	 */
	public boolean add(NPC content, int absX, int absY, int absZ) {
		int regX = absX / regionAbsWidth, regY = absY / regionAbsHeight;
		if (regX >= regions.length || regX <= 0) {
			return false;
		} else if (regY >= regions[0].length || regY <= 0) {
			return false;
		} else if (absZ >= regions[0][0].length || absZ < 0) {
			return false;
		}
		Region region = regions[regX][regY][absZ];

		if (region == null) {
			region = (regions[regX][regY][absZ] = new Region(regX, regY, absZ));
		}
		return region.add(content, absX, absY);
	}

	/**
	 * Removes the specified TileContent only from the specified X/Y/Z.
	 *
	 * @param content
	 *            The TileContent to remove.
	 * @param absX
	 *            The X coordinate.
	 * @param absY
	 *            The Y Coordinate.
	 * @param absZ
	 *            The Z coordinate.
	 * @return If the operation was successful.
	 */
	public boolean remove(NPC content, int absX, int absY, int absZ) {
		int regX = absX / regionAbsWidth, regY = absY / regionAbsHeight;
		// System.out.println(""+regions.length+" - "+regions[0].length+" -
		// "+regions[0][0].length);
		if (regX >= regions.length || regX <= 0) {
			return false;
		} else if (regY >= regions[0].length || regY <= 0) {
			return false;
		} else if (absZ >= regions[0][0].length || absZ < 0) {
			return false;
		}
		Region region = regions[regX][regY][absZ];
		if (region == null) {
			region = (regions[regX][regY][absZ] = new Region(regX, regY, absZ));
		}
		return region.remove(content, absX, absY);
	}

	/**
	 * Attempts to move the specified TileContent.
	 *
	 * @param content
	 *            The TileContent to move.
	 * @param fx
	 *            The old X coordinate.
	 * @param fy
	 *            The old Y coordinate.
	 * @param fz
	 *            The old Z coordinate.
	 * @param tox
	 *            The new X coordinate.
	 * @param toy
	 *            The new Y coordinate.
	 * @param toz
	 *            The new Z coordinate.
	 * @return If the add operation was successful.
	 */
	public boolean move(Player content, int fx, int fy, int fz, int tox,
			int toy, int toz) {
		// System.out.println("MOVE FUNCTION: fromX: "+fx
		// +" - fromY: "+fy+" - toX: "+tox+
		// " - toY: "+toy+" - height1: "+fz+" - height2: "+toz);
		if (fx == tox && fy == toy && fz == toz) {
			return false;
		}
		if (fx > 0 || fy > 0 || fz > 0 && fz < 40) {
			remove(content, fx, fy, fz);
			// System.out.println("removed: "+fx+" - "+fy);
		}
		if (tox > 0 || toy > 0 || toz > 0 && fz < 40) {
			return add(content, tox, toy, toz);
		}
		return false;
	}

	public boolean move(NPC content, int fx, int fy, int fz, int tox, int toy,
			int toz) {
		// System.out.println("MOVE FUNCTION: fromX: "+fx
		// +" - fromY: "+fy+" - toX: "+tox+
		// " - toY: "+toy+" - height1: "+fz+" - height2: "+toz);
		if (fx <= 0 || fy <= 0 || tox <= 0 || toy <= 0) {
			return false;
		}
		if (fx == tox && fy == toy && fz == toz) {
			return false;
		}
		boolean removed = remove(content, fx, fy, fz);
		if (!add(content, tox, toy, toz)) {
			if (removed)
				System.out.println("REMOVED BUT NOT ADDED: " + fx + " - y: "
						+ fy + " - tox: " + tox + " - toy: " + toy);
			return false;
		} else
			return true;
	}

	private class Region {

		public Region(int regX, int regY, int regZ) {
			this.regX = regX;
			this.regY = regY;
			this.regZ = regZ;
		}

		private final Tile[][] tiles = new Tile[regionAbsWidth][regionAbsHeight];
		private final int regX, regY, regZ;
		private long activeTiles = 0L;

		{
			for (int x = regionAbsWidth, y; --x != -1;) {
				for (y = regionAbsHeight; --y != -1;) {
					tiles[x][y] = new Tile();
				}
			}
		}

		private boolean add(Player content, int absX, int absY) {
			if (absX <= 0 || absY <= 0) {
				return false;
			}
			Tile tile = tiles[absX % regionAbsWidth][absY % regionAbsHeight];
			++tile.activePlayerTiles;
			++activeTiles;
			return tile.addPlayer(content);
		}

		private boolean add(NPC content, int absX, int absY) {
			// System.out.println("-add- absx; "+absX+" - absY: "+absY);
			if (absX <= 0 || absY <= 0) {
				return false;
			}
			Tile tile = tiles[absX % regionAbsWidth][absY % regionAbsHeight];
			++tile.activeNpcTiles;
			++activeTiles;
			return tile.addNPC(content);
		}

		private boolean remove(NPC content, int absX, int absY) {
			if (absX <= 0 || absY <= 0) {
				return false;
			}
			Tile tile = tiles[absX % regionAbsWidth][absY % regionAbsHeight];
			if (tile.removeNPC(content)) {
				--tile.activeNpcTiles;
				if (--activeTiles < 1) {
					regions[regX][regY][regZ] = null;
				}
				return true;
			}
			return false;
		}

		private boolean remove(Player content, int absX, int absY) {
			Tile tile = tiles[absX % regionAbsWidth][absY % regionAbsHeight];
			if (absX <= 0 || absY <= 0) {
				return false;
			}
			if (tile.removePlayer(content)) {
				--tile.activePlayerTiles;
				if (--activeTiles < 1) {
					regions[regX][regY][regZ] = null;
				}
				return true;
			}
			return false;
		}
	}

	public class Tile {

		public Player[] players = new Player[50];
		public NPC[] npcs = new NPC[50];
		public int emptyIndexPlayers = 1;
		public int emptyIndexNpcs = 1;
		public int highestIndexPlayer = 0;
		public int highestIndexNpcs = 0;
		public int activePlayerTiles = 0;
		public int activeNpcTiles = 0;

		public boolean addPlayer(Player player) {
			players[emptyIndexPlayers] = player;
			player.tileIndex = emptyIndexPlayers;
			if (highestIndexPlayer <= emptyIndexPlayers) {
				highestIndexPlayer = emptyIndexPlayers + 1;
			}
			findFreeIndex(true);

			return true;
		}

		public boolean removePlayer(Player player) {
			if (players[player.tileIndex] == null
					|| players[player.tileIndex].playerName == player.playerName) {
				players[player.tileIndex] = null;
				emptyIndexPlayers = player.tileIndex;
				player.tileIndex = 0;
				return true;
			} else {
				System.out.println("error at index: " + player.tileIndex
						+ " - no tile found: " + player.playerName + " - x: "
						+ player.absX + " - y: " + player.absY);
			}
			return false;
		}

		public boolean addNPC(NPC npc) {
			npcs[emptyIndexNpcs] = npc;
			npc.tileIndex = emptyIndexNpcs;
			if (highestIndexNpcs <= emptyIndexNpcs) {
				highestIndexNpcs = emptyIndexNpcs + 1;
			}
			findFreeIndex(false);
			return true;
		}

		public boolean removeNPC(NPC npc) {
			if (npc.tileIndex == 0) {
				return false;
			} else if (npcs[npc.tileIndex] == null
					|| npcs[npc.tileIndex].npcId != npc.npcId) {
				System.out.println("error NPC NOT FOUND AT TILES: "
						+ npc.npcType + " - npc" + npcs[npc.tileIndex]);
				return false;
			}
			npcs[npc.tileIndex] = null;
			emptyIndexNpcs = npc.tileIndex;
			npc.tileIndex = 0;
			return true;
		}

		public void findFreeIndex(boolean playerSearch) {
			Object[] entity = playerSearch ? players : npcs;
			int emptyIndex = playerSearch ? emptyIndexPlayers : emptyIndexNpcs;
			for (int i = emptyIndex - 1; i < entity.length; i++) {
				if (i <= 0 || i == emptyIndex)
					continue;
				if (entity[i] == null) {
					if (playerSearch)
						emptyIndexPlayers = i;
					else
						emptyIndexNpcs = i;
					return;
				}
			}
			for (int i = 1; i < emptyIndex - 1; i++) {
				if (entity[i] == null) {
					if (playerSearch)
						emptyIndexPlayers = i;
					else
						emptyIndexNpcs = i;
					return;
				}
			}
			System.out.println("[BIG ERROR] TILES ARRAY IS FULL!?");
		}
	}
}