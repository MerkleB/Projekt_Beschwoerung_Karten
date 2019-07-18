package main;

import java.awt.Graphics2D;

public class Spell extends MagicCollector implements Card {

	private String trivia;
	private Effect[] effects;
	private String name;
	private int neededMagicEnergy;
	
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
	public Effect[] getEffects() {
		return effects;
	}

	@Override
	public Effect getEffect(int index) {
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

	public int getNeededMagicEnergy() {
		return neededMagicEnergy;
	}

	public void setNeededMagicEnergy(int neededMagicEnergy) {
		this.neededMagicEnergy = neededMagicEnergy;
	}

	@Override
	public void show() {
		if(isCollector()) {
			showCollector();
		}else {
			System.out.println("<<<"+getType().toString()+"-Card>>>");
			System.out.println("Name: "+getName());
			System.out.println("MagicEnergy: "+getNeededMagicEnergy());
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

}
