package main.GameApplication;

import java.util.ArrayList;

public interface IsPhaseInGame extends GameObject{
	public String getName();
	public void restorePhaseStatus();
	public ArrayList<String> getActionsToActivate();
	public void process();
	public void leave();
}
