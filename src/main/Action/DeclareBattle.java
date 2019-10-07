package main.Action;

import java.util.ArrayList;
import java.util.UUID;

import main.Card.*;
import main.GameApplication.Application;
import main.GameApplication.Battle;
import main.GameApplication.IsAreaInGame;
import main.GameApplication.Player;
import main.GameApplication.ProcessesBattle;
import main.Listeners.GameActionListener;
import main.Listeners.GameListener;
import main.exception.NotActivableException;
import main.util.GameMessageProvider;

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
			((Summon)owningCard).setActivityStatus(Summon.USED);
			battle.setCombatants((Summon)owningCard, attackedSummon);
			GameListener.getInstance().actionExecuted(this);
			battle.start();
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
						GameListener.getInstance().removeGameActionListener(this);
					}
				}
				
				@Override
				public void actionActivated(GameAction action) {
				}
			};
			game.prompt(actionIsActivFor, GameMessageProvider.getInstance().getMessage("#3", Application.getInstance().getLanguage()));
			GameListener.getInstance().addGameActionListener(listener);
		}
	}

}
