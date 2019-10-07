package project.main.Card;

public interface CollectsMagicEnergy {
	public int getFreeEnergy();
	public int getUsedEnergy();
	public int getDepletedEnergy();
    public int restoreFreeEnergy();
    public int increaseFreeEnergy(int magicEnergy);
    public int decreaseFreeEnergy(int magicEnergy);
    public int increaseFreeEnergyFromUsed(int magicEnergy);
    public int increaseFreeEnergyFromDepleted(int magicEnergy);
    public int useEnergy(int magicEnergy);
    public int depleteEnergyFromFree(int magicEnergy);
    public int depleteEnergyFromUsed(int magicEnergy);
}