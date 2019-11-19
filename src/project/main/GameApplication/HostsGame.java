package project.main.GameApplication;

public interface HostsGame {
	public void setGame(Game game);
	public void setLanguage(String language);
	public String getLanguage();
	public Game getGame();
	/**
	 * Starts the game.
	 * Only possible if Language and Game are set.
	 */
	public void startGame();
}
