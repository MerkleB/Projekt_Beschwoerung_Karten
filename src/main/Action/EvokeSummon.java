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

public class EvokeSummon implements GameAction {

	private Summon owningCard;
	private boolean activ = false;
	private boolean withdrawn = false;
	private Hashtable<String, String> metadata;
	
	@Override
	public String getName() {
		return getClass().getName();
	}

	@Override
	public void activate() {
		if(activ) {
			initMetadata();
			metadata.put("ID", owningCard.getID().toString());
			GameStack.getInstance().addEntry(this);
			GameListener.getInstance().actionActivated(this);
		}
	}

	@Override
	public boolean activatable() {
		Player ownerOfCard = owningCard.getOwningPlayer();
		try {
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
	public void setActiv() {
		if(activatable()) {
			activ = true;
		}
	}

	@Override
	public void setInactiv() {
		activ = false;
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
	public void activateBy(Stackable stackable) {
		// TODO Auto-generated method stub

	}

}
