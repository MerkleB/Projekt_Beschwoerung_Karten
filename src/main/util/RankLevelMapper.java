package main.util;

import java.util.Hashtable;

public class RankLevelMapper implements mapsRankAndLevel{
	private static Hashtable<String, String> levelToRank;
	private static Hashtable<String, String> rankToLevel; 
	
	private static mapsRankAndLevel instance;
	
	public static mapsRankAndLevel getInstance() {
		if(instance == null) {
		instance = new RankLevelMapper();
		((RankLevelMapper) instance).initializeMaps();
		}
		return instance;
	}
	
	@Override
	public String mapLevelToRank(int level, String summonClass) {
		if(levelToRank == null) {
			initializeMaps();
		}
		return null;
	}
	
	@Override
	public int mapRankToLevel(String rank) {
		if(rankToLevel == null) {
			initializeMaps();
		}
		return -1;
	}
	
	private void initializeMaps() {
		
	}
}
