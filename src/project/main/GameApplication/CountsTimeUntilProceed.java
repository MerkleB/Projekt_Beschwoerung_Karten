package project.main.GameApplication;

public interface CountsTimeUntilProceed extends Runnable {
	public static final int CREATED = 0;
	public static final int STARTED = 1;
	public static final int ENDED = 2;
	
	/**
	 * Retrieves the remaining time of the timer in ms
	 * @return int (ms)
	 */
	public int getRemainingTime();
	/**
	 * Retrieves the time which the timer was initialized with in ms
	 * @return int (ms)
	 */
	public int getStartTimer();
	/**
	 * Retrieves the time in ms the timer waits before it starts 
	 * @return int (ms)
	 */
	public int getWaitingTimeBeforeStart();
	/**
	 * <p>Retrieves the status of the time</p>
	 * <p>0 = CREATED</p>
	 * <p>1 = STARTED</p>
	 * <p>2 = ENDED</p>
	 * @return int status
	 */
	public int getStatus();
	/**
	 * Adds a listener to the timer
	 * @param listener
	 */
	public void addTimerListener(TimerListener listener);
}
