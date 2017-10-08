package rs2.abyssalps.content.doors;


import java.util.HashMap;
import java.util.Map;

public class DoorsDefinitions {

	private static final Map<Integer, Boolean> doorsIds = new HashMap<>();
	private static final Map<Integer, int[]> doubleDoors = new HashMap<>();
	
	private static final Map<Integer, Boolean> openDoors = new HashMap<>();
	
	public static void defineDoorsId(int doorsId) {
		doorsIds.put(doorsId, true);
	}
	
	public static boolean isDoors(int doorsId) {
		Boolean isDoors = doorsIds.get(doorsId);
		return isDoors != null && isDoors;
	}
	
	public static void defineOpenDoors(int x, int y) {
		defineOpenDoors(x, y, 0);
	}
	
	public static void defineOpenDoors(int x, int y, int z) {
		openDoors.put(generateLocationId(x, y, z), true);
	}
	
	public static boolean isOpen(int x, int y, int z) {
		Boolean open = openDoors.get(generateLocationId(x, y, z));
		return open != null && open;
	}
	
	public static void defineDoubleDoorsIds(int leftDoorsId, int rightDoorsId) {
		int[] doorsIds = {leftDoorsId, rightDoorsId};
		doubleDoors.put(leftDoorsId, doorsIds);
		doubleDoors.put(rightDoorsId, doorsIds);
		defineDoorsId(leftDoorsId);
		defineDoorsId(rightDoorsId);
	}
	
	public static boolean isDoubleDoors(int doorsId) {
		return doubleDoors.get(doorsId) != null;
	}
	
	public static boolean isLeftSideDoors(int doorsId) {
		int[] doorsIds = doubleDoors.get(doorsId);
		return doorsIds == null ? false : doorsIds[0] == doorsId;
	}
	
	public static boolean isRightSideDoors(int doorsId) {
		return !isLeftSideDoors(doorsId);
	}
	
	
	private static int generateLocationId(int x, int y, int z) {
		return x << 32 | y << 16 | z;
	}
	
	static {
		/*
		 * Define doors ids.
		 */
		defineDoorsId(7122);
		defineDoorsId(1531);
		defineDoorsId(2025);
		defineDoorsId(24381);
		defineDoorsId(26808);
		defineDoorsId(26916);
		defineDoorsId(26917);

		/*
		 * Define double doors id pairs.
		 */
		defineDoubleDoorsIds(15514, 15516);
		defineDoubleDoorsIds(26906, 26908);
		defineDoubleDoorsIds(26910, 26913);
		
		/*
		 * Define locations that have open doors by default.
		 */
		defineOpenDoors(3080, 3507);
		
	}
	
}