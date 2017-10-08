package rs2.abyssalps.model.npcs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import rs2.util.tools.XML.XStreamUtil;

public class NPCSize {

	public static final int ALLOCATED_SIZE = 7095;

	private static ArrayList<NPCSize> instance = null;

	public static NPCSize forId(int id) {
		if (id < 0 || id > ALLOCATED_SIZE) {
			return null;
		}
		return instance.get(id);
	}

	public static void init() throws IOException {
		if (instance != null) {
			throw new IllegalStateException("NPC Sizes already loaded.");
		}
		instance = new ArrayList<NPCSize>(ALLOCATED_SIZE);
		instance = XStreamUtil.readXML(new File("./Data/XML/sizes.xml"));
	}

	private int id;

	private int size;

	public int getId() {
		return this.id;
	}

	public int getSize() {
		return this.size;
	}
}
