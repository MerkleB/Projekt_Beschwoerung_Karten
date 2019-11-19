package project.main.GameApplication;

public interface TimerListener {
	public void timerStarted(CountsTimeUntilProceed timer);
	public void timerCountedDown(CountsTimeUntilProceed timer);
	public void timerEnded(CountsTimeUntilProceed timer);
}
