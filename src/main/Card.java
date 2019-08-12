package main;

import java.awt.Graphics2D;
import java.util.UUID;

import main.exception.NoCardException;

public interface Card{
	public Player getOwningPlayer();
	public MagicCollector getCollector();
	public CardType getType();
	public String getTrivia();
	public Effect[] getEffects() throws NoCardException;
	public Effect getEffect(int index) throws NoCardException;
	public String getName();
	public UUID getID();
	public void setID(UUID uuid);
	public void show();
	public void show(Graphics2D graphics);
	public void setActiv(String[] actions);
	public void setInactive();
	public void activateGameAction(String action);
	public void activateGameAction(String action, Stackable activator);
	public void activateEffect(int effectNumber);
}
