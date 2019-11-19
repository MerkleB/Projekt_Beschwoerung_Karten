package project.main.Action;

import java.util.ArrayList;
import java.util.UUID;

import project.main.Card.*;
import project.main.GameApplication.Application;
import project.main.GameApplication.Battle;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.Player;
import project.main.GameApplication.ProcessesBattle;
import project.main.Listeners.GameActionListener;
import project.main.Listeners.GameListener;
import project.main.exception.NotActivableException;
import project.main.util.GameMessageProvider;

public class DeclareBattle extends Action {
	
	public ProcessesBattle battle;
	public Summon attackedSummon;
	public GameActionListener listener;
	
	@Override
	public String getCode() {
		return "DeclareBattle";
	}

	@Override
	public void execute() {
		if(isActivated && !isWithdrawn()) {
			super.execute();
			((Summon)owningCard).setActivityStatus(ActivityStatus.USED, 0);
			battle.setCombatants((Summon)owningCard, attackedSummon);
			GameListener.getInstance().removeGameActionListener(listener);
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
		if(!((Summon) owningCard).getActivityStatus().getStatus().equals(ActivityStatus.READY)) return false;
		Player otherPlayer = game.getOtherPlayer(activator);
		if(otherPlayer.getGameZone(SummonZone).getCards().size() == 0) return false;
		return true;
	}
	
	private void initializeBattle() {
		attackedSummon = null;
		battle = Battle.getInstance();
		battle.setGame(game);
		Player opponentPlayer = null;
		for(Player player : game.getPlayers()) {
			if(player != actionIsActivFor) {
				opponentPlayer = player;
			}
			ArrayList<IsAreaInGame> zones = player.getGameZones();
			for(IsAreaInGame zone : zones) {
				ArrayList<Card> cards = zone.getCards();
				for(Card card : cards) {
					if(card != owningCard) {
						card.setInactive();
					}
				}
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
			listener = new GameActionListener() {
				
				@Override
				public void actionExecuted(GameAction action) {
					if(action.getCode().equals(SummonSelect)) {
						String id = action.getMetaData().get("Summon-ID");
						attackedSummon = (Summon)zone.findCard(id);
					}
				}
				
				@Override
				public void actionActivated(GameAction action) {
				}
			};
			GameListener.getInstance().addGameActionListener(listener);
			game.prompt(actionIsActivFor, GameMessageProvider.getInstance().getMessage("#3", Application.getInstance().getLanguage()));
		}
	}

}
