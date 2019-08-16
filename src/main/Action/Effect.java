package main.Action;

import main.Card.Card;

public interface Effect extends Stackable {
	public String getDescription();
	public boolean isExecutable();
	public Card getOwningCard();
	public void setOwningCard(Card owningCard);
}
