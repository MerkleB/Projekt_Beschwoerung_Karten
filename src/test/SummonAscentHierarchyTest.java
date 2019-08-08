package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import main.CardGame;
import main.Game;
import main.Summon;
import main.build_cards.KnowsSummonAscentHierarchy;
import main.util.mapsRankAndLevel;

public class SummonAscentHierarchyTest {
	
	private mapsRankAndLevel mapperMok;
	private KnowsSummonAscentHierarchy cut;
	Summon card1;
	Summon card2;
	Summon card3;
	
	@Before
	public void setUp() throws Exception {
			cut = main.build_cards.SummonAscentHierarchy.getInstance();
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
		
		Field mapperField;
		try {
			Game game = CardGame.getInstance();
			mapperField = game.getClass().getDeclaredField("rankAndLevelMapper");
			//Inject mapperMok
			mapperField.setAccessible(true);
			mapperField.set(game, mapperMok);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			fail("Error in setup: "+e.getMessage());
		} 
		
		card1 = new Summon("Summon-0", "Test", null, 2, 1, 5, 1, 4, "NaturalBeast", "Cub", "Feuer", 1, 3, 4);
		card2 = new Summon("Summon-1", "Test", null, 2, 1, 5, 1, 4, "NaturalBeast", "Adult", "Feuer", 1, 3, 4);
		card3 = new Summon("Summon-2", "Test", null, 2, 1, 5, 1, 4, "NaturalBeast", "Legend", "Feuer", 1, 3, 4);
		
	}

	@Test
	public void testAddSummonToHierarchy() {
		cut.addSummonToHierarchy(card1);
		cut.addSummonToHierarchy(card2);
		cut.addSummonToHierarchy(card3);
		
		if(cut.getSummonOfLevel(0) != card1) {
			fail("Expected card1 for level 0 was not retrieved.");
		}
		if(cut.getSummonOfLevel(1) != card2) {
			fail("Expected card2 for level 1 was not retrieved.");
		}
		if(cut.getSummonOfLevel(2) != card3) {
			fail("Expected card3 for level 2 was not retrieved.");
		}
		
		if(card1.getSummonHierarchy() != cut) {
			fail("Hierarchy was not set card1");
		}
	}

	@Test
	public void testGetNextSummonInHierarchy() {
		cut.addSummonToHierarchy(card1);
		cut.addSummonToHierarchy(card2);
		cut.addSummonToHierarchy(card3);
		
		if(cut.getNextSummonInHierarchy(card1) != card2) {
			fail("Expected card2 was not retrieved as next level for card1");
		}
		if(cut.getNextSummonInHierarchy(card2) != card3) {
			fail("Expected card2 was not retrieved as next level for card1");
		}
		if(cut.getNextSummonInHierarchy(card3) != null) {
			fail("Expected null was not retrieved as next level for card1");
		}
	}
}
