package project.main.GameApplication;

import java.util.ArrayList;

import project.main.Listeners.GameListener;

public class SimplePhase extends GamePhase {

	public SimplePhase(String phaseName) {
		super(phaseName);
	}

	@Override
	public void restorePhaseStatus() {
		inactivateAll();
		Player player = game.getActivePlayer();
		ArrayList<IsAreaInGame> zones = player.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.activate(player, this);
		}
	}

	@Override
	public void leave() {
		finishedStacks.add(activeGameStack);
		activeGameStack = null;
		Player activPlayer = game.getActivePlayer();
		ArrayList<IsAreaInGame> zones = activPlayer.getGameZones();
		for(IsAreaInGame zone : zones) {
			zone.deavtivateAll();
		}
		GameListener.getInstance().phaseEnded(this);
	}

}
