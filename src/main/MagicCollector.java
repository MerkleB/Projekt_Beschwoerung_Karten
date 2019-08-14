package main;
import java.awt.Graphics2D;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import main.build_cards.CardTypes;
import main.exception.NoCardException;
import main.exception.NoCollectorException;

public class MagicCollector implements Card{

	private Card realCard;
	private int maxEnergy;
	private int freeEnergy;
	private int usedEnergy;
	private int depletedEnergy;
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
		this.maxHealth = collectorHealth;
		this.currentHealth = collectorHealth;
		this.isCollector = false;
	}
	
	public void setCollectorActions(GameAction[] actions) {
		if(this.actions == null) {
			this.actions = new TreeMap<String, GameAction>();
		}
		for(GameAction action : actions) {
			this.actions.put(action.getName(), action);
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

	public int getFreeEnergy() throws NoCollectorException {
		return freeEnergy;
	}
	
	public int restoreFreeEnergy() throws NoCollectorException {
		freeEnergy = freeEnergy + usedEnergy;
		usedEnergy = 0;
		return freeEnergy;
	}
	
	public int increaseFreeEnergy(int magicEnergy) throws NoCollectorException {
		int remainingEnergy = 0;
		if(freeEnergy + magicEnergy < maxEnergy) {
			freeEnergy = freeEnergy + magicEnergy;
		}else {
			remainingEnergy = (freeEnergy + magicEnergy) - maxEnergy;
			freeEnergy = maxEnergy;
		}
		return remainingEnergy;
	}
	
	public int decreaseFreeEnergy(int magicEnergy) throws NoCollectorException {
		int remainingEnergy = 0;
		if(freeEnergy - magicEnergy > 0) {
			freeEnergy = freeEnergy - magicEnergy;
		}else {
			remainingEnergy = magicEnergy - freeEnergy;
			freeEnergy = 0;
		}
		return remainingEnergy;
	}
	
	public int increaseFreeEnergyFromUsed(int magicEnergy) throws NoCollectorException {
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
	
	public int increaseFreeEnergyFromDepleted(int magicEnergy) throws NoCollectorException {
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
	
	public int getUsedEnergy() throws NoCollectorException {
		return usedEnergy;
	}
	
	public int useEnergy(int magicEnergy) throws NoCollectorException {
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
	
	public int getDepletedEnergy() throws NoCollectorException {
		return depletedEnergy;
	}
	
	public int depleteEnergyFromFree(int magicEnergy) throws NoCollectorException {
		int remainingDepletion = 0;
		if(freeEnergy > magicEnergy) {
			freeEnergy = freeEnergy - magicEnergy;
			depletedEnergy = depletedEnergy + magicEnergy;
		}else {
			remainingDepletion = magicEnergy - freeEnergy;
			depletedEnergy = maxEnergy - usedEnergy;
			freeEnergy = 0;			
		}
		return remainingDepletion;
	}
	
	public int depleteEnergyFromUsed(int magicEnergy) throws NoCollectorException {
		int remainingDepletion = 0;
		if(usedEnergy > magicEnergy) {
			usedEnergy = usedEnergy - magicEnergy;
			depletedEnergy = depletedEnergy + magicEnergy;
		}else {
			remainingDepletion = magicEnergy - usedEnergy;
			depletedEnergy = maxEnergy - freeEnergy;
			usedEnergy = 0;			
		}
		return remainingDepletion;
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
	
	protected void showCollector() {
		System.out.println("<<<Collector>>>");
		System.out.println("Free: "+freeEnergy+" Used: "+usedEnergy+" Depleted: "+depletedEnergy);
		System.out.println("Health: "+currentHealth);
		System.out.println("<<<End-Collector>>>");
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
	public UUID getID() {
		return realCard.getID();
	}

	@Override
	public void setID(UUID uuid) throws NoCardException {
		throw new NoCardException(realCard.getName()+" is currently used as collector!");	
	}

	@Override
	public void show() {
		showCollector();
	}

	@Override
	public void show(Graphics2D graphics) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActiv(String[] actions) {
		
	}

	@Override
	public void setInactive() {
		// TODO Auto-generated method stub
	}

	@Override
	public void activateGameAction(String action) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activateGameAction(String action, Stackable activator) {
		// TODO Auto-generated method stub
		
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
	
	


}
