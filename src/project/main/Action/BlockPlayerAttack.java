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
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(actionToBlock == null) return false;
		if(!((Summon)owningCard).getActivityStatus().getStatus().equals(ActivityStatus.READY)) return false;
		return true;
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn) {
			super.execute();
			actionToBlock.withdraw();
			((Summon)owningCard).setActivityStatus(ActivityStatus.USED, 0);
			Summon attacker = (Summon) owningCard;
			Summon defender = (Summon) actionToBlock.getCard();
			defender.setActivityStatus(ActivityStatus.USED, 0);
			ProcessesBattle battle = Battle.getInstance();
			battle.setCombatants(attacker, defender);
			battle.setGame(game);
			GameListener.getInstance().actionExecuted(this);
			battle.start();
		}
	}

	@Override
	public void actionActivated(GameAction action) {
		if(action.getCode().equals("AttackPlayer")) {
			if(action.getCard().getOwningPlayer() != owningCard.getOwningPlayer() && !((Summon)owningCard).getActivityStatus().getStatus().equals(ActivityStatus.NOT_IN_GAME)) {
				game.forbidGameStackProcessing(owningCard.getOwningPlayer());
				actionToBlock = (AttackPlayer)action;
				setActiv(owningCard.getOwningPlayer());
			}
		}

	}

	@Override
	public void actionExecuted(GameAction action) {
		// Do nothing
	}

}
