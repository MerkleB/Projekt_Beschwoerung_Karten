package project.main.build_cards;

import project.main.Action.GameAction;

public class GameActionFactory implements CreatesActions {
	
	public static CreatesActions getInstance() {
		return new GameActionFactory();
	}
	
	@Override
	public GameAction createAction(String actionName) {
		String className = "project.main.Action."+actionName;
		GameAction action = null;
		try {
			Class<?> actionClass = Class.forName(className);
			action = (GameAction)actionClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("GameActionFactory - Class "+className+" could no be found or instantiated!");
		}
		return action;
	}

}
