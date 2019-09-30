package main.Action;

import main.GameApplication.IsAreaInGame;
import main.GameApplication.Player;
import main.Listeners.GameActionListener;
import main.Listeners.GameListener;
import main.exception.NoCollectorException;
import main.exception.NotActivableException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;
import main.Card.*;

public class Heal extends Action{
	
	private Card cardToHeal;
	public GameAction selectAction;
	public boolean collectorHealed;
	public boolean summonHealed;
	
	@Override
	public String getCode() {
		return "Heal";
	}
	
	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);
		clear();
		selectCardToHeal();
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		clear();
		selectCardToHeal();
		GameListener.getInstance().actionActivated(this);
		execute();
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
		if(!((Summon)owningCard).getActivityStatus().equals(Summon.READY)) return false;
		if(actionIsActivFor.getGameZone(SummonZone).getCards().size() == 0 && actionIsActivFor.getGameZone(CollectorZone).getCards().size() == 0) return false;
		return true;
	}

	@Override
	public void execute() {
		if(isActivated && !isWithdrawn()) {
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
			((Summon)owningCard).setActivityStatus(Summon.USED);
			GameListener.getInstance().actionExecuted(this);
			game.getActivePhase().restorePhaseStatus();
		}
	}
	
	private void selectCardToHeal() {
		if(((Summon)owningCard).getStatus().getSummonClass().equals("Healer")) {
			for(Player player : game.getPlayers()) {
				ArrayList<IsAreaInGame> zones = player.getGameZones();
				for(IsAreaInGame zone : zones) {
					zone.deavtivateAll();
					if(player == actionIsActivFor) {
						if(zone.getName().equals(SummonZone) || zone.getName().equals(CollectorZone)) {
							ArrayList<Card> cards = zone.getCards();
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
			game.prompt(actionIsActivFor, "Please select the Summon or Magic Collector to heal.");
			GameActionListener listener = new GameActionListener() {
				
				@Override
				public void actionExecuted(GameAction action) {
					Hashtable<String, String> metadata = action.getMetaData();
					if(action.getCode().equals(SummonSelect)) {
						UUID id = UUID.fromString(metadata.get("Summon-ID"));
						cardToHeal = owningCard.getOwningPlayer().getGameZone(SummonZone).findCard(id);
						selectAction = action;
						summonHealed = true;
						
					}
					if(action.getCode().equals(CollectorSelect)) {
						UUID id = UUID.fromString(metadata.get("Collector-ID"));
						cardToHeal = owningCard.getOwningPlayer().getGameZone(CollectorZone).findCard(id);
						selectAction = action;
						collectorHealed = true;
					}
				}
				
				@Override
				public void actionActivated(GameAction action) {
				}
			};
			GameListener.getInstance().addGameActionListener(listener);
			while(cardToHeal == null);
			GameListener.getInstance().removeGameActionListener(listener);
		}else {
			cardToHeal = owningCard;
		}
	}
	
	private void clear() {
		cardToHeal = null;
		selectAction = null;
		collectorHealed = false;
		summonHealed = false;
	}

}
