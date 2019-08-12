package main.build_cards;

import main.GameAction;

public interface CreatesActions {
	public GameAction createAction(String actionName);
}
