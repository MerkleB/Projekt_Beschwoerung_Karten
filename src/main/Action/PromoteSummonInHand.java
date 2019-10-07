package main.Action;

import main.GameApplication.IsAreaInGame;
import main.GameApplication.Player;
import main.Listeners.GameActionListener;
import main.Listeners.GameListener;
import main.exception.NotActivableException;
import java.util.ArrayList;
import java.util.UUID;

import main.Card.StatusChange;
import main.Card.Summon;
import main.Card.SummonStatus;

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
			int currentLevel = ((Summon)owningCard).getLevel();
			Summon successor = ((Summon)owningCard).getSummonHierarchy().getSummonOfLevel(currentLevel+1);
			ArrayList<String> activActions = new ArrayList<String>();
			ArrayList<GameAction> owningCardActions = owningCard.getActions();
			for(GameAction action : owningCardActions) {
				if(action.activateable(owningCard.getOwningPlayer())) {
					activActions.add(action.getCode());
				}
			}
			activActions.add("UnpromoteSummonInHand");
			successor.setActiv(activActions, owningCard.getOwningPlayer());
			addSummonStatusChanges(successor);
			IsAreaInGame hand = owningCard.getOwningPlayer().getGameZone(HandZone);
			hand.removeCard(owningCard);
			hand.addCard(successor);
			GameListener.getInstance().actionExecuted(this);
		}
	}
	
	private void addSummonStatusChanges(Summon successor) {
		summoningPointsID = UUID.randomUUID();
		summonPointsChange = new StatusChange(StatusChange.SUMMONINGPOINT, summoningPointsID, StatusChange.TYPE_ADDITION, 1);
		wastageID = UUID.randomUUID();
		wastageChange = new StatusChange(StatusChange.MAGICWASTAGE, wastageID, StatusChange.TYPE_ADDITION, 2);
		changedStatus = successor.getStatus();
		changedStatus.addStatusChange(summonPointsChange);
		changedStatus.addStatusChange(wastageChange);
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
