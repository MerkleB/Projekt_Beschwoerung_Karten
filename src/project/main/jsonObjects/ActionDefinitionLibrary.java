package project.main.jsonObjects;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import project.main.util.FileLoader;

public class ActionDefinitionLibrary implements HoldsActionDefinitions {
	
	private static HoldsActionDefinitions instance;
	private String resourcePath;
	private TreeMap<String, ArrayList<String>> cardActions;
	private TreeMap<String, ArrayList<String>> phaseActions;
	private TreeMap<String, ArrayList<String>> zoneActions;
	
	public static HoldsActionDefinitions getInstance() {
		if(instance == null) {
			instance = new ActionDefinitionLibrary();
			((ActionDefinitionLibrary)instance).resourcePath = "project/json/game_settings/actions.json";
		}
		return instance;
	}

	@Override
	public TreeMap<String, ArrayList<String>> getCardActions() {
		load();
		return cardActions;
	}

	@Override
	public ArrayList<String> getCardActions(String cardType) {
		load();
		return cardActions.get(cardType);
	}

	@Override
	public TreeMap<String, ArrayList<String>> getPhaseActions() {
		load();
		return phaseActions;
	}

	@Override
	public ArrayList<String> getPhaseActions(String phase) {
		load();
		return phaseActions.get(phase);
	}

	@Override
	public TreeMap<String, ArrayList<String>> getZoneActions() {
		load();
		return zoneActions;
	}

	@Override
	public ArrayList<String> getZoneActions(String zone) {
		load();
		return zoneActions.get(zone);
	}
	
	private void load() {
		if(cardActions == null || phaseActions == null || zoneActions == null) {
			cardActions = new TreeMap<String, ArrayList<String>>();
			phaseActions = new TreeMap<String, ArrayList<String>>();
			zoneActions = new TreeMap<String, ArrayList<String>>();
			
			Gson gson = new Gson();
			InputStream jsonStream = new FileLoader().getFileAsStream(resourcePath);
			JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
			ActionDefinitionList listFromJSON = gson.fromJson(reader, ActionDefinitionList.class);
			
			for(int i=0; i<listFromJSON.actions.length; i++) {
				ActionDefinition action = listFromJSON.actions[i];
				sortIntoCardActions(action);
				sortIntoPhaseActions(action);
				sortIntoZoneActions(action);
			}
		}
	}
	
	private void sortIntoCardActions(ActionDefinition action) {
		for(int i=0; i<action.cardTypes.length; i++) {
			if(cardActions.containsKey(action.cardTypes[i]) == false) {
				cardActions.put(action.cardTypes[i], new ArrayList<String>());
			}
			cardActions.get(action.cardTypes[i]).add(action.actionName);
		}
	}
	
	private void sortIntoPhaseActions(ActionDefinition action) {
		for(int i=0; i<action.phases.length; i++) {
			if(phaseActions.containsKey(action.phases[i]) == false) {
				phaseActions.put(action.phases[i], new ArrayList<String>());
			}
			phaseActions.get(action.phases[i]).add(action.actionName);
		}
	}
	
	private void sortIntoZoneActions(ActionDefinition action) {
		for(int i=0; i<action.zones.length; i++) {
			if(zoneActions.containsKey(action.zones[i]) == false) {
				zoneActions.put(action.zones[i], new ArrayList<String>());
			}
			zoneActions.get(action.zones[i]).add(action.actionName);
		}
	}

}
