package main.GameApplication;

import java.util.ArrayList;

import main.Card.Card;
import main.Card.Summon;
import main.jsonObjects.ActionDefinitionLibrary;

public class RefreshmentPhase implements IsPhaseInGame {
	
	private ArrayList<String> actionsToActivate;
	
	@Override
	public String getName() {
		return "RefreshmentPhase";
	}

	@Override
	public void restorePhaseStatus() {
		Player activPlayer = Application.getInstance(null).getGame().getActivePlayer();
		int usedEnergy = activPlayer.getUsedEnergy();
		activPlayer.increaseFreeEnergyFromUsed(usedEnergy);
		SummonZone summonZone = (SummonZone)activPlayer.getGameZone("SummonZone");
		ArrayList<Card> cards = summonZone.getCards();
		for(Card card : cards) {
			Summon summon = (Summon)card;
			if(summon.getActivityStatus().equals(Summon.USED)) {
				summon.setActivityStatus(Summon.READY);
			}
		}
	}

	@Override
	public ArrayList<String> getActionsToActivate() {
		if(actionsToActivate == null) {
			actionsToActivate = ActionDefinitionLibrary.getInstance().getPhaseActions(getName());
		}
		return actionsToActivate;
	}

	@Override
	public void process() {
		restorePhaseStatus();
	}

	@Override
	public void leave() {
		Player activPlayer = Application.getInstance(null).getGame().getActivePlayer();
		ArrayList<IsAreaInGame> zones = activPlayer.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.deavtivateAll();
		}
	}

}
