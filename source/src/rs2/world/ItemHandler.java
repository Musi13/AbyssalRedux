package rs2.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.model.items.GroundItem;
import rs2.abyssalps.model.items.Item;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.abyssalps.model.player.Player;
import rs2.abyssalps.model.player.PlayerHandler;
import rs2.util.Misc;
import rs2.util.tools.log.LogSystem;
import rs2.util.tools.log.LogTypes;

/**
 * Handles ground items
 **/

public class ItemHandler {

	public List<GroundItem> items = new ArrayList<GroundItem>();
	public static final int HIDE_TICKS = 100;

	public ItemHandler() {
		loadItemPrices();
	}

	private static int[] itemPrice = new int[Config.ITEM_LIMIT];

	public static int[] getItemPrice() {
		return itemPrice;
	}

	public void loadItemPrices() {
		try {
			Scanner scanner = new Scanner(new File("./Data/cfg/prices.txt"));
			String line;
			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				String[] line1 = line.split(":");
				itemPrice[Integer.valueOf(line1[0])] = Integer
						.valueOf(line1[1]);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Adds item to list
	 **/
	public void addItem(GroundItem item) {
		items.add(item);
	}

	/**
	 * Removes item from list
	 **/
	public void removeItem(GroundItem item) {
		items.remove(item);
	}

	/**
	 * Item amount
	 **/
	public int itemAmount(String player, int itemId, int itemX, int itemY,
			int height) {
		for (GroundItem i : items) {
			if (i.hideTicks >= 1 && player.equalsIgnoreCase(i.getController())
					|| i.hideTicks < 1) {
				if (i.getId() == itemId && i.getX() == itemX
						&& i.getY() == itemY && i.getHeight() == height) {
					return i.getAmount();
				}
			}
		}
		return 0;
	}

	/**
	 * Item exists
	 **/
	public boolean itemExists(int itemId, int itemX, int itemY, int height) {
		for (GroundItem i : items) {
			if (i.getId() == itemId && i.getX() == itemX && i.getY() == itemY
					&& i.getHeight() == height) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Reloads any items if you enter a new region
	 **/
	public void reloadItems(Client player) {
		Predicate<GroundItem> visible = item -> (((player.getItems().tradeable(
				item.getId()) || item.getController().equalsIgnoreCase(
				player.playerName)) && player.distanceToPoint(item.getX(),
				item.getY()) <= 60)
				&& (item.hideTicks > 0
						&& item.getController().equalsIgnoreCase(
								player.playerName) || item.hideTicks == 0) && player.heightLevel == item
				.getHeight());
		items.stream().filter(visible)
				.forEach(item -> player.getItems().removeGroundItem(item));
		items.stream().filter(visible)
				.forEach(item -> player.getItems().createGroundItem(item));
	}

	public void process() {
		Iterator<GroundItem> it = items.iterator();
		while (it.hasNext()) {
			GroundItem i = it.next();
			if (i == null)
				continue;
			if (i.hideTicks > 0) {
				i.hideTicks--;
			}
			if (i.hideTicks == 1) {
				i.hideTicks = 0;
				createGlobalItem(i);
				i.removeTicks = HIDE_TICKS;
			}
			if (i.removeTicks > 0) {
				i.removeTicks--;
			}
			if (i.removeTicks == 1) {
				i.removeTicks = 0;
				PlayerHandler
						.stream()
						.filter(Objects::nonNull)
						.filter(p -> p.distanceToPoint(i.getX(), i.getY()) <= 60)
						.forEach(
								p -> ((Client) p).getItems().removeGroundItem(
										i.getId(), i.getX(), i.getY(),
										i.getAmount()));
				it.remove();
			}
		}
	}

	/**
	 * Creates the ground item
	 **/
	public int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 },
			{ 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 }, { 4720, 4896 },
			{ 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 },
			{ 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 },
			{ 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 }, { 4745, 4956 },
			{ 4747, 4926 }, { 4749, 4968 }, { 4751, 4994 }, { 4753, 4980 },
			{ 4755, 4986 }, { 4757, 4992 }, { 4759, 4998 } };

	public void createGroundItem(Client c, int itemId, int itemX, int itemY,
			int height, int itemAmount, int playerId) {
		try {
			if (itemId > 0) {
				if (c.heightLevel != height) {
					return;
				}
				if (itemId >= 2412 && itemId <= 2414) {
					c.sendMessage("The cape vanishes as it touches the ground.");
					return;
				}
				if (!Item.itemStackable[itemId] && itemAmount > 0) {
					if (itemAmount > 28)
						itemAmount = 28;
					for (int j = 0; j < itemAmount; j++) {
						c.getItems().createGroundItem(itemId, itemX, itemY, 1);
						GroundItem item = new GroundItem(itemId, itemX, itemY,
								height, 1, HIDE_TICKS,
								PlayerHandler.players[playerId].playerName);
						addItem(item);
					}
				} else {
					c.getItems().createGroundItem(itemId, itemX, itemY,
							itemAmount);
					GroundItem item = new GroundItem(itemId, itemX, itemY,
							height, itemAmount, HIDE_TICKS,
							PlayerHandler.players[playerId].playerName);
					addItem(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows items for everyone who is within 60 squares
	 **/
	public void createGlobalItem(GroundItem i) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = p;
				if (!person.playerName.equalsIgnoreCase(i.getController())) {
					if (!((Client) person).getItems().tradeable(i.getId())) {
						continue;
					}
					if (person.distanceToPoint(i.getX(), i.getY()) <= 60
							&& person.heightLevel == i.getHeight()) {
						((Client) person).getItems().createGroundItem(
								i.getId(), i.getX(), i.getY(), i.getAmount());
					}
				}
			}
		}
	}

	/**
	 * Removing the ground item
	 **/

	public void removeGroundItem(Client c, int itemId, int itemX, int itemY,
			int height, boolean add) {
		for (GroundItem i : items) {
			if (i.getId() == itemId && i.getX() == itemX && i.getY() == itemY
					&& i.getHeight() == height) {
				if (i.hideTicks > 0
						&& i.getController().equalsIgnoreCase(c.playerName)) {
					if (add) {
						if (c.getItems().addItem(i.getId(), i.getAmount())) {
							LogSystem.writeToFile(
									Misc.formatPlayerName(c.playerName),
									Misc.formatPlayerName(c.playerName)
											+ " picked up "
											+ Misc.formatNumbers(i.getAmount())
											+ " "
											+ ItemDefinitions.forId(i.getId())
													.getName() + "",
									LogTypes.PICKUP);
							removeControllersItem(i, c, i.getId(), i.getX(),
									i.getY(), i.getAmount());
							break;
						}
					} else {
						removeControllersItem(i, c, i.getId(), i.getX(),
								i.getY(), i.getAmount());
						break;
					}
				} else if (i.hideTicks <= 0) {
					if (add) {
						if (c.getItems().addItem(i.getId(), i.getAmount())) {
							removeGlobalItem(i, i.getId(), i.getX(), i.getY(),
									i.getHeight(), i.getAmount());
							break;
						}
					} else {
						removeGlobalItem(i, i.getId(), i.getX(), i.getY(),
								i.getHeight(), i.getAmount());
						break;
					}
				}
			}
		}
	}

	/**
	 * Remove item for just the item controller (item not global yet)
	 **/

	public void removeControllersItem(GroundItem i, Client c, int itemId,
			int itemX, int itemY, int itemAmount) {
		c.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount);
		removeItem(i);
	}

	/**
	 * Remove item for everyone within 60 squares
	 **/

	public void removeGlobalItem(GroundItem i, int itemId, int itemX,
			int itemY, int height, int itemAmount) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = p;
				if (person.distanceToPoint(itemX, itemY) <= 60
						&& person.heightLevel == height) {
					((Client) person).getItems().removeGroundItem(itemId,
							itemX, itemY, itemAmount);
				}
			}
		}
		removeItem(i);
	}

}
