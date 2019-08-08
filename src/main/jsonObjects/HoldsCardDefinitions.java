package main.jsonObjects;

import java.util.TreeMap;

public interface HoldsCardDefinitions {
	public CardDefinition getCardDefinition(String card_id);
	public TreeMap<String, CardDefinition> getCardSet(String cardSetName);
}
