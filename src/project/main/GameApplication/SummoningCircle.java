package project.main.GameApplication;

import java.util.ArrayList;
import java.util.UUID;

import project.main.Action.GameAction;
import project.main.Action.SelectSummoningCircle;
import project.main.Action.Stackable;
import project.main.Card.ActionOwner;
import project.main.Card.Card;
import project.main.Card.Summon;
import project.main.exception.NotActivableException;

public class SummoningCircle implements HoldingCards, ActionOwner {
	
	private Summon placedSummon;
	private Game game;
	private GameAction selectAction;
	private String id;
	
	public SummoningCircle(Player owner, int indexGreat, int index) {
		selectAction = new SelectSummoningCircle(this);
		id = owner.getID()+"_"+indexGreat+"_"+index;
	}
	
	public String getID() {
		return id;
	}
	
	public boolean isFree() {
		if(placedSummon == null) {
			return true;
		}else return false;
	}
	
	public GameAction getAction() {
		return selectAction;
	}
	
	@Override
	public void setActiv(ArrayList<String> actions, Player activFor) {
		for(String action : actions) {
			if(action.equals(selectAction.getCode())) {
				selectAction.setActiv(activFor);
				break;
			}
		}
	}

	@Override
	public void setActivBy(ArrayList<String> actions, Player activFor, Stackable activator) {
		for(String action : actions) {
			if(action.equals(selectAction.getCode())) {
				selectAction.setActivBy(activator, activFor);
				break;
			}
		}
	}

	@Override
	public void setInactive() {
		selectAction.setInactiv();
	}

	@Override
	public void setInactive(ArrayList<Stackable> exceptionList) {
		boolean exceptionFound = false;
		for(Stackable action : exceptionList) {
			if(action != selectAction) {
				exceptionFound = true;
			}
		}
		if(!exceptionFound) {
			selectAction.setInactiv();
		}
	}

	@Override
	public void activateGameAction(String action, Player activatingPlayer) {
		if(action.equals(selectAction.getCode())) {
			try {
				selectAction.activate(activatingPlayer);
			} catch (NotActivableException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@Override
	public void activateGameAction(String action, Player activatingPlayer, Stackable activator) {
		if(action.equals(selectAction.getCode())) {
			try {
				selectAction.activateBy(activator, activatingPlayer);
			} catch (NotActivableException e) {
				System.out.println(e.getMessage());
			}
		}		
	}

	@Override
	public ArrayList<GameAction> getActions() {
		ArrayList<GameAction> actions = new ArrayList<GameAction>();
		actions.add(selectAction);
		return actions;
	}
	
	@Override
	public Game getGame() {
		return game;
	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public Card findCard(String id) {
		if(placedSummon == null) return null;
		if(placedSummon.getID().equals(id)) {
			return placedSummon;
		}
		return null;
	}

	@Override
	public ArrayList<Card> getCards() {
		ArrayList<Card> list = new ArrayList<Card>();
		list.add(placedSummon);
		return list;
	}

	@Override
	public void addCard(Card card) {
		if(placedSummon == null) {
			placedSummon = (Summon)card;
		}
	}

	@Override
	public void removeCard(Card card) {
		if(placedSummon == card) {
			placedSummon = null;
		}
	}

	@Override
	public void removeCard(String id) {
		if(placedSummon.getID().equals(id)) {
			placedSummon = null;
		}
	}

}
