package rs2.abyssalps.model.items.defs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import rs2.util.tools.XML.XStreamUtil;

public class BetaDefinitions {

	/**
	 * We shall allocate a size for the HashMap!
	 */
	public static final int ALLOCATED_SIZE = 3366;

	/**
	 * The <code>EquipmentDefinition</code> map.
	 */
	public static ArrayList<BetaDefinitions> instance;

	/**
	 * @return the definitions
	 */
	public static ArrayList<BetaDefinitions> getDefinitions() {
		return instance;
	}
	
	public static BetaDefinitions forId(int id) {
		return instance.get(id);
	}

	private static final Logger logger = Logger.getLogger(BetaDefinitions.class
			.getName());

	public static void init() throws IOException {

		if (instance != null) {
			throw new IllegalStateException("beta definitions already loaded.");
		}

		try {
			logger.info("Loading item definitions...");
			instance = new ArrayList<BetaDefinitions>(ALLOCATED_SIZE);
			instance = XStreamUtil.readXML(new File("./Data/XML/BetaDefs.xml"));
			logger.info("Loaded " + instance.size() + " beta definitions.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int id;

	public int getId() {
		return id;
	}

	private String name;

	public String getName() {
		return name;
	}
}
