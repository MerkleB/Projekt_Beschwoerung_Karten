package main.build_cards;

import main.Action.GameAction;

public interface CreatesActions {
	public GameAction createAction(String actionName);
}
