package main.GameApplication;

public interface IsAreaInGame extends HoldingCards {
	public void activate(Player player, IsPhaseInGame gamePhase);
	public void deavtivateAll();
	public Player getOwner();
	public String getName();
}
