package rs2.abyssalps.content.bosslog;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import rs2.abyssalps.content.PlayerContent;
import rs2.abyssalps.content.achievement.Achievement;
import rs2.abyssalps.model.player.Client;

public class BossLogs extends PlayerContent {

	private HashMap<Integer, BossLog> bossMap = new HashMap<Integer, BossLog>();

	public BossLogs(Client client) {
		super(client);

		bossMap.put(0, new BossLog("King Black Dragon", 239));

		bossMap.put(1, new BossLog("Corporeal Beast", 319));

		bossMap.put(2, new BossLog("Cerberus", 5862));

		bossMap.put(3, new BossLog("Smoke Devil", 498));

		bossMap.put(4, new BossLog("Dagannoth Rex", 2267));

		bossMap.put(5, new BossLog("Dagannoth Prime", 2266));

		bossMap.put(6, new BossLog("Corporeal Supreme", 2265));

		bossMap.put(7, new BossLog("kree'arra", 3162));

		bossMap.put(8, new BossLog("Flockleader Geerin", 3164));

		bossMap.put(9, new BossLog("Wingman Skree", 3163));

		bossMap.put(10, new BossLog("Flight Klissa", 3165));

		bossMap.put(11, new BossLog("General Graardor", 2215));

		bossMap.put(12, new BossLog("Sergeant Grim Spike", 2215));

		bossMap.put(13, new BossLog("Sergeant Strongstack", 2218));

		bossMap.put(14, new BossLog("Sergeant Steelwill", 2217));

		bossMap.put(15, new BossLog("Commander Zilyana", 2205));

		bossMap.put(16, new BossLog("Starlight", 2206));

		bossMap.put(17, new BossLog("Growler", 2207));

		bossMap.put(18, new BossLog("Lizardman Shaman", 6766));

		bossMap.put(19, new BossLog("Zulrah", 2042));

		bossMap.put(20, new BossLog("Kraken", 494));

		bossMap.put(21, new BossLog("Scorpia", 6615));

		bossMap.put(22, new BossLog("Chaos Fanatic", 6619));

		bossMap.put(23, new BossLog("Venenatis", 6504));

		bossMap.put(24, new BossLog("Crazy Archaeologist", 6618));

		bossMap.put(25, new BossLog("Callisto", 6609));
	}

	public void updateBossLog(int subjectId) {
		for (BossLog log : getBossLogList()) {
			if (log.getSubjectId() != subjectId) {
				continue;
			}
			log.increaseCount();
			getClient().sendMessage(
					"@dre@" + log.getTitle() + " kill count: @red@"
							+ log.getCount());
		}
	}

	public void onKillNPC(int npcId) {
		updateBossLog(npcId);
	}

	public Collection<BossLog> getBossLogList() {
		return bossMap.values();
	}

	public Map<Integer, BossLog> getBossLogtMap() {
		return bossMap;
	}

	public BossLog getBossLog(int id) {
		return bossMap.get(id);
	}
}
