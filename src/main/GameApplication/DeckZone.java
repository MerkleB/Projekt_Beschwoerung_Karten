package main.GameApplication;

import java.util.ArrayList;
import java.util.Collections;

import javax.print.attribute.standard.MediaSize.Other;

import main.Card.Card;
import main.jsonObjects.ActionDefinitionLibrary;
import main.jsonObjects.HoldsActionDefinitions;
import main.util.ActionMatchFinder;

public class DeckZone extends GameZone implements AcceptPromptAnswers {

	private Player promptedPlayer;
	
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
			promptedPlayer = player;
			game.prompt(player, "#1-You have no cards to draw. "
																	+"You either can loose 1 HP or pay 5 HP to draw a card from Discard pile."
																	+"\r\nWhat do you want to do?"
																	+"\r\n=damage;pay", this);
			promptedPlayer = null;
		}
	}

	@Override
	public void accept(String answer) {
		switch(answer) {
		case "damage":
			promptedPlayer.decreaseHealthPoints(1);
			break;
		case "pay":
			promptedPlayer.decreaseHealthPoints(5);
			DiscardPile discardPile = (DiscardPile) promptedPlayer.getGameZone("DiscardPile");
			@SuppressWarnings("unchecked") 
			ArrayList<Card> cards = (ArrayList<Card>) discardPile.getCards().clone();
			Collections.shuffle(cards);
			Card drawnCard = cards.get(cards.size()-1);
			discardPile.removeCard(drawnCard);
			promptedPlayer.getGameZone("HandZone").addCard(drawnCard);
			break;
		default:
			System.out.println("Invalid command.");
			game.prompt(promptedPlayer, "#1-You have no cards to draw. "
					+"You either can loose 1 HP or pay 5 HP to draw a card from Discard pile."
					+"\r\nWhat do you want to do?"
					+"\r\n=damage;pay", this);
		}
	}
	
	

}
