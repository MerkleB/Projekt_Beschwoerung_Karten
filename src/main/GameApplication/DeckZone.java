package main.GameApplication;

import java.util.ArrayList;
import java.util.Collections;

import main.Action.Action;
import main.Action.Draw;
import main.Card.Card;
import main.Card.Spell;
import main.jsonObjects.ActionDefinitionLibrary;
import main.jsonObjects.HoldsActionDefinitions;
import main.util.ActionMatchFinder;

public class DeckZone extends GameZone{
	
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
		int lastIndex = cardList.size()-1;
		if(lastIndex != -1) {
			Card lastCard = cardList.get(lastIndex);
			lastCard.setActiv(actionsToActivate, player);
		}else {
			//Set a dummy card to have still a draw action
			Action[] actions = new Action[1];
			actions[0] = new Draw();
			Card dummyCard = new Spell("DUMMY", "Dummy", "", null, 0, 0, 0, player, actions);
			actions[0].setCard(dummyCard);
			dummyCard.setActiv(actionsToActivate, player);
		}
	}	

}
