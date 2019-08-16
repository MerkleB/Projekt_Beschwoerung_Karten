package main.GameApplication;

import main.Card.Card;
import main.Card.Spell;
import main.Card.Summon;
import main.Listeners.EffectListener;
import main.Listeners.GameActionListener;
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

}
