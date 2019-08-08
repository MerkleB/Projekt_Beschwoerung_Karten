package main.build_cards;

import java.util.Hashtable;

import main.Summon;

public interface KnowsSummonAscentHierarchy {
	public void addSummonToHierarchy(Summon summon);
	public Summon getNextSummonInHierarchy(Summon ancestorSummon);
	public Summon getSummonOfLevel(int level);
}
