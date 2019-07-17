package main;

public abstract class MagicCollector {

	private int maxEnergy;
	private int freeEnergy;
	private int usedEnergy;
	private int depletedEnergy;
	private int maxHealth;
	private int currentHealth;
	private boolean isCollector;
	
	public boolean isCollector() {
		return isCollector;
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
	
	public int getUsedEnergy() {
		return usedEnergy;
	}
	
	public int useEnergy(int magicEnergy) {
		int remainingUsedEnergy = 0;
		if(freeEnergy > magicEnergy) {
			freeEnergy = freeEnergy - magicEnergy;
			usedEnergy = usedEnergy + magicEnergy;
		}else {
			remainingUsedEnergy = magicEnergy - freeEnergy;
			freeEnergy = 0;
		}
		return remainingUsedEnergy;
	}
	
	public int getDepletedEnergy() {
		return depletedEnergy;
	}
	
	public int depleteEnergyFromFree(int magicEnergy) {
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
	
	public int depleteEnergyFromUsed(int magicEnergy) {
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
	
	public int getMMaxHealth() {
		// TODO Auto-generated method stub
		return maxHealth;
	}

	
	public void setMaxHealth(int health) {
		maxHealth = health;
	}

	
	public int getCurrentHealth() {
		return currentHealth;
	}

	
	public void setCurrentHealth(int health) {
		currentHealth = health;
	}

	
	public int decreaseCurrentHealth(int damage) {
		if(damage < currentHealth) {
			currentHealth = currentHealth - damage;
		}else {
			currentHealth = 0;
		}
		return currentHealth;
	}
	
	public int increaseCurrentHealth(int heal) {
		if(heal > maxHealth) {
			currentHealth = maxHealth;
		}else {
			currentHealth = currentHealth + heal;
		}
		return currentHealth;
	}

	
	public boolean isCompletelyDepleted() {
		if(maxEnergy == depletedEnergy) {
			return true;
		}else return false;
	}
	
	protected void showCollector() {
		System.out.println("<<<Collector>>>");
		System.out.println("Free: "+getFreeEnergy()+" Used: "+getUsedEnergy()+" Depleted: "+getDepletedEnergy());
		System.out.println("Health: "+getCurrentHealth());
		System.out.println("<<<End-Collector>>>");
	}


}
