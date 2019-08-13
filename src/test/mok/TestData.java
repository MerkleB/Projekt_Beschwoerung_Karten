package test.mok;

import java.util.Hashtable;

import main.Card;
import main.Effect;
import main.GameAction;
import main.Spell;
import main.Summon;

public class TestData {
	public static Card getCard(String card_id) {
		Hashtable<String, Card> testCards = new Hashtable<>();
		
		GameAction[] mokActions = new GameAction[1];
		mokActions[0] = MokProvider.getGameAction();
		String name = "Aries";
		String id = "bsc-su-00-0";
		String type = "SUMMON";
		int maxEnergy = 5;
		int maxHealth = 8;
		String trivia = "Ein sagenhafter Widder der einst Phrixos und seine Schwester Helle vor ihrer Stiefmutter Ino rettete";
		Effect[] effects = new Effect[1];;
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
		testCards.put(id, new Summon(name, trivia, effects, magicPreservationValue, summoningPoints, attack, heal, maxVitality, summonClass, rank, element, magicWastageOnDefeat, maxEnergy, maxHealth, null, actions));
		
		name = "Aries";
		id = "bsc-su-00-1";
		type = "SUMMON";
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
		testCards.put(id, new Summon(name, trivia, effects, magicPreservationValue, summoningPoints, attack, heal, maxVitality, summonClass, rank, element, magicWastageOnDefeat, maxEnergy, maxHealth, null, actions));
		
		name = "Aries";
		id = "bsc-su-00-2";
		type = "SUMMON";
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
		testCards.put(id, new Summon(name, trivia, effects, magicPreservationValue, summoningPoints, attack, heal, maxVitality, summonClass, rank, element, magicWastageOnDefeat, maxEnergy, maxHealth, null, actions));
		
		name = "Diamond Storm";
		id = "bsc-su-01";
		type = "SPELL";
		maxEnergy = 5;
		maxHealth = 8;
		trivia = "Ein Zauber, der einen Sturm von Eiskristallen loslässt.";
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
		testCards.put(id, new Spell(name, trivia, effects, neededMagicEnergy, maxEnergy, maxHealth, null, actions));
		
		return testCards.get(card_id);
	}
}
