package main.GameApplication;

import java.util.ArrayList;

import main.Action.Stackable;

public interface OwnsGameStack extends Runnable {
	public void addEntry(Stackable entry);
	public void start();
	public boolean hasStarted();
	public Stackable getFirstProcessingEntry();
	public boolean hasFinished();
	public ArrayList<Stackable> getEntries();
	public Stackable getEntry(int i);
}
