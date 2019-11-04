package project.main.GameApplication;

import project.main.Action.Stackable;

public interface ControlsStackables {
	/**
	 * Informs the controller that the given stackable was set activ
	 * @param stackable
	 */
	public void stackableWasSetActive(Stackable stackable);
	/**
	 * Informs the controller that the given stackable was set inactiv
	 * @param stackable
	 */
	public void stackableWasSetInactive(Stackable stackable);
}
