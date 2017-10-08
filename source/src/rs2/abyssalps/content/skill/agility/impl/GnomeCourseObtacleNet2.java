package rs2.abyssalps.content.skill.agility.impl;

import rs2.abyssalps.content.skill.agility.AgilityAssistant;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.player.Client;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class GnomeCourseObtacleNet2 extends AgilityAssistant {

	@Override
	public void execute(Client player) {
		if (player.getY() > player.objectY) {
			return;
		}
		player.startAnimation(animations()[0]);

		if (messages()[0] != null) {
			player.sendMessage(messages()[0]);
		}

		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getGnomeCourseDone()[4] = true;
				AgilityAssistant.end(player,
						new Position(player.getX(), player.getY() + 1, 0));

				container.stop();
			}

		}, 2);
	}

	@Override
	public int[] animations() {
		// TODO Auto-generated method stub
		return new int[] { 828 };
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
		return new String[] { "You climb the netting...", null };
	}

}
