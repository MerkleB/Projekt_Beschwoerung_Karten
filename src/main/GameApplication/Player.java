package main.GameApplication;

import java.util.ArrayList;
import java.util.UUID;

import main.Card.CollectsMagicEnergy;

public interface Player extends CollectsMagicEnergy{
	/**
	 * Retrieves the GameZone with zoneName
	 * @param zoneName
	 * @return GameZone of name == zoneName
	 */
	public IsAreaInGame getGameZone(String zoneName);
	
	/**
	 * Retrieves all game zones
	 * @return list of game zones
	 */
	public ArrayList<IsAreaInGame> getGameZones();
	
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
