package rs2.abyssalps.content.minigame.zulrah;

import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.npcs.NPC;
import rs2.util.CycledState;

public class ZulrahTransformation extends CycledState {

	private NPC npc;
	private int targetNpcId;
	private Position targetPosition;
	
	public ZulrahTransformation(NPC npc, int targetNpcId, Position targetPosition) {
		this.npc = npc;
		this.targetNpcId = targetNpcId;
		this.targetPosition = targetPosition;
		setState(States.DIVING);
		npc.setCanAttack(false);
	}
	
	@Override
	public boolean process() {
		increaseCycle();
		if (getState() == States.DIVING) {
			processDiving();
		} else if (getState() == States.MOVING) {
			processMoving();
		} else if (getState() == States.EMERGING) {
			 processEmerging();
		}
		return false;
	}
	
	public boolean isFinished() {
		return getState() == States.EMERGING && getCycle() >= 1;
	}
	
	
	public void processDiving() {
		switch (getCycle()) {
		case 3:
			npc.startAnimation(ZulrahConstants.DIVE_ANIMATION);
			break;
		case 5:
			npc.setVisible(false);
			resetState(States.MOVING);
			break;
		}
	}

	public void processMoving() {
		switch (getCycle()) {
		case 1:
			npc.absX = targetPosition.getX();
			npc.absY = targetPosition.getY();
			npc.makeX = npc.absX;
			npc.makeY = npc.absY;
			npc.npcType = targetNpcId;
			break;
		case 2:
			resetState(States.EMERGING);
			break;
		}
	}
	
	public void processEmerging() {
		switch (getCycle()) {
		case 1:
			npc.setVisible(true);
			npc.turnNpc(ZulrahConstants.ZULRAH_ISLAND_POSITION.getX(), ZulrahConstants.ZULRAH_ISLAND_POSITION.getY());
			npc.startAnimation(ZulrahConstants.EMERGE_ANIMATION);
			break;
		}
	}
	
	private static enum States {
		DIVING, MOVING, EMERGING
	}
	
}