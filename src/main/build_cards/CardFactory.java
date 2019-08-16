package main.build_cards;

import java.util.ArrayList;
import java.util.Hashtable;

import main.Action.Effect;
import main.Action.GameAction;
import main.Card.Card;
import main.Card.MagicCollector;
import main.Card.Spell;
import main.Card.Summon;
import main.exception.CardCreationException;
import main.exception.InvalidCardException;
import main.exception.NotAllowedCardException;
import main.jsonObjects.ActionDefinitionLibrary;
import main.jsonObjects.CardDefinition;
import main.jsonObjects.CardDefinitionLibrary;
import main.jsonObjects.HoldsActionDefinitions;
import main.jsonObjects.HoldsCardDefinitions;
import main.jsonObjects.SummonAscentHierarchyDefinition;

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
		checkCardIdIsAllowed(card_id);
		
		CardDefinition cardDefinition = cardLibrary.getCardDefinition(card_id);
		if(cardDefinition == null) throw new InvalidCardException(card_id+" does not exist.");
		
		Card card = null;
		switch(cardDefinition.type) {
		case "Summon":
			card = createCompleteSummon(cardDefinition);
			break;
		case "Spell":
			card = createSpell(cardDefinition);
			break;
		default:
			throw new CardCreationException("Not directly creatable card type: "+cardDefinition.type);
		}
		
		return card;
	}
	
	private Summon createCompleteSummon(CardDefinition definition) {
		Summon summon = createSummon(definition);
		setCollectorActions(summon.getCollector());
		summon.setSummonHierarchy(createSummonHierarchy(definition, summon));
		return summon;
	}
	
	private Summon createSummon(CardDefinition definition) {
		Effect[] effects = getEffects(definition); 
		GameAction[] actions = getActions(definition.type);
		Summon summon = new Summon(definition.name, definition.trivia, effects, definition.magicPreservationValue, definition.summoningPoints, definition.attack, definition.heal, definition.maxVitality, definition.summonClass, definition.rank, definition.element, definition.magicWastageOnDefeat, definition.maxEnergy, definition.maxHealth, null, actions);
		return summon;
	}
	
	private KnowsSummonAscentHierarchy createSummonHierarchy(CardDefinition definition, Summon summon) {
		KnowsSummonAscentHierarchy hierarchy = SummonAscentHierarchy.getInstance();
		hierarchy.addSummonToHierarchy(summon);
		for(SummonAscentHierarchyDefinition entry : definition.summonHierarchy) {
			try {
				checkCardIdIsAllowed(entry.card_id);
				CardDefinition successorCardDefinition = cardLibrary.getCardDefinition(entry.card_id);
				Summon successorSummon = createSummon(successorCardDefinition);
				setCollectorActions(successorSummon.getCollector());
				hierarchy.addSummonToHierarchy(successorSummon);
			} catch (NotAllowedCardException e) {
				System.out.println("CreateSummonHierarchy - Card of Level-"+entry.level+":"+e.getMessage());
			}
		}
		return hierarchy;
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
	
	private void checkCardIdIsAllowed(String card_id) throws NotAllowedCardException {
		if(allowedCards != null) {
			if(!allowedCards.containsKey(card_id)) {
				throw new NotAllowedCardException("Card with id "+card_id+" is not allowed.");
			}
		}
	}

}
