package project.main.GameApplication;

import java.util.ArrayList;
import java.util.Collections;

import project.main.Action.Action;
import project.main.Action.Draw;
import project.main.Card.Card;
import project.main.Card.Spell;
import project.main.jsonObjects.ActionDefinitionLibrary;
import project.main.jsonObjects.HoldsActionDefinitions;
import project.main.util.ActionMatchFinder;

public class DeckZone extends GameZone{
	
	private String name;
	
	public DeckZone(Player owner, ArrayList<Card> deck) {
		super(owner);
		cardList = deck;
		shuffleDeck();
		for(Card card : cardList) {
			cardHash.put(card.getID(), card);
		}
		name = "DeckZone";
	}

	@Override
	public String getName() {
		return name;
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
