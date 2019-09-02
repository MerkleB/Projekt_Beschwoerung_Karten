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
import main.build_cards.KnowsSummonAscentHierarchy;
import main.exception.NoCardException;
import main.exception.NotActivableException;
import main.util.RankLevelMapper;

/**
 * Represents a Summon Card in Game
 * @author Benjamin Merkle
 *
 */
public class Summon implements Card{	
	
	public static final String READY = "r";
	public static final String USED = "u";
	public static final String IMMOBILIZED = "i";
	
	private String name;
	private String trivia;
	private Effect[] effects;
	private String rank;
	private KnowsSummonAscentHierarchy summonHierarchy;
	private SummonStatus status;
	private String activityStatus;
	private UUID id;
	private MagicCollector collector;
	private Player owner;
	private TreeMap<String, GameAction> actions;
	private ArrayList<Effect> enchantments;
	
	public Summon(String name, String trivia, Effect[] effects, int preservationValue, int summoningPoints, int attack, int heal, int vitality, String summonClass, String rank, String element, int magicWastedOnDefeat, int energy, int collectorHealth, Player owner, GameAction[] actions) {
		this.name = name;
		this.collector = new MagicCollector(this, energy, collectorHealth);
		this.trivia = trivia;
		this.effects = effects;
		this.status = new SummonStatus(preservationValue, summoningPoints, attack, heal, vitality, 0, summonClass, element, magicWastedOnDefeat);
		this.rank = rank;
		this.setActivityStatus(READY);
		if(owner != null) {
			this.owner = owner;
		}
		id = UUID.randomUUID();
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
	
	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		if(!activityStatus.equals(IMMOBILIZED) && !activityStatus.equals(USED) && !activityStatus.equals(READY)) return;
		this.activityStatus = activityStatus;
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
			e.getMessage();
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
	
	public ArrayList<Effect> getEnchantments() {
		return enchantments;
	}

	@Override
	public void show() {
		System.out.println("<<<"+getType().toString()+"-Card>>>");
		System.out.println("Name: "+getName());
		System.out.println("Class: "+status.getSummonClass()+" Rank: "+getRank());
		System.out.println("Element: "+status.getElement());
		System.out.println("SummingPoints: "+status.getSummoningPoints() + " Preservation: "+status.getMagicPreservationValue());
		System.out.println("Attack: "+status.getAttack()+" Heal: "+status.getHeal()+" Vitality: "+status.getVitality());
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

	public void setName(String name) throws NoCardException {
		this.name = name;
	}

	public void setTrivia(String trivia) throws NoCardException {
		this.trivia = trivia;
	}
	
	public SummonStatus getStatus() {
		return status;
	}

	public void setEffects(Effect[] effects) throws NoCardException {
		this.effects = effects;
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
		try {
			this.effects[effectNumber].activate(owner);
		} catch (NotActivableException e) {
			System.out.println(e.getMessage());
		}		
	}

	@Override
	public boolean equals(Object obj) {
		if((obj instanceof Summon) == false) {
			return false;
		}
		Summon anObject = (Summon) obj;
		
		if(!name.equals(anObject.name)) return false;
		if(!trivia.equals(anObject.trivia)) return false;
		if(!status.getSummonClass().equals(anObject.getStatus().getSummonClass())) return false;
		if(!rank.equals(anObject.rank)) return false;
		if(!status.getElement().equals(anObject.getStatus().getElement())) return false;
		if(status.getMagicPreservationValue() != anObject.getStatus().getMagicPreservationValue()) return false;
		if(!collector.equals(anObject.collector)) return false;
		if(status.getSummoningPoints() != anObject.getStatus().getSummoningPoints()) return false;
		if(status.getAttack() != anObject.getStatus().getAttack()) return false;
		if(status.getHeal() != anObject.getStatus().getHeal()) return false;
		if(status.getMaxVitality() != anObject.getStatus().getMaxVitality()) return false;
		if(status.getVitality() != anObject.getStatus().getVitality()) return false;
		if(status.getMagicWastageOnDefeat() != anObject.getStatus().getMagicWastageOnDefeat()) return false;
		
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

	@Override
	public ArrayList<GameAction> getActions() {
		ArrayList<GameAction> actionList = new ArrayList<GameAction>();
		this.actions.forEach((key,value)->{
			actionList.add(value);
		});
		return actionList;
	}
	
}
