package rs2.abyssalps.content.cannon;

import rs2.Server;
import rs2.abyssalps.content.PlayerContent;
import rs2.abyssalps.model.items.GameItem;
import rs2.abyssalps.model.objects.Object;
import rs2.abyssalps.model.player.Client;
import rs2.util.cache.region.Region;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class Cannon extends PlayerContent {

	private int cannonAmmo = 0;
	private boolean shooting = false;

	public int getCannonAmmo() {
		return this.cannonAmmo;
	}

	public void setCannonAmmo(int amount) {
		this.cannonAmmo = amount;
	}

	public boolean isShooting() {
		return this.shooting;
	}

	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}

	public Cannon(Client client) {
		super(client);
	}

	public void pickupCannon(int objectId, int objectX, int objectY) {
		for (Object object : Server.objectManager.objects) {
			if (object.objectX == objectX && object.objectY == objectY) {
				if (!object.getOwner().equalsIgnoreCase(getClient().playerName)) {
					getClient().sendMessage("This is not your cannon.");
					continue;
				}
				Region.removeWorldObject(objectId, objectId, objectY,
						getClient().heightLevel);
				Server.objectManager.removeObject(object.objectX,
						object.objectY, object.height);
				//calling bank, sec
				if (getClient().getCannon().getCannonAmmo() > 0) {
					getClient().getItems().addItem(2,
							getClient().getCannon().getCannonAmmo());
				}

				getClient().getItems().addItem(
						CannonConstants.CANNON_BARRELS_ID, 1);

				getClient().getItems().addItem(CannonConstants.CANNON_STAND_ID,
						1);

				getClient().getItems().addItem(CannonConstants.CANNON_BASE_ID,
						1);

				getClient().sendMessage("You successfully pickup your cannon.");
				continue;
			}
		}
	}

	public void assembleCannon() {
		if (!getClient().getItems().playerHasItem(
				CannonConstants.CANNON_BASE_ID)
				|| !getClient().getItems().playerHasItem(
						CannonConstants.CANNON_STAND_ID)
				|| !getClient().getItems().playerHasItem(
						CannonConstants.CANNON_BARRELS_ID)) {
			getClient()
					.sendMessage("You need all the cannon parts to do this.");
			return;
		}
		CycleEventHandler.getSingleton().stopEvents(getClient(),
				CycleEventHandler.CANNON_EVENT_ID);
		CycleEventHandler.getSingleton().addEvent(
				CycleEventHandler.CANNON_EVENT_ID, getClient(),
				new CycleEvent() {
					int timer = 10;

					@Override
					public void execute(CycleEventContainer container) {

						if (timer == 9) {
							getClient().startAnimation(827);
						} else if (timer == 8) {
							getClient().getItems().deleteItem(
									CannonConstants.CANNON_BASE_ID, 1);
							new Object(CannonConstants.CANNON_BASE_OBJECT_ID,
									getClient().getX(), getClient().getY(),
									getClient().heightLevel, 1, 10, -1, 10);
						} else if (timer == 6) {
							getClient().startAnimation(827);
						} else if (timer == 5) {
							getClient().getItems().deleteItem(
									CannonConstants.CANNON_STAND_ID, 1);
							new Object(CannonConstants.CANNON_STAND_OBJECT_ID,
									getClient().getX(), getClient().getY(),
									getClient().heightLevel, 1, 10, -1, 10);
						} else if (timer == 3) {
							getClient().startAnimation(827);
						} else if (timer == 2) {
							getClient().getItems().deleteItem(
									CannonConstants.CANNON_BARRELS_ID, 1);
							Region.addWorldObject(9, getClient().getX(),
									getClient().getY(),
									getClient().heightLevel, 1);
							new Object(
									CannonConstants.CANNON_BARRELS_OBJECT_ID,
									getClient().getX(), getClient().getY(),
									getClient().heightLevel, 1, 10, -1, 100,
									getClient().playerName);
							container.stop();
						}

						timer--;
					}

				}, 1);
	}

	public void addCannonAmmo(GameItem cannonAmmo) {
		if (!getClient().getItems().playerHasItem(cannonAmmo.id,
				cannonAmmo.amount)) {
			return;
		}
		if (getClient().getCannon().isShooting()) {
			getClient().sendMessage(
					"You can't load your cannon while it's active.");
			return;
		}
		getClient().getCannon().setCannonAmmo(
				getClient().getCannon().getCannonAmmo() + cannonAmmo.amount);
		getClient().getItems().deleteItem(cannonAmmo.id,
				getClient().getItems().getItemSlot(cannonAmmo.id),
				cannonAmmo.amount);
		getClient().sendMessage(
				"@or2@You load the cannon with " + cannonAmmo.amount
						+ " cannonballs.");
	}
}
