package main.Action;

import main.GameApplication.Player;
import main.Listeners.GameListener;
import main.exception.NotActivableException;

public class SelectMagicCollector extends Action {

	@Override
	public String getCode() {
		return "SelectMagicCollector";
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		execute();
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			initMetadata();
			metadata.put("Collector-ID", owningCard.getID().toString());
			GameListener.getInstance().actionExecuted(this);
		}
	}

}
