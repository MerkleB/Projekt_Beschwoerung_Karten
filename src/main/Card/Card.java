package main.Card;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.UUID;

import main.Action.Effect;
import main.Action.GameAction;
import main.Action.Stackable;
import main.GameApplication.Player;
import main.build_cards.CardTypes;
import main.exception.NoCardException;

public interface Card{
	public Player getOwningPlayer();
	public void setOwningPlayer(Player owner) throws NoCardException;
	public MagicCollector getCollector();
	public CardTypes getType();
	public String getTrivia();
	public void setTrivia(String trivia);
	public Effect[] getEffects() throws NoCardException;
	public Effect getEffect(int index) throws NoCardException;
	public String getName();
	public String getCardID();
	public UUID getID();
	public void setID(UUID uuid) throws NoCardException;
	public void show();
	public void show(Graphics2D graphics);
	public void setActiv(ArrayList<String> actions, Player activFor);
	public void setActivBy(ArrayList<String> actions, Player activFor, Stackable activator);
	public void setInactive();
	public void activateGameAction(String action, Player activatingPlayer);
	public void activateGameAction(String action, Player activatingPlayer, Stackable activator);
	public ArrayList<GameAction> getActions();
	public void activateEffect(int effectNumber) throws NoCardException;
}
