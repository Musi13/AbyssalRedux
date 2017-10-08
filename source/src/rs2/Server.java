package rs2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

import com.rspserver.motivote.Motivote;

import rs2.abyssalps.content.multiplayer.MultiplayerSessionListener;
import rs2.abyssalps.content.vote.RewardParser;
import rs2.abyssalps.fs.FileSystem;
import rs2.abyssalps.model.npcs.NPCHandler;
import rs2.abyssalps.model.npcs.drop.Drops;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.net.ConnectionHandler;
import rs2.net.ConnectionThrottleFilter;
import rs2.sanction.SanctionHandler;
import rs2.util.cache.def.ObjectDef;
import rs2.util.cache.region.Region;
import rs2.util.mysql.RewardHandler;
import rs2.util.tools.XML.XStreamUtil;
import rs2.util.tools.dupe.DupeDetectionConstants;
import rs2.util.tools.event.CycleEventHandler;
import rs2.util.tools.event.impl.AccountScanner;
import rs2.util.tools.event.impl.GlobalShoutEvent;
import rs2.world.ClanManager;
import rs2.world.ItemHandler;
import rs2.world.ObjectHandler;
import rs2.world.ObjectManager;
import rs2.world.RegionSystem;
import rs2.world.ShopHandler;

/**
 * Server.java
 *
 * @author Sanity
 * @author Graham
 * @author Blake
 * @author Ryan Lmctruck30
 *
 */

public class Server {

	private static Logger logger = Logger.getLogger(Server.class.getName());

	public static boolean sleeping;
	public static boolean UpdateServer = false;
	public static boolean shutdownServer = false;
	public static boolean shutdownClientHandler;
	public static int serverlistenerPort;
	public static ItemHandler itemHandler = new ItemHandler();
	public static PlayerHandler playerHandler = new PlayerHandler();
	public static NPCHandler npcHandler = new NPCHandler();
	public static ShopHandler shopHandler = new ShopHandler();
	public static ObjectHandler objectHandler = new ObjectHandler();
	public static ObjectManager objectManager = new ObjectManager();
	public static FileSystem fileSystem;
	/**
	 * ClanChat Added by Valiant
	 */
	public static ClanManager clanManager = new ClanManager();

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		fileSystem = FileSystem.create("./Data/cache/");
		logger.info("Initializing " + Config.SERVER_NAME + "...");
		XStreamUtil.loadAllFiles();
		ObjectDef.loadConfig();
		Region.load();
		Drops.parse();
		RewardParser.parse();
		DupeDetectionConstants.loadDupeIps();
		SanctionHandler.loadSanctionList();

		new Motivote(new RewardHandler(),
				"http://51.255.45.125/~abyssalps/vote/", "b79c106a").start();

		GameEngine.initialize();

		Server.setupLoginChannels();

		CycleEventHandler.getSingleton().addEvent(100, new GlobalShoutEvent(),
				100);

		CycleEventHandler.getSingleton().addEvent(101, new AccountScanner(),
				DupeDetectionConstants.CYCLES_UNTIL_NEXT_CHECK);

		// CycleEventHandler.getSingleton().addEvent(101, new BackupEvent(),
		// 5040);

		logger.info(Config.SERVER_NAME + " is now online!");
	}

	public static void setupLoginChannels() {
		IoAcceptor acceptor = new SocketAcceptor();
		ConnectionHandler connectionHandler = new ConnectionHandler();
		ConnectionThrottleFilter throttleFilter = new ConnectionThrottleFilter(
				Config.CONNECTION_DELAY);

		SocketAcceptorConfig sac = new SocketAcceptorConfig();
		sac.getSessionConfig().setTcpNoDelay(false);
		sac.setReuseAddress(true);
		sac.setBacklog(100);

		sac.getFilterChain().addFirst("throttleFilter", throttleFilter);
		try {
			acceptor.bind(new InetSocketAddress(43594), connectionHandler, sac);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RegionSystem region = new RegionSystem();

	public static RegionSystem getRegion() {
		return region;
	}

	private static MultiplayerSessionListener multiplayerSessionListener = new MultiplayerSessionListener();

	public static MultiplayerSessionListener getMultiplayerSessionListener() {
		return multiplayerSessionListener;
	}
}
