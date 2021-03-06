package project.main.Card;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import project.main.Action.GameAction;
import project.main.Action.Stackable;
import project.main.Effect.Effect;
import project.main.GameApplication.Application;
import project.main.GameApplication.Player;
import project.main.build_cards.CardTypes;
import project.main.exception.NoCardException;
import project.main.exception.NoCollectorException;
import project.main.exception.NotActivableException;
import project.main.util.ManagesTextLanguages;
import project.main.util.TextProvider;

public class MagicCollector implements Card, CollectsMagicEnergy{

	private Card realCard;
	private int maxEnergy;
	private int freeEnergy;
	private int usedEnergy;
	private int depletedEnergy;
	private int blockedEnergy;
	private int maxHealth;
	private int currentHealth;
	private boolean isCollector;
	private TreeMap<String, GameAction> actions;
	
	public MagicCollector(Card card, int energy, int collectorHealth) {
		this.maxEnergy = energy;
		this.freeEnergy = energy;
		this.realCard = card;
		this.depletedEnergy = 0;
		this.usedEnergy = 0;
		this.blockedEnergy = 0;
		this.maxHealth = collectorHealth;
		this.currentHealth = collectorHealth;
		this.isCollector = false;
	}
	
	public void setCollectorActions(GameAction[] actions) {
		if(this.actions == null) {
			this.actions = new TreeMap<String, GameAction>();
		}
		for(GameAction action : actions) {
			action.setCard(this);
			this.actions.put(action.getCode(), action);
		}
	}
	
	public boolean isCollector() {
		return isCollector;
	}
	
	public Card getRealCard() {
		return realCard;
	}
	
	public void setIsCollector(boolean collector) {
		isCollector = collector;
	}
	
	public int getMaxEnergy() {
		return maxEnergy;
	}
	
	public void setMaxEnergy(int magicEnergy) {
		maxEnergy = magicEnergy;
	}

	public int getFreeEnergy() {
		return freeEnergy;
	}
	
	@Override
	public int restoreFreeEnergy() {
		freeEnergy = freeEnergy + usedEnergy;
		usedEnergy = 0;
		return freeEnergy;
	}
	
	@Override
	public int increaseFreeEnergy(int magicEnergy) {
		int remainingEnergy = 0;
		if(freeEnergy + magicEnergy < maxEnergy) {
			freeEnergy = freeEnergy + magicEnergy;
		}else {
			remainingEnergy = (freeEnergy + magicEnergy) - maxEnergy;
			freeEnergy = maxEnergy;
		}
		return remainingEnergy;
	}
	
	@Override
	public int decreaseFreeEnergy(int magicEnergy) {
		int remainingEnergy = 0;
		if(freeEnergy - magicEnergy > 0) {
			freeEnergy = freeEnergy - magicEnergy;
		}else {
			remainingEnergy = magicEnergy - freeEnergy;
			freeEnergy = 0;
		}
		return remainingEnergy;
	}
	
	@Override
	public int increaseFreeEnergyFromUsed(int magicEnergy) {
		int remainingEnergy = 0;
		if(usedEnergy > magicEnergy) {
			usedEnergy = usedEnergy - magicEnergy;
			remainingEnergy = increaseFreeEnergy(magicEnergy);
		}else {
			remainingEnergy = magicEnergy - usedEnergy;
			increaseFreeEnergy(usedEnergy);
			usedEnergy = 0;
		}
		return remainingEnergy;
	}
	
	@Override
	public int increaseFreeEnergyFromBlocked(int magicEnergy) {
		int remainingEnergy = 0;
		if(blockedEnergy > magicEnergy) {
			blockedEnergy = blockedEnergy - magicEnergy;
			remainingEnergy = increaseFreeEnergy(magicEnergy);
		}else {
			remainingEnergy = magicEnergy - blockedEnergy;
			increaseFreeEnergy(blockedEnergy);
			blockedEnergy = 0;
		}
		return remainingEnergy;
	}

	@Override
	public int increaseFreeEnergyFromDepleted(int magicEnergy) {
		int remainingEnergy = 0;
		if(depletedEnergy > magicEnergy) {
			depletedEnergy = depletedEnergy - magicEnergy;
			remainingEnergy = increaseFreeEnergy(magicEnergy);
		}else {
			remainingEnergy = magicEnergy - depletedEnergy;
			increaseFreeEnergy(depletedEnergy);
			depletedEnergy = 0;
		}
		return remainingEnergy;
	}
	
	@Override
	public int getUsedEnergy() {
		return usedEnergy;
	}
	
	@Override
	public int useEnergy(int magicEnergy) {
		int remainingUsedEnergy = 0;
		if(freeEnergy > magicEnergy) {
			freeEnergy = freeEnergy - magicEnergy;
			usedEnergy = usedEnergy + magicEnergy;
		}else {
			remainingUsedEnergy = magicEnergy - freeEnergy;
			usedEnergy = usedEnergy + magicEnergy - remainingUsedEnergy;
			freeEnergy = 0;
		}
		return remainingUsedEnergy;
	}
	
	@Override
	public int getDepletedEnergy() {
		return depletedEnergy;
	}
	
	@Override
	public int depleteEnergy(int magicEnergy) {
		int remainingEnergyToDeplete = depleteEnergyFromBlocked(magicEnergy);
		if(remainingEnergyToDeplete > 0) {
			remainingEnergyToDeplete = depleteEnergyFromFree(magicEnergy);
		}
		if(remainingEnergyToDeplete > 0) {
			remainingEnergyToDeplete = depleteEnergyFromUsed(magicEnergy);
		}
		return remainingEnergyToDeplete;
	}

	@Override
	public int depleteEnergyFromBlocked(int magicEnergy) {
		int remainingEnergyToDeplete = 0;
		if(blockedEnergy > magicEnergy) {
			blockedEnergy = blockedEnergy - magicEnergy;
			depletedEnergy = depletedEnergy + magicEnergy;
		}else {
			remainingEnergyToDeplete = magicEnergy - blockedEnergy;
			depletedEnergy = depletedEnergy + blockedEnergy;
			blockedEnergy = 0;
		}
		return remainingEnergyToDeplete;
	}

	@Override
	public int depleteEnergyFromFree(int magicEnergy) {
		int remainingDepletion = 0;
		if(freeEnergy > magicEnergy) {
			freeEnergy = freeEnergy - magicEnergy;
			depletedEnergy = depletedEnergy + magicEnergy;
		}else {
			remainingDepletion = magicEnergy - freeEnergy;
			depletedEnergy = depletedEnergy + freeEnergy;
			freeEnergy = 0;			
		}
		return remainingDepletion;
	}
	
	@Override
	public int depleteEnergyFromUsed(int magicEnergy) {
		int remainingDepletion = 0;
		if(usedEnergy > magicEnergy) {
			usedEnergy = usedEnergy - magicEnergy;
			depletedEnergy = depletedEnergy + magicEnergy;
		}else {
			remainingDepletion = magicEnergy - usedEnergy;
			depletedEnergy = depletedEnergy + usedEnergy;
			usedEnergy = 0;			
		}
		return remainingDepletion;
	}
	
	@Override
	public int getBlockedEnergy() {
		return blockedEnergy;
	}

	@Override
	public int blockEnergy(int magicEnergy) {
		int remainingBlockingEnergy = 0;
		if(freeEnergy > magicEnergy) {
			freeEnergy = freeEnergy - magicEnergy;
			blockedEnergy = blockedEnergy + magicEnergy;
		}else {
			remainingBlockingEnergy = magicEnergy - freeEnergy;
			blockedEnergy = blockedEnergy + freeEnergy;
			freeEnergy = 0;
		}
		return remainingBlockingEnergy;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	
	public void setMaxHealth(int health) {
		maxHealth = health;
	}

	
	public int getCurrentHealth() throws NoCollectorException {
		return currentHealth;
	}

	
	public void setCurrentHealth(int health) throws NoCollectorException {
		currentHealth = health;
	}

	
	public int decreaseCurrentHealth(int damage) throws NoCollectorException {
		if(damage < currentHealth) {
			currentHealth = currentHealth - damage;
		}else {
			currentHealth = 0;
		}
		return currentHealth;
	}
	
	public int increaseCurrentHealth(int heal) throws NoCollectorException {
		if(heal+currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}else {
			currentHealth = currentHealth + heal;
		}
		return currentHealth;
	}

	
	public boolean isCompletelyDepleted() throws NoCollectorException {
		if(maxEnergy == depletedEnergy) {
			return true;
		}else return false;
	}
	
	@Override
	public String toString() {
		ManagesTextLanguages text = TextProvider.getInstance();
		String language = Application.getInstance().getLanguage();
		String string =  text.getTerm("Type", language).text+": "+text.getTerm(getType().toString(), language).text+";"
				+text.getTerm("FreeEnergy", language).text+": "+freeEnergy+";"
				+text.getTerm("BlockedEnergy", language).text+": "+blockedEnergy+";"
				+text.getTerm("UsedEnergy", language).text+": "+usedEnergy+";"
				+text.getTerm("DepletedEnergy", language).text+":"+depletedEnergy+";"
				+text.getTerm("Health", language).text+":"+currentHealth;
		return string;
	}

	@Override
	public Player getOwningPlayer() {
		return realCard.getOwningPlayer();
	}

	@Override
	public MagicCollector getCollector() {
		return this;
	}

	@Override
	public CardTypes getType() {
		return realCard.getType();
	}

	@Override
	public String getTrivia() {
		return realCard.getTrivia();
	}

	@Override
	public Effect[] getEffects() throws NoCardException {
		throw new NoCardException(realCard.getName()+" is currently used as collector!");
	}

	@Override
	public Effect getEffect(int index) throws NoCardException {
		throw new NoCardException(realCard.getName()+" is currently used as collector!");
	}

	@Override
	public String getName() {
		return realCard.getName();
	}

	@Override
	public String getID() {
		return realCard.getID();
	}

	@Override
	public void setID(String id) throws NoCardException {
		throw new NoCardException(realCard.getName()+" is currently used as collector!");	
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
	public void setInactive(ArrayList<Stackable> exceptionList) {
		actions.forEach((k,a) -> {
			if(!exceptionList.contains(a)) {
				a.setInactiv();
			}
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
	public void activateEffect(int effectNumber) throws NoCardException {
		throw new NoCardException(realCard.getName()+" is currently used as collector!");
	}

	@Override
	public void setOwningPlayer(Player owner) throws NoCardException {
		throw new NoCardException(realCard.getName()+" is currently used as collector!");
	}

	@Override
	public boolean equals(Object obj) {
		if((obj instanceof MagicCollector) == false) {
			return false;
		}
		MagicCollector anObject = (MagicCollector) obj;
		if(maxEnergy != anObject.maxEnergy) return false;
		if(freeEnergy != anObject.freeEnergy) return false;
		if(usedEnergy != anObject.usedEnergy) return false;
		if(depletedEnergy != anObject.depletedEnergy) return false;
		if(maxHealth != anObject.maxHealth) return false;
		if(currentHealth != anObject.currentHealth) return false;
		
		if(actions != null) {
			if(actions.size() != anObject.actions.size()) return false;
			Set<String> keys = actions.keySet();
			for(String key : keys) {
				if(!actions.get(key).equals(anObject.actions.get(key))) return false;
			}
		}else if(anObject.actions != null) return false;
		
		
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

	@Override
	public String getCardID() {
		return realCard.getCardID();
	}

	@Override
	public void setTrivia(String trivia) {
		realCard.setTrivia(trivia);
	}

}
