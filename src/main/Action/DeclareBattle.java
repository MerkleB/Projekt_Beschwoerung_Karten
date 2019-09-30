package main.Action;

import main.Card.Summon;
import main.GameApplication.Player;
import main.GameApplication.ProcessesBattle;
import main.Listeners.GameListener;
import main.exception.NotActivableException;

public class DeclareBattle extends Action {
	
	public ProcessesBattle battle;
	
	@Override
	public String getCode() {
		return "DeclareBattle";
	}

	@Override
	public void execute() {
		
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		//TODO: Enable selection of target summon
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(!((Summon) owningCard).getActivityStatus().equals(Summon.READY)) return false;
		return true;
	}
	
	private void initializeBattle() {
		
	}

}
