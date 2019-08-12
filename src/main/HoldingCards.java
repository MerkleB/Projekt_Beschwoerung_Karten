package main;

public interface HoldingCards {
	public Card findCard(String id);
	public Summon findSummon(String id);
	public Spell findSpell(String id);
	public void activate(String[] actions, Player player);
	public void deavtivateAll();
}
