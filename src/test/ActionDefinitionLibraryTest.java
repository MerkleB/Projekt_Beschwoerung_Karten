package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import main.jsonObjects.ActionDefinitionLibrary;
import main.jsonObjects.HoldsActionDefinitions;

public class ActionDefinitionLibraryTest {

	private HoldsActionDefinitions cut;
	private ArrayList<String> summonActions;
	private ArrayList<String> spellActions;
	private ArrayList<String> collectorActions;
	private ArrayList<String> mainPhaseActions;
	private ArrayList<String> combatPhaseActions;
	private ArrayList<String> handZoneActions;
	private ArrayList<String> mcZoneActions;
	
	@Before
	public void setUp() throws Exception {
		cut = ActionDefinitionLibrary.getInstance();
		setPathToTestResources();
		summonActions = new ArrayList<>();
		summonActions.add("Summon");
		spellActions = new ArrayList<>();
		spellActions.add("Cast");
		collectorActions = new ArrayList<>();
		collectorActions.add("CallBack");
		mainPhaseActions = new ArrayList<>();
		mainPhaseActions.add("Summon");
		mainPhaseActions.add("Cast");
		mainPhaseActions.add("CallBack");
		combatPhaseActions = new ArrayList<>();
		combatPhaseActions.add("Cast");
		handZoneActions = new ArrayList<>();
		handZoneActions.add("Summon");
		handZoneActions.add("Cast");
		mcZoneActions = new ArrayList<>();
		mcZoneActions.add("CallBack");
	}

	@Test
	public void testGetCardActions() {
		TreeMap<String, ArrayList<String>> list = cut.getCardActions();
		
		ArrayList<String> actions = list.get("Summon");
		for(int i=0; i<actions.size(); i++) {
			if(summonActions.get(i).equals(actions.get(i)) == false) {
				fail("Wrong Summon actions were retrieved.");
			}
		}
		
		actions = list.get("Spell");
		for(int i=0; i<actions.size(); i++) {
			if(spellActions.get(i).equals(actions.get(i)) == false) {
				fail("Wrong Spell actions were retrieved.");
			}
		}
		
		actions = list.get("MagicCollector");
		for(int i=0; i<actions.size(); i++) {
			if(collectorActions.get(i).equals(actions.get(i)) == false) {
				fail("Wrong MagicCollector actions were retrieved.");
			}
		}
	}

	@Test
	public void testGetCardActionsString() {
		ArrayList<String> actions = cut.getCardActions("Summon");
		for(int i=0; i<actions.size(); i++) {
			if(summonActions.get(i).equals(actions.get(i)) == false) {
				fail("Wrong Summon actions were retrieved.");
			}
		}
	}

	@Test
	public void testGetPhaseActions() {
		TreeMap<String, ArrayList<String>> list = cut.getPhaseActions();
		
		ArrayList<String> actions = list.get("Main");
		for(int i=0; i<actions.size(); i++) {
			if(mainPhaseActions.get(i).equals(actions.get(i)) == false) {
				fail("Wrong Main actions were retrieved.");
			}
		}
		
		actions = list.get("Combat");
		for(int i=0; i<actions.size(); i++) {
			if(combatPhaseActions.get(i).equals(actions.get(i)) == false) {
				fail("Wrong Combat actions were retrieved.");
			}
		}
	}

	@Test
	public void testGetPhaseActionsString() {
		ArrayList<String> list = cut.getPhaseActions("Main");
		
		for(int i=0; i<list.size(); i++) {
			if(mainPhaseActions.get(i).equals(list.get(i)) == false) {
				fail("Wrong Main actions were retrieved.");
			}
		}
	}

	@Test
	public void testGetZoneActions() {
		TreeMap<String, ArrayList<String>> list = cut.getZoneActions();
		
		ArrayList<String> actions = list.get("HandZone");
		for(int i=0; i<actions.size(); i++) {
			if(handZoneActions.get(i).equals(actions.get(i)) == false) {
				fail("Wrong HandZone actions were retrieved.");
			}
		}
		
		actions = list.get("MagicCollectorZone");
		for(int i=0; i<actions.size(); i++) {
			if(mcZoneActions.get(i).equals(actions.get(i)) == false) {
				fail("Wrong MagicCollectorZone actions were retrieved.");
			}
		}
	}

	@Test
	public void testGetZoneActionsString() {
		ArrayList<String> actions = cut.getZoneActions("HandZone");
		for(int i=0; i<actions.size(); i++) {
			if(handZoneActions.get(i).equals(actions.get(i)) == false) {
				fail("Wrong HandZone actions were retrieved.");
			}
		}
	}
	
	public void setPathToTestResources() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		String testPath = "./test/testJSON/actions.json";
		Field pathField;
		pathField = cut.getClass().getDeclaredField("resourcePath");
		pathField.setAccessible(true);
		pathField.set(cut, testPath);
	}

}
