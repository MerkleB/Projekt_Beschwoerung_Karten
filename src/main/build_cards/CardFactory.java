package main.build_cards;

import main.Card;
import main.jsonObjects.ActionDefinitionLibrary;
import main.jsonObjects.CardDefinitionLibrary;
import main.jsonObjects.HoldsActionDefinitions;
import main.jsonObjects.HoldsCardDefinitions;

public class CardFactory implements CreatesCards {
	
	private HoldsActionDefinitions actionLibrary;
	private HoldsCardDefinitions cardLibrary;
	
	public static CreatesCards getInstance() {
		CardFactory instance = new CardFactory();
		instance.actionLibrary = ActionDefinitionLibrary.getInstance();
		instance.cardLibrary = CardDefinitionLibrary.getInstance();
		return instance;
	}
	
	@Override
	public Card createCard(String card_id) {
		// TODO Auto-generated method stub
		return null;
	}

}
