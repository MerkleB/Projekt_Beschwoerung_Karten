package project.main.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import project.main.jsonObjects.ElementDefinition;
import project.main.jsonObjects.ElementDefinitionList;

public class ElementDefinitionLibrary implements CalculatesElementEffectivities {

	private Hashtable<String, ElementDefinition> definitions;
	private String resourcePath;
	private static CalculatesElementEffectivities instance;
	
	public static CalculatesElementEffectivities getInstance() {
		if(instance == null) {
			instance = new ElementDefinitionLibrary();
			((ElementDefinitionLibrary)instance).loadDefinitions();
			((ElementDefinitionLibrary)instance).resourcePath = "project/main/json/game_settings/elements.json";
		}
		return instance;
	}
	
	@Override
	public double getEffectivity(String attackElement, String defendElement) {
		if(isStrong(attackElement, defendElement)) {
			return 1.25;
		}
		if(isWeak(attackElement, defendElement)) {
			return 0.75;
		}
		return 1;
	}
	
	private boolean isStrong(String attackElement, String defendElement) {
		boolean result = false;
		ElementDefinition element = definitions.get(attackElement);
		for(String strong : element.strong) {
			if(strong.equals(defendElement)) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	private boolean isWeak(String attackElement, String defendElement) {
		boolean result = false;
		ElementDefinition element = definitions.get(attackElement);
		for(String strong : element.weak) {
			if(strong.equals(defendElement)) {
				result = true;
				break;
			}
		}
		return result;
	}

	@Override
	public ElementDefinition getElementDefinition(String elementName) {
		return definitions.get(elementName);
	}
	
	private void loadDefinitions() {
		definitions = new Hashtable<String, ElementDefinition>();
		
		Gson gson = new Gson();
		InputStream jsonStream = new FileLoader().getFileAsStream(resourcePath);
		JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
		ElementDefinitionList  definitions = gson.fromJson(reader, ElementDefinitionList.class);
		
		for(ElementDefinition definition : definitions.elements) {
			this.definitions.put(definition.name, definition);
		}
	}

}
