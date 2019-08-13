package main.build_cards;

import java.util.ArrayList;
import java.util.Hashtable;

import main.Card;
import main.Effect;
import main.GameAction;
import main.MagicCollector;
import main.Spell;
import main.Summon;
import main.exception.CardCreationException;
import main.exception.InvalidCardException;
import main.exception.NotAllowedCardException;
import main.jsonObjects.ActionDefinitionLibrary;
import main.jsonObjects.CardDefinition;
import main.jsonObjects.CardDefinitionLibrary;
import main.jsonObjects.HoldsActionDefinitions;
import main.jsonObjects.HoldsCardDefinitions;

public class CardFactory implements CreatesCards {
	
	private HoldsActionDefinitions actionLibrary;
	private HoldsCardDefinitions cardLibrary;
	private CreatesActions actionFactory;
	private CreatesEffects effectFactory;
	private Hashtable<String, String> allowedCards;
	
	public static CreatesCards getInstance() {
		CardFactory instance = new CardFactory();
		instance.actionLibrary = ActionDefinitionLibrary.getInstance();
		instance.cardLibrary = CardDefinitionLibrary.getInstance();
		instance.actionFactory = GameActionFactory.getInstance();
		instance.effectFactory = EffectFactory.getInstance();
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
	public Card createCard(String card_id) throws NotAllowedCardException, InvalidCardException, CardCreationException {
		if(allowedCards != null) {
			if(allowedCards.containsKey(card_id)) throw new NotAllowedCardException(card_id+" is not in the list of allowed cards.");
		}
		
		CardDefinition cardDefinition = cardLibrary.getCardDefinition(card_id);
		if(cardDefinition == null) throw new InvalidCardException(card_id+" does not exist.");
		
		Card card = null;
		switch(cardDefinition.type) {
		case "Summon":
			card = createSummon(cardDefinition);
			break;
		case "Spell":
			card = createSpell(cardDefinition);
			break;
		default:
			throw new CardCreationException("Not directly creatable card type: "+cardDefinition.type);
		}
		
		return card;
	}
	
	private Summon createSummon(CardDefinition definition) {
		Effect[] effects = getEffects(definition); 
		GameAction[] actions = getActions(definition.type);
		Summon summon = new Summon(definition.name, definition.trivia, effects, definition.magicPreservationValue, definition.summoningPoints, definition.attack, definition.heal, definition.maxVitality, definition.summonClass, definition.rank, definition.element, definition.magicWastageOnDefeat, definition.maxEnergy, definition.maxHealth, null, actions);
		setCollectorActions(summon.getCollector());
		return summon;
	}
	
	private Spell createSpell(CardDefinition definition) {
		Effect[] effects = getEffects(definition); 
		GameAction[] actions = getActions(definition.type);
		Spell spell = new Spell(definition.name, definition.trivia, effects, definition.neededMagicEnergy, definition.maxEnergy, definition.maxHealth, null, actions);
		setCollectorActions(spell.getCollector());
		return spell;
	}
	
	private void setCollectorActions(MagicCollector collector) {
		GameAction[] actions = getActions("MagicCollector");
		collector.setCollectorActions(actions);
	}
	
	private Effect[] getEffects(CardDefinition definition) {
		Effect[] effects = new Effect[definition.effects.length];
		for(int i=0; i<definition.effects.length; i++) {
			effects[i] = effectFactory.createEffect(definition.effects[i].effectClass);
		}
		return effects;
	}
	
	private GameAction[] getActions(String cardType) {
		ArrayList<String> actionNames = actionLibrary.getCardActions(cardType);
		GameAction[] actions = new GameAction[actionNames.size()];
		for(int i=0; i<actionNames.size(); i++) {
			actions[i] = actionFactory.createAction(actionNames.get(i));
		}
		return actions;
	}

}
