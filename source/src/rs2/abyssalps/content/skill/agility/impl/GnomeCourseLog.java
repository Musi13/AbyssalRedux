package rs2.abyssalps.content.skill.agility.impl;

import rs2.abyssalps.content.skill.agility.AgilityAssistant;
import rs2.abyssalps.content.skill.agility.AgilityConstants;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.player.Client;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class GnomeCourseLog extends AgilityAssistant {

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
			int timer = 8;

			@Override
			public void execute(CycleEventContainer container) {
				if (timer > 0) {
					player.getPA().walkTo(0, -1);
				} else {
					player.getGnomeCourseDone()[0] = true;
					AgilityAssistant.end(player, new Position(2474, 3429, 0));
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
		return new String[] { "You carefully cross the slippery log...",
				"...And make it safetly to the other side." };
	}

}
