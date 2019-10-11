package project.test.mok;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import project.main.Card.Card;
import project.main.Card.MagicCollector;
import project.main.Card.Summon;
import project.main.GameApplication.CollectorZone;
import project.main.GameApplication.DeckZone;
import project.main.GameApplication.DiscardPile;
import project.main.GameApplication.HandZone;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.Player;
import project.main.GameApplication.SummonZone;

public class TestPlayer implements Player {
	
	private UUID id;
	private ArrayList<IsAreaInGame> zones;
	private Hashtable<String, IsAreaInGame> zonesTable;
	private int summoningPoints;
	private int healthPoints;
	private int collectorActions;
	private TestGame game;
	private PhysicalTestPlayer realPlayer;
	
	public TestPlayer(int sp, int hp, ArrayList<Card> deck) {
		id = UUID.randomUUID();
		zones = new ArrayList<IsAreaInGame>();
		zonesTable = new Hashtable<String, IsAreaInGame>();
		zones.add(new HandZone(this));
		zones.add(new DeckZone(this, deck));
		zones.add(new SummonZone(this));
		zones.add(new CollectorZone(this));
		zones.add(new DiscardPile(this));
		for(IsAreaInGame zone : zones) {
			zonesTable.put(zone.getName(), zone);
		}
		summoningPoints = sp;
		healthPoints = hp;
		collectorActions = 2;
	}
	
	@Override
	public int getFreeEnergy() {
		ArrayList<Card> collectors = zonesTable.get("CollectorZone").getCards();
		int energy = 0;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = energy + collector.getFreeEnergy();
		}
		return energy;
	}

	@Override
	public int getUsedEnergy() {
		ArrayList<Card> collectors = zonesTable.get("CollectorZone").getCards();
		int energy = 0;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = energy + collector.getUsedEnergy();
		}
		return energy;
	}

	@Override
	public int getDepletedEnergy() {
		ArrayList<Card> collectors = zonesTable.get("CollectorZone").getCards();
		int energy = 0;
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			energy = energy + collector.getDepletedEnergy();
		}
		return energy;
	}

	@Override
	public int restoreFreeEnergy() {
		ArrayList<Card> collectors = zonesTable.get("CollectorZone").getCards();
		int usedEnergy = getUsedEnergy();
		for(int i=0; i<collectors.size(); i++) {
			MagicCollector collector = (MagicCollector)collectors.get(i);
			usedEnergy = collector.increaseFreeEnergyFromUsed(usedEnergy);
			if(usedEnergy == 0) {
				break;
			}
		}
		ArrayList<Card> summons = zonesTable.get("SummonZone").getCards();
		for(int i=0; i<summons.size(); i++) {
			Summon summon = (Summon)summons.get(i);
			useEnergy(summon.getStatus().getMagicPreservationValue());
		}
		return getFreeEnergy();
	}

	@Override
	public int increaseFreeEnergy(int magicEnergy) {
		ArrayList<Card> collectors = zonesTable.get("CollectorZone").getCards();
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
		ArrayList<Card> collectors = zonesTable.get("CollectorZone").getCards();
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
		ArrayList<Card> collectors = zonesTable.get("CollectorZone").getCards();
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
		ArrayList<Card> collectors = zonesTable.get("CollectorZone").getCards();
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
		ArrayList<Card> collectors = zonesTable.get("CollectorZone").getCards();
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
		ArrayList<Card> collectors = zonesTable.get("CollectorZone").getCards();
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
		ArrayList<Card> collectors = zonesTable.get("CollectorZone").getCards();
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
	public IsAreaInGame getGameZone(String zoneName) {
		return zonesTable.get(zoneName);
	}

	@Override
	public ArrayList<IsAreaInGame> getGameZones() {
		return zones;
	}

	@Override
	public UUID getID() {
		return id;
	}

	@Override
	public int getSummoningPoints() {
		return summoningPoints;
	}

	@Override
	public int decreaseSummonigPoints(int costs) {
		summoningPoints = summoningPoints - costs;
		return summoningPoints;
	}

	@Override
	public int addSummoningPoints(int points) {
		summoningPoints = summoningPoints + points;
		return summoningPoints;
	}

	@Override
	public int getHealthPoints() {
		return healthPoints;
	}

	@Override
	public int increaseHealthPoints(int heal) {
		healthPoints = healthPoints + heal;
		return healthPoints;
	}

	@Override
	public int decreaseHealthPoints(int damage) {
		healthPoints = healthPoints - damage;
		return healthPoints;
	}

	@Override
	public int getNumberOfRemainingCollectorActions() {
		return collectorActions;
	}

	@Override
	public void increaseNumberOfRemainingCollectorActions(int actions) {
		collectorActions = collectorActions + actions;
	}

	@Override
	public void decreaseNumberOfRemainingCollectorActions(int actions) {
		collectorActions = collectorActions - actions;
	}
	
	public void setController(PhysicalTestPlayer p) {
		realPlayer = p;
	}
	
	public PhysicalTestPlayer getController() {
		return realPlayer;
	}

}
