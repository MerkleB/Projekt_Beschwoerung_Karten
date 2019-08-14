package main;

public interface Player extends HoldingCards{
	public GameZone getGameZone(String zoneName);
	public int getFreeEnergy();
	public int getUsedEnergy();
	public int getDepletedEnergy();
	public int getSummoningPoints();
}
