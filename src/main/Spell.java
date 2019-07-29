package main;

import java.awt.Graphics2D;
import java.util.UUID;

import main.exception.NoCardException;

public class Spell extends MagicCollector {

	private String trivia;
	private Effect[] effects;
	private String name;
	private int neededMagicEnergy;
	private UUID id;
	
	public Spell(String name, String trivia, Effect[] effects, int neededMagicEnergy, int energy, int collectorHealth) {
		super(name, energy, collectorHealth);
		this.trivia = trivia;
		this.neededMagicEnergy = neededMagicEnergy;
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
		checkCard();
		return effects;
	}

	@Override
	public Effect getEffect(int index) throws NoCardException {
		checkCard();
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
		checkCard();
		return neededMagicEnergy;
	}

	public void setNeededMagicEnergy(int neededMagicEnergy) throws NoCardException{
		checkCard();
		this.neededMagicEnergy = neededMagicEnergy;
	}

	@Override
	public void show() {
		if(isCollector()) {
			showCollector();
		}else {
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
	}

	@Override
	public void show(Graphics2D graphics) {
		// TODO Auto-generated method stub
	}

	@Override
	public UUID getID() {
		return id;
	}

}
