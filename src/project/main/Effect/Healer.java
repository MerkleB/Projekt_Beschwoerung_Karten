package project.main.Effect;

import project.main.GameApplication.Player;
import project.main.exception.NotActivableException;

public class Healer extends CardEffect {
	
	@Override
	public String getCode() {
		return "Healer";
	}

	@Override
	public void activate(Player activator) throws NotActivableException {
		throw new NotActivableException("PermanentEffect "+getCode()+"can't be activated");

	}

	@Override
	public boolean activateable(Player activator) {
		return false;
	}

	@Override
	public void execute() {
		//Permanent Effect - no execution
	}
}
