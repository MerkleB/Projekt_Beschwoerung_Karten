package project.main.Card;

import project.main.GameApplication.Player;
import project.main.build_cards.CardTypes;
import project.main.exception.NoCardException;

public interface Card extends ActionOwner, EffectOwner{
	public Player getOwningPlayer();
	public void setOwningPlayer(Player owner) throws NoCardException;
	public MagicCollector getCollector();
	public CardTypes getType();
	public String getTrivia();
	public void setTrivia(String trivia);
	public String getName();
	public String getCardID();
	public String getID();
	public void setID(String id) throws NoCardException;
}
