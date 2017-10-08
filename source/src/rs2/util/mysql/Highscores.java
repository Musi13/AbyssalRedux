package rs2.util.mysql;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.Player;

public class Highscores implements Runnable {

	public static final long MAX_XP = ((long) 5000000000.0);

	public static final String HOST = "";
	public static final String USER = "";
	public static final String PASS = "";
	public static final String DATABASE = "";

	private Client player;

	public Highscores(Client player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			Database db = new Database(HOST, USER, PASS, DATABASE);

			if (!db.init()) {
				return;
			}

			String name = player.playerName.replace("_", " ");
			ResultSet rs = db
					.executeQuery("SELECT * FROM hs_users WHERE username='"
							+ name + "' LIMIT 1");

			boolean hasNext = rs.next();

			if (hasNext) {
				rs.moveToCurrentRow(); // move to current row, if exists.
			} else {
				rs.moveToInsertRow(); // move to insert row if does not exist
			}

			rs.updateString("username", name);
			rs.updateInt("rights", player.getRank());

			rs.updateInt("total_level", player.getTotalLevel());
			rs.updateLong("overall_xp", player.getTotalExp());

			for (int i = 0; i < player.playerXP.length; i++) {

				String skillName = Player.skillNames[i].toLowerCase();

				if (skillName == "") {
					continue;
				}

				rs.updateInt(skillName + "_xp", player.playerXP[i]);
			}

			rs.updateTimestamp("dateline",
					Timestamp.valueOf(LocalDateTime.now()));

			if (hasNext) {
				rs.updateRow();
			} else {
				rs.insertRow();
			}
			db.destroyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
