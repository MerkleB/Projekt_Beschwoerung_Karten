package project.main.Listeners;

import project.main.Action.GameAction;

public interface GameActionListener {
	public void actionActivated(GameAction action);
	public void actionExecuted(GameAction action);
}
