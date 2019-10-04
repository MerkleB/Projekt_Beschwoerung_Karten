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
	private Game game;
	private OwnsGameStack activeGameStack;
	private ArrayList<OwnsGameStack> finishedStacks;
	
	public DrawPhase() {
		GameListener.getInstance().addGameActionListener(this);
		activeGameStack = GameStack.getInstance();
		finishedStacks = new ArrayList<OwnsGameStack>();
	}
	
	@Override
	public String getName() {
		return "DrawPhase";
	}

	@Override
	public void restorePhaseStatus() {
		activePlayer = game.getActivePlayer();
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
		Thread stackThread = new Thread(activeGameStack);
		stackThread.start();
		restorePhaseStatus();
		GameListener.getInstance().phaseStarted(this);
	}

	@Override
	public void leave() {
		activeGameStack.finish();
		finishedStacks.add(activeGameStack);
		activeGameStack = null;
		deactivateDrawActions();
		GameListener.getInstance().phaseEnded(this);
	}
	
	@Override
	public void actionActivated(GameAction action) {
		if(action.getName().equals("Draw") && action.getCard().getOwningPlayer() == activePlayer) {
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

	@Override
	public Game getGame() {
		return game;
	}

	@Override
	public void setGame(Game game) {
		if(game != null) {
			this.game = game;
		}
	}

	@Override
	public OwnsGameStack getActiveGameStack() {
		if(activeGameStack == null) {
			activeGameStack = GameStack.getInstance();
		}
		if(activeGameStack.hasFinished()) {
			finishedStacks.add(activeGameStack);
			activeGameStack = GameStack.getInstance();
		}
		return null;
	}

	@Override
	public ArrayList<OwnsGameStack> getFinisheGameStacks() {
		return finishedStacks;
	}

}
