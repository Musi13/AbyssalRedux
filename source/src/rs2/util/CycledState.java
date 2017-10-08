package rs2.util;

public abstract class CycledState {

	private Object state;
	private int cycle;
	
	public abstract boolean process();

	public abstract boolean isFinished();
	
	public void resetState(Object state) {
		setCycle(0);
		setState(state);
	}
	
	public void setState(Object state) {
		this.state = state;
	}

	public void increaseCycle() {
		cycle++;
	}
	
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

	public Object getState() {
		return state;
	}

	public int getCycle() {
		return cycle;
	}
	
}