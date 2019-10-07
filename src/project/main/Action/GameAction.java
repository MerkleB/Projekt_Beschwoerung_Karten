package project.main.Action;

import project.main.GameApplication.Player;
import project.main.exception.NotActivableException;

/**
 * Represents a action in the game
 * An action is a stackable which means it will be exectuted by the stack
 * Also an action can be activated by an other stackable oversteering the activatable check 
 * @author D054525
 *
 */
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
	 * Only activ actions can be activated
	 * @param stackable
	 * @param player
	 */
	public void setActivBy(Stackable stackable, Player player);
	/**
	 * Retrieves the stackable the action was activated from.
	 * @return Stackable activator
	 */
	public Stackable getActivatingStackable();
}
