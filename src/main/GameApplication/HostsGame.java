package main.GameApplication;

import main.Listeners.ListensToEverything;
import main.jsonObjects.HoldsActionDefinitions;
import main.jsonObjects.HoldsCardDefinitions;
import main.util.mapsRankAndLevel;

public interface HostsGame {
	public void setGame(Game game);
	public Game getGame();
	public ListensToEverything getGameListener();
	public HoldsCardDefinitions getCardLibrary();
	public HoldsActionDefinitions getActionLibrary();
	public mapsRankAndLevel getRankAndLevelMapper();
}
