package main.Listeners;

public interface ListensToEverything extends EffectListener, GameActionListener, PhaseListener, SummonListener{
	public void addGameActionListener(GameActionListener listener);
	public void addEffectListener(EffectListener listener);
	public void addPhaseListener(PhaseListener listener);
	public void addSummonListener(SummonListener listener);
	public void removeGameActionListener(GameActionListener listener);
	public void removeEffectListener(EffectListener listener);
	public void removePhaseListener(PhaseListener listener);
	public void removeSummonListener(SummonListener listener);
}
