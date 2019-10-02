package main.Action;

import main.GameApplication.DeckZone;
import main.GameApplication.GameStack;
import main.GameApplication.HandZone;
import main.GameApplication.Player;
import main.Listeners.GameListener;
import main.exception.NotActivableException;

public class Draw extends Action {

	@Override
	public String getCode() {
		return "Draw";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		initMetadata();
		metadata.put("Target-ID", owningCard.getID().toString());
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		initMetadata();
		metadata.put("Target-ID", owningCard.getID().toString());
		GameStack.getInstance().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			Player player = owningCard.getOwningPlayer();
			DeckZone deck = (DeckZone)player.getGameZone("DeckZone");
			HandZone hand = (HandZone)player.getGameZone("HandZone");
			deck.removeCard(owningCard);
			hand.addCard(owningCard);
			GameListener.getInstance().actionExecuted(this);
		}
	}

}
