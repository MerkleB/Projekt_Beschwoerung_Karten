package main;

import java.awt.Graphics2D;

public class Summon extends MagicCollector implements Card{	
	
	private String name;
	private String trivia;
	private Effect[] effects;
	private int magicPreservationValue;
	private int summoningPoints;
	private int attack;
	private int heal;
	private int vitality;
	private int maxVitality;
	private String summonClass;
	private String rank;
	private String element;
	
	@Override
	public CardType getType() {
		return CardType.SUMMON;
	}

	@Override
	public String getTrivia() {
		return trivia;
	}

	@Override
	public Effect[] getEffects() {
		// TODO Auto-generated method stub
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
	public void show() {
		if(isCollector()) {
			showCollector();
		}else {
			System.out.println("<<<"+getType().toString()+"-Card>>>");
			System.out.println("Name: "+getName());
			System.out.println("Class: "+getSummonClass()+" Rank: "+getRank());
			System.out.println("Element: "+getElement());
			System.out.println("SummingPoints: "+getSummoningPoints() + " Preservation: "+getMagicPreservationValue());
			System.out.println("Attack: "+getAttack()+" Heal: "+getHeal()+" Vitality: "+getVitality());
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
	public String getName() {
		return name;
	}

	public int getMagicPreservationValue() {
		return magicPreservationValue;
	}

	public void setMagicPreservationValue(int magicPreservationValue) {
		this.magicPreservationValue = magicPreservationValue;
	}

	public int getSummoningPoints() {
		return summoningPoints;
	}

	public void setSummoningPoints(int summoningPoints) {
		this.summoningPoints = summoningPoints;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getHeal() {
		return heal;
	}

	public void setHeal(int heal) {
		this.heal = heal;
	}

	public int getVitality() {
		return vitality;
	}

	public void setVitality(int vitality) {
		this.vitality = vitality;
	}
	
	public int decreaseVitality(int damage) {
		if(vitality > damage) {
			vitality = vitality - damage;
		}else {
			vitality = 0;
		}
		return vitality;
	}
	
	public int increaseVitality(int heal) {
		if(maxVitality > heal) {
			vitality = vitality + heal;
		}else {
			vitality = maxVitality;
		}
		return vitality;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTrivia(String trivia) {
		this.trivia = trivia;
	}

	public void setEffects(Effect[] effects) {
		this.effects = effects;
	}

	public int getMaxVitality() {
		return maxVitality;
	}

	public void setMaxVitality(int maxVitality) {
		this.maxVitality = maxVitality;
	}

	public String getSummonClass() {
		return summonClass;
	}

	public void setSummonClass(String summonClass) {
		this.summonClass = summonClass;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}
	
}
