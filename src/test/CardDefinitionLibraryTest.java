package test;

import static org.junit.Assert.*;

import java.lang.reflect.*;
import java.util.TreeMap;
import java.util.function.BiConsumer;

import org.junit.Before;
import org.junit.Test;

import main.jsonObjects.CardDefinition;
import main.jsonObjects.CardDefinitionLibrary;
import main.jsonObjects.EffectDefinition;
import main.jsonObjects.HoldsCardDefinitions;
import main.jsonObjects.SummonAscentHierarchyDefinition;

public class CardDefinitionLibraryTest {

	private HoldsCardDefinitions cut;
	private CardDefinition cardDefinition;
	private TreeMap<String, CardDefinition> referenceSet;
	
	@Before
	public void setUp() throws Exception {
		cut = CardDefinitionLibrary.getInstance();
		EffectDefinition ed = new EffectDefinition();
		ed.effectClass = "effect_000";
		EffectDefinition[] eds = {ed};
		SummonAscentHierarchyDefinition sahd1 = new SummonAscentHierarchyDefinition();
		sahd1.level = 1;
		sahd1.card_id = "bsc-su-00-1";
		SummonAscentHierarchyDefinition sahd2 = new SummonAscentHierarchyDefinition();
		sahd2.level = 2;
		sahd2.card_id = "bsc-su-00-2";
		SummonAscentHierarchyDefinition[] sahd = {sahd1, sahd2};
		
		cardDefinition = new CardDefinition(); 
		cardDefinition.name = "Aries";
		cardDefinition.card_id = "bsc-su-00-0";
		cardDefinition.type = "SUMMON";
		cardDefinition.maxEnergy = 5;
		cardDefinition.maxHealth = 8;
		cardDefinition.trivia = "Ein sagenhafter Widder der einst Phrixos und seine Schwester Helle vor ihrer Stiefmutter Ino rettete";
		cardDefinition.effects = eds;
		cardDefinition.magicPreservationValue = 2;
		cardDefinition.summoningPoints = 1;
		cardDefinition.attack = 2;
		cardDefinition.heal = 3;
		cardDefinition.maxVitality = 8;
		cardDefinition.summonClass = "NaturalBeast";
		cardDefinition.rank = "Cub";
		cardDefinition.summonHierarchy = sahd;
		cardDefinition.element = "Fire";
		cardDefinition.magicWastageOnDefeat = 1;
		cardDefinition.neededMagicEnergy = 0;
		
		referenceSet = new TreeMap<String, CardDefinition>();
		referenceSet.put(cardDefinition.card_id, cardDefinition);
	}

	@Test
	public void testGetCardDefinition() {
		try {
			setPathToTestResources();
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			fail(e.getMessage());
		}
		if(cut.getCardDefinition("bsc-su-00-0").equals(cardDefinition) == false) {
			fail("Retrieved CardDefinition does not equal the reference definition");
		}
		
	}

	@Test
	public void testGetCardSet() {
		try {
			setPathToTestResources();
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			fail(e.getMessage());
		}
		TreeMap<String, CardDefinition> retrievedSet = cut.getCardSet("basic_set");
		retrievedSet.forEach(new BiConsumer<String, CardDefinition>() {

			@Override
			public void accept(String t, CardDefinition u) {
				if(referenceSet.get(t).equals(u) == false) {
					fail("Retrieved set is not equal reference set.");
				}
			}
		});
		
	}
	
	public void setPathToTestResources() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String testPath = "test/testJSON/";
		Field pathField;
		pathField = cut.getClass().getDeclaredField("resourcePath");
		pathField.setAccessible(true);
		pathField.set(cut, testPath);
	}

}
