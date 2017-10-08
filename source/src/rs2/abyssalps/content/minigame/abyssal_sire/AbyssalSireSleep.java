package rs2.abyssalps.content.minigame.abyssal_sire;

import rs2.abyssalps.model.npcs.NPC;
import rs2.util.CycledState;

public class AbyssalSireSleep extends CycledState {

	NPC npc;

	AbyssalSireSleep(NPC npc) {
		this.npc = npc;
	}

	@Override
	public boolean process() {
		increaseCycle();
		if (getCycle() == 1) {
			npc.startAnimation(AbyssalSireConstants.SLEEPING_ANIMATION);
		} else if (getCycle() == 5) {
			npc.startAnimation(AbyssalSireConstants.SLEEPING_ANIMATION);
		} else if (getCycle() == 9) {
			npc.startAnimation(AbyssalSireConstants.SLEEPING_ANIMATION);
		} else if (getCycle() == 13) {
			npc.startAnimation(AbyssalSireConstants.WAKE_UP_ANIMATION);
		} else if (getCycle() == 23) {
			npc.walkTo(npc.getX(), npc.getY() - 8);
		}
		return false;
	}

	@Override
	public boolean isFinished() {
		if (!npc.isSleeping()) {
			return true;
		}
		return false;
	}

}
