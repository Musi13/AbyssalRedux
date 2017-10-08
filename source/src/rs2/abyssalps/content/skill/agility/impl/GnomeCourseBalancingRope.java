package rs2.abyssalps.content.skill.agility.impl;

import rs2.abyssalps.content.skill.agility.AgilityAssistant;
import rs2.abyssalps.content.skill.agility.AgilityConstants;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.player.Client;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class GnomeCourseBalancingRope extends AgilityAssistant {

	@Override
	public void execute(Client player) {

		if (messages()[0] != null) {
			player.sendMessage(messages()[0]);
		}

		player.setRun(false);
		player.setCanWalk(false);
		AgilityConstants.setPlayerAnimationIndex(player, animations()[0],
				animations()[0], animations()[0]);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			int timer = 6;

			@Override
			public void execute(CycleEventContainer container) {
				if (timer > 0) {
					player.getPA().walkTo(5, 0);
				} else {
					player.getGnomeCourseDone()[3] = true;
					AgilityAssistant.end(player, new Position(2483, 3420, 2));
					container.stop();
				}
				timer--;
			}

		}, 1);
	}

	@Override
	public int[] animations() {
		// TODO Auto-generated method stub
		return new int[] { 762 };
	}

	@Override
	public int levelRequired() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int experience() {
		// TODO Auto-generated method stub
		return (int) 7.5;
	}

	@Override
	public String[] messages() {
		// TODO Auto-generated method stub
		return new String[] { null, null };
	}

}
