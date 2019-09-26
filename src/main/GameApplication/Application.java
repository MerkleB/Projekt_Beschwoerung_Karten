package main.GameApplication;

import main.Listeners.GameListener;
import main.Listeners.ListensToEverything;
import main.jsonObjects.ActionDefinitionLibrary;
import main.jsonObjects.CardDefinitionLibrary;
import main.jsonObjects.HoldsActionDefinitions;
import main.jsonObjects.HoldsCardDefinitions;
import main.util.RankLevelMapper;
import main.util.mapsRankAndLevel;

public class Application implements HostsGame {

	
	private static HostsGame instance;
	private Game game;
	private ListensToEverything gameListener;
	private HoldsCardDefinitions cardLibrary;
	private HoldsActionDefinitions actionLibrary;
	private mapsRankAndLevel mapper;
	
	/**
	 * Get instance and set Game if not null
	 * @param game [optional]
	 * @return
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
