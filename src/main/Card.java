package main;

import java.awt.Graphics2D;

public interface Card{
	public CardType getType();
	public String getTrivia();
	public Effect[] getEffects();
	public Effect getEffect(int index);
	public String getName();
	public void show();
	public void show(Graphics2D graphics);
}
