package project.main.Action;

import project.main.Card.*;
import project.main.GameApplication.Application;
import project.main.GameApplication.Battle;
import project.main.GameApplication.Player;
import project.main.GameApplication.ProcessesBattle;
import project.main.Listeners.GameActionListener;
import project.main.Listeners.GameListener;
import project.main.exception.NotActivableException;
import project.main.util.GameMessageProvider;

public class BlockCollectorAttack extends Action implements GameActionListener {
	
	public AttackCollector actionToBlock;
	
	@Override
	public void initialize() {
		super.initialize();
		actionToBlock = null;
		GameListener.getInstance().addGameActionListener(this);
	}

	@Override
	public String getCode() {
		return "BlockCollectorAttack";
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
			actionToBlock = null;
		}
	}

	@Override
	public void actionActivated(GameAction action) {
		if(action.getCode().equals("SelectSummoningCircle")) return;
		if(((Summon)owningCard).getActivityStatus().getStatus().equals(ActivityStatus.NOT_IN_GAME)) return;
		if(owningCard.getOwningPlayer().getGameZone(SummonZone).findCard(owningCard.getID()) != owningCard) return;
		
		if(action.getCode().equals("AttackCollector") && action.getCard().getOwningPlayer() != owningCard.getOwningPlayer()) {
			actionToBlock = (AttackCollector)action;
		}
		if(action.getCode().equals("SelectMagicCollector") && actionToBlock != null && action.getCard().getOwningPlayer() == owningCard.getOwningPlayer()) {
			setActiv(owningCard.getOwningPlayer());
			String[] parameters = {action.getCard().getCollector().getRealCard().getName(), action.getCard().getCollector().getFreeEnergy()+"", action.getCard().getCollector().getBlockedEnergy()+"", action.getCard().getCollector().getUsedEnergy()+"", action.getCard().getCollector().getDepletedEnergy()+""};
			game.prompt(owningCard.getOwningPlayer(), GameMessageProvider.getInstance().getMessage("#10", Application.getInstance().getLanguage(), parameters));
		}

	}

	@Override
	public void actionExecuted(GameAction action) {
		// Do nothing
	}

}
