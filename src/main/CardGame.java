package main;

import main.Listeners.EffectListener;
import main.Listeners.GameActionListener;
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

	@Override
	public Card findCard(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Summon findSummon(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Spell findSpell(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activate(String[] actions, Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deavtivateAll() {
		// TODO Auto-generated method stub
		
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
	public void addGameActionListener(GameActionListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEffectListener(EffectListener listener) {
		// TODO Auto-generated method stub
		
	}

}
