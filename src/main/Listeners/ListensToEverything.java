package main.Listeners;

public interface ListensToEverything extends EffectListener, GameActionListener, PhaseListener, SummonListener{
	public void addGameActionListener(GameActionListener listener);
	public void addEffectListener(EffectListener listener);
	public void addPhaseListener(PhaseListener listener);
	public void addSummonListener(SummonListener listener);
}
