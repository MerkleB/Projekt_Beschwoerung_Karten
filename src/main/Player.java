package main;

import java.util.UUID;

public interface Player extends HoldingCards, CollectsMagicEnergy{
	/**
	 * Retrieves the GameZone with zoneName
	 * @param zoneName
	 * @return GameZone of name == zoneName
	 */
	public GameZone getGameZone(String zoneName);
	
	/**
	 * Get Players ID
	 * @return UUID
	 */
	public UUID getID();
	
	/**
	 * Current value of SummoningPoints
	 * @return
	 */
	public int getSummoningPoints();
	
	/**
	 * Decreases summoning points by costs
	 * @param costs
	 * @return Remaining summoning points; -1 in case costs are higher than summoning points
	 */
	public int decreaseSummonigPoints(int costs);
	
	/**
	 * Add points to summoning points
	 * @param points
	 * @return New value of summoning points
	 */
	public int addSummoningPoints(int points);
}
