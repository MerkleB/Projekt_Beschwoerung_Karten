package project.main.Action;

import java.util.ArrayList;

import project.main.Card.*;
import project.main.GameApplication.Application;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.Player;
import project.main.GameApplication.SummoningCircle;
import project.main.Listeners.GameActionListener;
import project.main.Listeners.GameListener;
import project.main.exception.NotActivableException;
import project.main.util.GameMessageProvider;

public class EvokeSummon extends Action {
	
	private GameActionListener listener;
	private SelectSummoningCircle selectAction;
	private SummoningCircle circleToSummon;
	
	@Override
	public String getCode() {
		return "EvokeSummon";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		super.activate(activator);	
		initMetadata();
		selectSummoningCircle();
		metadata.put("Target-ID", owningCard.getID().toString());
		game.getActivePhase().getActiveGameStack().addEntry(this);
		GameListener.getInstance().actionActivated(this);
		
	}
	
	@Override
	public void activateBy(Stackable activator, Player activatingPlayer) throws NotActivableException {
		super.activateBy(activator, activatingPlayer);
		initMetadata();
		selectSummoningCircle();
		metadata.put("Target-ID", owningCard.getID().toString());
		GameListener.getInstance().actionActivated(this);
	}

	@Override
	public boolean activateable(Player activator) {
		if(!super.activateable(activator)) return false;
			Player ownerOfCard = owningCard.getOwningPlayer();
			if(ownerOfCard.getSummoningPoints() >= ((Summon) owningCard).getStatus().getSummoningPoints()) {
				if(ownerOfCard.getMagicEnergyStock().getFreeEnergy() >= ((Summon) owningCard).getStatus().getMagicPreservationValue()) {
					return true;
				}
			}
		return false;
	}

	@Override
	public void execute() {
		if(isActivated && !withdrawn && circleToSummon != null) {
			super.execute();
			Player ownerOfCard = owningCard.getOwningPlayer();
			int remainingPoints = ownerOfCard.decreaseSummonigPoints(((Summon) owningCard).getStatus().getSummoningPoints());
			if(remainingPoints != -1) {
				IsAreaInGame handZone = actionIsActivFor.getGameZone("HandZone");
				if(handZone == null) {
					throw new RuntimeException("Fatal error: Player has no HandZone.");
				}
				handZone.removeCard(owningCard);
				project.main.GameApplication.SummonZone summonZone = (project.main.GameApplication.SummonZone)actionIsActivFor.getGameZone("SummonZone");
				if(summonZone == null) {
					throw new RuntimeException("Fatal error: Player has no SummonZone");
				}
				summonZone.addCardToCircle((Summon)owningCard, circleToSummon);
				GameListener.getInstance().actionExecuted(this);
				summonZone.deavtivateAll();
				summonZone.activate(ownerOfCard, game.getActivePhase());
			}
		}
	}
	
	private void selectSummoningCircle() {
		project.main.GameApplication.SummonZone zone = (project.main.GameApplication.SummonZone)owningCard.getOwningPlayer().getGameZone(SummonZone);
		ArrayList<SummoningCircle> circles = zone.getCircles();
		for(SummoningCircle circle : circles) {
			if(circle.isFree()) {
				circle.setActivBy(actionIsActivFor, this);
			}
		}
		listener = new GameActionListener() {
			
			@Override
			public void actionExecuted(GameAction action) {
				if(action == selectAction) {
					circleToSummon = selectAction.circle;
				}
			}
			
			@Override
			public void actionActivated(GameAction action) {
				if(action.getCode().equals("SelectSummoningCircle") && selectAction == null) {
					selectAction = (SelectSummoningCircle) action;
				}
			}
		};
		game.prompt(actionIsActivFor, GameMessageProvider.getInstance().getMessage("#4", Application.getInstance().getLanguage()));
		GameListener.getInstance().addGameActionListener(listener);
	}

}
