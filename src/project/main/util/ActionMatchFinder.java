package project.main.util;

import java.util.ArrayList;

public class ActionMatchFinder implements FindsActionMatches {
	
	public static FindsActionMatches getInstance() {
		FindsActionMatches instance = new ActionMatchFinder();
		return instance;
	}
	
	@Override
	public ArrayList<String> getMatchedActions(ArrayList<String> actions, ArrayList<String> referenceActions) {
		ArrayList<String> matchedActions = new ArrayList<String>();
		for(String action : actions) {
			for(String refAction : referenceActions) {
				if(action.equals(refAction)) {
					matchedActions.add(action);
				}
			}
		}
		return matchedActions;
	}

}
