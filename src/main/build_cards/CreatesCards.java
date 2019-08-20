package main.build_cards;

import main.Card.Card;
import main.GameApplication.Player;
import main.exception.*;

public interface CreatesCards {
	public Card createCard(String card_id) throws InvalidCardException, NotAllowedCardException, CardCreationException;
}
