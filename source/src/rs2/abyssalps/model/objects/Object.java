package rs2.abyssalps.model.objects;

import rs2.Server;

public class Object {

	public int objectId;
	public int objectX;
	public int objectY;
	public int height;
	public int face;
	public int type;
	public int newId;
	public int tick;

	public String owner = null;

	public Object(int id, int x, int y, int height, int face, int type,
			int newId, int ticks) {
		this.objectId = id;
		this.objectX = x;
		this.objectY = y;
		this.height = height;
		this.face = face;
		this.type = type;
		this.newId = newId;
		this.tick = ticks;
		Server.objectManager.addObject(this);
	}

	public Object(int id, int x, int y, int height, int face, int type,
			int newId, int ticks, String owner) {
		this.objectId = id;
		this.objectX = x;
		this.objectY = y;
		this.height = height;
		this.face = face;
		this.type = type;
		this.newId = newId;
		this.tick = ticks;
		this.owner = owner;
		Server.objectManager.addObject(this);
	}

	public String getOwner() {
		return this.owner;
	}
}