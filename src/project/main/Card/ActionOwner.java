package project.main.Card;

import java.util.ArrayList;

import project.main.Action.GameAction;
import project.main.Action.Stackable;
import project.main.GameApplication.Player;

public interface ActionOwner {
	public void setActiv(ArrayList<String> actions, Player activFor);
	public void setActivBy(ArrayList<String> actions, Player activFor, Stackable activator);
	public void setInactive();
	public void setInactive(ArrayList<Stackable> exceptionList);
	public void activateGameAction(String action, Player activatingPlayer);
	public void activateGameAction(String action, Player activatingPlayer, Stackable activator);
	public ArrayList<GameAction> getActions();
}
