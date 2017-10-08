package rs2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import rs2.abyssalps.content.minigame.fightpits.FightPitsMinigame;
import rs2.abyssalps.content.minigame.pestcontrol.PestControl;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.Player;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.tools.event.CycleEventHandler;

/**
 * @author lare96 <http://github.org/lare96>
 */
public final class GameEngine implements Runnable {

	private static final AtomicBoolean LOCK = new AtomicBoolean();
	private static final ScheduledExecutorService GAME_SERVICE = Executors
			.newSingleThreadScheduledExecutor();
	private static final ExecutorService EXECUTOR_SERVICE = Executors
			.newCachedThreadPool();

	private static State currentState = State.GAME;

	public static void initialize() {
		if (LOCK.compareAndSet(false, true)) {
			GameEngine gameEngine = new GameEngine();
			GAME_SERVICE.scheduleAtFixedRate(gameEngine, 600, 300,
					TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void run() {
		try {
			if (currentState == State.GAME) {
				cycle();
				currentState = State.IO;
			} else if (currentState == State.IO) {
				subCycle();
				currentState = State.GAME;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void cycle() {
		Server.playerHandler.process();
		if (Config.FIGHT_PITS_ACTIVE) {
			FightPitsMinigame.tick();
		}
		Server.npcHandler.process();
		Server.objectManager.process();
		Server.itemHandler.process();
		PestControl.tick();
		CycleEventHandler.getSingleton().process();
	}

	private static void subCycle() {
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			player.processQueuedPackets();
			switchTick((Client) player);
		}
	}

	private static void switchTick(Client client) {
		client.getItems().resetItems(3214);
	}

	public static void execute(Runnable task) {
		EXECUTOR_SERVICE.execute(task);
	}

	private enum State {
		IO, GAME
	}

	private GameEngine() {
	}

}
