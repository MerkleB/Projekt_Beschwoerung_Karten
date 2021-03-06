package project.main.jsonObjects;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import project.main.util.FileLoader;

import java.io.InputStream;
import java.io.InputStreamReader;

public class CardDefinitionLibrary implements HoldsCardDefinitions {
	
	private static HoldsCardDefinitions instance;	
	private Hashtable<String, SetToken> card_sets;
	private Hashtable<String, TreeMap<String, CardDefinition>> cardDefinitions;
	private String resourcePath;
	
	public static HoldsCardDefinitions getInstance() {
		if(instance == null) {
			instance = new CardDefinitionLibrary();
			((CardDefinitionLibrary)instance).resourcePath = "project/json/card_lists/";
		}
		return instance;
	}
	
	@Override
	public CardDefinition getCardDefinition(String card_id) {
		if(cardDefinitions == null) {
			cardDefinitions = new Hashtable<String, TreeMap<String, CardDefinition>>();
		}
		CardDefinition definition = null;
		String cardSetName = getCardSetName(card_id);
		if(!cardDefinitions.containsKey(cardSetName)) {
			loadCardSet(cardSetName);
		}
		definition = cardDefinitions.get(cardSetName).get(card_id);
		return definition;
	}
	
	@Override
	public String getCardSetName(String card_id) {
		String token = card_id.split("-")[0];
		if(card_sets == null) {
			card_sets = new Hashtable<String, SetToken>();
		}
		if(card_sets.size() == 0) {
			((CardDefinitionLibrary)instance).loadListOfCardSets();
		}
		return card_sets.get(token).name;
	}
	
	@Override
	public ArrayList<String> getCardIdsInSet(String cardSetName) {
		if(cardDefinitions == null) {
			cardDefinitions = new Hashtable<String, TreeMap<String, CardDefinition>>();
		}
		TreeMap<String, CardDefinition> definitions = cardDefinitions.get(cardSetName);
		if(definitions == null) {
			loadCardSet(cardSetName);
			definitions = cardDefinitions.get(cardSetName);
		}
		ArrayList<String> cardIds = new ArrayList<String>();
		definitions.forEach((k,v)->{
			cardIds.add(k);
		});
		return cardIds;
	}

	@Override
	public ArrayList<String> getCardSetNames() {
		if(card_sets == null) {
			card_sets = new Hashtable<String, SetToken>();
			loadListOfCardSets();
		}
		ArrayList<String> cardSetNames = new ArrayList<String>();
		card_sets.forEach((k,v)->{
			cardSetNames.add(v.name);
		});
		return cardSetNames;
	}

	private void loadCardSet(String cardSetName) {
		CardDefinitionList definitionList = getDefinitions(cardSetName);
		TreeMap<String, CardDefinition> definitionMap = new TreeMap<String, CardDefinition>();
		for(CardDefinition definition : definitionList.cards) {
			definitionMap.put(definition.card_id, definition);
		}
		cardDefinitions.put(cardSetName, definitionMap);
	}
	
	private CardDefinitionList getDefinitions(String cardSetName) {
		InputStream jsonStream = getStream(cardSetName+".json");
		JsonReader jsonReader = new JsonReader(new InputStreamReader(jsonStream));
		Gson gson = new Gson();
		CardDefinitionList list = gson.fromJson(jsonReader, CardDefinitionList.class);
		return list;
	}
	
	private void loadListOfCardSets() {
		InputStream jsonStream = getStream("sets.json");
		JsonReader jsonReader = new JsonReader(new InputStreamReader(jsonStream));
		Gson gson = new Gson();
		SetTokenList tokenList = gson.fromJson(jsonReader, SetTokenList.class);
		for(SetToken token : tokenList.sets) {
			card_sets.put(token.token, token);
		}
	}
	
	private InputStream getStream(String resourceName) {
		String filename = resourcePath+resourceName;
		FileLoader loader = new FileLoader();
		return loader.getFileAsStream(filename);
	}

	@Override
	public TreeMap<String, CardDefinition> getCardSet(String cardSetName) {
		if(cardDefinitions.containsKey(cardSetName) == false) {
			loadCardSet(cardSetName);
		}
		return cardDefinitions.get(cardSetName);
	}

}
