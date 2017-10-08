package rs2.abyssalps.content.skill.agility.impl;

import rs2.abyssalps.content.skill.agility.AgilityAssistant;
import rs2.abyssalps.content.skill.agility.AgilityConstants;
import rs2.abyssalps.model.Position;
import rs2.abyssalps.model.player.Client;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;
import rs2.util.tools.event.CycleEventHandler;

public class GnomeCourseObstaclePipe extends AgilityAssistant {

	@Override
	public void execute(Client player) {
		player.setRun(false);
		player.setCanWalk(false);
		AgilityConstants.setPlayerAnimationIndex(player, animations()[0],
				animations()[0], animations()[0]);
		player.getPA().walkTo(0, 2);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			int timer = 10;

			@Override
			public void execute(CycleEventContainer container) {
				if (timer > 5 && timer <= 8) {

					player.getCombat().getPlayerAnimIndex();
					player.setAppearanceUpdateRequired(true);
					player.updateRequired = true;

					player.getPA().walkTo(0, 1);
				} else if (timer > 2 && timer <= 4) {
					AgilityConstants.setPlayerAnimationIndex(player,
							animations()[1], animations()[1], animations()[1]);
					player.getPA().walkTo(0, 1);
				} else if (timer >= 1 && timer < 3) {
					player.getGnomeCourseDone()[5] = true;
					AgilityAssistant.end(player, new Position(player.getX(),
							player.getY(), 0));
					container.stop();
				}
				timer--;

			}

		}, 1);
	}

	@Override
	public int[] animations() {
		// TODO Auto-generated method stub
		return new int[] { 749, 748 };
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
		return new String[] { "You squeeze into the pipe...",
				"...You squeeze out of the pipe." };
	}

}
