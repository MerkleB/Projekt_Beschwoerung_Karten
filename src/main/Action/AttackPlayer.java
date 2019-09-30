package main.Action;

import main.GameApplication.GameStack;
import main.GameApplication.Player;
import main.Listeners.GameListener;
import main.exception.NotActivableException;
import main.Card.Summon;

public class AttackPlayer extends Action {

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		initMetadata();
		GameStack.getInstance().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		initMetadata();
		GameStack.getInstance().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public String getCode() {
		return "AttackPlayer";
	}
	
	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(((Summon)owningCard).getActivityStatus().equals(Summon.USED) || ((Summon)owningCard).getActivityStatus().equals(Summon.IMMOBILIZED)) {
			return false;
		}
		return true;
	}

	@Override
	public void execute() {
		if(isActivated && !isWithdrawn()) {
			Player[] players = game.getPlayers();
			Player attackedPlayer = null;
			for(Player player : players) {
				if(player != owningCard.getOwningPlayer()) {
					attackedPlayer = player;
					break;
				}
			}
			if(attackedPlayer == null) {
				throw new RuntimeException("No player to attack!");
			}else {
				int damage = ((Summon) owningCard).getStatus().getAttack();
				metadata.put("Damage",damage+"");
				attackedPlayer.decreaseHealthPoints(damage);
				((Summon) owningCard).setActivityStatus(Summon.USED);
			}
		}
		GameListener.getInstance().actionExecuted(this);
	}
}
