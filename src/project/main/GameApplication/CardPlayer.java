package project.main.GameApplication;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

import project.main.Card.Card;
import project.main.Card.CollectsMagicEnergy;
import project.main.exception.NoCardException;

public class CardPlayer implements Player {
	
	private String id;
	private ArrayList<IsAreaInGame> zones;
	private Hashtable<String, IsAreaInGame> zonesTable;
	private int summoningPoints;
	private int healthPoints;
	private int collectorActions;
	private Game game;
	private ControlsStackables controller;
	private CollectsMagicEnergy magicEnergyStock;
	
	public CardPlayer(String name, ArrayList<Card> deck) {
		id = name;
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
		for(Card card : deck) {
			try {
				card.setOwningPlayer(this);
			} catch (NoCardException e) {
				throw new RuntimeException("Non-Real-Card in deck: "+e.getMessage());
			}
		}
		summoningPoints = 2;
		healthPoints = 40;
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
	public String getID() {
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
	
	@Override
	public void setController(ControlsStackables c) {
		controller = c;
	}
	
	public ControlsStackables getController() {
		return controller;
	}

	@Override
	public CollectsMagicEnergy getMagicEnergyStock() {
		return magicEnergyStock;
	}

	@Override
	public void setGame(Game game) {
		this.game = game;
		for(IsAreaInGame zone : zones) {
			zone.setGame(game);
		}
	}

	@Override
	public Game getGame() {
		return game;
	}

}
