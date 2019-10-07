package project.main.GameApplication;

import java.util.ArrayList;

import project.main.Action.Stackable;

public class GameStack implements OwnsGameStack {
	
	private ArrayList<Stackable> stack;
	private boolean start;
	private int status;
	
	private GameStack() {
		stack = new ArrayList<Stackable>();
		start = false;
		status = 0;
	}
	
	public static OwnsGameStack getInstance() {
		return new GameStack();
	}
	
	@Override
	public void run() {
		while(status > -1) {
			while(!start);
			status = 1;
			for(int i=stack.size()-1; i>=0; i--) {
				if(!stack.get(i).isWithdrawn()) {
					stack.get(i).execute();
				}
				if(status == -1) {
					break;
				}
			}
			status = -1;
		}
	}

	@Override
	public void addEntry(Stackable entry) {
		stack.add(entry);
	}

	@Override
	public void start() {
		start = true;
	}

	@Override
	public boolean hasStarted() {
		if(status == 1) {
			return true;
		}else return false;
	}

	@Override
	public void finish() {
		status = -1;
		
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

}
