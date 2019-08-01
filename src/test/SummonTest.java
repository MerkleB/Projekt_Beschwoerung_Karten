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
import main.Effect;
import main.MagicCollector;
import main.Summon;
import main.exception.NoCardException;
import main.exception.NoCollectorException;
import main.util.mapsRankAndLevel;

public class SummonTest {

	private Effect[] effectDummies;
	private Summon cut;
	private Summon card1;
	private Summon card2;
	private mapsRankAndLevel mapperMok;

	@Before
	public void setUp(){
		Effect effectDummy = new Effect(){
		
			@Override
			public void execute() {
				
			}
		
			@Override
			public boolean isExecutable() {
				return false;
			}
		
			@Override
			public String getDescription() {
				return "This is an effect.";
			}

			@Override
			public void getName() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void activate() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean activatable() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Card getOwningCard() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setOwningCard(Card owningCard) {
				// TODO Auto-generated method stub
				
			}
		};

		effectDummies = new Effect[1];
		effectDummies[0] = effectDummy;
		cut = new Summon("Test Summon", "Test", effectDummies, 2, 1, 5, 1, 4, "NaturalBeast", "Cub", "Feuer", 1, 3, 4);
		card1 = new Summon("Test Summon", "Test", effectDummies, 2, 1, 7, 1, 4, "NaturalBeast", "Adult", "Feuer", 1, 3, 4);
		card2 = new Summon("Test Summon", "Test", effectDummies, 2, 1, 9, 1, 4, "NaturalBeast", "Legend", "Feuer", 1, 3, 4);
		cut.addNextRankCard(card1);
		cut.addNextRankCard(card2);
		
		mapperMok = new mapsRankAndLevel() {
			
			@Override
			public int mapRankToLevel(String rank) {
				switch(rank) {
				case "Cub":
					return 0;
				case "Adult":
					return 1;
				case "Legend":
					return 2;
				}
				return -1;
			}
			
			@Override
			public String mapLevelToRank(int level, String summonClass) {
				String result = null;
				switch(summonClass) {
				case "NaturalBeast":
					switch(level) {
					case 0:
						result = "Cub";
						break;
					case 1:
						result =  "Adult";
						break;
					case 2:
						result =  "Legend";
						break;
					}
				}
				return result;
			}
		};
		
		//Inject mapperMok
		Field mapperField;
		try {
			mapperField = cut.getClass().getDeclaredField("rankLevelMapper");

			mapperField.setAccessible(true);
			mapperField.set(cut, mapperMok);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			fail("Error in setup: "+e.getMessage());
		} 
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
		Summon lcut = new Summon("Test Summon", "Test", effectDummies, 2, 1, 5, 1, 4, "Natürliche Bestie", "Junges", "Feuer", 1, 3, 4);
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
		Summon lcut = new Summon("Test Summon", "Test", effectDummies, 2, 1, 5, 1, 4, "Natürliche Bestie", "Junges", "Feuer", 1, 3, 4);
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

	@Test
	public void testCheckCollector() {
		boolean exceptionWasNotThrown = true;
		try {
			cut.decreaseCurrentHealth(2);
		} catch (NoCollectorException e) {
			exceptionWasNotThrown = false;
		}
		
		if(exceptionWasNotThrown) {
			fail("NoCollectorException was expected but not thrown");
		}
	}

	@Test
	public void testCheckCard() {
		try {
			cut.setAttack(10);
		} catch (NoCardException e) {
			fail("NoCardException was thrown but not expected");
		}
	}
	
	@Test
	public void testRemoveLevel() {
		fail("Not implemented");
	}
	
	@Test
	public void testNextRankCard() {
		fail("Not implemented");
	}
	
	@Test
	public void testNextLowerRankCard() {
		fail("Not implemented");
	}
	
	@Test
	public void testAddNextRankCard() {
		fail("Not implemented");
	}	
	
	@Test
	public void testSetCardForLevel() {
		fail("Not implemented");
	}
	
	@Test
	public void testgetCardForLevel() {
		fail("Not implemented");
	}

}
