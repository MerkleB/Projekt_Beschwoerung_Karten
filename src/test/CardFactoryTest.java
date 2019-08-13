package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;

import main.Card;
import main.Spell;
import main.Summon;
import main.build_cards.CardFactory;
import main.build_cards.CreatesCards;
import main.build_cards.KnowsSummonAscentHierarchy;
import main.exception.InvalidCardException;
import main.exception.NotAllowedCardException;
import test.mok.MokProvider;
import test.mok.TestData;

public class CardFactoryTest {
	
	CreatesCards cut;
	
	@Before
	public void setUp() throws Exception {
		cut = CardFactory.getInstance();
				
		Field actionLibraryField = cut.getClass().getDeclaredField("actionLibrary");
		Field cardLibraryField = cut.getClass().getDeclaredField("cardLibrary");
		Field actionFactoryField = cut.getClass().getDeclaredField("actionFactory");
		actionLibraryField.setAccessible(true);
		actionLibraryField.set(cut, MokProvider.getActionDefinitions());
		cardLibraryField.setAccessible(true);
		cardLibraryField.set(cut, MokProvider.getCardDefinitions());
		actionFactoryField.setAccessible(true);
		actionFactoryField.set(cut, MokProvider.getActionFactoryMok());
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
		}
	}
	
	@Test
	public void testCreateSpell() {
		boolean exceptionAppeared = false;
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
		}
	}
	
	@Test
	public void testCreateNonExistingCard() {
		String card_id = "bsc-su-00-0";
		boolean exceptionAppeared = false;
		try {
			Card summon = cut.createCard(card_id);
		}catch(InvalidCardException e) {
			exceptionAppeared = true;
		} catch (NotAllowedCardException e) {
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
		}
	}
	
	@Test
	public void testCreateWithNoHierarchy() {
		String card_id = "bsc-su-00-0";
		String[] allowedCards = {card_id};
		cut = CardFactory.getInstance(allowedCards);
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
		}
	}
	
	@Test
	public void testCreateNotAllowedCard() {
		String card_id = "bsc-su-00-0";
		String[] allowedCards = {card_id};
		cut = CardFactory.getInstance(allowedCards);
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
		}
	}

}
