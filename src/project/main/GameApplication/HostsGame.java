package project.main.GameApplication;

import project.main.Listeners.ListensToEverything;
import project.main.jsonObjects.HoldsActionDefinitions;
import project.main.jsonObjects.HoldsCardDefinitions;
import project.main.util.MapsRankAndLevel;

public interface HostsGame {
	public void setGame(Game game);
	public void setLanguage(String language);
	public String getLanguage();
	public Game getGame();
	public ListensToEverything getGameListener();
	public HoldsCardDefinitions getCardLibrary();
	public HoldsActionDefinitions getActionLibrary();
	public MapsRankAndLevel getRankAndLevelMapper();
}
