package main;

import java.awt.Graphics2D;
import java.util.UUID;

import main.exception.NoCardException;

public interface Card{
	public CardType getType();
	public String getTrivia();
	public Effect[] getEffects() throws NoCardException;
	public Effect getEffect(int index) throws NoCardException;
	public String getName();
	public UUID getID();
	public void show();
	public void show(Graphics2D graphics);
}
