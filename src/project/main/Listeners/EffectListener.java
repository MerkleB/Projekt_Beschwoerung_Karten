package project.main.Listeners;

import project.main.Effect.Effect;

public interface EffectListener {
	public void effectActivated(Effect effect);
	public void effectExecuted(Effect effect);
}
