package rs2.util.cache.def;

import java.util.HashMap;
import java.util.Map;

public enum ObjectOrientation {

	/**
	 * The west orientation.
	 */
	WEST(0),

	/**
	 * The north orientation.
	 */
	NORTH(1),

	/**
	 * The east orientation.
	 */
	EAST(2),

	/**
	 * The south orientation.
	 */
	SOUTH(3),

	;

	/**
	 * The id of the orientation.
	 */
	private final int id;

	/**
	 * Constructs a new {@link GameObjectOrientation} with the specified
	 * orientation id.
	 *
	 * @param id
	 *            The orientation id.
	 */
	private ObjectOrientation(int id) {
		this.id = id;
	}

	/**
	 * Returns the id of this orientation.
	 */
	public final int getId() {
		return id;
	}

	/**
	 * A mutable {@link Map} of {@code int} keys to
	 * {@link GameObjectOrientation} values.
	 */
	private static final Map<Integer, ObjectOrientation> values = new HashMap<>();

	/**
	 * Populates the {@link #values} cache.
	 */
	static {
		for (ObjectOrientation orientation : values()) {
			values.put(orientation.getId(), orientation);
		}
	}

	public static ObjectOrientation valueOf(int id) {
		return values.get(id);
	}

}
