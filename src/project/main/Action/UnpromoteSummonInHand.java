package project.main.Action;

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
			IsAreaInGame hand = owningCard.getOwningPlayer().getGameZone(HandZone);
			hand.removeCard(owningCard);
			int currentLevel = ((Summon)owningCard).getLevel();
			if(currentLevel == 0) {
				throw new RuntimeException("Fatal error: Level 0 Summon can't be unpromoted. Card-ID: "+owningCard.getCardID());
			}
			hand.addCard(((Summon)owningCard).getSummonHierarchy().getSummonOfLevel(currentLevel-1));
			GameListener.getInstance().actionExecuted(this);
		}
	}

}
