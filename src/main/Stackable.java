package main;

import java.util.Hashtable;

public interface Stackable{
	public String getName();
	public void activate();
	public boolean activatable();
	public void execute();
	public void withdraw();
	public void setActiv();
	public void setInactiv();
	public Card getCard();
	public void setCard(Card card);
	/**
	 * Retrieves the metadata of the effect or action
	 * @return
	 */
	public Hashtable<String, String> getMetaData();
}
