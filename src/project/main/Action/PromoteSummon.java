package project.main.Action;

import project.main.Card.ActivityStatus;
import project.main.Card.Summon;
import project.main.GameApplication.Player;
import project.main.GameApplication.SummonZone;
import project.main.Listeners.GameListener;
import project.main.build_cards.KnowsSummonAscentHierarchy;
import project.main.exception.NotActivableException;

public class PromoteSummon extends Action {
	
	public KnowsSummonAscentHierarchy hierarchy;
	
	@Override
	public String getCode() {
		return "PromoteSummon";
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(((Summon)owningCard).getActivityStatus().getStatus().equals(ActivityStatus.IMMOBILIZED)) return false;
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
			((Summon)owningCard).setActivityStatus(ActivityStatus.USED, 0);
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
