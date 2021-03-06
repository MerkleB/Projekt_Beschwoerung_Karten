package project.main.Action;

import project.main.GameApplication.Player;
import project.main.Listeners.GameListener;
import project.main.exception.NotActivableException;

public class SelectMagicCollector extends Action {

	@Override
	public String getCode() {
		return "SelectMagicCollector";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			super.execute();
			initMetadata();
			metadata.put("Collector-ID", owningCard.getID().toString());
			GameListener.getInstance().actionExecuted(this);
		}
	}

}
