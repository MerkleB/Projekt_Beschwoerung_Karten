package main.Listeners;

import main.GameApplication.Battle;

public interface BattleListener {
	public void battleStarted(Battle battle);
	public void battleEnded(Battle battle);
	public void battleAbrupt(Battle battle);
	public void attackHappened(BattleEventObject eventDetails);
}
