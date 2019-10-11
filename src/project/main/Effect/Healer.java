package project.main.Effect;

import java.util.Hashtable;

import project.main.Card.Card;
import project.main.GameApplication.Game;
import project.main.GameApplication.Player;
import project.main.exception.NotActivableException;

public class Healer implements Effect {
	
	private Card owningCard;
	private Game game;
	
	@Override
	public String getCode() {
		return "Healer";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		throw new NotActivableException("PermanentEffect "+getCode()+"can't be activated");

	}

	@Override
	public boolean activateable(Player activator) {
		return false;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public void withdraw() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isWithdrawn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setActiv(Player activFor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setInactiv() {
		// TODO Auto-generated method stub

	}

	@Override
	public Card getCard() {
		return owningCard;
	}

	@Override
	public void setCard(Card card) {
		owningCard = card;
	}

	@Override
	public Hashtable<String, String> getMetaData() {
		// TODO Auto-generated method stub
		return null;
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
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isExecutable() {
		return false;
	}

}
