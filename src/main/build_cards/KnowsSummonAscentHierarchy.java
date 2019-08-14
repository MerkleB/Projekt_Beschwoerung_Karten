package main.build_cards;

import main.Summon;

public interface KnowsSummonAscentHierarchy {
	public void addSummonToHierarchy(Summon summon);
	public Summon getNextSummonInHierarchy(Summon ancestorSummon);
	public Summon getSummonOfLevel(int level);
}
