package project.main.GameApplication;

import java.util.ArrayList;

import project.main.Listeners.GameListener;
import project.main.jsonObjects.ActionDefinitionLibrary;

public class GamePhase implements IsPhaseInGame {

	private String name;
	private ArrayList<String> actionsToActivate;
	private Game game;
	private OwnsGameStack activeGameStack;
	private ArrayList<OwnsGameStack> finishedStacks;
	
	public GamePhase(String phaseName) {
		name = phaseName;
		activeGameStack = GameStack.getInstance();
		finishedStacks = new ArrayList<OwnsGameStack>();
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void restorePhaseStatus() {
		Player player = game.getActivePlayer();
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
		Player activPlayer = game.getActivePlayer();
		ArrayList<IsAreaInGame> zones = activPlayer.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.deavtivateAll();
		}
		GameListener.getInstance().phaseEnded(this);
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
		return activeGameStack;
	}

	@Override
	public ArrayList<OwnsGameStack> getFinisheGameStacks() {
		return finishedStacks;
	}

}
