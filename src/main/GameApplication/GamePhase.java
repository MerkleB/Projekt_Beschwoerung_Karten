package main.GameApplication;

import java.util.ArrayList;
import main.jsonObjects.ActionDefinitionLibrary;

public class GamePhase implements IsPhaseInGame {

	private String name;
	private ArrayList<String> actionsToActivate;
	
	public GamePhase(String phaseName) {
		name = phaseName;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void restorePhaseStatus() {
		Player player = Application.getInstance(null).getGame().getActivePlayer();
		ArrayList<IsAreaInGame> zones = player.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.activate(player, this);
		}
	}

	@Override
	public ArrayList<String> getActionsToActivate() {
		if(actionsToActivate == null) {
			actionsToActivate = ActionDefinitionLibrary.getInstance().getPhaseActions(name);
		}
		return actionsToActivate;
	}

}
