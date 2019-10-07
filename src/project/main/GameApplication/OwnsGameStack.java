package project.main.GameApplication;

import java.util.ArrayList;

import project.main.Action.Stackable;

/**
 * Represents an object in game which owns an controls the stack from where stackables are executed.
 * Each object owning a GameStack runs in a thread but the stack entries will only be executed once is was started 
 * @author D054525
 *
 */
public interface OwnsGameStack extends Runnable {
	/**
	 * Adds an stackable to the stack
	 * @param entry
	 */
	public void addEntry(Stackable entry);
	/**
	 * Starts the execution of the stack
	 * The last added entry in the stack will be executed first
	 */
	public void start();
	/**
	 * Finishes the execution of the stack
	 */
	public void finish();
	/**
	 * Retrieves if the execution of the stack has started
	 * @return status = 1 : true (boolean)
	 */
	public boolean hasStarted();
	/**
	 * Retrieves the stackable which will be executed first 
	 * @return Stackable
	 */
	public Stackable getFirstProcessingEntry();
	/**
	 * Retrieves if the execution of the stack finished
	 * @return status = -1 : true (boolean)
	 */
	public boolean hasFinished();
	/**
	 * Retrieves all entries of the stack
	 * @return ArrayList<Stackable>
	 */
	public ArrayList<Stackable> getEntries();
	/**
	 * Retrieves the entry of the stack of a certain index
	 * @param int i (Index)
	 * @return Stackable
	 */
	public Stackable getEntry(int i);
}
