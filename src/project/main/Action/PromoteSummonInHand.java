package project.main.Action;

import project.main.Card.ActivityStatus;
import project.main.Card.StatusChange;
import project.main.Card.Summon;
import project.main.Card.SummonStatus;
import project.main.Effect.Effect;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.Player;
import project.main.Listeners.GameActionListener;
import project.main.Listeners.GameListener;
import project.main.exception.NoCardException;
import project.main.exception.NotActivableException;

import java.util.ArrayList;
import java.util.UUID;

public class PromoteSummonInHand extends Action implements GameActionListener {
	
	private StatusChange summonPointsChange;
	private UUID summoningPointsID;
	private StatusChange wastageChange;
	private UUID wastageID;
	private SummonStatus changedStatus;

	@Override
	public String getCode() {
		return "PromoteSummonInHand";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}



	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
		game.processGameStack(activatingPlayer);
	}



	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(((Summon)owningCard).getLevel() > 1) return false;
		return true;
	}



	@Override
	public void execute() {
		if(activ && !withdrawn) {
			super.execute();
			int currentLevel = ((Summon)owningCard).getLevel();
			Summon successor = ((Summon)owningCard).getSummonHierarchy().getSummonOfLevel(currentLevel+1);
			successor.setID(((Summon)owningCard).getID());
			successor.setActivityStatus(ActivityStatus.READY, -1);
			((Summon)owningCard).setActivityStatus(ActivityStatus.NOT_IN_GAME, -1);
			addGameToSuccessor(successor);
			try {
				successor.setOwningPlayer(((Summon)owningCard).getOwningPlayer());
			} catch (NoCardException e) {
				throw new RuntimeException("Magic collector shouldn't be in hand.");
			}
			ArrayList<String> activeActions = new ArrayList<String>();
			ArrayList<GameAction> owningCardActions = owningCard.getActions();
			for(GameAction action : owningCardActions) {
				if(action.activateable(owningCard.getOwningPlayer())) {
					activeActions.add(action.getCode());
				}
			}
			activeActions.add("UnpromoteSummonInHand");
			successor.setActiv(activeActions, owningCard.getOwningPlayer());
			addSummonStatusChanges(successor, (Summon)owningCard);
			IsAreaInGame hand = owningCard.getOwningPlayer().getGameZone(HandZone);
			hand.removeCard(owningCard);
			hand.addCard(successor);
			GameListener.getInstance().actionExecuted(this);
		}
	}
	
	private void addSummonStatusChanges(Summon successor, Summon predecessor) {
		summoningPointsID = UUID.randomUUID();
		summonPointsChange = new StatusChange(StatusChange.SUMMONINGPOINT, summoningPointsID, StatusChange.TYPE_ADDITION, 1);
		wastageID = UUID.randomUUID();
		wastageChange = new StatusChange(StatusChange.MAGICWASTAGE, wastageID, StatusChange.TYPE_ADDITION, 2);
		changedStatus = successor.getStatus();
		changedStatus.addStatusChange(summonPointsChange);
		changedStatus.addStatusChange(wastageChange);
		ArrayList<StatusChange> predecessorChanges = new ArrayList<StatusChange>();
		ArrayList<StatusChange> summoningPointChanges = predecessor.getStatus().getChanges(StatusChange.SUMMONINGPOINT);
		if(summoningPointChanges != null) {
			predecessorChanges.addAll(summoningPointChanges);
		}
		ArrayList<StatusChange> wastageChanges = predecessor.getStatus().getChanges(StatusChange.MAGICWASTAGE);
		if(summoningPointChanges != null) {
			predecessorChanges.addAll(wastageChanges);
		}
		for(StatusChange change : predecessorChanges) {
			changedStatus.addStatusChange(change);
		}
	}
	
	private void addGameToSuccessor(Summon successor) {
		ArrayList<GameAction> actions = successor.getActions();
		for(GameAction action : actions) {
			action.setGame(game);
		}
		Effect[] effects;
		try {
			effects = successor.getEffects();
			for(Effect effect : effects) {
				effect.setGame(game);
			}
		} catch (NoCardException e) {
			//Effects are ignored
		}
	}

	@Override
	public void actionActivated(GameAction action) {
		//Do nothing		
	}

	@Override
	public void actionExecuted(GameAction action) {
		if(action.getCode().equals("UnpromoteSummonInHand")) {
			int currentLevel = ((Summon)owningCard).getLevel();
			Summon successor = ((Summon)owningCard).getSummonHierarchy().getSummonOfLevel(currentLevel+1);
			if(successor == action.getCard()) {
				changedStatus.removeStatus(summoningPointsID);
				changedStatus.removeStatus(wastageID);
				summoningPointsID = null;
				summonPointsChange = null;
				wastageID = null;
				wastageChange = null;
			}
		}
	}

}
