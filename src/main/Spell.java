package main;

import java.awt.Graphics2D;
import java.util.TreeMap;
import java.util.UUID;

import main.exception.NoCardException;

public class Spell implements Card {

	private String trivia;
	private Effect[] effects;
	private String name;
	private int neededMagicEnergy;
	private UUID id;
	private MagicCollector collector;
	private Player owner;
	private TreeMap<String, GameAction> actions;
	
	public Spell(String name, String trivia, Effect[] effects, int neededMagicEnergy, int energy, int collectorHealth, Player owner, GameAction[] actions) {
		this.collector = new MagicCollector(this, energy, collectorHealth);
		this.trivia = trivia;
		this.owner = owner;
		this.neededMagicEnergy = neededMagicEnergy;
		this.actions = new TreeMap<String, GameAction>();
		for(GameAction action : actions) {
			this.actions.put(action.getName(), action);
			action.setCard(this);
		}
	}
	
	@Override
	public void setActiv(String[] actions) {
		for(int i=0; i<= actions.length; i++) {
			this.actions.get(actions[i]).setActiv();
		}
	}
	
	@Override
	public void setInactive() {
		actions.forEach((k,a) -> {
			a.setInactiv();
		});
	}
	
	@Override
	public void activateGameAction(String action) {
		this.actions.get(action).activate();
	}
	
	@Override
	public void activateGameAction(String action, Stackable activator) {
		this.actions.get(action).activateBy(activator);
	}
	
	@Override
	public CardType getType() {
		return CardType.SPELL;
	}

	@Override
	public String getTrivia() {
		if(trivia == null) {
			trivia = "";
		}
		return trivia;
	}

	@Override
	public Effect[] getEffects() throws NoCardException {
		return effects;
	}

	@Override
	public Effect getEffect(int index) throws NoCardException {
		Effect effect = null;
		if(index < effects.length && index > -1) {
			effect = effects[index];
		}
		return effect;
	}

	@Override
	public String getName() {
		return name;
	}

	public int getNeededMagicEnergy() throws NoCardException {
		return neededMagicEnergy;
	}

	public void setNeededMagicEnergy(int neededMagicEnergy) throws NoCardException{
		this.neededMagicEnergy = neededMagicEnergy;
	}

	@Override
	public void show() {
		System.out.println("<<<"+getType().toString()+"-Card>>>");
		System.out.println("Name: "+getName());
		try {
			System.out.println("MagicEnergy: "+getNeededMagicEnergy());
		} catch (NoCardException e) {
			System.out.println(e.getMessage());
		}
		for(int i=0; i<effects.length; i++) {
			System.out.println("Effect"+i+": "+effects[i].getDescription());
		}
		System.out.println("<<<Trivia>>>");
		System.out.println(getTrivia());
		System.out.println("<<<End-Card>>>");
	}

	@Override
	public void show(Graphics2D graphics) {
		// TODO Auto-generated method stub
	}

	@Override
	public UUID getID() {
		return id;
	}

	@Override
	public void setID(UUID uuid) {
		id = uuid;
	}

	@Override
	public void activateEffect(int effectNumber) {
		this.effects[effectNumber].activate();		
	}

	@Override
	public Player getOwningPlayer() {
		return owner;
	}

	@Override
	public MagicCollector getCollector() {
		return collector;
	}

}
