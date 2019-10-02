package main.Action;

import main.GameApplication.Player;
import main.GameApplication.SummonZone;
import main.Listeners.GameListener;
import main.build_cards.KnowsSummonAscentHierarchy;
import main.exception.NotActivableException;
import main.Card.Summon;

public class PromoteSummon extends Action {
	
	public KnowsSummonAscentHierarchy hierarchy;
	
	@Override
	public String getCode() {
		return "PromoteSummon";
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		hierarchy = ((Summon)owningCard).getSummonHierarchy();
		if(!hierarchy.canAscend() || !owningCard.equals(hierarchy.getSummonOfLevel(2))) return false;
		return true;
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
	public void execute() {
		if(isActivated && !isWithdrawn()) {
			Summon currentLevelCard = (Summon)owningCard;
			Summon nextLevelCard = hierarchy.getNextSummonInHierarchy(currentLevelCard);
			SummonZone zone = (SummonZone)owningCard.getOwningPlayer().getGameZone("SummonZone");
			zone.removeCard(currentLevelCard);
			zone.addCard(nextLevelCard);
			hierarchy.clearExperience();
			GameListener.getInstance().actionExecuted(this);
		}
	}

}
