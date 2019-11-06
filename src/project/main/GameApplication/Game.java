package project.main.GameApplication;

import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import project.main.jsonObjects.MessageInLanguage;
import project.main.util.MapsRankAndLevel;

public interface Game extends Runnable{
	@Override
	/**
	 * {@inheritDoc}
	 * Starts the game and the processing of the phases
	 */
	public void run();
	/**
	 * Retrieves the mapper of Ranks and level
	 * @return MapsRankAndLevel mapper
	 */
	public MapsRankAndLevel getRankMapper();
	/**
	 * Retrieves the player which is currently active
	 * @return Player activePlayer
	 */
	public Player getActivePlayer();
	/**
	 * Retrieves both players
	 * @return Player[2]
	 */
	public Player[] getPlayers();
	/**
	 * Retrieves a decent player
	 * @param id : Id of the player
	 * @return Player : Player of given id
	 */
	public Player getPlayer(UUID id);
	/**
	 * Retrieves the other player
	 * @param Player player
	 * @return Player which is the other player from players view
	 */
	public Player getOtherPlayer(Player player);
	/**
	 * Retrieves the currently active game phase
	 * @return IsPhaseInGame activePhase
	 */
	public IsPhaseInGame getActivePhase();
	/**
	 * Retrieves the information if the game has ended
	 * @return true if the game finished; false otherwise
	 */
	public boolean hasEnded();
	/**
	 * Retrieves the information if the game has started
	 * @return true if the game started; false otherwise
	 */
	public boolean hasStarted();
	/**
	 * Player gives the permission to the game to proceed
	 * This ends the current phase
	 * @param player
	 * @return true if the game proceeds; false if it is still waiting
	 */
	public boolean proceed(Player player);
	/**
	 * Player tells the game that he want to procees the GameStack
	 * @param player
	 * @return true if the stack is processed; false otherwise
	 */
	public boolean processGameStack(Player player);
	/**
	 * Forbids the game to process the stack until the player wants to proceed
	 * Only possible if the Player is relevant for proceed 
	 * @param Player player
	 */
	public void forbidGameStackProcessing(Player player);
	/**
	 * Retrieves if the player is relevant for proceed. This means is able to do anything.
	 * Note: Active player is always relevant
	 * @param player
	 * @return true if the player has any activateable stackables or is active player; false otherwise
	 */
	public boolean playerIsRelevantForProceed(Player player);
	/**
	 * Retrieves the active battle instance
	 * @return {@link ProcessesBattle}; null if there is no active battle
	 */
	public ProcessesBattle getActiveBattle();
	/**
	 * Sets a battle to be active battle
	 * @param ProcessesBattle battle
	 */
	public void setActiveBattle(ProcessesBattle battle);
	/**
	 * Prompts the player to give an answer.
	 * Gives the answer directly to the prompter
	 * @param promptedPlayer : {@link Player} who was prompted
	 * @param message : Prompt-Message ({@link MessageInLanguage}) 
	 * @param prompter : Prompter which directly {@link AcceptPromptAnswers}
	 */
	public void prompt(Player promptedPlayer, MessageInLanguage message, AcceptPromptAnswers prompter);
	/**
	 * Prompts the player to give an answer
	 * The answer is retrieved indirectly by an action the player activated
	 * @param promptedPlayer : {@link Player} who was prompted
	 * @param message : Prompt-Message ({@link MessageInLanguage}) 
	 */
	public void prompt(Player promptedPlayer, MessageInLanguage message);
	/**
	 * Retrieves the lock object
	 * @return ReentrantLock lock
	 */
	public ReentrantLock getLock();
	/**
	 * Retrieves the condition object
	 * @return Condition condition
	 */
	public Condition getCondition();
}
