package main.Listeners;

import main.Effect;

public interface EffectListener {
	public void effectActivated(Effect effect);
	public void effectExecuted(Effect effect);
}
