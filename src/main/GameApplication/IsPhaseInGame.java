package main.GameApplication;

import java.util.ArrayList;

public interface IsPhaseInGame extends GameObject{
	/**
	 * Retrieves the name of the Phase
	 * @return String
	 */
	public String getName();
	/**
	 * Sets back the status of all actions with regard to this phase
	 */
	public void restorePhaseStatus();
	/**
	 * Retrieves which actions are set activ bey this phase
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getActionsToActivate();
	/**
	 * Start the phase
	 */
	public void process();
	/**
	 * Leaves the phase and deactivates all action also finishes all stack executions
	 */
	public void leave();
	/**
	 * Retrieves the active game stack
	 * @return OwnsGameStack
	 */
	public OwnsGameStack getActiveGameStack();
	/**
	 * Retrieves all stacks which where processed
	 * @return ArrayList<OwnsGameStack>
	 */
	public ArrayList<OwnsGameStack> getFinisheGameStacks();
}
