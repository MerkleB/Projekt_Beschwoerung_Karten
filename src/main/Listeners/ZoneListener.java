package main.Listeners;

import main.Card.Card;
import main.GameApplication.IsAreaInGame;

public interface ZoneListener {
	public void cardAdded(IsAreaInGame zone, Card card);
	public void cardRemoved(IsAreaInGame zone, Card card);
}
