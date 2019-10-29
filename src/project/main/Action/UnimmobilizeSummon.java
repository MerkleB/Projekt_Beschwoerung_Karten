package project.main.Action;

import project.main.Card.ActivityStatus;
import project.main.Card.Summon;
import project.main.GameApplication.Player;
import project.main.Listeners.GameListener;
import project.main.exception.NotActivableException;

public class UnimmobilizeSummon extends Action {

	@Override
	public String getCode() {
		return "UnimmobilizeSummon";
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
		ActivityStatus status = ((Summon)owningCard).getActivityStatus();
		if(!status.getStatus().equals(ActivityStatus.IMMOBILIZED)) return false;
		if(status.getDurability() > 0) return false;
		if(owningCard.getOwningPlayer().getMagicEnergyStock().getFreeEnergy() < ((Summon)owningCard).getStatus().getMagicPreservationValue()) return false;
		return true;
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			((Summon)owningCard).getActivityStatus().setStatus(ActivityStatus.USED, 0);
			setInactiv();
			GameListener.getInstance().actionExecuted(this);
		}
	}

}
