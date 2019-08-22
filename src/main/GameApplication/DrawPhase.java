package main.GameApplication;

import java.util.ArrayList;

import main.Action.GameAction;
import main.Listeners.GameActionListener;
import main.Listeners.GameListener;
import main.jsonObjects.ActionDefinitionLibrary;

public class DrawPhase implements IsPhaseInGame, GameActionListener {
	
	private String name;
	private ArrayList<String> actionsToActivate;
	private Player activePlayer;
	
	public DrawPhase(String phaseName) {
		name = phaseName;
		GameListener.getInstance().addGameActionListener(this);
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void restorePhaseStatus() {
		activePlayer = Application.getInstance(null).getGame().getActivePlayer();
		ArrayList<IsAreaInGame> zones = activePlayer.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.activate(activePlayer, this);
		}
	}

	@Override
	public ArrayList<String> getActionsToActivate() {
		if(actionsToActivate == null) {
			actionsToActivate = ActionDefinitionLibrary.getInstance().getPhaseActions(name);
		}
		return actionsToActivate;
	}

	@Override
	public void process() {
		restorePhaseStatus();
	}

	@Override
	public void leave() {
		deactivateDrawActions();
	}
	
	@Override
	public void actionActivated(GameAction action) {
		//do nothing
	}

	@Override
	public void actionExecuted(GameAction action) {
		if(action.getName().equals("Draw") && action.getCard().getOwningPlayer() == activePlayer) {
			deactivateDrawActions();
		}
	}
	
	private void deactivateDrawActions() {
		ArrayList<IsAreaInGame> zones = activePlayer.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.deavtivateAll();
		}
	}

}
