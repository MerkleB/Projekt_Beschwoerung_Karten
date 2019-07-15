package main;

public interface Card{
	public CardType getType();
	public String getTrivia();
	public Stackable[] getEffect();
	public Stackable getEffect(int index);
	public void show(boolean consoleMode);
}
