package project.test.mok;

import java.util.Hashtable;

import project.main.Action.GameAction;
import project.main.Card.Card;
import project.main.Card.MagicCollector;
import project.main.Card.Spell;
import project.main.Card.Summon;
import project.main.Effect.Effect;

public class TestData {
	public static Card getCard(String card_id) {
		Hashtable<String, Card> testCards = new Hashtable<>();
		
		
		String name = "Aries";
		String id = "bsc-su-00-0";
		String type = "Summon";
		int maxEnergy = 5;
		int maxHealth = 8;
		String trivia = "Ein sagenhafter Widder der einst Phrixos und seine Schwester Helle vor ihrer Stiefmutter Ino rettete";
		Effect[] effects = new Effect[1];
		GameAction[] mokActions = new GameAction[MokProvider.getActionDefinitions().getCardActions(type).size()];
		for(int i=0; i<MokProvider.getActionDefinitions().getCardActions(type).size(); i++) {
			mokActions[i] = MokProvider.getGameAction(MokProvider.getActionDefinitions().getCardActions(type).get(i));
		}
		effects[0] = MokProvider.getEffect();
		int magicPreservationValue = 2;
		int summoningPoints = 1;
		int attack = 2;
		int heal = 3;
		int maxVitality = 8;
		String summonClass = "NaturalBeast";
		String rank = "Cub";
		GameAction[] actions = mokActions;
		String element = "Fire";
		int magicWastageOnDefeat = 1;
		int neededMagicEnergy = 0;
		testCards.put(id, new Summon(id, name, trivia, effects, magicPreservationValue, summoningPoints, attack, heal, maxVitality, summonClass, rank, element, magicWastageOnDefeat, maxEnergy, maxHealth, null, actions));
		addActionsToCollector(testCards.get(id).getCollector());
		
		name = "Aries";
		id = "bsc-su-00-1";
		type = "Summon";
		maxEnergy = 5;
		maxHealth = 8;
		trivia = "Ein sagenhafter Widder der einst Phrixos und seine Schwester Helle vor ihrer Stiefmutter Ino rettete";
		magicPreservationValue = 2;
		summoningPoints = 1;
		attack = 3;
		heal = 6;
		maxVitality = 10;
		summonClass = "NaturalBeast";
		rank = "Adult";
		element = "Fire";
		magicWastageOnDefeat = 2;
		neededMagicEnergy = 0;
		testCards.put(id, new Summon(id, name, trivia, effects, magicPreservationValue, summoningPoints, attack, heal, maxVitality, summonClass, rank, element, magicWastageOnDefeat, maxEnergy, maxHealth, null, actions));
		addActionsToCollector(testCards.get(id).getCollector());
		
		name = "Aries";
		id = "bsc-su-00-2";
		type = "Summon";
		maxEnergy = 5;
		maxHealth = 8;
		trivia = "Ein sagenhafter Widder der einst Phrixos und seine Schwester Helle vor ihrer Stiefmutter Ino rettete";
		magicPreservationValue = 2;
		summoningPoints = 1;
		attack = 4;
		heal = 7;
		maxVitality = 12;
		summonClass = "NaturalBeast";
		rank = "Legend";
		element = "Fire";
		magicWastageOnDefeat = 3;
		neededMagicEnergy = 0;
		testCards.put(id, new Summon(id, name, trivia, effects, magicPreservationValue, summoningPoints, attack, heal, maxVitality, summonClass, rank, element, magicWastageOnDefeat, maxEnergy, maxHealth, null, actions));
		addActionsToCollector(testCards.get(id).getCollector());
		
		name = "Diamond Storm";
		id = "bsc-su-01";
		type = "Spell";
		maxEnergy = 5;
		maxHealth = 8;
		trivia = "Ein Zauber, der einen Sturm von Eiskristallen loslässt.";
		mokActions = new GameAction[MokProvider.getActionDefinitions().getCardActions(type).size()];
		for(int i=0; i<MokProvider.getActionDefinitions().getCardActions(type).size(); i++) {
			mokActions[i] = MokProvider.getGameAction(MokProvider.getActionDefinitions().getCardActions(type).get(i));
		}
		magicPreservationValue = 0;
		summoningPoints = 0;
		attack = 0;
		heal = 0;
		maxVitality = 0;
		summonClass = "";
		rank = "";
		element = "";
		magicWastageOnDefeat = 0;
		neededMagicEnergy = 5;
		testCards.put(id, new Spell(id, name, trivia, effects, neededMagicEnergy, maxEnergy, maxHealth, null, mokActions));
		addActionsToCollector(testCards.get(id).getCollector());
		
		return testCards.get(card_id);
	}
	
	private static void addActionsToCollector(MagicCollector collector) {
		GameAction[] actions = new GameAction[MokProvider.getActionDefinitions().getCardActions("MagicCollector").size()];
		for(int i=0; i<MokProvider.getActionDefinitions().getCardActions("MagicCollector").size(); i++) {
			actions[i] = MokProvider.getGameAction(MokProvider.getActionDefinitions().getCardActions("MagicCollector").get(i));
		}
		collector.setCollectorActions(actions);
	}
}
