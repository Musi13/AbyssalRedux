package rs2.abyssalps.model;

public class Position {

	private final int x, y, z;
	
	public Position(int x, int y) {
		this(x, y, 0);
	}
	
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	/**
	 * Gets the {@code X} region chunk relative to this position.
	 * @return the {@code X} region chunk.
	 */
	public final int getChunkX() {
		return (x >> 6);
	}
	
	/**
	 * Gets the {@code Y} region chunk relative to this position.
	 * @return the {@code Y} region chunk.
	 */
	public final int getChunkY() {
		return (y >> 6);
	}
	

	/**
	 * Gets the region identification relative to this position.
	 * @return the region identification.
	 */
	public final int getRegion() {
		return ((getChunkX() << 8) + getChunkY());
	}
	
}
