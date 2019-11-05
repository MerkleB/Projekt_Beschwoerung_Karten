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
	/**
	 * Executes the given command.
	 * Format: [Stackable-Type]:[Stackable-Code]@[PathPart1-PathPart2]
	 * E.g. Action:EvokeSummon@Player=APlayerIdHandZone-Card=ASummonID123
	 * or Action:Draw@DeckZone
	 * or Action:Surrender@Player=APlayerID
	 * @param command
	 */
	public void executeCommand(String command);
}
