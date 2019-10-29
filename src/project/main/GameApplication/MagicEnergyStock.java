package project.main.GameApplication;

import java.util.ArrayList;

import project.main.Action.GameAction;
import project.main.Card.ActivityStatus;
import project.main.Card.Card;
import project.main.Card.CollectsMagicEnergy;
import project.main.Card.MagicCollector;
import project.main.Card.Summon;
import project.main.Listeners.GameActionListener;
import project.main.Listeners.GameListener;
import project.main.Listeners.ZoneListener;
import project.main.util.GameMessageProvider;

public class MagicEnergyStock implements CollectsMagicEnergy, ZoneListener {

    private IsAreaInGame collectorZone;
    private IsAreaInGame summonZone;
    private GameActionListener listener;
    
    public MagicEnergyStock(IsAreaInGame collectorZone, IsAreaInGame summonZone) {
    	this.collectorZone = collectorZone;
    	this.summonZone = summonZone;
    	GameListener.getInstance().addZoneListener(this);
	}
	
	@Override
	public int getFreeEnergy() {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = 0;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = energy + collector.getFreeEnergy();
		}
		return energy;
	}

	@Override
	public int getUsedEnergy() {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = 0;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = energy + collector.getUsedEnergy();
		}
		return energy;
	}

	@Override
	public int getBlockedEnergy() {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = 0;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = energy + collector.getBlockedEnergy();
		}
		return energy;
	}

	@Override
	public int getDepletedEnergy() {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = 0;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = energy + collector.getDepletedEnergy();
		}
		return energy;
	}

	@Override
	public int restoreFreeEnergy() {
		ArrayList<Card> collectors = collectorZone.getCards();
		int usedEnergy = getUsedEnergy();
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			usedEnergy = collector.increaseFreeEnergyFromUsed(usedEnergy);
			if(usedEnergy == 0) {
				break;
			}
		}
		ArrayList<Card> summons = summonZone.getCards();
		for(int i=0; i<summons.size(); i++) {
			Summon summon = (Summon)summons.get(i);
			useEnergy(summon.getStatus().getMagicPreservationValue());
		}
		return getFreeEnergy();
	}

	@Override
	public int increaseFreeEnergy(int magicEnergy) {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = magicEnergy;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = collector.increaseFreeEnergy(energy);
			if(energy == 0) {
				break;
			}
		}
		return energy;
	}

	@Override
	public int decreaseFreeEnergy(int magicEnergy) {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = magicEnergy;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = collector.decreaseFreeEnergy(energy);
			if(energy == 0) {
				break;
			}
		}
		return energy;
	}

	@Override
	public int increaseFreeEnergyFromUsed(int magicEnergy) {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = magicEnergy;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = collector.increaseFreeEnergyFromUsed(energy);
			if(energy == 0) {
				break;
			}
		}
		return energy;
	}

	@Override
	public int increaseFreeEnergyFromBlocked(int magicEnergy) {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = magicEnergy;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = collector.increaseFreeEnergyFromBlocked(energy);
			if(energy == 0) {
				break;
			}
		}
		return energy;
	}

	@Override
	public int increaseFreeEnergyFromDepleted(int magicEnergy) {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = magicEnergy;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = collector.increaseFreeEnergyFromDepleted(energy);
			if(energy == 0) {
				break;
			}
		}
		return energy;
	}

	@Override
	public int useEnergy(int magicEnergy) {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = magicEnergy;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = collector.useEnergy(energy);
			if(energy == 0) {
				break;
			}
		}
		return energy;
	}

	@Override
	public int depleteEnergy(int magicEnergy) {
		int remainingEnergyToDeplete = 0;
		ArrayList<Card> collectors = collectorZone.getCards();
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			int depletingEnergy = magicEnergy;
			if(remainingEnergyToDeplete > 0) {
				depletingEnergy = remainingEnergyToDeplete;
			}
			remainingEnergyToDeplete = collector.depleteEnergyFromBlocked(depletingEnergy);
			if(remainingEnergyToDeplete == 0) break;
		}
		if(remainingEnergyToDeplete > 0) {
			for(int i=0; i<collectors.size(); i++) {
				MagicCollector collector = (MagicCollector)collectors.get(i);
				int depletingEnergy = 0;
				if(remainingEnergyToDeplete > 0) {
					depletingEnergy = remainingEnergyToDeplete;
				}
				remainingEnergyToDeplete = collector.depleteEnergyFromFree(depletingEnergy);
				if(remainingEnergyToDeplete == 0) break;
			}
		}
		if(remainingEnergyToDeplete > 0) {
			for(int i=0; i<collectors.size(); i++) {
				MagicCollector collector = (MagicCollector)collectors.get(i);
				int depletingEnergy = 0;
				if(remainingEnergyToDeplete > 0) {
					depletingEnergy = remainingEnergyToDeplete;
				}
				remainingEnergyToDeplete = collector.depleteEnergyFromUsed(depletingEnergy);
				if(remainingEnergyToDeplete == 0) break;
			}
		}
		int energyWhichCanAutomaticallyBlocked = checkIfThereIsEnoughEnergyForSummonPreservation();
		if(energyWhichCanAutomaticallyBlocked > 0) {
			blockEnergy(energyWhichCanAutomaticallyBlocked);
		}
		return remainingEnergyToDeplete;
	}
	
	private int checkIfThereIsEnoughEnergyForSummonPreservation() {
		int missingEnergy = 0;
		ArrayList<Card> summons = summonZone.getCards();
		int neededEnergyForPreservation = 0;
		for(int i=0; i<summons.size(); i++) {
			Summon summon = (Summon)summons.get(i);
			neededEnergyForPreservation = neededEnergyForPreservation + summon.getStatus().getMagicPreservationValue();
		}
		int blockedEnergy = getBlockedEnergy();
		if(blockedEnergy < neededEnergyForPreservation) {
			missingEnergy = neededEnergyForPreservation - blockedEnergy - getFreeEnergy();
			if(missingEnergy > 0) {
				missingEnergy = -1;
				promptPlayerToSelectSummonsToImmobilize(neededEnergyForPreservation);
			}else missingEnergy = neededEnergyForPreservation;
		}
		return missingEnergy;
	}
	
	private void promptPlayerToSelectSummonsToImmobilize(int neededEnergy) {
		ArrayList<Card> summons = summonZone.getCards();
		ArrayList<String> action = new ArrayList<String>();
		action.add("SelectSummon");
		for(int i=0; i<summons.size(); i++) {
			Summon summon = (Summon)summons.get(i);
			summon.setInactive();
			summon.setActiv(action, summon.getOwningPlayer());					
		}
		listener = new GameActionListener() {
			
			ArrayList<GameAction> actions = new ArrayList<GameAction>();
			int neededEnergyToBlock = neededEnergy;
			int neededEnergyUntilEndOfSelect = neededEnergy;
			boolean stillActive = true;
			
			@Override
			public void actionExecuted(GameAction action) {
				if(stillActive) {
					boolean actionRegistered = false;
					for(int i=0; i<actions.size(); i++) {
						if(action == actions.get(i)) {
							actionRegistered = true;
							break;
						}
					}
					if(actionRegistered) {
						int preservationValue = ((Summon)action.getCard()).getStatus().getMagicPreservationValue();
						//Decrease the needed energy by the summon which gets immobilized
						neededEnergyToBlock = neededEnergyToBlock - preservationValue; 
						((Summon)action.getCard()).getActivityStatus().setStatus(ActivityStatus.IMMOBILIZED, -1);
						increaseFreeEnergyFromBlocked(preservationValue);
						neededEnergyToBlock = blockEnergy(neededEnergyToBlock);
						if(neededEnergyToBlock <= 0) {
							stillActive = false;
						}
					}
				}
			}
			
			@Override
			public void actionActivated(GameAction action) {
				if(stillActive) {
					if(action.getCode().equals("SelectSummon")) {
						if(neededEnergyUntilEndOfSelect - getFreeEnergy() > 0) {
							neededEnergyUntilEndOfSelect = neededEnergyUntilEndOfSelect - ((Summon)action.getCard()).getStatus().getMagicPreservationValue();
							actions.add(action);
						}
					}
				}
			}
		};
		GameListener.getInstance().addGameActionListener(listener);
		collectorZone.getGame().prompt(collectorZone.getOwner(), GameMessageProvider.getInstance().getMessage("#7", Application.getInstance().getLanguage()));
	}

	@Override
	public int depleteEnergyFromBlocked(int magicEnergy) {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = magicEnergy;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = collector.depleteEnergyFromBlocked(energy);
			if(energy == 0) {
				break;
			}
		}
		return energy;
	}

	@Override
	public int depleteEnergyFromFree(int magicEnergy) {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = magicEnergy;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = collector.depleteEnergyFromFree(energy);
			if(energy == 0) {
				break;
			}
		}
		return energy;
	}

	@Override
	public int depleteEnergyFromUsed(int magicEnergy) {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = magicEnergy;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = collector.depleteEnergyFromUsed(energy);
			if(energy == 0) {
				break;
			}
		}
		return energy;
	}

	@Override
	public int blockEnergy(int magicEnergy) {
		ArrayList<Card> collectors = collectorZone.getCards();
		int energy = magicEnergy;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = collector.blockEnergy(energy);
			if(energy == 0) {
				break;
			}
		}
		return energy;
	}
	
	@Override
	public void cardAdded(IsAreaInGame zone, Card card) {
		if(zone == collectorZone) {
			ArrayList<Card> cards = summonZone.getCards();
			ArrayList<String> actions = new ArrayList<String>();
			actions.add("UnimmobilizeSummon");
			for(Card summon : cards) {
				if(((Summon)summon).getActivityStatus().getStatus().equals(ActivityStatus.IMMOBILIZED) && ((Summon)summon).getActivityStatus().getDurability() == -1) {
					summon.setActiv(actions, summon.getOwningPlayer());
				}
			}
		}
	}

	@Override
	public void cardRemoved(IsAreaInGame zone, Card card) {
		if(zone == collectorZone) {
			checkIfThereIsEnoughEnergyForSummonPreservation();
		}
	}

}
