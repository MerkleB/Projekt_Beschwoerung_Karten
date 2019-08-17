package main.GameApplication;

import java.util.UUID;

import main.util.RankLevelMapper;
import main.util.mapsRankAndLevel;

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
	public void prompt(Player promptedPlayer, String message) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Player getPlayer(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}