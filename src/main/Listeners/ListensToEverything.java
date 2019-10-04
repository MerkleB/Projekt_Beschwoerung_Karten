package main.Listeners;

public interface ListensToEverything extends EffectListener, GameActionListener, PhaseListener, ZoneListener, SummonListener, BattleListener{
	public void addGameActionListener(GameActionListener listener);
	public void addEffectListener(EffectListener listener);
	public void addPhaseListener(PhaseListener listener);
	public void addSummonListener(SummonListener listener);
	public void addBattleListener(BattleListener listener);
	public void addZoneListener(ZoneListener listener);
	public void removeGameActionListener(GameActionListener listener);
	public void removeEffectListener(EffectListener listener);
	public void removePhaseListener(PhaseListener listener);
	public void removeSummonListener(SummonListener listener);
	public void removeBattleListener(BattleListener listener);
	public void removeZoneListener(ZoneListener listener);
}
