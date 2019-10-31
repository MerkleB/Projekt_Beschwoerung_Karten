package project.main.Action;

import java.util.ArrayList;

import project.main.Card.ActivityStatus;
import project.main.Card.Card;
import project.main.Card.MagicCollector;
import project.main.Card.Summon;
import project.main.GameApplication.Application;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.Player;
import project.main.Listeners.GameActionListener;
import project.main.Listeners.GameListener;
import project.main.exception.NoCollectorException;
import project.main.exception.NotActivableException;
import project.main.util.GameMessageProvider;

public class AttackCollector extends Action {
	
	public MagicCollector attackedCollector;
	private GameActionListener listener;
	private SelectMagicCollector selectAction;
	
	@Override
	public String getCode() {
		return "AttackCollector";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		selectAttackedCollector();
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		selectAttackedCollector();
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void execute() {
		if(isActivated && ! withdrawn) {
			super.execute();
			((Summon)owningCard).setActivityStatus(ActivityStatus.USED, 0);
			int damage = ((Summon)owningCard).getStatus().getAttack();
			try {
				attackedCollector.decreaseCurrentHealth(damage);
			} catch (NoCollectorException e) {
				throw new RuntimeException("Collector which was not a collector was attacked. "+e.getMessage());
			}
			GameListener.getInstance().actionExecuted(this);
		}
		GameListener.getInstance().removeGameActionListener(listener);
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(!((Summon) owningCard).getActivityStatus().getStatus().equals(ActivityStatus.READY)) return false;
		Player otherPlayer = game.getOtherPlayer(activator);
		if(otherPlayer.getGameZone(CollectorZone).getCards().size() == 0) return false;
		return true;
	}
	
	private void selectAttackedCollector() {
		setSelectActionActiv();
		listenToSelectAction();
	}
	
	private void setSelectActionActiv() {
		Player otherPlayer = game.getOtherPlayer(actionIsActivFor);
		for(Player player : game.getPlayers()) {
			ArrayList<IsAreaInGame> zones = player.getGameZones();
			for(IsAreaInGame zone : zones) {
				ArrayList<Card> cards = zone.getCards();
				for(Card card : cards) {
					if(card != owningCard) {
						card.setInactive();
					}
				}
				if(player == otherPlayer) {
					if(zone.getName().equals(CollectorZone)) {
						for(Card card : cards) {
							ArrayList<GameAction> actions = card.getActions();
							for(GameAction action : actions) {
								if(action.getCode().equals(CollectorSelect)) {
									action.setActivBy(this, actionIsActivFor);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private void listenToSelectAction() {
		game.prompt(actionIsActivFor, GameMessageProvider.getInstance().getMessage("#8", Application.getInstance().getLanguage()));
		listener = new GameActionListener() {
			
			/**
			 * If the executed action is the same as the registered select-Action retrieve the selected card.
			 */
			@Override
			public void actionExecuted(GameAction action) {
				if(action != selectAction) return;
				attackedCollector = action.getCard().getCollector();
			}
			
			/**
			 * Registers a select action and if one was selected all actions are set as inactiv.
			 */
			@Override
			public void actionActivated(GameAction action) {
				if(selectAction == null) {
					if(action.getCode().equals(CollectorSelect)) {
						selectAction = (SelectMagicCollector) action;
					}
					
					if(selectAction != null) {
						owningCard.getOwningPlayer().getGameZone(SummonZone).deavtivateAll();
						owningCard.getOwningPlayer().getGameZone(CollectorZone).deavtivateAll();
					}
				}
			}
		};
		GameListener.getInstance().addGameActionListener(listener);
	}

}
