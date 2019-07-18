package main;

import java.awt.Graphics2D;

import main.exception.NoCardException;

public class Summon extends MagicCollector{	
	
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
	private int magicWastageOnDefeat;
	
	@Override
	public CardType getType() {
		return CardType.SUMMON;
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
	public void show() {
		if(isCollector()) {
			showCollector();
		}else {
			System.out.println("<<<"+getType().toString()+"-Card>>>");
			System.out.println("Name: "+getName());
			System.out.println("Class: "+getSummonClass()+" Rank: "+getRank());
			System.out.println("Element: "+getElement());
			System.out.println("SummingPoints: "+summoningPoints + " Preservation: "+magicPreservationValue);
			System.out.println("Attack: "+attack+" Heal: "+heal+" Vitality: "+vitality);
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

	public int getMagicPreservationValue() throws NoCardException {
		checkCard();
		return magicPreservationValue;
	}

	public void setMagicPreservationValue(int magicPreservationValue) throws NoCardException {
		checkCard();
		this.magicPreservationValue = magicPreservationValue;
	}

	public int getSummoningPoints() throws NoCardException {
		checkCard();
		return summoningPoints;
	}

	public void setSummoningPoints(int summoningPoints) throws NoCardException {
		checkCard();
		this.summoningPoints = summoningPoints;
	}

	public int getAttack() throws NoCardException {
		checkCard();
		return attack;
	}

	public void setAttack(int attack) throws NoCardException {
		checkCard();
		this.attack = attack;
	}

	public int getHeal() throws NoCardException {
		checkCard();
		return heal;
	}

	public void setHeal(int heal) throws NoCardException {
		checkCard();
		this.heal = heal;
	}

	public int getVitality() throws NoCardException {
		checkCard();
		return vitality;
	}

	public void setVitality(int vitality) throws NoCardException {
		checkCard();
		this.vitality = vitality;
	}
	
	public int decreaseVitality(int damage) throws NoCardException {
		checkCard();
		if(vitality > damage) {
			vitality = vitality - damage;
		}else {
			vitality = 0;
		}
		return vitality;
	}
	
	public int increaseVitality(int heal) throws NoCardException {
		checkCard();
		if(maxVitality > heal) {
			vitality = vitality + heal;
		}else {
			vitality = maxVitality;
		}
		return vitality;
	}

	public void setName(String name) throws NoCardException {
		checkCard();
		this.name = name;
	}

	public void setTrivia(String trivia) throws NoCardException {
		checkCard();
		this.trivia = trivia;
	}

	public void setEffects(Effect[] effects) throws NoCardException {
		checkCard();
		this.effects = effects;
	}

	public int getMaxVitality() throws NoCardException {
		checkCard();
		return maxVitality;
	}

	public void setMaxVitality(int maxVitality) throws NoCardException {
		checkCard();
		this.maxVitality = maxVitality;
	}

	public String getSummonClass() {
		return summonClass;
	}

	public void setSummonClass(String summonClass) throws NoCardException {
		checkCard();
		this.summonClass = summonClass;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) throws NoCardException{
		checkCard();
		this.rank = rank;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) throws NoCardException{
		checkCard();
		this.element = element;
	}

	public int getMagicWastageOnDefeat() {
		return magicWastageOnDefeat;
	}

	public void setMagicWastageOnDefeat(int magicWastageOnDefeat) throws NoCardException{
		checkCard();
		this.magicWastageOnDefeat = magicWastageOnDefeat;
	}
	
}