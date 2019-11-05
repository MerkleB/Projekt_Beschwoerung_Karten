package project.main.GameApplication;

import java.util.ArrayList;
import java.util.UUID;

import project.main.Card.CollectsMagicEnergy;

public interface Player{
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
	
	/**
	 * Retrieves the current health points
	 * @return Health points
	 */
	public int getHealthPoints();
	
	/**
	 * Heals the player 
	 * @param heal
	 * @return new value of health points
	 */
	public int increaseHealthPoints(int heal);
	
	/**
	 * Put damage to player
	 * @param damage
	 * @return new value of healt points
	 */
	public int decreaseHealthPoints(int damage);
	
	/**
	 * Retrieves the number of collector actions which can be done 
	 * @return int
	 */
	public int getNumberOfRemainingCollectorActions();
	
	/**
	 * Increases the number of possible collector actions by the given value
	 * @param int actions
	 */
	public void increaseNumberOfRemainingCollectorActions(int actions);
	
	/**
	 * Decreases the number of remaining collector actions
	 * @param int actions
	 */
	public void decreaseNumberOfRemainingCollectorActions(int actions);
	/**
	 * Returns the magic energy stock
	 * @return CollectsMagicEnergy 
	 */
	public CollectsMagicEnergy getMagicEnergyStock();
	/**
	 * Sets the game
	 * @param game
	 */
	public void setGame(Game game);
	/**
	 * Retrieves the game
	 * @return game
	 */
	public Game getGame();
	/**
	 * Returns the controller associated with the player
	 * @return ControlsStackables controller
	 */
	public ControlsStackables getController();
}
