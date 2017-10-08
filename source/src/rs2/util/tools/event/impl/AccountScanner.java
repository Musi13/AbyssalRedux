package rs2.util.tools.event.impl;

import rs2.util.tools.dupe.DupeDetection;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;

public class AccountScanner extends CycleEvent {

	@Override
	public void execute(CycleEventContainer container) {
		DupeDetection.scanPlayers();
	}

}
