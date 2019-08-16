package main.Listeners;

import main.Action.Effect;

public interface EffectListener {
	public void effectActivated(Effect effect);
	public void effectExecuted(Effect effect);
}
