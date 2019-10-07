package project.main.Listeners;

import project.main.Card.Card;
import project.main.GameApplication.IsAreaInGame;

public interface ZoneListener {
	public void cardAdded(IsAreaInGame zone, Card card);
	public void cardRemoved(IsAreaInGame zone, Card card);
}
