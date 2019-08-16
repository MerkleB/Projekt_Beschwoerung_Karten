package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.UUID;

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
import test.mok.MokProvider;
import test.mok.TestData;

public class HandZoneTest {
	
	private HandZone cut;
	private Summon summon;
	private Spell spell;
	private Player owner;
	
	@Before
	public void setUp() {
		cut = new HandZone();
		summon = (Summon)TestData.getCard("bsc-su-00-1");
		spell = (Spell)TestData.getCard("bsc-su-01");
		try {
			owner = MokProvider.getPlayer();
			spell.setOwningPlayer(owner);
			String uuidString = spell.getName()+spell.getOwningPlayer().getID()+1;
			spell.setID(UUID.fromString(uuidString));
			summon.setOwningPlayer(owner);
			uuidString = summon.getName()+summon.getOwningPlayer().getID()+1;
			summon.setID(UUID.fromString(uuidString));
			cut.addCard(spell);
			cut.addCard(summon);
		} catch (NoCardException e) {
			fail("Error in setUp: "+e.getMessage());
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
		String uuidString = summon.getName()+summon.getOwningPlayer().getID()+1;
		Card card = cut.findCard(UUID.fromString(uuidString));
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
		String uuidString = summon.getName()+summon.getOwningPlayer().getID()+2;
		UUID id = UUID.fromString(uuidString);
		newSummon.setID(id);
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
		String uuidString = summon.getName()+summon.getOwningPlayer().getID()+1;
		UUID id = UUID.fromString(uuidString);
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
		ArrayList<GameAction> actions;
		ArrayList<Card> cards = cut.getCards();
		ArrayList<String> controlList = getListOfActionsWichShouldBeActiv(phase.getName());
		boolean allActionsHaveCorrectActivValue = true;
		for(Card card : cards) {
			boolean allCardActionsAreCorrect = true;
			actions = card.getActions();
			for(GameAction action : actions) {
				try {
					Field activField = action.getClass().getDeclaredField("activ");
					boolean isActive = activField.getBoolean(action);
					boolean activFoundInControlList = false;
					boolean inactivFoundInControlList = false;
					if(isActive) {
						for(String controlEntry : controlList) {
							if(action.getName().equals(controlEntry)) {
								activFoundInControlList = true;
							}
						}
					}else {
						for(String controlEntry : controlList) {
							if(action.getName().equals(controlEntry)) {
								inactivFoundInControlList = true;
							}
						}
					}
					
					if(!activFoundInControlList || inactivFoundInControlList) {
						allCardActionsAreCorrect = false;
					}
				} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
					fail("ReflectionError in testActivate");
				}
			}
			if(!allCardActionsAreCorrect) {
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
