package main.GameApplication;

import java.util.UUID;

import main.util.mapsRankAndLevel;

public interface Game extends Runnable{
	public mapsRankAndLevel getRankMapper(); 
	public Player getActivePlayer();
	public Player[] getPlayers();
	public Player getPlayer(UUID id);
	public void prompt(Player promptedPlayer, String message, AcceptPromptAnswers prompter);
}
