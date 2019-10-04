package main.Listeners;

import main.Card.Summon;

public class BattleEventObject {
	public static final String ATTACK = "attack"; 
	private String type;
	private Summon attacker;
	private Summon defender;
	private int damage;
	
	public BattleEventObject(String type, Summon attacker, Summon defender, int damage) {
		this.type = type;
		this.attacker = attacker;
		this.defender = defender;
		this.damage = damage;
	}

	public String getType() {
		return type;
	}

	public Summon getAttacker() {
		return attacker;
	}

	public Summon getDefender() {
		return defender;
	}

	public int getDamage() {
		return damage;
	}
}
