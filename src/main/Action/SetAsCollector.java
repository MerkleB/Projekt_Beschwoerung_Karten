package main.Action;

import main.GameApplication.IsAreaInGame;
import main.GameApplication.Player;
import main.Listeners.GameListener;
import main.exception.NotActivableException;

public class SetAsCollector extends Action {

	@Override
	public String getCode() {
		return "SetAsCollector";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		activator.decreaseNumberOfRemainingCollectorActions(1);
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		activatingPlayer.decreaseNumberOfRemainingCollectorActions(1);
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(owningCard.getOwningPlayer().getNumberOfRemainingCollectorActions() >= 0) return false;
		return true;
	}

	@Override
	public void execute() {
		if(isActivated && !isWithdrawn()) {
			IsAreaInGame hand = owningCard.getOwningPlayer().getGameZone(HandZone);
			IsAreaInGame collectorZone = owningCard.getOwningPlayer().getGameZone(CollectorZone);
			hand.removeCard(owningCard);
			collectorZone.addCard(owningCard);
			GameListener.getInstance().actionExecuted(this);
		}
	}

}
