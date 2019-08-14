package main;

import java.util.ArrayList;

public interface OwnsGameStack extends Runnable {
	public void addEntry(Stackable entry);
	public void start();
	public boolean hasStarted();
	public Stackable getFirstProcessingEntry();
	public boolean hasFinished();
	public ArrayList<Stackable> getEntries();
	public Stackable getEntry(int i);
}
