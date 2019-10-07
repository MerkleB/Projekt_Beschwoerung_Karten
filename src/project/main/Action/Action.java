package project.main.Action;

import java.util.Hashtable;

import project.main.Card.Card;
import project.main.Card.Summon;
import project.main.GameApplication.Application;
import project.main.GameApplication.Game;
import project.main.GameApplication.Player;
import project.main.exception.NotActivableException;
import project.main.util.TextProvider;

public abstract class Action implements GameAction {
	protected static final String SummonSelect = "SelectSummon";
	protected static final String CollectorSelect = "SelectMagicCollector";
	protected static final String SummonZone = "SummonZone";
	protected static final String CollectorZone = "CollectorZone";
	protected static final String HandZone = "HandZone";
	protected static final String DiscardPile = "DiscardPile";
	
	protected Card owningCard;
	protected boolean activ = false;
	protected boolean withdrawn = false;
	protected boolean isActivated = false;
	protected Hashtable<String, String> metadata;
	protected Player actionIsActivFor;
	protected Stackable activator;
	protected Game game;
    
	@Override
	public String getName() {
		return TextProvider.getInstance().getActionName(getCode(), Application.getInstance().getLanguage());
	}
	
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

	@Override
	public Game getGame() {
		return game;
	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}

}
