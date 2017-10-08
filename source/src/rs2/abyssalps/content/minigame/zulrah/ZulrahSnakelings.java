package rs2.abyssalps.content.minigame.zulrah;

import rs2.Server;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.npcs.NPC;
import rs2.abyssalps.model.player.Client;
import rs2.util.CycledState;
import rs2.util.Misc;

public class ZulrahSnakelings extends CycledState {

	private Position position;
	private final NPC zulrah;
	private final Client client;

	public ZulrahSnakelings(NPC zulrah, Client client) {
		this.zulrah = zulrah;
		this.client = client;
		zulrah.setCanAttack(false);
	}

	@Override
	public boolean process() {
		increaseCycle();
		switch (getCycle()) {
		case 1:
			castSnakeling();
			break;
		case 6:
			spawnSnakelings();
			castSnakeling();
			break;
		case 11:
			spawnSnakelings();
			break;
		}
		return false;
	}

	@Override
	public boolean isFinished() {
		return getCycle() >= 11;
	}

	public void castSnakeling() {
		position = getRandomPosition();
		zulrah.turnNpc(position.getX(), position.getY());
		zulrah.startAnimation(ZulrahConstants.FARCAST_ANIMATION);
		castProjectile(position);
	}

	public void castProjectile(Position position) {
		int nX = zulrah.getX() + 2;
		int nY = zulrah.getY() + 2;
		int pX = position.getX() + 1;
		int pY = position.getY() + 1;
		int offX = (nY - pY) * -1;
		int offY = (nX - pX) * -1;
		int speed = 105 + (zulrah.distanceTo(position.getX(), position.getY()) * 5);
		client.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, speed, ZulrahConstants.SNAKELING_PROJECTILE_ID,
				83, 33, 0, 95, 0, 36);

	}

	public void spawnSnakelings() {
		NPC snakeling = Server.npcHandler.spawnNpc(client, ZulrahConstants.SNAKELING_ID, position.getX(), position.getY(),
				client.heightLevel, 1, 1, 15, 99, 1, true, false);
	}

	public Position getRandomPosition() {
		return ZulrahConstants.TOXIC_CLOUD_POSITIONS[Misc.random(ZulrahConstants.TOXIC_CLOUD_POSITIONS.length - 1)];
	}

}
