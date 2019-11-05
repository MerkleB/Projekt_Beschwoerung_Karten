package project.main.Card;

import java.util.ArrayList;
import java.util.UUID;

import project.main.Action.GameAction;
import project.main.Action.Stackable;
import project.main.Effect.Effect;
import project.main.GameApplication.Player;
import project.main.build_cards.CardTypes;
import project.main.exception.NoCardException;

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
	public void setActiv(ArrayList<String> actions, Player activFor);
	public void setActivBy(ArrayList<String> actions, Player activFor, Stackable activator);
	public void setInactive();
	public void setInactive(ArrayList<Stackable> exceptionList);
	public void activateGameAction(String action, Player activatingPlayer);
	public void activateGameAction(String action, Player activatingPlayer, Stackable activator);
	public ArrayList<GameAction> getActions();
	public void activateEffect(int effectNumber) throws NoCardException;
}
