package rs2.util.tools.event.impl;

import rs2.util.tools.account.BackupManager;
import rs2.util.tools.event.CycleEvent;
import rs2.util.tools.event.CycleEventContainer;

public class BackupEvent extends CycleEvent {

	@Override
	public void execute(CycleEventContainer container) {
		new Thread(new BackupManager()).start();
	}

}
