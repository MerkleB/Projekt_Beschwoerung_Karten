package main.Listeners;

import java.util.ArrayList;

import main.Action.Effect;
import main.Action.GameAction;
import main.Card.Card;
import main.Card.StatusChange;
import main.Card.Summon;
import main.GameApplication.IsPhaseInGame;

public class GameListener implements ListensToEverything {

	private static ListensToEverything instance;
	private ArrayList<EffectListener> effect_listeners;
	private ArrayList<GameActionListener> action_listeners;
	private ArrayList<PhaseListener> phase_listeners;
	private ArrayList<SummonListener> summon_listeners;
	
	public static ListensToEverything getInstance() {
		if(instance == null) {
			instance = new GameListener();
			((GameListener)instance).action_listeners = new ArrayList<GameActionListener>();
			((GameListener)instance).effect_listeners = new ArrayList<EffectListener>();
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
	public void removeGameActionListener(GameActionListener listener) {
		action_listeners.remove(listener);		
	}

	@Override
	public void removeEffectListener(EffectListener listener) {
		effect_listeners.remove(listener);		
	}

	@Override
	public void removePhaseListener(PhaseListener listener) {
		phase_listeners.remove(phase_listeners);
	}

	@Override
	public void removeSummonListener(SummonListener listener) {
		summon_listeners.remove(listener);		
	}

}
