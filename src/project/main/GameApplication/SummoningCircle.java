package project.main.GameApplication;

import java.util.ArrayList;
import java.util.UUID;

import project.main.Action.GameAction;
import project.main.Action.SelectSummoningCircle;
import project.main.Card.Card;
import project.main.Card.Summon;

public class SummoningCircle implements HoldingCards {
	
	private Summon placedSummon;
	private Game game;
	private GameAction selectAction;
	
	public SummoningCircle() {
		selectAction = new SelectSummoningCircle(this);
	}
	
	public boolean isFree() {
		if(placedSummon == null) {
			return true;
		}else return false;
	}
	
	public GameAction getAction() {
		return selectAction;
	}
	
	public void setActivBy(Player activFor, GameAction activatedBy) {
		selectAction.setActivBy(activatedBy, activFor);
	}
	
	public void setInactiv() {
		selectAction.setInactiv();
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
	public Card findCard(UUID id) {
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
	public void removeCard(UUID id) {
		if(placedSummon.getID().equals(id)) {
			placedSummon = null;
		}
	}

}
