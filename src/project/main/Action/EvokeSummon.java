package project.main.Action;

import project.main.Card.*;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.Player;
import project.main.Listeners.GameListener;
import project.main.exception.NotActivableException;

public class EvokeSummon extends Action {
	
	@Override
	public String getCode() {
		return "EvokeSummon";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
			
		initMetadata();
		metadata.put("Target-ID", owningCard.getID().toString());
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
		
	}
	
	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		
		initMetadata();
		metadata.put("Target-ID", owningCard.getID().toString());
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
			Player ownerOfCard = owningCard.getOwningPlayer();
			if(ownerOfCard.getSummoningPoints() >= ((Summon) owningCard).getStatus().getSummoningPoints()) {
				if(ownerOfCard.getFreeEnergy() >= ((Summon) owningCard).getStatus().getMagicPreservationValue()) {
					return true;
				}
			}
		return false;
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			Player ownerOfCard = owningCard.getOwningPlayer();
			int remainingPoints = ownerOfCard.decreaseSummonigPoints(((Summon) owningCard).getStatus().getSummoningPoints());
			if(remainingPoints != -1) {
				IsAreaInGame handZone = actionIsActivFor.getGameZone("HandZone");
				if(handZone == null) {
					throw new RuntimeException("Fatal error: Player has no HandZone.");
				}
				handZone.removeCard(owningCard);
				IsAreaInGame summonZone = actionIsActivFor.getGameZone("SummonZone");
				if(summonZone == null) {
					throw new RuntimeException("Fatal error: Player has no SummonZone");
				}
				summonZone.addCard(owningCard);
				GameListener.getInstance().actionExecuted(this);
				summonZone.deavtivateAll();
				summonZone.activate(ownerOfCard, game.getActivePhase());
			}
		}
	}

}