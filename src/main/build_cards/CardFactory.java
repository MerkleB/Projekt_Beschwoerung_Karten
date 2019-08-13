package main.build_cards;

import java.util.Hashtable;

import main.Card;
import main.jsonObjects.ActionDefinitionLibrary;
import main.jsonObjects.CardDefinitionLibrary;
import main.jsonObjects.HoldsActionDefinitions;
import main.jsonObjects.HoldsCardDefinitions;

public class CardFactory implements CreatesCards {
	
	private HoldsActionDefinitions actionLibrary;
	private HoldsCardDefinitions cardLibrary;
	private CreatesActions actionFactory;
	private Hashtable<String, String> allowedCards;
	
	public static CreatesCards getInstance() {
		CardFactory instance = new CardFactory();
		instance.actionLibrary = ActionDefinitionLibrary.getInstance();
		instance.cardLibrary = CardDefinitionLibrary.getInstance();
		instance.actionFactory = GameActionFactory.getInstance();
		return instance;
	}
	
	public static CreatesCards getInstance(String[] allowedCards) {
		CardFactory instance = (CardFactory)getInstance();
		instance.allowedCards = new Hashtable<String,String>();
		for(String entry : allowedCards) {
			instance.allowedCards.put(entry, entry);
		}
		return instance;
	}
	
	@Override
	public Card createCard(String card_id) {
		return null;
	}

}
