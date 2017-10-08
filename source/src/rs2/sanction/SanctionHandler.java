package rs2.sanction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.logging.Logger;

import rs2.abyssalps.model.player.Client;

public class SanctionHandler {
	private static final Logger log = Logger.getLogger(SanctionHandler.class
			.getName());

	public static final String LOCATION = "./Data/sanction/";

	private static ArrayList<String> mutedPlayers = new ArrayList<String>();
	private static ArrayList<String> IPMutedPlayers = new ArrayList<String>();
	private static ArrayList<String> bannedPlayers = new ArrayList<String>();
	private static ArrayList<String> IPBannedPlayers = new ArrayList<String>();
	private static ArrayList<String> macAddress = new ArrayList<String>();

	public static boolean isMuted(Client client) {
		if (mutedPlayers.contains(client.playerName)) {
			client.sendMessage("You are muted and cannot speak.");
			return true;
		}
		return false;
	}

	public static boolean isIPMuted(Client client) {
		if (mutedPlayers.contains(client.connectedFrom)) {
			client.sendMessage("You are IP muted and cannot speak.");
			return true;
		}
		return false;
	}

	public static boolean isIPBanned(String adr) {
		if (IPBannedPlayers.contains(adr)) {
			return true;
		}
		return false;
	}

	public static boolean isBanned(Client client) {
		if (bannedPlayers.contains(client.playerName)) {
			return true;
		}
		return false;
	}

	public static boolean isMacBanned(String a) {
		if (macAddress.contains(a)) {
			return true;
		}
		return false;
	}

	public static void mutePlayer(String username) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(LOCATION
					+ "mutes.txt", true));
			mutedPlayers.add(username);
			writer.write(username);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void IPMutePlayer(String connectedFrom) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(LOCATION
					+ "ipmutes.txt", true));
			IPMutedPlayers.add(connectedFrom);
			writer.write(connectedFrom);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void banPlayer(String username) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(LOCATION
					+ "bans.txt", true));
			bannedPlayers.add(username);
			writer.write(username);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void IPBanPlayer(String IP) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(LOCATION
					+ "ipbans.txt", true));
			IPBannedPlayers.add(IP);
			writer.write(IP);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void macBanPlayer(String mac) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(LOCATION
					+ "macs.txt", true));
			macAddress.add(mac);
			writer.write(mac);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void deleteFromSactionList(String file, String name) {
		try {
			if (file.contains("mutes")) {
				mutedPlayers.remove(name);
			}
			if (file.contains("bans")) {
				bannedPlayers.remove(name);
			}
			if (file.contains("ipmutes")) {
				IPMutedPlayers.remove(name);
			}
			BufferedReader r = new BufferedReader(new FileReader(file));
			ArrayList<String> contents = new ArrayList<String>();
			while (true) {
				String line = r.readLine();
				if (line == null) {
					break;
				} else {
					line = line.trim();
				}
				if (!line.equalsIgnoreCase(name)) {
					contents.add(line);
				}
			}
			r.close();
			BufferedWriter w = new BufferedWriter(new FileWriter(file));
			for (String line : contents) {
				w.write(line, 0, line.length());
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (Exception e) {
		}
	}

	public static void loadSanctionList() {
		loadMuteList();
		loadIPMuteList();
		loadBanList();
		loadIPBanList();
		loadMacBanList();
	}

	private static void loadIPMuteList() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(LOCATION
					+ "ipmutes.txt"));
			String names = "";
			try {
				log.info("Reading ipmutes.txt...");
				while ((names = reader.readLine()) != null) {
					IPMutedPlayers.add(names);
				}
				log.info(IPMutedPlayers.size()
						+ " IPs was added to the ipmute list.");
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void loadMuteList() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(LOCATION
					+ "mutes.txt"));
			String names = "";
			try {
				log.info("Reading mutes.txt...");
				while ((names = reader.readLine()) != null) {
					mutedPlayers.add(names);
				}
				log.info(mutedPlayers.size()
						+ " usernames was added to the mute list.");
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void loadBanList() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(LOCATION
					+ "bans.txt"));
			String names = "";
			try {
				log.info("Reading bans.txt...");
				while ((names = reader.readLine()) != null) {
					bannedPlayers.add(names);
				}
				log.info(bannedPlayers.size()
						+ " usernames was added to the ban list.");
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void loadIPBanList() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(LOCATION
					+ "ipbans.txt"));
			String names = "";
			try {
				log.info("Reading bans.txt...");
				while ((names = reader.readLine()) != null) {
					IPBannedPlayers.add(names);
				}
				log.info(IPBannedPlayers.size()
						+ " IPs was added to the IPban list.");
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void loadMacBanList() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(LOCATION
					+ "macs.txt"));
			String names = "";
			try {
				log.info("Reading macs.txt...");
				while ((names = reader.readLine()) != null) {
					macAddress.add(names);
				}
				log.info(macAddress.size()
						+ " mac address was added to the mac ban list.");
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
