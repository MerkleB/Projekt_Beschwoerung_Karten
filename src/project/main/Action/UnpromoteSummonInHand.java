package project.main.Action;

import project.main.Card.ActivityStatus;
import project.main.Card.StatusChange;
import project.main.Card.Summon;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.Player;
import project.main.Listeners.GameListener;
import project.main.exception.NotActivableException;

public class UnpromoteSummonInHand extends Action {

	@Override
	public String getCode() {
		return "UnpromoteSummonInHand";
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
		game.processGameStack(activatingPlayer);
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(((Summon)owningCard).getLevel() < 1) return false;
		return true;
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			super.execute();
			IsAreaInGame hand = owningCard.getOwningPlayer().getGameZone(HandZone);
			hand.removeCard(owningCard);
			int currentLevel = ((Summon)owningCard).getLevel();
			if(currentLevel == 0) {
				throw new RuntimeException("Fatal error: Level 0 Summon can't be unpromoted. Card-ID: "+owningCard.getCardID());
			}
			Summon predecessor = ((Summon)owningCard).getSummonHierarchy().getSummonOfLevel(currentLevel-1);
			hand.addCard(predecessor);
			predecessor.setActivityStatus(ActivityStatus.READY, -1);
			((Summon)owningCard).setActivityStatus(ActivityStatus.NOT_IN_GAME, -1);
			predecessor.getStatus().removeAll(StatusChange.SUMMONINGPOINT);
			predecessor.getStatus().removeAll(StatusChange.MAGICWASTAGE);
			GameListener.getInstance().actionExecuted(this);
		}
	}
}
