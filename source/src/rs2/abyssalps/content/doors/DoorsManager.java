package rs2.abyssalps.content.doors;

import rs2.Server;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.objects.WorldObject;
import rs2.util.cache.def.ObjectOrientation;
import rs2.util.cache.def.ObjectType;
import rs2.util.cache.region.Region;
import rs2.abyssalps.model.objects.Object;

public class DoorsManager {

	public static boolean toggle(int id, int x, int y, int z) {
		if (!DoorsDefinitions.isDoors(id)) {
			return false;
		}

		if (DoorsDefinitions.isDoubleDoors(id)) {
			toggleDoubleDoors(x, y, z);
		} else {
			toggleSingleDoors(x, y, z);
		}

		return true;
	}

	private static void toggleSingleDoors(int x, int y, int z) {
		boolean defaultPosition = true;
		boolean openByDefault = DoorsDefinitions.isOpen(x, y, z);

		WorldObject doorsObject = Region.getWorldObject(ObjectType.STRAIGHT_WALL, x, y, z);
	
		if (doorsObject == null) {
			doorsObject = Server.objectManager.getWorldObject(ObjectType.STRAIGHT_WALL, x, y, z);
			defaultPosition = false;
		}

		if (doorsObject == null) {
			return;
		}

		if (defaultPosition == openByDefault) {
			closeDoors(doorsObject);
		} else {
			openDoors(doorsObject);
		}
	}

	private static void openDoors(WorldObject doorsObject) {
		Position openPosition = getOpenPosition(doorsObject);
		ObjectOrientation openOrientation = rotateOrientation(doorsObject.getOrientation(), 1);
		new Object(-1, doorsObject.getX(), doorsObject.getY(), doorsObject.getZ(), doorsObject.getFace(), doorsObject.getType(), -1, 0);
		new Object(doorsObject.getId(), openPosition.getX(), openPosition.getY(), openPosition.getZ(), openOrientation.getId(), doorsObject.getType(), doorsObject.getId(), 0);
	}

	private static void closeDoors(WorldObject doorsObject) {
		Position closePosition = getClosedPosition(doorsObject);
		ObjectOrientation closeOrientation = rotateOrientation(doorsObject.getOrientation(), -1);
		if (DoorsDefinitions.isOpen(doorsObject.getX(), doorsObject.getY(), doorsObject.getZ())) {
			DoorsDefinitions.defineOpenDoors(closePosition.getX(), closePosition.getY(), closePosition.getZ());
		}
		new Object(-1, doorsObject.getX(), doorsObject.getY(), doorsObject.getZ(), doorsObject.getFace(), doorsObject.getType(), -1, 0);
		new Object(doorsObject.getId(), closePosition.getX(), closePosition.getY(), closePosition.getZ(), closeOrientation.getId(), doorsObject.getType(), doorsObject.getId(), 0);
	}

	private static Position getOpenPosition(WorldObject doorsObject) {
		int openX = doorsObject.getX(), openY = doorsObject.getY();
		switch (doorsObject.getOrientation()) {
		case NORTH:
			openY++;
			break;
		case EAST:
			openX++;
			break;
		case SOUTH:
			openY--;
			break;
		case WEST:
			openX--;
			break;
		}
		return new Position(openX, openY, doorsObject.getZ());
	}

	private static Position getClosedPosition(WorldObject doorsObject) {
		int openX = doorsObject.getX(), openY = doorsObject.getY();
		switch (doorsObject.getOrientation()) {
		case NORTH:
			openX++;
			break;
		case EAST:
			openY--;
			break;
		case SOUTH:
			openX--;
			break;
		case WEST:
			openY++;
			break;
		}
		return new Position(openX, openY, doorsObject.getZ());
	}

	private static void toggleDoubleDoors(int x, int y, int z) {
		/*boolean defaultPosition = true, openByDefault, currentlyOpen;

		GameObject doorsObject = Region.getObject(x, y, z, GameObjectType.STRAIGHT_WALL);

		if (doorsObject == null) {
			doorsObject = World.getObjectManager().get(x, y, z, GameObjectType.STRAIGHT_WALL);
			defaultPosition = false;
		}

		openByDefault = DoorsDefinitions.isOpen(x, y, z);

		/*
		 * If doors are open by default and currently in their original Position
		 * they are open. If doors are closed by default and currently not in
		 * their original Position they are closed.
		 */
		/*currentlyOpen = defaultPosition == openByDefault;

		GameObject leftDoorsObject, rightDoorsObject;

		if (DoorsDefinitions.isLeftSideDoors(doorsObject.getId())) {
			leftDoorsObject = doorsObject;
			Position relativePosition = getRelativeDoorsPosition(doorsObject, true, currentlyOpen);
			rightDoorsObject = defaultPosition ? Region.getObject(relativePosition, GameObjectType.STRAIGHT_WALL)
					: World.getObjectManager().get(relativePosition, GameObjectType.STRAIGHT_WALL);
		} else {
			rightDoorsObject = doorsObject;
			Position relativePosition = getRelativeDoorsPosition(doorsObject, false, currentlyOpen);
			leftDoorsObject = defaultPosition ? Region.getObject(relativePosition, GameObjectType.STRAIGHT_WALL)
					: World.getObjectManager().get(relativePosition, GameObjectType.STRAIGHT_WALL);
		}

		if (currentlyOpen) {
			closeDoubleDoors(leftDoorsObject, rightDoorsObject);
		} else {
			openDoubleDoors(leftDoorsObject, rightDoorsObject);
		}*/
	}

	private static void openDoubleDoors(WorldObject leftDoorsObject, WorldObject rightDoorsObject) {
		/*Position openRightDoorsPosition = getOpenPosition(rightDoorsObject);
		GameObject openRightDoorsObject = new GameObject(rightDoorsObject.getId(), openRightDoorsPosition.getX(), openRightDoorsPosition.getY(), openRightDoorsPosition.getZ(),
				GameObjectType.STRAIGHT_WALL, rotateOrientation(rightDoorsObject.getOrientation(), 1));

		Position openLeftDoorsPosition = getRelativeDoorsPosition(openRightDoorsObject, false, true);
		GameObject openLeftDoorsObject = new GameObject(leftDoorsObject.getId(), openLeftDoorsPosition.getX(), openLeftDoorsPosition.getY(), openLeftDoorsPosition.getZ(),
				GameObjectType.STRAIGHT_WALL, rotateOrientation(leftDoorsObject.getOrientation(), -1));
		World.getObjectManager().remove(leftDoorsObject);
		World.getObjectManager().remove(rightDoorsObject);
		World.getObjectManager().add(openLeftDoorsObject);
		World.getObjectManager().add(openRightDoorsObject);
	}

	private static void closeDoubleDoors(GameObject leftDoorsObject, GameObject rightDoorsObject) {
		Position openRightDoorsPosition = getClosedPosition(rightDoorsObject);
		GameObject openRightDoorsObject = new GameObject(rightDoorsObject.getId(), openRightDoorsPosition.getX(), openRightDoorsPosition.getY(), openRightDoorsPosition.getZ(),
				GameObjectType.STRAIGHT_WALL, rotateOrientation(rightDoorsObject.getOrientation(), -1));

		Position openLeftDoorsPosition = getRelativeDoorsPosition(openRightDoorsObject, false, false);
		GameObject openLeftDoorsObject = new GameObject(leftDoorsObject.getId(), openLeftDoorsPosition.getX(), openLeftDoorsPosition.getY(), openLeftDoorsPosition.getZ(),
				GameObjectType.STRAIGHT_WALL, rotateOrientation(leftDoorsObject.getOrientation(), 1));
		World.getObjectManager().remove(leftDoorsObject);
		World.getObjectManager().remove(rightDoorsObject);
		World.getObjectManager().add(openLeftDoorsObject);
		World.getObjectManager().add(openRightDoorsObject);*/
	}

	private static Position getRelativeDoorsPosition(WorldObject doorsObject, boolean findRightSide, boolean currentlyOpen) {
		int x = doorsObject.getX(), y = doorsObject.getY();
		if (currentlyOpen) {
			switch (doorsObject.getOrientation()) {
			case NORTH:
				y--;
				break;
			case EAST:
				x--;
				break;
			case SOUTH:
				y++;
				break;
			case WEST:
				x++;
				break;
			}
		} else {
			if (findRightSide) {
				switch (doorsObject.getOrientation()) {
				case NORTH:
					x++;
					break;
				case EAST:
					y--;
					break;
				case SOUTH:
					x--;
					break;
				case WEST:
					y++;
					break;
				}
			} else {
				switch (doorsObject.getOrientation()) {
				case NORTH:
					x--;
					break;
				case EAST:
					y++;
					break;
				case SOUTH:
					x++;
					break;
				case WEST:
					y--;
					break;
				}
			}
		}
		return new Position(x, y, doorsObject.getZ());
	}

	private static ObjectOrientation rotateOrientation(ObjectOrientation orientation, int offset) {
		int currentOrientation = orientation.getId();
		int offsetValue = offset / Math.abs(offset);
		for (int i = 0; i < Math.abs(offset); i++) {
			currentOrientation += offsetValue;
			if (currentOrientation > 3) {
				currentOrientation = 0;
			} else if (currentOrientation < 0) {
				currentOrientation = 3;
			}
		}
		return ObjectOrientation.valueOf(currentOrientation);
	}

}