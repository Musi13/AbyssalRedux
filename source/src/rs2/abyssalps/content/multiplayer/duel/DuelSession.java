package rs2.abyssalps.content.multiplayer.duel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import rs2.Config;
import rs2.Server;
import rs2.abyssalps.content.bank.BankItem;
import rs2.abyssalps.content.multiplayer.MultiplayerSession;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionFinalizeType;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionStage;
import rs2.abyssalps.content.multiplayer.MultiplayerSessionType;
import rs2.abyssalps.content.multiplayer.duel.DuelSessionRules.Rule;
import rs2.abyssalps.model.Boundary;
import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.items.ItemAssistant;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;
import rs2.util.Misc;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;
import rs2.util.tools.log.LogSystem;
import rs2.util.tools.log.LogTypes;

public class DuelSession extends MultiplayerSession {

	static final Boundary NO_OBSTACLE_ARENA = Boundary.DUEL_ARENAS[0];

	static final Boundary OBSTACLE_ARENA = Boundary.DUEL_ARENAS[1];

	DuelSessionRules rules = new DuelSessionRules();

	private Optional<Client> winner = Optional.empty();

	private Boundary arenaBoundary;

	private boolean attackingOperationable;

	private long lastRuleModification;

	public DuelSession(List<Client> players, MultiplayerSessionType type) {
		super(players, type);
	}

	@Override
	public void accept(Client player, Client recipient, int stageId) {
		switch (stageId) {
		case MultiplayerSessionStage.OFFER_ITEMS:
			if (System.currentTimeMillis() - lastRuleModification < 5_000) {
				player.sendMessage("<col=CC0000>A rule was changed in the last 5 seconds, you cannot accept yet.");
				player.getPA()
						.sendFrame126(
								"A rule was changed in recently, you cannot accept yet.",
								6684);
				return;
			}
			if (recipient.getItems().freeSlots() < getItems(player).size()) {
				player.getPA().sendFrame126(
						"You have offered more items than "
								+ recipient.playerName + " has free space.",
						6684);
				recipient.getPA().sendFrame126(
						"You do not have enough inventory space to continue.",
						6684);
				return;
			}
			if (recipient.getItems().freeSlots() < getDisabledEquipmentCount(recipient)) {
				player.getPA()
						.sendFrame126(
								"Player doesn't have enough space to unequip the disabled items.",
								6684);
				recipient
						.getPA()
						.sendFrame126(
								"Not enough space to remove the disabled equipped items.",
								6684);
				return;
			}
			if (rules.contains(Rule.NO_MELEE) && rules.contains(Rule.NO_MAGE)
					&& rules.contains(Rule.NO_WEAPON)) {
				player.getPA()
						.sendFrame126(
								"You cannot have no melee, no mage and no weapon selected.",
								6684);
				recipient
						.getPA()
						.sendFrame126(
								"You cannot have no melee, no mage and no weapon selected.",
								6684);
				return;
			}
			for (Client p : players) {
				GameItem overlap = getOverlappedItem(p);
				if (overlap != null) {
					p.getPA()
							.sendFrame126(
									"Too many of one item! The other player has "
											+ Misc.getValueRepresentation(overlap.amount)
											+ " "
											+ ItemAssistant.getItemName(overlap.id)
											+ " in their inventory.", 6684);
					getOther(p)
							.getPA()
							.sendFrame126(
									"The other player has offered too many of one item, they must remove some.",
									6684);
					return;
				}
			}
			if (stage.hasAttachment() && stage.getAttachment() != player) {
				stage.setStage(MultiplayerSessionStage.CONFIRM_DECISION);
				stage.setAttachment(null);
				updateMainComponent();
				return;
			}
			player.getPA().sendFrame126("Waiting for other player...", 6684);
			stage.setAttachment(player);
			recipient.getPA().sendFrame126("Other player has accepted", 6684);
			break;

		case MultiplayerSessionStage.CONFIRM_DECISION:
			if (stage.hasAttachment() && stage.getAttachment() != player) {
				stage.setStage(MultiplayerSessionStage.FURTHER_INTERACTION);
				Client opponent = getOther(player);
				clearPlayerAttributes(player);
				clearPlayerAttributes(opponent);
				arenaBoundary = rules.contains(Rule.OBSTACLES) ? OBSTACLE_ARENA
						: NO_OBSTACLE_ARENA;
				int teleportX = arenaBoundary.getMinimumX() + 6
						+ Misc.random(12);
				int teleportY = arenaBoundary.getMinimumY() + 1
						+ Misc.random(11);
				player.getPA().movePlayer(teleportX, teleportY, 0);
				opponent.getPA().movePlayer(teleportX, teleportY - 1, 0);
				player.getPA().createPlayerHints(10, opponent.playerId);
				opponent.getPA().createPlayerHints(10, player.playerId);
				player.getPA().removeAllWindows();
				opponent.getPA().removeAllWindows();
				removeDisabledEquipment(player);
				removeDisabledEquipment(opponent);
				CycleEventHandler.getSingleton().addEvent(this,
						new AttackingOperation(), 2);
				return;
			}
			stage.setAttachment(player);
			player.getPA().sendFrame126("Waiting for other player...", 6571);
			recipient.getPA().sendFrame126("Other player has accepted", 6571);
			break;

		default:
			finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			break;
		}
	}

	@Override
	public void updateOfferComponents() {
		for (Client player : items.keySet()) {
			Client recipient = getOther(player);
			player.getItems().resetItems(3322);
			refreshItemContainer(player, player, 6669);
			refreshItemContainer(player, getOther(player), 6670);
			player.getPA().sendFrame126("", 6684);
			player.getPA().sendFrame126(
					"Dueling with: " + recipient.playerName + " (level-"
							+ recipient.combatLevel + ")", 6671);
		}
	}

	@Override
	public boolean itemAddable(Client player, GameItem item) {
		if (!player.getItems().tradeable(item.id)) {
			player.sendMessage("You cannot stake this item, it is deemed as untradable.");
			return false;
		}
		if (item.id == 12926 || item.id == 12931 || item.id == 12904) {
			player.sendMessage("You cannot stake this item, it is deemed as untradable.");
			return false;
		}
		if (stage.getStage() != MultiplayerSessionStage.OFFER_ITEMS) {
			finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return false;
		}
		return true;
	}

	@Override
	public boolean itemRemovable(Client player, GameItem item) {
		if (!Server.getMultiplayerSessionListener().inAnySession(player)) {
			finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return false;
		}
		if (stage.getStage() != MultiplayerSessionStage.OFFER_ITEMS) {
			return false;
		}
		return true;
	}

	@Override
	public void updateMainComponent() {
		if (stage.getStage() == MultiplayerSessionStage.OFFER_ITEMS) {
			for (Client player : players) {
				Client recipient = getOther(player);
				player.setTrading(true);
				player.getItems().resetItems(3322);
				refreshItemContainer(player, player, 6669);
				refreshItemContainer(player, player, 6670);
				player.getPA().sendFrame126(
						"Dueling with: " + recipient.playerName + " (level-"
								+ recipient.combatLevel + ")", 6671);
				player.getPA().sendFrame126("", 6684);
				player.getPA().sendFrame248(6575, 3321);
				player.getPA().sendFrame87(286, 0);
			}
		} else if (stage.getStage() == MultiplayerSessionStage.CONFIRM_DECISION) {
			for (Client player : players) {
				Client recipient = getOther(player);
				player.getItems().resetItems(3214);
				StringBuilder itemList = new StringBuilder();
				List<GameItem> items = getItems(player);
				for (GameItem item : items) {
					if (item.id > 0 && item.amount > 0) {
						itemList.append(ItemAssistant.getItemName(item.id)
								+ " x "
								+ Misc.getValueRepresentation(item.amount)
								+ "\\n");
					}
				}
				player.getPA().sendFrame126(itemList.toString(), 6516);
				itemList = new StringBuilder();
				items = getItems(recipient);
				for (GameItem item : items) {
					if (item.id > 0 && item.amount > 0) {
						itemList.append(ItemAssistant.getItemName(item.id)
								+ " x "
								+ Misc.getValueRepresentation(item.amount)
								+ "\\n");
					}
				}
				player.getPA().sendFrame126(itemList.toString(), 6517);
				player.getPA().sendFrame126("", 8242);
				for (int i = 8238; i <= 8253; i++) {
					player.getPA().sendFrame126("", i);
				}
				player.getPA()
						.sendFrame126("Hitpoints will be restored.", 8250);
				player.getPA().sendFrame126("Boosted stats will be reset.",
						8238);
				int offset = 0;
				for (Rule rule : rules.rules) {
					if (!rule.getDetails().isEmpty()) {
						player.getPA().sendFrame126(rule.getDetails(),
								8242 + offset);
						offset++;
					}
				}
				player.getPA().sendFrame126("", 6571);
				player.getPA().sendFrame248(6412, 197);
			}
		}
	}

	@Override
	public void give() {
		if (!winner.isPresent()) {
			return;
		}
		players.forEach(player -> moveAndClearAttributes(player));
		if (!Objects.equals(getOther(winner.get()), winner.get())) {
			items.get(winner.get()).addAll(items.get(getOther(winner.get())));
			if (items.get(winner.get()).size() > 0) {
				for (GameItem item : items.get(winner.get())) {
					long totalSum = (long) winner.get().getItems()
							.getItemAmount(item.id)
							+ item.amount;
					if (winner.get().getItems().freeSlots() == 0
							|| winner.get().getItems().playerHasItem(item.id)
							&& totalSum > Integer.MAX_VALUE) {
						LogSystem.writeToFile(winner.get().playerName,
								Misc.formatPlayerName(winner.get().playerName)
										+ " won " + item.amount + " "
										+ ItemAssistant.getItemName(item.id)
										+ " from "
										+ getOther(winner.get()).playerName,
								LogTypes.DUEL);
						winner.get()
								.getItems()
								.sendItemToAnyTabOrDrop(
										new BankItem(item.id, item.amount),
										Config.DUELING_RESPAWN_X
												+ (Misc.random(Config.RANDOM_DUELING_RESPAWN)),
										Config.DUELING_RESPAWN_Y
												+ (Misc.random(Config.RANDOM_DUELING_RESPAWN)));
					} else {
						LogSystem.writeToFile(winner.get().playerName,
								Misc.formatPlayerName(winner.get().playerName)
										+ " won " + item.amount + " "
										+ ItemAssistant.getItemName(item.id)
										+ " from "
										+ getOther(winner.get()).playerName,
								LogTypes.DUEL);
						winner.get().getItems().addItem(item.id, item.amount);
					}
				}
			}
			showRewardComponent(winner.get());
		} else {
			winner.get().sendMessage(
					"You cannot be the winner and the loser of a duel.");
		}
		Server.itemHandler.reloadItems(winner.get());
		items.clear();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void withdraw() {
		for (Client player : items.keySet()) {
			if (Objects.isNull(player)) {
				continue;
			}
			if (items.get(player).size() <= 0) {
				continue;
			}
			for (GameItem item : items.get(player)) {
				player.getItems().addItem(item.id, item.amount);
			}
		}
	}

	public void toggleRule(Client player, Rule rule) {
		if (stage.getStage() != MultiplayerSessionStage.OFFER_ITEMS) {
			player.sendMessage("You cannot change rules whilst on the second interface.");
			return;
		}
		if (rule.equals(Rule.NO_MELEE) || rule.equals(Rule.NO_RANGE)
				|| rule.equals(Rule.NO_MAGE)) {
			long count = rules.rules
					.stream()
					.filter(r -> r.equals(Rule.NO_MELEE)
							|| r.equals(Rule.NO_RANGE)
							|| r.equals(Rule.NO_MAGE)).count();
			if (count >= 2 && !rules.contains(rule)) {
				player.getPA().sendFrame126(
						"You must fight with at least one combat style.", 6684);
				return;
			}
		}
		if (rules.contains(rule)) {
			rules.setTotalValue(rules.getTotalValue() - rule.getValue());
			rules.remove(rule);
		} else {
			rules.setTotalValue(rules.getTotalValue() + rule.getValue());
			rules.add(rule);
		}
		if (rules.contains(Rule.WHIP_AND_DDS) && rule != Rule.WHIP_AND_DDS
				&& rule != Rule.NO_SPECIAL_ATTACK) {
			if (rule.equals(Rule.NO_MELEE) || rule.equals(Rule.OBSTACLES)
					|| rule.equals(Rule.NO_WEAPON)) {
				rules.remove(Rule.WHIP_AND_DDS);
				rules.setTotalValue(rules.getTotalValue()
						- Rule.WHIP_AND_DDS.getValue());
			}
		}
		lastRuleModification = System.currentTimeMillis();
		stage.setAttachment(null);
		player.getPA().sendFrame126("", 6684);
		getOther(player).getPA().sendFrame126("", 6684);
		refreshRules();
	}

	public void moveAndClearAttributes(Client player) {
		player.getItems().addSpecialBar(
				player.playerEquipment[player.playerWeapon]);
		player.getPA().showOption(3, 0, "Challenge", 3);
		player.getPA().createPlayerHints(10, -1);
		player.getPA().movePlayer(Config.DUELING_RESPAWN_X,
				Config.DUELING_RESPAWN_Y, 0);
		player.freezeTimer = 1;
		player.getPA().resetFollow();
		player.getCombat().resetPlayerAttack();
		player.poisonDamage = 0;
		player.venomDamage = 0;
		player.isSkulled = false;
		player.attackedPlayers.clear();
		player.headIconPk = -1;
		player.skullTimer = -1;
		player.getPA().requestUpdates();
		clearPlayerAttributes(player);
	}

	private void clearPlayerAttributes(Client player) {
		for (int i = 0; i < player.playerLevel.length; i++) {
			player.playerLevel[i] = player.getPA().getLevelForXP(
					player.playerXP[i]);
			player.getPA().refreshSkill(i);
		}
		player.specAmount = 10.0;
		player.getItems().addSpecialBar(player.playerEquipment[3]);
		player.getCombat().resetPrayers();
		player.vengOn = false;
		player.usingSpecial = false;
		player.poisonDamage = 0;
		player.getItems().updateSpecialBar();
		player.doubleHit = false;
	}

	public void showRewardComponent(Client c) {
		if (Objects.isNull(c) || Objects.isNull(getOther(c))) {
			return;
		}
		List<GameItem> itemList = items.get(getOther(c));
		if (itemList.size() > 28) {
			itemList.subList(0, 27);
		}
		c.getPA().sendFrame126(Integer.toString(getOther(c).combatLevel), 6839);
		c.getPA().sendFrame126(getOther(c).playerName, 6840);
		c.getPA().showInterface(6733);
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(6822);
		c.getOutStream().writeWord(itemList.size());
		for (GameItem item : itemList) {
			if (item.amount > 254) {
				c.getOutStream().writeByte(255);
				c.getOutStream().writeDWord_v2(item.amount);
			} else {
				c.getOutStream().writeByte(item.amount);
			}
			if (item.id > Config.ITEM_LIMIT || item.id < 0) {
				item = new GameItem(Config.ITEM_LIMIT, item.id);
			}
			c.getOutStream().writeWordBigEndianA(item.id + 1);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
	}

	public void sendDuelEquipment() {
		players.stream().filter(Objects::nonNull).forEach(c -> {
			for (int i = 0; i < c.playerEquipment.length; i++) {
				c.getOutStream().createFrameVarSizeWord(34);
				c.getOutStream().writeWord(13824);
				c.getOutStream().writeByte(i);
				c.getOutStream().writeWord(c.playerEquipment[i] + 1);
				if (c.playerEquipment[i] > -1) {
					if (c.playerEquipmentN[i] > 254) {
						c.getOutStream().writeByte(255);
						c.getOutStream().writeDWord(c.playerEquipmentN[i]);
					} else {
						c.getOutStream().writeByte(c.playerEquipmentN[i]);
					}
				} else {
					c.getOutStream().writeByte(0);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();
			}
		});
	}

	private int getDisabledEquipmentCount(Client player) {
		final int MINIMUM_VALUE = Rule.NO_HELM.ordinal();
		int count = 0;
		for (Rule rule : Rule.values()) {
			if (rule.ordinal() < MINIMUM_VALUE) {
				continue;
			}
			if (!rules.contains(rule)) {
				continue;
			}
			int equipmentSlot = rule.ordinal() - MINIMUM_VALUE;
			if (equipmentSlot >= 6) {
				equipmentSlot++;
				if (equipmentSlot >= 8) {
					equipmentSlot++;
					if (equipmentSlot >= 11) {
						equipmentSlot++;
					}
				}
			}
			if (equipmentSlot == 6 || equipmentSlot == 8 || equipmentSlot == 11) {
				continue;
			}
			if (player.playerEquipment[equipmentSlot] > -1) {
				count++;
			}
		}
		return count;
	}

	private void removeDisabledEquipment(Client player) {
		final int MINIMUM_VALUE = Rule.NO_HELM.ordinal();
		for (Rule rule : Rule.values()) {
			if (rule.ordinal() < MINIMUM_VALUE) {
				continue;
			}
			if (!rules.contains(rule)) {
				continue;
			}
			int equipmentSlot = rule.ordinal() - MINIMUM_VALUE;
			if (equipmentSlot >= 6) {
				equipmentSlot++;
				if (equipmentSlot >= 8) {
					equipmentSlot++;
					if (equipmentSlot >= 11) {
						equipmentSlot++;
					}
				}
			}
			if (equipmentSlot == 6 || equipmentSlot == 8 || equipmentSlot == 11) {
				continue;
			}
			if (player.playerEquipment[equipmentSlot] > -1) {
				player.getItems().removeItem(
						player.playerEquipment[equipmentSlot], equipmentSlot);
			}
		}
	}

	public void setWinner(Client winner) {
		this.winner = Optional.of(winner);
	}

	public Optional<Client> getWinner() {
		return winner;
	}

	public void refreshRules() {
		players.stream()
				.filter(Objects::nonNull)
				.forEach(p -> p.getPA().sendFrame87(286, rules.getTotalValue()));
	}

	public DuelSessionRules getRules() {
		return rules;
	}

	public Boundary getArenaBoundary() {
		return arenaBoundary;
	}

	public boolean isAttackingOperationable() {
		return attackingOperationable;
	}

	class AttackingOperation extends CycleEvent {

		int time = 3;

		@Override
		public void execute(CycleEventContainer container) {
			for (Client player : players) {
				if (Objects.isNull(player)) {
					finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
					container.stop();
					return;
				}
			}
			if (time <= 0) {
				players.stream().filter(Objects::nonNull)
						.forEach(p -> p.forcedChat("FIGHT!"));
				attackingOperationable = true;
				container.stop();
			} else if (time > 0) {
				players.stream().filter(Objects::nonNull)
						.forEach(p -> p.forcedChat(Integer.toString(time)));
				time--;
			} else {
				container.stop();
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void logSession(MultiplayerSessionFinalizeType type) {

	}

	private String createQuery(
			ArrayList<Entry<Client, String>> participantList,
			MultiplayerSessionFinalizeType type) {
		String status;
		switch (type) {
		case GIVE_ITEMS:
			status = "Completed";
			break;
		default:
			status = "Error";
			break;
		}

		Client player = participantList.get(0).getKey();
		String items = participantList.get(0).getValue();
		Client playerOther = participantList.get(1).getKey();
		String itemsOther = participantList.get(1).getValue();

		String query = "INSERT into stakes (DATE, STAKESTATUS, WINNER, PLAYER, IP, STAKED, OTHERPLAYER, OTHERIP, OTHERSTAKED) VALUES(NOW(), '"
				+ status
				+ "', '"
				+ winner.get().playerName
				+ "', '"
				+ player.playerName
				+ "', '"
				+ getIp(player)
				+ "', '"
				+ items
				+ "', '"
				+ playerOther.playerName
				+ "', '"
				+ getIp(playerOther)
				+ "', '" + itemsOther + "')";
		return query;
	}

	private String getIp(Client player) {
		return "";
	}

	private String createItemList(Client player) {
		if (items.get(player).size() == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (GameItem item : items.get(player)) {
			sb.append(ItemAssistant.getItemName(item.id));
			if (item.amount != 1) {
				sb.append(" x" + item.amount);
			}
			sb.append(", ");
		}
		return sb.substring(0, sb.length() - 2).replaceAll("'", "\\\\'");
	}

}
