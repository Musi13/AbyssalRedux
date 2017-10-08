package rs2.abyssalps.content;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rs2.abyssalps.model.player.Client;

public class TotalTime extends PlayerContent {

	public TotalTime(Client client) {
		super(client);
	}

	private int[] time = new int[4];

	public int[] getTime() {
		return this.time;
	}

	private long lastTimeIncrease;

	public long getLastTimeIncrease() {
		return this.lastTimeIncrease;
	}

	public void setLastTimeIncrease(long lastTimeIncrease) {
		this.lastTimeIncrease = lastTimeIncrease;
	}

	public void tick() {
		if (System.currentTimeMillis()
				- getClient().getTotalTime().getLastTimeIncrease() > 60000) {
			getClient().getTotalTime().getTime()[1]++;
			if (getClient().getTotalTime().getTime()[1] >= 60) {
				getClient().getTotalTime().getTime()[2]++;
				getClient().getTotalTime().getTime()[1] = 0;
				if (getClient().getTotalTime().getTime()[2] >= 24) {
					getClient().getTotalTime().getTime()[3]++;
					getClient().getTotalTime().getTime()[2] = 0;
				}
			}
			getClient().getTotalTime().setLastTimeIncrease(
					System.currentTimeMillis());
		}
	}

	public static long getDateCreated(String file) {
		try {
			Path path = Paths.get("./Data/characters/" + file + ".txt");
			BasicFileAttributes attr = Files.readAttributes(path,
					BasicFileAttributes.class);

			FileTime date = attr.creationTime();

			return date.toMillis();
		} catch (IOException e) {
			System.out.println("EXCEPTION THROWN? " + e);
			return 0;
		}
	}

	public static String getTime(String name) {
		String time = "";
		time = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
				.format(getDateCreated(name));
		return time;
	}
}
