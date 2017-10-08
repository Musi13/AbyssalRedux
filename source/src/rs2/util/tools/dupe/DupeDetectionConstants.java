package rs2.util.tools.dupe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import rs2.abyssalps.model.items.GameItem;

public class DupeDetectionConstants {

	public static ArrayList<String> dupeIPs = new ArrayList<String>();

	public static void loadDupeIps() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					"./data/logs/dupers/potential_duper_ips.txt"));
			String ip = "";
			while ((ip = reader.readLine()) != null) {
				dupeIPs.add(ip);
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static final int CYCLES_UNTIL_NEXT_CHECK = 10;

	public static final String DUPE_LOG_DIRECTORY = "./Data/logs/dupers/potential_dupers.txt";

	public static final GameItem[] ITEMS_TO_CHECK = {

	new GameItem(1038, 10), // 10 unnoted red partyhats

			new GameItem(1039, 10), // 10 noted red partyhats

			new GameItem(1040, 10), // 10 unnoted yellow partyhats

			new GameItem(1041, 10), // 10 noted yellow partyhats

			new GameItem(1042, 10), // 10 unnoted blue partyhats

			new GameItem(1043, 10), // 10 noted blue partyhats

			new GameItem(1044, 10), // 10 unnoted green partyhats

			new GameItem(1045, 10), // 10 noted green partyhats

			new GameItem(1046, 10), // 10 unnoted purple partyhats

			new GameItem(1047, 10), // 10 noted purple partyhats

			new GameItem(1048, 10), // 10 unnoted white partyhats

			new GameItem(1049, 10), // 10 noted white partyhats

			new GameItem(11802, 10), // 10 unnoted armadyl godswords

			new GameItem(11803, 10), // 10 noted armadyl godswords

			new GameItem(11804, 10), // 10 unnoted bandos godswords

			new GameItem(11805, 10), // 10 noted bandos godswords

			new GameItem(11806, 10), // 10 unnoted x godswords

			new GameItem(11807, 10), // 10 noted x godswords

			new GameItem(11808, 10), // 10 unnoted x godswords

			new GameItem(11809, 10), // 10 noted x godswords

			new GameItem(6199, 100), // 100 mystery boxes

			new GameItem(536, 100000), // 1000 x unnoted dragon bones

			new GameItem(537, 1000), // 1000 x noted dragon bones

			new GameItem(6585, 50), // 50 x unnoted amulet of fury

			new GameItem(6586, 50), // 50 x noted amulet of fury

			new GameItem(4151, 100), // 100 x unnoted whips

			new GameItem(4152, 100), // 100 x noted whips

			new GameItem(4716, 100), // 100 x unnoted dharok's helm

			new GameItem(4717, 100), // 100 x noted dharok's helm

			new GameItem(4718, 100), // 100 x unnoted dharok's axe

			new GameItem(4719, 100), // 100 x noted dharok's axe

			new GameItem(4720, 100), // 100 x unnoted dharok's body

			new GameItem(4721, 100), // 100 x noted dharok's body

			new GameItem(4722, 100), // 100 x unnoted dharok's legs

			new GameItem(4721, 100), // 100 x noted dharok's legs

			new GameItem(11283, 30), // 30 x dragonfire shields

			new GameItem(11235, 100), // 100 x unnoted dark bow

			new GameItem(11236, 100), // 100 x noted dark bow
	};

}
