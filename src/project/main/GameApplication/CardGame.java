package project.main.GameApplication;

import java.util.UUID;

import project.main.util.MessageInLanguage;
import project.main.util.RankLevelMapper;
import project.main.util.mapsRankAndLevel;

public class CardGame implements Game {
	
	private static Game instance;
	private mapsRankAndLevel rankAndLevelMapper; 
	
	public static Game getInstance() {
		if(instance == null) {
			instance = new CardGame();
			((CardGame)instance).rankAndLevelMapper = RankLevelMapper.getInstance();
		}
		return instance;
	}
	
	@Override
	public mapsRankAndLevel getRankMapper() {
		return rankAndLevelMapper;
	}

	@Override
	public Player getActivePlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player[] getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void prompt(Player promptedPlayer, MessageInLanguage message, AcceptPromptAnswers prompter) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void prompt(Player promptedPlayer, MessageInLanguage message) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Player getPlayer(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public IsPhaseInGame getActivePhase() {
		// TODO Auto-generated method stub
		return null;
	}

}
