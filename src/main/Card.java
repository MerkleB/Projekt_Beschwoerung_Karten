package main;

public interface Card {
	public CardType getType();
	public Stackable[] getEffect();
	public Stackable getEffect(int index);
}
