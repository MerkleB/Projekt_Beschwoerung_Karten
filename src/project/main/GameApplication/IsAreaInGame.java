package project.main.GameApplication;

public interface IsAreaInGame extends HoldingCards {
	/**
	 * Activates actions for a given player and phase
	 * @param player
	 * @param gamePhase
	 */
	public void activate(Player player, IsPhaseInGame gamePhase);
	
	/**
	 * Deactivates all actions of cards which are stored in this zone;
	 */
	public void deavtivateAll();
	
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
