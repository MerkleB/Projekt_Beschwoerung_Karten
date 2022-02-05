package project.main.build_cards;

import project.main.Card.Summon;

public interface KnowsSummonAscentHierarchy {
	public void addSummonToHierarchy(Summon summon);
	public Summon getNextSummonInHierarchy(Summon ancestorSummon);
	public Summon getSummonOfLevel(int level);
	public boolean canAscend();
	/**
	 * Add 1 point to experience
	 */
	public void addExperience();
	public int getExperience();
	public void clearExperience();
}
