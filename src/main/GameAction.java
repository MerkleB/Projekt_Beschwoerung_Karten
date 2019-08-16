package main;

import main.exception.NotActivableException;

public interface GameAction extends Stackable {
	/**
	 * Activate the GameAction by a stackable. Set activ by must be called beforehand.
	 * ATTENTION: This oversteers the isActivable-Method 
	 * @param stackable
	 * @param activatingPlayer
	 * @throws NotActivableException: Thrown if stackable is not actions activator or activatingPlayer is not actions activFor 
	 */
	public void activateBy(Stackable stackable, Player activatingPlayer) throws NotActivableException;
	/**
	 * Sets the GameAction activ for a decent stackable
	 * @param stackable
	 * @param player
	 */
	public void setActivBy(Stackable stackable, Player player);
	public Stackable getActivatingStackable();
}
