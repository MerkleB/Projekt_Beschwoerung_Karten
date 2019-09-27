package main.Card;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import main.Action.Effect;
import main.Action.GameAction;
import main.Action.Stackable;
import main.GameApplication.Player;
import main.build_cards.CardTypes;
import main.exception.NoCardException;
import main.exception.NotActivableException;

public class Spell implements Card {

	private String cardID;
	private String trivia;
	private Effect[] effects;
	private String name;
	private int neededMagicEnergy;
	private UUID id;
	private MagicCollector collector;
	private Player owner;
	private TreeMap<String, GameAction> actions;
	
	public Spell(String cardID, String name, String trivia, Effect[] effects, int neededMagicEnergy, int energy, int collectorHealth, Player owner, GameAction[] actions) {
		this.cardID = cardID;
		this.name = name;
		this.collector = new MagicCollector(this, energy, collectorHealth);
		this.trivia = trivia;
		if(owner != null) {
			this.owner = owner;
		}
		id = UUID.randomUUID();
		this.effects = effects;
		this.neededMagicEnergy = neededMagicEnergy;
		this.actions = new TreeMap<String, GameAction>();
		for(GameAction action : actions) {
			this.actions.put(action.getName(), action);
			action.setCard(this);
		}
	}
	
	@Override
	public void setActiv(ArrayList<String> actions, Player activFor) {
		for(int i=0; i < actions.size(); i++) {
			String actionName = actions.get(i);
			if(this.actions.containsKey(actionName)) {
				this.actions.get(actionName).setActiv(activFor);
			}
		}
	}

	@Override
	public void setActivBy(ArrayList<String> actions, Player activFor, Stackable activator) {
		for(int i=0; i < actions.size(); i++) {
			String actionName = actions.get(i);
			if(this.actions.containsKey(actionName)) {
				this.actions.get(actions.get(i)).setActivBy(activator, activFor);
			}
		}
	}
	
	@Override
	public void setInactive() {
		actions.forEach((k,a) -> {
			a.setInactiv();
		});
	}
	
	@Override
	public void activateGameAction(String action, Player activatingPlayer) {
		try {
			this.actions.get(action).activate(activatingPlayer);
		} catch (NotActivableException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public void activateGameAction(String action, Player activatingPlayer, Stackable activator) {
		try {
			this.actions.get(action).activateBy(activator, activatingPlayer);
		} catch (NotActivableException e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Override
	public ArrayList<GameAction> getActions() {
		ArrayList<GameAction> actionList = new ArrayList<GameAction>();
		this.actions.forEach((key,value)->{
			actionList.add(value);
		});
		return actionList;
	}
	
	@Override
	public CardTypes getType() {
		return CardTypes.Spell;
	}

	@Override
	public void setTrivia(String trivia) {
		this.trivia = trivia;		
	}

	@Override
	public String getCardID() {
		return cardID;
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
		try {
			this.effects[effectNumber].activate(getOwningPlayer());
		} catch (NotActivableException e) {
			System.out.println(e.getMessage());
		}		
	}

	@Override
	public Player getOwningPlayer() {
		return owner;
	}

	@Override
	public MagicCollector getCollector() {
		return collector;
	}

	@Override
	public void setOwningPlayer(Player owner) throws NoCardException {
		this.owner = owner;
	}

	@Override
	public boolean equals(Object obj) {
		if((obj instanceof Spell) == false) {
			return false;
		}
		Spell anObject = (Spell) obj;
		
		if(!name.equals(anObject.name)) return false;
		if(!trivia.equals(anObject.trivia)) return false;
		if(!collector.equals(anObject.collector)) return false;
		if(neededMagicEnergy != anObject.neededMagicEnergy) return false;
		
		if(effects.length != anObject.effects.length) return false;
		for(int i=0; i<effects.length; i++) {
			if(!effects[i].equals(anObject.effects[i])) return false; 
		}
		
		if(actions.size() != anObject.actions.size()) return false;
		Set<String> keys = actions.keySet();
		for(String key : keys) {
			if(!actions.get(key).equals(anObject.actions.get(key))) return false;
		}
		
		if(owner != null) {
			if(!owner.equals(anObject.owner)) return false;
		}
		return true;
	}	

}
