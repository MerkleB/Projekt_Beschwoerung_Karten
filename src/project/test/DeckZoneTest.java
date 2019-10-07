package project.test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import project.main.Action.GameAction;
import project.main.Card.Card;
import project.main.Card.Spell;
import project.main.Card.Summon;
import project.main.GameApplication.DeckZone;
import project.main.GameApplication.HandZone;
import project.main.GameApplication.IsPhaseInGame;
import project.main.GameApplication.Player;
import project.main.exception.NoCardException;
import project.main.jsonObjects.ActionDefinitionLibrary;
import project.main.jsonObjects.HoldsActionDefinitions;
import project.test.mok.MokProvider;
import project.test.mok.TestData;

/**
 * Code implemented in super class GameZone is already tested in UnitTest HandZoneTest
 * @author Benjamin
 *
 */
public class DeckZoneTest {
	
	private DeckZone cut;
	private Summon summon;
	private Summon summon1;
	private Summon summon2;
	private Summon summon3;
	private Spell spell;
	private Spell spell1;
	private Spell spell2;
	private Spell spell3;
	private Player owner;
	private UUID spellID;
	private UUID summonID;
	private static boolean libraryAlreadyMokked;
	private static HoldsActionDefinitions realLibrary;

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		try {
			Field instanceField = realLibrary.getClass().getDeclaredField("instance");
			instanceField.setAccessible(true);
			instanceField.set(null, realLibrary);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			fail("Error in teardown. Subsequent test may be affected.");
		}
	}

	@Before
	public void setUp() throws Exception {
		owner = MokProvider.getPlayer();
		summon = (Summon)TestData.getCard("bsc-su-00-1");
		summon1 = (Summon)TestData.getCard("bsc-su-00-1");
		summon2 = (Summon)TestData.getCard("bsc-su-00-1");
		summon3 = (Summon)TestData.getCard("bsc-su-00-1");
		spell = (Spell)TestData.getCard("bsc-su-01");
		spell1 = (Spell)TestData.getCard("bsc-su-01");
		spell2 = (Spell)TestData.getCard("bsc-su-01");
		spell3 = (Spell)TestData.getCard("bsc-su-01");
		try {
			ArrayList<Card> deck = new ArrayList<Card>();
			spell.setOwningPlayer(owner);
			spellID = UUID.randomUUID();
			spell.setID(spellID);
			spell1.setOwningPlayer(owner);
			spellID = UUID.randomUUID();
			spell1.setID(spellID);
			spell2.setOwningPlayer(owner);
			spellID = UUID.randomUUID();
			spell2.setID(spellID);
			spell3.setOwningPlayer(owner);
			spellID = UUID.randomUUID();
			spell3.setID(spellID);
			summon.setOwningPlayer(owner);
			summonID = UUID.randomUUID();
			summon.setID(summonID);
			summon1.setOwningPlayer(owner);
			summonID = UUID.randomUUID();
			summon1.setID(summonID);
			summon2.setOwningPlayer(owner);
			summonID = UUID.randomUUID();
			summon2.setID(summonID);
			summon3.setOwningPlayer(owner);
			summonID = UUID.randomUUID();
			summon3.setID(summonID);
			deck.add(spell);
			deck.add(spell1);
			deck.add(spell2);
			deck.add(spell3);
			deck.add(summon);
			deck.add(summon1);
			deck.add(summon2);
			deck.add(summon3);
			cut = new DeckZone(owner, deck);
			if(!libraryAlreadyMokked) {
				libraryAlreadyMokked = true;
				HoldsActionDefinitions library = ActionDefinitionLibrary.getInstance();
				realLibrary = library;
				try {
					Field libraryInstance = library.getClass().getDeclaredField("instance");
					libraryInstance.setAccessible(true);
					HoldsActionDefinitions libraryMok = MokProvider.getActionDefinitions();
					libraryInstance.set(null, libraryMok);
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					fail("Error in setUp (mokking): "+e.getMessage());
				}
			}
			
		} catch (NoCardException e) {
			fail("Error in setUp: "+e.getMessage());
		}
		
	}

	@Test
	public void testActivate() {
		IsPhaseInGame phase = MokProvider.getGamePhase("DrawPhase");
		cut.activate(owner, phase);
		ArrayList<Card> cards = cut.getCards();
		int indexOfLastCard = cards.size()-1;
		Field activField;
		try {
			for(int i=0; i<cards.size(); i++) {
				ArrayList<GameAction> actions = cards.get(i).getActions();
				if(i == indexOfLastCard) {
					for(GameAction action : actions) {
						activField = action.getClass().getDeclaredField("activ");
						activField.setAccessible(true);
						if(activField.getBoolean(action) == true) {
							if(action.getName().equals("Draw") == false) {
								fail("Wrong action was set activ: "+action.getName());
							}
						}else if(action.getName().equals("Draw")) {
							fail("Action Draw was not set activ in last card");
						}
					}
				}else {
					for(GameAction action : actions) {
						activField = action.getClass().getDeclaredField("activ");
						activField.setAccessible(true);
						if(activField.getBoolean(action) == true) {
							fail("Action in other card than last card was set activ: "+action.getName());
						}
					}
				}
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			fail("Reflection error in activate test");
		}
	}

	@Test
	public void testShuffleDeck() {
		ArrayList<Card> deck = cut.getCards();
		UUID[] ids = new UUID[deck.size()];
		int i = 0;
		for(Card card : deck) {
			ids[i] = card.getID();
			i++;
		}
		cut.shuffleDeck();
		deck = cut.getCards();
		boolean cardsAreOnSamePosition = true;
		for(i=0; i<deck.size(); i++) {
			Card card = deck.get(i);
			if(card.getID() != ids[i]) {
				cardsAreOnSamePosition = false;
			}
			ids[i] = card.getID();
		}
		if(cardsAreOnSamePosition) {
			fail("In first try cards are still in same position as before after shuffle");
		}
		
		cut.shuffleDeck();
		deck = cut.getCards();
		cardsAreOnSamePosition = true;
		for(i=0; i<deck.size(); i++) {
			Card card = deck.get(i);
			if(card.getID() != ids[i]) {
				cardsAreOnSamePosition = false;
			}
			ids[i] = card.getID();
		}
		if(cardsAreOnSamePosition) {
			fail("In second try cards are still in same position as before after shuffle");
		}
		
		cut.shuffleDeck();
		deck = cut.getCards();
		cardsAreOnSamePosition = true;
		for(i=0; i<deck.size(); i++) {
			Card card = deck.get(i);
			if(card.getID() != ids[i]) {
				cardsAreOnSamePosition = false;
			}
			ids[i] = card.getID();
		}
		if(cardsAreOnSamePosition) {
			fail("In third try cards are still in same position as before after shuffle");
		}
	}

}
