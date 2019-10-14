package project.main.GameApplication;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import project.main.Action.Stackable;

public class GameStack implements OwnsGameStack {
	
	private ArrayList<Stackable> stack;
	private int status;
	private IsPhaseInGame phase;
	private ReentrantLock lock;
	private Condition condition;
	
	private GameStack(IsPhaseInGame phase) {
		stack = new ArrayList<Stackable>();
		status = 0;
		lock = new ReentrantLock();
		condition = lock.newCondition();
		this.phase = phase;
	}
	
	public static OwnsGameStack getInstance(IsPhaseInGame phase) {
		return new GameStack(phase);
	}

	@Override
	public ReentrantLock getLock() {
		return lock;
	}

	@Override
	public Condition getCondition() {
		return condition;
	}

	@Override
	public void run() {
		System.out.println("Stack start processing...");
		status = 1;
		for(int i=stack.size()-1; i>=0; i--) { 	
			stack.get(i).execute();
		}
		System.out.println("Stack finished Processing.");
		phase.restorePhaseStatus();
		lock.lock();
		try {
			condition.signalAll();
		}finally {
			lock.unlock();
			status = -1;
		}
	}

	@Override
	public void addEntry(Stackable entry) {
		stack.add(entry);
	}

	@Override
	public boolean hasStarted() {
		if(status == 1) {
			return true;
		}else return false;
	}

	@Override
	public Stackable getFirstProcessingEntry() {
		return stack.get(stack.size()-1);
	}

	@Override
	public boolean hasFinished() {
		if(status == -1) {
			return true;
		}else return false;
	}

	@Override
	public ArrayList<Stackable> getEntries() {
		return stack;
	}

	@Override
	public Stackable getEntry(int i) {
		return stack.get(i);
	}

	@Override
	public int getStatus() {
		return status;
	}

}
