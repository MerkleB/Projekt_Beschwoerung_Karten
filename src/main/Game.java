package main;

import main.Listeners.EffectListener;
import main.Listeners.GameActionListener;
import main.util.mapsRankAndLevel;

public interface Game  extends HoldingCards{
	public mapsRankAndLevel getRankMapper(); 
	public Player getActivePlayer();
	public Player[] getPlayers();
	public void addGameActionListener(GameActionListener listener);
	public void addEffectListener(EffectListener listener);
}
