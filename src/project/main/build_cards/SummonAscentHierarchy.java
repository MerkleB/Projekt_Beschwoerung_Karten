package project.main.build_cards;

import java.util.TreeMap;

import project.main.Card.Summon;

public class SummonAscentHierarchy implements KnowsSummonAscentHierarchy{
	
	private TreeMap<String, Summon> summonHierarchie;
	private int experience;
	
	public static KnowsSummonAscentHierarchy getInstance() {
		SummonAscentHierarchy instance = new SummonAscentHierarchy();
		instance.summonHierarchie = new TreeMap<String, Summon>();
		return instance;
	}

	@Override
	public void addSummonToHierarchy(Summon summon) {
		String key = "Level-"+summon.getLevel();
		summonHierarchie.put(key, summon);
		if(summon.getSummonHierarchy() != this) {
			summon.setSummonHierarchy(this);
		}
	}


	@Override
	public Summon getNextSummonInHierarchy(Summon ancestorSummon) {
		Summon nextSummon = null;
		int currentLevel = ancestorSummon.getLevel();
		String currentKey = "Level-"+currentLevel;
		Summon currentSummon = summonHierarchie.get(currentKey);
		if(currentSummon == ancestorSummon) {
			String nextKey = "Level-"+(currentLevel+1);
			nextSummon = summonHierarchie.get(nextKey);
		}
		return nextSummon;
	}

	@Override
	public Summon getSummonOfLevel(int level) {
		String key = "Level-"+level;
		return summonHierarchie.get(key);
	}

	@Override
	public boolean canAscend() {
		if(experience >= 4) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public void addExperience() {
		experience++;
	}

	@Override
	public int getExperience() {
		return experience;
	}

	@Override
	public void clearExperience() {
		experience = 0;
	}
	
	
}
