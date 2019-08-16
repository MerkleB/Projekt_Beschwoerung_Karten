package main.Action;

import java.util.Hashtable;

import main.Card.Card;
import main.GameApplication.Player;
import main.exception.NotActivableException;

public interface Stackable{
	public String getName();
	public void activate(Player activator) throws NotActivableException;
	public boolean activateable(Player activator);
	public void execute();
	public void withdraw();
	public boolean isWithdrawn();
	/**
	 * Enables the Stackable to be activated
	 * @param player: which can activate the stackable
	 */
	public void setActiv(Player activFor);
	public void setInactiv();
	public Card getCard();
	public void setCard(Card card);
	/**
	 * Retrieves the metadata of the effect or action
	 * @return
	 */
	public Hashtable<String, String> getMetaData();
}
