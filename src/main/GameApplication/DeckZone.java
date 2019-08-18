package main.GameApplication;

import java.util.ArrayList;
import java.util.Collections;

import main.Card.Card;
import main.jsonObjects.ActionDefinitionLibrary;
import main.jsonObjects.HoldsActionDefinitions;
import main.util.ActionMatchFinder;

public class DeckZone extends GameZone {

	public DeckZone(Player owner, ArrayList<Card> deck) {
		super(owner);
		cardList = deck;
		shuffleDeck();
		for(Card card : cardList) {
			cardHash.put(card.getID(), card);
		}
	}

	@Override
	public String getName() {
		return "DeckZone";
	}
	
	public void shuffleDeck() {
		Collections.shuffle(cardList);
	}

	@Override
	public void activate(Player player, IsPhaseInGame gamePhase) {
		HoldsActionDefinitions library = ActionDefinitionLibrary.getInstance();
		ArrayList<String> zoneActions = library.getZoneActions(getName());
		ArrayList<String> phaseActions = gamePhase.getActionsToActivate();
		ArrayList<String> actionsToActivate =  ActionMatchFinder.getInstance().getMatchedActions(phaseActions, zoneActions);
		Card lastCard = cardList.get(cardList.size()-1);
		lastCard.setActiv(actionsToActivate, player);
	}
	
	

}
