package rs2.abyssalps.model.items.defs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

import rs2.Config;
import rs2.util.tools.XML.XStreamUtil;

public class ItemDefinitions {

	/**
	 * Java logger
	 */
	private static final Logger logger = Logger.getLogger(ItemDefinitions.class
			.getName());

	@SuppressWarnings("unchecked")
	public static void init() throws FileNotFoundException {
		logger.info("Loading item definitions...");
		List<ItemDefinitions> itemList = (List<ItemDefinitions>) XStreamUtil
				.getxStream().fromXML(
						new FileInputStream("./data/XML/ItemDefinitions.xml"));
		instance = new ItemDefinitions[Config.ITEM_LIMIT];
		for (ItemDefinitions defs : itemList) {
			instance[defs.getId()] = defs;
		}
		logger.info(instance[4151].getName());
		logger.info(instance.length + " item definitions has been unpacked...");
	}

	public static ItemDefinitions[] instance = null;

	public static ItemDefinitions forId(int id) {
		if (id <= 0 || id >= Config.ITEM_LIMIT) {
			return null;
		}
		if (instance == null) {
			return null;
		}
		return instance[id];
	}

	private int id;
	private String name;
	private String description;
	private int price;
	private boolean stackable;
	private boolean noted;

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String description() {
		return this.description;
	}

	public int getPrice() {
		return this.price;
	}

	public boolean isStackable() {
		return this.stackable;
	}

	public boolean isNoted() {
		return this.noted;
	}
}
