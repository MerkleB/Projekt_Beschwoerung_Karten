package project.main.GameApplication;

import java.util.UUID;

import project.main.Card.Summon;
/**
 * Processes a battle between summons
 * @author D054525
 *
 */
public interface ProcessesBattle{
	public static final String ENDED = "ended";
	public static final String ABRUPT = "abrupt";
	public static final String RUNNING = "running";
	public static final String INIT = "initial";
	
	/**
	 * In some points of the process the battle waits for the player if the battle can proceed.
	 * This method ensures that the battle proceeds if the right player(s) declared to proceed.
	 * @param player
	 */
	public void proceed(Player player);
	/**
	 * Defines which cards will fight in the battle
	 * @param Summon attacker
	 * @param Summon defender
	 */
	public void setCombatants(Summon attacker, Summon defender);
	/**
	 * Starts the battle. The combatants must be set beforehand.
	 */
	public void start();
	/**
	 * Stops the battle and interrupt the processing.
	 */
	public void end();
	/**
	 * Removes one of the combatant. This ends a battle. Summons which flee from battle are set to immobilized.
	 * This status is set until end phase of owners next move 
	 * @param UUID summonID
	 */
	public void remove(UUID summonID);
	/**
	 * Retrieves the winning summon
	 * @return Summon winner
	 */
	public Summon getWinner();
	/**
	 * Retrieves the loosing summon
	 * @return Summon looser
	 */
	public Summon getLooser();
	/**
	 * Retrieves the status of the game
	 * The values are represented by constants in interface ProcessesBattle
	 * @return ENDED, ABRUPT, RUNNING, INI
	 */
	public String getStatus();
}
