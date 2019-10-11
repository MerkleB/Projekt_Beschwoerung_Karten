package project.main.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import project.main.jsonObjects.RankDefinition;
import project.main.jsonObjects.RankDefinitionList;

public class RankLevelMapper implements MapsRankAndLevel{
	private static Hashtable<String, Hashtable<String, String>> levelToRank;
	private static Hashtable<String, String> rankToLevel; 
	
	private static MapsRankAndLevel instance;
	
	private String resourcePath;
	
	public static MapsRankAndLevel getInstance() {
		if(instance == null) {
		instance = new RankLevelMapper();
		((RankLevelMapper) instance).resourcePath = "project/json/game_settings/ranks.json";
		((RankLevelMapper) instance).initializeMaps();
		}
		return instance;
	}
	
	@Override
	public String mapLevelToRank(int level, String summonClass) {
		if(levelToRank == null) {
			initializeMaps();
		}
		return levelToRank.get(summonClass).get(""+level);
	}
	
	@Override
	public int mapRankToLevel(String rank) {
		if(rankToLevel == null) {
			initializeMaps();
		}
		String levelString = rankToLevel.get(rank);
		int level = Integer.parseInt(levelString);
		return level;
	}
	
	private void initializeMaps() {
		levelToRank = new Hashtable<String, Hashtable<String, String>>();
		rankToLevel = new Hashtable<String, String>();
		FileLoader loader = new FileLoader();
		InputStream jsonStream = loader.getFileAsStream(resourcePath);
		JsonReader reader = new JsonReader(new InputStreamReader(jsonStream));
		Gson gson = new Gson();
		RankDefinitionList ranks = gson.fromJson(reader, RankDefinitionList.class);
		initializeLevelToRankMap(ranks);
		initializeRankToLevelMap(ranks);
	}
	
	private void initializeLevelToRankMap(RankDefinitionList ranks) {
		for(RankDefinition rank : ranks.ranks) {
			Hashtable<String, String> table = levelToRank.get(rank.summon_class);
			if(table == null) {
				table = new Hashtable<String, String>();
				levelToRank.put(rank.summon_class, table);
			}
			table.put(rank.level, rank.name);
		}
	}
	
	private void initializeRankToLevelMap(RankDefinitionList ranks) {
		for(RankDefinition rank : ranks.ranks) {
			rankToLevel.put(rank.name, rank.level);
		}
	}
}
