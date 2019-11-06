package project.main.GameApplication;

import project.main.Action.Stackable;
import project.main.jsonObjects.MessageInLanguage;

public interface ControlsStackables {
	/**
	 * Informs the controller that the given stackable was set activ
	 * @param stackable
	 */
	public void stackableWasSetActive(Stackable stackable);
	/**
	 * Informs the controller that the given stackable was set inactiv
	 * @param stackable
	 */
	public void stackableWasSetInactive(Stackable stackable);
	/**
	 * Executes the given command.
	 * Format: [Stackable-Type]:[Stackable-Code]@[Player={PlayerId}]-Zone={ZoneName}-Card={CardId}]
	 * E.g. Action:EvokeSummon@Player=APlayerId-Zone=HandZone-Card=ASummonID123
	 * or Action:Draw@DeckZone
	 * or Action:Surrender@Player=APlayerID
	 * @param command
	 */
	public void executeCommand(String command);
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
}
