package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;

import main.Action.GameAction;
import main.Card.Summon;
import main.build_cards.KnowsSummonAscentHierarchy;
import main.util.RankLevelMapper;
import main.util.mapsRankAndLevel;
import test.mok.MokProvider;

public class SummonAscentHierarchyTest {
	
	private mapsRankAndLevel mapperMok;
	private static boolean mapperAlreadyMokked;
	private KnowsSummonAscentHierarchy cut;
	private Summon card1;
	private Summon card2;
	private Summon card3;
	
	@Before
	public void setUp() throws Exception {
		cut = main.build_cards.SummonAscentHierarchy.getInstance();
		mapperMok = MokProvider.getMapperMok(); 
		if(!mapperAlreadyMokked){
			mapperAlreadyMokked = true;
			Field mapperField;
			try {
				mapsRankAndLevel mapper = RankLevelMapper.getInstance();
				if(mapper instanceof RankLevelMapper) {
					mapperField = mapper.getClass().getDeclaredField("instance");
					//Inject mapperMok
					mapperField.setAccessible(true);
					mapperField.set(mapper, mapperMok);
				}
			} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				fail("Error in setup: "+e.getMessage());
			}
		}
		
		GameAction[] mokActions = new GameAction[1];
		mokActions[0] = MokProvider.getGameAction("MokAction");
		
		card1 = new Summon("bsc-su-00-0","Summon-0", "Test", null, 2, 1, 5, 1, 4, "NaturalBeast", "Cub", "Feuer", 1, 3, 4, MokProvider.getPlayer(), mokActions);
		card2 = new Summon("bsc-su-00-1","Summon-1", "Test", null, 2, 1, 5, 1, 4, "NaturalBeast", "Adult", "Feuer", 1, 3, 4, MokProvider.getPlayer(), mokActions);
		card3 = new Summon("bsc-su-00-2","Summon-2", "Test", null, 2, 1, 5, 1, 4, "NaturalBeast", "Legend", "Feuer", 1, 3, 4, MokProvider.getPlayer(), mokActions);
		
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
