package rs2.abyssalps.content.minigame.kraken;

import rs2.abyssalps.model.npcs.NPC;
import rs2.util.CycledState;

public class KrakenTransformation extends CycledState {
	
	private final NPC npc;
	private final int targetId;
	private final int animationId;
	private final int delay;
	
	public KrakenTransformation(NPC npc, int targetId, int animationId, int delay) {
		this.npc = npc;
		this.targetId = targetId;
		this.animationId = animationId;
		this.delay = delay;
	}
	
	@Override
	public boolean process() {
		increaseCycle();
		switch (getCycle()) {
		case 1:
			npc.setVisible(false);
			npc.setCanAttack(false);
			break;
		case 2:
			npc.npcType = targetId;
			npc.setVisible(true);
			npc.startAnimation(animationId);
			break;
		}
		if (isFinished()) {
			npc.setCanAttack(true);
		}
		return false;
	}

	@Override
	public boolean isFinished() {
		return getCycle() >= delay;
	}

}