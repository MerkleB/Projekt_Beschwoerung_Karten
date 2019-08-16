package main.Action;

import main.GameStack;
import main.Player;
import main.Stackable;
import main.Listeners.GameListener;
import main.exception.NoCardException;
import main.exception.NotActivableException;

public class EvokeSummon extends Action {
	
	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
			
		initMetadata();
		metadata.put("Target-ID", owningCard.getID().toString());
		GameStack.getInstance().addEntry(this);
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
			
		try {
			Player ownerOfCard = owningCard.getOwningPlayer();
			if(ownerOfCard.getSummoningPoints() >= owningCard.getSummoningPoints()) {
				if(ownerOfCard.getFreeEnergy() >= owningCard.getMagicPreservationValue()) {
					return true;
				}
			}
		} catch (NoCardException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			Player ownerOfCard = owningCard.getOwningPlayer();
			try {
				int remainingPoints = ownerOfCard.decreaseSummonigPoints(owningCard.getSummoningPoints());
				if(remainingPoints != -1) {
					//TODO: Summon card
				}
			} catch (NoCardException e) {
				System.out.println("Servere Error: " + e.getMessage());
			}
		}
	}

}
