package project.main.GameApplication;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import project.main.Action.GameAction;
import project.main.Action.Stackable;
import project.main.Card.Card;

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
		if(status == -1) return; //Already finished stacks can't be executed.
		System.out.println("Stack start processing...");
		deactivateAllOtherStackables();
		status = 1;
		for(int i=stack.size()-1; i>=0; i--) { 	
			Stackable currentEntry = stack.get(i);
			currentEntry.execute();
			currentEntry.setInactiv();
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
	
	private void deactivateAllOtherStackables() {
		System.out.println("Deactivate stackables which are not in stack.");
		Game game = phase.getGame();
		Player[] players = game.getPlayers();
		for(Player player : players) {
			ArrayList<IsAreaInGame> zones = player.getGameZones();
			for(IsAreaInGame zone : zones) {
				zone.deavtivateAll(stack);
			}
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
