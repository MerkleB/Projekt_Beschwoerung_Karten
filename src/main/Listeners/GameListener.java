package main.Listeners;

import java.util.ArrayList;

import main.Action.Effect;
import main.Action.GameAction;
import main.GameApplication.IsPhaseInGame;

public class GameListener implements ListensToEverything {

	private static ListensToEverything instance;
	private ArrayList<EffectListener> effect_listeners;
	private ArrayList<GameActionListener> action_listeners;
	private ArrayList<PhaseListener> phase_listeners;
	
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

	
	public void addGameActionListener(GameActionListener listener) {
		action_listeners.add(listener);
	}
	public void addEffectListener(EffectListener listener) {
		effect_listeners.add(listener);
	}
	
	@Override
	public void addPhaseListener(PhaseListener listener) {
		// TODO Auto-generated method stub
		
	}

}
