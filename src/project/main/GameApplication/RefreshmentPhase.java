package project.main.GameApplication;

import java.util.ArrayList;

import project.main.Card.ActivityStatus;
import project.main.Card.Card;
import project.main.Card.Summon;
import project.main.Listeners.GameListener;
import project.main.jsonObjects.ActionDefinitionLibrary;

public class RefreshmentPhase extends GamePhase {
	
	public RefreshmentPhase() {
		super("RefreshmentPhase");
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void restorePhaseStatus() {
		inactivateAll();
		Player activPlayer = game.getActivePlayer();
		int usedEnergy = activPlayer.getMagicEnergyStock().getUsedEnergy();
		activPlayer.getMagicEnergyStock().increaseFreeEnergyFromUsed(usedEnergy);
		SummonZone summonZone = (SummonZone)activPlayer.getGameZone("SummonZone");
		ArrayList<Card> cards = summonZone.getCards();
		for(Card card : cards) {
			Summon summon = (Summon)card;
			ActivityStatus status = summon.getActivityStatus();
			
			if(status.getStatus().equals(ActivityStatus.USED)) {
				if(status.getDurability() == 0) {
					summon.setActivityStatus(ActivityStatus.READY, -1);
				}else status.decreaseDurability();
			}else if(status.getStatus().equals(ActivityStatus.IMMOBILIZED)) {
				if(status.getDurability() == 0) {
					summon.setActivityStatus(ActivityStatus.READY, -1);
				}else status.decreaseDurability();
			}
		}
		ArrayList<IsAreaInGame> zones = activPlayer.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.activate(activPlayer, this);
		}
	}

	@Override
	public void leave() {
		finishedStacks.add(activeGameStack);
		activeGameStack = null;
		Player activPlayer = game.getActivePlayer();
		ArrayList<IsAreaInGame> zones = activPlayer.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.deavtivateAll();
		}
		GameListener.getInstance().phaseEnded(this);
	}
}
