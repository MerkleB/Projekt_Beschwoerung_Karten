package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.Effect.AccelType;

import main.Card;
import main.CardGame;
import main.Effect;
import main.Game;
import main.GameAction;
import main.MagicCollector;
import main.Summon;
import main.exception.NoCardException;
import main.exception.NoCollectorException;
import main.util.mapsRankAndLevel;
import test.mok.MokProvider;

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
		cut = new Summon("Summon-0", "Test", effectDummies, 2, 1, 5, 1, 4, "NaturalBeast", "Cub", "Feuer", 1, 3, 4, MokProvider.getPlayer(), mokActions);
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
		Summon lcut = new Summon("Test Summon", "Test", effectDummies, 2, 1, 5, 1, 4, "Natürliche Bestie", "Junges", "Feuer", 1, 3, 4, MokProvider.getPlayer(), mokActions);
		try {
			lcut.decreaseVitality(2);
			if(lcut.getVitality() != 2) {
				fail("Decrease of vitality was not successful.");
			}
		} catch (NoCardException e) {
			fail("Unexpected NoCardException");
		}
	}

	@Test
	public void testIncreaseVitality() {
		Summon lcut = new Summon("Test Summon", "Test", effectDummies, 2, 1, 5, 1, 4, "Natürliche Bestie", "Junges", "Feuer", 1, 3, 4, MokProvider.getPlayer(), mokActions);
		try {
			lcut.decreaseVitality(2);
			lcut.increaseVitality(1);
			if(lcut.getVitality() != 3) {
				fail("Increase of vitality was not successful.");
			}
			lcut.increaseVitality(2);
			if(lcut.getVitality() != 4) {
				fail("Viatility should not become more than max vitality");
			}
		} catch (NoCardException e) {
			fail("Unexpected NoCardException");
		}
	}

}
