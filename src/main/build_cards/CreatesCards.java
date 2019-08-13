package main.build_cards;

import main.Card;
import main.exception.*;

public interface CreatesCards {
	public Card createCard(String card_id) throws InvalidCardException, NotAllowedCardException, CardCreationException;
}
