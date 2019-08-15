package main;

import main.exception.NotActivableException;

public interface GameAction extends Stackable {
	public void activateBy(Stackable stackable, Player activator) throws NotActivableException;
}
