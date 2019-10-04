package main.GameApplication;

import java.util.ArrayList;
import java.util.UUID;

import main.Card.Card;

public interface HoldingCards extends GameObject {
	/**
	 * Retrieves the Card with given id
	 * @param UUID id
	 * @return Card
	 */
	public Card findCard(UUID id);
	
	/**
	 * Retrieves all Cards.
	 * @return ArrayList<Card>
	 */
	public ArrayList<Card> getCards();
	
	/**
	 * Adds a card
	 * @param card
	 */
	public void addCard(Card card);
	
	/**
	 * Removes a given card
	 * @param Card card
	 */
	public void removeCard(Card card);
	
	/**
	 * Removes the card with the given id
	 * @param id
	 */
	public void removeCard(UUID id);
}
