package test;

import static org.junit.Assert.*;

import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import main.Action.GameAction;
import main.Card.Card;
import main.Card.Spell;
import main.Card.Summon;
import main.GameApplication.HandZone;
import main.GameApplication.IsPhaseInGame;
import main.GameApplication.Player;
import main.exception.NoCardException;
import main.jsonObjects.ActionDefinitionLibrary;
import main.jsonObjects.HoldsActionDefinitions;
import test.mok.MokProvider;
import test.mok.TestData;

public class HandZoneTest {
	
	private HandZone cut;
	private Summon summon;
	private Spell spell;
	private Player owner;
	private UUID spellID;
	private UUID summonID;
	private static boolean libraryAlreadyMokked;
	private static HoldsActionDefinitions realLibrary;
	
	@Before
	public void setUp() {
		owner = MokProvider.getPlayer();
		cut = new HandZone(owner);
		summon = (Summon)TestData.getCard("bsc-su-00-1");
		spell = (Spell)TestData.getCard("bsc-su-01");
		try {
			spell.setOwningPlayer(owner);
			spellID = UUID.randomUUID();
			spell.setID(spellID);
			summon.setOwningPlayer(owner);
			summonID = UUID.randomUUID();
			summon.setID(summonID);
			cut.addCard(spell);
			cut.addCard(summon);
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
	
	@AfterClass
	public static void teardown() {
		try {
			Field instanceField = realLibrary.getClass().getDeclaredField("instance");
			instanceField.setAccessible(true);
			instanceField.set(null, realLibrary);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			fail("Error in teardown. Subsequent test may be affected.");
		}
	}
	
	@Test
	public void testGetCards() {
		ArrayList<Card> cards = cut.getCards();
		if(cards.get(0) != spell) {
			fail("Expected spell was not retrieved.");
		}
		if(cards.get(1) != summon) {
			fail("Expected summon was not retrieved");
		}
	}

	@Test
	public void testFindCard() {
		Card card = cut.findCard(summonID);
		if(card != summon) {
			fail("Expected summon was not retrieved");
		}
	}

	@Test
	public void testAddCard() {
		Summon newSummon = (Summon)TestData.getCard("bsc-su-00-1");
		try {
			newSummon.setOwningPlayer(owner);
		} catch (NoCardException e) {
			fail("Unexpected NoCardException");
		}
		UUID id = UUID.randomUUID();
		newSummon.setID(id);
		cut.addCard(newSummon);
		if(cut.getCards().get(2) != newSummon) {
			fail("Expected newSummon was not retrieved");
		}
		if(cut.findCard(id) != newSummon) {
			fail("Expected newSummon was not retrieved from hash");
		}
	}
	
	@Test
	public void testAddCardWithExistingID() {
		Summon newSummon = (Summon)TestData.getCard("bsc-su-00-1");
		try {
			newSummon.setOwningPlayer(owner);
		} catch (NoCardException e) {
			fail("Unexpected NoCardException");
		}
		UUID id = UUID.randomUUID();
		newSummon.setID(id);
		if(cut.findCard(id) != null) {
			fail("Error: Same id should not be in hashtable");
		}
		if(cut.getCards().size() == 3) {
			fail("newSummon should not be added to cardList because of already existing id");
		}
	}

	@Test
	public void testRemoveCardCard() {
		UUID id = spell.getID();
		cut.removeCard(spell);
		if(cut.findCard(id) != null) {
			fail("Removed spell could still be found");
		}
		if(cut.getCards().get(0) == spell) {
			fail("Removed spell could still be found");
		}
	}

	@Test
	public void testRemoveCardUUID() {
		UUID id = spell.getID();
		cut.removeCard(id);
		if(cut.findCard(id) != null) {
			fail("Removed spell could still be found");
		}
		if(cut.getCards().get(0) == spell) {
			fail("Removed spell could still be found");
		}
	}

	@Test
	public void testActivate() {
		IsPhaseInGame phase = MokProvider.getGamePhase();
		cut.activate(owner, phase);
		ArrayList<Card> cards = cut.getCards();
		ArrayList<String> controlList = getListOfActionsWichShouldBeActiv(phase.getName());
		boolean allActionsHaveCorrectActivValue = true;
		for(Card card : cards) {
			if(allActionsAreCorrectlySetInCard(card, controlList) == false) {
				allActionsHaveCorrectActivValue = false;
			}
		}
		
		if(!allActionsHaveCorrectActivValue) {
			fail("Actions were not correctly activated.");
		}
	}
	
	private ArrayList<String> getListOfActionsWichShouldBeActiv(String phase){
		ArrayList<String> activActions = new ArrayList<String>();
		ArrayList<String> phaseActions = MokProvider.getActionDefinitions().getPhaseActions(phase);
		ArrayList<String> zoneActions = MokProvider.getActionDefinitions().getZoneActions("HandZone");
		
		for(String phaseAction : phaseActions) {
			for(String zoneAction : zoneActions) {
				if(phaseAction.equals(zoneAction)) {
					activActions.add(phaseAction);
				}
			}
		}
		return activActions;
	}
	
	private boolean allActionsAreCorrectlySetInCard(Card card, ArrayList<String> controlList) {
		for(GameAction action : card.getActions()) {
			if(fieldIsCorrectlySetInAction(action, controlList) == false) return false;
		}
		return true;
	}
	
	private boolean fieldIsCorrectlySetInAction(GameAction action, ArrayList<String> controlList) {
		Field activField;
		try {
			activField = action.getClass().getDeclaredField("activ");
			activField.setAccessible(true);
			boolean isActive = activField.getBoolean(action);
			if(isActive) {
				for(String controlEntry : controlList) {
					if(action.getName().equals(controlEntry)) {
						return true;
					}
				}
			}else {
				for(String controlEntry : controlList) {
					if(action.getName().equals(controlEntry)) {
						return false;
					}
				}
			}
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			fail("ReflectionError in testActivate");
		}
		return true; //not activ but this is correct
	}

	@Test
	public void testDeavtivateAll() {
		IsPhaseInGame phase = MokProvider.getGamePhase();
		cut.activate(owner, phase);
		cut.deavtivateAll();
		boolean allCardsAreInactiv = true;
		for(Card card : cut.getCards()) {
			boolean allInactiv = true;
			for(GameAction action : card.getActions()) {
				Field activField;
				try {
					activField = action.getClass().getDeclaredField("activ");
					activField.setAccessible(true);
					boolean isActive = activField.getBoolean(action);
					if(isActive) {
						allInactiv = false;
					}
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					fail("ReflectionError in testActivate");
				}
				
			}
			if(!allInactiv) {
				allCardsAreInactiv = false;
			}
		}
		
		if(!allCardsAreInactiv) {
			fail("Actions were not correctly deactivated");
		}
	}

}
