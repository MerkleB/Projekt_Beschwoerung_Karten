package main.Listeners;

import java.util.ArrayList;

import main.Action.Effect;
import main.Action.GameAction;
import main.Card.Card;
import main.Card.StatusChange;
import main.Card.Summon;
import main.GameApplication.Battle;
import main.GameApplication.IsAreaInGame;
import main.GameApplication.IsPhaseInGame;

public class GameListener implements ListensToEverything {

	private static ListensToEverything instance;
	private ArrayList<EffectListener> effect_listeners;
	private ArrayList<GameActionListener> action_listeners;
	private ArrayList<PhaseListener> phase_listeners;
	private ArrayList<SummonListener> summon_listeners;
	private ArrayList<BattleListener> battle_listeners;
	private ArrayList<ZoneListener> zone_listeners;
	
	public static ListensToEverything getInstance() {
		if(instance == null) {
			instance = new GameListener();
			((GameListener)instance).action_listeners = new ArrayList<GameActionListener>();
			((GameListener)instance).effect_listeners = new ArrayList<EffectListener>();
			((GameListener)instance).phase_listeners = new ArrayList<PhaseListener>();
			((GameListener)instance).summon_listeners = new ArrayList<SummonListener>();
			((GameListener)instance).battle_listeners = new ArrayList<BattleListener>();
			((GameListener)instance).zone_listeners = new ArrayList<ZoneListener>();
		}
		return instance;
	}
	
	@Override
	public void actionActivated(GameAction action) {
		for(GameActionListener listener : action_listeners) {
			listener.actionActivated(action);
		}
	}

	@Override
	public void actionExecuted(GameAction action) {
		for(GameActionListener listener : action_listeners) {
			listener.actionExecuted(action);
		}
	}

	@Override
	public void effectActivated(Effect effect) {
		for(EffectListener listener : effect_listeners) {
			listener.effectActivated(effect);
		}

	}

	@Override
	public void effectExecuted(Effect effect) {
		for(EffectListener listener : effect_listeners) {
			listener.effectExecuted(effect);
		}
	}
	
	@Override
	public void phaseStarted(IsPhaseInGame phase) {
		for(PhaseListener listener : phase_listeners) {
			listener.phaseStarted(phase);
		}
	}

	@Override
	public void phaseEnded(IsPhaseInGame phase) {
		for(PhaseListener listener : phase_listeners) {
			listener.phaseEnded(phase);
		}	
	}
	
	@Override
	public void summonDestroyed(Card destroyer, Summon summon) {
		for(SummonListener listener : summon_listeners) {
			listener.summonDestroyed(destroyer, summon);
		}
	}

	@Override
	public void summonEnchanted(Effect effect, Summon summon) {
		for(SummonListener listener : summon_listeners) {
			listener.summonEnchanted(effect, summon);
		}
	}

	@Override
	public void summonStatusChanged(StatusChange change, Summon summon) {
		for(SummonListener listener : summon_listeners) {
			listener.summonStatusChanged(change, summon);
		}
	}
	
	@Override
	public void battleStarted(Battle battle) {
		for(BattleListener listener : battle_listeners) {
			listener.battleStarted(battle);
		}
	}

	@Override
	public void battleEnded(Battle battle) {
		for(BattleListener listener : battle_listeners) {
			listener.battleEnded(battle);
		}	
	}

	@Override
	public void battleAbrupt(Battle battle) {
		for(BattleListener listener : battle_listeners) {
			listener.battleAbrupt(battle);
		}	
	}

	@Override
	public void attackHappened(BattleEventObject eventDetails) {
		for(BattleListener listener : battle_listeners) {
			listener.attackHappened(eventDetails);
		}
	}

	@Override
	public void cardAdded(IsAreaInGame zone, Card card) {
		for(ZoneListener listener : zone_listeners) {
			listener.cardAdded(zone, card);
		}
	}

	@Override
	public void cardRemoved(IsAreaInGame zone, Card card) {
		for(ZoneListener listener : zone_listeners) {
			listener.cardAdded(zone, card);
		}
	}

	public void addGameActionListener(GameActionListener listener) {
		action_listeners.add(listener);
	}
	public void addEffectListener(EffectListener listener) {
		effect_listeners.add(listener);
	}
	
	@Override
	public void addPhaseListener(PhaseListener listener) {
		phase_listeners.add(listener);
		
	}
	
	@Override
	public void addSummonListener(SummonListener listener) {
		summon_listeners.add(listener);		
	}

	@Override
	public void addZoneListener(ZoneListener listener) {
		zone_listeners.add(listener);		
	}

	@Override
	public void removeGameActionListener(GameActionListener listener) {
		action_listeners.remove(listener);		
	}

	@Override
	public void removeEffectListener(EffectListener listener) {
		effect_listeners.remove(listener);		
	}

	@Override
	public void removePhaseListener(PhaseListener listener) {
		phase_listeners.remove(listener);
	}

	@Override
	public void removeSummonListener(SummonListener listener) {
		summon_listeners.remove(listener);		
	}

	@Override
	public void addBattleListener(BattleListener listener) {
		battle_listeners.add(listener);
	}

	@Override
	public void removeBattleListener(BattleListener listener) {
		battle_listeners.remove(listener);
	}

	@Override
	public void removeZoneListener(ZoneListener listener) {
		zone_listeners.remove(listener);
	}

}
