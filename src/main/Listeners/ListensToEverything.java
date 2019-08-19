package main.Listeners;

public interface ListensToEverything extends EffectListener, GameActionListener, PhaseListener{
	public void addGameActionListener(GameActionListener listener);
	public void addEffectListener(EffectListener listener);
	public void addPhaseListener(PhaseListener listener);
}
