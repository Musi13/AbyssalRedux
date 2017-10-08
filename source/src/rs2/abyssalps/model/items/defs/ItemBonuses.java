package rs2.abyssalps.model.items.defs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

import rs2.Config;
import rs2.util.tools.XML.XStreamUtil;

public class ItemBonuses {

	/**
	 * Java logger
	 */
	private static final Logger logger = Logger.getLogger(ItemBonuses.class
			.getName());

	private static ItemBonuses[] instance = null;

	public static ItemBonuses forId(int id) {
		if (id < 0 || id >= Config.ITEM_LIMIT) {
			return null;
		}
		return instance[id];
	}

	@SuppressWarnings("unchecked")
	public static void init() throws FileNotFoundException {
		logger.info("Unpacking item bonuses...");
		List<ItemBonuses> list = (List<ItemBonuses>) XStreamUtil.getxStream()
				.fromXML(new FileInputStream("./data/XML/packedBonuses.xml"));
		instance = new ItemBonuses[Config.ITEM_LIMIT];
		for (ItemBonuses def : list) {
			instance[def.getId()] = def;
		}
		logger.info("Unpacked " + list.size() + " equipment bonuses");
	}

	private int id;

	private int[] bonuses = new int[14];

	public int getId() {
		return id;
	}

	public int[] getBonuses() {
		return bonuses;
	}

}
