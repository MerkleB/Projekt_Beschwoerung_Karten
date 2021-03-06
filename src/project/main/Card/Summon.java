package project.main.Card;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import project.main.Action.GameAction;
import project.main.Action.Stackable;
import project.main.Effect.Effect;
import project.main.GameApplication.Application;
import project.main.GameApplication.Player;
import project.main.build_cards.CardTypes;
import project.main.build_cards.KnowsSummonAscentHierarchy;
import project.main.exception.NoCardException;
import project.main.exception.NotActivableException;
import project.main.util.ManagesTextLanguages;
import project.main.util.RankLevelMapper;
import project.main.util.TextProvider;

/**
 * Represents a Summon Card in Game
 * @author Benjamin Merkle
 *
 */
public class Summon implements Card{
	
	private String cardID;
	private String name;
	private String trivia;
	private Effect[] effects;
	private String rank;
	private KnowsSummonAscentHierarchy summonHierarchy;
	private SummonStatus status;
	private ActivityStatus activityStatus;
	private String id;
	private MagicCollector collector;
	private Player owner;
	private TreeMap<String, GameAction> actions;
	private ArrayList<Effect> enchantments;
	
	public Summon(String cardID, String name, String trivia, Effect[] effects, int preservationValue, int summoningPoints, int attack, int heal, int vitality, String summonClass, String rank, String element, int magicWastedOnDefeat, int energy, int collectorHealth, Player owner, GameAction[] actions) {
		this.cardID = cardID;
		this.name = name;
		this.collector = new MagicCollector(this, energy, collectorHealth);
		this.trivia = trivia;
		this.effects = effects;
		this.status = new SummonStatus(preservationValue, summoningPoints, attack, heal, vitality, 0, summonClass, element, magicWastedOnDefeat);
		this.rank = rank;
		this.activityStatus = new ActivityStatus();
		if(owner != null) {
			this.owner = owner;
		}
		id = UUID.randomUUID().toString();
		this.actions = new TreeMap<String, GameAction>();
		for(GameAction action : actions) {
			this.actions.put(action.getCode(), action);
			action.setCard(this);
		}
		if(effects != null) {
			for(Effect effect : effects) {
				effect.setCard(this);
			}
		}
	}

	@Override
	public void setOwningPlayer(Player owner) throws NoCardException {
		if(summonHierarchy == null) {
			this.owner = owner;
		}else {
			for(int i=0; i<3; i++) {
				Summon summon = summonHierarchy.getSummonOfLevel(i);
				summon.owner = owner;
			}
		}
	}
	
	public ActivityStatus getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus, int durability) {
		if(!activityStatus.equals(ActivityStatus.IMMOBILIZED) && !activityStatus.equals(ActivityStatus.USED) && !activityStatus.equals(ActivityStatus.READY)&& !activityStatus.equals(ActivityStatus.NOT_IN_GAME)) return;
		this.activityStatus.setStatus(activityStatus, durability);
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
		for(Effect effect : effects) {
			effect.setInactiv();
		}
	}
	
	@Override
	public void setInactive(ArrayList<Stackable> exceptionList) {
		actions.forEach((k,a) -> {
			if(!exceptionList.contains(a)) {
				a.setInactiv();
			}
		});
		for(Effect effect : effects) {
			if(!exceptionList.contains(effect)) {
				effect.setInactiv();
			}
		}
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
	public String getCardID() {
		return cardID;
	}
	
	@Override
	public void setTrivia(String trivia){
		this.trivia = trivia;
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
	public String toString() {
		ManagesTextLanguages text = TextProvider.getInstance();
		String language = Application.getInstance().getLanguage();
		String string = text.getTerm("Type", language).text+":"+text.getTerm(getType().toString(), language).text+";"
				+text.getTerm("Name", language).text+":"+text.getCardName(cardID, language)+" ("+getName()+");"
				+text.getTerm("SummonClass", language).text+":"+status.getSummonClass()+";"
				+text.getTerm("Rank", language).text+":"+getRank()+";"
				+text.getTerm("Element", language).text+":"+status.getElement()+";"
				+text.getTerm("SummoningPoints", language).text+":"+status.getSummoningPoints() + ";"
				+text.getTerm("PreservationValue", language).text+":"+status.getMagicPreservationValue()+";"
				+text.getTerm("Attack", language).text+":"+status.getAttack()+";"
				+text.getTerm("Heal", language).text+":"+status.getHeal()+";"
				+text.getTerm("Vitality", language).text+":"+status.getVitality()+";";
		for(int i=0; i<effects.length; i++) {
			string = string+text.getTerm("Effect", language).text+"-"+i+":"+effects[i].getDescription()+";";
		}
		string = string+text.getTerm("Trivia", language).text+":"+getTrivia();
		return string;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name){
		this.name = name;
	}
	
	public SummonStatus getStatus() {
		return status;
	}

	public void setEffects(Effect[] effects) {
		this.effects = effects;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank){
		this.rank = rank;
	}
	
	public int getLevel() {
		return RankLevelMapper.getInstance().mapRankToLevel(getRank());
	}
	
	public void setSummonHierarchy(KnowsSummonAscentHierarchy hierarchy) {
		summonHierarchy = hierarchy;
		Summon level0 = summonHierarchy.getSummonOfLevel(0);
		Summon level1 = summonHierarchy.getSummonOfLevel(1);
		Summon level2 = summonHierarchy.getSummonOfLevel(2);
		if(level0 != null) level0.id = this.id;
		if(level1 != null) level1.id = this.id;
		if(level2 != null) level2.id = this.id;
	}
	
	public KnowsSummonAscentHierarchy getSummonHierarchy() {
		return summonHierarchy;
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public void setID(String id) {
		this.id = id;
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
