package project.main.GameApplication;

import java.util.UUID;

import project.main.util.MessageInLanguage;
import project.main.util.mapsRankAndLevel;

public interface Game extends Runnable{
	public mapsRankAndLevel getRankMapper(); 
	public Player getActivePlayer();
	public Player[] getPlayers();
	public Player getPlayer(UUID id);
	public IsPhaseInGame getActivePhase();
	public boolean hasEnded();
	public boolean hasStarted();
	public boolean processGameStack();
	public void prompt(Player promptedPlayer, MessageInLanguage message, AcceptPromptAnswers prompter);
	public void prompt(Player promptedPlayer, MessageInLanguage message);
}