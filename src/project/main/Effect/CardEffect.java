package project.main.Effect;

import java.util.Hashtable;

import project.main.Card.Card;
import project.main.GameApplication.Application;
import project.main.GameApplication.Game;
import project.main.GameApplication.Player;
import project.main.exception.NotActivableException;
import project.main.util.TextProvider;

public abstract class CardEffect implements Effect {
	
	protected Card owningCard;
	protected Game game;
	protected Hashtable<String, String> metaData;
	protected boolean withdrawn;
	protected boolean isActivated;
	protected boolean isActiv;
	protected Player isActivFor;

	@Override
	public String getName() {
		String name = TextProvider.getInstance().getEffectDescription(getCode(), Application.getInstance().getLanguage()).name;
		if(name.equals("")) {
			name = this.getCode();
		}
		return name;
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		if(!activateable(isActivFor)) {
			throw new NotActivableException("Effect "+getCode()+" is not activatable");
		}
		isActivated = true;
		
	}

	@Override
	public Player getActivator() {
		if(isActivated) {
			return isActivFor;
		}else {
			return null;
		}
	}

	@Override
	public void withdraw() {
		withdrawn = true;
	}

	@Override
	public boolean isWithdrawn() {
		return withdrawn;
	}

	@Override
	public void setActiv(Player activFor) {
		isActiv = true;
		isActivFor = activFor;
	}

	@Override
	public void setInactiv() {
		isActiv = false;
		isActivFor = null;
		isActivated = false;
	}

	@Override
	public Card getCard() {
		return owningCard;
	}

	@Override
	public void setCard(Card card) {
		this.owningCard = card;
	}

	@Override
	public Hashtable<String, String> getMetaData() {
		return metaData;
	}

	@Override
	public void initialize(String value) {
		metaData = new Hashtable<String,String>();	
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
		return TextProvider.getInstance().getEffectDescription(getCode(), Application.getInstance().getLanguage()).description;
	}

	@Override
	public boolean isExecutable() {
		return isActivated && !withdrawn;
	}

}
