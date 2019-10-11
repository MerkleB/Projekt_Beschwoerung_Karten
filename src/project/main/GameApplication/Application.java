package project.main.GameApplication;

import project.main.Listeners.GameListener;
import project.main.Listeners.ListensToEverything;
import project.main.jsonObjects.ActionDefinitionLibrary;
import project.main.jsonObjects.CardDefinitionLibrary;
import project.main.jsonObjects.HoldsActionDefinitions;
import project.main.jsonObjects.HoldsCardDefinitions;
import project.main.util.RankLevelMapper;
import project.main.util.mapsRankAndLevel;

public class Application implements HostsGame {

	
	private static HostsGame instance;
	private Game game;
	private String language; 
	private ListensToEverything gameListener;
	private HoldsCardDefinitions cardLibrary;
	private HoldsActionDefinitions actionLibrary;
	private mapsRankAndLevel mapper;
	
	/**
	 * Get instance and set Game if not null
	 * @return instance
	 */
	public static HostsGame getInstance() {
		if(instance == null) {
			instance = new Application();
			((Application)instance).gameListener = GameListener.getInstance();
			((Application)instance).cardLibrary = CardDefinitionLibrary.getInstance();
			((Application)instance).actionLibrary = ActionDefinitionLibrary.getInstance();
			((Application)instance).mapper = RankLevelMapper.getInstance();
		}
		return instance;
	}
	
	@Override
	public void setGame(Game game) {
		if(game != null) {
			this.game = game;
		}
	}
	
	@Override
	public Game getGame() {
		return game;
	}

	@Override
	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String getLanguage() {
		return this.language;
	}

	@Override
	public ListensToEverything getGameListener() {
		return gameListener;
	}

	@Override
	public HoldsCardDefinitions getCardLibrary() {
		return cardLibrary;
	}

	@Override
	public HoldsActionDefinitions getActionLibrary() {
		return actionLibrary;
	}

	@Override
	public mapsRankAndLevel getRankAndLevelMapper() {
		return mapper;
	}

}
