package project.main.Action;

import project.main.Card.*;
import project.main.GameApplication.Battle;
import project.main.GameApplication.Player;
import project.main.GameApplication.ProcessesBattle;
import project.main.Listeners.GameActionListener;
import project.main.Listeners.GameListener;
import project.main.exception.NotActivableException;

public class BlockPlayerAttack extends Action implements GameActionListener {
	
	public AttackPlayer actionToBlock;
	
	public BlockPlayerAttack() {
		actionToBlock = null;
		GameListener.getInstance().addGameActionListener(this);
	}
	
	@Override
	public String getCode() {
		return "BlockPlayerAttack";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		actionToBlock.withdraw();
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		actionToBlock.withdraw();
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(actionToBlock == null) return false;
		if(!((Summon)owningCard).getActivityStatus().equals(Summon.READY)) return false;
		return true;
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			((Summon)owningCard).setActivityStatus(Summon.USED);
			Summon attacker = (Summon) actionToBlock.getCard();
			Summon defender = (Summon) owningCard;
			ProcessesBattle battle = new Battle();
			battle.setCombatants(attacker, defender);
			GameListener.getInstance().actionExecuted(this);
			battle.start();
		}
	}

	@Override
	public void actionActivated(GameAction action) {
		if(action.getCode().equals("AttackPlayer")) {
			if(action.getCard().getOwningPlayer() != owningCard.getOwningPlayer()) {
				actionToBlock = (AttackPlayer)action;
			}
		}

	}

	@Override
	public void actionExecuted(GameAction action) {
		// Do nothing
	}

}