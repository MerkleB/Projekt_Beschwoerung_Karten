package main.GameApplication;

public interface GameZone extends HoldingCards {
	public void activate(Player player, IsPhaseInGame gamePhase);
	public void deavtivateAll();
}
