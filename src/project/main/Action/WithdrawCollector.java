package project.main.Action;

import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.Player;
import project.main.Listeners.GameListener;
import project.main.exception.NoCollectorException;
import project.main.exception.NotActivableException;

public class WithdrawCollector extends Action {

	@Override
	public String getCode() {
		return "WithdrawCollector";
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
		try {
			if((owningCard.getCollector().isCompletelyDepleted())) return false;
		} catch (NoCollectorException e) {
			System.out.println("The collector is not a collector");
			return false;
		}
		if(owningCard.getOwningPlayer().getNumberOfRemainingCollectorActions() == 0) return false;
		
		return true;
	}

	@Override
	public void execute() {
		if(isActivated && !isWithdrawn()) {
			super.execute();
			IsAreaInGame collectorZone = owningCard.getOwningPlayer().getGameZone(CollectorZone);
			IsAreaInGame hand = owningCard.getOwningPlayer().getGameZone(HandZone);
			owningCard.getCollector().setIsCollector(false);
			collectorZone.removeCard(owningCard);
			hand.addCard(owningCard.getCollector().getRealCard());
			GameListener.getInstance().actionExecuted(this);
		}
	}

}
