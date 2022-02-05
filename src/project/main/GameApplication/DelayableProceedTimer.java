package project.main.GameApplication;

import java.util.ArrayList;

public class DelayableProceedTimer implements CountsTimeUntilProceed {
	
	private int remainingTime;
	private int initialTime;
	private int delayTime;
	private int status;
	private ArrayList<TimerListener> listeners;
	
	/**
	 * @param timeToRun : Time in seconds
	 * @param delayTime : Time in seconds
	 */
	public DelayableProceedTimer(int timeToRun, int delayTime) {
		initialTime = timeToRun*1000;
		remainingTime = timeToRun*1000;
		this.delayTime = delayTime*1000;
		status = 0;
		listeners = new ArrayList<TimerListener>();
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(delayTime);
			timerStarted(this);
			while(remainingTime > 0) {
				if(Thread.currentThread().isInterrupted()) {
					throw new InterruptedException("Timer was interrupted.");
				}
				Thread.sleep(1000);
				remainingTime = remainingTime-1000;
				timerCountedDown(this);
			}
			timerEnded(this);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} finally {
			status = ENDED;
		}
		
		
	}

	@Override
	public int getRemainingTime() {
		return remainingTime;
	}

	@Override
	public int getStartTimer() {
		return initialTime;
	}

	@Override
	public int getWaitingTimeBeforeStart() {
		return delayTime;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void addTimerListener(TimerListener listener) {
		listeners.add(listener);
	}

	private void timerStarted(CountsTimeUntilProceed timer) {
		for(TimerListener listener : listeners) {
			listener.timerStarted(timer);
		}
	}

	private void timerCountedDown(CountsTimeUntilProceed timer) {
		for(TimerListener listener : listeners) {
			listener.timerCountedDown(timer);
		}
	}

	private void timerEnded(CountsTimeUntilProceed timer) {
		for(TimerListener listener : listeners) {
			listener.timerEnded(timer);
		}	
	}

}
