package rs2.abyssalps.model.objects;

import rs2.util.cache.def.ObjectOrientation;

public class WorldObject {

	public int x, y, z, id, type, face;

	public WorldObject() {

	}

	public WorldObject(int id, int x, int y, int z) {
		this(id, x, y, z, 0);
	}

	public WorldObject(int id, int x, int y, int z, int face) {
		this(id, x, y, z, face, 10);
	}

	public WorldObject(int id, int x, int y, int z, int face, int type) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.face = face;
		this.type = type;
	}
	
	public int getId() {
		return id;
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
	
	public int getType() {
		return type;
	}
	
	public int getFace() {
		return face;
	}

	public ObjectOrientation getOrientation() {
		return ObjectOrientation.valueOf(getFace());
	}
	
}
