package rs2.util.tools.XML;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;

import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.items.defs.ItemBonuses;
import rs2.abyssalps.model.npcs.NPCSize;

public class XStreamUtil {

	private static XStreamUtil instance = new XStreamUtil();

	/**
	 * The XStream Instance We don't have to implement any drivers as we use the
	 * default set one, which is very fast
	 * 
	 * Before you cry, i'm not making the XStream instance constant as it looks
	 * ugly
	 */
	private static final XStream xstream = new XStream();

	/**
	 * @return the xstream
	 */
	public static XStream getXstream() {
		return xstream;
	}

	public static XStreamUtil getInstance() {
		return instance;
	}

	public static XStream getxStream() {
		return xstream;
	}

	static {
		xstream.alias("itemDefinition", ItemDefinitions.class);
		xstream.alias("bonus", ItemBonuses.class);
		xstream.alias("betaDef",
				rs2.abyssalps.model.items.defs.BetaDefinitions.class);
		xstream.alias("npcSize", NPCSize.class);
	}

	public static void loadAllFiles() throws IOException {
		try {
			ItemDefinitions.init();
			ItemBonuses.init();
			NPCSize.init();
		} catch (ClassCastException e) {

		}
	}

	public static void writeXML(Object object, File file) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		try {
			xstream.toXML(object, out);
			out.flush();
		} finally {
			out.close();
		}
	}

	/**
	 * Reads an object from an XML file.
	 * 
	 * @author Graham Edgecombe
	 * @param file
	 *            The file.
	 * @return The object.
	 * @throws IOException
	 *             if an I/O error occurs. Edit Sir Sean: Now uses generic's
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readXML(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		try {
			return (T) xstream.fromXML(in);
		} finally {
			in.close();
		}
	}

	/**
	 * Reads an object from an XML string.
	 * 
	 * @author Graham Edgecombe
	 * @param s
	 *            The XML.
	 * @return The object. Edit Sir Sean: Now uses generic's
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readXML(String s) {
		return (T) xstream.fromXML(s);
	}
}
