package project.main.GameApplication;

/**
 * Is able to prompt and receive an answer from decent a player 
 * @author D054525
 *
 */
public interface AcceptPromptAnswers {
	/**
	 * Called by the instance where the prompt is handled to handover the answer of the prompted player.
	 * @param answer
	 */
	public void accept(String answer);
}
