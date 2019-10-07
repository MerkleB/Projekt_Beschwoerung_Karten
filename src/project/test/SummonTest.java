package project.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import project.main.Action.Effect;
import project.main.Action.GameAction;
import project.main.Card.Summon;
import project.main.exception.NoCardException;
import project.test.mok.MokProvider;

public class SummonTest {

	private Effect[] effectDummies;
	private Summon cut;
	private GameAction[] mokActions;

	@Before
	public void setUp(){
		Effect effectDummy = MokProvider.getEffect();	

		effectDummies = new Effect[1];
		effectDummies[0] = effectDummy;
		mokActions = new GameAction[1];
		mokActions[0] = MokProvider.getGameAction("MokAction");
		cut = new Summon("bsc-su-00-0", "Summon-0", "Test", effectDummies, 2, 1, 5, 1, 4, "NaturalBeast", "Cub", "Feuer", 1, 3, 4, MokProvider.getPlayer(), mokActions);
	}
	
	@Test
	public void testGetTrivia() {
		if(!cut.getTrivia().equals("Test")){
			fail("Expected Trivia was not retrieved");
		}
	}

	@Test
	public void testGetEffects() {
		try {
			Effect[] effects = cut.getEffects();
			if(!effects[0].getDescription().equals("This is an effect.")) {
				fail("The expected effect was not retrieved.");
			}
		} catch (NoCardException e) {
			fail("Unexpected NoCardException");
		}
		
	}

	@Test
	public void testDecreaseVitality() {
		Summon lcut = new Summon("bsc-su-00-0", "Test Summon", "Test", effectDummies, 2, 1, 5, 1, 4, "Natürliche Bestie", "Junges", "Feuer", 1, 3, 4, MokProvider.getPlayer(), mokActions);
			lcut.getStatus().decreaseVitality(2);
			if(lcut.getStatus().getVitality() != 2) {
				fail("Decrease of vitality was not successful.");
			}
	}

	@Test
	public void testIncreaseVitality() {
		Summon lcut = new Summon("bsc-su-00-0", "Test Summon", "Test", effectDummies, 2, 1, 5, 1, 4, "Natürliche Bestie", "Junges", "Feuer", 1, 3, 4, MokProvider.getPlayer(), mokActions);
			lcut.getStatus().decreaseVitality(2);
			lcut.getStatus().increaseVitality(1);
			if(lcut.getStatus().getVitality() != 3) {
				fail("Increase of vitality was not successful.");
			}
			lcut.getStatus().increaseVitality(2);
			if(lcut.getStatus().getVitality() != 4) {
				fail("Viatility should not become more than max vitality");
			}
	}

}
