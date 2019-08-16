package main.Listeners;

import main.Action.GameAction;

public interface GameActionListener {
	public void actionActivated(GameAction action);
	public void actionExecuted(GameAction action);
}
