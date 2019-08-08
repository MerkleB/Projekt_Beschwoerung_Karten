package main;

import main.util.RankLevelMapper;
import main.util.mapsRankAndLevel;

public class CardGame implements Game {
	
	private static CardGame instance;
	private mapsRankAndLevel rankAndLevelMapper; 
	
	public static Game getInstance() {
		if(instance == null) {
			instance = new CardGame();
			instance.rankAndLevelMapper = RankLevelMapper.getInstance();
		}
		return instance;
	}
	
	@Override
	public mapsRankAndLevel getRankMapper() {
		return rankAndLevelMapper;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
