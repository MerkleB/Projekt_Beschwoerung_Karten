package main.Listeners;

import main.GameApplication.GamePhase;
import main.GameApplication.IsPhaseInGame;

public interface PhaseListener {
	public void phaseStarted(IsPhaseInGame phase);
	public void phaseEnded(IsPhaseInGame phase);
}
