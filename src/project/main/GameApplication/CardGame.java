package project.main.GameApplication;

import java.util.UUID;

import project.main.jsonObjects.MessageInLanguage;
import project.main.util.RankLevelMapper;
import project.main.util.MapsRankAndLevel;

public class CardGame implements Game {
	
	private static Game instance;
	private MapsRankAndLevel rankAndLevelMapper; 
	
	public static Game getInstance() {
		if(instance == null) {
			instance = new CardGame();
			((CardGame)instance).rankAndLevelMapper = RankLevelMapper.getInstance();
		}
		return instance;
	}
	
	@Override
	public MapsRankAndLevel getRankMapper() {
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

	@Override
	public boolean hasEnded() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean processGameStack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ProcessesBattle getActiveBattle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setActiveBattle(ProcessesBattle battle) {
		// TODO Auto-generated method stub
		
	}

}
