package main.build_cards;

import main.Card.Card;
import main.GameApplication.Game;
import main.exception.*;

public interface CreatesCards {
	public Card createCard(String card_id, Game Game) throws InvalidCardException, NotAllowedCardException, CardCreationException;
}
