package main.Action;

import java.util.ArrayList;
import java.util.UUID;

import main.Card.*;
import main.GameApplication.Battle;
import main.GameApplication.IsAreaInGame;
import main.GameApplication.Player;
import main.GameApplication.ProcessesBattle;
import main.Listeners.GameActionListener;
import main.Listeners.GameListener;
import main.exception.NotActivableException;

public class DeclareBattle extends Action {
	
	public ProcessesBattle battle;
	public Summon attackedSummon;
	
	@Override
	public String getCode() {
		return "DeclareBattle";
	}

	@Override
	public void execute() {
		if(isActivated && !isWithdrawn()) {
			battle.start();
			while(battle.getStatus().equals(ProcessesBattle.RUNNING));
			game.getActivePhase().restorePhaseStatus();
		}
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		initializeBattle();
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		initializeBattle();
		GameListener.getInstance().actionActivated(this);
		execute();
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(!((Summon) owningCard).getActivityStatus().equals(Summon.READY)) return false;
		return true;
	}
	
	private void initializeBattle() {
		attackedSummon = null;
		battle = Battle.getInstance();
		Player opponentPlayer = null;
		for(Player player : game.getPlayers()) {
			if(player != actionIsActivFor) {
				opponentPlayer = player;
			}
			ArrayList<IsAreaInGame> zones = player.getGameZones();
			for(IsAreaInGame zone : zones) {
				zone.deavtivateAll();
			}
		}
		if(opponentPlayer == null) {
			throw new RuntimeException("Opponent was not found");
		}else {
			IsAreaInGame zone = opponentPlayer.getGameZone(SummonZone);
			ArrayList<Card> cards = zone.getCards();
			for(Card card : cards) {
				ArrayList<GameAction> actions = card.getActions();
				for(GameAction action : actions) {
					if(action.getCode().equals(SummonSelect)) {
						action.setActiv(actionIsActivFor);
						break;
					}
				}
			}
			GameActionListener listener = new GameActionListener() {
				
				@Override
				public void actionExecuted(GameAction action) {
					if(action.getCode().equals(SummonSelect)) {
						String id = action.getMetaData().get("Summon-ID");
						attackedSummon = (Summon)zone.findCard(UUID.fromString(id));
					}
				}
				
				@Override
				public void actionActivated(GameAction action) {
				}
			};
			game.prompt(actionIsActivFor, "Select the Summon you wan to attack!");
			GameListener.getInstance().addGameActionListener(listener);
			while(attackedSummon == null);
			GameListener.getInstance().removeGameActionListener(listener);
		}
	}

}
