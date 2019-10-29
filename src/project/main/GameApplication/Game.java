package project.main.GameApplication;

import java.util.UUID;

import project.main.jsonObjects.MessageInLanguage;
import project.main.util.MapsRankAndLevel;

public interface Game extends Runnable{
	public MapsRankAndLevel getRankMapper(); 
	public Player getActivePlayer();
	public Player[] getPlayers();
	public Player getPlayer(UUID id);
	public IsPhaseInGame getActivePhase();
	public boolean hasEnded();
	public boolean hasStarted();
	public boolean processGameStack(Player player);
	public ProcessesBattle getActiveBattle();
	public void setActiveBattle(ProcessesBattle battle);
	public void prompt(Player promptedPlayer, MessageInLanguage message, AcceptPromptAnswers prompter);
	public void prompt(Player promptedPlayer, MessageInLanguage message);
}
