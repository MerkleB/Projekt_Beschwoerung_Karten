package project.main.Card;

/**
 * Interface to manage the collected magic energy
 * There are four types of energy
 * Free energy: Energy which can be freely used.
 * Used energy: Energy which was used to cast spells or activate effects; Will be refreshed during next own refreshment phase
 * Blocked energy: Energy which is used to preserve a summon: Will be restored if the summon leaves the field
 * Depleted energy: Energy which was depleted because of a summon which fainted
 * @author D054525
 *
 */
public interface CollectsMagicEnergy {
	/**
	 * Retrieves the amount of free energy
	 * @return int
	 */
	public int getFreeEnergy();
	/**
	 * Retrieves the amount of used energy
	 * @return int
	 */
	public int getUsedEnergy();
	/**
	 * Retrieves the amount of depleted energy
	 * @return int
	 */
	public int getDepletedEnergy();
	/**
	 * Retrieves the amount of blocked energy
	 * @return int
	 */
	public int getBlockedEnergy();
	/**
	 * Restores the energy up to the possible borders
	 * E.g. until all of the used energy is converted to free energy
	 * @return int (amount of free energy)
	 */
    public int restoreFreeEnergy();
    /**
     * Increases the free energy
     * @param int magicEnergy: amount of energy by which the free energy will be increased
     * @return int: difference between magicEnergy and the amount of energy which really could be restored
     */
    public int increaseFreeEnergy(int magicEnergy);
    /**
     * Decreases the free energy by a given value
     * @param int magicEnergy: The amount of energy by which the free energy should be decreased
     * @return int: difference between magicEnergy and the amount of energy which really could be decreased
     */
    public int decreaseFreeEnergy(int magicEnergy);
    /**
     * Increases the free energy from used energy
     * @param int magicEnergy: Value by which the free energy should be increased from used energy
     * @return int: difference between magicEnergy and the amount of energy which really could be increased from used energy
     */
    public int increaseFreeEnergyFromUsed(int magicEnergy);
    /**
     * Increases the free energy from blocked energy
     * @param int magicEnergy: Value by which the free energy should be increased from blocked energy
     * @return int: difference between magicEnergy and the amount of energy which really could be increased from blocked energy
     */
    public int increaseFreeEnergyFromBlocked(int magicEnergy);
    /**
     * Increases the free energy from depleted energy
     * @param int magicEnergy: Value by which the free energy should be increased from depleted energy
     * @return int: difference between magicEnergy and the amount of energy which really could be increased from depleted energy
     */
    public int increaseFreeEnergyFromDepleted(int magicEnergy);
    /**
     * Uses energy from free
     * @param int magicEnergy: Value of the energy which should be used
     * @return int: Amount of energy which could not be used because there is not enough free energy
     */
    public int useEnergy(int magicEnergy);
    /**
     * Deplete energy from all kinds of energy:
     * First blocked energy gets depleted
     * Second free energy gets depleted
     * Third Used energy gets depleted
     * @param int magicEnergy: Value of the energy which should be depleted
     * @return int: Amount of energy which could not be used because there is not enough energy to deplete
     */
    public int depleteEnergy(int magicEnergy);
    /**
     * Deplete energy from free
     * @param int magicEnergy: Value of the energy which should be blocked
     * @return int: Amount of energy which could not be used because there is not enough blocked energy
     */
    public int depleteEnergyFromBlocked(int magicEnergy);
    /**
     * Deplete energy from free
     * @param int magicEnergy: Value of the energy which should be depleted
     * @return int: Amount of energy which could not be used because there is not enough free energy
     */
    public int depleteEnergyFromFree(int magicEnergy);
    /**
     * Deplete energy from used
     * @param int magicEnergy: Value of the energy which should be depleted
     * @return int: Amount of energy which could not be used because there is not enough used energy
     */
    public int depleteEnergyFromUsed(int magicEnergy);
    /**
     * Blocks energy (only from free)
     * @param int magicEnergy: Value of the energy which should be depleted
     * @return int: Amount of energy which could not be used because there is not enough free energy
     */
    public int blockEnergy(int magicEnergy);
}