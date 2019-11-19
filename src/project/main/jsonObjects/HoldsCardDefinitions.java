package project.main.jsonObjects;

import java.util.ArrayList;
import java.util.TreeMap;

public interface HoldsCardDefinitions {
	public CardDefinition getCardDefinition(String card_id);
	public TreeMap<String, CardDefinition> getCardSet(String cardSetName);
	public String getCardSetName(String card_id);
	public ArrayList<String> getCardIdsInSet(String cardSetName);
	public ArrayList<String> getCardSetNames();
}
