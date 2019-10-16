package project.main.Action;

import java.util.Hashtable;

import project.main.Card.Card;
import project.main.GameApplication.GameObject;
import project.main.GameApplication.Player;
import project.main.exception.NotActivableException;
/**
 * Represents a Entry in an Stack owned by a OwnsGameStack object
 * A stackable will be executed by the stack if it was activated and therefore added to the game stack 
 * @author D054525
 *
 */
public interface Stackable extends GameObject{
	/**
	 * Retrieves the code of a stackable
	 * @return
	 */
	public String getCode();
	/**
	 * Retrieves the name of a stackable applications language
	 * @return String name
	 */
	public String getName();
	/**
	 * Activates the stackable and put it to the stack
	 * A Stackable can only be activated if it is activatable for the activator 
	 * @param activator
	 * @throws NotActivableException
	 */
	public void activate(Player activator) throws NotActivableException;
	/**
	 * Retrieves the player which activated the action
	 * @return Player activator; null if the action is not activated
	 */
	public Player getActivator();
	/**
	 * Retrieves if a stackable ist activatable for the given activator.
	 * A stackable is only activatable if it is activ for the activator 
	 * and maybe if certain specific requierements are fullfilled.
	 * Sets the value of "activated" to true
	 * @param activator
	 * @return
	 */
	public boolean activateable(Player activator);
	/**
	 * Executes the stackable this is normally done by a stack object
	 * A stackable can only be executed if it is activated and not withdrawn 
	 */
	public void execute();
	/**
	 * Sets the status field "withdrawn" to true
	 */
	public void withdraw();
	/**
	 * Retrieves the value of status field "withdrawn" 
	 * @return boolean withdrawn
	 */
	public boolean isWithdrawn();
	/**
	 * Sets the status of the Stackable as "activ" for a specific player
	 * only activ Stackables can be activated
	 * @param player: which can activate the stackable
	 */
	public void setActiv(Player activFor);
	/**
	 * Sets the status field "activ" to false
	 */
	public void setInactiv();
	/**
	 * Retrieves the card the stackable belongs to
	 * @return Card owningCard
	 */
	public Card getCard();
	/**
	 * Sets the card the stackable belongs to
	 * @param card
	 */
	public void setCard(Card card);
	/**
	 * Retrieves the metadata of the stackable
	 * @return Hashtable<String, String> metadata (key, value)
	 */
	public Hashtable<String, String> getMetaData();
}
