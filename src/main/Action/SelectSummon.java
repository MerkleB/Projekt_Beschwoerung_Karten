package main.Action;

import main.Listeners.GameListener;

public class SelectSummon extends Action {

	@Override
	public String getCode() {
		return "SelectSummon";
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			initMetadata();
			metadata.put("Summon-ID", owningCard.getID().toString());
			GameListener.getInstance().actionExecuted(this);
		}
	}

}
