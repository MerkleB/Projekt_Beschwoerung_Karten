package main.GameApplication;

import java.util.ArrayList;
import java.util.UUID;

import main.Card.Card;

public interface HoldingCards {
	public Card findCard(UUID id);
	public ArrayList<Card> getCards();
	public void addCard(Card card);
	public void removeCard(Card card);
	public void removeCard(UUID id);
}
