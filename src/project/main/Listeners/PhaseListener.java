package project.main.Listeners;

import project.main.GameApplication.IsPhaseInGame;

public interface PhaseListener {
	public void phaseStarted(IsPhaseInGame phase);
	public void phaseEnded(IsPhaseInGame phase);
}
