package main;

import java.awt.Graphics2D;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import main.build_cards.CardTypes;
import main.build_cards.KnowsSummonAscentHierarchy;
import main.exception.NoCardException;
import main.util.RankLevelMapper;

public class Summon implements Card{	

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
	private KnowsSummonAscentHierarchy summonHierarchy;
	private String element;
	private int magicWastageOnDefeat;
	private UUID id;
	private MagicCollector collector;
	private Player owner;
	private TreeMap<String, GameAction> actions;
	
	public Summon(String name, String trivia, Effect[] effects, int preservationValue, int summoningPoints, int attack, int heal, int vitality, String summonClass, String rank, String element, int magicWastedOnDefeat, int energy, int collectorHealth, Player owner, GameAction[] actions) {
		this.name = name;
		this.collector = new MagicCollector(this, energy, collectorHealth);
		this.trivia = trivia;
		this.effects = effects;
		this.magicPreservationValue = preservationValue;
		this.summoningPoints = summoningPoints;
		this.attack = attack;
		this.heal = heal;
		this.maxVitality = vitality;
		this.vitality = vitality;
		this.summonClass = summonClass;
		this.rank = rank;
		this.element = element;
		this.magicWastageOnDefeat = magicWastedOnDefeat;
		if(owner != null) {
			this.owner = owner;
		}
		this.actions = new TreeMap<String, GameAction>();
		for(GameAction action : actions) {
			this.actions.put(action.getName(), action);
			action.setCard(this);
		}
	}

	@Override
	public void setOwningPlayer(Player owner) throws NoCardException {
		this.owner = owner;
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
	public Player getOwningPlayer() {
		return owner;
	}
	
	@Override
	public MagicCollector getCollector() {
		return collector;
	}
	
	@Override
	public CardTypes getType() {
		return CardTypes.Summon;
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
	public void show() {
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
	
	@Override
	public void show(Graphics2D graphics) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getName() {
		return name;
	}

	public int getMagicPreservationValue() throws NoCardException {
		return magicPreservationValue;
	}

	public void setMagicPreservationValue(int magicPreservationValue) throws NoCardException {
		this.magicPreservationValue = magicPreservationValue;
	}

	public int getSummoningPoints() throws NoCardException {
		return summoningPoints;
	}

	public void setSummoningPoints(int summoningPoints) throws NoCardException {
		this.summoningPoints = summoningPoints;
	}

	public int getAttack() throws NoCardException {
		return attack;
	}

	public void setAttack(int attack) throws NoCardException {
		this.attack = attack;
	}

	public int getHeal() throws NoCardException {
		return heal;
	}

	public void setHeal(int heal) throws NoCardException {
		this.heal = heal;
	}

	public int getVitality() throws NoCardException {
		return vitality;
	}

	public void setVitality(int vitality) throws NoCardException {
		this.vitality = vitality;
	}
	
	public int decreaseVitality(int damage) throws NoCardException {
		if(vitality > damage) {
			vitality = vitality - damage;
		}else {
			vitality = 0;
		}
		return vitality;
	}
	
	public int increaseVitality(int heal) throws NoCardException {
		if(maxVitality > vitality + heal) {
			vitality = vitality + heal;
		}else {
			vitality = maxVitality;
		}
		return vitality;
	}

	public void setName(String name) throws NoCardException {
		this.name = name;
	}

	public void setTrivia(String trivia) throws NoCardException {
		this.trivia = trivia;
	}

	public void setEffects(Effect[] effects) throws NoCardException {
		this.effects = effects;
	}

	public int getMaxVitality() throws NoCardException {
		return maxVitality;
	}

	public void setMaxVitality(int maxVitality) throws NoCardException {
		this.maxVitality = maxVitality;
	}

	public String getSummonClass() {
		return summonClass;
	}

	public void setSummonClass(String summonClass) throws NoCardException {
		this.summonClass = summonClass;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) throws NoCardException{
		this.rank = rank;
	}
	
	public int getLevel() {
		return RankLevelMapper.getInstance().mapRankToLevel(getRank());
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) throws NoCardException{
		this.element = element;
	}

	public int getMagicWastageOnDefeat() {
		return magicWastageOnDefeat;
	}

	public void setMagicWastageOnDefeat(int magicWastageOnDefeat) throws NoCardException{
		this.magicWastageOnDefeat = magicWastageOnDefeat;
	}
	
	public void setSummonHierarchy(KnowsSummonAscentHierarchy hierarchy) {
		summonHierarchy = hierarchy;
	}
	
	public KnowsSummonAscentHierarchy getSummonHierarchy() {
		return summonHierarchy;
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
	public boolean equals(Object obj) {
		if((obj instanceof Summon) == false) {
			return false;
		}
		Summon anObject = (Summon) obj;
		
		if(!name.equals(anObject.name)) return false;
		if(!trivia.equals(anObject.trivia)) return false;
		if(!summonClass.equals(anObject.summonClass)) return false;
		if(!rank.equals(anObject.rank)) return false;
		if(!element.equals(anObject.element)) return false;
		if(id != null) {
			if(!id.equals(anObject.id)) return false;
		}
		if(magicPreservationValue != anObject.magicPreservationValue) return false;
		if(!collector.equals(anObject.collector)) return false;
		if(summoningPoints != anObject.summoningPoints) return false;
		if(attack != anObject.attack) return false;
		if(heal != anObject.heal) return false;
		if(maxVitality != anObject.maxVitality) return false;
		if(vitality != anObject.vitality) return false;
		if(magicWastageOnDefeat != anObject.magicWastageOnDefeat) return false;
		
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
