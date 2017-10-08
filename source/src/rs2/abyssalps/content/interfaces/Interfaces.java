package rs2.abyssalps.content.interfaces;

import rs2.abyssalps.content.PlayerContent;
import rs2.abyssalps.model.items.defs.ItemDefinitions;
import rs2.abyssalps.model.player.Client;

public class Interfaces extends PlayerContent {

	private int selectedId = -1;

	private SelectionListener selectionListener = null;
	private AmountListener amountListener = null;

	public Interfaces(Client client) {
		super(client);
	}

	public int getButtonSelection(int buttonId) {
		switch (buttonId) {
		case 9157:
		case 9167:
		case 9178:
		case 9190:
		case 34167:
		case 34168:
		case 34169:
		case 34170:
			return 0;
		case 9158:
		case 9168:
		case 9179:
		case 9191:
		case 34171:
		case 34172:
		case 34173:
		case 34174:
			return 1;
		case 9169:
		case 9180:
		case 9192:
			return 2;
		case 9181:
		case 9193:
			return 3;
		case 9194:
			return 4;
		}
		return -1;
	}

	public int getButtonAmount(int buttonId) {
		switch (buttonId) {
		case 34167:
		case 34171:
			return 0;
		case 34168:
		case 34172:
			return 10;
		case 34169:
		case 34173:
			return 5;
		case 34170:
		case 34174:
			return 1;
		}
		return -1;
	}

	public boolean handleButton(int buttonId) {
		selectedId = getButtonSelection(buttonId);

		if (selectedId == -1) {
			return false;
		}
		
		if (amountListener != null) {
			int amount = getButtonAmount(buttonId);
			if (amount == 0) {
				openAmount();
			} else {
				handleAmount(amount);
			}
			return true;
		} else if (selectionListener != null) {
			handleSelection();
			return true;
		}
		
		return false;
	}

	public boolean handleAmount(int amount) {
		if (amountListener != null && selectedId != -1) {
			amountListener.onAmount(selectedId, amount);
			reset();
			return true;
		}
		return false;
	}

	public boolean handleSelection() {
		if (selectedId == -1) {
			return false;
		}
		selectionListener.onSelect(selectedId);
		reset();
		return true;
	}

	public void openAmount() {
		getClient().getOutStream().createFrame(27);
	}

	public void openItemSelection(String title, int itemA, int itemB, AmountListener listener) {
		ItemDefinitions defA = ItemDefinitions.forId(itemA);
		ItemDefinitions defB = ItemDefinitions.forId(itemB);
		if (defA == null || defB == null) {
			return;
		}
		int itemSelectionInterface = 8866;
		getClient().getPA().sendFrame126(title, 8879); // Title
		getClient().getPA().sendFrame246(8870, 250, itemB); // Right picture
		getClient().getPA().sendFrame246(8869, 250, itemA); // Left picture
		getClient().getPA().sendFrame126(defA.getName(), 8871); // Left black text
		getClient().getPA().sendFrame126(defA.getName(), 8874); // Left hover white text
		getClient().getPA().sendFrame126(defB.getName(), 8878); // Right black text
		getClient().getPA().sendFrame126(defB.getName(), 8875); // Right hover white text
		getClient().getPA().sendFrame164(itemSelectionInterface); // Interface id
		this.amountListener = listener;
	}

	public void openOptions(SelectionListener selectionListener, String title, String option, String... options) {
		if (options.length > 3) {
			getClient().getPA().sendFrame126(title, 2493);
			getClient().getPA().sendFrame126(option, 2494);
			getClient().getPA().sendFrame126(options[0], 2495);
			getClient().getPA().sendFrame126(options[1], 2496);
			getClient().getPA().sendFrame126(options[2], 2497);
			getClient().getPA().sendFrame126(options[3], 2498);
			getClient().getPA().sendFrame164(2492);
		} else if (options.length > 2) {
			getClient().getPA().sendFrame126(title, 2481);
			getClient().getPA().sendFrame126(option, 2482);
			getClient().getPA().sendFrame126(options[0], 2483);
			getClient().getPA().sendFrame126(options[1], 2484);
			getClient().getPA().sendFrame126(options[2], 2485);
			getClient().getPA().sendFrame164(2480);
		} else if (options.length > 1) {
			getClient().getPA().sendFrame126(title, 2470);
			getClient().getPA().sendFrame126(option, 2471);
			getClient().getPA().sendFrame126(options[0], 2472);
			getClient().getPA().sendFrame126(options[1], 2473);
			getClient().getPA().sendFrame164(2469);
		} else {
			getClient().getPA().sendFrame126(title, 2460);
			getClient().getPA().sendFrame126(option, 2461);
			getClient().getPA().sendFrame126(options[0], 2462);
			getClient().getPA().sendFrame164(2459);
		}
		this.selectionListener = selectionListener;
	}

	public void reset() {
		selectedId = -1;
		amountListener = null;
		selectionListener = null;
		getClient().getPA().closeAllWindows();
	}

}