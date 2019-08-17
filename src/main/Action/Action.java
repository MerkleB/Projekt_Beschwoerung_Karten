package main.Action;

import java.util.Hashtable;

import main.Card.Card;
import main.Card.Summon;
import main.GameApplication.Player;
import main.exception.NotActivableException;

public abstract class Action implements GameAction {
	protected Summon owningCard;

	protected boolean activ = false;
	protected boolean withdrawn = false;
	protected boolean isActivated = false;
	protected Hashtable<String, String> metadata;
	protected Player actionIsActivFor;
	protected Stackable activator;
    

	@Override
	public void activate(Player activator) throws NotActivableException {
		if(activator != actionIsActivFor || !activateable(activator)) {
			throw new NotActivableException(getName()+" is not activateable for card "+owningCard.getID());
		}else {
			isActivated = true;
		}		
	}
	
	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		if(!activ || activator != this.activator || activatingPlayer != this.actionIsActivFor) {
			throw new NotActivableException(getName()+" is currently only activatable by "
											+this.activator.getName()
											+" of Player "+this.actionIsActivFor.getID());
		}else {
			isActivated = true;
		}
	}

	@Override
	public boolean activateable(Player activator) {
		if(!activ || actionIsActivFor == activator){
            return false;
        }else return true;
	}

	@Override
	public void withdraw() {
		withdrawn = true;
	}
	
	@Override
	public boolean isWithdrawn() {
		return withdrawn;
	}

	@Override
	public void setActiv(Player player) {
		activ = true;
		actionIsActivFor = player;
	}

	@Override
	public void setActivBy(Stackable stackable, Player player) {
		activ = true;
		actionIsActivFor = player;
		activator = stackable;
	}

	@Override
	public void setInactiv() {
		activ = false;
		actionIsActivFor = null;
	}

	@Override
	public Stackable getActivatingStackable() {
		return activator;
	}

	@Override
	public Card getCard() {
		return owningCard;
	}

	@Override
	public void setCard(Card card) {
		owningCard = (Summon)card;
	}

	@Override
	public Hashtable<String, String> getMetaData() {
		return metadata;
	}
	
	protected void initMetadata() {
		if(metadata == null) {
			metadata = new Hashtable<>();
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean areEqual = false;
		if(obj instanceof Action) {
			Action action = (Action) obj;
			if(getName().equals(action.getName())) areEqual = true;
		}
		return areEqual;
	}

}