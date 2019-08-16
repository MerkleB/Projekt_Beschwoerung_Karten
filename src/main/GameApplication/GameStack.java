package main.GameApplication;

import java.util.ArrayList;

import main.Action.Stackable;

public class GameStack implements OwnsGameStack {

	private static OwnsGameStack instance;
	
	private ArrayList<Stackable> stack;
	private boolean start;
	private int status;
	
	public static OwnsGameStack getInstance() {
		if(instance == null) {
			instance = new GameStack();
			((GameStack)instance).stack = new ArrayList<Stackable>();
			((GameStack)instance).start = false;
			((GameStack)instance).status = 0;
		}
		return instance;
	}
	
	@Override
	public void run() {
		while(true) {
			while(!start);
			status = 1;
			for(int i=stack.size()-1; i>=0; i--) {
				if(!stack.get(i).isWithdrawn()) {
					stack.get(i).execute();
				}
			}
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
