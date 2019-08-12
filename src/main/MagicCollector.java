package main;
import java.util.TreeMap;

import main.exception.NoCardException;
import main.exception.NoCollectorException;

public class MagicCollector{

	private Card realCard;
	protected String name;
	private int maxEnergy;
	private int freeEnergy;
	private int usedEnergy;
	private int depletedEnergy;
	private int maxHealth;
	private int currentHealth;
	private boolean isCollector;
	
	protected MagicCollector(Card card, int energy, int collectorHealth) {
		this.maxEnergy = energy;
		this.freeEnergy = energy;
		this.realCard = card;
		this.depletedEnergy = 0;
		this.usedEnergy = 0;
		this.maxHealth = collectorHealth;
		this.currentHealth = collectorHealth;
		this.isCollector = false;
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


}
