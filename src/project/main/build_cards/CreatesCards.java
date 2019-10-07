package project.main.build_cards;

import project.main.Card.Card;
import project.main.GameApplication.Game;
import project.main.exception.*;

public interface CreatesCards {
	public Card createCard(String card_id, Game Game) throws InvalidCardException, NotAllowedCardException, CardCreationException;
}
