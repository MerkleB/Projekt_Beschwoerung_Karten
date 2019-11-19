package project.main.GameApplication;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import project.main.Listeners.GameListener;
import project.main.jsonObjects.ActionDefinitionLibrary;

public abstract class GamePhase implements IsPhaseInGame {

	protected String name;
	protected ArrayList<String> actionsToActivate;
	protected Game game;
	protected OwnsGameStack activeGameStack;
	protected ArrayList<OwnsGameStack> finishedStacks;
	
	public GamePhase(String phaseName) {
		name = phaseName;
		activeGameStack = GameStack.getInstance(this);
		finishedStacks = new ArrayList<OwnsGameStack>();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	protected void inactivateAll() {
		Player[] players = game.getPlayers();
		for(Player player : players) {
			ArrayList<IsAreaInGame> zones = player.getGameZones();
			for(IsAreaInGame zone : zones) {
				zone.deavtivateAll();
			}
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
		GameListener.getInstance().phaseStarted(this);
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
		ReentrantLock lock = new ReentrantLock();
		try {
			lock.lock();
			if(activeGameStack == null) {
				activeGameStack = GameStack.getInstance(this);
			}
			if(activeGameStack.hasFinished() || activeGameStack.hasStarted()) {
				finishedStacks.add(activeGameStack);
				activeGameStack = GameStack.getInstance(this);
			}
			return activeGameStack;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public ArrayList<OwnsGameStack> getFinisheGameStacks() {
		return finishedStacks;
	}

}
