package project.main.Listeners;

import project.main.Action.Effect;

public interface EffectListener {
	public void effectActivated(Effect effect);
	public void effectExecuted(Effect effect);
}
