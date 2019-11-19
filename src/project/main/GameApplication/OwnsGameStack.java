package project.main.GameApplication;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import project.main.Action.Stackable;

/**
 * Represents an object in game which owns an controls the stack from where stackables are executed.
 * Each object owning a GameStack runs in a thread but the stack entries will only be executed once is was started 
 * @author D054525
 *
 */
public interface OwnsGameStack extends Runnable{
	/**
	 * Processes the stack. 
	 * Gives a Signal to all instances which are waiting for a signal of stacks condition object.
	 */
	@Override
	public void run();
	/**
	 * Adds an stackable to the stack
	 * @param entry
	 */
	public void addEntry(Stackable entry);
	
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
	/**
	 * Retrieves the status of the stack.
	 * @return 0=initial; 1=started; 2=wait until start; -1=finished
	 */
	public int getStatus();
	/**
	 * Retrieves the Lock-Object associated with the stack
	 * @return ReentrantLock
	 */
	public ReentrantLock getLock();
	/**
	 * Retrieves the condition object of the stack
	 * @return Condition
	 */
	public Condition getCondition();
}
