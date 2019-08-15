package main.Action;

import java.util.Hashtable;

import main.Card;
import main.GameAction;
import main.GameStack;
import main.OwnsGameStack;
import main.Player;
import main.Stackable;
import main.Summon;
import main.Listeners.GameActionListener;
import main.Listeners.GameListener;
import main.exception.NoCardException;
import main.exception.NotActivableException;

public class EvokeSummon implements GameAction {

	private Summon owningCard;
	private boolean activ = false;
	private boolean withdrawn = false;
	private Hashtable<String, String> metadata;
	private Player actionIsActivFor;
	
	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		if(activator != actionIsActivFor) {
			return;
		}
		if(activateable(activator)) {
			initMetadata();
			metadata.put("ID", owningCard.getID().toString());
			GameStack.getInstance().addEntry(this);
			GameListener.getInstance().actionActivated(this);
		}else {
			throw new NotActivableException("EvokeSummon is not activateable for card "+owningCard.getID());
		}
	}

	@Override
	public boolean activateable(Player activator) {
		Player ownerOfCard = owningCard.getOwningPlayer();
		try {
			if(activ && actionIsActivFor == activator) {
				if(ownerOfCard.getSummoningPoints() >= owningCard.getSummoningPoints()) {
					if(ownerOfCard.getFreeEnergy() >= owningCard.getMagicPreservationValue()) {
						return true;
					}
				}
			}
		} catch (NoCardException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public void withdraw() {
		
	}
	
	@Override
	public boolean isWithdrawn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setActiv(Player player) {
		activ = true;
		actionIsActivFor = player;
	}

	@Override
	public void setInactiv() {
		activ = false;
		actionIsActivFor = null;
	}

	@Override
	public Card getCard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCard(Card card) {
		owningCard = (Summon)card;
	}

	@Override
	public Hashtable<String, String> getMetaData() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void initMetadata() {
		if(metadata == null) {
			metadata = new Hashtable<>();
		}
	}

	@Override
	public void activateBy(Stackable stackable, Player activator) {
		// TODO Auto-generated method stub
	}

}
