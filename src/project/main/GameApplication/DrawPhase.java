package project.main.GameApplication;

import java.util.ArrayList;

import project.main.Action.GameAction;
import project.main.Listeners.GameActionListener;
import project.main.Listeners.GameListener;
import project.main.jsonObjects.ActionDefinitionLibrary;

public class DrawPhase extends GamePhase implements GameActionListener {
	
	private Player activePlayer;
	private boolean alreadyDrawn;
	
	public DrawPhase() {
		super("DrawPhase");
		GameListener.getInstance().addGameActionListener(this);
		alreadyDrawn = false;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void restorePhaseStatus() {
		inactivateAll();
		if(!alreadyDrawn) {
			activePlayer = game.getActivePlayer();
			ArrayList<IsAreaInGame> zones = activePlayer.getGameZones();
			for(IsAreaInGame zone : zones) {
				zone.activate(activePlayer, this);
			}
		}
	}
	
	public void setAlreadyDrawn(boolean value) {
		alreadyDrawn = value;
	}
	
	public boolean getAlreadyDrawn() {
		return alreadyDrawn;
	}

	@Override
	public void leave() {
		finishedStacks.add(activeGameStack);
		activeGameStack = null;
		alreadyDrawn = false;
		deactivateDrawActions();
		GameListener.getInstance().phaseEnded(this);
	}
	
	@Override
	public void actionActivated(GameAction action) {
		if(action.getCode().equals("Draw") && action.getCard().getOwningPlayer() == game.getActivePlayer()) {
			deactivateDrawActions();
		}
	}

	@Override
	public void actionExecuted(GameAction action) {
		//Do nothing
	}
	
	private void deactivateDrawActions() {
		ArrayList<IsAreaInGame> zones = activePlayer.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.deavtivateAll();
		}
	}
}
