package project.main.GameApplication;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import project.main.Card.Card;
import project.main.Listeners.GameListener;
import project.main.exception.NoCardException;
import project.main.jsonObjects.ActionDefinitionLibrary;
import project.main.jsonObjects.HoldsActionDefinitions;
import project.main.util.ActionMatchFinder;

public abstract class GameZone implements IsAreaInGame {
	
	protected ArrayList<Card> cardList;
	protected Hashtable<UUID, Card> cardHash;
	protected Player owner;
	protected Game game;
	
	public GameZone(Player owner) {
		cardList = new ArrayList<Card>();
		cardHash = new Hashtable<UUID, Card>();
		this.owner = owner;
	}
	
	@Override
	public Card findCard(UUID id) {
		return cardHash.get(id);
	}

	@Override
	public ArrayList<Card> getCards() {
		return cardList;
	}

	@Override
	public void addCard(Card card) {	
		if(card == null) return;
		if(!cardHash.containsKey(card.getID())) {
			try {
				card.setOwningPlayer(owner);
				cardHash.put(card.getID(), card);
				cardList.add(card);
			} catch (NoCardException e) {
				System.out.println("Abort adding of card. Reason: "+e.getMessage());
			}
			GameListener.getInstance().cardAdded(this, card);
		}
	}

	@Override
	public void removeCard(Card card) {
		int indexForRemoval = -1;
		for(int i=0; i<cardList.size(); i++) {
			if(cardList.get(i) == card) {
				indexForRemoval = i;
				break;
			}
		}
		if(indexForRemoval > -1) cardList.remove(indexForRemoval);
		cardHash.remove(card.getID());
		GameListener.getInstance().cardRemoved(this, card);
	}

	@Override
	public void removeCard(UUID id) {
		Card cardToRemove = cardHash.get(id);
		if(cardToRemove != null) {
			removeCard(cardToRemove);
		}
	}

	@Override
	public void activate(Player player, IsPhaseInGame gamePhase) {
		HoldsActionDefinitions library = ActionDefinitionLibrary.getInstance();
		ArrayList<String> zoneActions = library.getZoneActions(getName());
		ArrayList<String> phaseActions = gamePhase.getActionsToActivate();
		ArrayList<String> actionsToActivate =  ActionMatchFinder.getInstance().getMatchedActions(phaseActions, zoneActions);
		for(Card card : cardList) {
			card.setActiv(actionsToActivate, player);
		}
	}

	@Override
	public void deavtivateAll() {
		for(Card card : cardList) {
			card.setInactive();
		}
	}

	@Override
	public Player getOwner() {
		return owner;
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
