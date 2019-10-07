package project.main.Action;

import project.main.Card.Spell;
import project.main.GameApplication.Player;
import project.main.Listeners.GameListener;
import project.main.exception.NoCardException;
import project.main.exception.NotActivableException;

public class Cast extends Action {

	@Override
	public String getCode() {
		return "Cast";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		activator.decreaseFreeEnergy(((Spell)owningCard).getNeededMagicEnergy());
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		activatingPlayer.decreaseFreeEnergy(((Spell)owningCard).getNeededMagicEnergy());
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(((Spell)owningCard).getNeededMagicEnergy() > activator.getFreeEnergy()) return false;
		try {
			if(!(owningCard.getEffect(0).activateable(activator))) return false;
		} catch (NoCardException e) {
			return false;
		}
		return true;
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			try {
				owningCard.getEffect(0).execute();
				GameListener.getInstance().actionExecuted(this);
				actionIsActivFor.getGameZone(HandZone).removeCard(owningCard);
				actionIsActivFor.getGameZone(DiscardPile).addCard(owningCard);
			} catch (NoCardException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
