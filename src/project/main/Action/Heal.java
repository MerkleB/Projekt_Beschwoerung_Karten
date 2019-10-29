package project.main.Action;

import project.main.Card.*;
import project.main.Effect.Effect;
import project.main.GameApplication.Application;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.Player;
import project.main.Listeners.GameActionListener;
import project.main.Listeners.GameListener;
import project.main.exception.NoCardException;
import project.main.exception.NoCollectorException;
import project.main.exception.NotActivableException;
import project.main.util.GameMessageProvider;

import java.util.ArrayList;

public class Heal extends Action{
	
	private Card cardToHeal;
	public GameAction selectAction;
	public boolean collectorHealed;
	public boolean summonHealed;
	private GameActionListener listener;
	
	@Override
	public String getCode() {
		return "Heal";
	}
	
	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		clear();
		try {
			selectCardToHeal();
		} catch (NoCardException e) {
			throw new NotActivableException("Not activatable: "+e.getMessage());
		}
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		clear();
		try {
			selectCardToHeal();
		} catch (NoCardException e) {
			throw new NotActivableException("Not activatable: "+e.getMessage());
		}
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(!((Summon)owningCard).getActivityStatus().getStatus().equals(ActivityStatus.READY)) return false;
		if(actionIsActivFor.getGameZone(SummonZone).getCards().size() == 0 && actionIsActivFor.getGameZone(CollectorZone).getCards().size() == 0) return false;
		return true;
	}

	@Override
	public void execute() {
		if(isActivated && !isWithdrawn()) {
			super.execute();
			((Summon)owningCard).setActivityStatus(ActivityStatus.USED, 0);
			int heal = ((Summon)owningCard).getStatus().getHeal();
			if(summonHealed) {
				((Summon)cardToHeal).getStatus().increaseVitality(heal);
			}
			if(collectorHealed) {
				try {
					((MagicCollector)cardToHeal).increaseCurrentHealth(heal);
				} catch (NoCollectorException e) {
					throw new RuntimeException("Non-Collector card was selected as collector");
				}
			}
			GameListener.getInstance().removeGameActionListener(listener);
			GameListener.getInstance().actionExecuted(this);
			game.getActivePhase().restorePhaseStatus();
		}
	}
	
	private void selectCardToHeal() throws NoCardException {
		if(isHealer((Summon)owningCard)) {
			setSelectActionsActiv();
			listenToSelectActions();
			
		}else {
			cardToHeal = owningCard;
		}
	}
	
	private boolean isHealer(Summon card) throws NoCardException {
		if(card.getStatus().getSummonClass().equals("Healer")) {
			return true;
		}
		Effect[] effects = card.getEffects();
		for(Effect effect : effects) {
			if(effect.getCode().equals("Healer")) {
				return true;
			}
		}
		return false;
	}
	
	private void setSelectActionsActiv() {
		for(Player player : game.getPlayers()) {
			ArrayList<IsAreaInGame> zones = player.getGameZones();
			for(IsAreaInGame zone : zones) {
				ArrayList<Card> cards = zone.getCards();
				for(Card card : cards) {
					if(card != owningCard) {
						card.setInactive();
					}
				}
				if(player == actionIsActivFor) {
					if(zone.getName().equals(SummonZone) || zone.getName().equals(CollectorZone)) {
						for(Card card : cards) {
							ArrayList<GameAction> actions = card.getActions();
							for(GameAction action : actions) {
								if(zone.getName().equals(SummonZone)) {
									if(action.getCode().equals(SummonSelect)) {
										action.setActivBy(this, actionIsActivFor);
									}
								}else {
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
	}
	
	private void listenToSelectActions() {
		game.prompt(actionIsActivFor, GameMessageProvider.getInstance().getMessage("#2", Application.getInstance().getLanguage()));
		listener = new GameActionListener() {
			
			/**
			 * If the executed action is the same as the registered select-Action retrieve the selected card.
			 */
			@Override
			public void actionExecuted(GameAction action) {
				if(action != selectAction) return;
				
				cardToHeal = action.getCard();
				if(action.getCode().equals(SummonSelect)) {
					summonHealed = true;
					
				}
				if(action.getCode().equals(CollectorSelect)) {
					collectorHealed = true;
				}
			}
			
			/**
			 * Registers a select action and if one was selected all actions are set as inactiv.
			 */
			@Override
			public void actionActivated(GameAction action) {
				if(selectAction == null) {
					if(action.getCode().equals(SummonSelect)) {
						selectAction = action;
					}
					if(action.getCode().equals(CollectorSelect)) {
						selectAction = action;
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
	
	private void clear() {
		cardToHeal = null;
		selectAction = null;
		collectorHealed = false;
		summonHealed = false;
	}

}
