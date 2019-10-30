package project.main.GameApplication;

import java.util.ArrayList;

import project.main.Action.Stackable;

public interface IsAreaInGame extends HoldingCards {
	/**
	 * Activates actions for a given player and phase
	 * @param player
	 * @param gamePhase
	 */
	public void activate(Player player, IsPhaseInGame gamePhase);
	
	/**
	 * Deactivates all stackables of cards which are stored in this zone;
	 */
	public void deavtivateAll();
	/**
	 * Deactivates all stackables of cards which are stored in this zone except they are in the exception list
	 * @param exeptionList
	 */
	public void deavtivateAll(ArrayList<Stackable> exeptionList);
	/**
	 * Retrieves the owning player of the zone
	 * @return Player owner
	 */
	public Player getOwner();
	/**
	 * Retrieves the name of the area
	 * @return String name
	 */
	public String getName();
}
