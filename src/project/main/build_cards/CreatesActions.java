package project.main.build_cards;

import project.main.Action.GameAction;

public interface CreatesActions {
	public GameAction createAction(String actionName);
}
