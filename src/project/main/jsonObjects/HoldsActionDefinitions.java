package project.main.jsonObjects;

import java.util.ArrayList;
import java.util.TreeMap;

public interface HoldsActionDefinitions {
	public TreeMap<String, ArrayList<String>> getCardActions();
	public ArrayList<String> getCardActions(String cardType);
	public TreeMap<String, ArrayList<String>> getPhaseActions();
	public ArrayList<String> getPhaseActions(String phase);
	public TreeMap<String, ArrayList<String>> getZoneActions();
	public ArrayList<String> getZoneActions(String zone);
}
