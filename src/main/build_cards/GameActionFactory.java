package main.build_cards;

import main.GameAction;

public class GameActionFactory implements CreatesActions {
	
	public static CreatesActions getInstance() {
		return new GameActionFactory();
	}
	
	@Override
	public GameAction createAction(String actionName) {
		// TODO Auto-generated method stub
		return null;
	}

}
