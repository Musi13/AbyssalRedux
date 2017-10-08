package rs2.util.mysql;

import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.abyssalps.model.player.PlayerSave;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Donation implements Runnable {

	public static void main(String[] args) {
		new Thread(new Donation(null)).start();
	}

	private Client player;

	public Donation(Client player) {
		this.player = player;
	}

	@Override
	public void run() {
		try {
			URLBuilder builder = new URLBuilder(
					"http://51.255.45.125/~abyssalps/store/validate.php");
			builder.addParam("auth", "96klR-3o32Df");
			builder.addParam("user", player.playerName);

			InputStream stream = new URL(builder.getFullUrl()).openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream));

			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(" ");

				int id = Integer.parseInt(data[0]);
				String item_name = data[1].replace("_", " ");
				int amount = Integer.parseInt(data[2]);
				int quantity = Integer.parseInt(data[3]);
				String currency = data[4];
				String sender = data[5];
				String receiver = data[6];
				String username = data[7];

				String error = null;

				if (!item_name.equalsIgnoreCase("AbyssalPS Tokens")) {
					error = "Item name has been changed";
				} else if (amount != quantity) {
					error = "paid amount does not equal quantity";
				} else if (!currency.equalsIgnoreCase("USD")) {
					error = "Currency is not USD";
				} else if (!receiver.equalsIgnoreCase("exielez@arravps.com")) {
					error = "Receiver email is not correct! Lean is gay";
				} else {
					player.getPoints()[2] += amount;
					player.getPA().sendFrame126(
							"@whi@AbyssalPS Tokens: @gre@"
									+ player.getPoints()[2], 29165);
					player.sendMessage("Your account was credited @blu@"
							+ amount + "@bla@ AbyssalPS Tokens.");
				}

				if (error != null) {
					System.err.println(String.format("Invalid purchase! %s",
							error));
					System.err.println(String.format(
							"ID: %d, Username: %s, Email: %s", id, username,
							receiver));
				}

			}

			stream.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class URLBuilder {

		private StringBuilder url;

		public URLBuilder(String url) {
			this.url = new StringBuilder();
			this.url.append(url);
		}

		public void addParam(String param, Object value)
				throws UnsupportedEncodingException {
			String newValue = convertToString(value);
			String[] split = this.url.toString().split("\\?");

			this.url.append(split.length < 2 ? "?" : "&").append(param)
					.append("=").append(URLEncoder.encode(newValue, "UTF-8"));
		}

		public String convertToString(Object value) {
			String newValue = "";
			if (value instanceof String)
				newValue = (String) value;
			else if (value instanceof Integer)
				newValue = Integer.toString((int) value);
			else if (value instanceof Long)
				newValue = Long.toString((long) value);
			else if (value instanceof Double)
				newValue = Double.toString((double) value);
			return newValue;
		}

		public String getFullUrl() {
			return url.toString();
		}

	}

}
