package rs2.abyssalps.fs;

/**
 * Represents a single region definition.
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public class RegionDefinition {

	/**
	 * The hash of the region coordinates.
	 */
	private final int hash;

	/**
	 * The terrain file id.
	 */
	private final int terrainFile;

	/**
	 * The object file id.
	 */
	private final int objectFile;
	
	/**
	 * Loaded default as false checks if the region has been loaded
	 */
	public boolean loaded = false;

	/**
	 * Constructs a new {@link RegionDefinition} with the specified hash, terrain
	 * file id, object file id and preload state.
	 * @param hash        The hash of the region coordinates.
	 * @param terrainFile The terrain file id.
	 * @param objectFile  The object file id.
	 */
	public RegionDefinition(int hash, int terrainFile, int objectFile) {
		this.hash = hash;
		this.terrainFile = terrainFile;
		this.objectFile = objectFile;
	}

	/**
	 * Returns the coordinate hash.
	 */
	public int getHash() {
		return hash;
	}

	/**
	 * Returns the terrain file id.
	 */
	public int getTerrainFile() {
		return terrainFile;
	}

	/**
	 * Returns the object file id.
	 */
	public int getObjectFile() {
		return objectFile;
	}
	
	/**
	 * Checks if region has been loaded
	 */
	public boolean loaded() {
		return loaded;
	}
	
	/**
	 * Loaded setter
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

}