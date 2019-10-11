package project.main.GameApplication;

import java.util.UUID;

import project.main.Card.Summon;

public interface ProcessesBattle{
	public static final String ENDED = "ended";
	public static final String ABRUPT = "abrupt";
	public static final String RUNNING = "running";
	public static final String INIT = "initial";
	
	public void proceed(Player player);
	public void setCombatants(Summon attacker, Summon defender);
	public void start();
	public void end();
	public void remove(UUID summonID);
	public Summon getWinner();
	public Summon getLooser();
	public String getStatus();
}