package rs2.abyssalps.content.minigame.zulrah;

import rs2.abyssalps.model.objects.Object;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.player.Client;
import rs2.util.CycledState;
import rs2.util.Misc;

public class ZulrahToxicClouds extends CycledState {

	private Position firstPosition, secondPosition;
	private final NPC zulrah;
	private final Client client;

	public ZulrahToxicClouds(NPC zulrah, Client client) {
		this.zulrah = zulrah;
		this.client = client;
		zulrah.setCanAttack(false);
	}

	@Override
	public boolean process() {
		increaseCycle();
		switch (getCycle()) {
		case 1:
			castClouds();
			break;
		case 6:
			spawnClouds();
			castClouds();
			break;
		case 11:
			spawnClouds();
			break;
		}
		return false;
	}

	@Override
	public boolean isFinished() {
		return getCycle() >= 11;
	}

	public void castClouds() {
		firstPosition = getRandomPosition();
		secondPosition = getRandomPosition();
		zulrah.turnNpc(firstPosition.getX(), firstPosition.getY());
		zulrah.startAnimation(ZulrahConstants.FARCAST_ANIMATION);
		castProjectile(firstPosition);
		castProjectile(secondPosition);
	}
	
	public void castProjectile(Position position) {
		int nX = zulrah.getX() + 2;
		int nY = zulrah.getY() + 2;
		int pX = position.getX() + 1;
		int pY = position.getY() + 1;
		int offX = (nY - pY) * -1;
		int offY = (nX - pX) * -1;
		int speed = 105 + (zulrah.distanceTo(position.getX(), position.getY()) * 5);
		client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, speed, ZulrahConstants.TOXIC_CLOUD_PROJECTILE_ID,
				83, 33, 0, 95, 0, 36);

	}

	public void spawnClouds() {
		new Object(ZulrahConstants.TOXIC_CLOUD_ID, firstPosition.getX(), firstPosition.getY(), client.heightLevel,
				0, 10, -1, 30);
		new Object(ZulrahConstants.TOXIC_CLOUD_ID, secondPosition.getX(), secondPosition.getY(), client.heightLevel,
				0, 10, -1, 30);
	}
	
	public Position getRandomPosition() {
		return ZulrahConstants.TOXIC_CLOUD_POSITIONS[Misc.random(ZulrahConstants.TOXIC_CLOUD_POSITIONS.length - 1)];
	}

}
