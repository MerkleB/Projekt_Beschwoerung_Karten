package project.main.Action;

import project.main.Card.Card;
import project.main.GameApplication.Game;
import project.main.GameApplication.Player;
import project.main.GameApplication.SummoningCircle;
import project.main.Listeners.GameListener;
import project.main.exception.NotActivableException;

public class SelectSummoningCircle extends Action {

	public SummoningCircle circle;
	
	public SelectSummoningCircle(SummoningCircle circle) {
		this.circle = circle;
	}
	
	@Override
	public String getCode() {
		return "SelectSummoningCircle";
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
	    getGame().getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
		
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		getGame().getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			super.execute();
			GameListener.getInstance().actionExecuted(this);
		}
	}

	@Override
	public Card getCard() {
		return circle.getCards().get(0);
	}

	@Override
	public Game getGame() {
		return circle.getGame();
	}

}
