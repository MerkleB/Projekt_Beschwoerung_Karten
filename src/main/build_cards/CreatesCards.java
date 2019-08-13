package main.build_cards;

import java.util.Hashtable;

import main.Card;
import main.Player;
import main.exception.InvalidCardException;
import main.exception.NotAllowedCardException;

public interface CreatesCards {
	public Card createCard(String card_id) throws InvalidCardException, NotAllowedCardException;
}
