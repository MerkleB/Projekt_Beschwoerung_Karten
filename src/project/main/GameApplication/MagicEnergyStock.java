package project.main.GameApplication;

import java.util.ArrayList;

import project.main.Card.Card;
import project.main.Card.CollectsMagicEnergy;
import project.main.Card.MagicCollector;
import project.main.Card.Summon;
import project.main.Listeners.PhaseListener;

public class MagicEnergyStock implements CollectsMagicEnergy, PhaseListener {

    private IsAreaInGame collectorZone;
    private IsAreaInGame summonZone;
	
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
	public void phaseStarted(IsPhaseInGame phase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void phaseEnded(IsPhaseInGame phase) {
		// TODO Auto-generated method stub
		
	}

}
