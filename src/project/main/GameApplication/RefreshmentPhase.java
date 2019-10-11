package project.main.GameApplication;

import java.util.ArrayList;

import project.main.Card.Card;
import project.main.Card.Summon;
import project.main.Listeners.GameListener;
import project.main.jsonObjects.ActionDefinitionLibrary;

public class RefreshmentPhase implements IsPhaseInGame {
	
	private ArrayList<String> actionsToActivate;
	private Game game;
	private OwnsGameStack activeGameStack;
	private ArrayList<OwnsGameStack> finishedStacks;
	
	public RefreshmentPhase() {
		activeGameStack = GameStack.getInstance(this);
		finishedStacks = new ArrayList<OwnsGameStack>();
	}
	
	@Override
	public String getName() {
		return "RefreshmentPhase";
	}

	@Override
	public void restorePhaseStatus() {
		Player activPlayer = game.getActivePlayer();
		int usedEnergy = activPlayer.getUsedEnergy();
		activPlayer.increaseFreeEnergyFromUsed(usedEnergy);
		SummonZone summonZone = (SummonZone)activPlayer.getGameZone("SummonZone");
		ArrayList<Card> cards = summonZone.getCards();
		for(Card card : cards) {
			Summon summon = (Summon)card;
			if(summon.getActivityStatus().equals(Summon.USED)) {
				summon.setActivityStatus(Summon.READY);
			}
		}
	}

	@Override
	public ArrayList<String> getActionsToActivate() {
		if(actionsToActivate == null) {
			actionsToActivate = ActionDefinitionLibrary.getInstance().getPhaseActions(getName());
		}
		return actionsToActivate;
	}

	@Override
	public void process() {
		restorePhaseStatus();
		GameListener.getInstance().phaseStarted(this);
	}

	@Override
	public void leave() {
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
			activeGameStack = GameStack.getInstance(this);
		}
		if(activeGameStack.hasFinished()) {
			finishedStacks.add(activeGameStack);
			activeGameStack = GameStack.getInstance(this);
		}
		return null;
	}

	@Override
	public ArrayList<OwnsGameStack> getFinisheGameStacks() {
		return finishedStacks;
	}

}
