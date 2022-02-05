package project.main.Action;

import project.main.Card.Summon;
import project.main.GameApplication.Player;
import project.main.GameApplication.ProcessesBattle;
import project.main.Listeners.GameListener;
import project.main.exception.NotActivableException;

public class WithdrawFromBattle extends Action {

	@Override
	public String getCode() {
		return "WithdrawFromBattle";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		ProcessesBattle battle = game.getActiveBattle();
		battle.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		if(!checkIfCardInBattle()) throw new NotActivableException("Summon can't be withdrawn from battle if it is not in battle.");
		ProcessesBattle battle = game.getActiveBattle();
		battle.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(!checkIfCardInBattle()) return false;
		return true;
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			super.execute();
			game.getActiveBattle().remove(owningCard.getID());
			GameListener.getInstance().actionExecuted(this);
		}

	}
	
	private boolean checkIfCardInBattle() {
		ProcessesBattle battle = game.getActiveBattle();
		if(battle == null) return false;
		if(!battle.getStatus().equals(ProcessesBattle.RUNNING)) return false;
		Summon[] combatants = game.getActiveBattle().getCombatants();
		if(combatants[0] != owningCard && combatants[1] != owningCard) return false;
		return true;
	}

}
