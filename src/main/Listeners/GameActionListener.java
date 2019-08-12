package main.Listeners;

import main.GameAction;

public interface GameActionListener {
	public void actionActivated(GameAction action);
	public void actionExecuted(GameAction action);
}
