package main.Listeners;

public interface ListensToEverything extends EffectListener, GameActionListener {
	public void addGameActionListener(GameActionListener listener);
	public void addEffectListener(EffectListener listener);
}
