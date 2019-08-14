package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;

import main.Card;
import main.CardGame;
import main.Game;
import main.Spell;
import main.Summon;
import main.build_cards.CardFactory;
import main.build_cards.CreatesCards;
import main.build_cards.KnowsSummonAscentHierarchy;
import main.exception.CardCreationException;
import main.exception.InvalidCardException;
import main.exception.NotAllowedCardException;
import main.util.RankLevelMapper;
import main.util.mapsRankAndLevel;
import test.mok.MokProvider;
import test.mok.TestData;

public class CardFactoryTest {
	
	private CreatesCards cut;
	private static boolean mapperAlreadyMokked;
	
	@Before
	public void setUp() throws Exception {
		cut = CardFactory.getInstance();
		mokFields((CardFactory)cut);		
	}
	
	private void mokFields(CardFactory factory) {
		try {
			Field actionLibraryField = factory.getClass().getDeclaredField("actionLibrary");
			Field cardLibraryField = factory.getClass().getDeclaredField("cardLibrary");
			Field actionFactoryField = factory.getClass().getDeclaredField("actionFactory");
			Field effectFactoryField = factory.getClass().getDeclaredField("effectFactory");
			actionLibraryField.setAccessible(true);
			actionLibraryField.set(factory, MokProvider.getActionDefinitions());
			cardLibraryField.setAccessible(true);
			cardLibraryField.set(factory, MokProvider.getCardDefinitions());
			actionFactoryField.setAccessible(true);
			actionFactoryField.set(factory, MokProvider.getActionFactoryMok());
			effectFactoryField.setAccessible(true);
			effectFactoryField.set(factory, MokProvider.getEffectFactory());

			if(!mapperAlreadyMokked) {
				mapperAlreadyMokked = true;
				mapsRankAndLevel mapper = RankLevelMapper.getInstance();
				Field mapperField = mapper.getClass().getDeclaredField("instance");
				//Inject mapperMok
				mapperField.setAccessible(true);
				mapperField.set(mapper, MokProvider.getMapperMok());
			}
		}catch(Exception e) {
			fail("Moking failed.");
		}
	}

	@Test
	public void testCreateSummon() {
		String card_id = "bsc-su-00-0";
		try {
			Card summon = cut.createCard(card_id);
			if((summon instanceof Summon) == false) {
				fail("Wrong type of card was retrieved");
			}
			Summon s = (Summon) summon;
			if(summon == null) fail("Null was retrieved instead of summon");
			if(!s.equals(TestData.getCard(card_id))) {
				fail("Wrong card was created!");
			}
			KnowsSummonAscentHierarchy summonHierarchy = s.getSummonHierarchy();
			Summon s_lvl_1 = summonHierarchy.getNextSummonInHierarchy(s);
			if(!s_lvl_1.equals(TestData.getCard("bsc-su-00-1"))) {
				fail("Wrong card for level 1 in hierarchy!");
			}
			
			Summon s_lvl_2 = summonHierarchy.getNextSummonInHierarchy(s_lvl_1);
			if(!s_lvl_2.equals(TestData.getCard("bsc-su-00-2"))) {
				fail("Wrong card for level 2 in hierarchy!");
			}
		}catch(InvalidCardException | NotAllowedCardException e) {
			fail("Unexpected exception");
		} catch (CardCreationException e) {
			fail("Unexpected exception");
		}
	}
	
	@Test
	public void testCreateSpell() {
		try {
			String card_id = "bsc-su-01";
			Spell spell = (Spell)cut.createCard(card_id);
			if((spell instanceof Spell)==false) {
				fail("Wrong type of card was retrieved");
			}
			if(!spell.equals(TestData.getCard(card_id))){
				fail("Wrong card was created!");
			}
		}catch(InvalidCardException | NotAllowedCardException e) {
			fail("Unexpected exception");
		} catch (CardCreationException e) {
			fail("Unexpected exception");
		}
	}
	
	@Test
	public void testCreateNonExistingCard() {
		String card_id = "bla-su-00-0";
		boolean exceptionAppeared = false;
		try {
			cut.createCard(card_id);
		}catch(InvalidCardException e) {
			exceptionAppeared = true;
		} catch (NotAllowedCardException e) {
			fail("Unexpected exception");
		} catch (CardCreationException e) {
			fail("Unexpected exception");
		}
		
		if(!exceptionAppeared) {
			fail("Expected exception was not thrown");
		}
	}
	
	@Test
	public void testCreateWithLevel1Hierarchy() {
		String card_id = "bsc-su-00-0";
		String[] allowedCards = {card_id,"bsc-su-00-1"};
		cut = CardFactory.getInstance(allowedCards);
		mapperAlreadyMokked = false;
		mokFields((CardFactory)cut);
		try {
			Card summon = cut.createCard(card_id);
			if((summon instanceof Summon) == false) {
				fail("Wrong type of card was retrieved");
			}
			Summon s = (Summon) summon;
			if(!s.equals(TestData.getCard(card_id))) {
				fail("Wrong card was created!");
			}
			KnowsSummonAscentHierarchy summonHierarchy = s.getSummonHierarchy();
			Summon s_lvl_1 = summonHierarchy.getNextSummonInHierarchy(s);
			if(!s_lvl_1.equals(TestData.getCard("bsc-su-00-1"))) {
				fail("Wrong card for level 1 in hierarchy!");
			}
			
			Summon s_lvl_2 = summonHierarchy.getNextSummonInHierarchy(s_lvl_1);
			if(s_lvl_2 != null) {
				fail("Level 2 in hierarchy should be NULL.");
			}
		}catch(InvalidCardException | NotAllowedCardException e) {
			fail("Unexpected exception");
		} catch (CardCreationException e) {
			fail("Unexpected exception");
		}
	}
	
	@Test
	public void testCreateWithNoHierarchy() {
		String card_id = "bsc-su-00-0";
		String[] allowedCards = {card_id};
		cut = CardFactory.getInstance(allowedCards);
		mapperAlreadyMokked = false;
		mokFields((CardFactory)cut);
		try {
			Card summon = cut.createCard(card_id);
			if((summon instanceof Summon) == false) {
				fail("Wrong type of card was retrieved");
			}
			Summon s = (Summon) summon;
			if(!s.equals(TestData.getCard(card_id))) {
				fail("Wrong card was created!");
			}
			KnowsSummonAscentHierarchy summonHierarchy = s.getSummonHierarchy();
			
			Summon s_lvl_1 = summonHierarchy.getNextSummonInHierarchy(s);
			if(s_lvl_1 != null) {
				fail("Level 1 in hierarchy should be NULL.");
			}
		}catch(InvalidCardException | NotAllowedCardException e) {
			fail("Unexpected exception");
		} catch (CardCreationException e) {
			fail("Unexpected exception");
		}
	}
	
	@Test
	public void testCreateNotAllowedCard() {
		String card_id = "bsc-su-00-0";
		String[] allowedCards = {};
		cut = CardFactory.getInstance(allowedCards);
		mapperAlreadyMokked = false;
		mokFields((CardFactory)cut);
		boolean exceptionWasThrown = false;
		try {
			cut.createCard(card_id);
		}catch(NotAllowedCardException e) {
			exceptionWasThrown = true;
		} catch (InvalidCardException e) {
			fail("Unexpected exception");
		} catch (CardCreationException e) {
			fail("Unexpected exception");
		}
		
		if(!exceptionWasThrown) {
			fail("Expected NotAllowedCardException was not thrown");
		}
	}

}
