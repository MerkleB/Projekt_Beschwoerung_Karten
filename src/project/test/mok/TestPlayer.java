package project.test.mok;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import project.main.Card.Card;
import project.main.Card.CollectsMagicEnergy;
import project.main.Card.MagicCollector;
import project.main.Card.Summon;
import project.main.GameApplication.CollectorZone;
import project.main.GameApplication.DeckZone;
import project.main.GameApplication.DiscardPile;
import project.main.GameApplication.Game;
import project.main.GameApplication.HandZone;
import project.main.GameApplication.IsAreaInGame;
import project.main.GameApplication.MagicEnergyStock;
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
	private CollectsMagicEnergy magicEnergyStock;
	
	public TestPlayer(int sp, int hp, ArrayList<Card> deck) {
		id = UUID.randomUUID();
		zones = new ArrayList<IsAreaInGame>();
		zonesTable = new Hashtable<String, IsAreaInGame>();
		zones.add(new HandZone(this));
		zones.add(new DeckZone(this, deck));
		IsAreaInGame summonZone = new SummonZone(this);
		zones.add(summonZone);
		IsAreaInGame collectorZone = new CollectorZone(this);
		zones.add(collectorZone);
		zones.add(new DiscardPile(this));
		for(IsAreaInGame zone : zones) {
			zonesTable.put(zone.getName(), zone);
		}
		summoningPoints = sp;
		healthPoints = hp;
		collectorActions = 2;
		magicEnergyStock = new MagicEnergyStock(collectorZone, summonZone);
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
		if(healthPoints < 0) {
			healthPoints = healthPoints*(-1)+healthPoints;
		}
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

	@Override
	public CollectsMagicEnergy getMagicEnergyStock() {
		return magicEnergyStock;
	}

	@Override
	public void setGame(Game game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Game getGame() {
		// TODO Auto-generated method stub
		return null;
	}

}
